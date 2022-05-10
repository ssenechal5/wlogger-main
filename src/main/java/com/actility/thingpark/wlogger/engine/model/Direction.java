package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public enum Direction {
  UPLINK("uplink"),
  DOWNLINK("downlink");

  private final String value;

  Direction(@Nonnull String value) {
    this.value = value;
  }

  @JsonValue
  @Nonnull
  public String getValue() {
    return value;
  }


  @JsonCreator
  @Nonnull
  public static Direction creator(String value) {
    return Optional.ofNullable(fromValue(value))
            .orElseThrow(() -> new IllegalArgumentException(value));
  }

  @Nullable
  public static Direction fromValue(String value) {
    for (Direction direction: values()) {
      if(direction.getValue().equals(value)) {
        return direction;
      }
    }
    return null;
  }
}
