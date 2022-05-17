package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetworkPartnerTest {

  Operator operator = new Operator("id","href",120);
  NetworkPartner networkPartner = new NetworkPartner(operator,"id","href");
  AuditInfo auditInfo = new AuditInfo();

  @Test
  void getAuditInfo() {
    auditInfo.update("name");
    networkPartner.setAuditInfo(auditInfo);
    assertEquals(networkPartner.getAuditInfo(),auditInfo);
  }

  @Test
  void getVersion() {
    networkPartner.setVersion(1);
    assertEquals(1 ,networkPartner.getVersion());
  }

  @Test
  void getOperator() {
    assertEquals(networkPartner.getOperator(),operator);
    assertEquals(networkPartner.getOperator().getID(),operator.getID());
    assertEquals(networkPartner.getOperator().getHref(),operator.getHref());
    assertEquals(networkPartner.getOperator().getID(),operator.getID());
  }
}