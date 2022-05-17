package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorEntityTest {

  ActorEntity actorEntity = new ActorEntity("id","href", ActorEntity.State.ACTIVE);

  @Test
  void getHref() {
    assertEquals("href",actorEntity.getHref());
  }

  @Test
  void getID() {
    assertEquals("id",actorEntity.getID());
  }

  @Test
  void getState() {
    assertEquals(ActorEntity.State.ACTIVE,actorEntity.getState());
  }

  @Test
  void getStateTimestamp() {
    assertEquals(actorEntity.getStateTimestamp(),actorEntity.getCreationTimestamp());
  }

  @Test
  void getCreationTimestamp() {
    assertEquals(actorEntity.getCreationTimestamp(),actorEntity.getStateTimestamp());
  }
}