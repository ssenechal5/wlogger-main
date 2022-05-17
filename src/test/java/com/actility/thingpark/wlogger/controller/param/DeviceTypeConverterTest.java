package com.actility.thingpark.wlogger.controller.param;

import com.actility.thingpark.wlogger.model.DeviceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTypeConverterTest {

  @Test
  void fromString() {
    DeviceTypeConverter c = new DeviceTypeConverter();
    assertEquals(DeviceType.LORA, c.fromString("0"));
    assertEquals(DeviceType.LTE, c.fromString("1"));
    assertThrows(NumberFormatException.class, () -> c.fromString("NaN"));
    assertThrows(IllegalArgumentException.class, () -> c.fromString("-1"));
  }

  @Test
  void testToString() {
    DeviceTypeConverter c = new DeviceTypeConverter();
    assertEquals("0", c.toString(DeviceType.LORA));
    assertEquals("1", c.toString(DeviceType.LTE));
  }

  @Test
  void getConverter() {
    DeviceTypeConverter c = new DeviceTypeConverter();
    assertNull(c.getConverter(String.class, null, null));
    assertEquals(c, c.getConverter(DeviceType.class, null, null));
  }

  @Test
  void testEquals() {
    DeviceTypeConverter c = new DeviceTypeConverter();
    assertTrue(c.equals(new DeviceTypeConverter()));
    assertFalse(c.equals(null));
    assertFalse(c.equals("new DirectionConverter()"));
  }
}
