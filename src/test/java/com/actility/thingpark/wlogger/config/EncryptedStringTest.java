package com.actility.thingpark.wlogger.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncryptedStringTest {

  @Test
  void testToString() {
    EncryptedString encryptedString = new EncryptedString("test");
    assertEquals("test",encryptedString.toString());
  }
}