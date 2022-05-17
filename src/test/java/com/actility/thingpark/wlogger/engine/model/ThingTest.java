package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThingTest {

  @Test
  void builder() {

    Map<String, Point> map = new HashMap<>();
    List<Record> records = new ArrayList<>();
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);
    records.add(Record.builder().coordinates(coordinates).build());
    map.put("0",Point.builder().unitId("unit").type("type").records(records).build());

    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();

    Thing thing = Thing.builder().application(thingInfo).model(thingInfo).points(map).build();
    assertEquals("moduleId",thing.application.moduleId);
    assertEquals("producerId",thing.application.producerId);
    assertEquals("version",thing.application.version);
    assertEquals("moduleId",thing.model.moduleId);
    assertEquals("producerId",thing.model.producerId);
    assertEquals("version",thing.model.version);

    assertEquals(1,thing.points.size());
    assertEquals(1,thing.points.get("0").records.size());
    assertEquals(2,thing.points.get("0").records.get(0).coordinates.size());
  }
}