package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.config.WloggerConfig;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequestScoped
@DefaultBean
public class SessionStore implements Store {

    static final String SESSION_KEY = SessionStore.class.getName();

    final HttpServletRequest request;

    private WloggerConfig wloggerConfig;

    final int maxInactiveInterval;

    @Inject
    SessionStore(HttpServletRequest request,
                 final WloggerConfig wloggerConfig) {
        this.request = request;
        this.wloggerConfig = wloggerConfig;
        this.maxInactiveInterval = this.wloggerConfig.maxInactiveInterval();
    }

    @Override
    public void setSession(WloggerSession session) {
        this.request.getSession().setAttribute(SESSION_KEY, session);
    }

    @Override
    public Optional<WloggerSession> getSession() {
        WloggerSession session = (WloggerSession) this.request.getSession().getAttribute(SESSION_KEY);
        return Optional.ofNullable(session);
    }

    @Override
    public void clear() {
        this.request.getSession().invalidate();
    }
}
