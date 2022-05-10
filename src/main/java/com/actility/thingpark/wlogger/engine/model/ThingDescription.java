package com.actility.thingpark.wlogger.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ThingDescription.Builder.class)
public class ThingDescription {
  public final ThingInfo model;
  public final ThingInfo application;

  private ThingDescription(Builder builder) {
    this.model = builder.model;
    this.application = builder.application;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private ThingInfo model;
    private ThingInfo application;

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

    public ThingDescription build() {
      return new ThingDescription(this);
    }
  }
}
