package com.actility.thingpark.wlogger.accesscode;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AccessCode {
  private static final String DIGEST_KEY = "h";
  private AccessCodePayload payload;
  private String signature;

  public AccessCode(AccessCodePayload payload, String signature) {
    this.payload = payload;
    this.signature = signature;
  }

  public AccessCodePayload getPayload() {
    return this.payload;
  }

  public String stringify() {
    TreeMap<String, String> map = new TreeMap<>(payload.getContent());
    map.put(DIGEST_KEY, signature);
    return AccessCodeUtils.stringify(map);
  }

  public void verifySignature(String signature) {
    if (!StringUtils.equals(this.signature, signature)) {
      throw new DecodeAccessCodeException("Invalid digest");
    }
  }

  public static AccessCode decodeContent(String accessCode) {
    if (StringUtils.isBlank(accessCode)) {
      throw new DecodeAccessCodeException("Empty access code");
    }
    String[] parts = accessCode.split(";");
    Map<String, String> map = new HashMap<>();
    for (String s : parts) {
      int i = s.indexOf("=");
      if (i < 0) {
        throw new DecodeAccessCodeException("Invalid access code: " + accessCode);
      }
      map.put(s.substring(0, i), s.substring(i + 1));
    }
    String signature = map.remove(DIGEST_KEY);
    return new AccessCode(new AccessCodePayload(map), signature);
  }
}
