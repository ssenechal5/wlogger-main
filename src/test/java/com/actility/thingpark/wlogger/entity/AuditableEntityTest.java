package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditableEntityTest {

  AuditableEntity auditableEntity = new AuditableEntity();

  @Test
  void getAuditInfo() {
    AuditInfo auditInfo = new AuditInfo();
    auditInfo.update("name");
    Date date = new Date();
    auditableEntity.setAuditInfo(auditInfo);
    assertEquals("name",auditableEntity.getAuditInfo().getLastUpdatedBy());
    assertEquals(auditableEntity.getAuditInfo().getLastUpdated().toString(),date.toString());
  }

  @Test
  void getVersion() {
    auditableEntity.setVersion(1L);
    assertEquals(1L,auditableEntity.getVersion());
  }
}