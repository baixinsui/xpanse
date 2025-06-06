/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.models.monitor.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.models.common.exceptions.UnsupportedEnumValueException;

/** The types of the metrics. */
public enum MetricType {
    COUNTER("counter"),
    GAUGE("gauge"),
    HISTOGRAM("histogram"),
    SUMMARY("summary");

    private final String type;

    MetricType(String type) {
        this.type = type;
    }

    /** For MetricsType serialize. */
    @JsonCreator
    public MetricType getByValue(String type) {
        for (MetricType metricsType : values()) {
            if (metricsType.type.equals(StringUtils.lowerCase(type))) {
                return metricsType;
            }
        }
        throw new UnsupportedEnumValueException(
                String.format("MetricType value %s is not supported.", type));
    }

    /** For MetricsType deserialize. */
    @JsonValue
    public String toValue() {
        return this.type;
    }
}
