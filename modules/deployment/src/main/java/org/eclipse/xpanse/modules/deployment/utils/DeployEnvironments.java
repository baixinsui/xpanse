/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.deployment.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.xpanse.modules.credential.CredentialCenter;
import org.eclipse.xpanse.modules.models.credential.AbstractCredentialInfo;
import org.eclipse.xpanse.modules.models.credential.CredentialVariable;
import org.eclipse.xpanse.modules.models.credential.CredentialVariables;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.eclipse.xpanse.modules.models.service.common.enums.Csp;
import org.eclipse.xpanse.modules.models.service.deploy.exceptions.FlavorInvalidException;
import org.eclipse.xpanse.modules.models.servicetemplate.DeployVariable;
import org.eclipse.xpanse.modules.models.servicetemplate.Flavor;
import org.eclipse.xpanse.modules.models.servicetemplate.Ocl;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.DeployVariableKind;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.SensitiveScope;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.ServiceHostingType;
import org.eclipse.xpanse.modules.orchestrator.PluginManager;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployTask;
import org.eclipse.xpanse.modules.security.common.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Environment variables utils for deployment.
 */
@Component
public class DeployEnvironments {

    private final AesUtil aesUtil;

    private final CredentialCenter credentialCenter;

    private final PluginManager pluginManager;

    private final Environment environment;

    /**
     * Constructor to initialize DeployEnvironments bean.
     *
     * @param credentialCenter CredentialCenter bean
     * @param aesUtil          AesUtil bean
     * @param pluginManager    PluginManager bean
     * @param environment      Environment bean
     */

    @Autowired
    public DeployEnvironments(CredentialCenter credentialCenter, AesUtil aesUtil,
                              PluginManager pluginManager, Environment environment) {
        this.credentialCenter = credentialCenter;
        this.aesUtil = aesUtil;
        this.pluginManager = pluginManager;
        this.environment = environment;
    }

    /**
     * Get environment variables for deployment.
     *
     * @param task the context of the task.
     */
    public Map<String, String> getEnvFromDeployTask(DeployTask task) {
        return getEnv(
                task.getDeployRequest().getServiceRequestProperties(),
                task.getOcl().getDeployment().getVariables());
    }

    /**
     * Build environment variables for serviceRequestProperties and deployVariables.
     *
     * @param serviceRequestProperties variables passed by end user during ordering.
     * @param deployVariables deploy variables defined in the service template.
     */
    private Map<String, String> getEnv(Map<String, Object> serviceRequestProperties,
                                      List<DeployVariable> deployVariables) {
        Map<String, String> variables = new HashMap<>();
        for (DeployVariable variable : deployVariables) {
            if (variable.getKind() == DeployVariableKind.ENV) {
                if (serviceRequestProperties.containsKey(variable.getName())
                        && serviceRequestProperties.get(variable.getName()) != null) {
                    variables.put(variable.getName(),
                            !SensitiveScope.NONE.toValue()
                                    .equals(variable.getSensitiveScope().toValue())
                                    ? aesUtil.decode(
                                            serviceRequestProperties.get(
                                                    variable.getName()).toString())
                                    : serviceRequestProperties.get(variable.getName()).toString());
                } else {
                    variables.put(variable.getName(), System.getenv(variable.getName()));
                }
            }

            if (variable.getKind() == DeployVariableKind.ENV_ENV) {
                variables.put(variable.getName(), System.getenv(variable.getName()));
            }

            if (variable.getKind() == DeployVariableKind.FIX_ENV) {
                variables.put(variable.getName(),
                        !SensitiveScope.NONE.toValue()
                                .equals(variable.getSensitiveScope().toValue())
                                ? aesUtil.decode(variable.getValue()) : variable.getValue());
            }
        }

        return variables;
    }

    /**
     * Get flavor variables.
     *
     * @param task the DeployTask.
     */
    public Map<String, String> getFlavorVariables(DeployTask task) {
        return getFlavorVariables(task.getOcl(), task.getDeployRequest().getFlavor());
    }

    private Map<String, String> getFlavorVariables(Ocl ocl, String requestedFlavor) {
        for (Flavor flavor : ocl.getFlavors()) {
            if (flavor.getName().equals(requestedFlavor)) {
                return flavor.getProperties();
            }
        }
        throw new FlavorInvalidException("Can not get an available flavor.");
    }

    /**
     * Get deployment variables.
     *
     * @param task the DeployTask.
     */
    public Map<String, Object> getVariablesFromDeployTask(DeployTask task,
                                                          boolean isDeployRequest) {
        return getVariables(
                task.getDeployRequest().getServiceRequestProperties(),
                task.getOcl().getDeployment().getVariables(),
                isDeployRequest);
    }

    /**
     * Get deployment variables.
     *
     * @param serviceRequestProperties variables provided by the end user.
     * @param deployVariables          variables configured in the service template.
     * @param isDeployRequest          defines if the variables are required for deploying the
     *                                 service. False if it is for any other use cases.
     */
    private Map<String, Object> getVariables(Map<String, Object> serviceRequestProperties,
                                            List<DeployVariable> deployVariables,
                                            boolean isDeployRequest) {
        Map<String, Object> variables = new HashMap<>();
        for (DeployVariable variable : deployVariables) {
            if (variable.getKind() == DeployVariableKind.VARIABLE) {
                if (serviceRequestProperties.containsKey(variable.getName())
                        && serviceRequestProperties.get(variable.getName()) != null) {
                    variables.put(variable.getName(),
                            (variable.getSensitiveScope() != SensitiveScope.NONE
                                    && isDeployRequest)
                                    ? aesUtil.decodeBackToOriginalType(variable.getDataType(),
                                    serviceRequestProperties.get(variable.getName()).toString())
                                    : serviceRequestProperties.get(variable.getName()));
                } else {
                    variables.put(variable.getName(), System.getenv(variable.getName()));
                }
            }

            if (variable.getKind() == DeployVariableKind.ENV_VARIABLE) {
                variables.put(variable.getName(), System.getenv(variable.getName()));
            }

            if (variable.getKind() == DeployVariableKind.FIX_VARIABLE) {
                variables.put(variable.getName(),
                        (variable.getSensitiveScope() != SensitiveScope.NONE && isDeployRequest)
                                ? aesUtil.decode(variable.getValue()) : variable.getValue());
            }
        }

        return variables;
    }

    /**
     * Get credential variables By ServiceHostingType.
     *
     * @param serviceHostingType serviceHostingType of the service.
     * @param credentialType credentialType used by the service.
     * @param csp CSP of the service.
     * @param userId ID of the user ordering the service.
     */
    public Map<String, String> getCredentialVariablesByHostingType(
            ServiceHostingType serviceHostingType,
            CredentialType credentialType,
            Csp csp,
            String userId) {
        Map<String, String> variables = new HashMap<>();
        AbstractCredentialInfo abstractCredentialInfo =
                this.credentialCenter.getCredential(
                        csp,
                        credentialType,
                        serviceHostingType == ServiceHostingType.SELF ? userId : null);
        if (Objects.nonNull(abstractCredentialInfo)) {
            for (CredentialVariable variable
                    : ((CredentialVariables) abstractCredentialInfo).getVariables()) {
                variables.put(variable.getName(), variable.getValue());
            }
        }
        return variables;
    }

    /**
     * Get plugin's mandatory variables.
     *
     * @param csp CSP for which the mandatory variables defined in its plugins must be returned.
     */
    public Map<String, String> getPluginMandatoryVariables(Csp csp) {
        Map<String, String> variables = new HashMap<>();
        this.pluginManager.getOrchestratorPlugin(csp)
                .requiredProperties().forEach(variable -> variables.put(variable,
                        this.environment.getRequiredProperty(variable)));
        return variables;
    }

    /**
     * Get all variables that are considered for a service.
     *
     * @param serviceRequestProperties variables provided by the end user.
     * @param deployVariables          variables configured in the service template.
     * @param requestedFlavor Flavor of the service ordered.
     * @param ocl OCL of the requested service template.
     */
    public Map<String, Object> getAllDeploymentVariablesForService(
            Map<String, Object> serviceRequestProperties,
            List<DeployVariable> deployVariables,
            String requestedFlavor,
            Ocl ocl) {
        Map<String, Object> allVariables = new HashMap<>();
        allVariables.putAll(getVariables(serviceRequestProperties, deployVariables, false));
        allVariables.putAll(getEnv(serviceRequestProperties, deployVariables));
        allVariables.putAll(getFlavorVariables(ocl, requestedFlavor));
        return allVariables;
    }
}
