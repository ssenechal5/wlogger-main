package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatedEntityTest {

  Operator operator = new Operator("id","href",120);
  OperatedEntity operatedEntity = new OperatedEntity(operator,
          "id","href", ActorEntity.State.ACTIVE);

  @Test
  void getOperator() {
    assertEquals(operatedEntity.getOperator(),operator);
    assertEquals(operatedEntity.getOperator().getID(),operator.getID());
    assertEquals(operatedEntity.getOperator().getHref(),operator.getHref());
    assertEquals(operatedEntity.getOperator().getID(),operator.getID());
  }
}