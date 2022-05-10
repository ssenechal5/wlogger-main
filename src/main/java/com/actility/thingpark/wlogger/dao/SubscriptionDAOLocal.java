package com.actility.thingpark.wlogger.dao;

import com.actility.common.dao.DaoLocal;
import com.actility.thingpark.wlogger.entity.Subscription;

public interface SubscriptionDAOLocal extends DaoLocal<Subscription> {

  Subscription findFirstASDSubscriptionByID(String subscriberID);

  Subscription findActiveSubscriptionByID(String subscriberID);

  Subscription findSubscriptionByHref(String href);

  void createSubscription(Subscription subscription);
}
