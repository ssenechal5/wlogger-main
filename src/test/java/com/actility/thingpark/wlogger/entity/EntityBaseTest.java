package com.actility.thingpark.wlogger.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityBaseTest {

  EntityBase entityBase = new EntityBase();

  @Test
  void getUID() {
    entityBase.setUID(1L);
    assertNotNull(entityBase.getUID());
    assertEquals(1L,entityBase.getUID());
  }

  @Test
  void testEquals() {
    entityBase.setUID(1L);
    assertFalse(entityBase.equals(null));
    assertTrue(entityBase.equals(entityBase));
    assertFalse(entityBase.equals(new EntityBase()));
    assertFalse(entityBase.equals("new EntityBase()"));
  }

  @Test
  void testHashCode() {
    entityBase.setUID(1L);
    assertNotEquals(1 ,entityBase.hashCode());
  }

  @Test
  void equalsEntity() {
    entityBase.setUID(1L);
    assertFalse(entityBase.equalsEntity(null));
    assertTrue(entityBase.equalsEntity(entityBase));
    assertFalse(entityBase.equalsEntity(new EntityBase()));
  }
}