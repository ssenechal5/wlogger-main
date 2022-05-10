package com.actility.thingpark.wlogger.entity;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class OperatedEntity extends ActorEntity {

  @NotNull
  @ManyToOne
  private Operator operator;

  protected OperatedEntity() {

  }

  protected OperatedEntity(Operator operator, String id, String href, State state) {
    super(id, href, state);
    this.operator = operator;
  }

  public Operator getOperator() {
    return operator;
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
