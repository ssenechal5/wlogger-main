package com.actility.thingpark.wlogger.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class AuditInfo {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "occUpdated")
  private Date lastUpdated;

  @Column(name = "occUpdater")
  private String lastUpdatedBy;

  public void update(String name) {
    lastUpdated = new Date();
    lastUpdatedBy = name;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }
}
