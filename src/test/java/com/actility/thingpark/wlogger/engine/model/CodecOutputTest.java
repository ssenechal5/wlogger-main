package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodecOutputTest {

  @Test
  void builder() {
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
    assertEquals("downlink",codecOutput.direction.getValue());

    assertEquals("binary",codecOutput.raw.binary);

    assertEquals("moduleId",codecOutput.thing.application.moduleId);
    assertEquals("producerId",codecOutput.thing.application.producerId);
    assertEquals("version",codecOutput.thing.application.version);
    assertEquals("moduleId",codecOutput.thing.model.moduleId);
    assertEquals("producerId",codecOutput.thing.model.producerId);
    assertEquals("version",codecOutput.thing.model.version);

    assertEquals(1,codecOutput.thing.points.size());
    assertEquals(1,codecOutput.thing.points.get("0").records.size());
    assertEquals(2,codecOutput.thing.points.get("0").records.get(0).coordinates.size());

  }
}