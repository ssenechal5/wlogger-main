package com.actility.thingpark.smp.rest.dto.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {

  private UserDto user = new UserDto("state",null,"gender","firstName","lastName","language","id","id","id","email","phone","phone","phone", "organization", null, null, 0, "message", true);

  @Test
  void getState() {
    assertEquals("state",user.getState());
  }

  @Test
  void getFirstName() {
    assertEquals("firstName",user.getFirstName());
  }

  @Test
  void getLastName() {
    assertEquals("lastName",user.getLastName());
  }

  @Test
  void getLanguage() {
    assertEquals("language",user.getLanguage());
  }

  @Test
  void getThingparkID() {
    assertEquals("id",user.getThingparkID());
  }

  @Test
  void getEmail() {
    assertEquals("email",user.getEmail());
  }
}