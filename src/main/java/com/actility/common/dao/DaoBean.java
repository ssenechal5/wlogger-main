package com.actility.common.dao;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class DaoBean<E> implements DaoLocal<E> {

  public static final String FROM = "FROM ";
  public static final String ORDER_BY = " ORDER BY ";

  @Override
  public void persist(E entity) {
    getEntityManager().persist(entity);
  }

  @Override
  public E findById(Object id) {
    return getEntityManager().find(getEntityClass(), id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<E> findAll() {
    String queryString = FROM + getTableName();
    return getEntityManager().createQuery(queryString).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<E> findAll(String orderedColumn) {
    String queryString = FROM + getTableName() + ORDER_BY + orderedColumn;
    return getEntityManager().createQuery(queryString).getResultList();
  }

  @Override
  public void delete(E entity) {
    getEntityManager().remove(entity);
  }

  @Override
  public void flush() {
    getEntityManager().flush();
  }

  @Override
  public void clear() {
    getEntityManager().clear();
  }

  protected String getTableName() {
    return getEntityClass().getSimpleName();
  }

  protected abstract EntityManager getEntityManager();

  protected abstract Class<E> getEntityClass();

}