package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.wlogger.MongoInitializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;

@QuarkusTest
@QuarkusTestResource(MongoInitializer.class)
@Tag("integration")
public class DeviceHistoryDaoITTest {
/*
    @Inject
    DeviceHistoryDao deviceHistoryDao;

    @Test
    public void testEmptyGet() throws WloggerException {
        List<DeviceHistory> dh =
                deviceHistoryDao.get(
                        Search.builder().withType(DeviceType.LORA).withDeviceIDs(Arrays.asList("1234", "5678")).build(), 0, 100);
        assertThat(dh, is(empty()));
    }

    @Test
    public void testOneGet() throws WloggerException {
        List<DeviceHistory> dh =
                deviceHistoryDao.get(
                        Search.builder().withType(DeviceType.LORA).withDeviceIDs(singletonList("000000000522d8eb")).build(),
                        0,
                        100);
        assertThat(dh, hasSize(2));
    }

  @Test
  public void testFullGet() throws WloggerException {
    List<DeviceHistory> dh =
        deviceHistoryDao.get(Search.builder().withType(DeviceType.LORA).build(), 0, 100);
    assertThat(dh, hasSize(4));
  }

 */
}
