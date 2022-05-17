package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest {

  Admin admin = new Admin("login","password");

  @Test
  void getLogin() {
    assertEquals("login",admin.getLogin());
  }

  @Test
  void getPassword() {
    assertEquals("password",admin.getPassword());
  }
}