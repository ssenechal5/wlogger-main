package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.utils.ResponseUtils;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

  @Context
  private HttpHeaders headers;

  @Override
  public Response toResponse(BadRequestException e) {

    Response response;

    if (e.getCause() instanceof IllegalArgumentException) {
      WloggerException exception = WloggerException.invalidQueryParameter(e.getCause());
      response = ResteasyProviderFactory.getInstance().getExceptionMapper(WloggerException.class).toResponse(exception);
    } else {

      int status = e.getResponse().getStatus();

      ResponseSimple error = new ResponseSimple().withSuccess(Boolean.FALSE)
              .withError(Errors.getErrorMessage(Errors.EXCEPTION_BAD_REQUEST, e.getMessage()))
              .withStatusCode(String.valueOf(status)).withErrorCode(String.valueOf(Errors.EXCEPTION_BAD_REQUEST));

      response = Response.status(status).type(ResponseUtils.getAcceptMediaType(headers.getAcceptableMediaTypes()))
              .entity(error).build();
    }

    return response;
  }
}
