package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.config.*;
import com.actility.thingpark.wlogger.dto.ElementData;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.response.ResponseFactory;
import com.actility.thingpark.wlogger.service.StoreService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

@Path("/boot")
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BootController {

  private static final String NAME_THINGPARK_APPID = "THINGPARK_APPID";
  private static final String NAME_VERSION = "VERSION";
  private static final String NAME_GOOGLE_BROWSER_API_KEY = "GOOGLE_BROWSER_API_KEY";
  private static final String NAME_MAP_SERVICE_MODULE_ACCESS = "MAP_SERVICE_MODULE_ACCESS";
  private static final String NAME_MAP_SERVICE_DIRECT_ACCESS = "MAP_SERVICE_DIRECT_ACCESS";
  private static final String NAME_LEAFLET_URL_TEMPLATE = "LEAFLET_URL_TEMPLATE";
  private static final String NAME_BMAPS_API_KEY = "BMAPS_API_KEY";
  private static final String NAME_GMAPS_ECJ_ENCRYPTION = "GMAPS_ECJ_ENCRYPTION";
  private static final String NAME_LTE_MODE = "LTE_MODE";
  private static final String NAME_LOCALIZATION = "LOCALIZATION";
  private static final String NAME_ADMIN_LOGIN = "ADMIN_LOGIN";
  private static final String NAME_PAGE_SIZE = "PAGE_SIZE";
  private static final String NAME_MAX_PAGE = "MAX_PAGE";
  private static final String NAME_MAX_AUTOMATIC_DECODED_PACKETS = "MAX_AUTOMATIC_DECODED_PACKETS";

  private static final String KEYCLOAK_URI = "keycloak.uri";
  private static final String KEYCLOAK_URI_GUI = "keycloak.uri.gui";
  private static final String KEYCLOAK_DISCOVERY_URI = "keycloak.discovery.uri";
  private static final String KEYCLOAK_CLI_GUI = "keycloak.clientId";
  private static final String KEYCLOAK_DEV_REALM = "keycloak.dev.realm";
  private static final String MODE_DEV = "MODE_DEV";
  private static final String KEYCLOAK_RESPONSE_MODE = "keycloak.response.mode";

  private StoreService storeService;
  private PaginationConfig paginationConfig;
  private WloggerConfig wloggerConfig;
  private SmpConfig smpConfig;
  private KeycloakConfig keycloakConfig;
  private UiConfig uiConfig;
  private UiMapConfig uiMapConfig;
  private DomainConfig domainConfig;

  @Inject
  void inject(
          final StoreService storeService,
          final PaginationConfig paginationConfig,
          final WloggerConfig wloggerConfig,
          final SmpConfig smpConfig,
          final KeycloakConfig keycloakConfig,
          final UiConfig uiConfig,
          final UiMapConfig uiMapConfig,
          final DomainConfig domainConfig) {
    this.storeService = storeService;
    this.paginationConfig = paginationConfig;
    this.wloggerConfig = wloggerConfig;
    this.smpConfig = smpConfig;
    this.keycloakConfig = keycloakConfig;
    this.uiConfig = uiConfig;
    this.uiMapConfig = uiMapConfig;
    this.domainConfig = domainConfig;
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseList boot(@QueryParam("domain") String domain) {
    String version = wloggerConfig.version();

    // TODO: understand how boot can handle this
    // Set NO LTE mode for NP Network Partner
    // String lteMode =
    //    ofNullable(user)
    //        .map(User::getSubscriptionHref)
    //        .filter(href -> href.contains("suppliers"))
    //        .map(href -> "0")
    //        .orElse(configurationService.getLteMode());

    boolean lteMode = wloggerConfig.lteMode();

    return ResponseFactory.createSuccessResponse()
        .withData(
            ImmutableList.of(
                ElementData.builder()
                    .name(NAME_THINGPARK_APPID)
                    .value(smpConfig.getApplicationID())
                    .build(),
                ElementData.builder().name(NAME_VERSION).value("V" + version).build(),
                ElementData.builder()
                    .name(NAME_GOOGLE_BROWSER_API_KEY)
                    .value(domainConfig.getGmapsApiKey(domain).orElse(null))
                    .build(),
                ElementData.builder()
                    .name(NAME_MAP_SERVICE_MODULE_ACCESS)
                    .value(uiMapConfig.moduleAccess())
                    .build(),
                ElementData.builder()
                    .name(NAME_MAP_SERVICE_DIRECT_ACCESS)
                    .value(uiMapConfig.directAccess())
                    .build(),
                ElementData.builder()
                    .name(NAME_LEAFLET_URL_TEMPLATE)
                    .value(domainConfig.getLeafletUrlTemplate(domain).orElse(null))
                    .build(),
                // new Data().name(JSONConstant.NOMINATIM_URL).value(nominatimUrl),
                ElementData.builder()
                    .name(NAME_BMAPS_API_KEY)
                    .value(domainConfig.getBmapsApiKey(domain).orElse(null))
                    .build(),
                ElementData.builder()
                    .name(NAME_GMAPS_ECJ_ENCRYPTION)
                    .value(
                        domainConfig
                            .getGmapsEcjEncryptMode(domain)
                            .map(bool -> bool ? "true" : "false")
                            .orElse(null))
                    .build(),
                ElementData.builder().name(NAME_LTE_MODE).value(lteMode ? "1" : "0").build(),
                ElementData.builder()
                    .name(NAME_LOCALIZATION)
                    .value(uiConfig.localizationMode())
                    .build(),
                ElementData.builder()
                    .name(NAME_ADMIN_LOGIN)
                    .value(wloggerConfig.adminLogin() ? "1": "0")
                    .build(),
                ElementData.builder()
                    .name(NAME_PAGE_SIZE)
                    .value(Integer.toString(paginationConfig.pageSize()))
                    .build(),
                ElementData.builder()
                    .name(NAME_MAX_PAGE)
                    .value(Integer.toString(paginationConfig.maxPages()))
                    .build(),
                ElementData.builder().name(KEYCLOAK_URI).value(keycloakConfig.uri()).build(),
                ElementData.builder().name(KEYCLOAK_URI_GUI).value(keycloakConfig.uriGui()).build(),
                ElementData.builder()
                    .name(KEYCLOAK_DISCOVERY_URI)
                    .value(keycloakConfig.discoveryUri())
                    .build(),
                ElementData.builder()
                    .name(KEYCLOAK_CLI_GUI)
                    .value(keycloakConfig.clientGui())
                    .build(),
                ElementData.builder()
                    .name(KEYCLOAK_RESPONSE_MODE)
                    .value(Strings.isNullOrEmpty(keycloakConfig.responseMode())? "query" : keycloakConfig.responseMode())
                    .build(),
                ElementData.builder()
                    .name(KEYCLOAK_DEV_REALM)
                    .value(keycloakConfig.realm().orElse(""))
                    .build(),
                ElementData.builder()
                        .name(NAME_MAX_AUTOMATIC_DECODED_PACKETS)
                        .value(Integer.toString(Objects.isNull(wloggerConfig.maxAutomaticDecodedPackets())? 500 : wloggerConfig.maxAutomaticDecodedPackets()))
                        .build(),
                ElementData.builder()
                    .name(MODE_DEV)
                    .value(ProfileManager.getActiveProfile().equals("dev") ? "1" : "0")
                    .build()));
  }
}
