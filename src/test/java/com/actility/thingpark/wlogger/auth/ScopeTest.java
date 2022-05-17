package com.actility.thingpark.wlogger.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTest {

    private Scope scope = new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "subId", null);

    @Test
    void testToString() {
        assertEquals("SUBSCRIBER:subId", new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "subId", null).toString());
        assertEquals("NETWORK_PARTNER:npId", new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "npId", null).toString());
        assertEquals("UNLIMITED:*", new Scope("foobar", Scope.ScopeType.UNLIMITED, "*", null).toString());
    }

    @Test
    void testEquals() {
        assertEquals(new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "subId", null), new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "subId", null));
        assertEquals(new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "npId", null), new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "npId", null));
        assertEquals(new Scope("foobar", Scope.ScopeType.UNLIMITED, "*", null), new Scope("foobar", Scope.ScopeType.UNLIMITED, "*", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "subId", null), new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "otherSubId", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "npId", null), new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "otherNpId", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.UNLIMITED, "*", null), new Scope("foobar", Scope.ScopeType.UNLIMITED, "**", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "id", null), new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "id", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.NETWORK_PARTNER, "id", null), new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "id", null));
        assertNotEquals(new Scope("foobar", Scope.ScopeType.UNLIMITED, "id", null), new Scope("foobar", Scope.ScopeType.SUBSCRIBER, "id", null));
        assertFalse(new Scope("foobar", Scope.ScopeType.UNLIMITED, "*", null).equals(null));

    }

  @Test
  void getId() {
      assertEquals("foobar",scope.getId());
  }

  @Test
  void getContextId() {
      assertEquals("subId",scope.getContextId());
  }

  @Test
  void getScopeType() {
      assertEquals(Scope.ScopeType.SUBSCRIBER,scope.getScopeType());
  }

  @Test
  void getCustomerId() {
      assertEquals("subId",scope.getCustomerId());
  }

  @Test
  void testHashCode() {
      assertNotEquals(1,scope.hashCode());
  }
}