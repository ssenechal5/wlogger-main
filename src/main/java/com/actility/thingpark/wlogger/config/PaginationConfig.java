package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "wlogger.pagination")
public interface PaginationConfig {

  int pageSize();

  int maxPages();
}
