package com.actility.thingpark.wlogger.controller.callback;

import com.actility.thingpark.smp.rest.dto.Status;
import com.actility.thingpark.smp.rest.dto.StatusDto;
import com.actility.thingpark.smp.rest.dto.application.SubscribedDto;
import com.actility.thingpark.smp.rest.dto.application.UnsubscribedDto;
import com.actility.thingpark.smp.rest.dto.application.UpdatedDto;
import com.actility.thingpark.smp.rest.dto.system.OperatorsDto;
import com.actility.thingpark.wlogger.Errors;
import com.actility.thingpark.wlogger.entity.NetworkPartner;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.entity.Subscription;
import com.actility.thingpark.wlogger.exception.SmpException;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.NetworkPartnerService;
import com.actility.thingpark.wlogger.service.OperatorService;
import com.actility.thingpark.wlogger.service.SubscriptionService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Controller implementation for subscription service. */
@Path("/appCallback")
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML})
public class SubscriptionCallbackControllerImpl {

  public static final String TRANSACTION_ID_ELEMENT_IS_MISSING = "transactionID element is missing";
  public static final String SUBSCRIBER_ELEMENT_IS_MISSING = "subscriber element is missing";
  public static final String HREF_ELEMENT_IS_MISSING = "href element is missing";
  public static final String ELEMENT_IS_MISSING = "Main element is missing";
  public static final String ID_SUBSCRIBER_ELEMENT_IS_MISSING = "ID subscriber element is missing";
  public static final String OPERATION_ELEMENT_IS_MISSING = "operation element is missing";
  public static final String NAME_ELEMENT_IS_MISSING = "name element is missing";
  public static final String TYPE_ELEMENT_IS_MISSING = "type element is missing";
  public static final String OPERATOR_ELEMENT_IS_MISSING = "operator element is missing";
  public static final String ID_OPERATOR_ELEMENT_IS_MISSING = "ID operator element is missing";
  public static final String VENDOR_ELEMENT_IS_MISSING = "vendor element is missing";
  public static final String ID_VENDOR_ELEMENT_IS_MISSING = "ID vendor element is missing";
  public static final String SUPPLIER_ELEMENT_IS_MISSING = "supplier element is missing";
  public static final String ID_SUPPLIER_ELEMENT_IS_MISSING = "ID supplier element is missing";
  public static final String APPLICATION_ELEMENT_IS_MISSING = "application element is missing";
  public static final String STATE_ELEMENT_IS_MISSING = "state element is missing";
  public static final String STATE_VALUE_ELEMENT_IS_MISSING = "state value element is missing";
  public static final String STATE_TIMESTAMP_ELEMENT_IS_MISSING = "state timestamp element is missing";

  public static final String ACTIVE = "ACTIVE";
  public static final String ACTIVE_SUSPENDED = "ACTIVE/SUSPENDED";

  public static final String COMMIT = "COMMIT";
  public static final String TRY = "TRY";
  public static final String CANCEL = "CANCEL";

  private static final Logger logger = Logger.getLogger(SubscriptionCallbackControllerImpl.class.getName());

  private SubscriptionService subscriptionService;
  private NetworkPartnerService networkPartnerService;
  private OperatorService operatorService;

  @Inject
  void inject(
      final SubscriptionService subscriptionService,
      final NetworkPartnerService networkPartnerService,
      final OperatorService operatorService) {
    this.subscriptionService = subscriptionService;
    this.networkPartnerService = networkPartnerService;
    this.operatorService = operatorService;
  }

  /**
   * Call back for a new subscription subscribed
   *
   * @param subscribedDto the subscribed subscription DTO informations
   */
  @Path("/subscriptions/subscribed")
  @POST
  @Transactional
  public Response subscriptionSubscribed(SubscribedDto subscribedDto) throws WloggerException {

    controlNotNullDto(subscribedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getHref() == null, HREF_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getSubscriber() == null,SUBSCRIBER_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getSubscriber().getID() == null,ID_SUBSCRIBER_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getSubscriber().getName() == null, NAME_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getSubscriber().getType() == null, TYPE_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getTransactionID() == null, TRANSACTION_ID_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getOperation() == null, OPERATION_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getOperator() == null,OPERATOR_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getOperator().getID() == null,ID_OPERATOR_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getVendor() == null,VENDOR_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getVendor().getID() == null,ID_VENDOR_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getSupplier() == null,SUPPLIER_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getSupplier().getID() == null,ID_SUPPLIER_ELEMENT_IS_MISSING);

    try {
      Subscription.Type.valueOf(subscribedDto.getSubscriber().getType());
    } catch (IllegalArgumentException e) {
      logger.log(Level.INFO,e.getMessage(),e);
      throw WloggerException.invalidDocument("type element is wrong : not a valid value");
    }

    Subscription subscription = null;
    if (subscribedDto.getOperation().equals(TRY) || subscribedDto.getOperation().equals(COMMIT)) {
      subscription = this.subscriptionService.findSubscriptionByHref(subscribedDto.getHref());
      if (subscription != null) {
        throw WloggerException.applicationError(Errors.SUBSCRIPTION_EXISTS_FOR_HREF, subscribedDto.getHref());
      }

      subscription = this.subscriptionService.findActiveSubscriptionByID(subscribedDto.getSubscriber().getID());
      if (subscription != null) {

        // Change du to RDTP-17768 : desactivate sub for subsriberId , and create a new one
        subscription = this.subscriptionService.deactivateSubscription(subscription.getHref(), new Date());
        if (subscription == null) {
          throw WloggerException.applicationError(Errors.NO_SUBSCRIPTION_FOR_HREF, subscription.getHref());
        }
        // throw WloggerException.applicationError(Errors.ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID, subscribedDto.getSubscriber().getID());
      }

      Operator operator = getOrCreateOperator(subscribedDto.getOperator().getID());

      Subscription.Type subscriberType = Subscription.Type.valueOf(subscribedDto.getSubscriber().getType());

      if (subscribedDto.getOperation().equals(COMMIT)) {
        this.subscriptionService.createSubscription(
            operator,
            subscribedDto.getSubscriber().getID(),
            subscribedDto.getSubscriber().getExtID(),
            subscribedDto.getHref(),
            subscriberType);
      }
      return Response.ok().build();
    } else if (subscribedDto.getOperation().equals(CANCEL)) {
      return Response.ok().build();
    } else {
      throw WloggerException.invalidDocument("operation must be TRY, COMMIT or CANCEL");
    }
  }

  private void controlNotNullDto(boolean b, String message) throws WloggerException {
    if (b) throw WloggerException.invalidDocument(message);
  }

  /**
   * Call back for a subscription updated
   *
   * @param updatedDto the updated subscription DTO informations
   */
  @Path("/subscriptions/updated")
  @POST
  @Transactional
  public Response subscriptionUpdated(UpdatedDto updatedDto) throws WloggerException {

    controlNotNullDto(updatedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getTransactionID() == null, TRANSACTION_ID_ELEMENT_IS_MISSING);

    controlNotNullDto(updatedDto.getSubscriber() == null,SUBSCRIBER_ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getSubscriber().getID() == null,ID_SUBSCRIBER_ELEMENT_IS_MISSING);

    controlNotNullDto(updatedDto.getApplication() == null, APPLICATION_ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getApplication().getHref() == null, HREF_ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getApplication().getState() == null, STATE_ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getApplication().getState().getValue() == null, STATE_VALUE_ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getApplication().getState().getTimestamp() == null, STATE_TIMESTAMP_ELEMENT_IS_MISSING);

    if (updatedDto.getApplication().getState().getValue().equals(ACTIVE)) {
      Subscription subscription =
          this.subscriptionService.activateSubscription(
              updatedDto.getApplication().getHref(),
              updatedDto
                  .getApplication()
                  .getState()
                  .getTimestamp()
                  .toGregorianCalendar()
                  .getTime());
      if (subscription == null) {
        throw WloggerException.applicationError(
            Errors.NO_SUBSCRIPTION_FOR_HREF, updatedDto.getApplication().getHref());
      }
      return Response.ok().build();
    } else if (updatedDto.getApplication().getState().getValue().equals(ACTIVE_SUSPENDED)) {
      Subscription subscription =
          this.subscriptionService.suspendSubscription(
              updatedDto.getApplication().getHref(),
              updatedDto
                  .getApplication()
                  .getState()
                  .getTimestamp()
                  .toGregorianCalendar()
                  .getTime());
      if (subscription == null) {
        throw WloggerException.applicationError(
            Errors.NO_SUBSCRIPTION_FOR_HREF, updatedDto.getApplication().getHref());
      }
      return Response.ok().build();
    } else {
      throw WloggerException.invalidDocument("state value must be ACTIVE or ACTIVE/SUSPENDED");
    }
  }

  /**
   * Call back for a subscription unsubscribed
   *
   * @param unsubscribedDto the unsubscribed subscription DTO informations
   */
  @Path("/subscriptions/unsubscribed")
  @POST
  @Transactional
  public Response subscriptionUnsubscribed(UnsubscribedDto unsubscribedDto) throws WloggerException {

    controlNotNullDto(unsubscribedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(unsubscribedDto.getHref() == null, HREF_ELEMENT_IS_MISSING);

    controlNotNullDto(unsubscribedDto.getSubscriber() == null,SUBSCRIBER_ELEMENT_IS_MISSING);
    controlNotNullDto(unsubscribedDto.getSubscriber().getID() == null,ID_SUBSCRIBER_ELEMENT_IS_MISSING);

    controlNotNullDto(unsubscribedDto.getTransactionID() == null, TRANSACTION_ID_ELEMENT_IS_MISSING);

    Subscription subscription = this.subscriptionService.deactivateSubscription(unsubscribedDto.getHref(), new Date());
    if (subscription == null) {
      throw WloggerException.applicationError(Errors.NO_SUBSCRIPTION_FOR_HREF, unsubscribedDto.getHref());
    }
    return Response.ok().build();
  }

  @Path("/modules/status")
  @GET
  public StatusDto getModuleStatus(
          @QueryParam("subscriberID") String subscriberID,
          @QueryParam("supplierID") String supplierID) throws WloggerException {

    if (subscriberID != null) {
      // cas subscriberID
      Subscription subscription = subscriptionService.findFirstSubscriptionByID(subscriberID);
      if (subscription == null) {
        throw WloggerException.notFound("subscriberID : " + subscriberID);
      }
      return new StatusDto(Status.fromValue(subscription.getState().toString()));
    } else if (supplierID != null) {
      // cas supplierID
      NetworkPartner networkPartner = networkPartnerService.findFirstSubscriptionByID(supplierID);
      if (networkPartner == null) {
        throw WloggerException.notFound("supplierID : " + supplierID);
      }
      return new StatusDto(Status.fromValue(networkPartner.getState().toString()));
    } else {
      throw WloggerException.notFound("No query parameters found");
    }
  }

  /**
   * Call back for a new subscription subscribed for supplier
   *
   * @param subscribedDto the subscribed subscription DTO informations
   */
  @Path("/partners/subscribed")
  @POST
  @Transactional
  public Response subscriptionPartnerSubscribed(
      com.actility.thingpark.smp.rest.dto.supplier.SubscribedDto subscribedDto)
      throws WloggerException {

    controlNotNullDto(subscribedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getHref() == null, HREF_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getOperator() == null,OPERATOR_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getOperator().getID() == null,ID_OPERATOR_ELEMENT_IS_MISSING);

    controlNotNullDto(subscribedDto.getSupplier() == null,SUPPLIER_ELEMENT_IS_MISSING);
    controlNotNullDto(subscribedDto.getSupplier().getID() == null,ID_SUPPLIER_ELEMENT_IS_MISSING);

    if (this.networkPartnerService.findSubscriptionByHref(subscribedDto.getHref()) != null) {
      throw WloggerException.applicationError(
          Errors.SUBSCRIPTION_EXISTS_FOR_HREF, subscribedDto.getHref());
    }

    if (this.networkPartnerService.findActiveSubscriptionByID(subscribedDto.getSupplier().getID())
        != null) {
      throw WloggerException.applicationError(
          Errors.ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID, subscribedDto.getSupplier().getID());
    }

    Operator operator = getOrCreateOperator(subscribedDto.getOperator().getID());
    this.networkPartnerService.createSubscription(
        operator, subscribedDto.getSupplier().getID(), subscribedDto.getHref());
    return Response.ok().build();
  }

  private Operator getOrCreateOperator(String operatorID) throws WloggerException {
    Operator operator = this.operatorService.findOperatorByID(operatorID);
    if (operator == null) {
      OperatorsDto operatorsDto;
      try {
        operatorsDto = this.operatorService.getOperators(operatorID);
      } catch (SmpException e) {
        logger.log(Level.INFO,e.getMessage(),e);
        throw WloggerException.applicationError(Errors.NO_OPERATOR_FOR_ID, operatorID);
      }
      if (operatorsDto == null) {
        throw WloggerException.applicationError(Errors.NO_OPERATOR_FOR_ID, operatorID);
      } else {
        String href = operatorsDto.getBriefs().getBrief().get(0).getHref();
        operator = this.operatorService.createOperator(operatorID, href);
      }
    }
    return operator;
  }

  /**
   * Call back for a subscription updated for supplier
   *
   * @param updatedDto the updated subscription DTO informations
   */
  @Path("/partners/updated")
  @POST
  @Transactional
  public Response subscriptionPartnerUpdated(
      com.actility.thingpark.smp.rest.dto.supplier.UpdatedDto updatedDto)
      throws WloggerException {

    controlNotNullDto(updatedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(updatedDto.getHref() == null, HREF_ELEMENT_IS_MISSING);

    NetworkPartner networkPartner =
        this.networkPartnerService.findSubscriptionByHref(updatedDto.getHref());
    if (networkPartner == null) {
      throw WloggerException.applicationError(
          Errors.NO_SUBSCRIPTION_FOR_HREF, updatedDto.getHref());
    } else {
      if (!networkPartner.getState().equals(Subscription.State.ACTIVE)) {
        throw WloggerException.applicationError(
            Errors.NO_ACTIVE_SUBSCRIPTION_EXISTS_FOR_ID, updatedDto.getHref());
      }
    }
    return Response.ok().build();
  }

  /**
   * Call back for a subscription unsubscribed for supplier
   *
   * @param unsubscribedDto the unsubscribed subscription DTO informations
   */
  @Path("/partners/unsubscribed")
  @POST
  @Transactional
  public Response subscriptionPartnerUnsubscribed(
      com.actility.thingpark.smp.rest.dto.supplier.UnsubscribedDto unsubscribedDto)
      throws WloggerException {

    controlNotNullDto(unsubscribedDto == null, ELEMENT_IS_MISSING);
    controlNotNullDto(unsubscribedDto.getHref() == null, HREF_ELEMENT_IS_MISSING);

    if (!this.networkPartnerService.deleteSubscription(unsubscribedDto.getHref())) {
      throw WloggerException.applicationError(
          Errors.NO_SUBSCRIPTION_FOR_HREF, unsubscribedDto.getHref());
    }
    return Response.ok().build();
  }
}
