package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotFoundExceptionMapperTest {

  NotFoundException exception;
  NotFoundExceptionMapper notFoundExceptionMapper;

  @BeforeEach
  void setUp() {
    exception = new NotFoundException("message");
    notFoundExceptionMapper = new NotFoundExceptionMapper();

    HttpHeaders headers = mock(HttpHeaders.class);
    when(headers.getAcceptableMediaTypes()).thenReturn(Collections.emptyList());

    notFoundExceptionMapper.setHeaders(headers);
  }

  @Test
  void toResponse() {
    Response response = notFoundExceptionMapper.toResponse(exception);
    assertEquals(404,response.getStatus());
    assertEquals("404",((ResponseSimple)response.getEntity()).getStatusCode());
    assertEquals(((ResponseSimple)response.getEntity()).getErrorCode(),String.valueOf(Errors.EXCEPTION_NOT_FOUND));
    assertEquals("Not found exception: message (message)",((ResponseSimple)response.getEntity()).getError());
  }
}