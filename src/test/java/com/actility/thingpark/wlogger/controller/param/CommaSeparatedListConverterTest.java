package com.actility.thingpark.wlogger.controller.param;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class CommaSeparatedListConverterTest {

    @Test
    void fromString() {
        CommaSeparatedListConverter c = new CommaSeparatedListConverter();
        assertEquals(new CommaSeparatedListString(singletonList("foo")), c.fromString("foo"));
        assertEquals(new CommaSeparatedListString(asList("foo", "bar", "baz")), c.fromString("foo,bar,baz"));
        assertEquals(new CommaSeparatedListString(singletonList("foo")), c.fromString("foo,"));
        assertEquals(new CommaSeparatedListString(singletonList("foo")), c.fromString(",foo"));
        assertEquals(new CommaSeparatedListString(asList("foo", "bar", "baz")), c.fromString("foo,,bar,baz"));
        assertEquals(new CommaSeparatedListString(emptyList()), c.fromString(""));

    }

    @Test
    void testToString() {
        CommaSeparatedListConverter c = new CommaSeparatedListConverter();
        assertEquals("foo,bar,baz", c.toString(new CommaSeparatedListString(asList("foo", "bar", "baz"))));
    }

    @Test
    void getConverter() {
        CommaSeparatedListConverter c = new CommaSeparatedListConverter();
        assertNull(c.getConverter(String.class, null, null));
        assertEquals(c, c.getConverter(CommaSeparatedListString.class, null, null));
    }

    @Test
    void equalsTest(){
        CommaSeparatedListConverter c1 = new CommaSeparatedListConverter();
        CommaSeparatedListConverter c2 = new CommaSeparatedListConverter();
        assertEquals(c1, c2);
        assertNotEquals(null, c1);
        assertNotEquals("foo", c1);
    }
}