package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.auth.*;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PortalControllerTest {

  @Test
  void portal() throws WloggerException {
    final AuthenticationService authenticationService = mock(AuthenticationService.class);
    final WloggerConfig wloggerConfig = mock(WloggerConfig.class);
    PortalController portal = new PortalController();
    portal.inject(wloggerConfig, authenticationService);

    final UriInfo uriInfo = mock(UriInfo.class);
    when(uriInfo.getAbsolutePath())
        .thenReturn(
            UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/portal").build());
    when(uriInfo.getRequestUri())
        .thenReturn(
            UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/portal?baz=foo").build());
    when(authenticationService.subscriptionAccessCodeLogin("fakeSubscriptionAccessCode"))
        .thenReturn(
            new SuccessLogin(
                "fakeSessionToken",
                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "suId", null)));
    Response resp =
        portal.portal(uriInfo, null, new String("fakeSubscriptionAccessCode"), "fakeDomain");
    URI locationUri = resp.getLocation();
    assertNotNull(locationUri);
    assertEquals("https", locationUri.getScheme());
    assertEquals("www.example.com", locationUri.getHost());
    assertEquals("/thingpark/wlogger/gui", locationUri.getRawPath());
    assertEquals(
        "uid=scopeId&type=portal&sessionToken=fakeSessionToken&userAccount=scopeId&lang=en&language-tag=en",
        locationUri.getQuery());
    verify(authenticationService).subscriptionAccessCodeLogin("fakeSubscriptionAccessCode");
  }

  @Test
  public void buildGuiUri() {
    assertEquals(
        "https://www.example.com/thingpark/wlogger/gui?uid=scopeId&type=portal&sessionToken=fakeSessionToken&userAccount=scopeId&lang=en&language-tag=en",
        PortalController.buildGuiUri(
                URI.create("https://www.example.com/thingpark/wlogger/rest/portal"),
                "scopeId",
                "portal",
                "fakeSessionToken",
                "en")
            .toString());
  }
}
