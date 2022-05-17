package com.actility.thingpark.wlogger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

  Direction direction = Direction.UPLINK;
  @Test
  void fromValue() {
    assertEquals(direction, Direction.fromValue(0));
  }

  @Test
  void parseValue() {
    assertEquals(direction, Direction.parseValue(0));
  }

  @Test
  void getValue() {
    assertEquals(0,direction.getValue());
  }

  @Test
  void getValueOrEmpty() {
    assertEquals("0",Direction.getValueOrEmpty(direction));
    assertEquals("",Direction.getValueOrEmpty(null));
  }
}