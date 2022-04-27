package com.actility.thingpark.wlogger.utils;

import java.util.List;

public final class JPAUtils {
  /**
   * Used to get the only possible result (or null if none) of a JPA query. Multiple results should
   * never happen (corrupt design such as lack of DB unique constraints) and lead to a fail-fast
   * error.
   */
  public static <T> T getSingleOrNull(List<T> results) {
    if (results == null || results.isEmpty()) {
      return null;
    }
    if (results.size() > 1) {
      throw new IllegalStateException("Multiple results");
    }
    return results.get(0);
  }

  /** Used to get the first possible result (or null if none) of a JPA query. */
  public static <T> T getFirstOrNull(List<T> results) {
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }
}
