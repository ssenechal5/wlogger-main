package com.actility.thingpark.wlogger.auth;

import com.actility.thingpark.wlogger.exception.WloggerException;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    AuthenticationService authenticationService;
    AuthenticationFilter filter;
    ContainerRequestContext containerRequestContext = mock(ContainerRequestContext.class);
    UriInfo uriInfo;
    User user;
    Scope scope;


    @BeforeEach
    void setup() {
        authenticationService = mock(AuthenticationService.class);
        filter = new AuthenticationFilter();
        filter.inject(authenticationService);
        containerRequestContext = mock(ContainerRequestContext.class);
        uriInfo = mock(UriInfo.class);
        when(containerRequestContext.getUriInfo()).thenReturn(uriInfo);
        when(containerRequestContext.getHeaderString(HttpHeaderNames.AUTHORIZATION)).thenReturn(null);
        user = new User(UserType.SMP_END_USER, "john.doe", "John", "Doe", "en", "oidcUserID");
        scope = new Scope("scopeId", Scope.ScopeType.SUBSCRIBER, "subId", null);
    }

    @Test
    void filterOauthHeaderNotNull() throws WloggerException {
        String authHeader = "authHeader";
        when(containerRequestContext.getHeaderString(HttpHeaderNames.AUTHORIZATION)).thenReturn(authHeader);
        filter.filter(containerRequestContext);
        verify(authenticationService).oauthAuthentication(authHeader, containerRequestContext);
        verify(containerRequestContext).getHeaderString(HttpHeaderNames.AUTHORIZATION);
        verifyNoMoreInteractions(containerRequestContext);
    }

    @Test
    void filterOauthHeaderNull() {
        when(containerRequestContext.getHeaderString(HttpHeaderNames.AUTHORIZATION)).thenReturn(null);
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>());
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getHeaderString(HttpHeaderNames.AUTHORIZATION);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        assertUnauthorized();
    }

    @Test
    void filterNoSessionToken() {
        when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<>());
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        assertUnauthorized();
    }

    @Test
    void filterEmptySessionToken() {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("sessionId", "");
        when(uriInfo.getQueryParameters()).thenReturn(params);
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        assertUnauthorized();
    }

    @Test
    void notAuthenticated() {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("sessionId", "foobar");
        when(uriInfo.getQueryParameters()).thenReturn(params);
        when(authenticationService.getWloggerSession()).thenReturn(Optional.empty());
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        verify(authenticationService).getWloggerSession();
        assertUnauthorized();
    }

    @Test
    void sessionTokenNotMatching() {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add("sessionId", "foobar");
        when(uriInfo.getQueryParameters()).thenReturn(params);
        when(authenticationService.getWloggerSession()).
                thenReturn(Optional.of(new WloggerSession("anothertoken", user, scope)));
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        verify(authenticationService).getWloggerSession();
        assertUnauthorized();
    }

    @Test
    void successWithSessionId() {
        successWith("sessionId");
    }

    @Test
    void successWithSessionToken() {
        successWith("sessionToken");
    }

    private void successWith(String param) {
        MultivaluedMap<String, String> params = new MultivaluedHashMap<>();
        params.add(param, new String("foobar"));
        when(uriInfo.getQueryParameters()).thenReturn(params);
        when(authenticationService.getWloggerSession()).
                thenReturn(Optional.of(new WloggerSession(new String("foobar"), user, scope)));
        filter.filter(containerRequestContext);
        verify(containerRequestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        verify(authenticationService).getWloggerSession();
        verify(containerRequestContext).getHeaderString(HttpHeaderNames.AUTHORIZATION);
        verifyNoMoreInteractions(containerRequestContext);
    }

    private void assertUnauthorized() {
        ArgumentCaptor<Response> argumentCaptor = ArgumentCaptor.forClass(Response.class);
        verify(containerRequestContext).abortWith(argumentCaptor.capture());
        Response response = argumentCaptor.getValue();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatus());
    }
}