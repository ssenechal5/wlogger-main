package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ConfigMapping(prefix = "httpClient")
public interface HttpClientConfig {
  int connectionRequestTimeout();

  int connectTimeout();

  int socketTimeout();

  int maxConnectionPerPool();

  int maxConnectionPerRoute();

  @ConfigProperty(name = "log.exclude-headers")
  Optional<String> excludedHeaders();

}
