/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.orchestrator.plugin.openstack.monitor.keystone;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import org.eclipse.xpanse.modules.credential.CredentialDefinition;
import org.eclipse.xpanse.modules.credential.CredentialVariable;
import org.eclipse.xpanse.modules.credential.enums.CredentialType;
import org.eclipse.xpanse.modules.monitor.exceptions.CredentialsNotFoundException;
import org.eclipse.xpanse.orchestrator.plugin.openstack.constants.OpenstackEnvironmentConstants;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Component;

/**
 * Class to encapsulate all calls to Keystone API.
 */
@Component
public class KeystoneManager {

    /**
     * Authenticates and sets the authentication details in the thread context which can be
     * used for the further calls to Openstack API.
     *
     * @param credentialDefinition Credential information available for Openstack in the runtime.
     */
    public void authenticate(CredentialDefinition credentialDefinition) {
        String userName = null;
        String password = null;
        String tenant = null;
        String url = null;
        String domain = null;
        if (CredentialType.VARIABLES.toValue().equals(credentialDefinition.getType().toValue())) {
            List<CredentialVariable> variables = credentialDefinition.getVariables();
            for (CredentialVariable credentialVariable : variables) {
                if (OpenstackEnvironmentConstants.USERNAME.equals(
                        credentialVariable.getName())) {
                    userName = credentialVariable.getValue();
                }
                if (OpenstackEnvironmentConstants.PASSWORD.equals(
                        credentialVariable.getName())) {
                    password = credentialVariable.getValue();
                }
                if (OpenstackEnvironmentConstants.TENANT.equals(
                        credentialVariable.getName())) {
                    tenant = credentialVariable.getValue();
                }
                if (OpenstackEnvironmentConstants.AUTH_URL.equals(
                        credentialVariable.getName())) {
                    url = credentialVariable.getValue();
                }
                if (OpenstackEnvironmentConstants.DOMAIN.equals(
                        credentialVariable.getName())) {
                    domain = credentialVariable.getValue();
                }
            }
        }
        if (Objects.isNull(userName) || Objects.isNull(password) || Objects.isNull(tenant)
                || Objects.isNull(url) || Objects.isNull(domain)) {
            throw new CredentialsNotFoundException(
                    "Values for all openstack credential"
                            + " variables to connect to Openstack API is not found");
        }
        OSFactory.enableHttpLoggingFilter(true);
        // there is no need to return the authenticated client because the below method already sets
        // the authentication details in the thread context.
        OSFactory
                .builderV3()
                .withConfig(buildClientConfig(url))
                .credentials(userName, password, Identifier.byName("default"))
                .scopeToProject(Identifier.byName(tenant), Identifier.byId("default"))
                .endpoint(url)
                .authenticate();
    }

    private static String getIpAddressFromUrl(String url) {
        try {
            return InetAddress.getByName(new URL(url).getHost()).getHostAddress();
        } catch (UnknownHostException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Config buildClientConfig(String url) {
        return Config.newConfig()
                .withEndpointNATResolution(getIpAddressFromUrl(url))
                .withEndpointURLResolver(new CustomEndPointResolver());
    }
}