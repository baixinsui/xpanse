/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.deployment;

import jakarta.annotation.Resource;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.database.resource.DeployResourceEntity;
import org.eclipse.xpanse.modules.database.service.DeployServiceEntity;
import org.eclipse.xpanse.modules.database.service.DeployServiceStorage;
import org.eclipse.xpanse.modules.database.serviceconfiguration.ServiceConfigurationEntity;
import org.eclipse.xpanse.modules.database.serviceorder.ServiceOrderEntity;
import org.eclipse.xpanse.modules.database.serviceorder.ServiceOrderStorage;
import org.eclipse.xpanse.modules.models.service.deploy.DeployRequest;
import org.eclipse.xpanse.modules.models.service.deploy.DeployResource;
import org.eclipse.xpanse.modules.models.service.enums.ServiceDeploymentState;
import org.eclipse.xpanse.modules.models.service.enums.TaskStatus;
import org.eclipse.xpanse.modules.models.service.order.enums.ServiceOrderType;
import org.eclipse.xpanse.modules.models.service.statemanagement.enums.ServiceState;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Bean to handle deployment result.
 */
@Component
@Slf4j
public class DeployResultManager {

    @Resource
    private DeployServiceStorage deployServiceStorage;

    @Resource
    private ServiceOrderStorage serviceOrderStorage;

    @Resource
    private SensitiveDataHandler sensitiveDataHandler;

    @Resource
    private DeployServiceEntityConverter deployServiceEntityConverter;

    /**
     * Method to update deployment result to DeployServiceEntity and store to database.
     *
     * @param deployResult Deployment Result.
     * @param storedEntity DB entity to be updated
     * @return updated entity.
     * @throws RuntimeException exception thrown in case of errors.
     */
    public DeployServiceEntity updateDeployServiceEntityWithDeployResult(
            DeployResult deployResult, DeployServiceEntity storedEntity) {
        if (Objects.isNull(deployResult) || Objects.isNull(deployResult.getIsTaskSuccessful())) {
            return storedEntity;
        }
        if (Objects.isNull(storedEntity)) {
            storedEntity = deployServiceStorage.findDeployServiceById(deployResult.getServiceId());
        }
        log.info("Update deploy service entity {} with deploy result {}",
                deployResult.getServiceId(), deployResult);
        DeployServiceEntity deployServiceToUpdate = new DeployServiceEntity();
        BeanUtils.copyProperties(storedEntity, deployServiceToUpdate);
        updateServiceEntityWithDeployResult(deployResult, deployServiceToUpdate);
        return deployServiceStorage.storeAndFlush(deployServiceToUpdate);
    }

    private void updateServiceEntityWithDeployResult(DeployResult deployResult,
                                                     DeployServiceEntity deployServiceEntity) {
        ServiceOrderType taskType = deployResult.getTaskType();
        boolean isTaskSuccessful = deployResult.getIsTaskSuccessful();
        ServiceDeploymentState deploymentState =
                getServiceDeploymentState(taskType, isTaskSuccessful);
        if (Objects.nonNull(deploymentState)) {
            deployServiceEntity.setServiceDeploymentState(deploymentState);
        }
        if (StringUtils.isNotBlank(deployResult.getMessage())) {
            deployServiceEntity.setResultMessage(deployResult.getMessage());
        } else {
            // When rollback successfully, the result message should be the previous error message.
            if (isTaskSuccessful && taskType != ServiceOrderType.ROLLBACK) {
                deployServiceEntity.setResultMessage(null);
            }
        }
        if (deploymentState == ServiceDeploymentState.MODIFICATION_SUCCESSFUL) {
            DeployRequest modifyRequest = deployServiceEntity.getDeployRequest();
            deployServiceEntity.setFlavor(modifyRequest.getFlavor());
            deployServiceEntity.setCustomerServiceName(modifyRequest.getCustomerServiceName());
        }

        updateServiceConfiguration(deploymentState, deployServiceEntity);
        updateServiceState(deploymentState, deployServiceEntity);

        if (CollectionUtils.isEmpty(deployResult.getPrivateProperties())) {
            if (isTaskSuccessful) {
                deployServiceEntity.getPrivateProperties().clear();
            }
        } else {
            deployServiceEntity.setPrivateProperties(deployResult.getPrivateProperties());
        }

        if (CollectionUtils.isEmpty(deployResult.getProperties())) {
            if (isTaskSuccessful) {
                deployServiceEntity.getProperties().clear();
            }
        } else {
            deployServiceEntity.setProperties(deployResult.getProperties());
        }

        if (CollectionUtils.isEmpty(deployResult.getResources())) {
            if (isTaskSuccessful) {
                deployServiceEntity.getDeployResourceList().clear();
            }
        } else {
            deployServiceEntity.setDeployResourceList(
                    getDeployResourceEntityList(deployResult.getResources(), deployServiceEntity));
        }
        sensitiveDataHandler.maskSensitiveFields(deployServiceEntity);
    }

    private void updateServiceConfiguration(ServiceDeploymentState state,
                                            DeployServiceEntity deployServiceEntity) {
        if (state == ServiceDeploymentState.DEPLOY_SUCCESS) {
            ServiceConfigurationEntity serviceConfigurationEntity =
                    deployServiceEntityConverter.getInitialServiceConfiguration(
                            deployServiceEntity);
            deployServiceEntity.setServiceConfigurationEntity(serviceConfigurationEntity);
        }
        if (state == ServiceDeploymentState.DESTROY_SUCCESS) {
            deployServiceEntity.setServiceConfigurationEntity(null);
        }
    }

    private void updateServiceState(ServiceDeploymentState state,
                                    DeployServiceEntity deployServiceEntity) {
        if (state == ServiceDeploymentState.DEPLOY_SUCCESS
                || state == ServiceDeploymentState.MODIFICATION_SUCCESSFUL) {
            deployServiceEntity.setServiceState(ServiceState.RUNNING);
            deployServiceEntity.setLastStartedAt(OffsetDateTime.now());
        }
        if (state == ServiceDeploymentState.DEPLOY_FAILED
                || state == ServiceDeploymentState.DESTROY_SUCCESS) {
            deployServiceEntity.setServiceState(ServiceState.NOT_RUNNING);
        }
        // case other cases, do not change the state of service.
    }

    private ServiceDeploymentState getServiceDeploymentState(ServiceOrderType taskType,
                                                             boolean isTaskSuccessful) {
        return switch (taskType) {
            case DEPLOY, RETRY -> isTaskSuccessful ? ServiceDeploymentState.DEPLOY_SUCCESS
                    : ServiceDeploymentState.DEPLOY_FAILED;
            case DESTROY -> isTaskSuccessful ? ServiceDeploymentState.DESTROY_SUCCESS
                    : ServiceDeploymentState.DESTROY_FAILED;
            case MODIFY -> isTaskSuccessful ? ServiceDeploymentState.MODIFICATION_SUCCESSFUL
                    : ServiceDeploymentState.MODIFICATION_FAILED;
            case ROLLBACK -> isTaskSuccessful ? ServiceDeploymentState.DEPLOY_FAILED
                    : ServiceDeploymentState.ROLLBACK_FAILED;
            case PURGE -> isTaskSuccessful ? ServiceDeploymentState.DESTROY_SUCCESS
                    : ServiceDeploymentState.MANUAL_CLEANUP_REQUIRED;
            default -> null;
        };
    }


    /**
     * Convert deploy resources to deploy resource entities.
     */
    private List<DeployResourceEntity> getDeployResourceEntityList(
            List<DeployResource> deployResources, DeployServiceEntity deployServiceEntity) {
        List<DeployResourceEntity> deployResourceEntities = new ArrayList<>();
        if (CollectionUtils.isEmpty(deployResources)) {
            return deployResourceEntities;
        }
        for (DeployResource resource : deployResources) {
            DeployResourceEntity deployResource = new DeployResourceEntity();
            BeanUtils.copyProperties(resource, deployResource);
            deployResource.setDeployService(deployServiceEntity);
            deployResourceEntities.add(deployResource);
        }
        return deployResourceEntities;
    }

    /**
     * Update service order task in the database by the deployment result. We must ensure the
     * order is not set to a final state until all related process is completed.
     *
     * @param deployResult Deployment Result.
     * @param storedEntity DB entity to be updated
     */
    public void updateServiceOrderTaskWithDeployResult(DeployResult deployResult,
                                                       ServiceOrderEntity storedEntity) {
        if (Objects.isNull(deployResult) || Objects.isNull(deployResult.getIsTaskSuccessful())) {
            // happens when the deployment is still asynchronously running.
            return;
        }
        if (Objects.isNull(storedEntity)) {
            storedEntity = serviceOrderStorage.getEntityById(deployResult.getOrderId());
        }
        ServiceOrderEntity entityToUpdate = new ServiceOrderEntity();
        BeanUtils.copyProperties(storedEntity, entityToUpdate);
        TaskStatus taskStatus =
                deployResult.getIsTaskSuccessful() ? TaskStatus.SUCCESSFUL : TaskStatus.FAILED;
        entityToUpdate.setTaskStatus(taskStatus);
        entityToUpdate.setCompletedTime(OffsetDateTime.now());
        serviceOrderStorage.storeAndFlush(entityToUpdate);
        if (deployResult.getTaskType() == ServiceOrderType.ROLLBACK) {
            // complete the parent service order and set its status to failed.
            if (Objects.nonNull(storedEntity.getParentOrderId())) {
                completeParentServiceOrder(storedEntity.getParentOrderId());
                log.info("Rollback order {} completed, related parent order {} is completed.",
                        storedEntity.getOrderId(), storedEntity.getParentOrderId());
            }
        }
    }

    private void completeParentServiceOrder(UUID parentOrderId) {
        ServiceOrderEntity parentOrder = serviceOrderStorage.getEntityById(parentOrderId);
        ServiceOrderEntity entityToUpdate = new ServiceOrderEntity();
        BeanUtils.copyProperties(parentOrder, entityToUpdate);
        entityToUpdate.setTaskStatus(TaskStatus.FAILED);
        entityToUpdate.setCompletedTime(OffsetDateTime.now());
        serviceOrderStorage.storeAndFlush(entityToUpdate);
    }
}
