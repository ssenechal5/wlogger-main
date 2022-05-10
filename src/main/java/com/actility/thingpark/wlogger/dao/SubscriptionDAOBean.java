package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.ActorEntity;
import com.actility.thingpark.wlogger.entity.Subscription;
import com.actility.thingpark.wlogger.utils.JPAUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class SubscriptionDAOBean extends WloggerDAO<Subscription> implements SubscriptionDAOLocal {

  public static final String SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_ORDER_BY_FIELD_STATE_STATE_1_STATE_2_STATE_3 = "select s from Subscription s where s.id = :id order by field(state,:state1,:state2,:state3)";
  public static final String SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_AND_NOT_S_STATE_STATE = "select s from Subscription s where s.id = :id and not s.state = :state";
  public static final String SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_HREF_HREF = "select s from Subscription s where s.href = :href";
  public static final String ID = "id";

  @Inject
  public SubscriptionDAOBean(final EntityManager em) {
    super(em);
  }

  @Override
  protected Class<Subscription> getEntityClass() {
    return Subscription.class;
  }

  @Override
  public Subscription findFirstASDSubscriptionByID(String subscriberID) {
    List<Subscription> results = getEntityManager()
            .createQuery(
                    SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_ORDER_BY_FIELD_STATE_STATE_1_STATE_2_STATE_3, Subscription.class)
            .setParameter(ID, subscriberID)
            .setParameter("state1", ActorEntity.State.ACTIVE.toString())
            .setParameter("state2", ActorEntity.State.SUSPENDED.toString())
            .setParameter("state3", ActorEntity.State.DEACTIVATED.toString())
            .getResultList();

    return JPAUtils.getFirstOrNull(results);
  }

  @Override
  public Subscription findActiveSubscriptionByID(String subscriberID) {
    List<Subscription> results = getEntityManager()
            .createQuery(
                    SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_AND_NOT_S_STATE_STATE, Subscription.class)
            .setParameter(ID, subscriberID)
            .setParameter("state", ActorEntity.State.DEACTIVATED)
            .getResultList();

    return JPAUtils.getSingleOrNull(results);
  }

  @Override
  public Subscription findSubscriptionByHref(String href) {
    List<Subscription> results = getEntityManager()
            .createQuery(SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_HREF_HREF, Subscription.class)
            .setParameter("href", href).getResultList();

    return JPAUtils.getSingleOrNull(results);
  }

  @Override
  public void createSubscription(Subscription subscription) {
    getEntityManager().persist(subscription);
  }
}
