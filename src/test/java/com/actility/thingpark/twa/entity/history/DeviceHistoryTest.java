package com.actility.thingpark.twa.entity.history;

import com.actility.thingpark.smp.rest.dto.DomainRestrictionDto;
import com.actility.thingpark.smp.rest.dto.DomainRestrictionsDto;
import com.actility.thingpark.smp.rest.dto.DomainTypeDto;
import com.actility.thingpark.wlogger.MongoDocumentFactory;
import com.actility.thingpark.wlogger.engine.model.Point;
import com.actility.thingpark.wlogger.engine.model.Record;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeviceHistoryTest {

  private MongoDocumentFactory mongoDocumentFactory;
  private DeviceHistory deviceHistory;

  @BeforeEach
  void setUp() {
    this.mongoDocumentFactory = new MongoDocumentFactory();
    this.deviceHistory = this.mongoDocumentFactory.newDeviceHistoriesList(DeviceType.LORA,1).get(0);
  }

  @Test
  void getLatitude() {
    assertEquals(123d,this.deviceHistory.getLatitude());
  }

  @Test
  void getLongitude() {
    assertEquals(125d,this.deviceHistory.getLongitude());
  }

  @Test
  void getLRRLatitude() {
    assertEquals(123d,this.deviceHistory.getLRRLatitude());
  }

  @Test
  void getLRRLongitude() {
    assertEquals(125d,this.deviceHistory.getLRRLongitude());
  }

  @Test
  void fillModelApplication() {
    this.deviceHistory.fillModelApplication();
    assertEquals("producerId",this.deviceHistory.getProducerId());
    assertEquals("moduleId",this.deviceHistory.getModuleId());
    assertEquals("version",this.deviceHistory.getVersion());

    assertEquals("producerId",this.deviceHistory.getProducerIdApp());
    assertEquals("moduleId",this.deviceHistory.getModuleIdApp());
    assertEquals("version",this.deviceHistory.getVersionApp());

  }

  @Test
  void setGeolocDecoded() {
    Map<String, Point> map = new HashMap<>();
    List<Record> records = new ArrayList<>();
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);
    records.add(Record.builder().coordinates(coordinates).build());
    map.put("0", Point.builder().unitId("unit").type("type").records(records).build());

    this.deviceHistory.setGeolocDecoded(map);

    assertEquals(125d, this.deviceHistory.getDecodedLongitude());
    assertEquals(123d, this.deviceHistory.getDecodedLatitude());
  }

  @Test
  void setGeolocDecoded_null() {
    this.deviceHistory.setGeolocDecoded(null);

    assertEquals(125d, this.deviceHistory.getDecodedLongitude());
    assertEquals(123d, this.deviceHistory.getDecodedLatitude());
  }

  @Test
  void getDecodedLongitude() {
    assertEquals(125d, this.deviceHistory.getDecodedLongitude());
  }

  @Test
  void getDecodedLatitude() {
    assertEquals(123d, this.deviceHistory.getDecodedLatitude());
  }

  private Search createSearch() {
    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
        new DomainRestrictionDto(
            DomainTypeDto.FULL, "France", new DomainRestrictionDto.Group("Site")));
    return Search.builder()
            .withLast("10")
            .withDecoder("AUTOMATIC")
            .withSubtype("subtype")
            .withLRRID("1")
            .withLRCID("1")
            .withDevADDRs(null)
            .withDeviceIDs(null)
            .withType(DeviceType.LORA)
            .withPageIndex(0)
            .withAsID(null)
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
  }

  @Test
  void isCustomerRestricted_empty_restrictions() {
    Search search = Search.builder()
            .withDomainRestrictions(null)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);
    assertNull(this.deviceHistory.getCustomerRestrictions());

    assertFalse(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void isCustomerRestricted() {
    this.deviceHistory.fillCustomerRestrictions(createSearch());
    assertEquals(1,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.FULL,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("France", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertFalse(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void fillCustomerRestrictions() {
    this.deviceHistory.fillCustomerRestrictions(createSearch());
    assertEquals(1,this.deviceHistory.getCustomerRestrictions().size());
  }

  @Test
  void getDomainFromDevDoms() {
    this.deviceHistory.fillCustomerRestrictions(createSearch());

    assertEquals(1 , this.deviceHistory.deviceDomains.size());

    assertNull(this.deviceHistory.getDomainFromDevDoms("Vertical"));
    assertEquals("France" , this.deviceHistory.getDomainFromDevDoms("Site").name);
    assertEquals("Site" , this.deviceHistory.getDomainFromDevDoms("Site").group);
  }

  @Test
  void isCustomerRestricted_more() {

    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "France/Caen", new DomainRestrictionDto.Group("Site")));
    Search search = Search.builder()
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);

    assertEquals(1,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France/Caen",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.FULL,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("France", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertTrue(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void isCustomerRestricted_withvertical() {

    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "France/Caen", new DomainRestrictionDto.Group("Site")),
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "Energy", new DomainRestrictionDto.Group("Vertical"))
            );
    Search search = Search.builder()
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);

    this.deviceHistory.deviceDomains = new ArrayList<>();
    this.deviceHistory.deviceDomains.add(new DomainMongo("France/Caen","Site"));
    this.deviceHistory.deviceDomains.add(new DomainMongo("Energy","Vertical"));

    assertEquals(2,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France/Caen",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.FULL,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("France/Caen", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertFalse(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void isCustomerRestricted_withvertical_prefix() {

    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
            new DomainRestrictionDto(
                    DomainTypeDto.PREFIX, "France", new DomainRestrictionDto.Group("Site")),
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "Energy", new DomainRestrictionDto.Group("Vertical"))
    );
    Search search = Search.builder()
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);

    this.deviceHistory.deviceDomains = new ArrayList<>();
    this.deviceHistory.deviceDomains.add(new DomainMongo("France/Caen","Site"));
    this.deviceHistory.deviceDomains.add(new DomainMongo("Energy","Vertical"));

    assertEquals(2,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.PREFIX,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("France/Caen", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertFalse(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void isCustomerRestricted_withvertical_prefix_nomatch() {

    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
            new DomainRestrictionDto(
                    DomainTypeDto.PREFIX, "France", new DomainRestrictionDto.Group("Site")),
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "Energy", new DomainRestrictionDto.Group("Vertical"))
    );
    Search search = Search.builder()
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);

    this.deviceHistory.deviceDomains = new ArrayList<>();
    this.deviceHistory.deviceDomains.add(new DomainMongo("Germany/Berlin","Site"));
    this.deviceHistory.deviceDomains.add(new DomainMongo("Energy","Vertical"));

    assertEquals(2,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.PREFIX,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("Germany/Berlin", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertTrue(this.deviceHistory.isCustomerRestricted());
  }

  @Test
  void isCustomerRestricted_withvertical_bis() {

    DomainRestrictionsDto customerRestrictions = new DomainRestrictionsDto();
    customerRestrictions.withAnds(
            new DomainRestrictionDto(
                    DomainTypeDto.FULL, "France/Caen", new DomainRestrictionDto.Group("Site")),
            new DomainRestrictionDto(
                    DomainTypeDto.PREFIX, "Energy", new DomainRestrictionDto.Group("Vertical"))
    );
    Search search = Search.builder()
            .withDomainRestrictions(customerRestrictions)
            .withSubscriberID("1").build();
    this.deviceHistory.fillCustomerRestrictions(search);

    this.deviceHistory.deviceDomains = new ArrayList<>();
    this.deviceHistory.deviceDomains.add(new DomainMongo("France/Caen","Site"));
    this.deviceHistory.deviceDomains.add(new DomainMongo("Location","Vertical"));

    assertEquals(2,this.deviceHistory.getCustomerRestrictions().size());
    assertEquals("France/Caen",this.deviceHistory.getCustomerRestrictions().get(0).getName());
    assertEquals(DomainTypeDto.FULL,this.deviceHistory.getCustomerRestrictions().get(0).getType());
    assertEquals("Site",this.deviceHistory.getCustomerRestrictions().get(0).getGroup().getName());

    assertEquals("France/Caen", this.deviceHistory.deviceDomains.get(0).name);
    assertEquals("Site", this.deviceHistory.deviceDomains.get(0).group);
    assertTrue(this.deviceHistory.isCustomerRestricted());
  }

}