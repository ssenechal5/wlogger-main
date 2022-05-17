package com.actility.thingpark.wlogger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceTypeTest {

  DeviceType deviceType = DeviceType.LORA;

  @Test
  void parseValue() {
    assertEquals(deviceType, DeviceType.parseValue(0));
  }

  @Test
  void getValue() {
    assertEquals(0,deviceType.getValue());
  }
}