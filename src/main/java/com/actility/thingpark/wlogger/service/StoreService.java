package com.actility.thingpark.wlogger.service;

import java.io.IOException;

public interface StoreService {

  String getDecoder(String operatorDomain, String locale, boolean lte) throws IOException;

  String getSubtype(String operatorDomain, String locale, boolean type, boolean np) throws IOException;

}
