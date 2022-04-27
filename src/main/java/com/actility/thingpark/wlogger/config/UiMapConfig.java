package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = UiMapConfig.PREFIX)
public interface UiMapConfig {
  String PREFIX = "ui.map";

  @WithName("module.access")
  String moduleAccess();

  @WithName("direct.access")
  String directAccess();
}
