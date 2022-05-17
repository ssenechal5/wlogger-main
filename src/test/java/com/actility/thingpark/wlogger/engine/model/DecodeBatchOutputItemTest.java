package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodeBatchOutputItemTest {

  @Test
  void builder() {
    ErrorInfo error = ErrorInfo.builder().code("code").message("message").data(null).build();

    Direction direction = Direction.DOWNLINK;
    Raw raw = Raw.builder().binary("binary").build();

    Map<String, Point> map = new HashMap<>();
    List<Record> records = new ArrayList<>();
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);
    records.add(Record.builder().coordinates(coordinates).build());
    map.put("0",Point.builder().unitId("unit").type("type").records(records).build());

    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();

    Thing thing = Thing.builder().application(thingInfo).model(thingInfo).points(map).build();

    CodecOutput codecOutput = CodecOutput.builder().direction(direction).message(null).meta(null).raw(raw).sourceTime(null).thing(thing).build();

    DecodeBatchOutputItem decodeBatchOutputItem = DecodeBatchOutputItem.builder().id("id").error(error).output(codecOutput).build();

    assertEquals("id",decodeBatchOutputItem.id);

    assertEquals("code",decodeBatchOutputItem.error.code);
    assertEquals("message",decodeBatchOutputItem.error.message);
    assertEquals(null,decodeBatchOutputItem.error.data);

    assertEquals(decodeBatchOutputItem.output.direction.getValue(), "downlink");

    assertEquals(decodeBatchOutputItem.output.raw.binary,"binary");

    assertEquals("moduleId",decodeBatchOutputItem.output.thing.application.moduleId);
    assertEquals("producerId",decodeBatchOutputItem.output.thing.application.producerId);
    assertEquals("version",decodeBatchOutputItem.output.thing.application.version);
    assertEquals("moduleId",decodeBatchOutputItem.output.thing.model.moduleId);
    assertEquals("producerId",decodeBatchOutputItem.output.thing.model.producerId);
    assertEquals("version",decodeBatchOutputItem.output.thing.model.version);

    assertEquals(1,decodeBatchOutputItem.output.thing.points.size());
    assertEquals(1,decodeBatchOutputItem.output.thing.points.get("0").records.size());
    assertEquals(2,decodeBatchOutputItem.output.thing.points.get("0").records.get(0).coordinates.size());
  }
}