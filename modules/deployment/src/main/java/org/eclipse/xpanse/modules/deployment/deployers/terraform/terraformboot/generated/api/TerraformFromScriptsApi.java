package org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.api;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.ApiClient;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformAsyncDeployFromScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformAsyncDestroyFromScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformAsyncModifyFromScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformDeployWithScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformDestroyWithScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformModifyWithScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformPlan;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformPlanWithScriptsRequest;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformResult;
import org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.model.TerraformValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.4.0")
@Component("org.eclipse.xpanse.modules.deployment.deployers.terraform.terraformboot.generated.api.TerraformFromScriptsApi")
public class TerraformFromScriptsApi {
    private ApiClient apiClient;

    public TerraformFromScriptsApi() {
        this(new ApiClient());
    }

    @Autowired
    public TerraformFromScriptsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * async deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncDeployFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void asyncDeployWithScripts(TerraformAsyncDeployFromScriptsRequest terraformAsyncDeployFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        asyncDeployWithScriptsWithHttpInfo(terraformAsyncDeployFromScriptsRequest, xCustomRequestId);
    }

    /**
     * 
     * async deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncDeployFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> asyncDeployWithScriptsWithHttpInfo(TerraformAsyncDeployFromScriptsRequest terraformAsyncDeployFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformAsyncDeployFromScriptsRequest;
        
        // verify the required parameter 'terraformAsyncDeployFromScriptsRequest' is set
        if (terraformAsyncDeployFromScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformAsyncDeployFromScriptsRequest' when calling asyncDeployWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<Void> localReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/deploy/async", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Async destroy the Terraform modules
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncDestroyFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void asyncDestroyWithScripts(TerraformAsyncDestroyFromScriptsRequest terraformAsyncDestroyFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        asyncDestroyWithScriptsWithHttpInfo(terraformAsyncDestroyFromScriptsRequest, xCustomRequestId);
    }

    /**
     * 
     * Async destroy the Terraform modules
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncDestroyFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> asyncDestroyWithScriptsWithHttpInfo(TerraformAsyncDestroyFromScriptsRequest terraformAsyncDestroyFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformAsyncDestroyFromScriptsRequest;
        
        // verify the required parameter 'terraformAsyncDestroyFromScriptsRequest' is set
        if (terraformAsyncDestroyFromScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformAsyncDestroyFromScriptsRequest' when calling asyncDestroyWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<Void> localReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/destroy/async", HttpMethod.DELETE,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * async modify resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncModifyFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void asyncModifyWithScripts(TerraformAsyncModifyFromScriptsRequest terraformAsyncModifyFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        asyncModifyWithScriptsWithHttpInfo(terraformAsyncModifyFromScriptsRequest, xCustomRequestId);
    }

    /**
     * 
     * async modify resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>202</b> - Accepted
     * @param terraformAsyncModifyFromScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> asyncModifyWithScriptsWithHttpInfo(TerraformAsyncModifyFromScriptsRequest terraformAsyncModifyFromScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformAsyncModifyFromScriptsRequest;
        
        // verify the required parameter 'terraformAsyncModifyFromScriptsRequest' is set
        if (terraformAsyncModifyFromScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformAsyncModifyFromScriptsRequest' when calling asyncModifyWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<Void> localReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/modify/async", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDeployWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return TerraformResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TerraformResult deployWithScripts(TerraformDeployWithScriptsRequest terraformDeployWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        return deployWithScriptsWithHttpInfo(terraformDeployWithScriptsRequest, xCustomRequestId).getBody();
    }

    /**
     * 
     * Deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDeployWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;TerraformResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TerraformResult> deployWithScriptsWithHttpInfo(TerraformDeployWithScriptsRequest terraformDeployWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformDeployWithScriptsRequest;
        
        // verify the required parameter 'terraformDeployWithScriptsRequest' is set
        if (terraformDeployWithScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformDeployWithScriptsRequest' when calling deployWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*", "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<TerraformResult> localReturnType = new ParameterizedTypeReference<TerraformResult>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/deploy", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Destroy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDestroyWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return TerraformResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TerraformResult destroyWithScripts(TerraformDestroyWithScriptsRequest terraformDestroyWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        return destroyWithScriptsWithHttpInfo(terraformDestroyWithScriptsRequest, xCustomRequestId).getBody();
    }

    /**
     * 
     * Destroy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDestroyWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;TerraformResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TerraformResult> destroyWithScriptsWithHttpInfo(TerraformDestroyWithScriptsRequest terraformDestroyWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformDestroyWithScriptsRequest;
        
        // verify the required parameter 'terraformDestroyWithScriptsRequest' is set
        if (terraformDestroyWithScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformDestroyWithScriptsRequest' when calling destroyWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*", "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<TerraformResult> localReturnType = new ParameterizedTypeReference<TerraformResult>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/destroy", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Modify resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformModifyWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return TerraformResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TerraformResult modifyWithScripts(TerraformModifyWithScriptsRequest terraformModifyWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        return modifyWithScriptsWithHttpInfo(terraformModifyWithScriptsRequest, xCustomRequestId).getBody();
    }

    /**
     * 
     * Modify resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformModifyWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;TerraformResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TerraformResult> modifyWithScriptsWithHttpInfo(TerraformModifyWithScriptsRequest terraformModifyWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformModifyWithScriptsRequest;
        
        // verify the required parameter 'terraformModifyWithScriptsRequest' is set
        if (terraformModifyWithScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformModifyWithScriptsRequest' when calling modifyWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*", "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<TerraformResult> localReturnType = new ParameterizedTypeReference<TerraformResult>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/modify", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Get Terraform Plan as JSON string from the list of script files provided
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformPlanWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return TerraformPlan
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TerraformPlan planWithScripts(TerraformPlanWithScriptsRequest terraformPlanWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        return planWithScriptsWithHttpInfo(terraformPlanWithScriptsRequest, xCustomRequestId).getBody();
    }

    /**
     * 
     * Get Terraform Plan as JSON string from the list of script files provided
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformPlanWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;TerraformPlan&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TerraformPlan> planWithScriptsWithHttpInfo(TerraformPlanWithScriptsRequest terraformPlanWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformPlanWithScriptsRequest;
        
        // verify the required parameter 'terraformPlanWithScriptsRequest' is set
        if (terraformPlanWithScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformPlanWithScriptsRequest' when calling planWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*", "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<TerraformPlan> localReturnType = new ParameterizedTypeReference<TerraformPlan>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/plan", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
    /**
     * 
     * Deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDeployWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return TerraformValidationResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TerraformValidationResult validateWithScripts(TerraformDeployWithScriptsRequest terraformDeployWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        return validateWithScriptsWithHttpInfo(terraformDeployWithScriptsRequest, xCustomRequestId).getBody();
    }

    /**
     * 
     * Deploy resources via Terraform
     * <p><b>422</b> - Unprocessable Entity
     * <p><b>400</b> - Bad Request
     * <p><b>503</b> - Service Unavailable
     * <p><b>502</b> - Bad Gateway
     * <p><b>200</b> - OK
     * @param terraformDeployWithScriptsRequest  (required)
     * @param xCustomRequestId  (optional)
     * @return ResponseEntity&lt;TerraformValidationResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TerraformValidationResult> validateWithScriptsWithHttpInfo(TerraformDeployWithScriptsRequest terraformDeployWithScriptsRequest, UUID xCustomRequestId) throws RestClientException {
        Object localVarPostBody = terraformDeployWithScriptsRequest;
        
        // verify the required parameter 'terraformDeployWithScriptsRequest' is set
        if (terraformDeployWithScriptsRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'terraformDeployWithScriptsRequest' when calling validateWithScripts");
        }
        

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        if (xCustomRequestId != null)
        localVarHeaderParams.add("X-Custom-RequestId", apiClient.parameterToString(xCustomRequestId));

        final String[] localVarAccepts = { 
            "*/*", "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "OAuth2Flow" };

        ParameterizedTypeReference<TerraformValidationResult> localReturnType = new ParameterizedTypeReference<TerraformValidationResult>() {};
        return apiClient.invokeAPI("/terraform-boot/scripts/validate", HttpMethod.POST,
                Collections.emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams,
                localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType,
                localVarAuthNames, localReturnType);
    }
}
