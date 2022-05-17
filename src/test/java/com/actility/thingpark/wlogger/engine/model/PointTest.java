package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointTest {

  @Test
  void builder() {

    Map<String, Point> map = new HashMap<>();
    List<Record> records = new ArrayList<>();
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);
    records.add(Record.builder().coordinates(coordinates).build());

    Point point = Point.builder().unitId("UnitId").type("type").records(records).build();
    assertEquals("UnitId",point.unitId);
    assertEquals("type",point.type);
    assertEquals(2,point.records.get(0).coordinates.size());
  }
}