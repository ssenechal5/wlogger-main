package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.smp.rest.dto.DomainRestrictionsDto;
import com.actility.thingpark.smp.rest.dto.admin.UserAccountDto;
import com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto;
import com.actility.thingpark.smp.rest.dto.application.UserContextsDto;
import com.actility.thingpark.smp.rest.dto.application.UserDto;
import com.actility.thingpark.smp.rest.dto.system.AuthenticateSystemDto;
import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.accesscode.AccessCodePayload;
import com.actility.thingpark.wlogger.accesscode.AccessCodeValidationException;
import com.actility.thingpark.wlogger.accesscode.DecodeAccessCodeException;
import com.actility.thingpark.wlogger.config.SmpConfig;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.dao.SubscriptionDAOLocal;
import com.actility.thingpark.wlogger.entity.ActorEntity;
import com.actility.thingpark.wlogger.entity.Subscription;
import com.actility.thingpark.wlogger.exception.SmpException;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.http.SmpClient;
import com.actility.thingpark.wlogger.service.AccessCodeService;
import com.actility.thingpark.wlogger.service.AdminService;
import com.actility.thingpark.wlogger.utils.IDUtils;
import com.google.common.base.Objects;
import io.quarkus.runtime.configuration.ProfileManager;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthenticationService {

    private final static int HTTPS_DEFAULT_PORT=443;

    private final Store store;

    private final SmpClient smpClient;

    private final SmpConfig smpConfig;

    private final AdminService adminService;

    private final WloggerConfig wloggerConfig;

    private final AccessCodeService accessCodeService;

    private final SubscriptionDAOLocal subscriptionDao;

    private final KeycloakDeploymentManager keycloakDeploymentManager;

    private final TokenService tokenService;

    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    @Inject
    AuthenticationService(
            SubscriptionDAOLocal subscriptionDao,
            Store store,
            SmpClient smpClient,
            SmpConfig smpConfig,
            AdminService adminService,
            WloggerConfig wloggerConfig,
            AccessCodeService accessCodeService,
            KeycloakDeploymentManager keycloakDeploymentManager,
            TokenService tokenService) {
        this.subscriptionDao = subscriptionDao;
        this.store = store;
        this.smpClient = smpClient;
        this.smpConfig = smpConfig;
        this.adminService = adminService;
        this.wloggerConfig = wloggerConfig;
        this.accessCodeService = accessCodeService;
        this.keycloakDeploymentManager = keycloakDeploymentManager;
        this.tokenService = tokenService;
    }

    /**
     * Check OAuth access token validity and store user information (retrieved from SMP) into user session (if missing)
     *
     * @param authHeader Authorization header
     * @return
     * @throws WloggerException
     */
    public SuccessLogin oauthAuthentication(String authHeader, ContainerRequestContext request) throws WloggerException {
        // Do some check on request and validate Access Token
        AccessToken accessToken = doOauthAuthentication(authHeader, request);

        String oidcUserId = accessToken.getSubject();

        Optional<WloggerSession> wloggerSession = getWloggerSession();
        User user = wloggerSession.map(WloggerSession::getAuthenticatedUser).orElse(null);

        // Save user info in session (if info is not present or info is from another user)
        if (user == null || user.getOidcUserID() == null || !user.getOidcUserID().equals(oidcUserId)) {
            try {
                if (ProfileManager.getActiveProfile().equals("dev")) {
//                if (ProfileManager.getActiveProfile().equals("prod")) {
//                    String appAccessCode = this.accessCodeService.generateSubscriberAccessCode("199906997");
                    String appAccessCode = this.accessCodeService.generateSubscriberAccessCode("199983788");
                    return internalAccessCodeLogin(appAccessCode, oidcUserId);
                } else {
                    // Retrieve user identifier
                    AuthenticateApplicationDto authenticateDto = this.smpClient.authenticateUserByOidc(oidcUserId, smpConfig.getApplicationID());

                    String userHref = authenticateDto.getUser().getHref();
                    UserDto userDto = this.smpClient.getUserInfos(userHref);
                    String subscriptionHref = null;
                    UserContextsDto contexts = this.smpClient.getUserContexts(userHref);
                    for (UserContextsDto.Briefs.Brief brief : contexts.getBriefs().getBrief()) {
                        subscriptionHref = brief.getHref();

                    }
                    if (subscriptionHref == null) {
                        throw WloggerException.applicationError(
                                Errors.AUTH_NO_SMP_SUBSCRIPTION_FOR_HREF_FOUND, subscriptionHref);
                    }
                    Subscription subscription = checkActiveSubscription(null, subscriptionHref);

                    DomainRestrictionsDto restrictions = null;
                    if (contexts.getBriefs().getBrief() != null && contexts.getBriefs().getBrief().get(0) != null) {
                        restrictions = contexts.getBriefs().getBrief().get(0).getDomainRestrictions();
                    }
                    return loginEndUser(userDto, userHref, subscription.getID(), oidcUserId, restrictions);
                }
            } catch (SmpException e) {
                throw WloggerException.forbidden(Errors.AUTH_NO_SMP_SUBSCRIPTION_FOR_HREF_FOUND, smpConfig.getApplicationID());
            }
        }
        return null;
    }

    private AccessToken doOauthAuthentication(String authHeader, ContainerRequestContext request) throws WloggerException {
        // Check presence of X-Realm-ID header
        String realm = request.getHeaderString("X-Realm-ID");
        if (realm == null) {
            logger.warning("Missing X-Realm-ID");
            throw WloggerException.badRequest(Errors.AUTH_FAILED, "Missing X-Realm-ID");
        }

        // Retrieve KeycloakDeployment instance
        KeycloakDeployment deployment = keycloakDeploymentManager.
                getKeycloakDeployment(request.getUriInfo(), realm);

        // Check if SSL is required
        // checkSslRequired(deployment);

        // Check Access Token
        return authorizeAccessToken(authHeader, deployment);
    }

    /**
     * Validate OAuth access token
     * @param authHeader Authorization header
     * @param deployment KeycloakDeployment instance
     * @return access token if valid
     * @throws WloggerException if token is invalid
     */
    private AccessToken authorizeAccessToken(String authHeader, KeycloakDeployment deployment) throws WloggerException {
        String[] splits = authHeader.split("Bearer");
        if (splits.length != 2) {
            throw WloggerException.badRequest(Errors.AUTH_FAILED, "Invalid Authorization header");
        }
        String tokenString = splits[1].trim();
        return tokenService.verifyToken(tokenString, deployment);
    }

    private static User buildUser(UserDto userDto, String oicdUserID) {
        return new User(
                UserType.SMP_END_USER,
                userDto.getThingparkID(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getLanguage(),
                oicdUserID);
    }

    private static User buildUser(UserAccountDto userDto) {
        return new User(
                UserType.SMP_ADMIN_USER,
                userDto.getThingparkID(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getLanguage(),
                null);
    }

    private static String extractLastSegment(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public SuccessLogin userAccessCodeLogin(String code) throws WloggerException {
        try {
            AuthenticateApplicationDto auth = this.smpClient.authenticateUser(code, smpConfig.getApplicationID());
            String userHref = auth.getUser().getHref();
            UserDto userDto = this.smpClient.getUserInfos(userHref);
            String subscriptionHref = null;
            UserContextsDto contexts = this.smpClient.getUserContexts(userHref);
            for (UserContextsDto.Briefs.Brief brief : contexts.getBriefs().getBrief()) {
                subscriptionHref = brief.getHref();
            }
            if (subscriptionHref == null) {
                throw WloggerException.applicationError(Errors.AUTH_NO_SMP_SUBSCRIPTION_FOR_HREF_FOUND, subscriptionHref);
            }
            Subscription subscription = checkActiveSubscription(null , subscriptionHref);

            DomainRestrictionsDto restrictions = null;
            if (contexts.getBriefs().getBrief() != null && contexts.getBriefs().getBrief().get(0) != null) {
                restrictions = contexts.getBriefs().getBrief().get(0).getDomainRestrictions();
            }
            return loginEndUser(userDto, userHref, subscription.getID(), null, restrictions);
        } catch (SmpException e) {
            throw WloggerException.applicationError(e, Errors.AUTH_SMP_FAILED);
        }
    }

    public SuccessLogin subscriptionAccessCodeLogin(String code) throws WloggerException {
        try {
            AuthenticateApplicationDto auth = this.smpClient.authenticateBySubscription(code, smpConfig.getApplicationID());
            String userHref = auth.getUser().getHref();
            UserDto userDto = this.smpClient.getUserInfos(userHref);
            return loginEndUser(
                userDto, userHref, auth.getSubscriber().getID(), null, auth.getDomainRestrictions());
        } catch (SmpException e) {
            throw WloggerException.applicationError(e, Errors.AUTH_SMP_FAILED);
        }
    }

    public SuccessLogin appAccessCodeLogin(String code) throws WloggerException {
        try {
            AuthenticateApplicationDto auth = this.smpClient.authenticateForThirdApp(code, smpConfig.getApplicationID());
            String userHref = auth.getUser().getHref();
            UserDto userDto = this.smpClient.getUserInfos(userHref);
            Subscription subscription = checkActiveSubscription(auth.getSubscriber().getID(), null);
            return loginEndUser(userDto, userHref, subscription.getID(), null, auth.getDomainRestrictions());
        } catch (SmpException e) {
            throw WloggerException.applicationError(e, Errors.AUTH_SMP_FAILED);
        }
    }

    // This should be removed, user shoud user user access code to login
    public SuccessLogin endUserLogin(String login, String password) throws WloggerException {
        try {
            AuthenticateSystemDto auth =
                    this.smpClient.authenticateUser(
                            new AuthenticateSystemDto()
                                    .withLogin(login)
                                    .withPassword(password));
            com.actility.thingpark.smp.rest.dto.user.AccessCodeDto accessCodeDto = new com.actility.thingpark.smp.rest.dto.user.AccessCodeDto();
            accessCodeDto.withApplicationID(smpConfig.getApplicationID());
            accessCodeDto = this.smpClient.getAccessCodeUserByApi(auth.getThingParkID(), accessCodeDto);
            return userAccessCodeLogin(accessCodeDto.getAccessCode());
        } catch (SmpException e) {
            throw WloggerException.applicationError(e, Errors.AUTH_SMP_FAILED);
        }
    }

    private SuccessLogin adminAccessCodeLogin(String adminAccessCode, String moduleId, AdminAuthenticator authenticator) throws WloggerException {
        try {
            AuthenticateSystemDto auth = this.smpClient.authenticateAdminUser(adminAccessCode, moduleId);
            UserAccountDto userDto = this.smpClient.getAdminUserNames(auth.getAdmin().getHref());
            User user = buildUser(userDto);
            Scope context = authenticator.buildScope(auth);
            return login(user, context);
        } catch (SmpException e) {
            throw WloggerException.applicationError(e, Errors.AUTH_SMP_FAILED_S);
        }
    }

    private Subscription checkActiveSubscription(String subscriberID, String subscriptionHref) throws WloggerException  {
        // check subscription status is active
        Subscription subscription;
        if (subscriberID != null)
            subscription = this.subscriptionDao.findFirstASDSubscriptionByID(subscriberID);
        else
            subscription = this.subscriptionDao.findSubscriptionByHref(subscriptionHref);

        if (subscription == null) {
            throw WloggerException.applicationError(Errors.AUTH_FAILED_NO_SUBSCRIPTION_FOR_ID_FOUND, subscriberID);
        } else if (subscription.getState().equals(ActorEntity.State.DEACTIVATED)) {
            throw WloggerException.applicationError(Errors.AUTH_FAILED_SUBSCRIPTION_DEACTIVATED_FOR_ID, subscriberID);
        } else if (subscription.getState().equals(ActorEntity.State.SUSPENDED)) {
            throw WloggerException.applicationError(Errors.AUTH_FAILED_SUBSCRIPTION_SUSPENDED_FOR_ID, subscriberID);
        }
        return subscription;
    }

    public SuccessLogin supplierAdminAccessCodeLogin(String adminAccessCode) throws WloggerException {
        return adminAccessCodeLogin(adminAccessCode, smpConfig.getSupplierModuleID(),
                auth -> new Scope(
                        extractLastSegment(auth.getSupplier().getHref()),
                        Scope.ScopeType.NETWORK_PARTNER,
                        auth.getSupplier().getID(), null));
    }

    public SuccessLogin subscriberAdminAccessCodeLogin(String adminAccessCode) throws WloggerException {
        return adminAccessCodeLogin(adminAccessCode, smpConfig.getSubscriberModuleID(),
                auth -> new Scope(
                        extractLastSegment(auth.getSubscriber().getID()),
                        Scope.ScopeType.SUBSCRIBER,
                        auth.getSubscriber().getID(), null));
    }

    public SuccessLogin internalAccessCodeLogin(String appAccessCode, String oicdUserID) throws WloggerException {
        AccessCodePayload code;
        try {
            code = accessCodeService.decode(appAccessCode);
        } catch (DecodeAccessCodeException e) {
            throw WloggerException.applicationError(e, Errors.AUTHENTICATION_FAILED, "Invalid Access Code");
        } catch (AccessCodeValidationException e) {
            throw WloggerException.applicationError(e, Errors.AUTHENTICATION_FAILED, e.getMessage());
        }
        String id = code.getID();
        User user = buildInternalUser(id, oicdUserID);
        Scope scope = new Scope(id, Scope.ScopeType.SUBSCRIBER, id, null);
        return login(user, scope);
    }

    public SuccessLogin superAdminLogin(String username, String password) throws WloggerException {
        String id = this.adminService.logAdmin(username, password);
        User user = buildInternalUser(id, null);
//        Scope scope = new Scope(id, Scope.ScopeType.UNLIMITED, "*", null);
        Scope scope = new Scope(id, Scope.ScopeType.UNLIMITED, "*", getTestRestrictions()); // for test admin local
        return login(user, scope);
    }

    // for test admin local
    private DomainRestrictionsDto getTestRestrictions() {
        return null;
/*
        DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
        return customerRestrictions.withAnds(
//                new UserContextsDto.DomainRestrictions.And(
//                        "FULL", "France/Caen", new UserContextsDto.DomainRestrictions.And.Group("Site")),
                new DomainRestrictionDto(
                        DomainTypeDto.PREFIX, "France", new DomainRestrictionDto.Group("Site")),
                new DomainRestrictionDto(
                        DomainTypeDto.FULL, "Energy", new DomainRestrictionDto.Group("Vertical"))
        );
*/
    }

    private User buildInternalUser(String id, String oicdUserID) {
        return new User(
                UserType.INTERNAL_ACCESS_CODE,
                id,
                "API",
                "API",
                this.wloggerConfig.defaultLanguage(),
                oicdUserID);
    }

    private SuccessLogin loginEndUser(UserDto userDto, String userHref, String subscriberId,
                                      String oicdUserID, DomainRestrictionsDto domainRestrictions) {
        User user = buildUser(userDto, oicdUserID);
        Scope context = new Scope(extractLastSegment(userHref), Scope.ScopeType.SUBSCRIBER, subscriberId, domainRestrictions);
        return login(user, context);
    }

    private SuccessLogin login(User user, Scope scope) {
        WloggerSession session = createOrRefreshSession(user, scope);
        return new SuccessLogin(session.getSessionToken(), user, scope);
    }

    private WloggerSession createOrRefreshSession(User user, Scope scope) {
        return store.getSession().
                filter(s -> Objects.equal(s.getAuthenticatedUser().getType(), user.getType()) &&
                        Objects.equal(s.getAuthenticatedUser().getId(), user.getId())).
                map(s -> {
                    s.addScope(scope);
                    return s;
                }).orElseGet(() -> createNewSession(user, scope));
    }

    private WloggerSession createNewSession(User user, Scope scope) {
        WloggerSession session = new WloggerSession(generateSessionToken(), user, scope);
        store.setSession(session);
        return session;
    }

    @Produces
    public WloggerSession getWloggerSessionOrNull() {
        return getWloggerSession().orElse(null);
    }

    public Optional<WloggerSession> getWloggerSession() {
        return store.getSession();
    }

    private static String generateSessionToken() {
        return IDUtils.newID();
    }

    public void endSession() {
        store.clear();
    }

    private interface AdminAuthenticator {
        Scope buildScope(AuthenticateSystemDto auth);
    }

}
