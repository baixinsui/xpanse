/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.runtime.modules.credential;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.xpanse.common.openapi.OpenApiUtil;
import org.eclipse.xpanse.modules.credential.CredentialOpenApiGenerator;
import org.eclipse.xpanse.modules.credential.CredentialsStore;
import org.eclipse.xpanse.modules.credential.cache.CaffeineCredentialCacheManager;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.eclipse.xpanse.modules.models.service.common.enums.Csp;
import org.eclipse.xpanse.modules.orchestrator.PluginManager;
import org.eclipse.xpanse.runtime.XpanseApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Test of CredentialOpenApiGenerator.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {XpanseApplication.class, CaffeineCredentialCacheManager.class,
        CredentialsStore.class,
        ServletUriComponentsBuilder.class, OpenApiUtil.class, PluginManager.class})
class CredentialOpenApiGeneratorTest {

    @Autowired
    private CredentialOpenApiGenerator credentialOpenApiGenerator;

    @Autowired
    private OpenApiUtil openApiUtil;


    @Test
    void testGetServiceUrl() {

        final String result = credentialOpenApiGenerator.getServiceUrl();

        assertNotEquals(0, result.length());
    }

    @Test
    void testRun() {
        credentialOpenApiGenerator.run(null);
    }

    @Test
    void testGetCredentialOpenApiUrl() {
        Csp csp = Csp.HUAWEI;
        CredentialType type = CredentialType.VARIABLES;

        String result = credentialOpenApiGenerator.getCredentialOpenApiUrl(csp, type);

        assertNotEquals(0, result.length());
    }
}