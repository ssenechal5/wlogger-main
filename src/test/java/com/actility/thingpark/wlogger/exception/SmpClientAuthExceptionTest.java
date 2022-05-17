package com.actility.thingpark.wlogger.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmpClientAuthExceptionTest {

  SmpClientAuthException smpClientAuthException = new SmpClientAuthException(1,"message");

  @Test
  void getErrorCode() {assertEquals(1,smpClientAuthException.getErrorCode());}
}