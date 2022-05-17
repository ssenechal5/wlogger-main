package com.actility.thingpark.wlogger.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  private User user = new User(UserType.SMP_ADMIN_USER, "id", "firstName", "lastName", "FR", "oidcUserID");

  @Test
  void getId() {
    assertEquals("id",user.getId());
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
    assertEquals("FR",user.getLanguage());
  }

  @Test
  void getType() {
    assertEquals(UserType.SMP_ADMIN_USER,user.getType());
  }

  @Test
  void testEquals() {
    User userEquals = new User(UserType.SMP_ADMIN_USER, "id", "firstName", "lastName", "FR", "oidcUserID");
    assertTrue(user.equals(userEquals));
  }

  @Test
  void testEqualsOtherType() {
    User userEquals = new User(UserType.INTERNAL_ACCESS_CODE, "id", "firstName", "lastName", "FR", "oidcUserID");
    assertTrue(user.equals(userEquals));
  }

  @Test
  void testNotEquals() {
    User userEquals = new User(UserType.SMP_ADMIN_USER, "id2", "firstName", "lastName", "FR", "oidcUserID");
    assertFalse(user.equals(userEquals));
  }

  @Test
  void testHashCode() {
    assertEquals(903985612,user.hashCode());
  }
}