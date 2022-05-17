package com.actility.thingpark.smp.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeatureDtoTest {

  FeatureDto featureDto = new FeatureDto("name","val");
  @Test
  void getName() {
    assertEquals("name",featureDto.getName());
  }

  @Test
  void getVal() {
    assertEquals("val",featureDto.getVal());
  }
}