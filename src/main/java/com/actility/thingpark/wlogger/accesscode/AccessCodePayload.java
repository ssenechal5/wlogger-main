package com.actility.thingpark.wlogger.accesscode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

public class AccessCodePayload implements Payload {
  public static Clock clock = Clock.systemUTC();
  static final String TIMESTAMP_KEY = "t";
  static final long DEFAULT_TIMEOUT = 5 * 60 * 1000L;
  static final TimeZone UTC = TimeZone.getTimeZone("UTC");

  private Map<String, String> content = new TreeMap<>();

  public AccessCodePayload(@Nonnull AccessCodeType type, @Nonnull String id) {
    Calendar c = Calendar.getInstance(UTC, Locale.US);
    c.setTimeInMillis(Instant.now(clock).toEpochMilli());
    content.put(TIMESTAMP_KEY, DatatypeConverter.printDateTime(c));
    content.put(type.getKey(), id);
  }

  public AccessCodePayload(@Nonnull Map<String, String> content) {
    this.content.putAll(content);
  }

  @Nonnull
  public String stringify() {
    return AccessCodeUtils.stringify(content);
  }

  public Map<String, String> getContent() {
    return content;
  }

  private long getTimestamp() {
    try {
      Calendar c = DatatypeConverter.parseDateTime(content.get(TIMESTAMP_KEY));
      return c.getTimeInMillis();
    } catch (IllegalArgumentException e) {
      throw new AccessCodeValidationException("Invalid timestamp", e);
    }
  }

  private boolean hasExpired(Instant instant) {
    long generationTime = getTimestamp();
    return ((instant.toEpochMilli() - generationTime) > DEFAULT_TIMEOUT);
  }

  @Nullable
  private AccessCodeType getType() {
    if (content.containsKey(AccessCodeType.SUBSCRIBER.getKey())) {
      return AccessCodeType.SUBSCRIBER;
    }
    if (content.containsKey(AccessCodeType.USER.getKey())) {
      return AccessCodeType.USER;
    }
    return null;
  }

  @Nullable
  public String getID() {
    AccessCodeType type = getType();
    if (type == AccessCodeType.USER) {
      return content.get(AccessCodeType.USER.getKey());
    }
    if (type == AccessCodeType.SUBSCRIBER) {
      return content.get(AccessCodeType.SUBSCRIBER.getKey());
    }
    return null; // unreacheable
  }

  void validateTimestamp(@Nonnull Instant instant) {
    if (hasExpired(instant)) {
      throw new AccessCodeValidationException("Expired Access Code");
    }
  }

  void validateType() {
    if (getType() == null) {
      throw new AccessCodeValidationException("Missing type");
    }
  }

  @Override
  public void validate() {
    validateTimestamp(Instant.now(clock));
    validateType();
  }
}
