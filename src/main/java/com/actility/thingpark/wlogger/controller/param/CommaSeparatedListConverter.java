package com.actility.thingpark.wlogger.controller.param;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;

@Provider
public class CommaSeparatedListConverter implements ParamConverter<CommaSeparatedListString>, ParamConverterProvider {

    private static final char COMMA = ',';

    private static List<String> parseList(String list){
        if(isBlank(list)){
            return Collections.emptyList();
        } else {
            return Arrays.asList(split(list, COMMA));
        }
    }

    /**
     * Parse the supplied value and create an instance of {@code T}.
     *
     * @param value the string value.
     * @return the newly created instance of {@code T}.
     * @throws IllegalArgumentException if the supplied string cannot be
     *                                  parsed or is {@code null}.
     */
    @Override
    public CommaSeparatedListString fromString(String value) {
        return new CommaSeparatedListString(parseList(value));
    }

    /**
     * Convert the supplied value to a String.
     * <p>
     * This method is reserved for future use. Proprietary API extensions may leverage the method.
     * Users should be aware that any such support for the method comes at the expense of producing
     * non-portable code.
     * </p>
     *
     * @param value the value of type {@code T}.
     * @return a String representation of the value.
     * @throws IllegalArgumentException if the supplied object cannot be
     *                                  serialized or is {@code null}.
     */
    @Override
    public String toString(CommaSeparatedListString value) {
        return String.join(String.valueOf(COMMA), value);
    }

    /**
     * Obtain a {@link ParamConverter} that can provide from/to string conversion
     * for an instance of a particular Java type.
     *
     * @param rawType     the raw type of the object to be converted.
     * @param genericType the type of object to be converted. E.g. if an String value
     *                    representing the injected request parameter
     *                    is to be converted into a method parameter, this will be the
     *                    formal type of the method parameter as returned by {@code Class.getGenericParameterTypes}.
     * @param annotations an array of the annotations associated with the convertible
     *                    parameter instance. E.g. if a string value is to be converted into a method parameter,
     *                    this would be the annotations on that parameter as returned by
     *                    {@link Method#getParameterAnnotations}.
     * @return the string converter, otherwise {@code null}.
     */
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if(rawType.equals(CommaSeparatedListString.class)){
            return (ParamConverter<T>) new CommaSeparatedListConverter();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return (!(obj == null || obj.getClass() != this.getClass()));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
