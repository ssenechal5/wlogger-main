package com.actility.thingpark.wlogger.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

public enum Direction {
  UPLINK(0),
  DOWNLINK(1),
  LOCATION(2),
  MULTICAST_SUMMARY(3),
  MICROFLOW(4),
  DEVICE_RESET(5);

  private final int value;

  Direction(int value) {
    this.value = value;
  }

  @Nullable
  public static Direction fromValue(int value) {
    for (Direction direction : values()) {
      if (value == direction.getValue()) {
        return direction;
      }
    }
    return null;
  }

  @Nonnull
  public static Direction parseValue(int value) {
    return ofNullable(fromValue(value))
        .orElseThrow(() -> new IllegalArgumentException("unknown device type: " + value));
  }

  public int getValue() {
    return value;
  }

  @Nonnull
  public static String getValueOrEmpty(@Nullable Direction direction) {
    return ofNullable(direction)
            .map(Direction::getValue)
            .map(val -> Integer.toString(val))
            .orElse("");
  }
}
