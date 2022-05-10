package com.actility.thingpark.wlogger.http;

import com.actility.thingpark.rest.dto.ErrorTO;
import com.actility.thingpark.smp.rest.dto.admin.LoginDto;
import com.actility.thingpark.smp.rest.dto.admin.UserAccountDto;
import com.actility.thingpark.smp.rest.dto.application.HrefDto;
import com.actility.thingpark.smp.rest.dto.application.SubscriptionDto;
import com.actility.thingpark.smp.rest.dto.application.UserContextsDto;
import com.actility.thingpark.smp.rest.dto.application.UserDto;
import com.actility.thingpark.smp.rest.dto.system.AuthenticateSystemDto;
import com.actility.thingpark.smp.rest.dto.system.OperatorsDto;
import com.actility.thingpark.smp.rest.dto.user.AccessCodeDto;
import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.config.SmpClientConfig;
import com.actility.thingpark.wlogger.exception.SmpClientAuthException;
import com.actility.thingpark.wlogger.exception.SmpException;
import com.actility.thingpark.wlogger.utils.XMLUtils;
import io.quarkus.arc.DefaultBean;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@DefaultBean
public class SmpClientImpl implements SmpClient {

  private static final String PATH_SMP_REST = "thingpark/smp/rest";
  private static final String PATH_APPLICATIONS_ID = PATH_SMP_REST + "/applications/id";
  private static final String PATH_SUB_USERS_AUTH = "subscriptions/users/authenticate";
  private static final String PATH_SUB_USERS_OIDC = "subscriptions/oidcUsers";
  private static final String PATH_SUB_AUTH = "subscriptions/authenticate";
  private static final String PATH_APP_AUTH = "applications/authenticate";
  private static final String PATH_CONTEXTS = "/contexts";
  private static final String USERS = PATH_SMP_REST + "/users";

  private static final String PATH_SYSTEMS_1_OPERATORS = PATH_SMP_REST + "/systems/1/operators";
  private static final String PATH_ADMIN_AUTHENTICATE_REST = PATH_SMP_REST + "/admins/login";
  private static final String PATH_SYSTEMS_1_SUBSCRIBERS_AUTH =
      PATH_SMP_REST + "/systems/1/subscribers/users/authenticate";
  private static final String PATH_SYSTEMS_1_MODULES_ADMINS_AUTH =
      PATH_SMP_REST + "/systems/1/modules/admins/authenticate";

  private static final String PARAM_APPLICATION_ID = "applicationID";
  private static final String PARAM_ACCESSCODE = "accessCode";
  private static final String PARAM_ID = "ID";
  private static final String PARAM_SESSION_TOKEN = "sessionToken";
  public static final String MODULE_ID = "moduleID";

  private static final Logger logger = Logger.getLogger(SmpClientImpl.class.getName());
  public static final String FAILED_TO_AUTHENTICATE = "Failed to authenticate";
  public static final String HTTP_ERROR = "HTTP Error: ";
  public static final String HTTP_ERROR1 = "HTTP Error";
  public static final String ERROR = ", ERROR: ";
  public static final String STATUS_CODE = ", STATUS CODE: ";
  public static final String HTTP_AUTH_ERROR = "HTTP AUTH Error: ";

  private final HttpClient httpClient;
  private final SmpClientConfig configuration;

  private String sessionToken;

  @Inject
  public SmpClientImpl(
          HttpClient httpClient,
          SmpClientConfig configuration) {
    this.httpClient = httpClient;
    this.configuration = configuration;
  }

  private UriBuilder newSmpUriBuilder() {
    return UriBuilder.fromUri(this.configuration.uri());
  }

  private URI adminLoginUri() {
    return newSmpUriBuilder().path(PATH_ADMIN_AUTHENTICATE_REST).build();
  }

  private LoginDto adminLogin() throws SmpException, SmpClientAuthException {
    LoginDto loginDtoInput =
            new LoginDto()
                    .withLogin(this.configuration.login())
                    .withPassword(this.configuration.password().toString());
    try {
      return this.httpPost(
          adminLoginUri(),
          // this.objectFactory.createLogin(loginDtoInput),
          loginDtoInput,
          LoginDto.class);
    } catch (SmpException e) {
      logger.log(Level.SEVERE, "Cannot authent to SMP with admin login/password", e);
      throw e;
    }
  }

  @Override
  public String getApplicationHref(String applicationID) throws SmpException {
    URI uri = newSmpUriBuilder().path(PATH_APPLICATIONS_ID)
            .queryParam(PARAM_APPLICATION_ID, applicationID)
            .build();
    return this.get(uri, HrefDto.class).getHref();
  }

  @Override
  public UserDto getUserInfos(String userHref) throws SmpException {
    return this.get(newSmpUriBuilder().clone().path(userHref).build(), UserDto.class);
  }

  @Override
  public UserContextsDto getUserContexts(String userHref) throws SmpException {
    return this.get(newSmpUriBuilder().path(userHref).path(PATH_CONTEXTS).build(),
            UserContextsDto.class);
  }

  protected URI authenticateForThirdAppURI(String accessCode, String applicationID)
      throws SmpException {
    return newSmpUriBuilder()
            .clone()
            .path(this.getApplicationHref(applicationID))
            .path(PATH_APP_AUTH)
            .queryParam(PARAM_ACCESSCODE, accessCode)
            .build();
  }

  @Override
  public com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateForThirdApp(
      String accessCode, String applicationID) throws SmpException {
    return this.get(
        authenticateForThirdAppURI(accessCode, applicationID),
        com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto.class);
  }

  protected URI authenticateBySubscriptionURI(String accessCode, String applicationID)
      throws SmpException {
    return newSmpUriBuilder()
            .clone()
            .path(this.getApplicationHref(applicationID))
            .path(PATH_SUB_AUTH)
            .queryParam(PARAM_ACCESSCODE, accessCode)
            .build();
  }

  @Override
  public com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateBySubscription(
      String accessCode, String applicationID) throws SmpException {
    return this.get(
        authenticateBySubscriptionURI(accessCode, applicationID),
        com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto.class);
  }

  private URI authenticateUserURI(String accessCode, String applicationID) throws SmpException {
    return newSmpUriBuilder()
            .clone()
            .path(this.getApplicationHref(applicationID))
            .path(PATH_SUB_USERS_AUTH)
            .queryParam(PARAM_ACCESSCODE, accessCode)
            .build();
  }

  private URI authenticateUserOidcURI(String oidcUserId, String applicationID) throws SmpException {
    return newSmpUriBuilder()
            .clone()
            .path(this.getApplicationHref(applicationID))
            .path(PATH_SUB_USERS_OIDC)
            .path("{1}")
            .build(oidcUserId);
  }

  @Override
  public com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateUserByOidc(
          String oidcUserId, String applicationID) throws SmpException {
    return this.get(
            authenticateUserOidcURI(oidcUserId, applicationID),
            com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto.class);
  }

  @Override
  public com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto authenticateUser(
      String accessCode, String applicationID) throws SmpException {
    return this.get(
        authenticateUserURI(accessCode, applicationID),
        com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto.class);
  }

  private URI authenticateUserURI() {
    return newSmpUriBuilder()
            .path(PATH_SYSTEMS_1_SUBSCRIBERS_AUTH)
            .build();
  }

  @Override
  public AuthenticateSystemDto authenticateUser(AuthenticateSystemDto authenticateDto) throws SmpException {
    try {
      return this.post(
          authenticateUserURI(),
          authenticateDto,
          AuthenticateSystemDto.class);
    } catch (SmpException e) {
      logger.log(Level.SEVERE,
          "Cannot authent to SMP by API with user login/password : " + e.getMessage());
      throw e;
    }
  }

  private URI getAuthAdminUserUriBuilder(String accessCode, String moduleID) {
    return newSmpUriBuilder()
            .path(PATH_SYSTEMS_1_MODULES_ADMINS_AUTH)
            .queryParam(PARAM_ACCESSCODE, accessCode)
            .queryParam(MODULE_ID, moduleID)
            .build();
  }

  @Override
  public AuthenticateSystemDto authenticateAdminUser(String adminAccessCode, String moduleID)
      throws SmpException {
    return this.get(getAuthAdminUserUriBuilder(adminAccessCode, moduleID), AuthenticateSystemDto.class);
  }

  private URI getOperatorsURI(String id) {
    return newSmpUriBuilder()
            .path(PATH_SYSTEMS_1_OPERATORS)
            .queryParam(PARAM_ID, id)
            .build();
  }

  @Override
  public OperatorsDto getOperators(String id) throws SmpException {
    return this.get(getOperatorsURI(id), OperatorsDto.class);
  }

  @Override
  public SubscriptionDto getApplicationSubscription(String href) throws SmpException {
    return this.get(
            newSmpUriBuilder().path(href).build(), SubscriptionDto.class);
  }

  @Override
  public UserAccountDto getAdminUserNames(String userHref) throws SmpException {
    return this.get(
            newSmpUriBuilder().path(userHref).build(), UserAccountDto.class);
  }

  private URI getAccessCodeUserByApiUriBuilder(String thingParkID) {
    return newSmpUriBuilder().path(USERS).path(thingParkID).path(PARAM_ACCESSCODE).build();
  }

  @Override
  public AccessCodeDto getAccessCodeUserByApi(String thingParkID, AccessCodeDto accessCodeDto) throws SmpException {
    try {
      return this.post(getAccessCodeUserByApiUriBuilder(thingParkID),
            accessCodeDto,
            AccessCodeDto.class);
    } catch (SmpException e) {
      logger.log(Level.SEVERE, "Cannot authent to SMP by API with user ID : " + e.getMessage());
      throw e;
    }
  }

  private String createSession() throws SmpException, SmpClientAuthException {
    LoginDto loginDto = adminLogin();
    String token = loginDto.getSessionToken();
    if (token == null){
      throw new SmpException("No session token found in SMP admin login response");
    }
    return token;
  }

  private void clearSession(){
    this.sessionToken = null;
  }

  private URI appendSessionToken(URI uri) throws SmpException, SmpClientAuthException {
    if(sessionToken == null){
      sessionToken = createSession();
    }
    return UriBuilder.fromUri(uri).replaceQueryParam(PARAM_SESSION_TOKEN, this.sessionToken).build();
  }

  private <E, T> T get(URI uri, Class<T> entity) throws SmpException {
    try {
      return this.getThroughAuth(uri, entity);
    } catch (SmpClientAuthException firstAttemptException) {
      logger.log(Level.INFO, "Session token not valid : Retrieving new session token from SMP", firstAttemptException);
      clearSession();
      try {
        return this.getThroughAuth(uri, entity);
      } catch (SmpClientAuthException retryAttemptException) {
        logger.log(Level.SEVERE, FAILED_TO_AUTHENTICATE, retryAttemptException);
        throw new SmpException(FAILED_TO_AUTHENTICATE, retryAttemptException);
      }
    }
  }

  private <T> T getThroughAuth(URI uri, Class<T> entity)
      throws SmpException, SmpClientAuthException {
    URI uriWithToken = appendSessionToken(uri);
    logger.fine("Sending GET request to SMP: " + uriWithToken);
    return this.httpGet(uriWithToken, entity);
  }

  private <E, T> T post(URI uri, E body, Class<T> entity) throws SmpException {
    try {
      return this.postThroughAuth(uri, body, entity);
    } catch (SmpClientAuthException firstAttemptException) {
      logger.log(Level.INFO, "Session token not valid : Retrieving new session token from SMP", firstAttemptException);
      clearSession();
      try {
        return this.postThroughAuth(uri, body, entity);
      } catch (SmpClientAuthException retryAttemptException) {
        logger.log(Level.SEVERE, FAILED_TO_AUTHENTICATE, retryAttemptException);
        throw new SmpException(FAILED_TO_AUTHENTICATE, retryAttemptException);
      }
    }
  }

  private <E, T> T postThroughAuth(URI uri, E body, Class<T> entity)
      throws SmpException, SmpClientAuthException {
    URI uriWithToken = appendSessionToken(uri);
    logger.fine("Sending POST request to SMP: " + uriWithToken);

    return this.httpPost(
              uriWithToken,
              body,
              entity);
  }

  private <T> T httpGet(URI uri, Class<T> entity) throws SmpException, SmpClientAuthException {
    try {
      return readEntity(this.httpClient.get(uri), entity);
    } catch (IOException e) {
      logger.fine(HTTP_ERROR + e.getMessage());
      throw new SmpException(HTTP_ERROR1, e);
    }
  }

  private <E, T> T httpPost(URI uri, E body, Class<T> entity) throws SmpException, SmpClientAuthException {
    try {
      final byte[] xmlBody = XMLUtils.writeSmpObject(body);
      return readEntity(
              this.httpClient.post(
                      uri,
                      new ByteArrayEntity(xmlBody, ContentType.APPLICATION_XML)),
              entity);
    } catch (IOException e) {
      logger.fine(HTTP_ERROR + e.getMessage());
      throw new SmpException(HTTP_ERROR1, e);
    } catch (JAXBException e) {
      logger.fine("Serialization error: " + e.getMessage());
      throw new SmpException("Serialization error", e);
    }
  }

  public static <T> T readEntity(Pair<HttpResponse, byte[]> response, Class<T> entity)
          throws SmpException, SmpClientAuthException {
    try {
      if (response.getLeft().getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
        String entityResponse = new String(response.getRight());
        return XMLUtils.convertToSmpObject(entityResponse, entity);
      } else if (response.getLeft().getStatusLine().getStatusCode() == Response.Status.FORBIDDEN.getStatusCode()) {
        if (response.getLeft().getEntity() != null
                && response.getLeft().getEntity().getContentType() != null) {
          String contentType = response.getLeft().getEntity().getContentType().getValue();
          if (MediaType.APPLICATION_XML.equals(contentType)) {
            String entityResponse = new String(response.getRight());
            ErrorTO error = XMLUtils.convertToSmpObject(entityResponse, ErrorTO.class);
            logger.fine("AUTH Error: " + error.getCode()
                            + ERROR + error.getMessage()
                            + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
            throw new SmpClientAuthException(error.getCode(), error.getMessage());
          }
        }
        logger.fine(HTTP_AUTH_ERROR + response.getLeft().getStatusLine()
                        + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
        throw new SmpClientAuthException(Errors.EXCEPTION,
                HTTP_AUTH_ERROR + response.getLeft().getStatusLine()
                        + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
      } else {
        if (response.getLeft().getEntity() != null && response.getLeft().getEntity().getContentType() != null) {
          String contentType = response.getLeft().getEntity().getContentType().getValue();
          if (MediaType.APPLICATION_XML.equals(contentType)) {
            String entityResponse = new String(response.getRight());
            ErrorTO error = XMLUtils.convertToSmpObject(entityResponse, ErrorTO.class);
            logger.fine(HTTP_ERROR + response.getLeft().getStatusLine()
                            + ERROR + error.getMessage()
                            + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
            throw new SmpException(HTTP_ERROR + response.getLeft().getStatusLine()
                            + ERROR + error.getMessage()
                            + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
          }
        }
      }
      logger.fine(HTTP_ERROR + response.getLeft().getStatusLine()
                      + STATUS_CODE + response.getLeft().getStatusLine());
      throw new SmpException(HTTP_ERROR + response.getLeft().getStatusLine()
                      + STATUS_CODE + response.getLeft().getStatusLine().getStatusCode());
    } catch (UnsupportedOperationException | JAXBException e) {
      logger.fine(HTTP_ERROR + response.getLeft().getStatusLine()
                      + ", [UnsupportedOperationException or JAXBException] ERROR: " + e.getMessage());
      throw new SmpException(HTTP_ERROR + response.getLeft().getStatusLine()
                      + ", [UnsupportedOperationException or JAXBException] ERROR: " + e.getMessage(), e);
    }
  }

}
