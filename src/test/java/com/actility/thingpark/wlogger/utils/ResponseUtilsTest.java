package com.actility.thingpark.wlogger.utils;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseUtilsTest {

  @Test
  void getAcceptMediaType() {
    assertEquals(MediaType.APPLICATION_JSON_TYPE,ResponseUtils.getAcceptMediaType(Collections.emptyList()));
    assertEquals(MediaType.APPLICATION_XML_TYPE,ResponseUtils.getAcceptMediaType(Collections.singletonList(MediaType.APPLICATION_XML_TYPE)));
  }
}