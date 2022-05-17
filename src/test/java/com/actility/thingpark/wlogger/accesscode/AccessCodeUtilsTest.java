package com.actility.thingpark.wlogger.accesscode;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessCodeUtilsTest {

  @Test
  void stringify_empty() {
    assertEquals("", AccessCodeUtils.stringify(new HashMap<>()));
  }

  @Test
  void stringify_multiple_entries() {
    TreeMap<String, String> map = new TreeMap<>();
    map.put("test", "test1");
    map.put("test2", "test3");
    assertEquals("test=test1;test2=test3", AccessCodeUtils.stringify(map));
  }

  @Test
  void stringify_single_entry() {
    TreeMap<String, String> map = new TreeMap<>();
    map.put("test", "test1");
    assertEquals("test=test1", AccessCodeUtils.stringify(map));
  }

}