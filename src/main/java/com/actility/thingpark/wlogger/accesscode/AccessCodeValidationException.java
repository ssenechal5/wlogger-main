package com.actility.thingpark.wlogger.accesscode;

public class AccessCodeValidationException extends RuntimeException {
  public AccessCodeValidationException(String message) {
    super(message);
  }

  public AccessCodeValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
