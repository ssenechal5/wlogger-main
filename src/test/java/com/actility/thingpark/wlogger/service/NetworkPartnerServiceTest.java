package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dao.NetworkPartnerDAOLocal;
import com.actility.thingpark.wlogger.entity.NetworkPartner;
import com.actility.thingpark.wlogger.entity.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NetworkPartnerServiceTest {

  private NetworkPartnerDAOLocal daoMock;
  private NetworkPartnerService service;

  private String supplierId = "supplierId";
  private String subscriberHref = "/href";
  private Operator operator;

  @BeforeEach
  void setUp() {
    daoMock = mock(NetworkPartnerDAOLocal.class);
    service = new NetworkPartnerService(daoMock);

    operator = new Operator("1", "/href", 120);

    NetworkPartner networkPartner = new NetworkPartner(operator,supplierId, subscriberHref);

    when(daoMock.findSubscriptionByHref(subscriberHref)).thenReturn(networkPartner);
    when(daoMock.findActiveSubscriptionByID(supplierId)).thenReturn(networkPartner);
    when(daoMock.findFirstASDSubscriptionByID(supplierId)).thenReturn(networkPartner);
  }


  @Test
  void createSubscription() {
    NetworkPartner networkPartner = this.service.createSubscription(operator, supplierId, subscriberHref);
    assertEquals(networkPartner.getHref(), subscriberHref);
  }

  @Test
  void findSubscriptionByHref() {
    NetworkPartner networkPartner = this.service.findSubscriptionByHref(subscriberHref);
    assertEquals(networkPartner.getHref(), subscriberHref);
  }

  @Test
  void findFirstSubscriptionByID() {
    NetworkPartner networkPartner = this.service.findFirstSubscriptionByID(supplierId);
    assertEquals(networkPartner.getHref(), subscriberHref);
  }

  @Test
  void findActiveSubscriptionByID() {
    NetworkPartner networkPartner = this.service.findActiveSubscriptionByID(supplierId);
    assertEquals(networkPartner.getHref(), subscriberHref);
  }

  @Test
  void deleteSubscription() {
    assertTrue(this.service.deleteSubscription(subscriberHref));
  }
}