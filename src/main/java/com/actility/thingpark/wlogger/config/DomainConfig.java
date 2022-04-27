package com.actility.thingpark.wlogger.config;

import org.eclipse.microprofile.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class DomainConfig {
  private static final String GMAPS_API_KEY = UiMapConfig.PREFIX + ".gmaps.api-key";
  private static final String GMAPS_ECJ_ENCRYPT_MODE = UiMapConfig.PREFIX + ".gmaps.ecj-encrypt-mode";
  private static final String LEAFLET_URL_TEMPLATE = UiMapConfig.PREFIX + ".leaflet.url-template";
  private static final String BMAPS_API_KEY = UiMapConfig.PREFIX + ".bmaps.api-key";

  @Inject
  Config config;

  public Optional<String> getGmapsApiKey(String domain) {
    return getDomainCustomProperty(GMAPS_API_KEY, domain, String.class);
  }

  public Optional<Boolean> getGmapsEcjEncryptMode(String domain) {
    return getDomainCustomProperty(GMAPS_ECJ_ENCRYPT_MODE, domain, Boolean.class);
  }

  public Optional<String> getLeafletUrlTemplate(String domain) {
    return getDomainCustomProperty(LEAFLET_URL_TEMPLATE, domain, String.class);
  }

  public Optional<String> getBmapsApiKey(String domain) {
    return getDomainCustomProperty(BMAPS_API_KEY, domain, String.class);
  }

  private <T> Optional<T> getDomainCustomProperty(String key, String domain, Class<T> propertyType) {
    Optional<T> defaultConf = config.getOptionalValue(key, propertyType);
    Optional<T> operatorConf = config.getOptionalValue(key+"."+domain, propertyType);
    if(operatorConf.isPresent()) {
      return operatorConf;
    }
    return defaultConf;
  }

}
