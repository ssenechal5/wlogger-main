package com.actility.thingpark.wlogger.engine;

import com.actility.thingpark.wlogger.engine.model.DecodeBatchInputItem;
import com.actility.thingpark.wlogger.engine.model.DecodeBatchOutputItem;
import com.actility.thingpark.wlogger.engine.model.ErrorInfo;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@DefaultBean
public class EngineClientImpl implements EngineClient {

  private static final Logger logger = Logger.getLogger(EngineClientImpl.class.getName());
  public static final String ENGINE_ERROR_HTTP = "Engine Error : HTTP : ";
  public static final String ENGINE_ERROR = "Engine Error";
  public static final String PAYLOAD_CANNOT_BE_DECODED_TPX_IO_T_FLOW_ENGINE_IS_NOT_REACHABLE = "Payload cannot be decoded: TPX IoT Flow Engine is not reachable";
  public static final String CODE = "-1";

  private final IotFlowApi iotFlowApi;

  @Inject
  public EngineClientImpl(@RestClient final IotFlowApi iotFlowApi) {
    this.iotFlowApi = iotFlowApi;
  }

  @Override
  public List<DecodeBatchOutputItem> decodeBatch(List<DecodeBatchInputItem> body, String subscriberId, String realmId)
      throws EngineException {
    try {
      return this.iotFlowApi.decodeBatch(body, subscriberId, realmId);
    } catch (WebApplicationException e) {
      logger.severe(ENGINE_ERROR_HTTP + e.getMessage());
      ErrorInfo errorInfo = e.getResponse().readEntity(ErrorInfo.class);
      throw new EngineException(ENGINE_ERROR, errorInfo, e);
    } catch (Exception e) {
      logger.severe(ENGINE_ERROR_HTTP + e.getMessage());
      ErrorInfo error = ErrorInfo.builder().code(CODE).message(PAYLOAD_CANNOT_BE_DECODED_TPX_IO_T_FLOW_ENGINE_IS_NOT_REACHABLE).build();
      throw new EngineException(ENGINE_ERROR,error,e);
    }
  }
}
