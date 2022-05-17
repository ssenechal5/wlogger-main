package com.actility.thingpark.wlogger.controller.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchInputTest {

  SearchInput searchInput = new SearchInput();

  @Test
  void getDecoder() {
    searchInput.decoder = "decoder";
    assertEquals("decoder",searchInput.getDecoder());
  }

  @Test
  void getStartUid() {
    searchInput.startUid = "1";
    assertEquals("1",searchInput.getStartUid());
  }

  @Test
  void getEndUid() {
    searchInput.endUid = "1";
    assertEquals("1",searchInput.getEndUid());
  }

  @Test
  void getAsID() {
    searchInput.asID = "1";
    assertEquals("1",searchInput.getAsID());
  }

  @Test
  void getSubscriberID() {
    searchInput.subscriberID = "1";
    assertEquals("1",searchInput.getSubscriberID());
  }
}