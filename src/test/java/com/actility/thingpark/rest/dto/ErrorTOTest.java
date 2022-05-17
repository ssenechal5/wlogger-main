package com.actility.thingpark.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorTOTest {

  private ErrorTO error = new ErrorTO(0, "message", "info1", "info2", null);

  @Test
  void getCode() {
    assertEquals(0,error.getCode());
  }

  @Test
  void getMessage() {
    assertEquals("message",error.getMessage());
  }

  @Test
  void getInfo1() {
    assertEquals("info1",error.getInfo1());
  }

  @Test
  void getInfo2() { assertEquals("info2",error.getInfo2());
  }
}