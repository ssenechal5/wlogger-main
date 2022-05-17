package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThingDescriptionTest {

  @Test
  void builder() {
    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();

    ThingDescription thingDescription = ThingDescription.builder().model(thingInfo).application(thingInfo).build();
    assertEquals("moduleId",thingDescription.application.moduleId);
    assertEquals("producerId",thingDescription.application.producerId);
    assertEquals("version",thingDescription.application.version);
    assertEquals("moduleId",thingDescription.model.moduleId);
    assertEquals("producerId",thingDescription.model.producerId);
    assertEquals("version",thingDescription.model.version);

  }
}