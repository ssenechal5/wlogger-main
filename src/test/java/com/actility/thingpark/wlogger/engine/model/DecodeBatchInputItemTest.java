package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodeBatchInputItemTest {

  @Test
  void builder() {
    Direction direction = Direction.DOWNLINK;
    Raw raw = Raw.builder().binary("binary").build();

    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();

    ThingDescription thingDescription = ThingDescription.builder().model(thingInfo).application(thingInfo).build();
    DecodeInput decodeInput = DecodeInput.builder().direction(direction).meta(null).raw(raw).sourceTime(null).thing(thingDescription).build();

    DecodeBatchInputItem decodeBatchInputItem = DecodeBatchInputItem.builder().input(decodeInput).id("id").build();
    assertEquals("id",decodeBatchInputItem.id);
    assertEquals("downlink",decodeBatchInputItem.input.direction.getValue());

    assertEquals("binary",decodeBatchInputItem.input.raw.binary);

    assertEquals("moduleId",decodeBatchInputItem.input.thing.application.moduleId);
    assertEquals("producerId",decodeBatchInputItem.input.thing.application.producerId);
    assertEquals("version",decodeBatchInputItem.input.thing.application.version);
    assertEquals("moduleId",decodeBatchInputItem.input.thing.model.moduleId);
    assertEquals("producerId",decodeBatchInputItem.input.thing.model.producerId);
    assertEquals("version",decodeBatchInputItem.input.thing.model.version);

  }
}