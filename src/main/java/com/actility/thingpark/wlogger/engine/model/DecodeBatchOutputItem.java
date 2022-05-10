package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = DecodeBatchOutputItem.Builder.class)
public class DecodeBatchOutputItem {
  public final String id;
  public final CodecOutput output;
  public final ErrorInfo error;

  private DecodeBatchOutputItem(Builder builder) {
    this.id = builder.id;
    this.error = builder.error;
    this.output = builder.output;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String id;
    private CodecOutput output;
    private ErrorInfo error;

    private Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder output(CodecOutput output) {
      this.output = output;
      return this;
    }

    public Builder error(ErrorInfo error) {
      this.error = error;
      return this;
    }

    public DecodeBatchOutputItem build() {
      return new DecodeBatchOutputItem(this);
    }
  }
}
