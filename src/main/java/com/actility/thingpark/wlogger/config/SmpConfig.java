package com.actility.thingpark.wlogger.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "wlogger")
public interface SmpConfig {
    @WithName("application.id")
    String getApplicationID();

    @WithName("supplier.module.id")
    String getSupplierModuleID();

    @WithName("subscriber.module.id")
    String getSubscriberModuleID();
}
