package com.actility.thingpark.wlogger.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodingUtilsTest {

  @Test
  void checkSHA256Password() {
    assertEquals(false,CodingUtils.checkSHA256Password(null,"password"));
    assertEquals(false,CodingUtils.checkSHA256Password("password",null));
    assertEquals(false,CodingUtils.checkSHA256Password(null,null));
    assertEquals(false,CodingUtils.checkSHA256Password("passwordpasswordpasswordpasswordpasswordpasswordpasswordpassword","password"));
    assertEquals(true,CodingUtils.checkSHA256Password("a99f6f31ac627c4f686b50c754840ef854fc0dd5619a1c76015d723d4fe596e05b55735307946a8b2737d07eb61d93d083814dfa9b312ded4e06b493aeda2aa7","WLoggerAdmin5"));
  }
}