package com.actility.thingpark.wlogger.engine;

import com.actility.thingpark.wlogger.engine.model.DecodeBatchInputItem;
import com.actility.thingpark.wlogger.engine.model.DecodeBatchOutputItem;

import java.util.List;

public interface EngineClient {
  List<DecodeBatchOutputItem> decodeBatch(List<DecodeBatchInputItem> body, String subscriberId, String realmId) throws EngineException;
}
