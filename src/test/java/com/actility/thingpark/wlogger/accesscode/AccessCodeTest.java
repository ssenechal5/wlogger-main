package com.actility.thingpark.wlogger.accesscode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccessCodeTest {

  @BeforeEach
  void setUp() {
    AccessCodePayload.clock = Clock.systemUTC();
  }

  @Test
  void decodeContent_null() {
    assertThrows(DecodeAccessCodeException.class, () -> AccessCode.decodeContent(null), "Empty access code");
  }

  @Test
  void decodeContent_empty() {
    assertThrows(DecodeAccessCodeException.class, () -> AccessCode.decodeContent(""), "Empty access code");
  }

  @Test
  void decodeContent_blank() {
    assertThrows(DecodeAccessCodeException.class, () -> AccessCode.decodeContent(" "), "Empty access code");
  }

  @Test
  void decodeContent_missingSemicolon() {
    assertThrows(DecodeAccessCodeException.class, () -> AccessCode.decodeContent("test"), "Invalid access code: test");
  }

  @Test
  void decodeContent() {
    AccessCodePayload.clock =
            Clock.fixed(LocalDateTime.of(2018, 1, 1, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    AccessCode accessCode = AccessCode.decodeContent("h=signature;s=tpk-12;t=2020-01-31T15:18:53.735+01:00");
    assertNotNull(accessCode);
    accessCode.verifySignature("signature");
    accessCode.getPayload().validate();
    assertEquals("tpk-12", accessCode.getPayload().getID());
  }

  @Test
  void stringify_singleEntry() {
    assertEquals("h=signature", new AccessCode(new AccessCodePayload(new HashMap<>()), "signature").stringify());
  }

  @Test
  void stringify_multipleEntries() {
    Map<String, String> map = new HashMap<>();
    map.put("test", "test1");
    assertEquals("h=signature;test=test1", new AccessCode(new AccessCodePayload(map), "signature").stringify());
  }

  @Test
  void verifySignature() {
    AccessCode accessCode = AccessCode.decodeContent("h=signature;s=tpk-12;t=2020-01-31T15:18:53.735+01:00");
    assertNotNull(accessCode);
    accessCode.verifySignature("signature");
  }

  @Test
  void verifySignature_mismatch() {
    AccessCode accessCode = AccessCode.decodeContent("h=signature;s=tpk-12;t=2020-01-31T15:18:53.735+01:00");
    assertNotNull(accessCode);
    assertThrows(DecodeAccessCodeException.class, () -> accessCode.verifySignature("invalid"), "Invalid digest");
  }

  @Test
  void testDecodeContent() {}
}