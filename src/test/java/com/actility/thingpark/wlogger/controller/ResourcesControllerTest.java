package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.exception.WloggerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResourcesControllerTest {

  private WloggerConfig wloggerConfig = new WloggerConfig() {
    @Override
    public String configurationFolder() {
      return "../conf";
    }

    @Override
    public String version() {
      return null;
    }

    @Override
    public String defaultLanguage() {
      return null;
    }

    @Override
    public boolean passiveRoaming() {
      return false;
    }

    @Override
    public boolean lteMode() {
      return false;
    }

    @Override
    public String csvExportDelimiter() {
      return null;
    }

    @Override
    public String accessCodeSecret() {
      return null;
    }

    @Override
    public boolean adminLogin() {
      return false;
    }

    @Override
    public int adminSessionLifetime() {
      return 0;
    }

    @Override
    public int maxAutomaticDecodedPackets() { return 500; }

    @Override
    public int subscriberViewRoamingInTraffic() {
      return 0;
    }

    @Override
    public int maxInactiveInterval() {
      return 0;
    }
  };

  private ResourcesController resources;
  private UriInfo uriInfo;

  private static final String KEY_DECODER_FILE = "decoder.file";

  private String domain = "domain";
  private String locale = "en-US";

  @BeforeEach
  void setUp() {
    this.resources = new ResourcesController();
    this.resources.inject(wloggerConfig);

    this.uriInfo = mock(UriInfo.class);
    when(uriInfo.getRequestUri())
            .thenReturn(
                    UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/resources/images").build());
    when(uriInfo.getAbsolutePath())
            .thenReturn(
                    UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/resources/images?baz=foo").build());

  }

  @Test
  void keyencrypt() throws WloggerException {
    Response response = this.resources.keyencrypt("password", "0");
    assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    assertEquals("5BDFCE5ADADBBD24B0E8184EC8D1C058",response.getEntity().toString());
  }

  @Test
  void getTranslations() throws WloggerException {
    Response response = this.resources.getTranslations(domain,locale,null);
    assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
  }

  @Test
  void getErrorsMessages() throws WloggerException {
    Response response = this.resources.getErrorsMessages(domain,locale,null);
    assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
  }

}