package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.smp.rest.dto.system.OperatorsDto;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.dao.OperatorDAOLocal;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.exception.SmpException;
import com.actility.thingpark.wlogger.http.SmpClient;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class OperatorService {

  private static final Logger logger = Logger.getLogger(OperatorService.class.getName());
  private OperatorDAOLocal operatorDao;
  private SmpClient client;
  private WloggerConfig wloggerConfig;

  @Inject
  public OperatorService(
      final OperatorDAOLocal operatorDao,
      final SmpClient client,
      final WloggerConfig wloggerConfig) {
    this.operatorDao = operatorDao;
    this.client = client;
    this.wloggerConfig = wloggerConfig;
  }

  @Nullable
  public OperatorsDto getOperators(String id) throws SmpException {
    if (id == null) {
      logger.log(Level.WARNING, "ID value is not correct to get operator from SMP : " + id);
      return null;
    }
    return this.client.getOperators(id);
  }

  public Operator createOperator(@NotNull String id, @NotEmpty String href) {
    Operator operator = new Operator(id, href, this.wloggerConfig.adminSessionLifetime());
    this.operatorDao.createOperator(operator);
    return operator;
  }

  public Operator findOperatorByID(String id) {
    return this.operatorDao.findOperatorByID(id);
  }
}
