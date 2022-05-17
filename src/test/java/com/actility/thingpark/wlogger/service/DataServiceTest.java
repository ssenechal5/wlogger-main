package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.MongoDocumentFactory;
import com.actility.thingpark.wlogger.config.PaginationConfig;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.dao.DeviceHistoryDao;
import com.actility.thingpark.wlogger.dto.*;
import com.actility.thingpark.wlogger.engine.EngineClient;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wlogger.response.ResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DataServiceTest {

  private static final Logger logger = Logger.getLogger(DataServiceTest.class.getName());

  private EngineClient engineMock;
  private DecoderService decoderService;
  private CSVService csvServiceMock;
  private DeviceHistoryDao deviceHistoryDaoMock;

  private DataService dataService;

  private MongoDocumentFactory mongoDocumentFactory;

  private ArrayList<DeviceHistory> deviceHistoryArrayList;

  private PaginationConfig paginationConfig = new PaginationConfig() {
    @Override
    public int pageSize() {
      return 100;
    }

    @Override
    public int maxPages() {
      return 1000;
    }
  };

  private WloggerConfig wloggerConfig = new WloggerConfig() {
    @Override
    public String configurationFolder() {
      return null;
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
      return true;
    }

    @Override
    public boolean lteMode() {
      return false;
    }

    @Override
    public String csvExportDelimiter() {
      return ";";
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

  @BeforeEach
  void setUp() {
    this.engineMock = mock(EngineClient.class);
    this.decoderService = new DecoderService(engineMock);
    this.csvServiceMock = mock(CSVService.class);
    this.deviceHistoryDaoMock = mock(DeviceHistoryDao.class);

    this.dataService = new DataService(deviceHistoryDaoMock, csvServiceMock, decoderService, paginationConfig, wloggerConfig);

    this.mongoDocumentFactory = new MongoDocumentFactory();

    this.deviceHistoryArrayList = new ArrayList<>();

  }

  private static Stream<Arguments> provideDataArguments() {
    return Stream.of(
            Arguments.of(DeviceType.LORA, 0, 0, 1),
            Arguments.of(DeviceType.LORA, 1, 0, 1),
            Arguments.of(DeviceType.LORA, 100, 0, 1),
            Arguments.of(DeviceType.LORA, 101, 0, 1),
            Arguments.of(DeviceType.LORA, 125, 0, 1),
            Arguments.of(DeviceType.LTE, 0, 0, 1),
            Arguments.of(DeviceType.LTE, 1, 0, 1),
            Arguments.of(DeviceType.LTE, 100, 0, 1),
            Arguments.of(DeviceType.LTE, 101, 0, 1),
            Arguments.of(DeviceType.LTE, 125, 0, 1));
  }

  @ParameterizedTest(name = "run data => (type :{0}, size:{1}, offset:{2}, pageIndex:{3})")
  @MethodSource("provideDataArguments")
  void data(DeviceType deviceType, int size, int offset, int pageIndex) throws WloggerException {
    Search search = Search.builder().withType(deviceType).withPageIndex(pageIndex).build();

    this.deviceHistoryArrayList.addAll(
        mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(search, offset, 101)).thenReturn(this.deviceHistoryArrayList);

    ResponsePaginatedList successResponseData =
        ResponseFactory.createSuccessResponseDatas().withMore(size > 100);

    ResponsePaginatedList data = (ResponsePaginatedList) dataService.data(search, "", "");

    assertEquals(data.getError(), successResponseData.getError());
    assertEquals(data.isMore(), successResponseData.isMore());
    assertEquals(data.getData().size(), (size > 100 ? 100 : size));

    List<Element> decodedHistories = data.getData();
    if (decodedHistories.size() > 0) {
      String devEUI = this.deviceHistoryArrayList.get(0).deviceEUI;
      if (deviceType.equals(DeviceType.LTE)) {
        decodedHistories.forEach(
            decodedHistory -> {
              ElementDeviceHistoryLte decodedHistoryLte = (ElementDeviceHistoryLte) decodedHistory;
              assertEquals(decodedHistoryLte.getDevEUI(), devEUI);
              assertEquals("123.0",decodedHistoryLte.getDevLAT());
              assertEquals("125.0",decodedHistoryLte.getDevLON() );
            });
      } else {
        decodedHistories.forEach(
            decodedHistory -> {
              ElementDeviceHistory decodedHistoryLora = (ElementDeviceHistory) decodedHistory;
              assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
              assertEquals("123.0",decodedHistoryLora.getDevLAT());
              assertEquals("125.0",decodedHistoryLora.getDevLON());
              assertEquals("us915",decodedHistoryLora.ismb);
              assertEquals("foreignOperatorNSID",decodedHistoryLora.foreignOperatorNSID);
              assertEquals("rfRegion",decodedHistoryLora.rfRegion);
              assertEquals(true,decodedHistoryLora.lrrList.startsWith("<table class='chainList'><thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}</td><td>ISM Band</td><td>RF Region</td><td>GWID</td><td>GWToken</td>"));
              assertEquals(true,decodedHistoryLora.lrrList.contains("<td>US 902-928MHz</td>"));
              assertEquals(true,decodedHistoryLora.lrrList.contains("<td>rfRegion</td>"));
              assertEquals(true,decodedHistoryLora.lrrList.contains("<td>foreignOperatorNSID</td>"));
            });
      }
    }
  }

  @ParameterizedTest(name = "run extract => (type :{0}, size:{1}, offset:{2}, pageIndex:{3})")
  @MethodSource("provideDataArguments")
  void extract(DeviceType deviceType, int size, int offset, int pageIndex) throws WloggerException {
    Search search = Search.builder().withType(deviceType).withPageIndex(pageIndex).build();

    this.deviceHistoryArrayList.addAll(
            mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(search, offset, 101)).thenReturn(this.deviceHistoryArrayList);

    ResponsePaginatedList successResponseData =
            ResponseFactory.createSuccessResponseDatas().withMore(size > 100);

    ResponsePaginatedList data = (ResponsePaginatedList) dataService.extract(search, "", "");

    assertEquals(data.getError(), successResponseData.getError());
    assertEquals(data.isMore(), successResponseData.isMore());
    assertEquals(data.getData().size(), (size > 100 ? 100 : size));

    List<Element> decodedHistories = data.getData();
    if (decodedHistories.size() > 0) {
      if (deviceType.equals(DeviceType.LTE)) {
        String devAddr = this.deviceHistoryArrayList.get(0).deviceAddress;
        decodedHistories.forEach(
            decodedHistory -> {
              ElementDeviceHistoryLteExtract decodedHistoryLte =
                  (ElementDeviceHistoryLteExtract) decodedHistory;
              assertEquals(decodedHistoryLte.ipv4, devAddr);
              if (Direction.UPLINK.getValue() == Integer.parseInt(decodedHistoryLte.getDirection())) {
                assertEquals("123.0",decodedHistoryLte.getDevLAT());
                assertEquals("125.0",decodedHistoryLte.getDevLON());
              } else {
                  assertEquals(null,decodedHistoryLte.getDevLAT());
                  assertEquals(null,decodedHistoryLte.getDevLON());
              }
            });
      } else {
        String devEUI = this.deviceHistoryArrayList.get(0).deviceEUI;
        decodedHistories.forEach(
            decodedHistory -> {
              ElementDeviceHistoryExtract decodedHistoryLora =
                  (ElementDeviceHistoryExtract) decodedHistory;
              assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
              if (Direction.UPLINK.getValue()
                  == Integer.parseInt(decodedHistoryLora.getDirection())) {
                assertEquals("123.0", decodedHistoryLora.getDevLAT());
                assertEquals("125.0", decodedHistoryLora.getDevLON());

                assertEquals(1, decodedHistoryLora.lrrs.size());
                ElementLrrs element = (ElementLrrs) decodedHistoryLora.lrrs.get(0);
                assertEquals(true, element.isGwDL());
                assertEquals("us915", element.getIsmBand());
                assertEquals("rfRegion", element.getRfRegion());
                assertEquals("foreignOperatorNSID", element.getForeignOperatorNSID());

              } else {
                assertEquals(null, decodedHistoryLora.getDevLAT());
                assertEquals(null, decodedHistoryLora.getDevLON());
              }

              assertEquals("us915", decodedHistoryLora.ismb);
              assertEquals("foreignOperatorNSID", decodedHistoryLora.foreignOperatorNSID);
              assertEquals("rfRegion", decodedHistoryLora.rfRegion);

            });
      }
    }
  }

  @ParameterizedTest(name = "run export => (type :{0}, size:{1}, offset:{2}, pageIndex:{3})")
  @MethodSource("provideDataArguments")
  void export(DeviceType deviceType, int size, int offset, int pageIndex) throws WloggerException {
    Search search = Search.builder().withType(deviceType).withPageIndex(pageIndex).build();

    this.deviceHistoryArrayList.addAll(
            mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(search, offset, 101)).thenReturn(this.deviceHistoryArrayList);

    Response response = dataService.export(search, "", "");

    assertEquals("attachment; filename=export.csv",response.getHeaderString("Content-Disposition"));
  }

  @ParameterizedTest(name = "run devicesLocations => (type :{0}, size:{1}, offset:{2}, pageIndex:{3})")
  @MethodSource("provideDataArguments")
  void devicesLocations(DeviceType deviceType, int size, int offset, int pageIndex) throws WloggerException {
    Search search = Search.builder().withType(deviceType).withPageIndex(pageIndex).build();

    this.deviceHistoryArrayList.addAll(
            mongoDocumentFactory.newDeviceHistoriesList(deviceType, size));

    when(deviceHistoryDaoMock.get(search, offset, 101)).thenReturn(this.deviceHistoryArrayList);

    ResponsePaginatedList successResponseData =
            ResponseFactory.createSuccessResponseDatas().withMore(size > 100);

    ResponsePaginatedList data = (ResponsePaginatedList) dataService.devicesLocations(search, "", "");

    assertEquals(data.getError(), successResponseData.getError());
    assertEquals(data.isMore(), successResponseData.isMore());
    if (deviceType.equals(DeviceType.LTE)) {
      assertEquals(data.getData().size(), (size > 100 ? 100 : size));
    } else {
      assertEquals(data.getData().size(), (size >= 100 ? 50 : size));
    }

    List<Element> decodedHistories = data.getData();
    if (decodedHistories.size() > 0) {
      String devEUI = this.deviceHistoryArrayList.get(0).deviceEUI;
      if (deviceType.equals(DeviceType.LTE)) {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistoryLte decodedHistoryLte = (ElementDeviceHistoryLte) decodedHistory;
                  assertEquals(decodedHistoryLte.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLte.getDevLAT());
                  assertEquals("125.0",decodedHistoryLte.getDevLON());
                });
      } else {
        decodedHistories.forEach(
                decodedHistory -> {
                  ElementDeviceHistory decodedHistoryLora = (ElementDeviceHistory) decodedHistory;
                  assertEquals(decodedHistoryLora.getDevEUI(), devEUI);
                  assertEquals("123.0",decodedHistoryLora.getDevLAT());
                  assertEquals("125.0",decodedHistoryLora.getDevLON());
                  assertEquals("us915",decodedHistoryLora.ismb);
                  assertEquals("foreignOperatorNSID",decodedHistoryLora.foreignOperatorNSID);
                  assertEquals("rfRegion",decodedHistoryLora.rfRegion);
                  assertEquals(true,decodedHistoryLora.lrrList.startsWith("<table class='chainList'><thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}</td><td>ISM Band</td><td>RF Region</td><td>GWID</td><td>GWToken</td>"));
                  assertEquals(true,decodedHistoryLora.lrrList.contains("<td>US 902-928MHz</td>"));
                  assertEquals(true,decodedHistoryLora.lrrList.contains("<td>rfRegion</td>"));
                  assertEquals(true,decodedHistoryLora.lrrList.contains("<td>foreignOperatorNSID</td>"));
                });
      }
    }
  }
}