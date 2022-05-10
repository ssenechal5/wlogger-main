package com.actility.thingpark.wlogger.model;

import javax.annotation.Nonnull;

public enum DeviceType {
  LORA(0),
  LTE(1);

  private final int value;

  DeviceType(int value) {
    this.value = value;
  }

  @Nonnull
  public static DeviceType parseValue(int value) {
    for (DeviceType deviceType : values()) {
      if (value == deviceType.getValue()) {
        return deviceType;
      }
    }
    throw new IllegalArgumentException("unknown device type: " + value);
  }

  public int getValue() {
    return value;
  }
}
