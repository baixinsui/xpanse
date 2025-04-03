/*
 * Tofu-Maker API
 * RESTful Services to interact with opentofu CLI
 *
 * The version of the OpenAPI document: 1.0.14-SNAPSHOT
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.UUID;

/** TofuMakerSystemStatus */
@JsonPropertyOrder({
    TofuMakerSystemStatus.JSON_PROPERTY_REQUEST_ID,
    TofuMakerSystemStatus.JSON_PROPERTY_HEALTH_STATUS,
    TofuMakerSystemStatus.JSON_PROPERTY_SERVICE_TYPE,
    TofuMakerSystemStatus.JSON_PROPERTY_SERVICE_URL,
    TofuMakerSystemStatus.JSON_PROPERTY_ERROR_MESSAGE
})
@jakarta.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        comments = "Generator version: 7.11.0")
public class TofuMakerSystemStatus {
    public static final String JSON_PROPERTY_REQUEST_ID = "requestId";
    @jakarta.annotation.Nonnull private UUID requestId;

    /** The health status of api service. */
    public enum HealthStatusEnum {
        OK(String.valueOf("OK")),

        NOK(String.valueOf("NOK"));

        private String value;

        HealthStatusEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static HealthStatusEnum fromValue(String value) {
            for (HealthStatusEnum b : HealthStatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_HEALTH_STATUS = "healthStatus";
    @jakarta.annotation.Nonnull private HealthStatusEnum healthStatus;

    public static final String JSON_PROPERTY_SERVICE_TYPE = "serviceType";
    @jakarta.annotation.Nonnull private String serviceType;

    public static final String JSON_PROPERTY_SERVICE_URL = "serviceUrl";
    @jakarta.annotation.Nonnull private String serviceUrl;

    public static final String JSON_PROPERTY_ERROR_MESSAGE = "errorMessage";
    @jakarta.annotation.Nullable private String errorMessage;

    public TofuMakerSystemStatus() {}

    public TofuMakerSystemStatus requestId(@jakarta.annotation.Nonnull UUID requestId) {

        this.requestId = requestId;
        return this;
    }

    /**
     * ID of the request.
     *
     * @return requestId
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRequestId(@jakarta.annotation.Nonnull UUID requestId) {
        this.requestId = requestId;
    }

    public TofuMakerSystemStatus healthStatus(
            @jakarta.annotation.Nonnull HealthStatusEnum healthStatus) {

        this.healthStatus = healthStatus;
        return this;
    }

    /**
     * The health status of api service.
     *
     * @return healthStatus
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_HEALTH_STATUS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public HealthStatusEnum getHealthStatus() {
        return healthStatus;
    }

    @JsonProperty(JSON_PROPERTY_HEALTH_STATUS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setHealthStatus(@jakarta.annotation.Nonnull HealthStatusEnum healthStatus) {
        this.healthStatus = healthStatus;
    }

    public TofuMakerSystemStatus serviceType(@jakarta.annotation.Nonnull String serviceType) {

        this.serviceType = serviceType;
        return this;
    }

    /**
     * The service type of tofu-maker.
     *
     * @return serviceType
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getServiceType() {
        return serviceType;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setServiceType(@jakarta.annotation.Nonnull String serviceType) {
        this.serviceType = serviceType;
    }

    public TofuMakerSystemStatus serviceUrl(@jakarta.annotation.Nonnull String serviceUrl) {

        this.serviceUrl = serviceUrl;
        return this;
    }

    /**
     * The url of tofu-maker service.
     *
     * @return serviceUrl
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE_URL)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getServiceUrl() {
        return serviceUrl;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_URL)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setServiceUrl(@jakarta.annotation.Nonnull String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public TofuMakerSystemStatus errorMessage(@jakarta.annotation.Nullable String errorMessage) {

        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * The error message of tofu-maker service.
     *
     * @return errorMessage
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ERROR_MESSAGE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty(JSON_PROPERTY_ERROR_MESSAGE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setErrorMessage(@jakarta.annotation.Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TofuMakerSystemStatus tofuMakerSystemStatus = (TofuMakerSystemStatus) o;
        return Objects.equals(this.requestId, tofuMakerSystemStatus.requestId)
                && Objects.equals(this.healthStatus, tofuMakerSystemStatus.healthStatus)
                && Objects.equals(this.serviceType, tofuMakerSystemStatus.serviceType)
                && Objects.equals(this.serviceUrl, tofuMakerSystemStatus.serviceUrl)
                && Objects.equals(this.errorMessage, tofuMakerSystemStatus.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, healthStatus, serviceType, serviceUrl, errorMessage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TofuMakerSystemStatus {\n");
        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
        sb.append("    healthStatus: ").append(toIndentedString(healthStatus)).append("\n");
        sb.append("    serviceType: ").append(toIndentedString(serviceType)).append("\n");
        sb.append("    serviceUrl: ").append(toIndentedString(serviceUrl)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first
     * line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
