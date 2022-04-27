package com.actility.thingpark.wlogger.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class AuditableEntity extends EntityBase implements Auditable {

  @Version
  @Column(name = "occVersion")
  private long version;

  @Embedded
  private AuditInfo auditInfo;

  @Override
  public AuditInfo getAuditInfo() {
    return auditInfo;
  }

  @Override
  public void setAuditInfo(AuditInfo auditInfo) {
    this.auditInfo = auditInfo;
  }

  @Override
  public long getVersion() {
    return version;
  }

  // Just for test
  public void setVersion(long version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
