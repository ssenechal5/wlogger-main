package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OperatorDAOBeanTest {

  private OperatorDAOBean operatorDAOBean;
  private EntityManager entityManagerMock;

  private Class operatorClass = Operator.class;

  private Operator operator = new Operator("id","href",120);

  @BeforeEach
  void setUp() {
    this.entityManagerMock = mock(EntityManager.class);
    this.operatorDAOBean = new OperatorDAOBean(this.entityManagerMock);

    when(this.entityManagerMock.find(operatorClass,1L)).thenReturn(operator);

    when(this.entityManagerMock.createQuery(OperatorDAOBean.SELECT_O_FROM_OPERATOR_O_WHERE_O_ID_ID,operatorClass)).thenReturn(new TypedQuery<Operator>() {
      @Override
      public List getResultList() {
        ArrayList<Operator> list = new ArrayList<Operator>();
        list.add(operator);
        return list;
      }

      @Override
      public Stream<Operator> getResultStream() {
        return (Stream<Operator>) operator;
      }

      @Override
      public Operator getSingleResult() {
        return operator;
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
    });
  }

  @Test
  void getEntityClass() {assertEquals(this.operatorDAOBean.getEntityClass(),operatorClass);}

  @Test
  void findOperatorByID() {
    assertEquals(this.operatorDAOBean.findOperatorByID("id"),operator);
  }

  @Test
  void createOperator() {
    doNothing().when(this.entityManagerMock).persist(any(operatorClass));
    this.operatorDAOBean.createOperator(operator);
    verify(this.entityManagerMock, times(1)).persist(operator);
  }
}