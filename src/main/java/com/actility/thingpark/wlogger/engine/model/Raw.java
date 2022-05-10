package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Raw.Builder.class)
public class Raw {
  public final String binary;

  private Raw(Builder builder) {
    this.binary = builder.binary;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String binary;

    private Builder() {
    }

    public Builder binary(String binary) {
      this.binary = binary;
      return this;
    }

    public Raw build() {
      return new Raw(this);
    }
  }
}
