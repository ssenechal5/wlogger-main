package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.config.WloggerConfig;
import io.quarkus.arc.DefaultBean;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Singleton
@DefaultBean
public class StoreServiceImpl implements StoreService {

  private static JsonObject textValueJson(String text, String value) {
    return new JsonObject().put("text", text).put("value", value);
  }

  private static final String DEFAULT = "~DEFAULT";

  private static final String DECODERS =
      new JsonArray()
          .add(textValueJson("raw", "raw"))
          .add(textValueJson("ASCII", "ASCII"))
          .toString();
  private static final String SUBTYPE =
      new JsonArray()
          .add(textValueJson("Uplink (Data only)", "0"))
          .add(textValueJson("Uplink (MAC + Data)", "1"))
          .add(textValueJson("Uplink (MAC only)", "2"))
          .add(textValueJson("Uplink (Join)", "4"))
          .add(textValueJson("Downlink Unicast (Data only)", "1000"))
          .add(textValueJson("Downlink Unicast (MAC + Data)", "1001"))
          .add(textValueJson("Downlink Unicast (MAC only)", "1002"))
          .add(textValueJson("Downlink Unicast (Join)", "1004"))
          .add(textValueJson("Downlink Multicast", "1005"))
          .add(textValueJson("Multicast Summary", "3000"))
          .add(textValueJson("Location", "2000"))
          .add(textValueJson("Device Reset", "5000"))
          .toString();
  private static final String SUBTYPELTE =
      new JsonArray()
          .add(textValueJson("Uplink (Data)", "100"))
          .add(textValueJson("Uplink (Session)", "101"))
          .add(textValueJson("Downlink", "1100"))
          .add(textValueJson("Microflow", "4000"))
          .toString();
  private static final String SUBTYPENP =
      new JsonArray()
          .add(textValueJson("Uplink (Data only)", "0"))
          .add(textValueJson("Uplink (MAC + Data)", "1"))
          .add(textValueJson("Uplink (MAC only)", "2"))
          .add(textValueJson("Uplink (Join)", "4"))
          .add(textValueJson("Downlink Unicast (Data only)", "1000"))
          .add(textValueJson("Downlink Unicast (MAC + Data)", "1001"))
          .add(textValueJson("Downlink Unicast (MAC only)", "1002"))
          .add(textValueJson("Downlink Unicast (Join)", "1004"))
          .add(textValueJson("Downlink Multicast", "1005"))
          .toString();

  private WloggerConfig wloggerConfig;

  @Inject
  public StoreServiceImpl(final WloggerConfig wloggerConfig) {
    this.wloggerConfig = wloggerConfig;
  }

  private Optional<String> tryReadFile(File file) throws IOException {
    if (!file.exists()) {
      return Optional.empty();
    }
    return Optional.of(FileUtils.readFileToString(file, "UTF-8"));
  }

  @Override
  public String getDecoder(String operatorDomain, String locale, boolean lte)
      throws IOException {
    Optional<String> decoder = tryReadFile(getDecoderPath(operatorDomain, locale, lte));
    if (!decoder.isPresent()) {
      decoder = tryReadFile(getDecoderPath(null, null, lte));
    }
    return decoder.orElse(DECODERS);
  }

  @Override
  public String getSubtype(String operatorDomain, String locale, boolean lte, boolean np)
      throws IOException {
    Optional<String> subtype = tryReadFile(getSubtypePath(operatorDomain, locale, lte, np));
    if (!subtype.isPresent()) {
      subtype = tryReadFile(getSubtypePath(null, null, lte, np));
    }
    if (subtype.isPresent()) {
      return subtype.get();
    }
    if (lte) {
      return SUBTYPELTE;
    }
    if (np) {
      return SUBTYPENP;
    }
    return SUBTYPE;
  }

  private File getDecoderPath(String operatorDomain, String locale, boolean lte) {
    String file = "decoder-lora.json";
    if (lte) {
      file = "decoder-lte.json";
    }
    return getConfFile(operatorDomain, locale, "decoder", file);
  }

  private File getSubtypePath(String operatorDomain, String locale, boolean lte, boolean np) {
    String file = "subtype.json";
    if (lte) {
      file = "subtype-lte.json";
    }
    if (np) {
      file = "subtype-np.json";
    }
    return getConfFile(operatorDomain, locale, "subtype", file);
  }

  private File getConfFile(String operatorDomain,
      String locale,
      String folder,
      String file) {
    if (isBlank(operatorDomain)) {
      operatorDomain = DEFAULT;
    }
    if (isBlank(locale)) {
      locale = wloggerConfig.defaultLanguage();
    }
    Path path = Paths.get(wloggerConfig.configurationFolder(), operatorDomain, folder, locale, file);
    return path.toFile();
  }
}
