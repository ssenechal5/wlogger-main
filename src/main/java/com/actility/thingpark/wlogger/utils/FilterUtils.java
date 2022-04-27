package com.actility.thingpark.wlogger.utils;

import com.actility.thingpark.wlogger.model.DeviceType;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class FilterUtils {

    private static final String STAR = "*";
    private static final String ESCAPED_ASTERISK = Pattern.quote(STAR);
    public static final String POINT_STAR = ".*";

    public static Bson beginWithOrEqual(final String fieldName, final String q, final HashMap<String,Boolean> map){
        if (map.get(q)) {
            return Filters.eq(fieldName, q);
        } else {
            return beginWith(fieldName, q);
        }
    }

    /**
     * Creates a filter that matches all documents containing a field that begin with the query.
     * The query is a simple pattern containing eventually * chars.
     *
     * @param fieldName the field name
     * @param q         the filter query
     * @return the filter
     * @mongodb.driver.manual reference/operator/query/elemMatch $elemMatch
     */
    public static Bson beginWith(final String fieldName, final String q){
        Pattern p = Pattern.compile("^" + parseSimplePattern(q));
        return Filters.regex(fieldName, p);
    }

    /**
     * Convert a simple pattern containing eventually * chars to
     * a string compilable with Pattern.compile. Any char expect *
     * are escaped. * are replaced with .* pattern.
     *
     * @param q simple pattern
     * @return escaped pattern
     */
    public static String parseSimplePattern(final String q){
        return Arrays.stream(q.split(ESCAPED_ASTERISK)).
                map(Pattern::quote).
                collect(Collectors.joining(POINT_STAR));
    }

    /**
     * Analyse parameters
     *
     * @param params parameters
     * @param deviceType deviceType
     * @param isDevEUI isDevEUI
     * @return Map
     */
    public static HashMap<String,Boolean> cleanSearchParams(List<String> params, DeviceType deviceType, boolean isDevEUI) {
        int size = 16;
        String regex = "^[0-9A-F]+$";
        String regexStar = "^[0-9A-F/*]+$";
        boolean testSize = true;
        if (isDevEUI) {
            switch (deviceType) {
                case LORA:
                    size = 16;
                    break;
                case LTE:
                    size = 14;
                    regex = "^[0-9]+$";
                    regexStar = "^[0-9/*]+$";
                    break;
                default:
                      break;
            }
        } else { // devAddr
            switch (deviceType) {
                case LORA:
                    size = 8;
                    break;
                case LTE:
                    size = 15;
                    regex = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
                    regexStar = "^[0-9/*.]+$";
                    testSize = false;
                    break;
                default:
                    break;
            }
        }
        HashMap<String,Boolean> map = new HashMap<String,Boolean>();
        for (String param : params) {
            param = param.toUpperCase();
            boolean isSize = (param.length() == size);
            boolean tooLong = (param.length() > size);
            boolean isHex = param.matches(regex);
            boolean isHexStar = param.matches(regexStar);
            if (testSize) {
                if (!tooLong) {
                    if (isSize) {
                        if (isHex) {
                            map.put(param, true); // exact match
                        } else if (isHexStar) {
                            map.put(param, false); // not exact match
                        } // else ignore
                    } else {
                        if (isHexStar) {
                            map.put(param, false); // not exact match
                        } // else ignore
                    }
                } // else ignore because too long
            } else {
                if (!tooLong) {
                    if (isHex) {
                        map.put(param, true); // exact match
                    } else if (isHexStar) {
                        map.put(param, false); // not exact match
                    } // else ignore
                } // else ignore because too long
            }
        }
        return map;
    }
}
