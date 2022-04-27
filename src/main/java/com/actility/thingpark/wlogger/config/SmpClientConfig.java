package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "wlogger.smp-client")
public interface SmpClientConfig {
    String uri();

    String login();

    EncryptedString password();
}
