package com.actility.thingpark.wlogger.dto.subscription;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionDtoTest {

  SubscriptionDto subscriptionDto = new SubscriptionDto();

  @Test
  void getStateValue() {
    subscriptionDto.state("value",null);
    assertEquals("value",subscriptionDto.getStateValue());
  }

  @Test
  void getSubscriberId() {
    subscriptionDto.subscriber("id", "id","org", "name");
    assertEquals("id",subscriptionDto.getSubscriberId());
  }

  @Test
  void getSubscriberName() {
    subscriptionDto.subscriber("id", "id","org", "name");
    assertEquals("name",subscriptionDto.getSubscriberName());
  }
}