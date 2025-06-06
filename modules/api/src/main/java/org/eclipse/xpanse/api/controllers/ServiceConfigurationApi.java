/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.api.controllers;

import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_ADMIN;
import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.api.config.AuditApiRequest;
import org.eclipse.xpanse.modules.deployment.ServiceConfigurationManager;
import org.eclipse.xpanse.modules.models.service.order.ServiceOrder;
import org.eclipse.xpanse.modules.models.serviceconfiguration.ServiceConfigurationDetails;
import org.eclipse.xpanse.modules.models.serviceconfiguration.ServiceConfigurationUpdate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** REST interface methods for service configuration. */
@Slf4j
@RestController
@RequestMapping("/xpanse")
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_USER})
@ConditionalOnProperty(name = "enable.agent.api.only", havingValue = "false", matchIfMissing = true)
public class ServiceConfigurationApi {

    @Resource private ServiceConfigurationManager serviceConfigurationManager;

    /**
     * Query the service's current configuration by id of the deployed service.
     *
     * @param serviceId id of the deployed service.
     * @return ServiceConfigurationEntity.
     */
    @Tag(name = "ServiceConfiguration", description = "APIs for managing service's configuration.")
    @GetMapping(
            value = "/service/current/config/{serviceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description =
                    "Query the service's current configuration by" + " id of the deployed service.")
    @AuditApiRequest(methodName = "getCspFromServiceId", paramTypes = UUID.class)
    public ServiceConfigurationDetails getCurrentConfigurationOfService(
            @Parameter(name = "serviceId", description = "The id of the deployed service")
                    @PathVariable("serviceId")
                    UUID serviceId) {
        return serviceConfigurationManager.getCurrentConfigurationOfService(serviceId);
    }

    /**
     * Update the service's configuration to the registered service template.
     *
     * @param serviceId id of the deployed service
     * @param serviceConfigurationUpdate serviceConfigurationUpdate.
     * @return serviceOrder.
     */
    @Tag(name = "ServiceConfiguration", description = "APIs for managing service's configuration.")
    @PutMapping(value = "/services/config/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            description = "Update the service's configuration to the registered service template.")
    @AuditApiRequest(methodName = "getCspFromServiceId", paramTypes = UUID.class)
    public ServiceOrder changeServiceConfiguration(
            @Parameter(name = "serviceId", description = "The id of the deployed service")
                    @PathVariable("serviceId")
                    UUID serviceId,
            @Valid @RequestBody ServiceConfigurationUpdate serviceConfigurationUpdate) {
        return serviceConfigurationManager.changeServiceConfiguration(
                serviceId, serviceConfigurationUpdate);
    }
}
