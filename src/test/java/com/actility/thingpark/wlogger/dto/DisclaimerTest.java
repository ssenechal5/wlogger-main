package com.actility.thingpark.wlogger.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisclaimerTest {

  Disclaimer disclaimer = new Disclaimer();

  @Test
  void getWho() {
    disclaimer.setWho("who");
    assertEquals("who",disclaimer.getWho());
  }

  @Test
  void getDisclaimerMessage() {
    disclaimer.setDisclaimerMessage("message");
    assertEquals("message",disclaimer.getDisclaimerMessage());
  }

  @Test
  void isDisclaimerRequired() {
    disclaimer.setDisclaimerRequired(true);
    assertEquals(true,disclaimer.isDisclaimerRequired());
  }
}