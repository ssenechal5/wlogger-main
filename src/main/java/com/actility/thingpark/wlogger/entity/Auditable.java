package com.actility.thingpark.wlogger.entity;

public interface Auditable {

  AuditInfo getAuditInfo();

  void setAuditInfo(AuditInfo auditInfo);

  long getVersion();
}
