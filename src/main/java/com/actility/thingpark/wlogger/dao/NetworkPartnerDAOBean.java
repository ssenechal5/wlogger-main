package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.ActorEntity;
import com.actility.thingpark.wlogger.entity.NetworkPartner;
import com.actility.thingpark.wlogger.utils.JPAUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class NetworkPartnerDAOBean extends WloggerDAO<NetworkPartner> implements NetworkPartnerDAOLocal {

  public static final String SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_ID_ID_ORDER_BY_FIELD_STATE_STATE_1_STATE_2_STATE_3 = "select s from NetworkPartner s where s.id = :id order by field(state,:state1,:state2,:state3)";
  public static final String SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_ID_ID_AND_NOT_S_STATE_STATE = "select s from NetworkPartner s where s.id = :id and not s.state = :state";
  public static final String SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_HREF_HREF = "select s from NetworkPartner s where s.href = :href";
  public static final String ID = "id";

  @Inject
  public NetworkPartnerDAOBean(final EntityManager em) {
    super(em);
  }

  @Override
  protected Class<NetworkPartner> getEntityClass() {
    return NetworkPartner.class;
  }

  @Override
  public NetworkPartner findFirstASDSubscriptionByID(String supplierID) {
    List<NetworkPartner> results = getEntityManager()
            .createQuery(
                    SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_ID_ID_ORDER_BY_FIELD_STATE_STATE_1_STATE_2_STATE_3, NetworkPartner.class)
            .setParameter(ID, supplierID)
            .setParameter("state1", ActorEntity.State.ACTIVE.toString())
            .setParameter("state2", ActorEntity.State.SUSPENDED.toString())
            .setParameter("state3", ActorEntity.State.DEACTIVATED.toString())
            .getResultList();

    return JPAUtils.getFirstOrNull(results);
  }

  @Override
  public NetworkPartner findActiveSubscriptionByID(String supplierID) {
    List<NetworkPartner> results = getEntityManager()
            .createQuery(
                    SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_ID_ID_AND_NOT_S_STATE_STATE, NetworkPartner.class)
            .setParameter(ID, supplierID)
            .setParameter("state", ActorEntity.State.DEACTIVATED)
            .getResultList();

    return JPAUtils.getSingleOrNull(results);
  }

  @Override
  public NetworkPartner findSubscriptionByHref(String href) {
    List<NetworkPartner> results = getEntityManager()
            .createQuery(SELECT_S_FROM_NETWORK_PARTNER_S_WHERE_S_HREF_HREF, NetworkPartner.class)
            .setParameter("href", href).getResultList();

    return JPAUtils.getSingleOrNull(results);
  }

  @Override
  public void createSubscription(NetworkPartner networkPartner) {
    getEntityManager().persist(networkPartner);
  }
}
