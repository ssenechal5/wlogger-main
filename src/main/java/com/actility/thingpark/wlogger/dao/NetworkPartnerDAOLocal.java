package com.actility.thingpark.wlogger.dao;

import com.actility.common.dao.DaoLocal;
import com.actility.thingpark.wlogger.entity.NetworkPartner;

public interface NetworkPartnerDAOLocal extends DaoLocal<NetworkPartner> {

  NetworkPartner findFirstASDSubscriptionByID(String supplierId);

  NetworkPartner findActiveSubscriptionByID(String supplierId);

  NetworkPartner findSubscriptionByHref(String href);

  void createSubscription(NetworkPartner networkPartner);
}
