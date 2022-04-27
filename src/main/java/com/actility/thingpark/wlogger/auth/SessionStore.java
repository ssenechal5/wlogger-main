package com.actility.thingpark.wlogger.auth;

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

    final int maxInactiveInterval;

    @Inject
    SessionStore(HttpServletRequest request,
                 @ConfigProperty(name = "wlogger.max-inactive-interval")
                         int maxInactiveInterval) {
        this.request = request;
        this.maxInactiveInterval = maxInactiveInterval;
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
