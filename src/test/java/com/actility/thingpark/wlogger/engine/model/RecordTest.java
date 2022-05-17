package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordTest {

  @Test
  void builder() {
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);

    Record record = Record.builder().coordinates(coordinates).eventTime(null).value(null).build();
    assertEquals(2,record.coordinates.size());
  }
}