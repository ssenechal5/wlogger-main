package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.accesscode.AccessCodePayload;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class AccessCodeServiceTest {

  private AccessCodeService service;
  private WloggerConfig wloggerConfig;

  @BeforeEach
  void setUp() {
    AccessCodePayload.clock = Clock.systemUTC();
    wloggerConfig =
        new WloggerConfig() {
          public String configurationFile() {
            return null;
          }

          @Override
          public String version() {
            return null;
          }

          @Override
          public String defaultLanguage() {
            return null;
          }

          @Override
          public boolean passiveRoaming() {
            return false;
          }

          @Override
          public boolean lteMode() {
            return false;
          }

          @Override
          public String csvExportDelimiter() {
            return null;
          }

          @Override
          public String accessCodeSecret() {
            return "___1414!.tayaat182277:.";
          }

          @Override
          public boolean adminLogin() {
            return false;
          }

          @Override
          public int adminSessionLifetime() {
            return 0;
          }

          @Override
          public String configurationFolder() {
            return null;
          }

          @Override
          public int maxAutomaticDecodedPackets() { return 500; }

            @Override
            public int subscriberViewRoamingInTraffic() {
                return 0;
            }

            @Override
            public int maxInactiveInterval() {
                return 0;
            }
        };
    service = new AccessCodeService(wloggerConfig);
  }

  @Test
  void computeSignature() {
    assertEquals(
        DigestUtils.sha256Hex("content___1414!.tayaat182277:."),
        service.computeSignature("content"));
  }

  @Test
  void base64Encode() {
    assertEquals("dD9zdC1jb250ZW50IA==", AccessCodeService.base64Encode("tést-content "));
  }

  @Test
  void base64Encode_with_utf8_expected() {
    assertNotEquals("dMOpc3QtY29udGVudCA=", AccessCodeService.base64Encode("tést-content "));
  }

  @Test
  void base64Decode() {
    assertEquals("t?st-content ", AccessCodeService.base64Decode("dD9zdC1jb250ZW50IA=="));
  }

  @Test
  void base64Decode_with_utf8_expected() {
    assertNotEquals("tést-content ", AccessCodeService.base64Decode("dMOpc3QtY29udGVudCA="));
  }

  @Test
  void generateSubscriberAccessCode() {
    AccessCodePayload.clock = Clock.fixed(LocalDateTime.of(2020,1,31, 15,18,53,735000000).toInstant(ZoneOffset.ofHours(1)), ZoneOffset.ofHours(1));
    String accessCode = service.generateSubscriberAccessCode("tpk-12");
      assertNotNull(accessCode);
      assertEquals("aD02YTYyZDA5NjI2ZGJhZDc2MmYzMzAyOTgyOWVhYTEwZDlmMTA5NjUxZjNhMzMzNDlhYWE2NzljMjAwMjQyYjFiO3M9dHBrLTEyO3Q9MjAyMC0wMS0zMVQxNDoxODo1My43MzVa", accessCode);
  }

  @Test
  void generateUserAccessCode() {
    AccessCodePayload.clock = Clock.fixed(LocalDateTime.of(2020,1,1, 0,0,0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    String accessCode = service.generateUserAccessCode("usr-1");
      assertNotNull(accessCode);
      assertEquals("ZT11c3ItMTtoPTZhNDg0YzA3YWE1ODgyMzM4ZGI0ZjVhZWY0ODgxNWE0ZDExZmU2OTkyZDI0ZWZlZTM4MWEyZDJmMzYwMDRmY2I7dD0yMDIwLTAxLTAxVDAwOjAwOjAwWg==", accessCode);
  }

  @Test
  void decode() {
    AccessCodePayload.clock =
        Clock.fixed(LocalDateTime.of(2018, 1, 1, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    AccessCodePayload payload =
        service.decode(
            "aD05OWUwMjM5ODYxNmIwZGFiYTY2MDdiZDg1OTM1NTViOWIyNDk0NTkzOGNhOWVkNWRjZmQyZWUzN2E5YTk5MTM0O3M9dHBrLTEyO3Q9MjAyMC0wMS0zMVQxNToxODo1My43MzUrMDE6MDA=");
    assertNotNull(payload);
  }
}
