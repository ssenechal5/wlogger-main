package com.actility.thingpark.smp.rest.dto.subscriber;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressDtoTest {

  private AddressDto addressDto = new AddressDto(  "typeIndicator",   "personalName",   "name",   "address1",   "address2",   "city",
    "region",   "zip",   "country",   "contactPhone",   "contactInfo",   null,   "id");

    @Test
  void getPersonalName() {
      assertEquals("personalName",addressDto.getPersonalName());
    }

  @Test
  void getName() {
    assertEquals("name",addressDto.getName());
  }

  @Test
  void getCity() {
    assertEquals("city",addressDto.getCity());
  }

  @Test
  void getAddress1() {
    assertEquals("address1",addressDto.getAddress1());
  }

  @Test
  void getAddress2() {
    assertEquals("address2",addressDto.getAddress2());
  }

  @Test
  void getContactPhone() {
    assertEquals("contactPhone",addressDto.getContactPhone());
  }
}