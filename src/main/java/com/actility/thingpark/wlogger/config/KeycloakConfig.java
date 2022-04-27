package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.Optional;

@ConfigMapping(prefix = "keycloak")
public interface KeycloakConfig {

  @WithName("uri")
  String uri();

  @WithName("uri.gui")
  String uriGui();

  @WithName("discovery.uri")
  String discoveryUri();

  @WithName("client.gui")
  String clientGui();

  @WithName("client.api")
  String clientApi();

  @WithName("realm")
  Optional<String> realm(); //dev only

  @WithName("response.mode")
  String responseMode();

}
