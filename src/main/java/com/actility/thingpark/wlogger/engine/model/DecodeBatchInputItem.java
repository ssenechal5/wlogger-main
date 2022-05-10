package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = DecodeInput.Builder.class)
public class DecodeBatchInputItem {
  public final String id;
  public final DecodeInput input;

  private DecodeBatchInputItem(Builder builder) {
    this.id = builder.id;
    this.input = builder.input;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String id;
    private DecodeInput input;

    private Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder input(DecodeInput input) {
      this.input = input;
      return this;
    }

    public DecodeBatchInputItem build() {
      return new DecodeBatchInputItem(this);
    }
  }
}
