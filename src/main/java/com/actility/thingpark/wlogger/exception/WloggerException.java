package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.Errors;

public class WloggerException extends Exception {

  private final int statusCode;
  private final int errorCode;

  public WloggerException(int statusCode) {
    this(statusCode, 0, null, null);
  }

  public WloggerException(int statusCode, String message) {
    this(statusCode, 0, message, null);
  }

  public WloggerException(int statusCode, int errorCode) {
    this(statusCode, errorCode, null, null);
  }

  public WloggerException(int statusCode, String message, Throwable cause) {
    this(statusCode, 0, message, cause);
  }

  public WloggerException(int statusCode, int errorCode, String message) {
    this(statusCode, errorCode, message, null);
  }

  public WloggerException(int statusCode, int errorCode, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
    this.errorCode = errorCode;
  }

  public static WloggerException invalidQueryParameter(Throwable cause) {
    return new WloggerException(
        400,
        Errors.INVALID_QUERY_PARAMETERS,
        Errors.getErrorMessage(Errors.INVALID_QUERY_PARAMETERS, cause.getMessage()),
        cause);
  }

  public static WloggerException invalidQueryParameter(String message) {
    return exception(400, Errors.INVALID_QUERY_PARAMETERS, message);
  }

  public static WloggerException applicationError(int errorCode, Object... args) {
    return exception(409, errorCode, args);
  }

  public static WloggerException applicationError(Throwable cause, int errorCode, Object... args) {
    return exception(cause, 409, errorCode, args);
  }

  public static WloggerException forbidden(int errorCode, Object... args) {
    return exception(403, errorCode, args);
  }

  public static WloggerException badRequest(int errorCode, Object... args) {
    return exception(400, errorCode, args);
  }

  public static WloggerException notAuthorized() {
    return new WloggerException(
        403, Errors.PERMISSION_DENIED, "User is not authorized to perform this operation");
  }

  public static WloggerException notLoggedIn() {
    return new WloggerException(403, Errors.FORBIDDEN, "User is not logged in");
  }

  public static WloggerException notLoggedIn401(int errorCode, String message) {
    return new WloggerException(401, errorCode, message);
  }

  public static WloggerException internalError() {
    return WloggerException.internalError("Internal Server Error");
  }

  public static WloggerException internalError(String message) {
    return new WloggerException(500, message);
  }

  public static WloggerException internalError(Throwable t) {
    return new WloggerException(500, "An unexpected error has occured: " + t.getMessage(), t);
  }

  public static WloggerException notFound(String resource) {
    return exception(404, Errors.UNKNOWN_RESOURCE, resource);
  }

  public static WloggerException notFound(int errorCode, Object... args) {
    return exception(404, errorCode, args);
  }

  public static WloggerException invalidValue(String message) {
    return exception(400, Errors.INVALID_DOCUMENT, message);
  }

  public static WloggerException invalidDocument(String message) {
    return exception(400, Errors.INVALID_DOCUMENT, message);
  }

  public static WloggerException exception(int statusCode, int errorCode, Object... args) {
    return new WloggerException(statusCode, errorCode, Errors.getErrorMessage(errorCode, args));
  }

  public static WloggerException exception(
      Throwable cause, int statusCode, int errorCode, Object... args) {
    return new WloggerException(
        statusCode, errorCode, Errors.getErrorMessage(errorCode, args), cause);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
