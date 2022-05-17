package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.dto.ResponseSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WloggerExceptionMapperTest {

  WloggerException exception;
  WloggerExceptionMapper wloggerExceptionMapper;

  @BeforeEach
  void setUp() {
    exception = WloggerException.notLoggedIn401(1, "message");
    wloggerExceptionMapper = new WloggerExceptionMapper();

    HttpHeaders headers = mock(HttpHeaders.class);
    when(headers.getAcceptableMediaTypes()).thenReturn(Collections.emptyList());

    wloggerExceptionMapper.setHeaders(headers);
  }

  @Test
  void toResponse() {
    Response response = wloggerExceptionMapper.toResponse(exception);
    assertEquals(401,response.getStatus());
    assertEquals("401",((ResponseSimple)response.getEntity()).getStatusCode());
    assertEquals("1",((ResponseSimple)response.getEntity()).getErrorCode());
    assertEquals("message",((ResponseSimple)response.getEntity()).getError());
  }
}