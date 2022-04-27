package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElementDeviceHistoryLte extends ElementDeviceHisto {

  public final String lteIPPacketSize;

  @JsonProperty("IPV4Decoded")
  public final String ipv4Decoded;

  public final String ltePacketProtocol;
  public final String lteCause;
  public final String lteEBI;
  public final String lteAPN;
  public final String lteMSISDN;
  public final String lteIMSI;
  public final String lteRAT;
  public final String lteCELLID;
  public final String lteCELLTAC;
  public final String lteSERVNET;
  public final String lteMCCMNC;
  public final String lteIMEI;
  public final String mfSzU;
  public final String mfSzD;
  public final String mfDur;

  private ElementDeviceHistoryLte(Builder builder) {
    super();
    this.uid = builder.uid;
    this.direction = builder.direction;
    this.timestamp = builder.timestamp;
    this.timestampUTC = builder.timestampUTC;
    this.devEUI = builder.devEUI;
    this.devAddr = builder.devAddr;
    this.payloadHex = builder.payloadHex;
    this.lrrid = builder.lrrid;
    this.lrcId = builder.lrcId;
    this.devLAT = builder.devLAT;
    this.devLON = builder.devLON;
    this.mType = builder.mType;
    this.mTypeText = builder.mTypeText;
    this.hasPayload = builder.hasPayload;
    this.payloadDecoded = builder.payloadDecoded;
    this.late = builder.late;
    this.payloadSize = builder.payloadSize;
    this.lteIPPacketSize = builder.lteIPPacketSize;
    this.ipv4Decoded = builder.ipv4Decoded;
    this.ltePacketProtocol = builder.ltePacketProtocol;
    this.lteCause = builder.lteCause;
    this.lteEBI = builder.lteEBI;
    this.lteAPN = builder.lteAPN;
    this.lteMSISDN = builder.lteMSISDN;
    this.lteIMSI = builder.lteIMSI;
    this.lteRAT = builder.lteRAT;
    this.lteCELLID = builder.lteCELLID;
    this.lteCELLTAC = builder.lteCELLTAC;
    this.lteSERVNET = builder.lteSERVNET;
    this.lteMCCMNC = builder.lteMCCMNC;
    this.lteIMEI = builder.lteIMEI;
    this.asID = builder.asID;
    this.mfSzU = builder.mfSzU;
    this.mfSzD = builder.mfSzD;
    this.mfDur = builder.mfDur;
    this.asReportDeliveryID = builder.asReportDeliveryID;
    this.asRecipients = builder.asRecipients;
    this.asDeliveryStatus = builder.asDeliveryStatus;
  }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
    private String uid;
    private String direction;
    private String timestamp;
    private String timestampUTC;
    private String devEUI;
    private String devAddr;
    private String payloadHex;
    private String lrrid;
    private String lrcId;
    private String devLAT;
    private String devLON;
    private String mType;
    private String mTypeText;
    private String hasPayload;
    private String payloadDecoded;
    private String late;
    private String payloadSize;
    private String lteIPPacketSize;
    private String ipv4Decoded;
    private String ltePacketProtocol;
    private String lteCause;
    private String lteEBI;
    private String lteAPN;
    private String lteMSISDN;
    private String lteIMSI;
    private String lteRAT;
    private String lteCELLID;
    private String lteCELLTAC;
    private String lteSERVNET;
    private String lteMCCMNC;
    private String lteIMEI;
    private String asID;
    private String mfSzU;
    private String mfSzD;
    private String mfDur;
    private String asReportDeliveryID;
    private String asRecipients;
    private String asDeliveryStatus;

    private Builder() {}

    public Builder uid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder direction(String direction) {
      this.direction = direction;
      return this;
    }

    public Builder timestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder timestampUTC(String timestampUTC) {
      this.timestampUTC = timestampUTC;
      return this;
    }

    public Builder devEUI(String devEUI) {
      this.devEUI = devEUI;
      return this;
    }

    public Builder devAddr(String devAddr) {
      this.devAddr = devAddr;
      return this;
    }

    public Builder payloadHex(String payloadHex) {
      this.payloadHex = payloadHex;
      return this;
    }

    public Builder lrrid(String lrrid) {
      this.lrrid = lrrid;
      return this;
    }

    public Builder lrcId(String lrcId) {
      this.lrcId = lrcId;
      return this;
    }

    public Builder devLAT(String devLAT) {
      this.devLAT = devLAT;
      return this;
    }

    public Builder devLON(String devLON) {
      this.devLON = devLON;
      return this;
    }

    public Builder mType(String mType) {
      this.mType = mType;
      return this;
    }

    public Builder mTypeText(String mTypeText) {
      this.mTypeText = mTypeText;
      return this;
    }

    public Builder hasPayload(String hasPayload) {
      this.hasPayload = hasPayload;
      return this;
    }

    public Builder payloadDecoded(String payloadDecoded) {
      this.payloadDecoded = payloadDecoded;
      return this;
    }

    public Builder late(String late) {
      this.late = late;
      return this;
    }

    public Builder payloadSize(String payloadSize) {
      this.payloadSize = payloadSize;
      return this;
    }

    public Builder lteIPPacketSize(String lteIPPacketSize) {
      this.lteIPPacketSize = lteIPPacketSize;
      return this;
    }

    public Builder ipv4Decoded(String ipv4Decoded) {
      this.ipv4Decoded = ipv4Decoded;
      return this;
    }

    public Builder ltePacketProtocol(String ltePacketProtocol) {
      this.ltePacketProtocol = ltePacketProtocol;
      return this;
    }

    public Builder lteCause(String lteCause) {
      this.lteCause = lteCause;
      return this;
    }

    public Builder lteEBI(String lteEBI) {
      this.lteEBI = lteEBI;
      return this;
    }

    public Builder lteAPN(String lteAPN) {
      this.lteAPN = lteAPN;
      return this;
    }

    public Builder lteMSISDN(String lteMSISDN) {
      this.lteMSISDN = lteMSISDN;
      return this;
    }

    public Builder lteIMSI(String lteIMSI) {
      this.lteIMSI = lteIMSI;
      return this;
    }

    public Builder lteRAT(String lteRAT) {
      this.lteRAT = lteRAT;
      return this;
    }

    public Builder lteCELLID(String lteCELLID) {
      this.lteCELLID = lteCELLID;
      return this;
    }

    public Builder lteCELLTAC(String lteCELLTAC) {
      this.lteCELLTAC = lteCELLTAC;
      return this;
    }

    public Builder lteSERVNET(String lteSERVNET) {
      this.lteSERVNET = lteSERVNET;
      return this;
    }

    public Builder lteMCCMNC(String lteMCCMNC) {
      this.lteMCCMNC = lteMCCMNC;
      return this;
    }

    public Builder lteIMEI(String lteIMEI) {
      this.lteIMEI = lteIMEI;
      return this;
    }

    public Builder asID(String asID) {
      this.asID = asID;
      return this;
    }

    public Builder mfSzU(String mfSzU) {
      this.mfSzU = mfSzU;
      return this;
    }

    public Builder mfSzD(String mfSzD) {
      this.mfSzD = mfSzD;
      return this;
    }

    public Builder mfDur(String mfDur) {
      this.mfDur = mfDur;
      return this;
    }

    public Builder asReportDeliveryID(String asReportDeliveryID) {
      this.asReportDeliveryID = asReportDeliveryID;
      return this;
    }

    public Builder asRecipients(String asRecipients) {
      this.asRecipients = asRecipients;
      return this;
    }

      public Builder asDeliveryStatus(String asDeliveryStatus) {
        this.asDeliveryStatus = asDeliveryStatus;
        return this;
      }

    public ElementDeviceHistoryLte build() {
      return new ElementDeviceHistoryLte(this);
    }
  }
}
