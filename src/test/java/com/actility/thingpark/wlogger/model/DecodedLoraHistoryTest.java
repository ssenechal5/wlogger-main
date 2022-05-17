package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementLfd;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodedLoraHistoryTest {

  DecodedLoraHistory.Builder builder = DecodedLoraHistory.builder();

  @Test
  void getUid() {
    assertEquals("1",builder.withUid("1").build().getUid());
  }

  @Test
  void getDirection() {
    assertEquals(Direction.UPLINK,builder.withDirection(Direction.UPLINK).build().getDirection());
  }

  @Test
  void getDevAddr() {
    assertEquals("1",builder.withDevAddr("1").build().getDevAddr());
  }

  @Test
  void getLrrLAT() {
    assertEquals("1",builder.withLrrLAT("1").build().getLrrLAT());
  }

  @Test
  void getLrrLON() {
    assertEquals("1",builder.withLrrLON("1").build().getLrrLON());
  }

  @Test
  void getCustomerId() {
    assertEquals("1",builder.withCustomerId("1").build().getCustomerId());
  }

  @Test
  void getLfdAsElement() {
    ArrayList lfdList = new ArrayList<>();
    lfdList.add(Lfd.builder().withCnt(1).withDfc("dfc").build());
    lfdList.add(Lfd.builder().withCnt(2).withDfc("dfc2").build());
    DecodedLoraHistory decodedLoraHistory = builder.withLfd(lfdList).build();
    List<Element> list = decodedLoraHistory.getLfdAsElement();
    assertEquals(2, list.size());
    ElementLfd element = (ElementLfd) list.get(0);
    assertEquals("1", element.getCnt());
    assertEquals("dfc", element.getDfc());
    element = (ElementLfd) list.get(1);
    assertEquals("2", element.getCnt());
    assertEquals("dfc2", element.getDfc());
  }

  @Test
  void getLfdAsJsonString() {
    ArrayList lfdList = new ArrayList<>();
    lfdList.add(Lfd.builder().withCnt(1).withDfc("dfc").build());
    lfdList.add(Lfd.builder().withCnt(2).withDfc("dfc2").build());
    DecodedLoraHistory decodedLoraHistory = builder.withLfd(lfdList).build();
    assertEquals(
        "[{\"dfc\":\"dfc\",\"cnt\":1},{\"dfc\":\"dfc2\",\"cnt\":2}]",
        decodedLoraHistory.getLfdAsJsonString());
  }

  @Test
  void getLfdAsCsvString() {
    ArrayList lfdList = new ArrayList<>();
    lfdList.add(Lfd.builder().withCnt(1).withDfc("dfc").build());
    lfdList.add(Lfd.builder().withCnt(2).withDfc("dfc2").build());
    DecodedLoraHistory decodedLoraHistory = builder.withLfd(lfdList).build();
    assertEquals("dfc=1,dfc2=2",decodedLoraHistory.getLfdAsCsvString());
  }

}