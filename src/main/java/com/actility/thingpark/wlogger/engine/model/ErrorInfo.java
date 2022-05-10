package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ErrorInfo.Builder.class)
public class ErrorInfo  implements Serializable {

  private static final long serialVersionUID = 1905122041950251207L;

  public final String code;
  public final transient ObjectNode data;
  public final String message;

  private ErrorInfo(Builder builder) {
    this.data = builder.data;
    this.code = builder.code;
    this.message = builder.message;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String code;
    private ObjectNode data;
    private String message;

    private Builder() {
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder data(ObjectNode data) {
      this.data = data;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public ErrorInfo build() {
      return new ErrorInfo(this);
    }
  }
}
