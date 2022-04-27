package com.actility.thingpark.wlogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Errors {

  private static final Logger logger = Logger.getLogger(Errors.class.getName());

  // COMMON : 1 - 30
  public static final int INVALID_QUERY_PARAMETERS = 1;
  public static final int INVALID_DOCUMENT = 2;
  public static final int PERMISSION_DENIED = 3;
  public static final int FORBIDDEN = 4;
  public static final int EXCEPTION = 6;
  public static final int EXCEPTION_NOT_FOUND = 7;
  public static final int EXCEPTION_BAD_REQUEST = 8;
  public static final int EXCEPTION_XML_PARSING = 10;
  public static final int HTTP_ENGINE_EXCEPTION = 11;

  // NOT FOUND RESOURCES : 31 - 80
  public static final int UNKNOWN_RESOURCE = 31;
  public static final int UNKNOWN_OPERATOR = 34;
  public static final int UNKNOWN_SUBSCRIPTION = 51;

  // CONFIGURATION : 91 - 100
  public static final int CONFIGURATION_MODULE_ID_NOT_FOUND = 91;
  public static final int CONFIGURATION_APPLICATION_ID_NOT_FOUND = 92;
  public static final int CONFIGURATION_SMP_ID_NOT_FOUND = 93;
  public static final int CONFIGURATION_ENGINE_ID_NOT_FOUND = 94;

  // OCC : 101 - 110
  public static final int AUTHENTICATION_FAILED = 108;

  // LANGUAGE : 111 - 120

  // COMMON : 121 - 140
  public static final int UNKNOWN_ID = 121;

  // AUTH : 141 - 160
  public static final int AUTH_FAILED = 141;
  public static final int AUTH_SMP_FAILED = 142;
  public static final int AUTH_MISSING_ACCESS_CODE = 143;
  public static final int AUTH_NO_SUBSCRIPTION_FOR_HREF_FOUND = 149;
  public static final int AUTH_NO_SMP_SUBSCRIPTION_FOR_HREF_FOUND = 151;
  public static final int AUTH_FAILED_SUBSCRIPTION_SUSPENDED_FOR_HREF = 153;
  public static final int AUTH_FAILED_SUBSCRIPTION_DEACTIVATED_FOR_HREF = 154;
  public static final int AUTH_FAILED_NO_SUBSCRIPTION_FOR_ID_FOUND = 158;
  public static final int AUTH_FAILED_SUBSCRIPTION_DEACTIVATED_FOR_ID = 159;
  public static final int AUTH_FAILED_SUBSCRIPTION_SUSPENDED_FOR_ID = 162;

  public static final int AUTH_FAILED_INVALID_SESSION = 163;
  public static final int AUTH_FAILED_NOT_AUTHENTICATED = 164;

  // CUSTOMERS : 201 - 300
  public static final int SUBSCRIPTION_EXISTS_FOR_HREF = 201;
  public static final int NO_SUBSCRIPTION_FOR_HREF = 205;
  public static final int KEY_ENCRYPTION_FAILED = 228;

  public static final int ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID = 241;
  public static final int NO_ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID = 242;

  // SUBSCRIPTION LICENSE MANAGER : 451 - 500

  // SYSTEMS : 501 - 600
  public static final int NO_OPERATOR_FOR_ID = 501;
  public static final int CSV_FILE_GENERATION_FAIL = 504;

  protected static final String[] messages = new String[520];

  static {
    messages[INVALID_QUERY_PARAMETERS] = "Invalid query parameter: %s";
    messages[INVALID_DOCUMENT] = "Invalid document: %s";
    messages[PERMISSION_DENIED] = "Permission denied, not authorized to perform the operation";
    messages[FORBIDDEN] = "Access denied, user not logging or session expired";
    messages[EXCEPTION] = "Unexpected exception: %s";
    messages[EXCEPTION_NOT_FOUND] = "Not found exception: %s";
    messages[EXCEPTION_BAD_REQUEST] = "Bad request exception: %s";
    messages[EXCEPTION_XML_PARSING] = "XML parsing exception: %s";
    messages[HTTP_ENGINE_EXCEPTION] = "Http Engine exception: %s";
    messages[UNKNOWN_RESOURCE] = "Resource not found: %s";
    messages[UNKNOWN_OPERATOR] = "Operator not found: %s";
    messages[UNKNOWN_SUBSCRIPTION] = "Subscription not found: %s";
    messages[CONFIGURATION_MODULE_ID_NOT_FOUND] = "The module ID has not been found in the configuration file: %s";
    messages[CONFIGURATION_APPLICATION_ID_NOT_FOUND] = "The application ID has not been found in the configuration file: %s";
    messages[CONFIGURATION_SMP_ID_NOT_FOUND] = "The SMP URI has not been found in the configuration file: %s";
    messages[CONFIGURATION_ENGINE_ID_NOT_FOUND] = "The TPX Engine URI has not been found in the configuration file: %s";
    messages[AUTHENTICATION_FAILED] = "Authentication failed: %s";
    messages[UNKNOWN_ID] = "Unknown ID: %s";
    messages[AUTH_FAILED] = "Failed to authenticate user: %s";
    messages[AUTH_SMP_FAILED] = "SMP authentication failed: %s";
    messages[AUTH_MISSING_ACCESS_CODE] = "Missing access code";
    messages[AUTH_NO_SUBSCRIPTION_FOR_HREF_FOUND] = "No Subscription in WLogger for href: %s";
    messages[AUTH_NO_SMP_SUBSCRIPTION_FOR_HREF_FOUND] = "No SMP subscription for: %s";
    messages[AUTH_FAILED_SUBSCRIPTION_SUSPENDED_FOR_HREF] = "Auth failed, Subscription SUSPENDED for href: %s";
    messages[AUTH_FAILED_SUBSCRIPTION_DEACTIVATED_FOR_HREF] = "Auth failed, Subscription DEACTIVATED for href: %s";
    messages[AUTH_FAILED_NO_SUBSCRIPTION_FOR_ID_FOUND] = "Auth failed, no Subscription in WLogger for subscriber ID: %s";
    messages[AUTH_FAILED_SUBSCRIPTION_DEACTIVATED_FOR_ID] = "Auth failed, Subscription DEACTIVATED for subscriber ID: %s";
    messages[AUTH_FAILED_SUBSCRIPTION_SUSPENDED_FOR_ID] = "Auth failed, Subscription SUSPENDED for subscriber ID: %s";
    messages[AUTH_FAILED_INVALID_SESSION] = "Auth failed, invalid session";
    messages[AUTH_FAILED_NOT_AUTHENTICATED] = "Auth failed, not authenticated";
    messages[SUBSCRIPTION_EXISTS_FOR_HREF] = "A Subscription exists for href: %s";
    messages[NO_SUBSCRIPTION_FOR_HREF] = "No Subscription for href: %s";
    messages[KEY_ENCRYPTION_FAILED] = "Error during key[%s] encryption : %s";
    messages[ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID] = "An ACTIVE Subscription exists for ID: %s";
    messages[NO_ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID] = "No ACTIVE Subscription exists for ID: %s";
    messages[NO_OPERATOR_FOR_ID] = "No operator for ID: %s";
    messages[CSV_FILE_GENERATION_FAIL] = "CSV file generation fail : %s";
  }

  private Errors() {
    throw new IllegalStateException("Errors class");
  }

  public static String getErrorMessage(int code, Object... args) {
    String template = messages[code];
    try {
      return String.format(template, args);
    } catch (Exception e) {
      logger.log(Level.INFO,e.getMessage(),e);
      return template;
    }
  }

}
