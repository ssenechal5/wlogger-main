package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.google.common.collect.ImmutableList;
import org.jboss.resteasy.util.HttpHeaderNames;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Authenticated
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final List<String> SESSION_TOKEN_QUERY_PARAMS = ImmutableList.of("sessionId", "sessionToken");

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    private AuthenticationService authenticationService;

    @Inject
    void inject(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        String authHeader = containerRequestContext.getHeaderString(HttpHeaderNames.AUTHORIZATION);
        if (authHeader != null) {
            try {
                authenticationService.oauthAuthentication(authHeader, containerRequestContext);
            } catch (WloggerException e) {
                this.logger.fine("Authentication error: " + e.getMessage());
                abortWithUnauthorized(containerRequestContext, Errors.AUTH_FAILED_NOT_AUTHENTICATED,e.getMessage());
            }
        } else {
            // Legacy authentication with session token
          final MultivaluedMap<String, String> queryParam =
                  containerRequestContext.getUriInfo().getQueryParameters();
          Optional<String> sessionToken =
              SESSION_TOKEN_QUERY_PARAMS.stream()
                  .map(queryParam::getFirst)
                  .filter(Objects::nonNull)
                  .findFirst();

          if (sessionToken.map(t -> t.length() < 1).orElse(true)) {
            abortWithUnauthorized(
                    containerRequestContext,
                Errors.AUTH_FAILED_NOT_AUTHENTICATED,
                "Missing session token query param");
            return;
          }

          Optional<WloggerSession> wloggerSession = authenticationService.getWloggerSession();

          if (!wloggerSession.isPresent()) {
              abortWithUnauthorized(containerRequestContext, Errors.AUTH_FAILED_NOT_AUTHENTICATED,"Session not found !!!");
              return;
          } else {
              validateWloggerSession(containerRequestContext, wloggerSession.get(), (sessionToken.orElse(null)));
          }
      }
    }

    private void validateWloggerSession(ContainerRequestContext requestContext, WloggerSession wloggerSession, String sessionToken) {
        String expectedSessionToken = wloggerSession.getSessionToken();
        if (!Objects.equals(expectedSessionToken, sessionToken)) {
            logger.severe(String.format("Session token does not match, expected: %s, given: %s",
                    expectedSessionToken,
                    sessionToken));
            abortWithUnauthorized(requestContext, Errors.AUTH_FAILED_INVALID_SESSION,"Session token is not valid !!!");
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, int code, String message) {
        ResponseSimple error = new ResponseSimple().withSuccess(Boolean.FALSE).withError(message)
                .withStatusCode(String.valueOf(Response.Status.UNAUTHORIZED)).withErrorCode(String.valueOf(code));
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).entity(error)
                        .build());
    }
}