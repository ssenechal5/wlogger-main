package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.auth.AuthenticationService;
import com.actility.thingpark.wlogger.auth.SuccessLogin;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/portal")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class PortalController {

    private final Logger logger = Logger.getLogger(PortalController.class.getName());

    private WloggerConfig wloggerConfig;
    private AuthenticationService authenticationService;

    @Inject
    void inject(
            final WloggerConfig wloggerConfig,
            final AuthenticationService authenticationService) {
        this.wloggerConfig = wloggerConfig;
        this.authenticationService = authenticationService;
    }

    static URI buildGuiUri(
            URI uri, String scopeId, String type, String sessionToken, String languageTag) {
        return UriBuilder.fromUri(uri)
                .replacePath("/thingpark/wlogger/gui")
                .queryParam("uid", scopeId)
                .queryParam("type", type)
                .queryParam("sessionToken", sessionToken)
                .queryParam("userAccount", scopeId)
                .queryParam("lang", languageTag)
                .queryParam("language-tag", languageTag)
                .build();
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response portal(
            @Context UriInfo uriInfo,
            @QueryParam("userAccessCode") String userAccessCode,
            @QueryParam("subscriptionAccessCode") String subscriptionAccessCode,
            @QueryParam("domain") String domain)
            throws WloggerException {
        final SuccessLogin login = this.userOrSubscriberAccessCodeLogin(userAccessCode, subscriptionAccessCode);
        final String language =
                ofNullable(login.getUser().getLanguage()).orElse(wloggerConfig.defaultLanguage());
        final String type = ofNullable(subscriptionAccessCode).map(__ -> "portal").orElse("standalone");
        final URI redirectionUri = buildGuiUri(
                uriInfo.getAbsolutePath(),
                login.getScope().getId(),
                type,
                login.getSessionToken(),
                language);
        logger.log(Level.FINE, "Redirecting to %s", redirectionUri);
        return Response.temporaryRedirect(redirectionUri).build();
    }

    private SuccessLogin userOrSubscriberAccessCodeLogin(String userAccessCode, String subscriptionAccessCode) throws WloggerException {
        if (isNotBlank(userAccessCode)) {
            return this.authenticationService.userAccessCodeLogin(userAccessCode);
        } else if (isNotBlank(subscriptionAccessCode)) {
            return this.authenticationService.subscriptionAccessCodeLogin(subscriptionAccessCode);
        } else {
            throw WloggerException.applicationError(Errors.AUTH_MISSING_ACCESS_CODE);
        }
    }
}
