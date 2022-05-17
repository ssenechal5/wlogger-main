package com.actility.thingpark.wlogger.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchTest {

    @Test
    void builderDefaults() {
        Search s = Search.builder().build();
        assertEquals(Collections.EMPTY_LIST, s.getDeviceIDs());
        assertEquals(Collections.EMPTY_LIST, s.getDevADDRs());
    }

    @Test
    void builderNullDefault(){
        Search s = Search.builder().
                withDevADDRs((List<String>) null).
                withDeviceIDs((List<String>) null).
                build();
        assertEquals(Collections.EMPTY_LIST, s.getDeviceIDs());
        assertEquals(Collections.EMPTY_LIST, s.getDevADDRs());
    }
}