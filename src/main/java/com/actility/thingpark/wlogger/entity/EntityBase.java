package com.actility.thingpark.wlogger.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class EntityBase implements WloggerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long uid;

  @Override
  public Long getUID() {
    return this.uid;
  }

  // Just for test
  public void setUID(Long uid) {
    this.uid = uid;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    return this.equalsEntity((EntityBase) obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }

  public boolean equalsEntity(EntityBase entity) {
    if (entity == null) {
      return false;
    }
    if (entity.uid == null && this.uid == null) {
      return true;
    }
    if (entity.uid == null && this.uid != null) {
      return false;
    }
    return entity.uid.equals(this.uid);
  }
}
