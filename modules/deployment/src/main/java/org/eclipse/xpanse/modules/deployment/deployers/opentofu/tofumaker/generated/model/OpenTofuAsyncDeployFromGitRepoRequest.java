/*
 * Tofu-Maker API
 * RESTful Services to interact with opentofu CLI
 *
 * The version of the OpenAPI document: 1.0.13-SNAPSHOT
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/** OpenTofuAsyncDeployFromGitRepoRequest */
@JsonPropertyOrder({
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_REQUEST_ID,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_OPEN_TOFU_VERSION,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_IS_PLAN_ONLY,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_VARIABLES,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_ENV_VARIABLES,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_GIT_REPO_DETAILS,
    OpenTofuAsyncDeployFromGitRepoRequest.JSON_PROPERTY_WEBHOOK_CONFIG
})
@jakarta.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        comments = "Generator version: 7.11.0")
public class OpenTofuAsyncDeployFromGitRepoRequest {
    public static final String JSON_PROPERTY_REQUEST_ID = "requestId";
    @jakarta.annotation.Nullable private UUID requestId;

    public static final String JSON_PROPERTY_OPEN_TOFU_VERSION = "openTofuVersion";
    @jakarta.annotation.Nonnull private String openTofuVersion;

    public static final String JSON_PROPERTY_IS_PLAN_ONLY = "isPlanOnly";
    @jakarta.annotation.Nonnull private Boolean isPlanOnly;

    public static final String JSON_PROPERTY_VARIABLES = "variables";
    @jakarta.annotation.Nonnull private Map<String, Object> variables = new HashMap<>();

    public static final String JSON_PROPERTY_ENV_VARIABLES = "envVariables";
    @jakarta.annotation.Nullable private Map<String, String> envVariables = new HashMap<>();

    public static final String JSON_PROPERTY_GIT_REPO_DETAILS = "gitRepoDetails";
    @jakarta.annotation.Nonnull private OpenTofuScriptGitRepoDetails gitRepoDetails;

    public static final String JSON_PROPERTY_WEBHOOK_CONFIG = "webhookConfig";
    @jakarta.annotation.Nonnull private WebhookConfig webhookConfig;

    public OpenTofuAsyncDeployFromGitRepoRequest() {}

    public OpenTofuAsyncDeployFromGitRepoRequest requestId(
            @jakarta.annotation.Nullable UUID requestId) {

        this.requestId = requestId;
        return this;
    }

    /**
     * Id of the request.
     *
     * @return requestId
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public UUID getRequestId() {
        return requestId;
    }

    @JsonProperty(JSON_PROPERTY_REQUEST_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRequestId(@jakarta.annotation.Nullable UUID requestId) {
        this.requestId = requestId;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest openTofuVersion(
            @jakarta.annotation.Nonnull String openTofuVersion) {

        this.openTofuVersion = openTofuVersion;
        return this;
    }

    /**
     * The required version of the OpenTofu which will execute the scripts.
     *
     * @return openTofuVersion
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_OPEN_TOFU_VERSION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getOpenTofuVersion() {
        return openTofuVersion;
    }

    @JsonProperty(JSON_PROPERTY_OPEN_TOFU_VERSION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setOpenTofuVersion(@jakarta.annotation.Nonnull String openTofuVersion) {
        this.openTofuVersion = openTofuVersion;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest isPlanOnly(
            @jakarta.annotation.Nonnull Boolean isPlanOnly) {

        this.isPlanOnly = isPlanOnly;
        return this;
    }

    /**
     * Flag to control if the deployment must only generate the OpenTofu or it must also apply the
     * changes.
     *
     * @return isPlanOnly
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IS_PLAN_ONLY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getIsPlanOnly() {
        return isPlanOnly;
    }

    @JsonProperty(JSON_PROPERTY_IS_PLAN_ONLY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIsPlanOnly(@jakarta.annotation.Nonnull Boolean isPlanOnly) {
        this.isPlanOnly = isPlanOnly;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest variables(
            @jakarta.annotation.Nonnull Map<String, Object> variables) {

        this.variables = variables;
        return this;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest putVariablesItem(
            String key, Object variablesItem) {
        this.variables.put(key, variablesItem);
        return this;
    }

    /**
     * Key-value pairs of variables that must be used to execute the OpenTofu request.
     *
     * @return variables
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Map<String, Object> getVariables() {
        return variables;
    }

    @JsonProperty(JSON_PROPERTY_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setVariables(@jakarta.annotation.Nonnull Map<String, Object> variables) {
        this.variables = variables;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest envVariables(
            @jakarta.annotation.Nullable Map<String, String> envVariables) {

        this.envVariables = envVariables;
        return this;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest putEnvVariablesItem(
            String key, String envVariablesItem) {
        if (this.envVariables == null) {
            this.envVariables = new HashMap<>();
        }
        this.envVariables.put(key, envVariablesItem);
        return this;
    }

    /**
     * Key-value pairs of variables that must be injected as environment variables to OpenTofu
     * process.
     *
     * @return envVariables
     */
    @jakarta.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ENV_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, String> getEnvVariables() {
        return envVariables;
    }

    @JsonProperty(JSON_PROPERTY_ENV_VARIABLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEnvVariables(@jakarta.annotation.Nullable Map<String, String> envVariables) {
        this.envVariables = envVariables;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest gitRepoDetails(
            @jakarta.annotation.Nonnull OpenTofuScriptGitRepoDetails gitRepoDetails) {

        this.gitRepoDetails = gitRepoDetails;
        return this;
    }

    /**
     * GIT Repo details from where the scripts can be fetched.
     *
     * @return gitRepoDetails
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_GIT_REPO_DETAILS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OpenTofuScriptGitRepoDetails getGitRepoDetails() {
        return gitRepoDetails;
    }

    @JsonProperty(JSON_PROPERTY_GIT_REPO_DETAILS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setGitRepoDetails(
            @jakarta.annotation.Nonnull OpenTofuScriptGitRepoDetails gitRepoDetails) {
        this.gitRepoDetails = gitRepoDetails;
    }

    public OpenTofuAsyncDeployFromGitRepoRequest webhookConfig(
            @jakarta.annotation.Nonnull WebhookConfig webhookConfig) {

        this.webhookConfig = webhookConfig;
        return this;
    }

    /**
     * Configuration information of webhook.
     *
     * @return webhookConfig
     */
    @jakarta.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_WEBHOOK_CONFIG)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public WebhookConfig getWebhookConfig() {
        return webhookConfig;
    }

    @JsonProperty(JSON_PROPERTY_WEBHOOK_CONFIG)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setWebhookConfig(@jakarta.annotation.Nonnull WebhookConfig webhookConfig) {
        this.webhookConfig = webhookConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenTofuAsyncDeployFromGitRepoRequest openTofuAsyncDeployFromGitRepoRequest =
                (OpenTofuAsyncDeployFromGitRepoRequest) o;
        return Objects.equals(this.requestId, openTofuAsyncDeployFromGitRepoRequest.requestId)
                && Objects.equals(
                        this.openTofuVersion, openTofuAsyncDeployFromGitRepoRequest.openTofuVersion)
                && Objects.equals(this.isPlanOnly, openTofuAsyncDeployFromGitRepoRequest.isPlanOnly)
                && Objects.equals(this.variables, openTofuAsyncDeployFromGitRepoRequest.variables)
                && Objects.equals(
                        this.envVariables, openTofuAsyncDeployFromGitRepoRequest.envVariables)
                && Objects.equals(
                        this.gitRepoDetails, openTofuAsyncDeployFromGitRepoRequest.gitRepoDetails)
                && Objects.equals(
                        this.webhookConfig, openTofuAsyncDeployFromGitRepoRequest.webhookConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                requestId,
                openTofuVersion,
                isPlanOnly,
                variables,
                envVariables,
                gitRepoDetails,
                webhookConfig);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpenTofuAsyncDeployFromGitRepoRequest {\n");
        sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
        sb.append("    openTofuVersion: ").append(toIndentedString(openTofuVersion)).append("\n");
        sb.append("    isPlanOnly: ").append(toIndentedString(isPlanOnly)).append("\n");
        sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
        sb.append("    envVariables: ").append(toIndentedString(envVariables)).append("\n");
        sb.append("    gitRepoDetails: ").append(toIndentedString(gitRepoDetails)).append("\n");
        sb.append("    webhookConfig: ").append(toIndentedString(webhookConfig)).append("\n");
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
