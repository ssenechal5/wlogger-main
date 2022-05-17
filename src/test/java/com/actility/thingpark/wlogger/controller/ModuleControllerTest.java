package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.auth.*;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuleControllerTest {

  private AuthenticationService authenticationServiceMock;
  private ModuleController module;

  private HttpServletRequest requestMock;

  private UriInfo uriInfoMock;

  private WloggerConfig config = new WloggerConfig() {
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
      return false;
    }

    @Override
    public boolean lteMode() {
      return false;
    }

    @Override
    public String csvExportDelimiter() {
      return null;
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

  @BeforeEach
  void setUp() {
    authenticationServiceMock = mock(AuthenticationService.class);
    module = new ModuleController();
    module.inject(config, authenticationServiceMock);

    requestMock = mock(HttpServletRequest.class);
    when(requestMock.getContextPath()).thenReturn("/thingpark");

    uriInfoMock = mock(UriInfo.class);
    when(uriInfoMock.getAbsolutePath())
            .thenReturn(
                    UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/module").build());
    when(uriInfoMock.getRequestUri())
            .thenReturn(
                    UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/module?baz=foo").build());
  }

  @Test
  void buildGuiUri() {
    assertEquals(
            "https://www.example.com/thingpark/wlogger/gui?uid=uid&type=module&sessionToken=fakeSessionToken&operator=operator.origin.com&lang=en&language-tag=en",
            ModuleController.buildGuiUri(
                    URI.create("https://www.example.com/thingpark/wlogger/rest/module"),
                    "uid",
                    "fakeSessionToken",
                    "http://operator.origin.com/path",
                    "en")
                    .toString());

  }

  @Test
  void module() throws WloggerException {

    when(authenticationServiceMock.subscriberAdminAccessCodeLogin("fakeAccessCode"))
            .thenReturn(
                    new SuccessLogin(
                            "fakeSessionToken",
                            new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                            new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "suId", null)));
    Response resp =
            module.module(requestMock, uriInfoMock, "fakeAccessCode", "SUB", "en-US", "https://fakeDomain/titi");
    URI locationUri = resp.getLocation();
    assertNotNull(locationUri);
    assertEquals("https", locationUri.getScheme());
    assertEquals("www.example.com", locationUri.getHost());
    assertEquals("/thingpark/wlogger/gui", locationUri.getRawPath());
    assertEquals(
            "uid=scopeId&type=module&sessionToken=fakeSessionToken&operator=fakeDomain&lang=en&language-tag=en",
            locationUri.getQuery());
    verify(authenticationServiceMock).subscriberAdminAccessCodeLogin("fakeAccessCode");
  }

  @Test
  void moduleSupplier() throws WloggerException {

    when(authenticationServiceMock.supplierAdminAccessCodeLogin("fakeAccessCode"))
            .thenReturn(
                    new SuccessLogin(
                            "fakeSessionToken",
                            new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                            new Scope("scopeId", Scope.ScopeType.NETWORK_PARTNER, "suId", null)));
    Response resp =
            module.module(requestMock, uriInfoMock, "fakeAccessCode", "SUPPLIER", "en-US", "https://fakeDomain/titi");
    URI locationUri = resp.getLocation();
    assertNotNull(locationUri);
    assertEquals("https", locationUri.getScheme());
    assertEquals("www.example.com", locationUri.getHost());
    assertEquals("/thingpark/wlogger/gui", locationUri.getRawPath());
    assertEquals(
            "uid=scopeId&type=module&sessionToken=fakeSessionToken&operator=fakeDomain&lang=en&language-tag=en",
            locationUri.getQuery());
    verify(authenticationServiceMock).supplierAdminAccessCodeLogin("fakeAccessCode");
  }

  @Test
  void moduleError() {
    assertThrows(WloggerException.class, () -> module.module(requestMock, uriInfoMock,null,null,null,null));
  }

  @Test
  void logout() throws WloggerException {
    Response response = module.logout(requestMock, "fakeSessionToken", "fakeUID");
    assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
  }
}