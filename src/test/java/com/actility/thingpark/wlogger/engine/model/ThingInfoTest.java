package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThingInfoTest {

  @Test
  void builder() {
    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();
    assertEquals("version",thingInfo.version);
    assertEquals("moduleId",thingInfo.moduleId);
    assertEquals("producerId",thingInfo.producerId);
  }
}