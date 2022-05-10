package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.exception.WloggerException;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.exceptions.TokenNotActiveException;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
public class TokenService {

    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    public AccessToken verifyToken(String tokenString, KeycloakDeployment deployment) throws WloggerException {
        try {
            AccessToken token = AdapterTokenVerifier.verifyToken(tokenString, deployment);
            logger.fine("Token authorized: " + token.getSubject());
            return token;
        } catch (TokenNotActiveException e) {
            logger.warning("Failed to verify token: " + e.getMessage());
            throw WloggerException.notLoggedIn401(Errors.AUTH_FAILED, e.getMessage());
        } catch (VerificationException e) {
            logger.warning("Failed to verify token: " + e.getMessage());
            // For security purpose, do not return detailed explanation to the user
            throw WloggerException.notLoggedIn401(Errors.AUTH_FAILED, "Token is invalid");
        } catch (NullPointerException e) {
            logger.warning("Failed to verify token: " + e.getMessage());
            // For security purpose, do not return detailed explanation to the user
            throw WloggerException.notLoggedIn401(Errors.AUTH_FAILED, "Get deployment.getJwksUrl() error");
        }
    }
}
