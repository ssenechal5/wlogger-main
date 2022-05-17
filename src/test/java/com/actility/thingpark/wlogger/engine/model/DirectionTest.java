package com.actility.thingpark.wlogger.engine.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

  Direction direction = Direction.DOWNLINK;

  @Test
  void getValue() {
    assertEquals("downlink",direction.getValue() );
  }

  @Test
  void creator() {
    assertEquals(direction, Direction.creator("downlink"));
  }

  @Test
  void fromValue() {
    assertEquals(direction, Direction.fromValue("downlink"));
  }

  @Test
  void fromValue_null() {
    assertEquals(null,Direction.fromValue("downlinkk"));
  }
}