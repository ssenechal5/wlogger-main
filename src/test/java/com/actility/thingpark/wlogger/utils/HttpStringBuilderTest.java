package com.actility.thingpark.wlogger.utils;

import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpStringBuilderTest {

  HttpRequestBase httpRequestBaseMock;
  HttpResponse httpResponseMock;

  @BeforeEach
  void setUp() {
    httpRequestBaseMock = mock(HttpRequestBase.class);
    when(httpRequestBaseMock.getMethod()).thenReturn("");
    when(httpRequestBaseMock.getURI()).thenReturn(URI.create(""));
    when(httpRequestBaseMock.getProtocolVersion()).thenReturn(new ProtocolVersion("http",1,1));
    HeaderIterator headerIterator = new HeaderIterator() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public Object next() {
        return null;
      }

      @Override
      public Header nextHeader() {
        return null;
      }
    };
    when(httpRequestBaseMock.headerIterator()).thenReturn(headerIterator);

    httpResponseMock = mock(HttpResponse.class);
    when(httpResponseMock.headerIterator()).thenReturn(headerIterator);
    when(httpResponseMock.getStatusLine()).thenReturn(new StatusLine() {
      @Override
      public ProtocolVersion getProtocolVersion() {
        return new ProtocolVersion("http",1,1);
      }

      @Override
      public int getStatusCode() {
        return 0;
      }

      @Override
      public String getReasonPhrase() {
        return "";
      }
    });
  }

  @Test
  void getHttpRequestInReadableStringFormat() {
    assertTrue(HttpStringBuilder.getHttpRequestInReadableStringFormat(httpRequestBaseMock,null, Collections.emptyList()).length() >= 40);
  }

  @Test
  void getHttpResponseInReadableStringFormat() {
    assertTrue(HttpStringBuilder.getHttpResponseInReadableStringFormat(httpResponseMock,null, Collections.emptyList()).length() >= 40);
  }
}