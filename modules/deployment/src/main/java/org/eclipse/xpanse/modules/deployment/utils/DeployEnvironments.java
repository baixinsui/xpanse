/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.deployment.utils;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.DeployTask;
import org.eclipse.xpanse.modules.models.enums.DeployVariableKind;
import org.eclipse.xpanse.modules.models.resource.DeployVariable;
import org.eclipse.xpanse.modules.models.resource.Flavor;

/**
 * Environment variables utils for deployment.
 */
public class DeployEnvironments {

    private DeployEnvironments() {
    }

    /**
     * Get environment variables for deployment.
     *
     * @param task the context of the task.
     */
    public static Map<String, String> getEnv(DeployTask task) {
        Map<String, String> variables = new HashMap<>();
        Map<String, String> request = task.getCreateRequest().getProperty();
        for (DeployVariable variable : task.getOcl().getDeployment().getContext()) {
            if (variable.getKind() == DeployVariableKind.ENV) {
                if (request.containsKey(variable.getName())
                        && request.get(variable.getName()) != null) {
                    variables.put(variable.getName(), request.get(variable.getName()));
                } else {
                    variables.put(variable.getName(), System.getenv(variable.getName()));
                }
            }

            if (variable.getKind() == DeployVariableKind.ENV_ENV) {
                variables.put(variable.getName(), System.getenv(variable.getName()));
            }

            if (variable.getKind() == DeployVariableKind.FIX_ENV) {
                variables.put(variable.getName(), variable.getValue());
            }
        }

        return variables;
    }

    /**
     * Get flavor variables.
     *
     * @param task the DeployTask.
     */
    public static Map<String, String> getFlavorVariables(DeployTask task) {
        Map<String, String> variables = new HashMap<>();
        for (Flavor flavor : task.getOcl().getFlavors()) {
            if (flavor.getName().equals(task.getCreateRequest().getFlavor())) {
                for (Map.Entry<String, String> entry : flavor.getProperty().entrySet()) {
                    variables.put((entry.getKey()), entry.getValue());
                }
                return variables;
            }
        }

        throw new RuntimeException("Can not get an available flavor.");
    }

    /**
     * Get deployment variables.
     *
     * @param task the DeployTask.
     */
    public static Map<String, String> getVariables(DeployTask task) {
        Map<String, String> variables = new HashMap<>();
        Map<String, String> request = task.getCreateRequest().getProperty();
        for (DeployVariable variable : task.getOcl().getDeployment().getContext()) {
            if (variable.getKind() == DeployVariableKind.VARIABLE) {
                if (request.containsKey(variable.getName())
                        && request.get(variable.getName()) != null) {
                    variables.put(variable.getName(), request.get(variable.getName()));
                } else {
                    variables.put(variable.getName(), System.getenv(variable.getName()));
                }
            }

            if (variable.getKind() == DeployVariableKind.ENV_VARIABLE) {
                variables.put(variable.getName(), System.getenv(variable.getName()));
            }

            if (variable.getKind() == DeployVariableKind.FIX_VARIABLE
                    && request.containsKey(variable.getName())) {
                variables.put(variable.getName(), variable.getValue());
            }
        }
        return variables;
    }
}