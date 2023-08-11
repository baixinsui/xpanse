/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.service.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.models.common.exceptions.UnsupportedEnumValueException;

/**
 * Cloud service providers.
 */
public enum Csp {
    HUAWEI("huawei"),
    FLEXIBLE_ENGINE("flexibleEngine"),
    OPENSTACK("openstack"),
    ALICLOUD("alicloud"),
    AWS("aws"),
    AZURE("azure"),
    GOOGLE("google"),
    SCS("scs");

    private final String value;

    Csp(String value) {
        this.value = value;
    }

    /**
     * For CSP serialize.
     */
    @JsonCreator
    public static Csp getByValue(String name) {
        for (Csp csp : values()) {
            if (StringUtils.equalsIgnoreCase(csp.value, name)) {
                return csp;
            }
        }
        throw new UnsupportedEnumValueException(
                String.format("Csp value %s is not supported.", name));
    }

    /**
     * For CSP deserialize.
     */
    @JsonValue
    public String toValue() {
        return this.value;
    }
}
