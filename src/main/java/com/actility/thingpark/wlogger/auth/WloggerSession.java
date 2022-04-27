package com.actility.thingpark.wlogger.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class WloggerSession implements Serializable {
    private static final long serialVersionUID = 5751813093553547448L;
    private final User authenticatedUser;
    private final transient HashMap<String, Scope> scopes;
    private final String sessionToken;

    public WloggerSession(String sessionToken, User user, Scope scope) {
        this.sessionToken = sessionToken;
        this.authenticatedUser = user;
        this.scopes = new HashMap<>();
        if (scope != null)
            addScope(scope);
    }

    public void addScope(Scope scope) {
        this.scopes.put(scope.getId(), scope);
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public Scope getScope(String id) {
        return scopes.get(id);
    }

    public Scope getFirstScope() {
        return scopes.entrySet().iterator().next().getValue();
    }

    public String getSessionToken() {
        return sessionToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WloggerSession session = (WloggerSession) o;
        return authenticatedUser.equals(session.authenticatedUser) &&
                scopes.equals(session.scopes) &&
                sessionToken.equals(session.sessionToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatedUser, scopes, sessionToken);
    }
}
