package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.auth.AuthenticationService;
import com.actility.thingpark.wlogger.auth.SuccessLogin;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.net.URI;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Path("/module")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ModuleController {

  public static final String SUPPLIER = "SUPPLIER";

  private WloggerConfig wloggerConfig;
  private AuthenticationService authenticationService;

  @Inject
  void inject(
          final WloggerConfig wloggerConfig,
          final AuthenticationService authenticationService) {
    this.wloggerConfig = wloggerConfig;
    this.authenticationService = authenticationService;
  }

  static URI buildGuiUri(
          URI uri, String uid, String sessionToken, String origin, String languageTag) {
    return UriBuilder.fromUri(uri)
            .replacePath("/thingpark/wlogger/gui")
            .queryParam("uid", uid)
            .queryParam("type", "module")
            .queryParam("sessionToken", sessionToken)
            .queryParam("operator", URI.create(origin).getHost())
            .queryParam("lang", languageTag)
            .queryParam("language-tag", languageTag)
            .build();
  }

  @GET
  @Path("")
//  @Produces(MediaType.APPLICATION_JSON)
  public Response module(
      @Context HttpServletRequest request,
      @Context UriInfo uriInfo,
      @QueryParam("adminAccessCode") String adminAccessCode,
      @QueryParam("type") String type,
      @QueryParam("language-tag") String languageTag,
      @QueryParam("origin") String origin)
      throws WloggerException {
    if (adminAccessCode == null) {
      throw WloggerException.applicationError(Errors.AUTH_MISSING_ACCESS_CODE);
    }
    SuccessLogin login = adminAccessCodeLogin(type, adminAccessCode);
    String language =
            ofNullable(login.getUser().getLanguage()).orElse(wloggerConfig.defaultLanguage());
    return Response.seeOther(
            buildGuiUri(
                    uriInfo.getAbsolutePath(),
                    login.getScope().getId(),
                    login.getSessionToken(),
                    origin,
                    language))
            .build();
  }

  private SuccessLogin adminAccessCodeLogin(String type, String code) throws WloggerException {
    if (type != null && type.equals(SUPPLIER)) {
      return authenticationService.supplierAdminAccessCodeLogin(code);
    }
    return authenticationService.subscriberAdminAccessCodeLogin(code);
  }

  @GET
  @Path("/logout")
  // @Produces(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_HTML)
  public Response logout(
          @Context HttpServletRequest request,
          @QueryParam("sessionToken") String sessionToken,
          @QueryParam("uid") String uid) {
    authenticationService.endSession();
    String path = request.getContextPath().replace("/rest","");
    return Response.ok(
            format(
                    "<html><head><meta charset=\"UTF-8\"><title>ThingPark® Log out</title>"
                            + "<script type=\"text/javascript\" src=\"%s/gui/library/extjs/ext-all.min.js\"></script>"
                            + "<script type=\"text/javascript\" src=\"%s/gui/resources/js/logout.min.js\"></script>"
                            + "</head><body></body></html>",
                    path, path))
            .build();
  }
}
