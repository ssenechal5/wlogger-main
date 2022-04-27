package com.actility.thingpark.wlogger.utils;

import javax.ws.rs.core.MediaType;
import java.util.Iterator;
import java.util.List;

public final class ResponseUtils {

  public static final MediaType getAcceptMediaType(List<MediaType> accept) {
    Iterator<MediaType> it = accept.iterator();
    while (it.hasNext()) {
      MediaType mt = it.next();
      if (MediaType.APPLICATION_XML_TYPE.getType().equals(mt.getType())
              && MediaType.APPLICATION_XML_TYPE.getSubtype().equals(mt.getSubtype())) {
        return MediaType.APPLICATION_XML_TYPE;
      }
    }
    return MediaType.APPLICATION_JSON_TYPE;
  }
}
