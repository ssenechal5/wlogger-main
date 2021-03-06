package com.actility.thingpark.wlogger.http;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitorThread extends Thread {

  private final HttpClientConnectionManager connMgr;
  private volatile boolean shutdown;

  public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
    super();
    this.connMgr = connMgr;
  }

  @Override
  public void run() {
    try {
      while (!shutdown) {
        synchronized (this) {
          wait(5000);
          connMgr.closeExpiredConnections();
          connMgr.closeIdleConnections(10, TimeUnit.SECONDS);
        }
      }
    } catch (InterruptedException ex) {
      // Ignore
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    }
  }

  public void shutdown() {
    shutdown = true;
    synchronized (this) {
      notifyAll();
    }
  }
}
