/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.orchestrator.price;

import java.util.UUID;
import lombok.Data;
import org.eclipse.xpanse.modules.models.billing.RatingMode;
import org.eclipse.xpanse.modules.models.billing.enums.BillingMode;

/** Define the request for calculating the price of the flavor of the service. */
@Data
public class ServiceFlavorPriceRequest {
    private UUID serviceTemplateId;
    private String flavorName;
    private String userId;
    private String regionName;
    private String siteName;
    private RatingMode flavorRatingMode;
    private BillingMode billingMode;
}
