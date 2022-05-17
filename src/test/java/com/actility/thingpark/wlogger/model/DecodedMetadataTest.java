package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.engine.model.ThingInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodedMetadataTest {

  DecodedMetadata decodedMetadata = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").buildDecodedMetadata();

  @Test
  void testToString() {
    assertEquals(decodedMetadata.toString(),String.join(":", "producerId", "moduleId", "version"));
  }
}