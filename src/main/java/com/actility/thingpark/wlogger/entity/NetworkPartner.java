package com.actility.thingpark.wlogger.entity;

import javax.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "supplierID"))
@EntityListeners(AuditListener.class)
public class NetworkPartner extends OperatedEntity implements Auditable {

  @Version
  @Column(name = "occVersion")
  private long version;

  @Embedded
  private AuditInfo auditInfo;

  protected NetworkPartner() {
  }

  public NetworkPartner(Operator operator, String supplierID, String subscriptionHref) {
    super(operator, supplierID, subscriptionHref, State.ACTIVE);
  }

  @Override
  public AuditInfo getAuditInfo() {
    return this.auditInfo;
  }

  @Override
  public void setAuditInfo(AuditInfo auditInfo) {
    this.auditInfo = auditInfo;
  }

  @Override
  public long getVersion() {
    return this.version;
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
