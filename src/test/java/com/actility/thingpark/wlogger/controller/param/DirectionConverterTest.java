package com.actility.thingpark.wlogger.controller.param;

import com.actility.thingpark.wlogger.model.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionConverterTest {

  @Test
  void fromString() {
    DirectionConverter c = new DirectionConverter();
    assertEquals(Direction.UPLINK, c.fromString("0"));
    assertEquals(Direction.DOWNLINK, c.fromString("1"));
    assertEquals(Direction.LOCATION, c.fromString("2"));
    assertEquals(Direction.MULTICAST_SUMMARY, c.fromString("3"));
    assertEquals(Direction.MICROFLOW, c.fromString("4"));
    assertEquals(Direction.DEVICE_RESET, c.fromString("5"));
    assertThrows(NumberFormatException.class, () -> c.fromString("NaN"));
    assertThrows(IllegalArgumentException.class, () -> c.fromString("-1"));
  }

  @Test
  void testToString() {
    DirectionConverter c = new DirectionConverter();
    assertEquals("0", c.toString(Direction.UPLINK));
    assertEquals("1", c.toString(Direction.DOWNLINK));
    assertEquals("2", c.toString(Direction.LOCATION));
    assertEquals("3", c.toString(Direction.MULTICAST_SUMMARY));
    assertEquals("4", c.toString(Direction.MICROFLOW));
    assertEquals("5", c.toString(Direction.DEVICE_RESET));
  }

  @Test
  void getConverter() {
    DirectionConverter c = new DirectionConverter();
    assertNull(c.getConverter(String.class, null, null));
    assertEquals(c, c.getConverter(Direction.class, null, null));
  }

  @Test
  void testEquals() {
    DirectionConverter c = new DirectionConverter();
    assertTrue(c.equals(new DirectionConverter()));
    assertFalse(c.equals(null));
    assertFalse(c.equals("new DirectionConverter()"));
  }
}
