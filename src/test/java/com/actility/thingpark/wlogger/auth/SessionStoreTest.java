package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.config.WloggerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SessionStoreTest {

    HttpServletRequest request;
    HttpSession session;
    SessionStore store;
    WloggerConfig wloggerConfig;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        wloggerConfig = mock(WloggerConfig.class);
        when(request.getSession()).thenReturn(session);
        store = new SessionStore(request, wloggerConfig);
    }

    @Test
    void getEmptySession() {
        when(session.getAttribute(SessionStore.SESSION_KEY)).thenReturn(null);
        assertEquals(Optional.empty(), store.getSession());
        verify(request).getSession();
        verify(session).getAttribute(SessionStore.SESSION_KEY);
    }

    @Test
    void getSession() {
        WloggerSession wloggerSession = new WloggerSession(
                "fooToken",
                new User(UserType.SMP_END_USER, "fooId", "John", "Doe", "en", "oidcUserID"),
                new Scope("scopeId", Scope.ScopeType.UNLIMITED, "*", null));
        when(session.getAttribute(SessionStore.SESSION_KEY)).thenReturn(
                wloggerSession);
        assertEquals(Optional.of(wloggerSession), store.getSession());
        verify(request).getSession();
        verify(session).getAttribute(SessionStore.SESSION_KEY);
    }

    @Test
    void setSession() {
        WloggerSession wloggerSession = new WloggerSession(
                "fooToken",
                new User(UserType.SMP_END_USER, "fooId", "John", "Doe", "en", "oidcUserID"),
                new Scope("scopeId", Scope.ScopeType.UNLIMITED, "*", null));
        store.setSession(wloggerSession);
        verify(session).setAttribute(SessionStore.SESSION_KEY, wloggerSession);
    }

    @Test
    void clear() {
        store.clear();
        verify(request).getSession();
        verify(session).invalidate();
    }
}