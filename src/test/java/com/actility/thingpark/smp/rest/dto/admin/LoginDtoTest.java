package com.actility.thingpark.smp.rest.dto.admin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginDtoTest {

  private LoginDto login = new LoginDto("login","password","sessiontoken","id","domain");

  @Test
  void getLogin() {
    assertEquals("login",login.getLogin());
  }

  @Test
  void getPassword() {
    assertEquals("password",login.getPassword());
  }

  @Test
  void getSessionToken() {
    assertEquals("sessiontoken",login.getSessionToken());
  }

  @Test
  void getThingparkID() {
    assertEquals("id",login.getThingparkID());
  }

  @Test
  void getDomain() {
    assertEquals("domain",login.getDomain());
  }
}