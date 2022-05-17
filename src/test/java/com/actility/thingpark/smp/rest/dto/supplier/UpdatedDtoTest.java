package com.actility.thingpark.smp.rest.dto.supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdatedDtoTest {

  private UpdatedDto updatedDto = new UpdatedDto("id",null,"href");

  @Test
  void getTransactionID() {
    assertEquals("id",updatedDto.getTransactionID());
  }

  @Test
  void getHref() {
    assertEquals("href",updatedDto.getHref());
  }
}