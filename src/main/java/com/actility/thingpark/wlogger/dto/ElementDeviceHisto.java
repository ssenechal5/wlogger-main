package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ElementDeviceHisto implements Element {

    protected String uid;
    protected String direction;
    protected String timestamp;
    protected String timestampUTC;
    protected String asID;

    @JsonProperty("DevEUI")
    protected String devEUI;

    @JsonProperty("DevAddr")
    protected String devAddr;

    @JsonProperty("payload_hex")
    protected String payloadHex;

    @JsonProperty("Lrrid")
    protected String lrrid;

    @JsonProperty("LrcId")
    protected String lrcId;

    @JsonProperty("DevLAT")
    protected String devLAT;

    @JsonProperty("DevLON")
    protected String devLON;

    @JsonProperty("MType")
    protected String mType;

    @JsonProperty("MTypeText")
    protected String mTypeText;

    @JsonProperty("hasPayload")
    protected String hasPayload;

    @JsonProperty("payload_decoded")
    protected String payloadDecoded;

    @JsonProperty("Late")
    protected String late;

    @JsonProperty("PayloadSize")
    protected String payloadSize;

    @JsonProperty("asReportDeliveryID")
    protected String asReportDeliveryID;
    @JsonProperty("asRecipients")
    protected String asRecipients;
    @JsonProperty("asDeliveryStatus")
    protected String asDeliveryStatus;

    public String getUid() {
        return uid;
    }

    public String getDirection() {
        return direction;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTimestampUTC() {
        return timestampUTC;
    }

    public String getAsID() {
        return asID;
    }

    public String getDevEUI() {
        return devEUI;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public String getPayloadHex() {
        return payloadHex;
    }

    public String getLrrid() {
        return lrrid;
    }

    public String getLrcId() {
        return lrcId;
    }

    public String getDevLAT() {
        return devLAT;
    }

    public String getDevLON() {
        return devLON;
    }

    public String getmType() {
        return mType;
    }

    public String getmTypeText() {
        return mTypeText;
    }

    public String getHasPayload() {
        return hasPayload;
    }

    public String getPayloadDecoded() {
        return payloadDecoded;
    }

    public String getLate() {
        return late;
    }

    public String getPayloadSize() {
        return payloadSize;
    }
}
