package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.utils.PrennetUtils;
import org.apache.commons.io.FileUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/resources")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ResourcesController {

  private static final Logger logger = Logger.getLogger(ResourcesController.class.getName());

  private WloggerConfig wloggerConfig;

  @Inject
  void inject(final WloggerConfig wloggerConfig) {
    this.wloggerConfig = wloggerConfig;
  }

  @GET
  @Path("/keyencrypt")
  @Produces(MediaType.APPLICATION_JSON)
  public Response keyencrypt(
      @QueryParam("password") String password, @QueryParam("key_version") String keyVersion)
      throws WloggerException {
    try {
      return Response.ok().entity(PrennetUtils.prennanGantMestr(password, keyVersion)).build();
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | IllegalBlockSizeException
        | BadPaddingException e) {
      logger.log(Level.INFO, e.getMessage(), e);
      throw WloggerException.applicationError(
          Errors.KEY_ENCRYPTION_FAILED, keyVersion, e.getMessage());
    }
  }

  @GET
  @Path("/files/translations")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTranslations(
      @QueryParam("domain") String domain,
      @QueryParam("language-tag") String languageTag,
      @QueryParam("application") String application)
      throws WloggerException {
    try {
      return getContentResponse(languageTag,"translations");
    } catch (IOException e) {
      throw WloggerException.internalError(e);
    }
  }

  @GET
  @Path("/files/errors")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getErrorsMessages(
      @QueryParam("domain") String domain,
      @QueryParam("language-tag") String languageTag,
      @QueryParam("application") String application)
      throws WloggerException {
    try {
      return getContentResponse(languageTag, "error-messages");
    } catch (IOException e) {
      throw WloggerException.internalError(e);
    }
  }

  private Response getContentResponse(String languageTag, String filename) throws IOException{
    return Response.ok().entity(getContent(languageTag, filename)).build();
  }

  private InputStream getContent(
      String languageTag, String filename) throws IOException {
    String replacement = filename;
    if (isNotBlank(languageTag)) {
      replacement = filename + "-" + languageTag;
    }
    this.logger.warning(wloggerConfig.configurationFolder() + "/" + replacement + ".json");
    File file = new File(wloggerConfig.configurationFolder() + "/" + replacement + ".json");
    this.logger.warning(file.getAbsolutePath());
    String content = "";
    if (file.exists()) {
      return FileUtils.openInputStream(file);
    } else {
      this.logger.warning(wloggerConfig.configurationFolder() + "/" + filename + ".json");
      File fallbackFile = new File(wloggerConfig.configurationFolder() + "/" + filename + ".json");
      if (fallbackFile.exists()) {
        return FileUtils.openInputStream(fallbackFile);
      }
    }
    return toInputStream(content, "UTF-8");
  }
}
