package com.actility.thingpark.smp.rest.dto.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorsDtoTest {

  private OperatorsDto operatorsDto = new OperatorsDto(new OperatorsDto.Briefs(), false);

  @Test
  void getBriefs() {
    assertEquals(0,operatorsDto.getBriefs().getBrief().size());
  }

  @Test
  void isMore() {
    assertEquals(false,operatorsDto.isMore());
  }
}