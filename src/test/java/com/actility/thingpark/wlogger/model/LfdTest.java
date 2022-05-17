package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.ElementLfd;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LfdTest {

  Lfd lfd;

  @BeforeEach
  void setUp() {
    Lfd.Builder builder = Lfd.builder();
    lfd = builder.withCnt(1).withDfc("dfc").build();
  }
  
  @Test
  void getAsElement() {
    ElementLfd element = lfd.getAsElement();
    assertEquals("1",element.getCnt());
    assertEquals("dfc",element.getDfc());
  }

  @Test
  void getAsJson() {
    JsonObject obj = lfd.getAsJson();
    assertEquals(1,obj.getInteger("cnt"));
    assertEquals("dfc",obj.getString("dfc"));
  }

  @Test
  void getAsCsv() {
    assertEquals("dfc=1",lfd.getAsCsv());
  }
}