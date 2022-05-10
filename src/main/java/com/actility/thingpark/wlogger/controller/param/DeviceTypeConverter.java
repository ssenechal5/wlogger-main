package com.actility.thingpark.wlogger.controller.param;

import com.actility.thingpark.wlogger.model.DeviceType;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class DeviceTypeConverter implements ParamConverter<DeviceType>, ParamConverterProvider {
  @Override
  public DeviceType fromString(String value) {
    return DeviceType.parseValue(Integer.parseInt(value));
  }

  @Override
  public String toString(DeviceType value) {
    return String.valueOf(value.getValue());
  }

  @Override
  public <T> ParamConverter<T> getConverter(
      Class<T> rawType, Type genericType, Annotation[] annotations) {
    if (rawType.equals(DeviceType.class)) {
      return (ParamConverter<T>) new DeviceTypeConverter();
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
