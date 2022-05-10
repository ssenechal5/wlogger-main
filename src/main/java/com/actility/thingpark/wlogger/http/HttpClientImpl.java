package com.actility.thingpark.wlogger.http;

import com.actility.thingpark.wlogger.config.HttpClientConfig;
import com.actility.thingpark.wlogger.utils.HttpStringBuilder;
import com.google.common.net.HttpHeaders;
import io.quarkus.arc.DefaultBean;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@DefaultBean
public class HttpClientImpl implements HttpClient {
    private static final Logger logger = Logger.getLogger(HttpClientImpl.class.getName());
    private static final Logger httpConnectorLog = Logger.getLogger("http-connector-log-smp");
    private final org.apache.http.client.HttpClient httpClient;
    private final HttpClientConfig httpClientConfig;
    private final static String[] TLS_VERSIONS = new String[]{"TLSv1.2"};

    @Inject
    public HttpClientImpl(final HttpClientConfig httpClientConfig) {
        this(httpClientConfig, buildConfiguredHttpClient(httpClientConfig));
    }

    public HttpClientImpl(final HttpClientConfig httpClientConfig, final org.apache.http.client.HttpClient httpClient) {
        this.httpClient = httpClient;
        this.httpClientConfig = httpClientConfig;
    }

    private static ConnectionKeepAliveStrategy buildHttpClientConnectionKeepAliveStrategy(
            int connectTimeout) {
        return (response, context) -> {
            HeaderElementIterator it =
                    new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return connectTimeout;
        };
    }

    private static HttpClientConnectionManager buildHttpClientConnectionManager(HttpClientConfig config) {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory.INSTANCE)
                                .register(
                                        "https",
                                        new SSLConnectionSocketFactory(
                                                SSLContexts.createDefault(),
                                                TLS_VERSIONS,
                                                null,
                                                SSLConnectionSocketFactory.getDefaultHostnameVerifier()))
                                .build());
        connectionManager.setDefaultMaxPerRoute(config.maxConnectionPerRoute());
        connectionManager.setMaxTotal(config.maxConnectionPerPool());

        IdleConnectionMonitorThread staleMonitorCredClient =
                new IdleConnectionMonitorThread(connectionManager);

        // Close all connection older than 5 seconds. Run as separate thread.
        staleMonitorCredClient.start();
        try {
            staleMonitorCredClient.join(1000);
        } catch (InterruptedException e) {
            logger.warning("Connection Interrupted : " + e.getMessage());
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
        return connectionManager;
    }

    private static CloseableHttpClient buildConfiguredHttpClient(HttpClientConfig config) {
        int connectTimeout = config.connectTimeout();
        HttpClientBuilder builder =
                HttpClients.custom()
                        .setConnectionManager(buildHttpClientConnectionManager(config))
                        .setConnectionManagerShared(true)
                        .setDefaultRequestConfig(
                                RequestConfig.custom()
                                        .setConnectTimeout(connectTimeout)
                                        .setConnectionRequestTimeout(config.connectionRequestTimeout())
                                        .setSocketTimeout(config.socketTimeout())
                                        .setCookieSpec(CookieSpecs.STANDARD)
                                        .build())
                        .setKeepAliveStrategy(buildHttpClientConnectionKeepAliveStrategy(connectTimeout));
        return builder.build();
    }

    private List<String> getExcludedHeaders() {
        return Arrays.asList(httpClientConfig.excludedHeaders().orElse("").split(","));
    }

    private void httpLog(HttpRequestBase request, byte[] requestContent, HttpResponse response, byte[] responseContent) {
        List<String> excludedHeaders = getExcludedHeaders();
        httpConnectorLog.fine(
                HttpStringBuilder.getHttpRequestInReadableStringFormat(request, requestContent, excludedHeaders)
                        + HttpStringBuilder.getHttpResponseInReadableStringFormat(
                        response, responseContent, excludedHeaders));
    }

    @Override
    public Pair<HttpResponse, byte[]> get(URI uri) throws IOException {
        HttpGet get = new HttpGet(uri);
        return request(get, null);
    }

    @Override
    public Pair<HttpResponse, byte[]> post(URI uri, HttpEntity body) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.setEntity(body);
        post.addHeader(
                HttpHeaders.ACCEPT,
                body.getContentType() == null
                        ? MediaType.APPLICATION_XML
                        : body.getContentType().getValue());
        post.addHeader(
                HttpHeaders.CONTENT_TYPE,
                body.getContentType() == null
                        ? MediaType.APPLICATION_XML
                        : body.getContentType().getValue());
        return request(post, body);
    }

    private static byte[] entityToByteArrayEventually(HttpEntity entity) throws IOException {
        if (entity == null) {
            return null;
        }
        return EntityUtils.toByteArray(entity);
    }

    private Pair<HttpResponse, byte[]> request(HttpRequestBase request, HttpEntity requestEntity) throws IOException {
        try {
            HttpResponse response = this.httpClient.execute(request);
            byte[] responseContent = entityToByteArrayEventually(response.getEntity());
            byte[] requestContent = entityToByteArrayEventually(requestEntity);
            httpLog(request, requestContent, response, responseContent);
            return new ImmutablePair<>(response, responseContent);
        } finally {
            request.releaseConnection();
        }
    }
}
