package com.actility.thingpark.wlogger.auth;

import java.util.Optional;

public interface Store {

    Optional<WloggerSession> getSession();

    void setSession(WloggerSession session);

    void clear();
}
