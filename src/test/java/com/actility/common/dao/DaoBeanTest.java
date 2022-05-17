package com.actility.common.dao;

import com.actility.thingpark.wlogger.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DaoBeanTest {

  private EntityManager entityManagerMock;
  private DaoBean daoBean;

  private Class aClass = Admin.class;

  private Admin admin = new Admin("login","password");

  @BeforeEach
  void setUp() {
    this.entityManagerMock = mock(EntityManager.class);
    this.daoBean = new DaoBean() {
      @Override
      protected EntityManager getEntityManager() {
        return entityManagerMock;
      }

      @Override
      protected Class getEntityClass() {
        return aClass;
      }
    };

    doNothing().when(this.entityManagerMock).persist(any(Admin.class));
    doNothing().when(this.entityManagerMock).remove(any(Admin.class));
    doNothing().when(this.entityManagerMock).flush();

    when(this.entityManagerMock.find(aClass,"id")).thenReturn(admin);

    when(this.entityManagerMock.createQuery(any(String.class))).thenReturn(new Query() {
      @Override
      public List getResultList() {
        return null;
      }

      @Override
      public Object getSingleResult() {
        return null;
      }

      @Override
      public int executeUpdate() {
        return 0;
      }

      @Override
      public Query setMaxResults(int i) {
        return null;
      }

      @Override
      public int getMaxResults() {
        return 0;
      }

      @Override
      public Query setFirstResult(int i) {
        return null;
      }

      @Override
      public int getFirstResult() {
        return 0;
      }

      @Override
      public Query setHint(String s, Object o) {
        return null;
      }

      @Override
      public Map<String, Object> getHints() {
        return null;
      }

      @Override
      public <T> Query setParameter(Parameter<T> parameter, T t) {
        return null;
      }

      @Override
      public Query setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
        return null;
      }

      @Override
      public Query setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
        return null;
      }

      @Override
      public Query setParameter(String s, Object o) {
        return null;
      }

      @Override
      public Query setParameter(String s, Calendar calendar, TemporalType temporalType) {
        return null;
      }

      @Override
      public Query setParameter(String s, Date date, TemporalType temporalType) {
        return null;
      }

      @Override
      public Query setParameter(int i, Object o) {
        return null;
      }

      @Override
      public Query setParameter(int i, Calendar calendar, TemporalType temporalType) {
        return null;
      }

      @Override
      public Query setParameter(int i, Date date, TemporalType temporalType) {
        return null;
      }

      @Override
      public Set<Parameter<?>> getParameters() {
        return null;
      }

      @Override
      public Parameter<?> getParameter(String s) {
        return null;
      }

      @Override
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
      public Query setFlushMode(FlushModeType flushModeType) {
        return null;
      }

      @Override
      public FlushModeType getFlushMode() {
        return null;
      }

      @Override
      public Query setLockMode(LockModeType lockModeType) {
        return null;
      }

      @Override
      public LockModeType getLockMode() {
        return null;
      }

      @Override
      public <T> T unwrap(Class<T> aClass) {
        return null;
      }
    });

  }

  @Test
  void persist() {
    this.daoBean.persist(admin);
    verify(this.entityManagerMock, times(1)).persist(admin);
  }

  @Test
  void findById() {
    assertEquals(this.daoBean.findById("id"),admin);
  }

  @Test
  void findAll() {
    assertEquals(null,this.daoBean.findAll());
    verify(this.entityManagerMock, times(1)).createQuery((any(String.class)));
  }

  @Test
  void testFindAll() {
    assertEquals(null,this.daoBean.findAll("id"));
    verify(this.entityManagerMock, times(1)).createQuery((any(String.class)));
  }

  @Test
  void delete() {
    this.daoBean.delete(admin);
    verify(this.entityManagerMock, times(1)).remove(admin);
  }

  @Test
  void flush() {
    this.daoBean.flush();
    verify(this.entityManagerMock, times(1)).flush();
  }

  @Test
  void clear() {
    this.daoBean.clear();
    verify(this.entityManagerMock, times(1)).clear();
  }

  @Test
  void getTableName() {
    assertEquals(this.daoBean.getTableName(),aClass.getSimpleName());
  }

  @Test
  void getEntityManager() {
    assertEquals(this.daoBean.getEntityManager(),this.entityManagerMock);
  }

  @Test
  void getEntityClass() {
    assertEquals(this.daoBean.getEntityClass(),aClass);
  }
}