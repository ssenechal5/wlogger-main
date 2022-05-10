package com.actility.thingpark.wlogger.http;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;

public interface HttpClient {

  Pair<HttpResponse, byte[]> get(URI uri) throws IOException;

  Pair<HttpResponse, byte[]> post(URI uri, HttpEntity body) throws IOException;
}
