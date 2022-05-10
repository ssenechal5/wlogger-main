package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dao.NetworkPartnerDAOLocal;
import com.actility.thingpark.wlogger.entity.NetworkPartner;
import com.actility.thingpark.wlogger.entity.Operator;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;

@ApplicationScoped
public class NetworkPartnerService {

  private final NetworkPartnerDAOLocal networkPartnerDao;

  @Inject
  public NetworkPartnerService(final NetworkPartnerDAOLocal networkPartnerDao) {
    this.networkPartnerDao = networkPartnerDao;
  }

  public NetworkPartner createSubscription(
      @Nonnull Operator operator, @Nonnull String supplierId, @NotEmpty String subscriberHref) {
    NetworkPartner networkPartner = new NetworkPartner(operator, supplierId, subscriberHref);
    this.networkPartnerDao.createSubscription(networkPartner);
    return networkPartner;
  }

  public NetworkPartner findSubscriptionByHref(String href) {
    return this.networkPartnerDao.findSubscriptionByHref(href);
  }

  public NetworkPartner findFirstSubscriptionByID(String id) {
    return this.networkPartnerDao.findFirstASDSubscriptionByID(id);
  }

  public NetworkPartner findActiveSubscriptionByID(String id) {
    return this.networkPartnerDao.findActiveSubscriptionByID(id);
  }

  public boolean deleteSubscription(String href) {
    NetworkPartner networkPartner = findSubscriptionByHref(href);
    if (networkPartner == null) {
      return false;
    }
    this.networkPartnerDao.delete(networkPartner);
    return true;
  }
}
