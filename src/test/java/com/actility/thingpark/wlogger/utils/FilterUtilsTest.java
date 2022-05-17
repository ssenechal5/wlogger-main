package com.actility.thingpark.wlogger.utils;

import com.actility.thingpark.wlogger.model.DeviceType;
import org.bson.BsonDocument;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.BsonDocument.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;


class FilterUtilsTest {

    @Test
    void beginWith() {
        assertEquals(
                parse("{foo : {$regex : \"^\\\\Qfoobar\\\\E\", $options : \"\"}}"),
                FilterUtils.beginWith("foo", "foobar").
                        toBsonDocument(BsonDocument.class, getDefaultCodecRegistry()));
    }

    @Test
    void checkCleanSearchParamsLoraDevAddr() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("058c8B6d");
        params.add("12aB");
        params.add("12*aB");
        params.add("foobar");
        params.add("058c8B6dd");
        HashMap<String, Boolean> map = FilterUtils.cleanSearchParams(params, DeviceType.LORA, false);
        assertEquals(3,map.size());
        assertEquals(true,map.get("058C8B6D"));
        assertEquals(false,map.get("12AB"));
        assertEquals(false,map.get("12*AB"));
    }

    @Test
    void checkCleanSearchParamsLTEDevAddr() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("1.2.3.4");
        params.add("1.2");
        params.add("1.2.*.3");
        params.add("foobar");
        params.add("058c8B6dd");
        params.add("111.222.333.444");
        params.add("111.222.333.4444");
        HashMap<String, Boolean> map = FilterUtils.cleanSearchParams(params, DeviceType.LTE, false);
        assertEquals(4,map.size());
        assertEquals(true,map.get("1.2.3.4"));
        assertEquals(false,map.get("1.2"));
        assertEquals(false,map.get("1.2.*.3"));
        assertEquals(true,map.get("111.222.333.444"));
    }

    @Test
    void checkCleanSearchParamsLoraDevEUI() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("20635f0106000324");
        params.add("12aB");
        params.add("12*aB");
        params.add("foobar");
        params.add("20635f01060003244");
        HashMap<String, Boolean> map = FilterUtils.cleanSearchParams(params, DeviceType.LORA, true);
        assertEquals(3,map.size());
        assertEquals(true,map.get("20635F0106000324"));
        assertEquals(false,map.get("12AB"));
        assertEquals(false,map.get("12*AB"));
    }

    @Test
    void checkCleanSearchParamsLTEDevEUI() {
        ArrayList<String> params = new ArrayList<String>();
        params.add("12348415214861");
        params.add("4587");
        params.add("45*87");
        params.add("foobar");
        params.add("1234841521486c");
        params.add("45a87");
        params.add("20635f01060003244");
        HashMap<String, Boolean> map = FilterUtils.cleanSearchParams(params, DeviceType.LTE, true);
        assertEquals(3,map.size());
        assertEquals(true,map.get("12348415214861"));
        assertEquals(false,map.get("4587"));
        assertEquals(false,map.get("45*87"));
    }

    private static class pastPatternTestCase {
        public final String name;
        public final String query;
        public final String expected;

        public pastPatternTestCase(String name, String query, String expected) {
            this.name = name;
            this.query = query;
            this.expected = expected;
        }
    }

    @TestFactory
    Stream<DynamicTest> parsePattern() {
        List<pastPatternTestCase> cases = Arrays.asList(
                new pastPatternTestCase("nothing to escape", "foobar", "\\Qfoobar\\E"),
                new pastPatternTestCase("don't escape *", "foo*bar", "\\Qfoo\\E.*\\Qbar\\E")
        );

        return cases.stream().
                map(c -> DynamicTest.
                        dynamicTest(c.name, () -> assertEquals(c.expected, FilterUtils.parseSimplePattern(c.query).toString()))
                );
    }
}