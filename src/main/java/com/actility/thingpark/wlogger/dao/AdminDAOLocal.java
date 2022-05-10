package com.actility.thingpark.wlogger.dao;

import com.actility.common.dao.DaoLocal;
import com.actility.thingpark.wlogger.entity.Admin;

public interface AdminDAOLocal extends DaoLocal<Admin> {

  Admin findAdmin(Long uid);

  Admin findAdminByLogin(String login);

  void createAdmin(Admin admin);
}
