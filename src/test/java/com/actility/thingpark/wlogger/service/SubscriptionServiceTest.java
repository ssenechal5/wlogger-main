package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dao.SubscriptionDAOLocal;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.entity.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionServiceTest {

  private SubscriptionDAOLocal daoMock;
  private SubscriptionService service;

  private String subscriberId = "subscriberId";
  private String subscriberHref = "/href";
  private Operator operator;

  Subscription subscription;

  @BeforeEach
  void setUp() {
    daoMock = mock(SubscriptionDAOLocal.class);
    service = new SubscriptionService(daoMock);
    operator = new Operator("1","/href",120);

    subscription = new Subscription(operator, subscriberId,
            "subscriberExternalId", subscriberHref, Subscription.Type.PRODUCTION);

    when(daoMock.findSubscriptionByHref(subscriberHref)).thenReturn(subscription);
    when(daoMock.findFirstASDSubscriptionByID(subscriberId)).thenReturn(subscription);
    when(daoMock.findActiveSubscriptionByID(subscriberId)).thenReturn(subscription);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void createSubscription() {
    assertEquals(subscription,this.service.createSubscription(operator,subscriberId,
            "subscriberExternalId", subscriberHref, Subscription.Type.PRODUCTION));
  }

  @Test
  void findSubscriptionByHref() {
    assertEquals(subscription, this.service.findSubscriptionByHref(subscriberHref));
  }

  @Test
  void findFirstSubscriptionByID() {
    assertEquals(subscription, this.service.findFirstSubscriptionByID(subscriberId));
  }

  @Test
  void findActiveSubscriptionByID() {
    assertEquals(subscription, this.service.findActiveSubscriptionByID(subscriberId));
  }

  @Test
  void suspendSubscription() {
    assertEquals(subscription.getHref(), this.service.suspendSubscription(subscriberHref,new Date()).getHref());
    assertEquals(Subscription.State.SUSPENDED, this.service.suspendSubscription(subscriberHref,new Date()).getState());
  }

  @Test
  void activateSubscription() {
    assertEquals(subscription.getHref(), this.service.activateSubscription(subscriberHref,new Date()).getHref());
    assertEquals(Subscription.State.ACTIVE, this.service.activateSubscription(subscriberHref,new Date()).getState());
  }

  @Test
  void deactivateSubscription() {
    assertEquals(subscription.getHref(), this.service.deactivateSubscription(subscriberHref,new Date()).getHref());
    assertEquals(Subscription.State.DEACTIVATED, this.service.deactivateSubscription(subscriberHref,new Date()).getState());
  }
}