package com.actility.thingpark.wlogger.auth;

public class SuccessLogin {

    private final User user;
    private final String sessionToken;
    private final Scope scope;

    public SuccessLogin(String sessionToken, User user, Scope scope) {
        this.sessionToken = sessionToken;
        this.user = user;
        this.scope = scope;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public User getUser() {
        return user;
    }

    public Scope getScope() {
        return scope;
    }
}
