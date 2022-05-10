package com.actility.thingpark.wlogger.controller.input;

import com.actility.thingpark.wlogger.controller.param.CommaSeparatedListString;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Direction;

import javax.ws.rs.QueryParam;

public class SearchInput {
  @QueryParam("last")
  String last;

  @QueryParam("devicesID")
  CommaSeparatedListString deviceIDs;

  @QueryParam("decoder")
  String decoder;

  @QueryParam("subtype")
  String subtype;

  @QueryParam("LRRID")
  String lrrId;

  @QueryParam("LRCID")
  String lrcId;

  @QueryParam("fromDate")
  String fromDate;

  @QueryParam("toDate")
  String toDate;

  @QueryParam("startUid")
  String startUid;

  @QueryParam("endUid")
  String endUid;

  @QueryParam("direction")
  Direction direction;

  @QueryParam("devicesAddr")
  CommaSeparatedListString devADDRs;

  @QueryParam("type")
  DeviceType type;

  @QueryParam("pageIndex")
  Integer pageIndex;

  @QueryParam("ASID")
  String asID;

  @QueryParam("subscriberID")
  String subscriberID;

  public String getLast() {
    return last;
  }

  public CommaSeparatedListString getDeviceIDs() {
    return deviceIDs;
  }

  public String getDecoder() {
    return decoder;
  }

  public String getSubtype() {
    return subtype;
  }

  public String getLRRID() {
    return lrrId;
  }

  public String getLRCID() {
    return lrcId;
  }

  public String getFromDate() {
    return fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public String getStartUid() {
    return startUid;
  }

  public String getEndUid() {
    return endUid;
  }

  public Direction getDirection() {
    return direction;
  }

  public CommaSeparatedListString getDevADDRs() {
    return devADDRs;
  }

  public DeviceType getType() {
    return type;
  }

  public Integer getPageIndex() {
    return pageIndex;
  }

  public String getAsID() {
    return asID;
  }

  public String getSubscriberID() {
    return subscriberID;
  }
}
