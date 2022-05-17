package com.actility.thingpark.wlogger.engine;

import com.actility.thingpark.wlogger.engine.model.Record;
import com.actility.thingpark.wlogger.engine.model.*;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EngineClientImplTest {

  @Test
  void decodeBatch() throws EngineException, ConnectException {
    IotFlowApi iotFlowApiMock = mock(IotFlowApi.class);
    EngineClientImpl engine = new EngineClientImpl(iotFlowApiMock);

    List<DecodeBatchInputItem> decodeBatchInputItemList = new ArrayList<>();
    Direction direction = Direction.DOWNLINK;
    Raw raw = Raw.builder().binary("binary").build();
    Map<String, Point> map = new HashMap<>();
    List<Record> records = new ArrayList<>();
    List<Double> coordinates = new ArrayList<>();
    coordinates.add(125d);
    coordinates.add(123d);
    records.add(Record.builder().coordinates(coordinates).build());
    map.put("0",Point.builder().unitId("unit").type("type").records(records).build());
    ThingInfo thingInfo = ThingInfo.builder().moduleId("moduleId").producerId("producerId").version("version").build();
    ThingDescription thingDescription = ThingDescription.builder().model(thingInfo).application(thingInfo).build();
    DecodeInput decodeInput = DecodeInput.builder().direction(direction).meta(null).raw(raw).sourceTime(null).thing(thingDescription).build();
    DecodeBatchInputItem decodeBatchInputItem = DecodeBatchInputItem.builder().input(decodeInput).id("id").build();
    decodeBatchInputItemList.add(decodeBatchInputItem);

    List<DecodeBatchOutputItem> decodeBatchOutputItemList = new ArrayList<>();
    ErrorInfo error = ErrorInfo.builder().code("code").message("message").data(null).build();
    Thing thing = Thing.builder().application(thingInfo).model(thingInfo).points(map).build();
    CodecOutput codecOutput = CodecOutput.builder().direction(direction).message(null).meta(null).raw(raw).sourceTime(null).thing(thing).build();
    DecodeBatchOutputItem decodeBatchOutputItem = DecodeBatchOutputItem.builder().id("id").error(error).output(codecOutput).build();
    decodeBatchOutputItemList.add(decodeBatchOutputItem);

    when(iotFlowApiMock.decodeBatch(decodeBatchInputItemList, null, null)).thenReturn(decodeBatchOutputItemList);

    List<DecodeBatchOutputItem> response = engine.decodeBatch(decodeBatchInputItemList, null, null);

    assertEquals(1,response.size());
    assertEquals("id",response.get(0).id);

    assertEquals("code",response.get(0).error.code);
    assertEquals("message",response.get(0).error.message);
    assertEquals(null,response.get(0).error.data);

    assertEquals("downlink",response.get(0).output.direction.getValue());

    assertEquals("binary",response.get(0).output.raw.binary);

    assertEquals("moduleId",response.get(0).output.thing.application.moduleId);
    assertEquals("producerId",response.get(0).output.thing.application.producerId);
    assertEquals("version",response.get(0).output.thing.application.version);
    assertEquals("moduleId",response.get(0).output.thing.model.moduleId);
    assertEquals("producerId",response.get(0).output.thing.model.producerId);
    assertEquals("version",response.get(0).output.thing.model.version);

    assertEquals(1,response.get(0).output.thing.points.size());
    assertEquals(1,response.get(0).output.thing.points.get("0").records.size());
    assertEquals(2,response.get(0).output.thing.points.get("0").records.get(0).coordinates.size());
  }

  @Test
  void decodeBatch_null() throws EngineException, ConnectException {
    IotFlowApi iotFlowApiMock = mock(IotFlowApi.class);
    EngineClientImpl engine = new EngineClientImpl(iotFlowApiMock);

    when(iotFlowApiMock.decodeBatch(null, null, null)).thenReturn(null);

    List<DecodeBatchOutputItem> response = engine.decodeBatch(null, null, null);
    assertEquals(null,response);
  }
}