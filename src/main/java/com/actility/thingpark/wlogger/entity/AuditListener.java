package com.actility.thingpark.wlogger.entity;

import com.actility.thingpark.wlogger.auth.WloggerSession;

import javax.inject.Inject;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditListener {

  private WloggerSession wloggerSession;

  @Inject
  void inject(final WloggerSession wloggerSession) {
    this.wloggerSession = wloggerSession;
  }

  @PreUpdate
  @PrePersist
  public void update(Object entity) {
    if (entity instanceof Auditable) {
      Auditable auditable = (Auditable) entity;
      if (auditable.getAuditInfo() == null)
        auditable.setAuditInfo(new AuditInfo());
      String name = "unknown";
      if (wloggerSession != null) {
        name = String.format("%s %s",
                wloggerSession.getAuthenticatedUser().getFirstName(),
                wloggerSession.getAuthenticatedUser().getLastName());
      }
      auditable.getAuditInfo().update(name);
    }
  }
}
