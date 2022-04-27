package com.actility.thingpark.wlogger.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.actility.thingpark.wlogger.config.*;
import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementData;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BootControllerTest {

    private StoreService storeServiceMock;
    private PaginationConfig paginationConfig = new PaginationConfig() {
        @Override
        public int pageSize() {
            return 100;
        }

        @Override
        public int maxPages() {
            return 10;
        }
    };
    private WloggerConfig wloggerConfig = new WloggerConfig() {
        @Override
        public String configurationFolder() {
            return null;
        }

        @Override
        public String version() {
            return "11.4.0";
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
            return true;
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
    };
    private SmpConfig smpConfig = new SmpConfig() {
        @Override
        public String getApplicationID() {
            return null;
        }

        @Override
        public String getSupplierModuleID() {
            return null;
        }

        @Override
        public String getSubscriberModuleID() {
            return null;
        }
    };
    private KeycloakConfig keycloakConfig = new KeycloakConfig() {
        @Override
        public String uri() {
            return null;
        }

        @Override
        public String uriGui() {
            return null;
        }

        @Override
        public String discoveryUri() {
            return null;
        }

        @Override
        public String clientGui() {
            return null;
        }

        @Override
        public String clientApi() {
            return null;
        }

        @Override
        public Optional<String> realm() {
            return Optional.empty();
        }

        @Override
        public String responseMode() { return "query"; }
    };
    private UiConfig uiConfig = () -> "LOCALE";

    private UiMapConfig uiMapConfig = new UiMapConfig() {
        @Override
        public String moduleAccess() {
            return null;
        }

        @Override
        public String directAccess() {
            return null;
        }
    };
    private DomainConfig domainConfigMock;

    private static final String NAME_VERSION = "VERSION";
    private static final String NAME_LOCALIZATION = "LOCALIZATION";
    private static final String NAME_ADMIN_LOGIN = "ADMIN_LOGIN";
    private static final String NAME_PAGE_SIZE = "PAGE_SIZE";
    private static final String NAME_MAX_PAGE = "MAX_PAGE";

    private static final String MODE_DEV = "MODE_DEV";

    private final String domain = "actility";
    private final String pageSize = "100";
    private final String modeDev = "0";
    private final String adminLogin = "1";
    private final String localization = "LOCALE";

    @BeforeEach
    void setUp() {
        this.storeServiceMock = mock(StoreService.class);
        this.domainConfigMock = mock(DomainConfig.class);
    }

    @Test
    void boot() {
        BootController boot = new BootController();
        boot.inject(storeServiceMock, paginationConfig, wloggerConfig, smpConfig, keycloakConfig, uiConfig, uiMapConfig, domainConfigMock);
        ResponseList data = boot.boot(domain);
        assertNull(data.getError());
        assertEquals(21, data.getData().size());
        assertContainData(data.getData(), NAME_VERSION, "V11.4.0");
        assertContainData(data.getData(), MODE_DEV, modeDev);
        assertContainData(data.getData(), NAME_PAGE_SIZE, pageSize);
        assertContainData(data.getData(), NAME_MAX_PAGE, "10");
        assertContainData(data.getData(), NAME_ADMIN_LOGIN, adminLogin);
        assertContainData(data.getData(), NAME_LOCALIZATION, localization);
    }

    private void assertContainData(List<Element> elements, String name, String value) {
        Optional<ElementData> data =
                elements.stream()
                        .map(element -> (ElementData) element)
                        .filter(element -> element.name.equals(name))
                        .findFirst();
        assertTrue(data.isPresent());
        assertEquals(value, data.map(elem -> elem.value).get());
    }
}