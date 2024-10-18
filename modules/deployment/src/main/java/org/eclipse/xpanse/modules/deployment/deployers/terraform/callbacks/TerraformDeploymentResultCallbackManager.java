/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.deployment.deployers.terraform.callbacks;

import jakarta.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.database.service.DeployServiceEntity;
import org.eclipse.xpanse.modules.database.servicemigration.ServiceMigrationEntity;
import org.eclipse.xpanse.modules.database.serviceorder.ServiceOrderEntity;
import org.eclipse.xpanse.modules.database.serviceorder.ServiceOrderStorage;
import org.eclipse.xpanse.modules.database.servicerecreate.ServiceRecreateEntity;
import org.eclipse.xpanse.modules.database.servicetemplate.ServiceTemplateEntity;
import org.eclipse.xpanse.modules.database.servicetemplate.ServiceTemplateStorage;
import org.eclipse.xpanse.modules.deployment.DeployResultManager;
import org.eclipse.xpanse.modules.deployment.DeployService;
import org.eclipse.xpanse.modules.deployment.DeployServiceEntityConverter;
import org.eclipse.xpanse.modules.deployment.ResourceHandlerManager;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformResult;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.utils.TfResourceTransUtils;
import org.eclipse.xpanse.modules.deployment.migration.MigrationService;
import org.eclipse.xpanse.modules.deployment.migration.consts.MigrateConstants;
import org.eclipse.xpanse.modules.deployment.recreate.RecreateService;
import org.eclipse.xpanse.modules.deployment.recreate.consts.RecreateConstants;
import org.eclipse.xpanse.modules.models.service.order.enums.ServiceOrderType;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.DeployerKind;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployResult;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployTask;
import org.eclipse.xpanse.modules.workflow.utils.WorkflowUtils;
import org.springframework.stereotype.Component;

/**
 * Bean for managing deployer terraform callback functions.
 */
@Slf4j
@Component
public class TerraformDeploymentResultCallbackManager {
    @Resource
    private ResourceHandlerManager resourceHandlerManager;
    @Resource
    private DeployResultManager deployResultManager;
    @Resource
    private DeployServiceEntityConverter deployServiceEntityConverter;
    @Resource
    private DeployService deployService;
    @Resource
    private MigrationService migrationService;
    @Resource
    private RecreateService recreateService;
    @Resource
    private WorkflowUtils workflowUtils;
    @Resource
    private ServiceTemplateStorage serviceTemplateStorage;
    @Resource
    private ServiceOrderStorage serviceOrderStorage;

    /**
     * Handle the callback of the order task.
     *
     * @param orderId the orderId of the task.
     * @param result  execution result of the task.
     */
    public void orderCallback(UUID orderId, TerraformResult result) {
        log.info("Receive the callback result: {} of the order task: {}", result, orderId);
        ServiceOrderEntity serviceOrderEntity = serviceOrderStorage.getEntityById(orderId);
        DeployServiceEntity storedServiceEntity = serviceOrderEntity.getDeployServiceEntity();
        DeployResult deployResult = getDeployResult(result);
        deployResult.setOrderId(orderId);
        deployResult.setTaskType(serviceOrderEntity.getTaskType());
        deployResult.setServiceId(storedServiceEntity.getId());
        // update deployServiceEntity
        DeployServiceEntity updatedServiceEntity =
                updateDeployServiceEntity(deployResult, storedServiceEntity);
        // when failed task type is deploy or retry, start a new order ro rollback.
        if (!deployResult.getIsTaskSuccessful()
                && (serviceOrderEntity.getTaskType() == ServiceOrderType.DEPLOY
                || serviceOrderEntity.getTaskType() == ServiceOrderType.RETRY)) {
            DeployTask rollbackTask = deployServiceEntityConverter.getDeployTaskByStoredService(
                    updatedServiceEntity);
            rollbackTask.setParentOrderId(orderId);
            deployService.rollbackOnDeploymentFailure(rollbackTask, updatedServiceEntity);
        }
        // TODO refactor logic of service migration.
        UUID serviceId = updatedServiceEntity.getId();
        ServiceMigrationEntity serviceMigrationEntity =
                migrationService.getServiceMigrationEntityByNewServiceId(serviceId);
        if (Objects.nonNull(serviceMigrationEntity)) {
            workflowUtils.completeReceiveTask(serviceMigrationEntity.getMigrationId().toString(),
                    MigrateConstants.MIGRATION_DEPLOY_RECEIVE_TASK_ACTIVITY_ID);
        }
        // TODO refactor logic of service recreate.
        ServiceRecreateEntity serviceRecreateEntity =
                recreateService.getServiceRecreateEntityByNewServiceId(serviceId);
        if (Objects.nonNull(serviceRecreateEntity)) {
            workflowUtils.completeReceiveTask(serviceRecreateEntity.getRecreateId().toString(),
                    RecreateConstants.RECREATE_DEPLOY_RECEIVE_TASK_ACTIVITY_ID);
        }
        // update serviceOrderEntity
        updateServiceOrderEntity(deployResult);
    }


    private DeployResult getDeployResult(TerraformResult result) {
        DeployResult deployResult = new DeployResult();
        deployResult.setDeployerVersionUsed(result.getTerraformVersionUsed());
        if (Boolean.FALSE.equals(result.getCommandSuccessful())) {
            deployResult.setIsTaskSuccessful(false);
            deployResult.setMessage(result.getCommandStdError());
        } else {
            deployResult.setIsTaskSuccessful(true);
            deployResult.setMessage(null);
        }
        deployResult.setTfStateContent(result.getTerraformState());
        deployResult.getPrivateProperties()
                .put(TfResourceTransUtils.STATE_FILE_NAME, result.getTerraformState());
        if (Objects.nonNull(result.getImportantFileContentMap())) {
            deployResult.getPrivateProperties().putAll(result.getImportantFileContentMap());
        }
        return deployResult;
    }

    private DeployServiceEntity updateDeployServiceEntity(DeployResult deployResult,
                                                          DeployServiceEntity deployServiceEntity) {
        if (StringUtils.isNotBlank(deployResult.getTfStateContent())) {
            ServiceTemplateEntity serviceTemplateEntity =
                    serviceTemplateStorage.getServiceTemplateById(
                            deployServiceEntity.getServiceTemplateId());
            DeployerKind deployerKind = serviceTemplateEntity.getOcl().getDeployment()
                    .getDeployerTool().getKind();
            resourceHandlerManager.getResourceHandler(deployServiceEntity.getCsp(), deployerKind)
                    .handler(deployResult);
        }
        return deployResultManager.updateDeployServiceEntityWithDeployResult(deployResult,
                deployServiceEntity);
    }

    private void updateServiceOrderEntity(DeployResult deployResult) {
        ServiceOrderEntity serviceOrderEntity =
                serviceOrderStorage.getEntityById(deployResult.getOrderId());
        deployResultManager.updateServiceOrderTaskWithDeployResult(deployResult,
                serviceOrderEntity);
    }
}
