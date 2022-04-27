package com.actility.thingpark.wlogger.utils;

import org.apache.commons.codec.binary.Hex;

import java.util.Random;

public final class IDUtils {

  private static final Random random = new Random();

  public static String newID() {
    byte[] b = new byte[8];
    synchronized (random) {
      random.nextBytes(b);
    }
    return Hex.encodeHexString(b);
  }
}
