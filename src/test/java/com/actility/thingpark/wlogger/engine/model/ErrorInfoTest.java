package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorInfoTest {

  @Test
  void builder() {
    ErrorInfo error = ErrorInfo.builder().code("code").message("message").data(null).build();
    assertEquals("code",error.code);
    assertEquals("message",error.message);
    assertEquals(null,error.data);
  }
}