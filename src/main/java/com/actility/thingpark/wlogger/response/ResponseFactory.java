package com.actility.thingpark.wlogger.response;

import com.actility.thingpark.wlogger.dto.ResponseData;
import com.actility.thingpark.wlogger.dto.ResponseList;
import com.actility.thingpark.wlogger.dto.ResponsePaginatedList;

public class ResponseFactory {

  public static final String DEUX_CENT = "200";

  public static ResponseData createSuccessResponseData() {
    return new ResponseData().withStatusCode(DEUX_CENT).withSuccess(Boolean.TRUE);
  }

  public static ResponsePaginatedList createSuccessResponseDatas() {
    return new ResponsePaginatedList().withStatusCode(DEUX_CENT).withSuccess(Boolean.TRUE);
  }

  public static ResponseList createSuccessResponse() {
    return new ResponseList().withStatusCode(DEUX_CENT).withSuccess(Boolean.TRUE);
  }

}
