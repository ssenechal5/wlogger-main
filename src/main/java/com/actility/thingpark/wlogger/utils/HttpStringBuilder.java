package com.actility.thingpark.wlogger.utils;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.List;

public final class HttpStringBuilder {
  private static final String LS = System.getProperty("line.separator");

  public static String getHttpRequestInReadableStringFormat(
      HttpRequestBase request, byte[] requestContent, List<String> excludedHeaders) {
    StringBuilder requestMessage =
        new StringBuilder(LS)
            .append("<<<<<<<<<< [HTTP Request] ")
            .append(LS)
            .append(request.getMethod())
            .append(" ")
            .append(request.getURI())
            .append(" ")
            .append(request.getProtocolVersion())
            .append(LS)
            .append(getHeaders(request, excludedHeaders))
            .append(LS);
    if (requestContent != null) {
      requestMessage.append("Content: ").append(LS).append(new String(requestContent)).append(LS);
    }
    return requestMessage.toString();
  }

  public static String getHttpResponseInReadableStringFormat(
      HttpResponse response, byte[] responseContent, List<String> excludedHeaders) {
    StringBuilder responseMessage =
        new StringBuilder(LS)
            .append(">>>>>>>>>> [HTTP Response] ")
            .append(LS)
            .append(getHeaders(response, excludedHeaders))
            .append("Response status: ")
            .append(response.getStatusLine().getStatusCode())
            .append(LS)
            .append(LS);
    if (responseContent != null) {
      responseMessage
          .append("Response body: ")
          .append(LS)
          .append(new String(responseContent))
          .append(LS);
    }
    return responseMessage.toString();
  }

  private static String getHeaders(HttpMessage httpMessage, List<String> excludedHeaders) {
    HeaderIterator iterator = httpMessage.headerIterator();
    StringBuilder headers = new StringBuilder();

    while (iterator.hasNext()) {
      Header header = (Header) iterator.next();
      if (!excludedHeaders.contains(header.getName())) {
        headers.append(header.getName()).append(": ").append(header.getValue()).append(LS);
      }
    }
    return headers.toString();
  }
}
