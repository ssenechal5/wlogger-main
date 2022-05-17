package com.actility.thingpark.wlogger.http;

import com.actility.thingpark.wlogger.config.HttpClientConfig;
import com.github.paweladamski.httpclientmock.HttpClientMock;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class HttpClientImplTest {

    @Test
    void get() throws IOException {
        HttpClientMock httpClientMock = new HttpClientMock();
        httpClientMock.onGet("http://www.example.com")
                .withParameter("user", "john")
                .doReturn("Ok");

        HttpClientConfig httpClientConfig = mock(HttpClientConfig.class);

        HttpClient client = new HttpClientImpl(httpClientConfig, httpClientMock);
        Pair<HttpResponse, byte[]> response = client.get(URI.create("http://www.example.com?user=john"));
        assertArrayEquals("Ok".getBytes(), response.getRight());
    }

    @Test
    void getFailure() throws IOException {
        HttpClientMock httpClientMock = new HttpClientMock();
        httpClientMock.onGet("http://www.example.com")
                .doReturnStatus(500);

        HttpClientConfig httpClientConfig = mock(HttpClientConfig.class);

        HttpClient client = new HttpClientImpl(httpClientConfig, httpClientMock);
        Pair<HttpResponse, byte[]> response = client.get(URI.create("http://www.example.com"));
        assertEquals(500, response.getLeft().getStatusLine().getStatusCode());
    }

    @Test
    void post() throws IOException {
        HttpClientMock httpClientMock = new HttpClientMock();
        httpClientMock.onPost("http://www.example.com")
                .withParameter("user", "john")
                .withBody(equalTo("Foo"))
                .doReturn("Ok");

        HttpClientConfig httpClientConfig = mock(HttpClientConfig.class);

        HttpClient client = new HttpClientImpl(httpClientConfig, httpClientMock);
        Pair<HttpResponse, byte[]> response = client.post(URI.create("http://www.example.com?user=john"),
                new ByteArrayEntity("Foo".getBytes(), ContentType.TEXT_PLAIN));

        assertArrayEquals("Ok".getBytes(), response.getRight());
        assertEquals(200, response.getLeft().getStatusLine().getStatusCode());
    }

}