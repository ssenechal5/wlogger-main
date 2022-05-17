package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Search;
import com.mongodb.client.*;
import org.bson.conversions.Bson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeviceHistoryDaoTest {

  MongoClient mongoClientMock = mock(MongoClient.class);
  DeviceHistoryDao deviceHistoryDao = new DeviceHistoryDao(mongoClientMock, "wireless");


  private static Stream<Arguments> provideDataArguments() {
    return Stream.of(
            Arguments.of(DeviceType.LORA),
            Arguments.of(DeviceType.LTE));
  }

  @ParameterizedTest(name = "run get => (type :{0})")
  @MethodSource("provideDataArguments")
  void get(DeviceType deviceType) {

    MongoDatabase databaseMock = mock(MongoDatabase.class);

    MongoCollection<DeviceHistory> mongoCollectionMock = mock(MongoCollection.class);

    FindIterable<DeviceHistory> findIterableMock = mock(FindIterable.class);

    MongoCursor<DeviceHistory> mongoCursorMock = mock(MongoCursor.class);

    when(findIterableMock.limit(any(Integer.class))).thenReturn(findIterableMock);
    when(findIterableMock.sort(any(Bson.class))).thenReturn(findIterableMock);
    when(findIterableMock.skip(any(Integer.class))).thenReturn(findIterableMock);
    when(findIterableMock.iterator()).thenReturn(mongoCursorMock);

    when(mongoCursorMock.hasNext()).thenReturn(Boolean.FALSE);
    when(mongoCursorMock.next()).thenReturn(new DeviceHistory());

    when(mongoCollectionMock.find(any(Bson.class))).thenReturn(findIterableMock);

    when(databaseMock.getCollection(DeviceHistoryDao.TABLE_NAME, DeviceHistory.class)).thenReturn(mongoCollectionMock);

    when(mongoClientMock.getDatabase("wireless")).thenReturn(databaseMock);

//    assertThrows(NullPointerException.class, () -> deviceHistoryDao.get(Search.builder().withType(deviceType).build(), 0, 100));

    List<DeviceHistory> dh = deviceHistoryDao.get(Search.builder().withType(deviceType).build(), 0, 100);
    assertEquals(dh.size(),0);
  }
}