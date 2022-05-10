package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = EngineDriver.Builder.class)
public class EngineDriver {
  public final String id;
  public final String text;

  private EngineDriver(Builder builder) {
    this.id = builder.id;
    this.text = builder.text;
  }


  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String id;
    private String text;

    private Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public EngineDriver build() {
      return new EngineDriver(this);
    }
  }
}
