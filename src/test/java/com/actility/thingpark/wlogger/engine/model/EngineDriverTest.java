package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EngineDriverTest {

  @Test
  void builder() {
    EngineDriver engineDriver = EngineDriver.builder().id("id").text("text").build();
    assertEquals("id",engineDriver.id);
    assertEquals("text",engineDriver.text);
  }
}