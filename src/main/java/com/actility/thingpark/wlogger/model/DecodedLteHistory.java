package com.actility.thingpark.wlogger.model;

import java.util.Date;
import java.util.List;

public class DecodedLteHistory extends DecodedHistory {
  private final String lteIPPacketSize;
  private final String ipv4Decoded;
  private final String ltePacketProtocol;
  private final String lteCause;
  private final String lteEBI;
  private final String lteAPN;
  private final String lteMSISDN;
  private final String lteIMSI;
  private final String lteRAT;
  private final String lteCELLID;
  private final String lteCELLTAC;
  private final String lteSERVNET;
  private final String lteMCCMNC;
  private final String lteIMEI;
  private final String mfSzU;
  private final String mfSzD;
  private final String mfDur;

  private DecodedLteHistory(Builder builder) {
    this.hasPayload = builder.hasPayload;
    this.ipv4Decoded = builder.ipv4Decoded;
    this.lteAPN = builder.lteAPN;
    this.mfSzU = builder.mfSzU;
    this.lteMCCMNC = builder.lteMCCMNC;
    this.ltePacketProtocol = builder.ltePacketProtocol;
    this.payloadDecoded = builder.payloadDecoded;
    this.lteIPPacketSize = builder.lteIPPacketSize;
    this.lteCELLID = builder.lteCELLID;
    this.uid = builder.uid;
    this.devEUI = builder.devEUI;
    this.lteIMEI = builder.lteIMEI;
    this.lteEBI = builder.lteEBI;
    this.lteIMSI = builder.lteIMSI;
    this.lteMSISDN = builder.lteMSISDN;
    this.timestamp = builder.timestamp;
    this.asID = builder.asID;
    this.mTypeText = builder.mTypeText;
    this.lteCause = builder.lteCause;
    this.lteSERVNET = builder.lteSERVNET;
    this.mfSzD = builder.mfSzD;
    this.lrrid = builder.lrrid;
    this.payloadHex = builder.payloadHex;
    this.direction = builder.direction;
    this.late = builder.late;
    this.devAddr = builder.devAddr;
    this.lteCELLTAC = builder.lteCELLTAC;
    this.lteRAT = builder.lteRAT;
    this.lrcId = builder.lrcId;
    this.mfDur = builder.mfDur;
    this.devLAT = builder.devLAT;
    this.devLON = builder.devLON;
    this.payloadSize = builder.payloadSize;
    this.mType = builder.mType;
    this.asReportDeliveryID = builder.asReportDeliveryID;
    this.asRecipients = builder.asRecipients;
  }

  public String getLteIPPacketSize() {
    return lteIPPacketSize;
  }

  public String getIpv4Decoded() {
    return ipv4Decoded;
  }

  public String getLtePacketProtocol() {
    return ltePacketProtocol;
  }

  public String getLteCause() {
    return lteCause;
  }

  public String getLteEBI() {
    return lteEBI;
  }

  public String getLteAPN() {
    return lteAPN;
  }

  public String getLteMSISDN() {
    return lteMSISDN;
  }

  public String getLteIMSI() {
    return lteIMSI;
  }

  public String getLteRAT() {
    return lteRAT;
  }

  public String getLteCELLID() {
    return lteCELLID;
  }

  public String getLteCELLTAC() {
    return lteCELLTAC;
  }

  public String getLteSERVNET() {
    return lteSERVNET;
  }

  public String getLteMCCMNC() {
    return lteMCCMNC;
  }

  public String getLteIMEI() {
    return lteIMEI;
  }

  public String getMfSzU() {
    return mfSzU;
  }

  public String getMfSzD() {
    return mfSzD;
  }

  public String getMfDur() {
    return mfDur;
  }

  public static Builder  builder() {
    return new Builder();
  }

  public static final class Builder {
    private String uid;
    private Direction direction;
    private Date timestamp;
    private String devEUI;
    private String devAddr;
    private String payloadHex;
    private String lrrid;
    private String lrcId;
    private Double devLAT;
    private Double devLON;
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
    private List<Recipient> asRecipients;

    private Builder() {
    }

    public Builder withUid(String uid) {
      this.uid = uid;
      return this;
    }

    public Builder withDirection(Direction direction) {
      this.direction = direction;
      return this;
    }

    public Builder withTimestamp(Date timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder withDevEUI(String devEUI) {
      this.devEUI = devEUI;
      return this;
    }

    public Builder withDevAddr(String devAddr) {
      this.devAddr = devAddr;
      return this;
    }

    public Builder withPayloadHex(String payloadHex) {
      this.payloadHex = payloadHex;
      return this;
    }

    public Builder withLrrid(String lrrid) {
      this.lrrid = lrrid;
      return this;
    }

    public Builder withLrcId(String lrcId) {
      this.lrcId = lrcId;
      return this;
    }

    public Builder withDevLAT(Double devLAT) {
      this.devLAT = devLAT;
      return this;
    }

    public Builder withDevLON(Double devLON) {
      this.devLON = devLON;
      return this;
    }

    public Builder withMType(String mType) {
      this.mType = mType;
      return this;
    }

    public Builder withMTypeText(String mTypeText) {
      this.mTypeText = mTypeText;
      return this;
    }

    public Builder withHasPayload(String hasPayload) {
      this.hasPayload = hasPayload;
      return this;
    }

    public Builder withPayloadDecoded(String payloadDecoded) {
      this.payloadDecoded = payloadDecoded;
      return this;
    }

    public Builder withLate(String late) {
      this.late = late;
      return this;
    }

    public Builder withPayloadSize(String payloadSize) {
      this.payloadSize = payloadSize;
      return this;
    }

    public Builder withLteIPPacketSize(String lteIPPacketSize) {
      this.lteIPPacketSize = lteIPPacketSize;
      return this;
    }

    public Builder withIpv4Decoded(String ipv4Decoded) {
      this.ipv4Decoded = ipv4Decoded;
      return this;
    }

    public Builder withLtePacketProtocol(String ltePacketProtocol) {
      this.ltePacketProtocol = ltePacketProtocol;
      return this;
    }

    public Builder withLteCause(String lteCause) {
      this.lteCause = lteCause;
      return this;
    }

    public Builder withLteEBI(String lteEBI) {
      this.lteEBI = lteEBI;
      return this;
    }

    public Builder withLteAPN(String lteAPN) {
      this.lteAPN = lteAPN;
      return this;
    }

    public Builder withLteMSISDN(String lteMSISDN) {
      this.lteMSISDN = lteMSISDN;
      return this;
    }

    public Builder withLteIMSI(String lteIMSI) {
      this.lteIMSI = lteIMSI;
      return this;
    }

    public Builder withLteRAT(String lteRAT) {
      this.lteRAT = lteRAT;
      return this;
    }

    public Builder withLteCELLID(String lteCELLID) {
      this.lteCELLID = lteCELLID;
      return this;
    }

    public Builder withLteCELLTAC(String lteCELLTAC) {
      this.lteCELLTAC = lteCELLTAC;
      return this;
    }

    public Builder withLteSERVNET(String lteSERVNET) {
      this.lteSERVNET = lteSERVNET;
      return this;
    }

    public Builder withLteMCCMNC(String lteMCCMNC) {
      this.lteMCCMNC = lteMCCMNC;
      return this;
    }

    public Builder withLteIMEI(String lteIMEI) {
      this.lteIMEI = lteIMEI;
      return this;
    }

    public Builder withAsID(String asID) {
      this.asID = asID;
      return this;
    }

    public Builder withMfSzU(String mfSzU) {
      this.mfSzU = mfSzU;
      return this;
    }

    public Builder withMfSzD(String mfSzD) {
      this.mfSzD = mfSzD;
      return this;
    }

    public Builder withMfDur(String mfDur) {
      this.mfDur = mfDur;
      return this;
    }

    public Builder withAsReportDeliveryID(String asReportDeliveryID) {
      this.asReportDeliveryID = asReportDeliveryID;
      return this;
    }

    public Builder withAsRecipients(List<Recipient> asRecipients) {
      this.asRecipients = asRecipients;
      return this;
    }

    public DecodedLteHistory build() {
      return new DecodedLteHistory(this);
    }
  }
}
