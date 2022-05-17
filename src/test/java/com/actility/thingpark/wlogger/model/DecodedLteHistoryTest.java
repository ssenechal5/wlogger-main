package com.actility.thingpark.wlogger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodedLteHistoryTest {

  DecodedLteHistory.Builder builder = DecodedLteHistory.builder();

  @Test
  void getUid() {
    assertEquals("1",builder.withUid("1").build().getUid());
  }

  @Test
  void getDirection() {
    assertEquals(Direction.UPLINK,builder.withDirection(Direction.UPLINK).build().getDirection());
  }

  @Test
  void getDevAddr() {
    assertEquals("1",builder.withDevAddr("1").build().getDevAddr());
  }

  @Test
  void getIpv4Decoded() {
    assertEquals("1",builder.withIpv4Decoded("1").build().getIpv4Decoded());
  }

  @Test
  void getLteCause() {
    assertEquals("1",builder.withLteCause("1").build().getLteCause());
  }

  @Test
  void getLteEBI() {
    assertEquals("1",builder.withLteEBI("1").build().getLteEBI());
  }

  @Test
  void getLteIMSI() {
    assertEquals("1",builder.withLteIMSI("1").build().getLteIMSI());
  }

  @Test
  void getLteRAT() {
    assertEquals("1",builder.withLteRAT("1").build().getLteRAT());
  }
}