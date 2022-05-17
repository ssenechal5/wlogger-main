package com.actility.thingpark.smp.rest.dto.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessCodeDtoTest {

  AccessCodeDto accessCodeDto = new AccessCodeDto("id","code");

  @Test
  void getApplicationID() {
    assertEquals("id",accessCodeDto.getApplicationID());
  }

  @Test
  void getAccessCode() {
    assertEquals("code",accessCodeDto.getAccessCode());
  }
}