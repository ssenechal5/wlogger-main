package com.actility.thingpark.wlogger.engine.model;

import com.actility.thingpark.wlogger.model.DecodedMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ThingInfo.Builder.class)
public class ThingInfo {
  public final String producerId;
  public final String moduleId;
  public final String version;

  private ThingInfo(Builder builder) {
    this.version = builder.version;
    this.producerId = builder.producerId;
    this.moduleId = builder.moduleId;
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String producerId;
    private String moduleId;
    private String version;

    public Builder() {
      // Default constructor
    }

    public Builder producerId(String producerId) {
      this.producerId = producerId;
      return this;
    }

    public Builder moduleId(String moduleId) {
      this.moduleId = moduleId;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public String getProducerId() {
      return producerId;
    }

    public String getModuleId() {
      return moduleId;
    }

    public String getVersion() {
      return version;
    }

    public ThingInfo build() {
      return new ThingInfo(this);
    }

    public DecodedMetadata buildDecodedMetadata() {
      return new DecodedMetadata(this);
    }

  }
}
