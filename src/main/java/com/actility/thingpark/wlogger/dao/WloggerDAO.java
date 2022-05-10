package com.actility.thingpark.wlogger.dao;

import com.actility.common.dao.DaoBean;

import javax.persistence.EntityManager;

public abstract class WloggerDAO<E> extends DaoBean<E> {

  private final EntityManager em;

  protected WloggerDAO(final EntityManager em) {
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

}
