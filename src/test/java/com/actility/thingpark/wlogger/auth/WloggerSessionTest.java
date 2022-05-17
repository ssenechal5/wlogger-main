package com.actility.thingpark.wlogger.auth;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WloggerSessionTest {

  @Test
  void addScope() {
      WloggerSession same = new WloggerSession(
              new String("fooToken"),
              new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
              null);
      same.addScope(new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));

      assertNotNull(same.getScope("scopeId"));
  }

  @Test
  void getAuthenticatedUser() {
      WloggerSession same = new WloggerSession(
              new String("fooToken"),
              new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
              null);
      same.addScope(new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));

      assertNotNull(same.getAuthenticatedUser());
  }

  @Test
  void getScope() {
      WloggerSession same = new WloggerSession(
              new String("fooToken"),
              new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
              null);
      same.addScope(new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));

      assertNotNull(same.getScope("scopeId"));
  }

  @Test
  void getFirstScope() {
      WloggerSession same = new WloggerSession(
              new String("fooToken"),
              new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
              null);
      same.addScope(new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));

      assertNotNull(same.getFirstScope());
  }

  @Test
  void getSessionToken() {
      WloggerSession same = new WloggerSession(
              new String("fooToken"),
              new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
              null);
      same.addScope(new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));

      assertNotNull(same.getSessionToken());
      assertEquals("fooToken",same.getSessionToken());
  }

    private static class equalsTestCase {
        public final String name;
        public final WloggerSession first;
        public final Object other;
        public final boolean expected;

        public equalsTestCase(String name, WloggerSession first, Object other, boolean expected) {
            this.name = name;
            this.first = first;
            this.other = other;
            this.expected = expected;
        }
    }

    private static List<equalsTestCase> cases = getEqualsTestCases();

    private static List<equalsTestCase> getEqualsTestCases() {
        WloggerSession same = new WloggerSession(
                new String("fooToken"),
                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subsriberId", null));
        return Arrays.asList(
                new equalsTestCase(
                        "Equals",
                        new WloggerSession(
                                new String("fooToken"),
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        new WloggerSession(
                                new String("fooToken"),
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        true
                ),
                new equalsTestCase(
                        "different sessionToken",
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        new WloggerSession(
                                "barToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        false
                ),
                new equalsTestCase(
                        "different user",
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "otherUserId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        false
                ),
                new equalsTestCase(
                        "different scope",
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        new WloggerSession(
                                "userId",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("otherScopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        false
                ),
                new equalsTestCase(
                        "null",
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        null,
                        false
                ),
                new equalsTestCase(
                        "same",
                        same,
                        same,
                        true
                ),
                new equalsTestCase(
                        "different type",
                        new WloggerSession(
                                "fooToken",
                                new User(UserType.SMP_END_USER, "userId", "John", "Doe", "en", "oidcUserID"),
                                new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subscriberId", null)),
                        new String("foobar"),
                        false
                )
        );
    }

    @TestFactory
    Stream<DynamicTest> testEquals() {
        return cases.stream().
                map(c -> DynamicTest.
                        dynamicTest(c.name, () -> assertEquals(c.expected,
                                c.first.equals(c.other)))
                );
    }

    @TestFactory
    Stream<DynamicTest> testHashCode() {
        return cases.stream().filter(c -> c.first != null && c.other != null).
                map(c -> DynamicTest.
                        dynamicTest(c.name, () -> {
                            if (c.expected) {
                                assertEquals(c.first.hashCode(),
                                        c.other.hashCode());
                            } else {
                                assertNotEquals(c.first.hashCode(),
                                        c.other.hashCode());
                            }
                        })
                );
    }
}