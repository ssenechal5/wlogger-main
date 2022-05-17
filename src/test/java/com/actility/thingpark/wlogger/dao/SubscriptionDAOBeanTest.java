package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.entity.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionDAOBeanTest {

  private SubscriptionDAOBean subscriptionDAOBean;
  private EntityManager entityManagerMock;

  private Class subscriptionClass = Subscription.class;

  private Subscription subscription = new Subscription(
          new Operator("id", "href", 120), "id", "ext", "href", Subscription.Type.PRODUCTION);

  @BeforeEach
  void setUp() {
    this.entityManagerMock = mock(EntityManager.class);
    this.subscriptionDAOBean = new SubscriptionDAOBean(this.entityManagerMock);

    when(this.entityManagerMock.find(subscriptionClass,1L)).thenReturn(subscription);

    TypedQuery<Subscription> query = new TypedQuery<Subscription>() {
      @Override
      public List getResultList() {
        ArrayList<Subscription> list = new ArrayList<Subscription>();
        list.add(subscription);
        return list;
      }

      @Override
      public Stream<Subscription> getResultStream() {
        return (Stream<Subscription>) subscription;
      }

      @Override
      public Subscription getSingleResult() {
        return subscription;
      }

      @Override
      public int executeUpdate() {
        return 0;
      }

      @Override
      public TypedQuery setMaxResults(int i) {
        return this;
      }

      @Override
      public int getMaxResults() {
        return 0;
      }

      @Override
      public TypedQuery setFirstResult(int i) {
        return this;
      }

      @Override
      public int getFirstResult() {
        return 0;
      }

      @Override
      public TypedQuery setHint(String s, Object o) {
        return this;
      }

      @Override
      public Map<String, Object> getHints() {
        return null;
      }

      @Override
      public TypedQuery setParameter(String s, Object o) {
        return this;
      }

      @Override
      public TypedQuery setParameter(String s, Calendar calendar, TemporalType temporalType) {
        return this;
      }

      @Override
      public TypedQuery setParameter(String s, Date date, TemporalType temporalType) {
        return this;
      }

      @Override
      public TypedQuery setParameter(int i, Object o) {
        return null;
      }

      @Override
      public TypedQuery setParameter(int i, Calendar calendar, TemporalType temporalType) {
        return this;
      }

      @Override
      public TypedQuery setParameter(int i, Date date, TemporalType temporalType) {
        return this;
      }

      @Override
      public Set<Parameter<?>> getParameters() {
        return null;
      }

      @Override
      public Parameter<?> getParameter(String s) {
        return null;
      }

      public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
        return null;
      }

      @Override
      public Parameter<?> getParameter(int i) {
        return null;
      }

      @Override
      public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
        return null;
      }

      @Override
      public boolean isBound(Parameter<?> parameter) {
        return false;
      }

      @Override
      public <T> T getParameterValue(Parameter<T> parameter) {
        return null;
      }

      @Override
      public Object getParameterValue(String s) {
        return null;
      }

      @Override
      public Object getParameterValue(int i) {
        return null;
      }

      @Override
      public TypedQuery setFlushMode(FlushModeType flushModeType) {
        return this;
      }

      @Override
      public FlushModeType getFlushMode() {
        return null;
      }

      @Override
      public TypedQuery setLockMode(LockModeType lockModeType) {
        return this;
      }

      @Override
      public LockModeType getLockMode() {
        return null;
      }

      @Override
      public <T> T unwrap(Class<T> aClass) {
        return null;
      }

      @Override
      public TypedQuery setParameter(Parameter parameter, Date date, TemporalType temporalType) {
        return this;
      }

      @Override
      public TypedQuery setParameter(Parameter parameter, Calendar calendar, TemporalType temporalType) {
        return this;
      }

      @Override
      public TypedQuery setParameter(Parameter parameter, Object o) {
        return this;
      }
    };

    when(this.entityManagerMock.createQuery(SubscriptionDAOBean.SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_HREF_HREF,subscriptionClass)).thenReturn(query);
    when(this.entityManagerMock.createQuery(SubscriptionDAOBean.SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_AND_NOT_S_STATE_STATE,subscriptionClass)).thenReturn(query);
    when(this.entityManagerMock.createQuery(SubscriptionDAOBean.SELECT_S_FROM_SUBSCRIPTION_S_WHERE_S_ID_ID_ORDER_BY_FIELD_STATE_STATE_1_STATE_2_STATE_3,subscriptionClass)).thenReturn(query);

  }

  @Test
  void getEntityClass() {assertEquals(this.subscriptionDAOBean.getEntityClass(),subscriptionClass);}

  @Test
  void findFirstASDSubscriptionByID() {
    assertEquals(this.subscriptionDAOBean.findFirstASDSubscriptionByID("id"),subscription);
  }

  @Test
  void findActiveSubscriptionByID() {
    assertEquals(this.subscriptionDAOBean.findActiveSubscriptionByID("id"),subscription);
  }

  @Test
  void findSubscriptionByHref() {
    assertEquals(this.subscriptionDAOBean.findSubscriptionByHref("href"),subscription);
  }

  @Test
  void createSubscription() {
    doNothing().when(this.entityManagerMock).persist(any(subscriptionClass));
    this.subscriptionDAOBean.createSubscription(subscription);
    verify(this.entityManagerMock, times(1)).persist(subscription);
  }
}