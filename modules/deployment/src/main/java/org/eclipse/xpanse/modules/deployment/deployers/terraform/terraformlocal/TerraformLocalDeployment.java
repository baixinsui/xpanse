/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformlocal;

import static org.eclipse.xpanse.modules.async.TaskConfiguration.ASYNC_EXECUTOR_NAME;

import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.database.service.DeployServiceEntity;
import org.eclipse.xpanse.modules.deployment.DeployServiceEntityHandler;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.exceptions.OpenTofuExecutorException;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.callbacks.TerraformDeploymentResultCallbackManager;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.TerraformBootDeployment;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformResult;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformlocal.config.TerraformLocalConfig;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.utils.TfResourceTransUtils;
import org.eclipse.xpanse.modules.deployment.utils.DeployEnvironments;
import org.eclipse.xpanse.modules.deployment.utils.DeployResultFileUtils;
import org.eclipse.xpanse.modules.deployment.utils.ScriptsGitRepoManage;
import org.eclipse.xpanse.modules.models.service.deploy.exceptions.ServiceNotDeployedException;
import org.eclipse.xpanse.modules.models.servicetemplate.Deployment;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.DeployerKind;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployResult;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployTask;
import org.eclipse.xpanse.modules.orchestrator.deployment.Deployer;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeploymentScriptValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Implementation of the deployment with terraform.
 */
@Slf4j
@Component
@ConditionalOnMissingBean(TerraformBootDeployment.class)
public class TerraformLocalDeployment implements Deployer {
    public static final String SCRIPT_FILE_NAME = "resources.tf";
    public static final String STATE_FILE_NAME = "terraform.tfstate";
    public static final String TF_DEBUG_FLAG = "TF_LOG";
    @Value("${clean.workspace.after.deployment.enabled:true}")
    private boolean cleanWorkspaceAfterDeploymentEnabled;
    @Resource
    private TerraformInstaller terraformInstaller;
    @Resource
    private DeployEnvironments deployEnvironments;
    @Resource
    private TerraformLocalConfig terraformLocalConfig;
    @Resource(name = ASYNC_EXECUTOR_NAME)
    private Executor taskExecutor;
    @Resource
    private TerraformDeploymentResultCallbackManager terraformDeploymentResultCallbackManager;
    @Resource
    private DeployServiceEntityHandler deployServiceEntityHandler;
    @Resource
    private ScriptsGitRepoManage scriptsGitRepoManage;
    @Resource
    private DeployResultFileUtils deployResultFileUtils;

    /**
     * Deploy the DeployTask.
     *
     * @param task the task for the deployment.
     */
    @Override
    public DeployResult deploy(DeployTask task) {
        DeployResult deployResult = new DeployResult();
        deployResult.setOrderId(task.getOrderId());
        asyncExecDeploy(task);
        return deployResult;
    }


    /**
     * Destroy the DeployTask.
     *
     * @param task the task for the deployment.
     */
    @Override
    public DeployResult destroy(DeployTask task) {
        DeployServiceEntity deployServiceEntity =
                deployServiceEntityHandler.getDeployServiceEntity(task.getServiceId());
        String resourceState = TfResourceTransUtils.getStoredStateContent(deployServiceEntity);
        if (StringUtils.isBlank(resourceState)) {
            String errorMsg = String.format("tfState of deployed service with id %s not found.",
                    task.getServiceId());
            log.error(errorMsg);
            throw new ServiceNotDeployedException(errorMsg);
        }

        DeployResult destroyResult = new DeployResult();
        destroyResult.setOrderId(task.getOrderId());
        asyncExecDestroy(task, resourceState);
        return destroyResult;
    }

    /**
     * Modify the DeployTask.
     *
     * @param task the task for the deployment.
     */
    @Override
    public DeployResult modify(DeployTask task) {
        DeployServiceEntity deployServiceEntity =
                deployServiceEntityHandler.getDeployServiceEntity(task.getServiceId());
        String resourceState = TfResourceTransUtils.getStoredStateContent(deployServiceEntity);
        if (StringUtils.isBlank(resourceState)) {
            String errorMsg = String.format("tfState of deployed service with id %s not found.",
                    task.getServiceId());
            log.error(errorMsg);
            throw new ServiceNotDeployedException(errorMsg);
        }
        DeployResult modifyResult = new DeployResult();
        modifyResult.setOrderId(task.getOrderId());
        asyncExecModify(task, resourceState);
        return modifyResult;
    }

    private void asyncExecDeploy(DeployTask task) {
        String workspace = createWorkspaceForTask(task.getOrderId());
        prepareDeploymentScripts(workspace, task.getOcl().getDeployment(), null);
        TerraformLocalExecutor executor = getExecutorForDeployTask(task, workspace, true);
        // Execute the terraform command asynchronously.
        taskExecutor.execute(() -> {
            TerraformResult terraformResult = new TerraformResult();
            terraformResult.setRequestId(task.getOrderId());
            try {
                executor.deploy();
                terraformResult.setCommandSuccessful(true);
            } catch (RuntimeException tfEx) {
                log.error("Execute Terraform deploy script failed. {}", tfEx.getMessage());
                terraformResult.setCommandSuccessful(false);
                terraformResult.setCommandStdError(tfEx.getMessage());
            }
            terraformResult.setTerraformState(executor.getTerraformState());
            terraformResult.setTerraformVersionUsed(
                    terraformInstaller.getExactVersionOfTerraform(executor.getExecutorPath()));
            Map<String, String> importantFileContentMap = executor.getImportantFilesContent();
            terraformResult.setImportantFileContentMap(importantFileContentMap);
            terraformDeploymentResultCallbackManager.orderCallback(task.getOrderId(),
                    terraformResult);
            if (cleanWorkspaceAfterDeploymentEnabled) {
                deleteWorkspace(workspace);
            }
        });
    }

    private void asyncExecDestroy(DeployTask task, String tfState) {
        String workspace = createWorkspaceForTask(task.getOrderId());
        prepareDeploymentScripts(workspace, task.getOcl().getDeployment(), tfState);
        TerraformLocalExecutor executor = getExecutorForDeployTask(task, workspace, false);
        // Execute the terraform command asynchronously.
        taskExecutor.execute(() -> {
            TerraformResult terraformResult = new TerraformResult();
            terraformResult.setRequestId(task.getOrderId());
            try {
                executor.destroy();
                terraformResult.setCommandSuccessful(true);
            } catch (RuntimeException tfEx) {
                log.error("Execute terraform destroy script failed. {}", tfEx.getMessage());
                terraformResult.setCommandSuccessful(false);
                terraformResult.setCommandStdError(tfEx.getMessage());
            }
            terraformResult.setTerraformState(executor.getTerraformState());
            terraformResult.setTerraformVersionUsed(
                    terraformInstaller.getExactVersionOfTerraform(executor.getExecutorPath()));
            Map<String, String> importantFileContentMap = executor.getImportantFilesContent();
            terraformResult.setImportantFileContentMap(importantFileContentMap);
            terraformDeploymentResultCallbackManager.orderCallback(task.getOrderId(),
                    terraformResult);
            if (cleanWorkspaceAfterDeploymentEnabled) {
                deleteWorkspace(workspace);
            }
        });
    }

    private void asyncExecModify(DeployTask task, String tfState) {
        String workspace = createWorkspaceForTask(task.getOrderId());
        prepareDeploymentScripts(workspace, task.getOcl().getDeployment(), tfState);
        TerraformLocalExecutor executor = getExecutorForDeployTask(task, workspace, true);
        // Execute the terraform command asynchronously.
        taskExecutor.execute(() -> {
            TerraformResult terraformResult = new TerraformResult();
            terraformResult.setRequestId(task.getOrderId());
            try {
                executor.deploy();
                terraformResult.setCommandSuccessful(true);
            } catch (RuntimeException tfEx) {
                log.error("Execute terraform modify script failed. {}", tfEx.getMessage());
                terraformResult.setCommandSuccessful(false);
                terraformResult.setCommandStdError(tfEx.getMessage());
            }
            terraformResult.setTerraformState(executor.getTerraformState());
            terraformResult.setTerraformVersionUsed(
                    terraformInstaller.getExactVersionOfTerraform(executor.getExecutorPath()));
            Map<String, String> importantFileContentMap = executor.getImportantFilesContent();
            terraformResult.setImportantFileContentMap(importantFileContentMap);
            terraformDeploymentResultCallbackManager.orderCallback(task.getOrderId(),
                    terraformResult);
            if (cleanWorkspaceAfterDeploymentEnabled) {
                deleteWorkspace(workspace);
            }
        });
    }

    @Override
    public String getDeploymentPlanAsJson(DeployTask task) {
        String workspace = createWorkspaceForTask(task.getOrderId());
        prepareDeploymentScripts(workspace, task.getOcl().getDeployment(), null);
        // Execute the terraform command.
        TerraformLocalExecutor executor = getExecutorForDeployTask(task, workspace, true);
        return executor.getTerraformPlanAsJson();
    }

    /**
     * delete workspace.
     */
    private void deleteWorkspace(String workspace) {
        Path path = Paths.get(workspace);
        try (Stream<Path> pathStream = Files.walk(path)) {
            pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (Exception e) {
            log.error("Delete workspace:{} error.", workspace, e);
        }
    }

    /**
     * Get a TerraformExecutor.
     *
     * @param task         the task for the deployment.
     * @param workspace    the workspace of the deployment.
     * @param isDeployTask if the task is for deploying a service.
     */
    private TerraformLocalExecutor getExecutorForDeployTask(DeployTask task, String workspace,
                                                            boolean isDeployTask) {
        Map<String, String> envVariables = this.deployEnvironments.getEnvironmentVariables(task);
        Map<String, Object> inputVariables = this.deployEnvironments.getInputVariables(
                task, isDeployTask);
        return getExecutor(envVariables, inputVariables, workspace, task.getOcl().getDeployment());
    }

    private TerraformLocalExecutor getExecutor(Map<String, String> envVariables,
                                               Map<String, Object> inputVariables, String workspace,
                                               Deployment deployment) {
        if (terraformLocalConfig.isDebugEnabled()) {
            log.info("Debug enabled for Terraform CLI with level {}",
                    terraformLocalConfig.getDebugLogLevel());
            envVariables.put(TF_DEBUG_FLAG, terraformLocalConfig.getDebugLogLevel());
        }
        String executorPath = terraformInstaller.getExecutorPathThatMatchesRequiredVersion(
                deployment.getDeployerTool().getVersion());
        return new TerraformLocalExecutor(executorPath, envVariables, inputVariables, workspace,
                getSubDirectory(deployment), deployResultFileUtils);
    }

    private String createWorkspaceForTask(UUID orderId) {
        String workspace = terraformLocalConfig.getWorkspaceDirectory() + File.separator + orderId;
        File ws = new File(System.getProperty("java.io.tmpdir"), workspace);
        if (!ws.exists() && !ws.mkdirs()) {
            throw new OpenTofuExecutorException(
                    "Create workspace failed, File path not created: " + ws.getAbsolutePath());
        }
        return ws.getAbsolutePath();
    }

    private void prepareDeploymentScripts(String workspace, Deployment deployment, String tfState) {
        if (Objects.nonNull(deployment.getDeployer())) {
            createScriptFile(workspace, deployment.getDeployer());
            if (StringUtils.isNotBlank(tfState)) {
                createServiceStateFile(workspace, tfState);
            }
        } else if (Objects.nonNull(deployment.getScriptsRepo())) {
            scriptsGitRepoManage.checkoutScripts(workspace, deployment.getScriptsRepo());
            String scriptPath =
                    workspace + File.separator + deployment.getScriptsRepo().getScriptsPath();
            if (StringUtils.isNotBlank(tfState)) {
                createServiceStateFile(scriptPath, tfState);
            }
        }
    }

    private void createScriptFile(String path, String scriptContext) {
        String scriptPath = path + File.separator + SCRIPT_FILE_NAME;
        try (FileWriter scriptWriter = new FileWriter(scriptPath)) {
            scriptWriter.write(scriptContext);
            log.info("Create script file success.");
        } catch (IOException ex) {
            log.error("Create script file success.", ex);
            throw new OpenTofuExecutorException("Create script file success.", ex);
        }
    }

    private void createServiceStateFile(String path, String tfStateContext) {
        String tfStateFileName = path + File.separator + STATE_FILE_NAME;
        try (FileWriter scriptWriter = new FileWriter(tfStateFileName)) {
            scriptWriter.write(tfStateContext);
            log.info("Create service state file success.");
        } catch (IOException e) {
            log.error("Create service state file failed.", e);
            throw new OpenTofuExecutorException("Create service state file failed.", e);
        }
    }


    /**
     * Get the deployer kind.
     */
    @Override
    public DeployerKind getDeployerKind() {
        return DeployerKind.TERRAFORM;
    }

    /**
     * Validates the Terraform script.
     */
    @Override
    public DeploymentScriptValidationResult validate(Deployment deployment) {
        String workspace = createWorkspaceForTask(UUID.randomUUID());
        prepareDeploymentScripts(workspace, deployment, null);
        TerraformLocalExecutor executor =
                getExecutor(new HashMap<>(), new HashMap<>(), workspace, deployment);
        DeploymentScriptValidationResult validationResult = executor.tfValidate();
        validationResult.setDeployerVersionUsed(
                terraformInstaller.getExactVersionOfTerraform(executor.getExecutorPath()));
        return validationResult;
    }

    @Nullable
    private String getSubDirectory(Deployment deployment) {
        if (Objects.nonNull(deployment.getDeployer())) {
            return null;
        } else if (Objects.nonNull(deployment.getScriptsRepo())) {
            return deployment.getScriptsRepo().getScriptsPath();
        }
        return null;
    }
}
