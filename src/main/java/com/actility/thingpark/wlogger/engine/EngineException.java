package com.actility.thingpark.wlogger.engine;

import com.actility.thingpark.wlogger.engine.model.ErrorInfo;

public class EngineException extends Exception {

  private final ErrorInfo error;

  public ErrorInfo getError() {
    return error;
  }

  public EngineException(String message, ErrorInfo error, Throwable cause) {
    super(message, cause);
    this.error = error;
  }
}
