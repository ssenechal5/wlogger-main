package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.utils.JPAUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class OperatorDAOBean extends WloggerDAO<Operator> implements OperatorDAOLocal {

  public static final String SELECT_O_FROM_OPERATOR_O_WHERE_O_ID_ID = "select o from Operator o where o.id = :id";

  @Inject
  public OperatorDAOBean(final EntityManager em) {
    super(em);
  }

  @Override
  protected Class<Operator> getEntityClass() {
    return Operator.class;
  }

  @Override
  public Operator findOperatorByID(String id) {
    List<Operator> results = getEntityManager().createQuery(SELECT_O_FROM_OPERATOR_O_WHERE_O_ID_ID, Operator.class)
            .setParameter("id", id).getResultList();
    return JPAUtils.getSingleOrNull(results);
  }

  public void createOperator(Operator operator) {
    getEntityManager().persist(operator);
  }

}
