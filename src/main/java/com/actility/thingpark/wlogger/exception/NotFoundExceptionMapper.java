package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.utils.ResponseUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

  @Context
  private HttpHeaders headers;

  // Just for test
  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Response toResponse(NotFoundException e) {

    Throwable t = ExceptionUtils.getRootCause(e);

    String message = e.getMessage();
    if (t != null)
      message = message + " (" + t.getMessage() + ")";

    int status = e.getResponse().getStatus();

    ResponseSimple error = new ResponseSimple().withSuccess(Boolean.FALSE)
            .withError(Errors.getErrorMessage(Errors.EXCEPTION_NOT_FOUND, message))
            .withStatusCode(String.valueOf(status)).withErrorCode(String.valueOf(Errors.EXCEPTION_NOT_FOUND));

    Response.ResponseBuilder builder = Response.status(status).entity(error)
            .type(ResponseUtils.getAcceptMediaType(headers.getAcceptableMediaTypes()));

    return builder.build();
  }
}
