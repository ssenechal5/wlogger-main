package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.utils.ResponseUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;
import org.jboss.resteasy.spi.Failure;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger logger = Logger.getLogger(DefaultExceptionMapper.class.getName());

  @Context
  private HttpHeaders headers;

  @Override
  public Response toResponse(Exception e) {

    if (e instanceof DefaultOptionsMethodException) {
      return ((DefaultOptionsMethodException) e).getResponse();
    }

    ResponseBuilder builder = Response.serverError();

    String message = e.getMessage();
    if (e.getCause() != null)
      message = message + " / " + ExceptionUtils.getRootCauseMessage(e);
    if (message == null || message.length() == 0)
      message = e.toString();

    logger.log(Level.SEVERE, "An unexpected exception occurred: " + message, e);

    ResponseSimple error = new ResponseSimple()
            .withSuccess(Boolean.FALSE)
            .withError(Errors.getErrorMessage(Errors.EXCEPTION, e.getMessage()))
            .withErrorCode(String.valueOf(Errors.EXCEPTION));

    if (e instanceof Failure) {
        int status = ((Failure) e).getResponse().getStatus();
        error.withStatusCode(String.valueOf(status));
    }

    return builder.entity(error).type(ResponseUtils.getAcceptMediaType(headers.getAcceptableMediaTypes()))
            .build();
  }
}
