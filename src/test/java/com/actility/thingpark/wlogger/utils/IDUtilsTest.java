package com.actility.thingpark.wlogger.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IDUtilsTest {

  @Test
  void newID() {
    assertEquals(16,IDUtils.newID().length());
  }
}