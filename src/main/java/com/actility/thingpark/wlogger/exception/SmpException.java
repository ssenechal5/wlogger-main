package com.actility.thingpark.wlogger.exception;

@SuppressWarnings("serial")
public class SmpException extends Exception {

  public SmpException(String message) {
    super(message);
  }

  public SmpException(String message, Throwable cause) {
    super(message, cause);
  }
}
