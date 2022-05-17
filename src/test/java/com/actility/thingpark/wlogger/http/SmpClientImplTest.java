package com.actility.thingpark.wlogger.http;

import com.actility.thingpark.smp.rest.dto.AuthenticateDto;
import com.actility.thingpark.wlogger.config.EncryptedString;
import com.actility.thingpark.wlogger.config.SmpClientConfig;
import com.actility.thingpark.wlogger.exception.SmpException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.xmlunit.matchers.CompareMatcher;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SmpClientImplTest {

    private HttpClient httpClientMock;
    private SmpClientConfig configuration;

    @BeforeEach
    void setUp() {
        httpClientMock = mock(HttpClient.class);
        configuration = new SmpClientConfig() {
            @Override
            public String uri() {
                return "https://fakesmp";
            }

            @Override
            public String login() {
                return "fakeSystemLogin";
            }

            @Override
            public EncryptedString password() {
                return new EncryptedString("p@ssw0rd");
            }
        };
    }

    @Test
    void authenticateBySubscription() throws SmpException, IOException {
        StatusLine okStatusLine = mock(StatusLine.class);
        when(okStatusLine.getStatusCode()).thenReturn(200);
        HttpResponse okResponse = mock(HttpResponse.class);
        when(okResponse.getStatusLine()).thenReturn(okStatusLine);

        when(httpClientMock.post(eq(URI.create("https://fakesmp/thingpark/smp/rest/admins/login")),
                any(HttpEntity.class))).
                thenReturn(new ImmutablePair<>(
                        okResponse,
                        ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<ns:login xmlns:ns=\"http://www.actility.com/smp/ws/admin\">\n" +
                                "  <sessionToken>testToken</sessionToken>\n" +
                                "  <thingParkID>test-thingpark-id</thingParkID>\n" +
                                "</ns:login>").getBytes()));

        when(httpClientMock.get(eq(
                URI.create("https://fakesmp/thingpark/smp/rest/applications/id?applicationID=fakeApplicationID&sessionToken=testToken")))).
                thenReturn(new ImmutablePair<>(
                        okResponse,
                        ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<ns:id xmlns:ns=\"http://www.actility.com/smp/ws/application\">\n" +
                                "  <href>/thingpark/smp/rest/applications/1</href>\n" +
                                "</ns:id>").getBytes()));

        when(httpClientMock.get(eq(
                URI.create("https://fakesmp/thingpark/smp/rest/applications/1/subscriptions/authenticate?accessCode=fakeAccessCode&sessionToken=testToken")))).
                thenReturn(new ImmutablePair<>(
                        okResponse,
                        ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<ns:authenticate xmlns:ns=\"http://www.actility.com/smp/ws/application\">\n" +
                                "  <grantedPermissions>\n" +
                                "    <permission>a</permission>\n" +
                                "    <permission>x</permission>\n" +
                                "  </grantedPermissions>\n" +
                                "  <href>/thingpark/smp/rest/applications/app1/subscriptions/subs1</href>\n" +
                                "  <subscriber>\n" +
                                "    <ID>100000002</ID>\n" +
                                "    <extID>fakeExt</extID>\n" +
                                "  </subscriber>\n" +
                                "  <user>\n" +
                                "    <ID>tpk-100000002</ID>\n" +
                                "    <href>/thingpark/smp/rest/applications/app1/subscriptions/users/usr1</href>\n" +
                                "  </user>\n" +
                                "</ns:authenticate>").getBytes()));

        SmpClientImpl client = new SmpClientImpl(httpClientMock, configuration);

        AuthenticateDto auth = client.authenticateBySubscription("fakeAccessCode",
                "fakeApplicationID");
        assertNotNull(auth);
        assertNotNull(auth.getGrantedPermissions());
        assertEquals(Arrays.asList("a", "x"), auth.getGrantedPermissions().getPermission());
        assertEquals("/thingpark/smp/rest/applications/app1/subscriptions/subs1", auth.getHref());
        assertNotNull(auth.getSubscriber());
        assertEquals("100000002", auth.getSubscriber().getID());
        assertEquals("fakeExt", auth.getSubscriber().getExtID());
        assertNotNull(auth.getUser());
        assertEquals("tpk-100000002", auth.getUser().getID());
        assertEquals("/thingpark/smp/rest/applications/app1/subscriptions/users/usr1", auth.getUser().getHref());

        ArgumentCaptor<HttpEntity> loginBody = ArgumentCaptor.forClass(HttpEntity.class);
        verify(httpClientMock).post(eq(URI.create("https://fakesmp/thingpark/smp/rest/admins/login")), loginBody.capture());
        HttpEntity loginEntity = loginBody.getValue();
        assertNotNull(loginEntity);

        String responseXml = EntityUtils.toString(loginEntity);
        assertThat(responseXml, CompareMatcher.isSimilarTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns:login xmlns:ns=\"http://www.actility.com/smp/ws/admin\">\n" +
                "  <login>fakeSystemLogin</login>\n" +
                "  <password>p@ssw0rd</password>\n" +
                "</ns:login>").ignoreWhitespace());
    }

}