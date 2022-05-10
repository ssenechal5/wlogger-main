package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.auth.*;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.controller.input.SearchInput;
import com.actility.thingpark.wlogger.dto.Disclaimer;
import com.actility.thingpark.wlogger.dto.ElementAuth;
import com.actility.thingpark.wlogger.dto.ElementCustomerId;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wlogger.response.ResponseFactory;
import com.actility.thingpark.wlogger.service.AccessCodeService;
import com.actility.thingpark.wlogger.service.DataService;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/users")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@RequestScoped
public class UsersController {

    private DataService dataService;
    private AuthenticationService authenticationService;
    private AccessCodeService accessCodeService;
    private Store store;
    private WloggerConfig wloggerConfig;

    private static final Logger logger = Logger.getLogger(UsersController.class.getName());

    @Inject
    void inject(
            final DataService dataService,
            final AuthenticationService authenticationService,
            final AccessCodeService accessCodeService,
            final Store store,
            final WloggerConfig wloggerConfig) {
        this.dataService = dataService;
        this.authenticationService = authenticationService;
        this.store = store;
        this.accessCodeService = accessCodeService;
        this.wloggerConfig = wloggerConfig;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public ResponseSimple getUsers(
            @Context HttpServletRequest request,
            @Context UriInfo uriInfo ){

        Optional<WloggerSession> wloggerSession = this.store.getSession();
        return ResponseFactory.createSuccessResponseData()
                .withData(buildElementAuth(wloggerSession.get().getFirstScope(), wloggerSession.get().getSessionToken(),
                        wloggerSession.get().getAuthenticatedUser(),
                        uriInfo));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple getAllUsers(
            @Context HttpServletRequest request,
            @Context UriInfo uriInfo ){
        return getUsers(request,uriInfo);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple loginApi(
            @Context HttpServletRequest request,
            @Context UriInfo uriInfo,
            @QueryParam("login") String login,
            @QueryParam("password") String password,
            @QueryParam("appAccessCode") String appAccessCode,
            @QueryParam("subscriptionAccessCode") String subscriptionAccessCode,
            @QueryParam("username") String username,
            @QueryParam("domain") String domain)
            throws WloggerException {
        if (appAccessCode != null) {
            return buildLoginResponse(
                    this.authenticationService.internalAccessCodeLogin(appAccessCode, null),
                    uriInfo);
        } else if (subscriptionAccessCode != null) {
            return buildLoginResponse(
                    this.authenticationService.subscriptionAccessCodeLogin(subscriptionAccessCode),
                    uriInfo
            );
        } else if (login != null && password != null) {
            return buildLoginResponse(
                    this.authenticationService.endUserLogin(login, password),
                    uriInfo
            );
        } else if (username != null && password != null) {
            return buildLoginResponse(
                    this.authenticationService.superAdminLogin(
                            username, password),
                    uriInfo
            );
        } else {
            throw WloggerException.applicationError(Errors.AUTH_MISSING_ACCESS_CODE);
        }

    }

    private static ElementAuth buildElementAuth(Scope scope, String sessionToken, User user, UriInfo uriInfo) {
        return new ElementAuth(
                scope.getId(),
                // Bug Network Survey "/rest/users/" => "/thingpark/wlogger/rest/users/"
                buildUserHref(uriInfo, scope.getId()),
                singletonList(new ElementCustomerId(scope.getCustomerId())),
                sessionToken,
                new Disclaimer(
                        user.getFirstName() + " " + user.getLastName(),
                        null,
                        null,
                        0,
                        "",
                        false));
    }

    private static ResponseSimple buildLoginResponse(SuccessLogin loginContext, UriInfo uriInfo) {
        return ResponseFactory.createSuccessResponseData()
                .withData(
                        buildElementAuth(loginContext.getScope(), loginContext.getSessionToken(),
                                loginContext.getUser(), uriInfo)
                );
    }

    static String buildUserHref(UriInfo uriInfo, String id) {
        return uriInfo
                .getBaseUriBuilder()
                .path(UsersController.class)
                .path(UsersController.class, "getUser")
                .build(id)
                .getPath();
    }

    @POST
    @Path("/logingui")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple loginGUI(
            @Context UriInfo uriInfo,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("login") String login,
            @FormParam("appAccessCode") String appAccessCode,
            @FormParam("domain") String domain)
            throws WloggerException {

        login = defaultIfNull(username, login);
        if (isNotBlank(login)) {
            return buildLoginResponse(
                    this.authenticationService.superAdminLogin(login, password),
                    uriInfo
            );
        } else if (isNotBlank(appAccessCode)) {
            return buildLoginResponse(
                    this.authenticationService.appAccessCodeLogin(appAccessCode),
                    uriInfo
            );
        } else {
            throw WloggerException.notLoggedIn401(
                    Errors.AUTH_MISSING_ACCESS_CODE, "wrong login or password");
        }
    }


    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple logout() {
        authenticationService.endSession();
        return ResponseFactory.createSuccessResponse();
    }

    @GET
    @Path("/logout")
    public Response logoutGet() {
        authenticationService.endSession();
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/appaccesscode/generate")
    @Produces(MediaType.APPLICATION_JSON)
//    @Deprecated
    public ResponseSimple generateAppAccessCode(
            @Context HttpServletRequest request, @QueryParam("sid") String subscriberId)
            throws WloggerException {
        if (!ProfileManager.getActiveProfile().equals("dev")) {
            throw WloggerException.applicationError(Errors.EXCEPTION_BAD_REQUEST, "Request not allowed");
        }
        String code = accessCodeService.generateSubscriberAccessCode(subscriberId);
        return ResponseFactory.createSuccessResponseData()
                .withData(
                        new ElementAuth(
                                subscriberId,
                                request.getContextPath() + "/rest/users/" + subscriberId,
                                singletonList(new ElementCustomerId(subscriberId)),
                                code,
                                null));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getUser(@PathParam("id") String id) {
        throw new NotFoundException();
    }

    @POST
    @Path("/{id}/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple logoutGetById(@PathParam("id") String id) {
        return logout();
    }

    @GET
    @Path("/{id}/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseSimple logoutGetByIdGet() {
        return logout();
    }

    @POST
    @Path("/{id}/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public ResponseSimple authenticateById(
            @PathParam("id") String userId,
            @Context UriInfo uriInfo) throws WloggerException {
        Scope scope = checkSession(userId);
        Optional<WloggerSession> wloggerSession = this.store.getSession();
        return ResponseFactory.createSuccessResponseData()
                .withData(
                        buildElementAuth(scope, wloggerSession.get().getSessionToken(), wloggerSession.get().getAuthenticatedUser(), uriInfo)
                );
    }

    @GET
    @Path("/{id}/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public ResponseSimple dataById(@PathParam("id") String userId,
                                   @BeanParam SearchInput searchInput,
                                   @HeaderParam("X-Realm-ID")String realmId) throws WloggerException {
        Scope scope = checkSession(userId);
        return this.dataService.data(
                applyScope(
                        builderSearch(searchInput)
                                .withFromDate(searchInput.getFromDate())
                                .withToDate(searchInput.getToDate()),
                        scope)
                        .build(),
                getSubscriberId(scope),realmId
        );
    }

    @GET
    @Path("/{id}/extract")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public ResponseSimple extractById(
            @PathParam("id") String userId,
            @BeanParam SearchInput searchInput,
            @HeaderParam("X-Realm-ID")String realmId) throws WloggerException {
        Scope scope = checkSession(userId);
        return this.dataService.extract(
                applyScope(builderSearch(searchInput)
                                .withDirection(searchInput.getDirection())
                                .withFromDate(searchInput.getFromDate())
                                .withToDate(searchInput.getToDate()),
                        scope)
                        .build(),
                getSubscriberId(scope), realmId
        );
    }

    @GET
    @Path("/{id}/export")
    @Produces(MediaType.TEXT_PLAIN)
    @Authenticated
    // @Produces("text/csv")
    public Response exportById(
            @PathParam("id") String userId,
            @BeanParam SearchInput searchInput,
            @HeaderParam("X-Realm-ID")String realmId) throws WloggerException {
        Scope scope = checkSession(userId);
        return this.dataService.export(
                applyScope(
                        builderSearch(searchInput)
                                .withFromDate(searchInput.getFromDate())
                                .withToDate(searchInput.getToDate())
                        , scope)
                        .build(),
                getSubscriberId(scope), realmId
        );
    }

    @GET
    @Path("/{id}/devices/{devID}/locations")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public ResponseSimple devicesLocationsById(
            @PathParam("id") String userId,
            @PathParam("devID") String devID,
            @BeanParam SearchInput searchInput,
            @HeaderParam("X-Realm-ID")String realmId) throws WloggerException {
        Scope scope = checkSession(userId);
        return this.dataService.devicesLocations(
                applyScope(
                        builderSearch(searchInput)
                                .withStartUid(searchInput.getStartUid())
                                .withEndUid(searchInput.getEndUid())
                                .withDeviceIDs(Collections.singletonList(devID)),
                        scope).build(),
                getSubscriberId(scope), realmId
        );
    }

    private Scope checkSession(String userId) throws WloggerException {
        Optional<WloggerSession> wloggerSession = this.store.getSession();
        Scope scope = wloggerSession.get().getScope(userId);
        if (scope == null) {
            throw WloggerException.notLoggedIn401(Errors.UNKNOWN_ID, "Account not found in session");
        }
        return scope;
    }

    private Search.Builder applyScope(Search.Builder builder, Scope scope) throws WloggerException {
//        logger.warning("Scope restrictions : " +
//                (scope.getCustomerRestrictions() != null && scope.getCustomerRestrictions().getAnd() != null ?
//                        scope.getCustomerRestrictions().getAnd().stream().map(and -> and.getGroup().getName() + ":" + and.getName()).collect(Collectors.joining(",")) : "NO"));
//        logger.warning("Scope type : " + scope.getScopeType());
        switch (scope.getScopeType()) {
            case UNLIMITED:
                return builder.withDomainRestrictions(scope.getCustomerRestrictions());   // for test admin local
            case SUBSCRIBER:
                return builder.withSubscriberID(scope.getContextId()).withSubscriberViewRoamingInTraffic(this.wloggerConfig.subscriberViewRoamingInTraffic()).withDomainRestrictions(scope.getCustomerRestrictions());
            case NETWORK_PARTNER:
                return builder.withNetPartId(scope.getContextId());
            default:
                throw WloggerException.notLoggedIn401(Errors.UNKNOWN_ID, "Unknown scope type");
        }
    }

    private String getSubscriberId(Scope scope) {
        switch (scope.getScopeType()) {
            case SUBSCRIBER:
                return scope.getContextId();
            default:
                return "";
        }
    }

    private Search.Builder builderSearch(SearchInput searchInput) {
        return Search.builder()
                .withLast(searchInput.getLast())
                .withDecoder(searchInput.getDecoder())
                .withSubtype(searchInput.getSubtype())
                .withLRRID(searchInput.getLRRID())
                .withLRCID(searchInput.getLRCID())
                .withDevADDRs(searchInput.getDevADDRs())
                .withDeviceIDs(searchInput.getDeviceIDs())
                .withType(defaultIfNull(searchInput.getType(), DeviceType.LORA))
                .withPageIndex(searchInput.getPageIndex())
                .withAsID(searchInput.getAsID())
                .withSubscriberID(searchInput.getSubscriberID());
    }
}
