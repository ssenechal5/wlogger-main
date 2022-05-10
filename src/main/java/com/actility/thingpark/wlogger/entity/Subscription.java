package com.actility.thingpark.wlogger.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "subscriberID"))
@EntityListeners(AuditListener.class)
public class Subscription extends OperatedEntity implements Auditable {

  @Length(max = 255)
  @Column(length = 255)
  private String subscriberExtlID;
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Type subscriberType;
  @Version
  @Column(name = "occVersion")
  private long version;
  @Embedded
  private AuditInfo auditInfo;

  protected Subscription() {
  }

  public Subscription(Operator operator, String subscriberID, String subscriberExternalID, String subscriptionHref,
                      Type subscriberType) {
    super(operator, subscriberID, subscriptionHref, State.ACTIVE);
    this.subscriberType = subscriberType;
    this.subscriberExtlID = subscriberExternalID;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public String getSubscriberExtlID() {
    return this.subscriberExtlID;
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

  public enum Type {
    PRODUCTION, TEST
  }
}
