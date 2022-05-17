package com.actility.thingpark.smp.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdTest {

  private Id id = new Id("id");
  @Test
  void getID() {
    assertEquals("id",id.getID());
  }

  @Test
  void withID() {
    assertEquals(id.withID("id"),id);
  }
}