package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodeInputTest {

  @Test
  void builder() {
    Direction direction = Direction.DOWNLINK;
    Raw raw = Raw.builder().binary("binary").build();

    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();

    ThingDescription thingDescription = ThingDescription.builder().model(thingInfo).application(thingInfo).build();

    DecodeInput decodeInput = DecodeInput.builder().direction(direction).meta(null).raw(raw).sourceTime(null).thing(thingDescription).build();
    assertEquals("downlink",decodeInput.direction.getValue());

    assertEquals("binary",decodeInput.raw.binary);

    assertEquals("moduleId",decodeInput.thing.application.moduleId);
    assertEquals("producerId",decodeInput.thing.application.producerId);
    assertEquals("version",decodeInput.thing.application.version);
    assertEquals("moduleId",decodeInput.thing.model.moduleId);
    assertEquals("producerId",decodeInput.thing.model.producerId);
    assertEquals("version",decodeInput.thing.model.version);
  }
}