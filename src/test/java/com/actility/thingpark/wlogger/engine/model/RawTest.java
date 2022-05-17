package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RawTest {

  @Test
  void builder() {
    Raw raw = Raw.builder().binary("binary").build();
    assertEquals("binary",raw.binary);
  }
}