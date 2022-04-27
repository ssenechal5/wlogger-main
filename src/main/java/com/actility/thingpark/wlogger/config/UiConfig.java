package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "ui")
public interface UiConfig {
  @WithName("localization.mode")
  String localizationMode();
}
