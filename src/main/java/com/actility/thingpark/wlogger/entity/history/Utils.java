package com.actility.thingpark.wlogger.entity.history;

public class Utils {

  public static String getToStringOrEmpty(Boolean obj) {
    if (obj == null)
      return "";
    else {
      if (obj.booleanValue())
        return "1";
      else
        return "0";
    }
  }

  public static String getToString(Boolean obj) {
    if (obj == null)
      return "0";
    else {
      if (obj.booleanValue())
        return "1";
      else
        return "0";
    }
  }

  public static String getToStringOrNull(Boolean obj) {
    if (obj == null)
      return "null";
    else {
      if (obj.booleanValue())
        return "1";
      else
        return "0";
    }
  }

  public static String getToString(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString();
  }

  public static String getIsmBand(Object obj) {
    if (obj == null)
      return "";
    else {
      switch (obj.toString()) {
        case "eu868":
          return "EU 863-870MHz";
        case "eu433":
          return "EU 433MHz";
        case "cn779":
          return "China 779-787MHz";
        case "as923":
          return "AS923MHz";
        case "kr920":
          return "South Korea 920-923MHz";
        case "sg920":
          return "Singapore 920-923MHz";
        case "tw920":
          return "Taiwan 920-923MHz";
        case "us915":
          return "US 902-928MHz";
        case "au915":
          return "Australia 915-928MHz";
        case "cn470":
          return "CN 470-510MHz";
        case "ru864":
          return "RU 864-870MHz";
        case "ww2g4":
          return "WW2400";
        default:
          return "";
      }
    }
  }

  public static String getEmptyIfNull(String obj) {
    if (obj == null)
      return "";
    else if (obj.equals("null"))
      return "";
    else
      return obj;
  }

  public static String getToStringOrEmpty(Object obj) {
    if (obj == null)
      return "";
    else
      return obj.toString();
  }

  public static String getToStringOrNull(Object obj) {
    if (obj == null)
      return "null";
    else
      return obj.toString();
  }

  public static String getToStringOrNone(Object obj) {
    if (obj == null)
      return "None";
    else
      return obj.toString();
  }

}
