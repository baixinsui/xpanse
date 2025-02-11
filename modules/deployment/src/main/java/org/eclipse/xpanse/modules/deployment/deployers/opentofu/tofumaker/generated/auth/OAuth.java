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

package org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.auth;

import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

/** Provides support for RFC 6750 - Bearer Token usage for OAUTH 2.0 Authorization. */
@jakarta.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        comments = "Generator version: 7.11.0")
public class OAuth implements Authentication {
    private Supplier<String> tokenSupplier;

    /**
     * Returns the bearer token used for Authorization.
     *
     * @return The bearer token
     */
    public String getAccessToken() {
        return tokenSupplier.get();
    }

    /**
     * Sets the bearer access token used for Authorization.
     *
     * @param accessToken The bearer token to send in the Authorization header
     */
    public void setAccessToken(String accessToken) {
        setAccessToken(() -> accessToken);
    }

    /**
     * Sets the supplier of bearer tokens used for Authorization.
     *
     * @param tokenSupplier The supplier of bearer tokens to send in the Authorization header
     */
    public void setAccessToken(Supplier<String> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public void applyToParams(
            MultiValueMap<String, String> queryParams,
            HttpHeaders headerParams,
            MultiValueMap<String, String> cookieParams) {
        Optional.ofNullable(tokenSupplier)
                .map(Supplier::get)
                .ifPresent(
                        accessToken ->
                                headerParams.add(
                                        HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
    }
}
