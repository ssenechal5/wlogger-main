package com.actility.thingpark.wlogger.utils;

import org.apache.commons.codec.digest.DigestUtils;

public final class CodingUtils {

    public static boolean checkSHA256Password(String thingparkPassword, String password) {
        if (thingparkPassword == null || password == null)
            return false;

        String salt = thingparkPassword.substring(0, 64);
        String storedPassword = thingparkPassword.substring(64);
        String hashed = DigestUtils.sha256Hex(salt + password);
        return storedPassword.equals(hashed);
    }
}
