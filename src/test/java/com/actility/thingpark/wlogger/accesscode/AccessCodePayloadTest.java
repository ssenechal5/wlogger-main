package com.actility.thingpark.wlogger.accesscode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccessCodePayloadTest {

  @BeforeEach
  void setUp() {
    AccessCodePayload.clock = Clock.systemUTC();
  }

  @Test
  void stringify_empty() {
    assertEquals("", new AccessCodePayload(new HashMap<>()).stringify());
  }

  @Test
  void getContent() {
    Instant now = LocalDateTime.of(2018, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
    AccessCodePayload.clock = Clock.fixed(now, ZoneOffset.UTC);
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.SUBSCRIBER, "id");
    assertEquals(2, payload.getContent().size());
    assertEquals(
            "2018-01-01T00:00:00Z", payload.getContent().get(AccessCodePayload.TIMESTAMP_KEY));
    assertEquals("id", payload.getContent().get(AccessCodeType.SUBSCRIBER.getKey()));
  }

  @Test
  void getID_subscriber() {
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.SUBSCRIBER, "id");
    assertEquals("id", payload.getID());
  }

  @Test
  void getID_user() {
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    assertEquals("id", payload.getID());
  }

  @Test
  void getID_null() {
    AccessCodePayload payload = new AccessCodePayload(new HashMap<>());
    assertNull(payload.getID());
  }

  @Test
  void validateTimestamp() {
    Instant now = LocalDateTime.of(2020, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
    AccessCodePayload.clock = Clock.fixed(now, ZoneOffset.UTC);
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    payload.validateTimestamp(now);
  }

  @Test
  void validateTimestamp_invalid_timestamp() {
    Instant now = Instant.now();
    Map<String, String> map = new HashMap<>();
    map.put(AccessCodePayload.TIMESTAMP_KEY, "invalid-timestamp");
    AccessCodePayload payload = new AccessCodePayload(map);
    assertThrows(
        AccessCodeValidationException.class,
        () -> payload.validateTimestamp(now),
        "Invalid timestamp");
  }

  @Test
  void validateTimestamp_expired() {
    Instant now = LocalDateTime.of(2020, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
    AccessCodePayload.clock = Clock.fixed(now, ZoneOffset.UTC);
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    assertThrows(
        AccessCodeValidationException.class,
        () -> payload.validateTimestamp(now.plusSeconds(6 * 60)),
        "Expired Access Code");
  }

  @Test
  void validateTimestamp_exact_expiration_time() {
    Instant now = LocalDateTime.of(2020, 1, 1, 0, 0).toInstant(ZoneOffset.UTC);
    AccessCodePayload.clock = Clock.fixed(now, ZoneOffset.UTC);
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    assertDoesNotThrow(
        () -> payload.validateTimestamp(now.plusMillis(AccessCodePayload.DEFAULT_TIMEOUT)));
    assertThrows(
        AccessCodeValidationException.class,
        () -> payload.validateTimestamp(now.plusMillis(AccessCodePayload.DEFAULT_TIMEOUT + 1)),
        "Expired Access Code");
  }

  @Test
  void validateType() {
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    assertDoesNotThrow(payload::validateType);
  }

  @Test
  void validateType_invalid() {
    AccessCodePayload payload = new AccessCodePayload(new HashMap<>());
    assertThrows(AccessCodeValidationException.class, payload::validateType, "Missing type");
  }

  @Test
  void validate() {
    AccessCodePayload payload = new AccessCodePayload(AccessCodeType.USER, "id");
    assertDoesNotThrow(payload::validate);
  }
}
