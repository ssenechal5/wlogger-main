package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditInfoTest {

  AuditInfo auditInfo = new AuditInfo();

  @Test
  void getLastUpdated() {
    auditInfo.update("name");
    assertEquals(auditInfo.getLastUpdated().getDay(), new Date().getDay());
  }

  @Test
  void getLastUpdatedBy() {
    auditInfo.update("name");
    assertEquals("name", auditInfo.getLastUpdatedBy());
    assertEquals(auditInfo.getLastUpdated().getDay(), new Date().getDay());
  }
}