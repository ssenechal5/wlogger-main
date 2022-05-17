package com.actility.thingpark.wlogger.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseSimpleTest {

  ResponseSimple responseSimple = new ResponseSimple("statusCode",true,   "error",   "errorCode");

  @Test
  void getStatusCode() {
    assertEquals("statusCode",responseSimple.getStatusCode());
  }

  @Test
  void isSuccess() {
    assertEquals("statusCode",responseSimple.getStatusCode());
  }

  @Test
  void getError() {
    assertEquals("error",responseSimple.getError());
  }

  @Test
  void getErrorCode() {
    assertEquals("errorCode",responseSimple.getErrorCode());
  }
}