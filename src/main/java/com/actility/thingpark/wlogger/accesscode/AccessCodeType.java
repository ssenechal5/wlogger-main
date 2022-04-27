package com.actility.thingpark.wlogger.accesscode;

public enum AccessCodeType {
  USER("e"), SUBSCRIBER("s");
  private String key;

  AccessCodeType(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
