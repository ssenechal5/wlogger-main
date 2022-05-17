package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementChains;
import com.actility.thingpark.wlogger.dto.ElementLrrs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LrrTest {

  public static final String T_10_30_15_000000001_Z = "2020-03-25T10:30:15.000000001Z";
  Lrr lrr;

  Lrr lrrWithoutChains;

  String sDate ="25/03/2020 10:30:15";
  Date date;

  @BeforeEach
  void setUp() throws ParseException {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Lrr.Chain.TIME_ZONE));
    date = simpleDateFormat.parse(sDate);

    Lrr.Builder builder = Lrr.builder();
    Lrr.Chain.Builder builderChain = Lrr.Chain.builder();
    builderChain.withChain(1).withChannel("Channel").withNanoseconds(1).withTimestamp(date).withTimestampType(1);
    List<Lrr.Chain> chains = new ArrayList<>();
    chains.add(builderChain.build());
    builder.withLrrId("id").withForeignOperatorNSID("ForeignOperatorNSID")
            .withRfRegion("RfRegion").withIsmBand("IsmBand")
            .withEsp(0.0f).withGwDLAllowed(true).withGwId("GwId").withRssi(0.1f).withSnr(0.2f)
            .withForeignOperatorNetId("ForeignOperatorNetId").withGwToken("GwToken").withNetworkPartnerId("NetworkPartnerId")
            .withInstantPer(0.3f).withChains(chains);

    lrr = builder.build();

    builder.withChains(null);
    lrrWithoutChains = builder.build();
  }

  @Test
  void getAsElement() {
    Optional<ElementLrrs> lrrs =  lrr.getAsElement(true,true);
    assertEquals(true,lrrs.isPresent());

    ElementLrrs element = lrrs.get();
    assertEquals("id",element.getLrrid());
    assertEquals("ForeignOperatorNSID",element.getForeignOperatorNSID());
    assertEquals("RfRegion",element.getRfRegion());
    assertEquals("IsmBand",element.getIsmBand());
    assertEquals(true,element.getGwDL());
    assertEquals("GwId",element.getGwID());
    assertEquals("ForeignOperatorNetId",element.getGwOp());
    assertEquals("GwToken",element.getGwTk());
    assertEquals("0.0",element.getLrrESP());
    assertEquals("0.1",element.getLrrRSSI());
    assertEquals("0.2",element.getLrrSNR());
    assertEquals(1,element.getLrrChains().size());

    ElementChains chain = (ElementChains) element.getLrrChains().get(0);
    assertEquals("Channel",chain.getChannel());
    assertEquals(T_10_30_15_000000001_Z,chain.getTime());
    assertEquals("1",chain.getChain());
    assertEquals("-",chain.getTtype());
  }

  @Test
  void getAsElementNotAll() {
    Optional<ElementLrrs> lrrs =  lrr.getAsElement(false,true);
    assertEquals(true,lrrs.isPresent());

    ElementLrrs element = lrrs.get();
    assertEquals("id",element.getLrrid());
    assertEquals("ForeignOperatorNSID",element.getForeignOperatorNSID());
    assertEquals("RfRegion",element.getRfRegion());
    assertEquals("IsmBand",element.getIsmBand());
    assertEquals(false,element.getGwDL());
    assertEquals("",element.getGwID());
    assertEquals("",element.getGwOp());
    assertEquals("",element.getGwTk());
    assertEquals("",element.getLrrESP());
    assertEquals("",element.getLrrRSSI());
    assertEquals("",element.getLrrSNR());
    assertEquals(1,element.getLrrChains().size());

    ElementChains chain = (ElementChains) element.getLrrChains().get(0);
    assertEquals("Channel",chain.getChannel());
    assertEquals(T_10_30_15_000000001_Z,chain.getTime());
    assertEquals("1",chain.getChain());
    assertEquals("-",chain.getTtype());
  }

  @Test
  void getAsElementNotPassiveRoaming() {
    Optional<ElementLrrs> lrrs =  lrr.getAsElement(true,false);
    assertEquals(true,lrrs.isPresent());

    ElementLrrs element = lrrs.get();
    assertEquals("id",element.getLrrid());
    assertEquals("ForeignOperatorNSID",element.getForeignOperatorNSID());
    assertEquals("RfRegion",element.getRfRegion());
    assertEquals("IsmBand",element.getIsmBand());
    assertEquals(false,element.getGwDL());
    assertEquals("",element.getGwID());
    assertEquals("",element.getGwOp());
    assertEquals("",element.getGwTk());
    assertEquals("0.0",element.getLrrESP());
    assertEquals("0.1",element.getLrrRSSI());
    assertEquals("0.2",element.getLrrSNR());
    assertEquals(1,element.getLrrChains().size());

    ElementChains chain = (ElementChains) element.getLrrChains().get(0);
    assertEquals("Channel",chain.getChannel());
    assertEquals(T_10_30_15_000000001_Z,chain.getTime());
    assertEquals("1",chain.getChain());
    assertEquals("-",chain.getTtype());
  }

  @Test
  void getAsElementWithoutChains() {
    Optional<ElementLrrs> lrrs =  lrrWithoutChains.getAsElement(true,true);
    assertEquals(true,lrrs.isPresent());

    ElementLrrs element = lrrs.get();
    assertEquals("id",element.getLrrid());
    assertEquals("ForeignOperatorNSID",element.getForeignOperatorNSID());
    assertEquals("RfRegion",element.getRfRegion());
    assertEquals("IsmBand",element.getIsmBand());
    assertEquals(true,element.getGwDL());
    assertEquals("GwId",element.getGwID());
    assertEquals("ForeignOperatorNetId",element.getGwOp());
    assertEquals("GwToken",element.getGwTk());
    assertEquals("0.0",element.getLrrESP());
    assertEquals("0.1",element.getLrrRSSI());
    assertEquals("0.2",element.getLrrSNR());
    assertNull(element.getLrrChains());
  }

  @Test
  void getAsCsv() {
    assertEquals(12,lrr.getAsCsv(true).size());
    assertEquals(7,lrr.getAsCsv(false).size());
  }

  @Test
  void getAsCsvWithoutDestinations() {
    assertEquals(12,lrrWithoutChains.getAsCsv(true).size());
    assertEquals(7,lrrWithoutChains.getAsCsv(false).size());
  }

  @Test
  void getEmptyCsv() {
    assertEquals(12,lrr.getEmptyCsv(true).size());
    assertEquals(7,lrr.getEmptyCsv(false).size());
  }

  @Test
  void getEmptyCsvWithoutDestinations() {
    assertEquals(12,lrrWithoutChains.getEmptyCsv(true).size());
    assertEquals(7,lrrWithoutChains.getEmptyCsv(false).size());
  }

  @Test
  void getChainsAsElements() {
    List<Element> list = lrr.getChainsAsElements();
    assertEquals(1,list.size());

    ElementChains chain = (ElementChains) list.get(0);
    assertEquals("Channel",chain.getChannel());
    assertEquals(T_10_30_15_000000001_Z,chain.getTime());
    assertEquals("1",chain.getChain());
    assertEquals("-",chain.getTtype());
  }

  @Test
  void getChainsAsElementsWithoutChains() {
    assertNull(lrrWithoutChains.getChainsAsElements());
  }

  @Test
  void getChainsAsElementsWithoutDestinations() {
    assertNull(lrrWithoutChains.getChainsAsElements());
  }

  @Test
  void getChainsAsCsv() {
    assertEquals(
        "[{\"chain\":\"1\",\"channel\":\"Channel\",\"time\":\""+T_10_30_15_000000001_Z+"\",\"ttype\":\"-\"}]",
        lrr.getChainsAsCsv());
  }

  @Test
  void getAsHtmlRow() {
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td> CHAIN[1]:"+T_10_30_15_000000001_Z+" {-}<br/></td><td></td><td>RfRegion</td><td>GwId</td><td>GwToken</td><td>1</td><td>ForeignOperatorNetId</td><td>ForeignOperatorNSID</td></tr>",
        lrr.getAsHtmlRow(Direction.UPLINK, true));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td> CHAIN[1]:"+T_10_30_15_000000001_Z+" {-}<br/></td><td></td><td>RfRegion</td></tr>",
        lrr.getAsHtmlRow(Direction.UPLINK, false));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td> CHAIN[1]:"+T_10_30_15_000000001_Z+" {-}{Channel}<br/></td><td></td><td>RfRegion</td><td>GwId</td><td>GwToken</td><td>1</td><td>ForeignOperatorNetId</td><td>ForeignOperatorNSID</td></tr>",
        lrr.getAsHtmlRow(Direction.LOCATION, true));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td> CHAIN[1]:"+T_10_30_15_000000001_Z+" {-}{Channel}<br/></td><td></td><td>RfRegion</td></tr>",
        lrr.getAsHtmlRow(Direction.LOCATION,false));
  }

  @Test
  void getChainsAsCsvWithoutChains() {
    assertEquals(
            "",
            lrrWithoutChains.getChainsAsCsv());
  }

  @Test
  void getAsHtmlRowWithoutChains() {
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td>-</td><td></td><td>RfRegion</td><td>GwId</td><td>GwToken</td><td>1</td><td>ForeignOperatorNetId</td><td>ForeignOperatorNSID</td></tr>",
        lrrWithoutChains.getAsHtmlRow(Direction.UPLINK, true));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td>-</td><td></td><td>RfRegion</td></tr>",
        lrrWithoutChains.getAsHtmlRow(Direction.UPLINK,false));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td>-</td><td></td><td>RfRegion</td><td>GwId</td><td>GwToken</td><td>1</td><td>ForeignOperatorNetId</td><td>ForeignOperatorNSID</td></tr>",
        lrrWithoutChains.getAsHtmlRow(Direction.LOCATION, true));
    assertEquals(
        "<tr><td>id</td><td>0.1</td><td>0.2</td><td>0.0</td><td>-</td><td></td><td>RfRegion</td></tr>",
        lrrWithoutChains.getAsHtmlRow(Direction.LOCATION,false));
  }
}