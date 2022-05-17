package com.actility.thingpark.wlogger.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuccessLoginTest {

  SuccessLogin successLogin = new SuccessLogin("sessiontoken",
          new User(UserType.SMP_ADMIN_USER, "id", "joe", "name", "fr", "oidcUserID"),
          new Scope("id", Scope.ScopeType.SUBSCRIBER, "context", null));

  @Test
  void getSessionToken() {
    assertEquals("sessiontoken",successLogin.getSessionToken());
  }

  @Test
  void getUser() {
    assertEquals("id",successLogin.getUser().getId());
    assertEquals("fr",successLogin.getUser().getLanguage());
    assertEquals("joe",successLogin.getUser().getFirstName());
    assertEquals("name",successLogin.getUser().getLastName());
  }

  @Test
  void getScope() {
    assertEquals("id",successLogin.getScope().getId());
    assertEquals("context",successLogin.getScope().getContextId());
    assertEquals("context",successLogin.getScope().getCustomerId());
  }
}