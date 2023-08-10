/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.security.zitadel;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.eclipse.xpanse.modules.models.security.model.CurrentUserInfo;
import org.eclipse.xpanse.modules.models.security.model.TokenResponse;
import org.eclipse.xpanse.modules.models.system.BackendSystemStatus;
import org.eclipse.xpanse.modules.models.system.enums.BackendSystemType;
import org.eclipse.xpanse.modules.models.system.enums.HealthStatus;
import org.eclipse.xpanse.modules.models.system.enums.IdentityProviderType;
import org.eclipse.xpanse.modules.security.IdentityProviderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Service for authorization of IAM 'Zitadel'.
 */
@Slf4j
@Profile("zitadel")
@Service
public class ZitadelIdentityProviderService implements IdentityProviderService {

    private static final String USER_ID_KEY = "sub";
    private static final Map<String, String> CODE_CHALLENGE_MAP = initCodeChallengeMap();
    private static final String USER_NAME_KEY = "preferred_username";
    @Resource
    private RestTemplate restTemplate;
    @Value("${authorization-server-endpoint}")
    private String iamServerEndpoint;
    @Value("${authorization-swagger-ui-client-id}")
    private String clientId;

    private static Map<String, String> initCodeChallengeMap() {
        Map<String, String> map = new HashMap<>(2);
        try {
            SecureRandom sr = new SecureRandom();
            byte[] code = new byte[32];
            sr.nextBytes(code);
            String verifier = Base64.encodeBase64String(code);
            map.put("code_verifier", verifier);

            byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes, 0, bytes.length);
            byte[] digest = md.digest();
            String challenge = Base64.encodeBase64URLSafeString(digest);
            map.put("code_challenge", challenge);
        } catch (NoSuchAlgorithmException e) {
            log.error("initCodeChallengeMap error.", e);
        }
        log.info("CODE_CHALLENGE_MAP:{}", map);
        return map;

    }

    @Override
    public BackendSystemStatus getIdentityProviderStatus() {
        BackendSystemStatus status = new BackendSystemStatus();
        status.setBackendSystemType(BackendSystemType.IDENTITY_PROVIDER);
        status.setName(IdentityProviderType.ZITADEL.toValue());
        status.setEndpoint(iamServerEndpoint);
        status.setHealthStatus(HealthStatus.NOK);
        String healthCheckUrl = iamServerEndpoint + "/debug/healthz";
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(healthCheckUrl, String.class);
            if (Objects.equals(HttpStatus.OK, response.getStatusCode())) {
                status.setHealthStatus(HealthStatus.OK);
            } else {
                status.setDetails(response.getBody());
            }
        } catch (RestClientException e) {
            status.setDetails(e.getMessage());
            log.error("Get health status of the IAM error.", e);
        }
        return status;
    }

    @Override
    public IdentityProviderType getIdentityProviderType() {
        return IdentityProviderType.ZITADEL;
    }

    @Override
    public CurrentUserInfo getCurrentUserInfo() {
        BearerTokenAuthentication tokenAuthentication =
                (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(tokenAuthentication)) {
            Map<String, Object> claimsMap = tokenAuthentication.getTokenAttributes();
            CurrentUserInfo currentUserInfo = new CurrentUserInfo();
            if (claimsMap.containsKey(USER_ID_KEY)) {
                currentUserInfo.setUserId(String.valueOf(claimsMap.get(USER_ID_KEY)));
            }

            if (claimsMap.containsKey(USER_NAME_KEY)) {
                currentUserInfo.setUserName(String.valueOf(claimsMap.get(USER_NAME_KEY)));
            }

            List<String> roles = tokenAuthentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();
            currentUserInfo.setRoles(roles);
            return currentUserInfo;
        }
        return null;
    }

    @Override
    public String getAuthorizeUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString() + "/auth/token";
        stringBuilder.append(iamServerEndpoint).append("/oauth/v2/authorize").append("?")
                .append("client_id=").append(clientId).append("&")
                .append("response_type=code").append("&")
                .append("scope=openid").append("&")
                .append("redirect_uri=").append(redirectUrl).append("&")
                .append("code_challenge_method=S256").append("&")
                .append("code_challenge=").append(CODE_CHALLENGE_MAP.get("code_challenge"));
        return stringBuilder.toString();
    }

    @Override
    public TokenResponse getAccessToken(String code) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("code_verifier", CODE_CHALLENGE_MAP.get("code_verifier"));
        String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString() + "/auth/token";
        map.add("redirect_uri", redirectUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(map, headers);

        String tokenUrl = iamServerEndpoint + "/oauth/v2/token";
        try {
            ResponseEntity<TokenResponse> response =
                    restTemplate.postForEntity(tokenUrl, param, TokenResponse.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Get access token by code:{} form the IAM error.", code, e);
        }
        return null;
    }


}