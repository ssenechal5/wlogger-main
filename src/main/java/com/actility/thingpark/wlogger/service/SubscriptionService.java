package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dao.SubscriptionDAOLocal;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.entity.Subscription;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApplicationScoped
public class SubscriptionService {

  private final SubscriptionDAOLocal subscriptionDao;

  @Inject
  public SubscriptionService(
      SubscriptionDAOLocal subscriptionDao) {
    this.subscriptionDao = subscriptionDao;
  }

  public Subscription createSubscription(
      @NotNull Operator operator,
      @NotNull String subscriberId,
      String subscriberExternalId,
      @NotEmpty String subscriberHref,
      @NotNull Subscription.Type subscriberType) {
    Subscription subscription =
        new Subscription(
            operator, subscriberId, subscriberExternalId, subscriberHref, subscriberType);
    this.subscriptionDao.createSubscription(subscription);
    return subscription;
  }

  public Subscription findSubscriptionByHref(String href) {
    return this.subscriptionDao.findSubscriptionByHref(href);
  }

  public Subscription findFirstSubscriptionByID(String id) {
    return this.subscriptionDao.findFirstASDSubscriptionByID(id);
  }

  public Subscription findActiveSubscriptionByID(String id) {
    return this.subscriptionDao.findActiveSubscriptionByID(id);
  }

  public Subscription suspendSubscription(String href, Date timestamp) {
    Subscription subscription = findSubscriptionByHref(href);
    if (subscription != null) {
      subscription.setState(Subscription.State.SUSPENDED);
      subscription.setStateTimestamp(timestamp);
    }
    return subscription;
  }

  public Subscription activateSubscription(String href, Date timestamp) {
    Subscription subscription = findSubscriptionByHref(href);
    if (subscription != null) {
      subscription.setState(Subscription.State.ACTIVE);
      subscription.setStateTimestamp(timestamp);
    }
    return subscription;
  }

  public Subscription deactivateSubscription(String href, Date timestamp) {
    Subscription subscription = findSubscriptionByHref(href);
    if (subscription != null) {
      subscription.setState(Subscription.State.DEACTIVATED);
      subscription.setStateTimestamp(timestamp);
    }
    return subscription;
  }
}
