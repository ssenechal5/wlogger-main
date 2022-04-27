package com.actility.thingpark.wlogger.accesscode;

import java.util.Map;
import java.util.stream.Collectors;

public class AccessCodeUtils {
  static String stringify(Map<String, String> map) {
    return map.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(";"));
  }
}
