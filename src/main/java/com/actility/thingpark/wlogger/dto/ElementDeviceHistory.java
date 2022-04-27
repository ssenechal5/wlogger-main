package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElementDeviceHistory extends ElementDeviceHisto {

  @JsonProperty("FPort")
  public final String fPort;

  @JsonProperty("FCnt")
  public final String fCnt;

  @JsonProperty("mic_hex")
  public final String micHex;

  @JsonProperty("LrrRSSI")
  public final String lrrRSSI;

  @JsonProperty("LrrSNR")
  public final String lrrSNR;

  @JsonProperty("LrrESP")
  public final String lrrESP;

  @JsonProperty("SpFact")
  public final String spFact;

  @JsonProperty("AirTime")
  public final String airTime;

  @JsonProperty("SubBand")
  public final String subBand;

  @JsonProperty("Channel")
  public final String channel;

  @JsonProperty("LrrLAT")
  public final String lrrLAT;

  @JsonProperty("LrrLON")
  public final String lrrLON;

  @JsonProperty("DevLrrCnt")
  public final String devLrrCnt;

  @JsonProperty("DevLocRadius")
  public final String devLocRadius;

  @JsonProperty("DevLocTime")
  public final String devLocTime;

  @JsonProperty("DevAlt")
  public final String devAlt;

  @JsonProperty("DevAltRadius")
  public final String devAltRadius;

  @JsonProperty("DevAcc")
  public final String devAcc;

  public final String customerId;
  public final String customerData;

  public final String rawMacCommands;

  public final String lrcRequestedRxDelay;

  @JsonProperty("ADRbit")
  public final String adRbit;

  @JsonProperty("ADRAckReq")
  public final String adrAckReq;

  @JsonProperty("AckRequested")
  public final String ackRequested;

  @JsonProperty("ACKbit")
  public final String acKbit;

  @JsonProperty("FPending")
  public final String fPending;

  @JsonProperty("DLStatus")
  public final String dlStatus;

  @JsonProperty("DLFailedCause1")
  public final String dlFailedCause1;

  @JsonProperty("DLFailedCause2")
  public final String dlFailedCause2;

  @JsonProperty("DLFailedCause3")
  public final String dlFailedCause3;

  public final String distance;

  @JsonProperty("LRRList")
  public final String lrrList;

  public final String hasMac;

  @JsonProperty("MACDecoded")
  public final String macDecoded;

  @JsonProperty("DLStatusText")
  public final String dlStatusText;

  @JsonProperty("DLFailedCause1Text")
  public final String dlFailedCause1Text;

  @JsonProperty("DLFailedCause2Text")
  public final String dlFailedCause2Text;

  @JsonProperty("DLFailedCause3Text")
  public final String dlFailedCause3Text;

  @JsonProperty("SolvLAT")
  public final String solvLAT;

  @JsonProperty("SolvLON")
  public final String solvLON;

  public final String gwOp;
  public final String gwID;
  public final String gwTk;
  public final String ismb;
  public final String mod;
  public final String dr;

  @JsonProperty("DevNorthVelocity")
  public final String devNorthVelocity;

  @JsonProperty("DevEastVelocity")
  public final String devEastVelocity;

  public final String mcc;
  public final String tss;
  public final String lcn;
  public final String lsc;
  public final String lfc;
  public final String rt;
  public final String rr;

  @JsonProperty("aFCntDown")
  public final String afCntDown;

  @JsonProperty("nFCntDown")
  public final String nfCntDown;

  public final String confAFCntDown;
  public final String confNFCntDown;
  public final String confFCntUp;
  public final String lfd;
  public final String ts;
  public final String rul;
  public final String freq;

  @JsonProperty("class")
  public final String clazz;

  public final String bper;
  public final String notifTp;
  public final String resetTp;

  @JsonProperty("payload_driver_id")
  public final String payloadDriverId;

  @JsonProperty("payload_driver_model")
  public final String payloadDriverModel;

  @JsonProperty("payload_driver_application")
  public final String payloadDriverApplication;

  @JsonProperty("payload_decoding_error")
  public final String payloadDecodingError;

  public final String lcMU;

  @JsonProperty("payload_encryption")
  public final String payloadEncryption;

  public final String tags;

  public final String foreignOperatorNSID;
  public final String rfRegion;

  private ElementDeviceHistory(Builder builder) {
    super();
    this.uid = builder.uid;
    this.direction = builder.direction;
    this.timestamp = builder.timestamp;
    this.devEUI = builder.devEUI;
    this.fPort = builder.fPort;
    this.fCnt = builder.fCnt;
    this.payloadHex = builder.payloadHex;
    this.micHex = builder.micHex;
    this.lrrRSSI = builder.lrrRSSI;
    this.lrrSNR = builder.lrrSNR;
    this.lrrESP = builder.lrrESP;
    this.spFact = builder.spFact;
    this.airTime = builder.airTime;
    this.subBand = builder.subBand;
    this.channel = builder.channel;
    this.lrrid = builder.lrrid;
    this.lrrLAT = builder.lrrLAT;
    this.lrrLON = builder.lrrLON;
    this.devLrrCnt = builder.devLrrCnt;
    this.devLAT = builder.devLAT;
    this.devLON = builder.devLON;
    this.devLocRadius = builder.devLocRadius;
    this.devLocTime = builder.devLocTime;
    this.devAlt = builder.devAlt;
    this.devAltRadius = builder.devAltRadius;
    this.devAcc = builder.devAcc;
    this.customerId = builder.customerId;
    this.customerData = builder.customerData;
    this.lrcId = builder.lrcId;
    this.timestampUTC = builder.timestampUTC;
    this.rawMacCommands = builder.rawMacCommands;
    this.mType = builder.mType;
    this.lrcRequestedRxDelay = builder.lrcRequestedRxDelay;
    this.devAddr = builder.devAddr;
    this.adRbit = builder.adRbit;
    this.adrAckReq = builder.adrAckReq;
    this.ackRequested = builder.ackRequested;
    this.acKbit = builder.acKbit;
    this.fPending = builder.fPending;
    this.dlStatus = builder.dlStatus;
    this.dlFailedCause1 = builder.dlFailedCause1;
    this.dlFailedCause2 = builder.dlFailedCause2;
    this.dlFailedCause3 = builder.dlFailedCause3;
    this.distance = builder.distance;
    this.lrrList = builder.lrrList;
    this.mTypeText = builder.mTypeText;
    this.hasMac = builder.hasMac;
    this.macDecoded = builder.macDecoded;
    this.hasPayload = builder.hasPayload;
    this.payloadDecoded = builder.payloadDecoded;
    this.dlStatusText = builder.dlStatusText;
    this.dlFailedCause1Text = builder.dlFailedCause1Text;
    this.dlFailedCause2Text = builder.dlFailedCause2Text;
    this.dlFailedCause3Text = builder.dlFailedCause3Text;
    this.late = builder.late;
    this.solvLAT = builder.solvLAT;
    this.solvLON = builder.solvLON;
    this.payloadSize = builder.payloadSize;
    this.gwOp = builder.gwOp;
    this.gwID = builder.gwID;
    this.gwTk = builder.gwTk;
    this.ismb = builder.ismb;
    this.mod = builder.mod;
    this.dr = builder.dr;
    this.devNorthVelocity = builder.devNorthVelocity;
    this.devEastVelocity = builder.devEastVelocity;
    this.asID = builder.asID;
    this.mcc = builder.mcc;
    this.tss = builder.tss;
    this.lcn = builder.lcn;
    this.lsc = builder.lsc;
    this.lfc = builder.lfc;
    this.rt = builder.rt;
    this.rr = builder.rr;
    this.afCntDown = builder.afCntDown;
    this.nfCntDown = builder.nfCntDown;
    this.confAFCntDown = builder.confAFCntDown;
    this.confNFCntDown = builder.confNFCntDown;
    this.confFCntUp = builder.confFCntUp;
    this.lfd = builder.lfd;
    this.ts = builder.ts;
    this.rul = builder.rul;
    this.freq = builder.freq;
    this.clazz = builder.clazz;
    this.bper = builder.bper;
    this.notifTp = builder.notifTp;
    this.resetTp = builder.resetTp;
    this.payloadDriverId = builder.payloadDriverId;
    this.payloadDriverModel = builder.payloadDriverModel;
    this.payloadDriverApplication = builder.payloadDriverApplication;
    this.payloadDecodingError = builder.payloadDecodingError;
    this.lcMU = builder.lcMU;
    this.payloadEncryption = builder.payloadEncryption;
    this.tags = builder.tags;
    this.foreignOperatorNSID = builder.foreignOperatorNSID;
    this.rfRegion = builder.rfRegion;
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
    private String devEUI;
    private String fPort;
    private String fCnt;
    private String payloadHex;
    private String micHex;
    private String lrrRSSI;
    private String lrrSNR;
    private String lrrESP;
    private String spFact;
    private String airTime;
    private String subBand;
    private String channel;
    private String lrrid;
    private String lrrLAT;
    private String lrrLON;
    private String devLrrCnt;
    private String devLAT;
    private String devLON;
    private String devLocRadius;
    private String devLocTime;
    private String devAlt;
    private String devAltRadius;
    private String devAcc;
    private String customerId;
    private String customerData;
    private String lrcId;
    private String timestampUTC;
    private String rawMacCommands;
    private String mType;
    private String lrcRequestedRxDelay;
    private String devAddr;
    private String adRbit;
    private String adrAckReq;
    private String ackRequested;
    private String acKbit;
    private String fPending;
    private String dlStatus;
    private String dlFailedCause1;
    private String dlFailedCause2;
    private String dlFailedCause3;
    private String distance;
    private String lrrList;
    private String mTypeText;
    private String hasMac;
    private String macDecoded;
    private String hasPayload;
    private String payloadDecoded;
    private String dlStatusText;
    private String dlFailedCause1Text;
    private String dlFailedCause2Text;
    private String dlFailedCause3Text;
    private String late;
    private String solvLAT;
    private String solvLON;
    private String payloadSize;
    private String gwOp;
    private String gwID;
    private String gwTk;
    private String ismb;
    private String mod;
    private String dr;
    private String devNorthVelocity;
    private String devEastVelocity;
    private String asID;
    private String mcc;
    private String tss;
    private String lcn;
    private String lsc;
    private String lfc;
    private String rt;
    private String rr;
    private String afCntDown;
    private String nfCntDown;
    private String confAFCntDown;
    private String confNFCntDown;
    private String confFCntUp;
    private String lfd;
    private String ts;
    private String rul;
    private String freq;
    private String clazz;
    private String bper;
    private String notifTp;
    private String resetTp;
    private String payloadDriverId;
    private String payloadDriverModel;
    private String payloadDriverApplication;
    private String payloadDecodingError;
    private String lcMU;
    private String payloadEncryption;
    private String tags;
    private String foreignOperatorNSID;
    private String rfRegion;
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

    public Builder devEUI(String devEUI) {
      this.devEUI = devEUI;
      return this;
    }

    public Builder fPort(String fPort) {
      this.fPort = fPort;
      return this;
    }

    public Builder fCnt(String fCnt) {
      this.fCnt = fCnt;
      return this;
    }

    public Builder payloadHex(String payloadHex) {
      this.payloadHex = payloadHex;
      return this;
    }

    public Builder micHex(String micHex) {
      this.micHex = micHex;
      return this;
    }

    public Builder lrrRSSI(String lrrRSSI) {
      this.lrrRSSI = lrrRSSI;
      return this;
    }

    public Builder lrrSNR(String lrrSNR) {
      this.lrrSNR = lrrSNR;
      return this;
    }

    public Builder lrrESP(String lrrESP) {
      this.lrrESP = lrrESP;
      return this;
    }

    public Builder spFact(String spFact) {
      this.spFact = spFact;
      return this;
    }

    public Builder airTime(String airTime) {
      this.airTime = airTime;
      return this;
    }

    public Builder subBand(String subBand) {
      this.subBand = subBand;
      return this;
    }

    public Builder channel(String channel) {
      this.channel = channel;
      return this;
    }

    public Builder lrrid(String lrrid) {
      this.lrrid = lrrid;
      return this;
    }

    public Builder lrrLAT(String lrrLAT) {
      this.lrrLAT = lrrLAT;
      return this;
    }

    public Builder lrrLON(String lrrLON) {
      this.lrrLON = lrrLON;
      return this;
    }

    public Builder devLrrCnt(String devLrrCnt) {
      this.devLrrCnt = devLrrCnt;
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

    public Builder devLocRadius(String devLocRadius) {
      this.devLocRadius = devLocRadius;
      return this;
    }

    public Builder devLocTime(String devLocTime) {
      this.devLocTime = devLocTime;
      return this;
    }

    public Builder devAlt(String devAlt) {
      this.devAlt = devAlt;
      return this;
    }

    public Builder devAltRadius(String devAltRadius) {
      this.devAltRadius = devAltRadius;
      return this;
    }

    public Builder devAcc(String devAcc) {
      this.devAcc = devAcc;
      return this;
    }

    public Builder customerId(String customerId) {
      this.customerId = customerId;
      return this;
    }

    public Builder customerData(String customerData) {
      this.customerData = customerData;
      return this;
    }

    public Builder lrcId(String lrcId) {
      this.lrcId = lrcId;
      return this;
    }

    public Builder timestampUTC(String timestampUTC) {
      this.timestampUTC = timestampUTC;
      return this;
    }

    public Builder rawMacCommands(String rawMacCommands) {
      this.rawMacCommands = rawMacCommands;
      return this;
    }

    public Builder mType(String mType) {
      this.mType = mType;
      return this;
    }

    public Builder lrcRequestedRxDelay(String lrcRequestedRxDelay) {
      this.lrcRequestedRxDelay = lrcRequestedRxDelay;
      return this;
    }

    public Builder devAddr(String devAddr) {
      this.devAddr = devAddr;
      return this;
    }

    public Builder adRbit(String adRbit) {
      this.adRbit = adRbit;
      return this;
    }

    public Builder adrAckReq(String adrAckReq) {
      this.adrAckReq = adrAckReq;
      return this;
    }

    public Builder ackRequested(String ackRequested) {
      this.ackRequested = ackRequested;
      return this;
    }

    public Builder acKbit(String acKbit) {
      this.acKbit = acKbit;
      return this;
    }

    public Builder fPending(String fPending) {
      this.fPending = fPending;
      return this;
    }

    public Builder dlStatus(String dlStatus) {
      this.dlStatus = dlStatus;
      return this;
    }

    public Builder dlFailedCause1(String dlFailedCause1) {
      this.dlFailedCause1 = dlFailedCause1;
      return this;
    }

    public Builder dlFailedCause2(String dlFailedCause2) {
      this.dlFailedCause2 = dlFailedCause2;
      return this;
    }

    public Builder dlFailedCause3(String dlFailedCause3) {
      this.dlFailedCause3 = dlFailedCause3;
      return this;
    }

    public Builder distance(String distance) {
      this.distance = distance;
      return this;
    }

    public Builder lrrList(String lrrList) {
      this.lrrList = lrrList;
      return this;
    }

    public Builder mTypeText(String mTypeText) {
      this.mTypeText = mTypeText;
      return this;
    }

    public Builder hasMac(String hasMac) {
      this.hasMac = hasMac;
      return this;
    }

    public Builder macDecoded(String macDecoded) {
      this.macDecoded = macDecoded;
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

    public Builder dlStatusText(String dlStatusText) {
      this.dlStatusText = dlStatusText;
      return this;
    }

    public Builder dlFailedCause1Text(String dlFailedCause1Text) {
      this.dlFailedCause1Text = dlFailedCause1Text;
      return this;
    }

    public Builder dlFailedCause2Text(String dlFailedCause2Text) {
      this.dlFailedCause2Text = dlFailedCause2Text;
      return this;
    }

    public Builder dlFailedCause3Text(String dlFailedCause3Text) {
      this.dlFailedCause3Text = dlFailedCause3Text;
      return this;
    }

    public Builder late(String late) {
      this.late = late;
      return this;
    }

    public Builder solvLAT(String solvLAT) {
      this.solvLAT = solvLAT;
      return this;
    }

    public Builder solvLON(String solvLON) {
      this.solvLON = solvLON;
      return this;
    }

    public Builder payloadSize(String payloadSize) {
      this.payloadSize = payloadSize;
      return this;
    }

    public Builder gwOp(String gwOp) {
      this.gwOp = gwOp;
      return this;
    }

    public Builder gwID(String gwID) {
      this.gwID = gwID;
      return this;
    }

    public Builder gwTk(String gwTk) {
      this.gwTk = gwTk;
      return this;
    }

    public Builder ismb(String ismb) {
      this.ismb = ismb;
      return this;
    }

    public Builder mod(String mod) {
      this.mod = mod;
      return this;
    }

    public Builder dr(String dr) {
      this.dr = dr;
      return this;
    }

    public Builder devNorthVelocity(String devNorthVelocity) {
      this.devNorthVelocity = devNorthVelocity;
      return this;
    }

    public Builder devEastVelocity(String devEastVelocity) {
      this.devEastVelocity = devEastVelocity;
      return this;
    }

    public Builder asID(String asID) {
      this.asID = asID;
      return this;
    }

    public Builder mcc(String mcc) {
      this.mcc = mcc;
      return this;
    }

    public Builder tss(String tss) {
      this.tss = tss;
      return this;
    }

    public Builder lcn(String lcn) {
      this.lcn = lcn;
      return this;
    }

    public Builder lsc(String lsc) {
      this.lsc = lsc;
      return this;
    }

    public Builder lfc(String lfc) {
      this.lfc = lfc;
      return this;
    }

    public Builder rt(String rt) {
      this.rt = rt;
      return this;
    }

    public Builder rr(String rr) {
      this.rr = rr;
      return this;
    }

    public Builder afCntDown(String afCntDown) {
      this.afCntDown = afCntDown;
      return this;
    }

    public Builder nfCntDown(String nfCntDown) {
      this.nfCntDown = nfCntDown;
      return this;
    }

    public Builder confAFCntDown(String confAFCntDown) {
      this.confAFCntDown = confAFCntDown;
      return this;
    }

    public Builder confNFCntDown(String confNFCntDown) {
      this.confNFCntDown = confNFCntDown;
      return this;
    }

    public Builder confFCntUp(String confFCntUp) {
      this.confFCntUp = confFCntUp;
      return this;
    }

    public Builder lfd(String lfd) {
      this.lfd = lfd;
      return this;
    }

    public Builder ts(String ts) {
      this.ts = ts;
      return this;
    }

    public Builder rul(String rul) {
      this.rul = rul;
      return this;
    }

    public Builder freq(String freq) {
      this.freq = freq;
      return this;
    }

    public Builder clazz(String clazz) {
      this.clazz = clazz;
      return this;
    }

    public Builder bper(String bper) {
      this.bper = bper;
      return this;
    }

    public Builder notifTp(String notifTp) {
      this.notifTp = notifTp;
      return this;
    }

    public Builder resetTp(String resetTp) {
      this.resetTp = resetTp;
      return this;
    }

    public Builder payloadDriverId(String payloadDriverId) {
      this.payloadDriverId = payloadDriverId;
      return this;
    }

    public Builder payloadDriverModel(String payloadDriverModel) {
      this.payloadDriverModel = payloadDriverModel;
      return this;
    }

    public Builder payloadDriverApplication(String payloadDriverApplication) {
      this.payloadDriverApplication = payloadDriverApplication;
      return this;
    }

    public Builder payloadDecodingError(String payloadDecodingError) {
      this.payloadDecodingError = payloadDecodingError;
      return this;
    }

    public Builder lcMU(String lcMU) {
      this.lcMU = lcMU;
      return this;
    }

    public Builder payloadEncryption(String payloadEncryption) {
      this.payloadEncryption = payloadEncryption;
      return this;
    }

    public Builder tags(String tags) {
      this.tags = tags;
      return this;
    }

    public Builder foreignOperatorNSID(String foreignOperatorNSID) {
      this.foreignOperatorNSID = foreignOperatorNSID;
      return this;
    }

    public Builder rfRegion(String rfRegion) {
      this.rfRegion = rfRegion;
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

    public ElementDeviceHistory build() {
      return new ElementDeviceHistory(this);
    }
  }
}
