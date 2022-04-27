package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class ElementDeviceHistoExtract implements Element {

    protected String timestampUTC;
    protected String direction;
    protected String asID;

    @JsonProperty("DevAddr")
    protected String devAddr;

    @JsonProperty("DevEUI")
    protected String devEUI;

    @JsonProperty("LrcId")
    protected String lrcId;

    @JsonProperty("MType")
    protected String mType;

    @JsonProperty("MTypeText")
    protected String mTypeText;

    @JsonProperty("payload_hex")
    protected String payloadHex;

    @JsonProperty("payload_decoded")
    protected String payloadDecoded;

    @JsonProperty("DevLAT")
    protected String devLAT;

    @JsonProperty("DevLON")
    protected String devLON;

    @JsonProperty("Late")
    protected String late;

    @JsonProperty("asReportDeliveryID")
    protected String asReportDeliveryID;
    @JsonProperty("asRecipients")
    protected List<Element> asRecipients;
    @JsonProperty("asDeliveryStatus")
    protected String asDeliveryStatus;

    public String getTimestampUTC() {
        return timestampUTC;
    }

    public String getDirection() {
        return direction;
    }

    public String getAsID() {
        return asID;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public String getDevEUI() {
        return devEUI;
    }

    public String getLrcId() {
        return lrcId;
    }

    public String getmType() {
        return mType;
    }

    public String getmTypeText() {
        return mTypeText;
    }

    public String getPayloadHex() {
        return payloadHex;
    }

    public String getPayloadDecoded() {
        return payloadDecoded;
    }

    public String getDevLAT() {
        return devLAT;
    }

    public String getDevLON() {
        return devLON;
    }

    public String getLate() {
        return late;
    }
}
