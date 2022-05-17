package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminDAOBeanTest {

  private AdminDAOBean adminDAOBean;
  private EntityManager entityManagerMock;

  private Class adminClass = Admin.class;

  private Admin admin = new Admin("login","password");

  @BeforeEach
  void setUp() {
    this.entityManagerMock = mock(EntityManager.class);
    this.adminDAOBean = new AdminDAOBean(this.entityManagerMock);

    when(this.entityManagerMock.find(adminClass,1L)).thenReturn(admin);

    when(this.entityManagerMock.createQuery(AdminDAOBean.SELECT_A_FROM_ADMIN_A_WHERE_A_LOGIN_LOGIN,adminClass)).thenReturn(new TypedQuery<Admin>() {
      @Override
      public List getResultList() {
        ArrayList<Admin> list = new ArrayList<Admin>();
        list.add(admin);
        return list;
      }

      @Override
      public Stream<Admin> getResultStream() {
        return (Stream<Admin>) admin;
      }

      @Override
      public Admin getSingleResult() {
        return admin;
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
  void getEntityClass() {
      assertEquals(this.adminDAOBean.getEntityClass(),adminClass);
    }

  @Test
  void findAdmin() {
    assertEquals(this.adminDAOBean.findAdmin(1L),admin);
  }

  @Test
  void findAdminByLogin() {
    assertEquals(this.adminDAOBean.findAdminByLogin("login"),admin);
  }

  @Test
  void createAdmin() {
    doNothing().when(this.entityManagerMock).persist(any(adminClass));
    this.adminDAOBean.createAdmin(admin);
    verify(this.entityManagerMock, times(1)).persist(admin);
  }
}