package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = DecodeInput.Builder.class)
public class DecodeInput {
  public final Direction direction;
  public final OffsetDateTime sourceTime;
  public final ObjectNode meta;
  public final ThingDescription thing;
  public final Raw raw;

  private DecodeInput(Builder builder){
    this.direction = builder.direction;
    this.sourceTime = builder.sourceTime;
    this.meta = builder.meta;
    this.thing = builder.thing;
    this.raw = builder.raw;
  }

  public String getSourceTime() {
    return sourceTime.format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private Direction direction;
    private OffsetDateTime sourceTime;
    private ObjectNode meta;
    private ThingDescription thing;
    private Raw raw;
    private String DevEUI;
    private Integer ADRbit;
    private Double Frequency;
    private Integer FCntUp;

    private Builder() {
    }

    public Builder direction(Direction direction) {
      this.direction = direction;
      return this;
    }

    public Builder sourceTime(OffsetDateTime sourceTime) {
      this.sourceTime = sourceTime;
      return this;
    }

    public Builder meta(ObjectNode meta) {
      this.meta = meta;
      return this;
    }

    public Builder thing(ThingDescription thing) {
      this.thing = thing;
      return this;
    }

    public Builder raw(Raw raw) {
      this.raw = raw;
      return this;
    }

    public Builder DevEUI(String DevEUI) {
      this.DevEUI = DevEUI;
      return this;
    }

    public Builder ADRbit(Integer ADRbit) {
      this.ADRbit = ADRbit;
      return this;
    }

    public Builder Frequency(Double Frequency) {
      this.Frequency = Frequency;
      return this;
    }

    public Builder FCntUp(Integer FCntUp) {
      this.FCntUp = FCntUp;
      return this;
    }

    public DecodeInput build() {
      return new DecodeInput(this);
    }
  }
}
