package com.actility.thingpark.wlogger.config;

public class EncryptedString {

    private final String value;

    public EncryptedString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
