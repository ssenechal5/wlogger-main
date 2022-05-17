package com.actility.thingpark.wlogger.entity.history;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

  @Test
  void getToStringOrEmpty() {
    assertEquals("",Utils.getToStringOrEmpty(""));
    assertEquals("titi",Utils.getToStringOrEmpty("titi"));
    assertEquals("",Utils.getToStringOrEmpty(null));
  }

  @Test
  void getToString() {
    assertEquals("1",Utils.getToString(Boolean.TRUE));
    assertEquals("0",Utils.getToString(Boolean.FALSE));
    assertEquals("0",Utils.getToString((Boolean)null));
  }

  @Test
  void getToStringOrNull() {
    assertEquals("1",Utils.getToStringOrNull(Boolean.TRUE));
    assertEquals("0",Utils.getToStringOrNull(Boolean.FALSE));
    assertEquals("null",Utils.getToStringOrNull(null));
  }

  @Test
  void testGetToString() {
    assertEquals("",Utils.getToString(""));
    assertEquals("titi",Utils.getToString("titi"));
    assertEquals("",Utils.getToString((String)null));
  }

  @Test
  void getEmptyIfNull() {
    assertEquals("",Utils.getEmptyIfNull(null));
    assertEquals("",Utils.getEmptyIfNull(""));
    assertEquals("titi",Utils.getEmptyIfNull("titi"));
    assertEquals("",Utils.getEmptyIfNull("null"));
  }

  @Test
  void testGetToStringOrEmpty() {
    assertEquals("",Utils.getToStringOrEmpty(null));
    assertEquals("",Utils.getToStringOrEmpty(""));
    assertEquals("titi",Utils.getToStringOrEmpty("titi"));
    assertEquals("null",Utils.getToStringOrEmpty("null"));
  }

  @Test
  void testGetToStringOrNull() {
    assertEquals("null",Utils.getToStringOrNull(null));
    assertEquals("",Utils.getToStringOrNull(""));
    assertEquals("titi",Utils.getToStringOrNull("titi"));
    assertEquals("null",Utils.getToStringOrNull("null"));
  }

  @Test
  void getToStringOrNone() {
    assertEquals("None",Utils.getToStringOrNone(null));
    assertEquals("",Utils.getToStringOrNone(""));
    assertEquals("titi",Utils.getToStringOrNone("titi"));
    assertEquals("null",Utils.getToStringOrNone("null"));
  }
}