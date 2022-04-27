package com.actility.thingpark.wlogger.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public class ActorEntity extends EntityBase {

  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date creationTimestamp;

  @NotNull
  @Length(max = 512)
  @Column(nullable = false, length = 512)
  private String href;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private State state;

  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date stateTimestamp;

  @NotNull
  @Column(name = "ID")
  private String id;

  protected ActorEntity() {
  }

  protected ActorEntity(String id, String href, State state) {
    this.id = id;
    this.href = href;
    this.state = state;
    this.stateTimestamp = new Date();
    this.creationTimestamp = this.stateTimestamp;
  }

  public String getHref() {
    return href;
  }

  public String getID() {
    return id;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Date getStateTimestamp() {
    return stateTimestamp;
  }

  public void setStateTimestamp(Date stateTimestamp) {
    this.stateTimestamp = stateTimestamp;
  }

  public Date getCreationTimestamp() {
    return creationTimestamp;
  }

  public enum State {
    ACTIVE, SUSPENDED, DEACTIVATED;
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
