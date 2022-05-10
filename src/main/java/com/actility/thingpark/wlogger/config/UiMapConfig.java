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

  @WithName("gmaps.apikey")
  String gmapsApikey();

  @WithName("gmaps.ecjencryptmode")
  String gmapsEcjencryptmode();

  @WithName("bmaps.apikey")
  String bmapsApikey();

  @WithName("leaflet.urltemplate")
  String leafletUrltemplate();

}
