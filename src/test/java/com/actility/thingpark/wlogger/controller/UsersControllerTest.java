package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.MongoDocumentFactory;
import com.actility.thingpark.wlogger.auth.*;
import com.actility.thingpark.wlogger.config.PaginationConfig;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.controller.input.SearchInput;
import com.actility.thingpark.wlogger.dao.DeviceHistoryDao;
import com.actility.thingpark.wlogger.dto.*;
import com.actility.thingpark.wlogger.engine.EngineClient;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wlogger.service.*;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.configuration.ProfileManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersControllerTest {

  private StoreServiceImpl configurationServiceMock;
  private UsersController users;
  private UriInfo uriInfo;
  private DataService dataService;
  private AuthenticationService authenticationServiceMock;
  private AccessCodeService accessCodeServiceMock;

  private DeviceHistoryDao deviceHistoryDaoMock;
  private CSVService csvServiceMock;
  private EngineClient engineMock;
  private DecoderService decoderService;
  private HttpServletRequest requestMock;
  private Store store;
  private WloggerConfig wloggerConfig = new WloggerConfig() {
    @Override
    public String configurationFolder() {
      return null;
    }

    @Override
    public String version() {
      return null;
    }

    @Override
    public String defaultLanguage() {
      return null;
    }

    @Override
    public boolean passiveRoaming() {
      return true;
    }

    @Override
    public boolean lteMode() {
      return false;
    }

    @Override
    public String csvExportDelimiter() {
      return ";";
    }

    @Override
    public String accessCodeSecret() {
      return null;
    }

    @Override
    public boolean adminLogin() {
      return false;
    }

    @Override
    public int adminSessionLifetime() {
      return 0;
    }

    @Override
    public int maxAutomaticDecodedPackets() { return 500; }

    @Override
    public int subscriberViewRoamingInTraffic() {
      return 0;
    }

    @Override
    public int maxInactiveInterval() {
      return 0;
    }

  };
  private PaginationConfig paginationConfig = new PaginationConfig() {
    @Override
    public int pageSize() {
      return 100;
    }

    @Override
    public int maxPages() {
      return 1000;
    }
  };

  private String sessionToken = "sessionToken";
  private Scope context = new Scope("userHref", Scope.ScopeType.SUBSCRIBER, "subscriberId", null);
  private User user = new User(
          UserType.SMP_END_USER,
          "thingparkID",
          "firstName",
          "lastName",
          "language", "oidcUserID");

  private MongoDocumentFactory mongoDocumentFactory;

  @BeforeEach
  void setUp() {
    this.deviceHistoryDaoMock = mock(DeviceHistoryDao.class);
    this.csvServiceMock = mock(CSVService.class);
    this.engineMock = mock(EngineClient.class);
    this.decoderService = new DecoderService(engineMock);
    this.store = mock(Store.class);

    this.configurationServiceMock = mock(StoreServiceImpl.class);
    this.accessCodeServiceMock = mock(AccessCodeService.class);

    this.dataService =
        new DataService(
            this.deviceHistoryDaoMock,
            this.csvServiceMock,
            this.decoderService,
                this.paginationConfig,
                this.wloggerConfig);

    this.authenticationServiceMock = mock(AuthenticationService.class);
    this.users = new UsersController();
    this.users.inject(this.dataService, this.authenticationServiceMock, this.accessCodeServiceMock,this.store, this.wloggerConfig);

    this.uriInfo = mock(UriInfo.class);
    when(uriInfo.getBaseUriBuilder())
              .thenReturn(UriBuilder.fromUri("http://example.com/thingpark/wlogger/rest"));

    this.requestMock = mock(HttpServletRequest.class);
    when(requestMock.getContextPath()).thenReturn("/thingpark");
    SuccessLogin login = new SuccessLogin(sessionToken, user, context);
    when(store.getSession()).thenReturn(java.util.Optional.of(new WloggerSession(
            login.getSessionToken(),
            login.getUser(),
            login.getScope()
    )));
    
    this.mongoDocumentFactory = new MongoDocumentFactory();
  }

  @Test
  public void buildUserHref() {
    when(uriInfo.getBaseUriBuilder())
        .thenReturn(UriBuilder.fromUri("http://example.com/thingpark/wlogger/rest"));
    Assertions.assertEquals("/thingpark/wlogger/rest/users/3", users.buildUserHref(uriInfo, "3"));
  }

  @Test
  void getUser() {
    assertThrows(NotFoundException.class, () -> this.users.getUser("1"));
  }

  @Test
  void getUsers() {
    ResponseData response = (ResponseData) this.users.getUsers(requestMock, uriInfo);
    assertResponseData(response, context.getId());
  }

  @Test
  void getAllUsers() {
    ResponseData response = (ResponseData) this.users.getAllUsers(requestMock, uriInfo);
    assertResponseData(response, context.getId());
  }

  private static Stream<Arguments> provideDataArgumentsAgain() {
    return Stream.of(
            Arguments.of("login", "password", null, null, null, "domain"),
            Arguments.of(null, "password", null, null, "username", "domain"),
            Arguments.of(null, null, "appAccessCode", null, null, "domain"),
            Arguments.of(null, null, null, "subscriptionAccessCode", null, "domain"),
            Arguments.of(null, null, null, null, null, null));
  }

  @ParameterizedTest(name = "run loginApi => (login :{0}, password:{1}, appAccessCode:{2}, subscriptionAccessCode:{3}, username:{4}, domain:{5})")
  @MethodSource("provideDataArgumentsAgain")
  void loginApi(String login, String password,
          String appAccessCode, String subscriptionAccessCode,
          String username, String domain) throws WloggerException {

    if (appAccessCode != null) {
      when(authenticationServiceMock.internalAccessCodeLogin(appAccessCode, null)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginApi(requestMock,uriInfo,login,password,appAccessCode, subscriptionAccessCode, username, domain);
      assertResponseData(response, context.getId());
    } else if (subscriptionAccessCode != null) {
      when(authenticationServiceMock.subscriptionAccessCodeLogin(subscriptionAccessCode)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginApi(requestMock,uriInfo,login,password,appAccessCode, subscriptionAccessCode, username, domain);
      assertResponseData(response, context.getId());
    } else if (login != null && password != null) {
      when(authenticationServiceMock.endUserLogin(login,password)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginApi(requestMock,uriInfo,login,password,appAccessCode, subscriptionAccessCode, username, domain);
      assertResponseData(response, context.getId());
    } else if (username != null && password != null) {
      when(authenticationServiceMock.superAdminLogin(username,password)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginApi(requestMock,uriInfo,login,password,appAccessCode, subscriptionAccessCode, username, domain);
      assertResponseData(response, context.getId());
    } else {
      assertThrows(WloggerException.class, () -> this.users.loginApi(requestMock,uriInfo,login,password,appAccessCode, subscriptionAccessCode, username, domain));
    }
  }

  private static Stream<Arguments> provideDataArgumentsAgain2() {
    return Stream.of(
            Arguments.of("username", "password", null, null, "domain"),
            Arguments.of(null, "password", "login", null, "domain"),
            Arguments.of(null, null, null, "appAccessCode", "domain"),
            Arguments.of(null, null, null, null, null));
  }

  @ParameterizedTest(name = "run loginGUI => (username :{0}, password:{1}, login:{2}, appAccessCode:{3}, domain:{4})")
  @MethodSource("provideDataArgumentsAgain2")
  void loginGUI(String username, String password,
                String login, String appAccessCode,
                String domain) throws WloggerException {

    if (isNotBlank(login) || isNotBlank(username)) {
      when(authenticationServiceMock.superAdminLogin(login,password)).thenReturn(new SuccessLogin(sessionToken,user,context));
      when(authenticationServiceMock.superAdminLogin(username,password)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginGUI(uriInfo,username,password,login, appAccessCode, domain);
      assertResponseData(response, context.getId());
    } else if (isNotBlank(appAccessCode)) {
      when(authenticationServiceMock.appAccessCodeLogin(appAccessCode)).thenReturn(new SuccessLogin(sessionToken,user,context));
      ResponseData response = (ResponseData) this.users.loginGUI(uriInfo,username,password,login, appAccessCode, domain);
      assertResponseData(response, context.getId());
    } else {
      assertThrows(WloggerException.class, () -> this.users.loginGUI(uriInfo,username,password,login, appAccessCode, domain));
    }
  }

  @Test
  void logout() {
    ResponseList response = (ResponseList) this.users.logout();
    assertResponseList(response, 0);
  }

  @Test
  void logoutGet() {
    Response response = this.users.logoutGet();
    assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
  }

  @Test
  void generateAppAccessCodeError() {
    // when(configurationServiceMock.isEnabledDevelopmentAuthentication()).thenReturn(false);
    ProfileManager.setLaunchMode(LaunchMode.TEST);
    assertThrows(WloggerException.class, () -> this.users.generateAppAccessCode(requestMock,context.getContextId()));
  }

  @Test
  void generateAppAccessCode() throws WloggerException {
    // when(configurationServiceMock.isEnabledDevelopmentAuthentication()).thenReturn(true);
    ProfileManager.setLaunchMode(LaunchMode.DEVELOPMENT);
    ResponseData response = (ResponseData) this.users.generateAppAccessCode(requestMock, context.getContextId());
    assertResponseData(response, context.getContextId());
  }

  @Test
  void logoutGetById() {
      ResponseList response = (ResponseList) this.users.logoutGetById("1");
      assertResponseList(response, 0);
  }

  @Test
  void logoutGetByIdGet() {
      ResponseList response = (ResponseList) this.users.logoutGetByIdGet();
      assertResponseList(response, 0);
  }

  @Test
  void authenticateById() throws WloggerException {
      ResponseData response = (ResponseData) this.users.authenticateById(context.getId(), uriInfo);
      assertResponseData(response, context.getId());
  }

  private static Stream<Arguments> provideDataArguments() {
    return Stream.of(
            Arguments.of(DeviceType.LORA, 0),
            Arguments.of(DeviceType.LORA, 1),
            Arguments.of(DeviceType.LORA, 100),
            Arguments.of(DeviceType.LORA, 101),
            Arguments.of(DeviceType.LORA, 125));
  }

  @ParameterizedTest(name = "run dataById => (type :{0}, size:{1})")
  @MethodSource("provideDataArguments")
  void dataById(DeviceType deviceType, int size) throws WloggerException {
    SearchInput searchInput = new SearchInput();

    List<DeviceHistory> deviceHistoryArrayList =  new ArrayList<DeviceHistory>();
    deviceHistoryArrayList.addAll(mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(any(Search.class), any(Integer.class), any(Integer.class))).thenReturn(deviceHistoryArrayList);

    ResponsePaginatedList response = (ResponsePaginatedList) this.users.dataById(context.getId(), searchInput, "X-Realm-ID");

    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertEquals(null,response.getError());
    assertEquals(response.isMore(), (size > 100));
    assertEquals(response.getData().size(), (size > 100 ? 100 : size));

    List<Element> decodedHistories = response.getData();
    if (decodedHistories.size() > 0) {
      String devEUI = deviceHistoryArrayList.get(0).deviceEUI;
      if (deviceType.equals(DeviceType.LTE)) {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistoryLte decodedHistoryLte = (ElementDeviceHistoryLte) decodedHistory;
                  assertEquals(decodedHistoryLte.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLte.getDevLAT());
                  assertEquals("125.0",decodedHistoryLte.getDevLON());
                });
      } else {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistory decodedHistoryLora = (ElementDeviceHistory) decodedHistory;
                  assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLora.getDevLAT());
                  assertEquals("125.0",decodedHistoryLora.getDevLON());
                });
      }
    }
  }

  @ParameterizedTest(name = "run extractById => (type :{0}, size:{1})")
  @MethodSource("provideDataArguments")
  void extractById(DeviceType deviceType, int size) throws WloggerException {
    SearchInput searchInput = new SearchInput();

    List<DeviceHistory> deviceHistoryArrayList =  new ArrayList<DeviceHistory>();
    deviceHistoryArrayList.addAll(mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(any(Search.class), any(Integer.class), any(Integer.class))).thenReturn(deviceHistoryArrayList);

    ResponsePaginatedList response = (ResponsePaginatedList) this.users.extractById(context.getId(), searchInput, "realmId");

    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertEquals(null,response.getError());
    assertEquals(response.isMore(), (size > 100));
    assertEquals(response.getData().size(), (size > 100 ? 100 : size));

    List<Element> decodedHistories = response.getData();
    if (decodedHistories.size() > 0) {
      if (deviceType.equals(DeviceType.LTE)) {
        String devAddr = deviceHistoryArrayList.get(0).deviceAddress;
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistoryLteExtract decodedHistoryLte =
                          (ElementDeviceHistoryLteExtract) decodedHistory;
                  assertEquals(decodedHistoryLte.ipv4, devAddr);
                  if (Direction.UPLINK.getValue() == Integer.parseInt(decodedHistoryLte.getDirection())) {
                    assertEquals("123.0",decodedHistoryLte.getDevLAT());
                    assertEquals("125.0",decodedHistoryLte.getDevLON());
                  } else {
                    assertEquals(decodedHistoryLte.getDevLAT(), null);
                    assertEquals(decodedHistoryLte.getDevLON(), null);
                  }
                });
      } else {
        String devEUI = deviceHistoryArrayList.get(0).deviceEUI;
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistoryExtract decodedHistoryLora = (ElementDeviceHistoryExtract) decodedHistory;
                  assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
                  if (Direction.UPLINK.getValue() == Integer.parseInt(decodedHistoryLora.getDirection())) {
                    assertEquals("123.0",decodedHistoryLora.getDevLAT());
                    assertEquals("125.0",decodedHistoryLora.getDevLON());
                  } else {
                    assertEquals(null,decodedHistoryLora.getDevLAT());
                    assertEquals(null,decodedHistoryLora.getDevLON());
                  }
                });
      }
    }
  }

  @ParameterizedTest(name = "run exportById => (type :{0}, size:{1})")
  @MethodSource("provideDataArguments")
  void exportById(DeviceType deviceType, int size) throws WloggerException {
    SearchInput searchInput = new SearchInput();

    List<DeviceHistory> deviceHistoryArrayList =  new ArrayList<DeviceHistory>();
    deviceHistoryArrayList.addAll(mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(any(Search.class), any(Integer.class), any(Integer.class))).thenReturn(deviceHistoryArrayList);

    Response response = this.users.exportById(context.getId(), searchInput, "realmId");

    assertEquals("attachment; filename=export.csv",response.getHeaderString("Content-Disposition"));
  }

  @ParameterizedTest(name = "run devicesLocationsById => (type :{0}, size:{1})")
  @MethodSource("provideDataArguments")
  void devicesLocationsById(DeviceType deviceType, int size) throws WloggerException {
    SearchInput searchInput = new SearchInput();

    List<DeviceHistory> deviceHistoryArrayList =  new ArrayList<DeviceHistory>();
    deviceHistoryArrayList.addAll(mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(any(Search.class), any(Integer.class), any(Integer.class))).thenReturn(deviceHistoryArrayList);

    ResponsePaginatedList response = (ResponsePaginatedList) this.users.devicesLocationsById(context.getId(), "devId", searchInput, "realmId");

    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertEquals(null,response.getError());
    assertEquals(response.isMore(), (size > 100));

    if (deviceType.equals(DeviceType.LTE)) {
      assertEquals(response.getData().size(), (size > 100 ? 100 : size));
    } else {
      assertEquals(response.getData().size(), (size >= 100 ? 50 : size));
    }

    List<Element> decodedHistories = response.getData();
    if (decodedHistories.size() > 0) {
      String devEUI = deviceHistoryArrayList.get(0).deviceEUI;
      if (deviceType.equals(DeviceType.LTE)) {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistoryLte decodedHistoryLte = (ElementDeviceHistoryLte) decodedHistory;
                  assertEquals(decodedHistoryLte.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLte.getDevLAT());
                  assertEquals("125.0",decodedHistoryLte.getDevLON());
                });
      } else {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistory decodedHistoryLora = (ElementDeviceHistory) decodedHistory;
                  assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLora.getDevLAT());
                  assertEquals("125.0",decodedHistoryLora.getDevLON());
                });
      }
    }
  }

  private void assertResponseList(ResponseList response, int length) {
    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertEquals(length, response.getData().size());
  }

  private void assertResponseData(ResponseData response, String userHref) {
    assertNotNull(response);
    assertEquals("200", response.getStatusCode());
    assertNotNull(response.isSuccess());
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertEquals(userHref, ((ElementAuth) response.getData()).getID());
  }
}
