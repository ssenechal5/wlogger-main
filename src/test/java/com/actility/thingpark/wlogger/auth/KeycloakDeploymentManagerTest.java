package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.config.KeycloakConfig;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.KeycloakDeployment;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeycloakDeploymentManagerTest {

    @Test
    void getKeycloakDeployment() {

        KeycloakConfig keycloakConfig = new KeycloakConfig() {
            @Override
            public String uri() {
                return "/auth";
            }

            @Override
            public String uriGui() {
                return "/auth";
            }

            @Override
            public String discoveryUri() {
                return null;
            }

            @Override
            public String clientGui() {
                return null;
            }

            @Override
            public String clientApi() {
                return "wlogger-api";
            }

            @Override
            public Optional<String> realm() {
                return Optional.empty();
            }

            @Override
            public String responseMode() { return "query"; }
        };
        KeycloakDeploymentManager keycloakDeploymentManager = new KeycloakDeploymentManager(keycloakConfig);

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("http://wlogger.test/rest/foo"));
        String realm = "realm";
        String realm2 = "realm2";

        assertEquals(true, keycloakDeploymentManager.getKeycloakDeploymentMap().isEmpty());

        KeycloakDeployment keycloakDeployment = keycloakDeploymentManager.getKeycloakDeployment(uriInfo, realm);

        assertEquals(1, keycloakDeploymentManager.getKeycloakDeploymentMap().size());
        assertEquals(true, keycloakDeployment != null);
        assertEquals(realm, keycloakDeployment.getRealm());
        assertEquals("https://wlogger.test/auth", keycloakDeployment.getAuthServerBaseUrl());

        keycloakDeployment = keycloakDeploymentManager.getKeycloakDeployment(uriInfo, realm);
        assertEquals(1, keycloakDeploymentManager.getKeycloakDeploymentMap().size());
        assertEquals(true, keycloakDeployment != null);
        assertEquals(realm, keycloakDeployment.getRealm());

        keycloakDeployment = keycloakDeploymentManager.getKeycloakDeployment(uriInfo, realm2);
        assertEquals(2, keycloakDeploymentManager.getKeycloakDeploymentMap().size());
        assertEquals(true, keycloakDeployment != null);
        assertEquals(realm2, keycloakDeployment.getRealm());
    }

    @Test
    void getKeycloakDeploymentAbsoluteUri() {

        KeycloakConfig keycloakConfig = new KeycloakConfig() {
            @Override
            public String uri() {
                return "http://foobar/auth";
            }

            @Override
            public String uriGui() {
                return "/auth";
            }

            @Override
            public String discoveryUri() {
                return null;
            }

            @Override
            public String clientGui() {
                return null;
            }

            @Override
            public String clientApi() {
                return "wlogger-api";
            }

            @Override
            public Optional<String> realm() {
                return Optional.empty();
            }

            @Override
            public String responseMode() { return "query"; }
        };
        KeycloakDeploymentManager keycloakDeploymentManager = new KeycloakDeploymentManager(keycloakConfig);

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getBaseUriBuilder()).thenReturn(UriBuilder.fromUri("http://wlogger.test/rest/foo"));
        String realm = "realm";
        KeycloakDeployment keycloakDeployment = keycloakDeploymentManager.getKeycloakDeployment(uriInfo, realm);
        assertNotNull(keycloakDeployment);
        assertEquals(realm, keycloakDeployment.getRealm());
        assertEquals("http://foobar/auth", keycloakDeployment.getAuthServerBaseUrl());
    }
}