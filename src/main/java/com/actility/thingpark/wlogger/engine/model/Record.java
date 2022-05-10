package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Record.Builder.class)
public class Record {
  public final OffsetDateTime eventTime;
  public final List<Double> coordinates;
  public final JsonNode value;

  private Record(Builder builder) {
    eventTime = builder.eventTime;
    coordinates = builder.coordinates;
    value = builder.value;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private OffsetDateTime eventTime;
    private List<Double> coordinates;
    private JsonNode value;

    private Builder() {
    }

    public Builder eventTime(OffsetDateTime eventTime) {
      this.eventTime = eventTime;
      return this;
    }

    public Builder coordinates(List<Double> coordinates) {
      this.coordinates = coordinates;
      return this;
    }

    public Builder value(JsonNode value) {
      this.value = value;
      return this;
    }

    public Record build() {
      return new Record(this);
    }
  }
}
