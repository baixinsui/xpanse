/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.serviceconfiguration;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Data;

/** Details of the service configuration. */
@Data
public class ServiceConfigurationDetails {
    private Map<String, Object> configuration;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
}
