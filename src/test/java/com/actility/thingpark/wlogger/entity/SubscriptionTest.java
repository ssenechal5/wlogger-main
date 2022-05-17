package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionTest {

  Operator operator = new Operator("id","href",120);
  Subscription subscription = new Subscription(operator,"id","id","href", Subscription.Type.PRODUCTION);
  AuditInfo auditInfo = new AuditInfo();

  @Test
  void getSubscriberExtlID() {
    assertEquals("id",subscription.getSubscriberExtlID());
  }

  @Test
  void getAuditInfo() {
    auditInfo.update("name");
    subscription.setAuditInfo(auditInfo);
    assertEquals(subscription.getAuditInfo(),auditInfo);
  }

  @Test
  void getVersion() {
    subscription.setVersion(1);
    assertEquals(1,subscription.getVersion());
  }
}