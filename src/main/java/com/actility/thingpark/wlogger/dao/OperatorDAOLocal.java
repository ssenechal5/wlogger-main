package com.actility.thingpark.wlogger.dao;

import com.actility.common.dao.DaoLocal;
import com.actility.thingpark.wlogger.entity.Operator;

public interface OperatorDAOLocal extends DaoLocal<Operator> {

  Operator findOperatorByID(String id);

  void createOperator(Operator operator);
}
