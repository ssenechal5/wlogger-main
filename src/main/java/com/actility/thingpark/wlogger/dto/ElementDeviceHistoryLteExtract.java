package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElementDeviceHistoryLteExtract extends ElementDeviceHistoExtract {

  @JsonProperty("IPV4")
  public final String ipv4;

  @JsonProperty("IMEI")
  public final String imei;

  @JsonProperty("IMSI")
  public final String imsi;

  @JsonProperty("MTC")
  public final String mtc;

  @JsonProperty("Cause")
  public final String cause;

  @JsonProperty("EBI")
  public final String ebi;

  @JsonProperty("APN")
  public final String apn;

  @JsonProperty("MSISDN")
  public final String msisdn;

  @JsonProperty("RAT")
  public final String rat;

  @JsonProperty("CELLID")
  public final String cellid;

  @JsonProperty("CELLTAC")
  public final String celltac;

  @JsonProperty("SERVNET")
  public final String servnet;

  @JsonProperty("MCCMNC")
  public final String mccmnc;

  public final String mfSzU;
  public final String mfSzD;
  public final String mfDur;

  public ElementDeviceHistoryLteExtract(Builder builder) {
    super();
    this.timestampUTC = builder.timestampUTC;
    this.direction = builder.direction;
    this.mType = builder.mType;
    this.mTypeText = builder.mTypeText;
    this.ipv4 = builder.ipv4;
    this.imei = builder.imei;
    this.imsi = builder.imsi;
    this.payloadHex = builder.payloadHex;
    this.payloadDecoded = builder.payloadDecoded;
    this.mtc = builder.mtc;
    this.lrcId = builder.lrcId;
    this.devLAT = builder.devLAT;
    this.devLON = builder.devLON;
    this.late = builder.late;
    this.cause = builder.cause;
    this.ebi = builder.ebi;
    this.apn = builder.apn;
    this.msisdn = builder.msisdn;
    this.rat = builder.rat;
    this.cellid = builder.cellid;
    this.celltac = builder.celltac;
    this.servnet = builder.servnet;
    this.mccmnc = builder.mccmnc;
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
    private String timestampUTC;
    private String direction;
    private String mType;
    private String mTypeText;
    private String ipv4;
    private String imei;
    private String imsi;
    private String payloadHex;
    private String payloadDecoded;
    private String mtc;
    private String lrcId;
    private String devLAT;
    private String devLON;
    private String late;
    private String cause;
    private String ebi;
    private String apn;
    private String msisdn;
    private String rat;
    private String cellid;
    private String celltac;
    private String servnet;
    private String mccmnc;
    private String asID;
    private String mfSzU;
    private String mfSzD;
    private String mfDur;
    private String asReportDeliveryID;
    private List<Element> asRecipients;
    private String asDeliveryStatus;

    private Builder() {}

    public Builder timestampUTC(String timestampUTC) {
      this.timestampUTC = timestampUTC;
      return this;
    }

    public Builder direction(String direction) {
      this.direction = direction;
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

    public Builder ipv4(String ipv4) {
      this.ipv4 = ipv4;
      return this;
    }

    public Builder imei(String imei) {
      this.imei = imei;
      return this;
    }

    public Builder imsi(String imsi) {
      this.imsi = imsi;
      return this;
    }

    public Builder payloadHex(String payloadHex) {
      this.payloadHex = payloadHex;
      return this;
    }

    public Builder payloadDecoded(String payloadDecoded) {
      this.payloadDecoded = payloadDecoded;
      return this;
    }

    public Builder mtc(String mtc) {
      this.mtc = mtc;
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

    public Builder late(String late) {
      this.late = late;
      return this;
    }

    public Builder cause(String cause) {
      this.cause = cause;
      return this;
    }

    public Builder ebi(String ebi) {
      this.ebi = ebi;
      return this;
    }

    public Builder apn(String apn) {
      this.apn = apn;
      return this;
    }

    public Builder msisdn(String msisdn) {
      this.msisdn = msisdn;
      return this;
    }

    public Builder rat(String rat) {
      this.rat = rat;
      return this;
    }

    public Builder cellid(String cellid) {
      this.cellid = cellid;
      return this;
    }

    public Builder celltac(String celltac) {
      this.celltac = celltac;
      return this;
    }

    public Builder servnet(String servnet) {
      this.servnet = servnet;
      return this;
    }

    public Builder mccmnc(String mccmnc) {
      this.mccmnc = mccmnc;
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

    public Builder asRecipients(List<Element> asRecipients) {
      this.asRecipients = asRecipients;
      return this;
    }

    public Builder asDeliveryStatus(String asDeliveryStatus) {
      this.asDeliveryStatus = asDeliveryStatus;
      return this;
    }

    public ElementDeviceHistoryLteExtract build() {
      return new ElementDeviceHistoryLteExtract(this);
    }
  }
}
