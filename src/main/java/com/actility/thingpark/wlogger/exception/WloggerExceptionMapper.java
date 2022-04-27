package com.actility.thingpark.wlogger.exception;

import com.actility.thingpark.wlogger.dto.ResponseSimple;
import com.actility.thingpark.wlogger.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class WloggerExceptionMapper implements ExceptionMapper<WloggerException> {

  private static final Logger logger = Logger.getLogger(WloggerExceptionMapper.class.getName());

  @Context
  private HttpHeaders headers;

  // Just for test
  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Response toResponse(WloggerException e) {

    String message = e.getMessage();
    if (e.getCause() != null)
      message = StringUtils.join(message, " / ", ExceptionUtils.getRootCauseMessage(e));

    logger.log(Level.SEVERE, "WLogger exception occurred: " + message, e);

    ResponseSimple error = new ResponseSimple().withSuccess(Boolean.FALSE).withError(message)
            .withStatusCode(String.valueOf(e.getStatusCode())).withErrorCode(String.valueOf(e.getErrorCode()));

    ResponseBuilder builder = Response.status(e.getStatusCode()).entity(error);
    builder.type(ResponseUtils.getAcceptMediaType(this.headers.getAcceptableMediaTypes()));

    return builder.build();
  }
}
