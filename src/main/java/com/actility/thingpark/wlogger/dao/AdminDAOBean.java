package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.entity.Admin;
import com.actility.thingpark.wlogger.utils.JPAUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;

@Singleton
public class AdminDAOBean extends WloggerDAO<Admin> implements AdminDAOLocal {

  public static final String SELECT_A_FROM_ADMIN_A_WHERE_A_LOGIN_LOGIN = "select a from Admin a where a.login = :login";

  @Inject
  public AdminDAOBean(final EntityManager em) {
    super(em);
  }

  @Override
  protected Class<Admin> getEntityClass() {
    return Admin.class;
  }

  @Override
  public Admin findAdmin(Long uid) {
    return getEntityManager().find(Admin.class, uid);
  }

  @Override
  public Admin findAdminByLogin(String login) {
    List<Admin> results = getEntityManager().createQuery(SELECT_A_FROM_ADMIN_A_WHERE_A_LOGIN_LOGIN, Admin.class)
            .setParameter("login", login).getResultList();
    return JPAUtils.getSingleOrNull(results);
  }

  @Override
  public void createAdmin(Admin admin) {
    getEntityManager().persist(admin);
  }

}
