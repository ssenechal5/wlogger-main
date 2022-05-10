package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Point.Builder.class)
public class Point {
  public final String unitId;
  public final String type;
  public final List<Record> records;

  private Point(Builder builder) {
    unitId = builder.unitId;
    type = builder.type;
    records = builder.records;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String unitId;
    private String type;
    private List<Record> records;

    private Builder() {
    }

    public Builder unitId(String unitId) {
      this.unitId = unitId;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder records(List<Record> records) {
      this.records = records;
      return this;
    }

    public Point build() {
      return new Point(this);
    }
  }
}
