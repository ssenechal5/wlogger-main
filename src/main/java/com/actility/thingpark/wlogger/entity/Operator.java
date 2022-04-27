package com.actility.thingpark.wlogger.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Operator extends ActorEntity {

  @Column(nullable = false)
  @NotNull
  private Integer webSessionLifetime;

  @Column(nullable = false)
  @NotNull
  private Integer webSessionTokenRequired;

  public Operator(String id, String href, int webSessionLifetime) {
    super(id, href, State.ACTIVE);
    this.webSessionLifetime = webSessionLifetime;
    this.webSessionTokenRequired = 1;
  }

  public Operator() {}

  public Integer getWebSessionLifetime() {
    return webSessionLifetime;
  }

  public Integer getWebSessionTokenRequired() {
    return webSessionTokenRequired;
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
