package com.actility.thingpark.wlogger.utils;

import com.actility.thingpark.wlogger.entity.Admin;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JPAUtilsTest {

  @Test
  void getSingleOrNull() {
    assertEquals((Byte) null,JPAUtils.getSingleOrNull(null));

    assertEquals((Byte) null,JPAUtils.getSingleOrNull(Collections.emptyList()));

    Admin admin = new Admin();
    List<Admin> list = new ArrayList<>();
    list.add(admin);
    assertEquals(admin,JPAUtils.getSingleOrNull(list));

    list.add(admin);
    assertThrows(IllegalStateException.class,() -> JPAUtils.getSingleOrNull(list));
  }

  @Test
  void getFirstOrNull() {
    assertEquals((Byte) null,JPAUtils.getFirstOrNull(null));

    assertEquals((Byte) null,JPAUtils.getFirstOrNull(Collections.emptyList()));

    Admin admin = new Admin();
    List<Admin> list = new ArrayList<>();
    list.add(admin);
    assertEquals(admin,JPAUtils.getFirstOrNull(list));

    list.add(admin);
    assertEquals(admin,JPAUtils.getFirstOrNull(list));
  }
}