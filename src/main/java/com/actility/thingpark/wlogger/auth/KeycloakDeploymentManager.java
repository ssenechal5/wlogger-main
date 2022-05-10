package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.config.KeycloakConfig;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ApplicationScoped
public class KeycloakDeploymentManager {

    private static final Logger logger = Logger.getLogger(KeycloakDeploymentManager.class.getName());

    // cache of all Keycloak deployments, per realm
    private final Map<String, KeycloakDeployment> keycloakDeploymentMap = new ConcurrentHashMap<>();

    private final KeycloakConfig keycloakConfig;

    @Inject
    public KeycloakDeploymentManager(KeycloakConfig keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
    }

    public KeycloakDeployment getKeycloakDeployment(UriInfo uriInfo, String realmID) {
        URI forcedHttpsUri = uriInfo.getBaseUriBuilder().scheme("https").build();
        URI keycloakUrl = forcedHttpsUri.resolve(URI.create(keycloakConfig.uri()));

        return cachedKeycloakDeployment(keycloakUrl, realmID).
                orElseGet(() -> {
                    KeycloakDeployment deployment = buildKeycloakDeployment(keycloakUrl, realmID);
                    cacheKeycloakDeployment(keycloakUrl, realmID, deployment);
                    return deployment;
                });
    }

    private KeycloakDeployment buildKeycloakDeployment(URI keycloakUrl, String realmID) {
        logger.fine("Create new KeycloayDeployment instance for realm " + realmID + " with keycloak uri " + keycloakUrl);
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setAuthServerUrl(keycloakUrl.toString());
        // client is not needed to validate oauth token but it is mandatory for KeycloakDeployment creation, set a fake value
        adapterConfig.setResource(keycloakConfig.clientApi());
        adapterConfig.setRealm(realmID);
        return KeycloakDeploymentBuilder.build(adapterConfig);
    }

    private Optional<KeycloakDeployment> cachedKeycloakDeployment(URI keycloakUrl, String realmID) {
        return Optional.ofNullable(keycloakDeploymentMap.get(cacheKey(keycloakUrl, realmID)));
    }

    private void cacheKeycloakDeployment(URI keycloakUrl, String realmID, KeycloakDeployment deployment) {
        keycloakDeploymentMap.put(cacheKey(keycloakUrl, realmID), deployment);
    }

    private static String cacheKey(URI keycloakUrl, String realmID) {
        return keycloakUrl.resolve(realmID).toString();
    }

    // For Tests
    public Map<String, KeycloakDeployment> getKeycloakDeploymentMap() {
        return keycloakDeploymentMap;
    }
}
