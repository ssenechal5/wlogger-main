package com.actility.thingpark.wlogger.controller.param;

import com.actility.thingpark.wlogger.model.Direction;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class DirectionConverter implements ParamConverter<Direction>, ParamConverterProvider{
  @Override
  public Direction fromString(String value) {
    return Direction.parseValue(Integer.parseInt(value));
  }

  @Override
  public String toString(Direction value) {
    return Integer.toString(value.getValue());
  }

  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    if(rawType.equals(Direction.class)) {
      return (ParamConverter<T>) new DirectionConverter();
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    return  (!(obj == null || obj.getClass() != this.getClass()));
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
