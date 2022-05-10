package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = CodecOutput.Builder.class)
public class CodecOutput {
  public final Direction direction;
  public final OffsetDateTime sourceTime;
  public final ObjectNode meta;
  public final Raw raw;
  public final JsonNode message;
  public final Thing thing;

  private CodecOutput(Builder builder) {
    this.direction = builder.direction;
    this.sourceTime = builder.sourceTime;
    this.meta = builder.meta;
    this.raw = builder.raw;
    this.message = builder.message;
    this.thing = builder.thing;
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
    private Raw raw;
    private JsonNode message;
    private Thing thing;

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

    public Builder raw(Raw raw) {
      this.raw = raw;
      return this;
    }

    public Builder message(JsonNode message) {
      this.message = message;
      return this;
    }

    public Builder thing(Thing thing) {
      this.thing = thing;
      return this;
    }

    public CodecOutput build() {
      return new CodecOutput(this);
    }
  }
}
