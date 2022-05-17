package com.actility.thingpark.wlogger.controller.callback;

import com.actility.thingpark.smp.rest.dto.StateType;
import com.actility.thingpark.smp.rest.dto.StatusDto;
import com.actility.thingpark.smp.rest.dto.application.SubscribedDto;
import com.actility.thingpark.smp.rest.dto.application.UnsubscribedDto;
import com.actility.thingpark.smp.rest.dto.application.UpdatedDto;
import com.actility.thingpark.wlogger.dao.NetworkPartnerDAOLocal;
import com.actility.thingpark.wlogger.dao.SubscriptionDAOLocal;
import com.actility.thingpark.wlogger.entity.NetworkPartner;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.entity.Subscription;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.NetworkPartnerService;
import com.actility.thingpark.wlogger.service.OperatorService;
import com.actility.thingpark.wlogger.service.SubscriptionService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionCallbackControllerImplTest {

  private SubscriptionService subscriptionService;
  private SubscriptionDAOLocal subscriptionDaoMock;

  private NetworkPartnerService networkPartnerService;
  private NetworkPartnerDAOLocal networkPartnerDaoMock;

  private OperatorService operatorServiceMock;

  private SubscriptionCallbackControllerImpl subscriptionCallbackController;

  @BeforeEach
  void setUp() {
    this.subscriptionDaoMock = mock(SubscriptionDAOLocal.class);
    this.subscriptionService = new SubscriptionService(subscriptionDaoMock);

    this.networkPartnerDaoMock = mock(NetworkPartnerDAOLocal.class);
    this.networkPartnerService = new NetworkPartnerService(this.networkPartnerDaoMock);

    this.operatorServiceMock = mock(OperatorService.class);

    this.subscriptionCallbackController = new SubscriptionCallbackControllerImpl();
    this.subscriptionCallbackController.inject(this.subscriptionService, this.networkPartnerService, this.operatorServiceMock);
  }

  @Test
  void subscriptionSubscribed() throws WloggerException {
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);
    String subscriberId = "1";
    String subscriberExternalId = "1001";
    String subscriberHref = "/href/1";

    SubscribedDto subscribedDto = new SubscribedDto();
    subscribedDto.withOperator(new SubscribedDto.Operator(operatorId))
                  .withHref(subscriberHref)
                  .withOperation("TRY")
                  .withTransactionID("1")
                  .withSubscriber(new SubscribedDto.Subscriber("name",subscriberId, subscriberExternalId, "PRODUCTION"))
                  .withVendor(new SubscribedDto.Vendor("1"))
                  .withSupplier(new SubscribedDto.Supplier("1"));

    when(subscriptionDaoMock.findSubscriptionByHref(subscriberHref)).thenReturn(null);
    when(subscriptionDaoMock.findActiveSubscriptionByID(subscriberId)).thenReturn(null);

    when(operatorServiceMock.findOperatorByID(operatorId)).thenReturn(operator);

    Response response = this.subscriptionCallbackController.subscriptionSubscribed(subscribedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
  }

  @Test
  void subscriptionSubscribedError() {
    String operatorId = "1";
    String subscriberId = "1";
    String subscriberExternalId = "1001";
//    String subscriberHref = "/href/1";

    SubscribedDto subscribedDto = new SubscribedDto();
    subscribedDto.withOperator(new SubscribedDto.Operator(operatorId))
//            .withHref(subscriberHref)
            .withOperation("TRY")
            .withTransactionID("1")
            .withSubscriber(new SubscribedDto.Subscriber("name",subscriberId, subscriberExternalId, "PRODUCTION"))
            .withVendor(new SubscribedDto.Vendor("1"))
            .withSupplier(new SubscribedDto.Supplier("1"));

    assertThrows(WloggerException.class, () -> this.subscriptionCallbackController.subscriptionSubscribed(subscribedDto));
  }

  private static Stream<Arguments> provideDataArguments() {
    return Stream.of(
            Arguments.of("ACTIVE", Subscription.State.ACTIVE),
            Arguments.of("ACTIVE/SUSPENDED", Subscription.State.SUSPENDED));
  }

  @ParameterizedTest(name = "run subscriptionUpdated => (state before :{0}, state after :{1})")
  @MethodSource("provideDataArguments")
  void subscriptionUpdated(String state , Subscription.State afterState) throws WloggerException, DatatypeConfigurationException {
    String subscriberId = "1";
    String subscriberExternalId = "1001";
    String subscriberHref = "/href/1";
    final DateTime now = new DateTime();
    XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(now.toGregorianCalendar());
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);
    Subscription subscription = new Subscription(operator,subscriberId,subscriberExternalId,subscriberHref, Subscription.Type.PRODUCTION);
    subscription.setState(Subscription.State.DEACTIVATED);

    UpdatedDto updatedDto = new UpdatedDto();
    updatedDto.withTransactionID("1")
            .withSubscriber(new UpdatedDto.Subscriber(subscriberId,subscriberExternalId))
            .withApplication(new UpdatedDto.Application(new StateType(state, date, ""),"/app/1"));

    when(subscriptionDaoMock.findSubscriptionByHref(updatedDto.getApplication().getHref())).thenReturn(subscription);

    assertEquals(Subscription.State.DEACTIVATED, subscription.getState());

    Response response = this.subscriptionCallbackController.subscriptionUpdated(updatedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
    assertEquals(afterState, subscription.getState());
  }

  private static Stream<Arguments> provideDataArgumentsAgain() {
    return Stream.of(
            Arguments.of(Subscription.State.ACTIVE, Subscription.State.DEACTIVATED),
            Arguments.of(Subscription.State.SUSPENDED, Subscription.State.DEACTIVATED));
  }
  @ParameterizedTest(name = "run subscriptionUnsubscribed => (state before :{0}, state after :{1})")
  @MethodSource("provideDataArgumentsAgain")
  void subscriptionUnsubscribed(Subscription.State beforeState , Subscription.State afterState) throws WloggerException, DatatypeConfigurationException {
    String subscriberId = "1";
    String subscriberExternalId = "1001";
    String subscriberHref = "/href/1";
    final DateTime now = new DateTime();
    XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(now.toGregorianCalendar());
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);
    Subscription subscription = new Subscription(operator,subscriberId,subscriberExternalId,subscriberHref, Subscription.Type.PRODUCTION);
    subscription.setState(beforeState);

    UnsubscribedDto unsubscribedDto = new UnsubscribedDto();
    unsubscribedDto.withTransactionID("1")
            .withSubscriber(new UnsubscribedDto.Subscriber(subscriberId,subscriberExternalId))
            .withHref("/app/1");

    when(subscriptionDaoMock.findSubscriptionByHref(unsubscribedDto.getHref())).thenReturn(subscription);

    assertEquals(beforeState, subscription.getState());

    Response response = this.subscriptionCallbackController.subscriptionUnsubscribed(unsubscribedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
    assertEquals(afterState, subscription.getState());
  }

  private static Stream<Arguments> provideDataArguments2() {
    return Stream.of(
            Arguments.of("1", null, Subscription.State.ACTIVE),
            Arguments.of("1", null, Subscription.State.SUSPENDED),
            Arguments.of("1", null, Subscription.State.DEACTIVATED),
            Arguments.of(null, "1", Subscription.State.ACTIVE));
  }
  @ParameterizedTest(name = "run getModuleStatus => (subscriberId :{0}, supplierId :{1})")
  @MethodSource("provideDataArguments2")
  void getModuleStatus(String subscriberId, String supplierId, Subscription.State state) throws WloggerException {
    String subscriberExternalId = "1001";
    String subscriberHref = "/href/1";
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);
    Subscription subscription = new Subscription(operator,subscriberId,subscriberExternalId,subscriberHref, Subscription.Type.PRODUCTION);
    subscription.setState(state);

    NetworkPartner networkPartner = new NetworkPartner(operator, supplierId, subscriberHref);
    networkPartner.setState(state);

    when(subscriptionDaoMock.findFirstASDSubscriptionByID(subscriberId)).thenReturn(subscription);
    when(networkPartnerDaoMock.findFirstASDSubscriptionByID(supplierId)).thenReturn(networkPartner);

    if (subscriberId != null)
      assertEquals(state, subscription.getState());
    else
      assertEquals(state, networkPartner.getState());

    StatusDto dto = this.subscriptionCallbackController.getModuleStatus(subscriberId, supplierId);

    if (subscriberId != null)
      assertEquals(dto.getState().value(),subscription.getState().toString());
    else
      assertEquals(dto.getState().value(),networkPartner.getState().toString());
  }

  @Test
  void subscriptionPartnerSubscribed() throws WloggerException {
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);
    String supplierId = "1";
    String subscriberHref = "/href/1";

    com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto subscribedDto = new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto();
    subscribedDto.withOperator(new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto.Operator(operatorId))
            .withHref(subscriberHref)
            .withOperation("TRY")
            .withTransactionID("1")
            .withSupplier(new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto.Supplier(supplierId));

    when(networkPartnerDaoMock.findSubscriptionByHref(subscriberHref)).thenReturn(null);
    when(networkPartnerDaoMock.findActiveSubscriptionByID(supplierId)).thenReturn(null);

    when(operatorServiceMock.findOperatorByID(operatorId)).thenReturn(operator);

    Response response = this.subscriptionCallbackController.subscriptionPartnerSubscribed(subscribedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
  }

  @Test
  void subscriptionPartnerSubscribedError() {
    String operatorId = "1";
    String supplierId = "1";
//    String subscriberHref = "/href/1";

    com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto subscribedDto = new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto();
    subscribedDto.withOperator(new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto.Operator(operatorId))
//            .withHref(subscriberHref)
            .withOperation("TRY")
            .withTransactionID("1")
            .withSupplier(new com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto.Supplier(supplierId));

    assertThrows(WloggerException.class, () -> this.subscriptionCallbackController.subscriptionPartnerSubscribed(subscribedDto));
  }

  @Test
  void subscriptionPartnerUpdated() throws WloggerException {
    String subscriberHref = "/href/1";
    String supplierId = "1";
    String operatorId = "1";

    Operator operator = new Operator(operatorId,"/operator/1",200);

    NetworkPartner networkPartner = new NetworkPartner(operator, supplierId, subscriberHref);
    networkPartner.setState(Subscription.State.ACTIVE);

    com.actility.thingpark.smp.rest.dto.supplier.UpdatedDto updatedDto = new com.actility.thingpark.smp.rest.dto.supplier.UpdatedDto();
    updatedDto.withHref(subscriberHref);

    when(networkPartnerDaoMock.findSubscriptionByHref(updatedDto.getHref())).thenReturn(networkPartner);

    Response response = this.subscriptionCallbackController.subscriptionPartnerUpdated(updatedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
  }

  @Test
  void subscriptionPartnerUpdatedError() {
    String subscriberHref = "/href/1";
    String supplierId = "1";
    String operatorId = "1";

    Operator operator = new Operator(operatorId,"/operator/1",200);

    NetworkPartner networkPartner = new NetworkPartner(operator, supplierId, subscriberHref);
    networkPartner.setState(Subscription.State.SUSPENDED);

    com.actility.thingpark.smp.rest.dto.supplier.UpdatedDto updatedDto = new com.actility.thingpark.smp.rest.dto.supplier.UpdatedDto();
    updatedDto.withHref(subscriberHref);

    when(networkPartnerDaoMock.findSubscriptionByHref(updatedDto.getHref())).thenReturn(networkPartner);

    assertThrows(WloggerException.class, () -> this.subscriptionCallbackController.subscriptionPartnerUpdated(updatedDto));
  }

  private static Stream<Arguments> provideDataArgumentsAgain2() {
    return Stream.of(
            Arguments.of(Subscription.State.ACTIVE),
            Arguments.of(Subscription.State.SUSPENDED));
  }
  @ParameterizedTest(name = "run subscriptionPartnerUnsubscribed => (state before :{0}, state after :{1})")
  @MethodSource("provideDataArgumentsAgain2")
  void subscriptionPartnerUnsubscribed(Subscription.State beforeState) throws WloggerException {
    String supplierId = "1";
    String subscriberHref = "/href/1";
    String operatorId = "1";
    Operator operator = new Operator(operatorId,"/operator/1",200);

    com.actility.thingpark.smp.rest.dto.supplier.UnsubscribedDto unsubscribedDto = new com.actility.thingpark.smp.rest.dto.supplier.UnsubscribedDto();
    unsubscribedDto.withTransactionID("1")
            .withHref("/app/1");

    NetworkPartner networkPartner = new NetworkPartner(operator, supplierId, subscriberHref);
    networkPartner.setState(beforeState);

    when(networkPartnerDaoMock.findSubscriptionByHref(unsubscribedDto.getHref())).thenReturn(networkPartner);

    assertEquals(beforeState, networkPartner.getState());

    Response response = this.subscriptionCallbackController.subscriptionPartnerUnsubscribed(unsubscribedDto);

    assertNotNull(response);
    assertEquals(200, response.getStatus());
  }
}