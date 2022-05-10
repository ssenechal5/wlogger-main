package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.entity.Admin;
import com.actility.thingpark.wlogger.exception.WloggerException;

public interface AdminService {
  ResponseList create(String login, String password) throws WloggerException;

  ResponseList get(String id) throws WloggerException;

  ResponseList getByLogin(String login) throws WloggerException;

  ResponseList deleteByLogin(String login) throws WloggerException;

  Admin getAdminByLogin(String login);

  String logAdmin(String login, String password) throws WloggerException;

}
