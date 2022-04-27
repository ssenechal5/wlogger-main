package com.actility.thingpark.wlogger.dto.subscription;

import com.actility.thingpark.wlogger.entity.AuditInfo;
import com.actility.thingpark.wlogger.entity.Auditable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class OccContext {

  private long version;
  private Date lastUpdate;
  private String who;

  public OccContext() {
  }

  public OccContext(Auditable auditable) {
    this.version = auditable.getVersion();
    AuditInfo auditInfo = auditable.getAuditInfo();

    if (auditInfo != null) {
      this.lastUpdate = auditInfo.getLastUpdated();
      this.who = auditInfo.getLastUpdatedBy();
    }
  }
}
