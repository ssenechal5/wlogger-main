package com.actility.thingpark.wlogger.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WloggerExceptionTest {

  WloggerException wloggerException = new WloggerException(1,"message");

  @Test
  void notLoggedIn401() {
    assertEquals(401,WloggerException.notLoggedIn401(1,"message").getStatusCode());
    assertEquals(1,WloggerException.notLoggedIn401(1,"message").getErrorCode());
    assertEquals("message",WloggerException.notLoggedIn401(1,"message").getMessage());
  }

  @Test
  void getStatusCode() {
    assertEquals(1,wloggerException.getStatusCode());
  }

  @Test
  void getErrorCode() {
    assertEquals(0,wloggerException.getErrorCode());
  }
}