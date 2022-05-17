package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.smp.rest.dto.system.OperatorsDto;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.dao.OperatorDAOLocal;
import com.actility.thingpark.wlogger.entity.Operator;
import com.actility.thingpark.wlogger.exception.SmpException;
import com.actility.thingpark.wlogger.http.SmpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorServiceTest {

  private OperatorDAOLocal daoMock;
  private SmpClient clientMock;
  private OperatorService service;

  private String operatorId = "1";
  private WloggerConfig wloggerConfig = new WloggerConfig() {
    @Override
    public String configurationFolder() {
      return null;
    }

    @Override
    public String version() {
      return null;
    }

    @Override
    public String defaultLanguage() {
      return null;
    }

    @Override
    public boolean passiveRoaming() {
      return false;
    }

    @Override
    public boolean lteMode() {
      return false;
    }

    @Override
    public String csvExportDelimiter() {
      return null;
    }

    @Override
    public String accessCodeSecret() {
      return null;
    }

    @Override
    public boolean adminLogin() {
      return false;
    }

    @Override
    public int adminSessionLifetime() {
      return 120;
    }

    @Override
    public int maxAutomaticDecodedPackets() { return 500; }

    @Override
    public int subscriberViewRoamingInTraffic() {
      return 0;
    }

    @Override
    public int maxInactiveInterval() {
      return 0;
    }
  };

  Operator operator;

  @BeforeEach
  void setUp() throws SmpException {
    daoMock = mock(OperatorDAOLocal.class);
    clientMock = mock(SmpClient.class);
    service = new OperatorService(daoMock, clientMock, wloggerConfig);

    OperatorsDto operatorsDto = new OperatorsDto();
    operatorsDto.withBriefs(new OperatorsDto.Briefs().withBrief(new OperatorsDto.Briefs.Brief("name", "description", "id", "href"))).withMore(false);
    when(this.clientMock.getOperators(operatorId)).thenReturn(operatorsDto);

    operator = new Operator(operatorId,"/href",120);
    when(this.daoMock.findOperatorByID(operatorId)).thenReturn(operator);
  }

  @Test
  void getOperators() throws SmpException {
    OperatorsDto operatorsDto = this.service.getOperators(operatorId);
    assertEquals(false,operatorsDto.isMore());
    assertEquals(1 ,operatorsDto.getBriefs().getBrief().size());
  }

  @Test
  void getOperators_null() throws SmpException {
    assertEquals(null,this.service.getOperators("0"));
  }

  @Test
  void createOperator() {
    assertEquals(operator,this.service.createOperator(operatorId,"href"));
  }

  @Test
  void findOperatorByID() {
    assertEquals(operator, this.service.findOperatorByID(operatorId));
  }
}