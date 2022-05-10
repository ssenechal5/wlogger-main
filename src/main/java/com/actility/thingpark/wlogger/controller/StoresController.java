package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.StoreService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/stores")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class StoresController {

  private static final Logger logger = Logger.getLogger(StoresController.class.getName());
  public static final String MODULE = "module";

  private StoreService storeService;

  @Inject
  void inject(final StoreService storeService) {
    this.storeService = storeService;
  }

  @GET
  @Path("/decoder")
  @Produces(MediaType.APPLICATION_JSON)
  public String decoder(
      @Context HttpServletRequest request,
      @Context UriInfo uriInfo,
      @QueryParam("domain") String domain,
      @QueryParam("type") String type,
      @QueryParam("locale") String locale,
      @QueryParam("decType") String decType)
      throws WloggerException {
    String operatorDomain = uriInfo.getRequestUri().getHost();
    if (StringUtils.equals(type, MODULE) && !isBlank(domain)) {
      operatorDomain = domain;
    }
    logger.info(
        format(
            "Get decoder with type=%s domain=%s locale=%s decType=%s",
            type, operatorDomain, locale, decType));
    try {
      return storeService.getDecoder(operatorDomain, locale, toBool(decType));
    } catch (IOException e) {
      throw WloggerException.internalError(e);
    }
  }

  @GET
  @Path("/subtype")
  @Produces(MediaType.APPLICATION_JSON)
  public String subtype(
      @Context HttpServletRequest request,
      @Context UriInfo uriInfo,
      @QueryParam("domain") String domain,
      @QueryParam("type") String type,
      @QueryParam("locale") String locale,
      @QueryParam("subtype") Integer subtype,
      @QueryParam("np") Integer np) throws WloggerException {
    String operatorDomain = uriInfo.getRequestUri().getHost();
    if (isNotBlank(type) && type.equals(MODULE) && isNotBlank(domain)) {
      operatorDomain = domain;
    }
    logger.info(
        format(
            "Get subtype with type=%s domain=%s locale=%s subtype=%s np=%s",
            type, operatorDomain, locale, subtype, np));
    try {
      return this.storeService.getSubtype(
          operatorDomain, locale, toBool(subtype), toBool(np));
    } catch (IOException e) {
      throw WloggerException.internalError(e);
    }
  }

  private static boolean toBool(@Nullable Integer integer) {
    return ofNullable(integer).map(i -> i == 1).orElse(false);
  }

  private static boolean toBool(@Nullable String string) {
    return ofNullable(string).map(s -> s.equals("1")).orElse(false);
  }
}
