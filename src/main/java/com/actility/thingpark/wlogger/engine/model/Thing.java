package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Thing.Builder.class)
public class Thing {
  public final ThingInfo model;
  public final ThingInfo application;
  public final Map<String, Point> points;

  private Thing(Builder builder) {
    this.application = builder.application;
    this.model = builder.model;
    this.points = builder.points;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private ThingInfo model;
    private ThingInfo application;
    private Map<String, Point> points;

    private Builder() {
    }

    public Builder model(ThingInfo model) {
      this.model = model;
      return this;
    }

    public Builder application(ThingInfo application) {
      this.application = application;
      return this;
    }

    public Builder points(Map<String, Point> points) {
      this.points = points;
      return this;
    }

    public Thing build() {
      return new Thing(this);
    }
  }
}
