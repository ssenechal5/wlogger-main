package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorTest {

  Operator operator = new Operator("id","href",120);

  @Test
  void getWebSessionLifetime() {
    assertEquals(120,operator.getWebSessionLifetime());
  }

  @Test
  void getWebSessionTokenRequired() {
    assertEquals(1,operator.getWebSessionTokenRequired());
  }
}