package com.actility.thingpark.wlogger.exception;

@SuppressWarnings("serial")
public class SmpClientAuthException extends Exception {
  private final int errorCode;

  public SmpClientAuthException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
