package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "wlogger")
public interface WloggerConfig {

  String configurationFolder();

  String version();

  String defaultLanguage();

  boolean passiveRoaming();

  boolean lteMode();

  String csvExportDelimiter();

  String accessCodeSecret();

  boolean adminLogin();

  int adminSessionLifetime();

  int maxAutomaticDecodedPackets();

  @WithName("subscriber.view-roaming-in-traffic")
  int subscriberViewRoamingInTraffic();
}
