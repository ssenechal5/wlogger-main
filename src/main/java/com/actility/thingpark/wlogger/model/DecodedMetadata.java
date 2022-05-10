package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.engine.model.ThingInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ThingInfo.Builder.class)
public class DecodedMetadata {
  public final String producerId;
  public final String moduleId;
  public final String version;

  public DecodedMetadata(ThingInfo.Builder builder) {
    this.moduleId = builder.getModuleId();
    this.version = builder.getVersion();
    this.producerId = builder.getProducerId();
  }

  @Override
  public String toString() {
    return String.join(":", this.producerId, this.moduleId, this.version);
  }

}
