package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.decoder.Decoder;
import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wloggerwrap.WLWrapper;
import io.vertx.core.json.JsonArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class DecodedLoraHistory extends DecodedHistory {
  private final String fPort;
  private final String fCnt;
  private final String micHex;
  private final String lrrRSSI;
  private final String lrrSNR;
  private final String lrrESP;
  private final String spFact;
  private final String airTime;
  private final String subBand;
  private final String channel;
  private final String lrrLAT;
  private final String lrrLON;
  private final String devLrrCnt;
  private final String devLocRadius;
  private final String devLocTime;
  private final String devAlt;
  private final String devAltRadius;
  private final String devAcc;
  private final String customerId;
  private final String customerData;
  private final String rawMacCommands;
  private final String lrcRequestedRxDelay;
  private final String adRbit;
  private final String adrAckReq;
  private final String ackRequested;
  private final String acKbit;
  private final String fPending;
  private final String dlStatus;
  private final String dlFailedCause1;
  private final String dlFailedCause2;
  private final String dlFailedCause3;
  private final String distance;
  private final List<Lrr> lrrs;
  private final String hasMac;
  private final String macDecoded;
  private final String dlStatusText;
  private final String dlFailedCause1Text;
  private final String dlFailedCause2Text;
  private final String dlFailedCause3Text;
  private final String solvLAT;
  private final String solvLON;
  private final String gwOp;
  private final String gwID;
  private final String gwTk;
  private final String ismb;
  private final String mod;
  private final String dr;
  private final String devNorthVelocity;
  private final String devEastVelocity;
  private final String mcc;
  private final String tss;
  private final String lcn;
  private final String lsc;
  private final String lfc;
  private final String rt;
  private final String rr;
  private final String afCntDown;
  private final String nfCntDown;
  private final String confAFCntDown;
  private final String confNFCntDown;
  private final String confFCntUp;
  private final List<Lfd> lfd;
  private final String ts;
  private final String rul;
  private final String freq;
  private final String clazz;
  private final String bper;
  private final String notifTp;
  private final String resetTp;
  private final String payloadDriverId;
  private final String payloadDriverModel;
  private final String payloadDriverApplication;
  private final String payloadDecodingError;
  private final String lcMU;
  private final String payloadEncryption;
  private final String tags;
  private final String rfRegion;
  private final String foreignOperatorNSID;

  private Decoder payloadDecoder = null;
  private WLWrapper wrapper = null;

  private DecodedLoraHistory(Builder builder) {
    this.mTypeText = builder.mTypeText;
    this.spFact = builder.spFact;
    this.hasMac = builder.hasMac;
    this.dlFailedCause3 = builder.dlFailedCause3;
    this.devAltRadius = builder.devAltRadius;
    this.devAlt = builder.devAlt;
    this.dlFailedCause2Text = builder.dlFailedCause2Text;
    this.bper = builder.bper;
    this.timestamp = builder.timestamp;
    this.devAddr = builder.devAddr;
    this.customerId = builder.customerId;
    this.devLAT = builder.devLAT;
    this.mcc = builder.mcc;
    this.payloadDriverApplication = builder.payloadDriverApplication;
    this.dlFailedCause3Text = builder.dlFailedCause3Text;
    this.confFCntUp = builder.confFCntUp;
    this.fPending = builder.fPending;
    this.lfd = builder.lfd;
    this.lrrESP = builder.lrrESP;
    this.devLrrCnt = builder.devLrrCnt;
    this.tss = builder.tss;
    this.devNorthVelocity = builder.devNorthVelocity;
    this.asID = builder.asID;
    this.rul = builder.rul;
    this.channel = builder.channel;
    this.micHex = builder.micHex;
    this.acKbit = builder.acKbit;
    this.lfc = builder.lfc;
    this.lrrLON = builder.lrrLON;
    this.customerData = builder.customerData;
    this.airTime = builder.airTime;
    this.notifTp = builder.notifTp;
    this.dlFailedCause1Text = builder.dlFailedCause1Text;
    this.subBand = builder.subBand;
    this.gwOp = builder.gwOp;
    this.adRbit = builder.adRbit;
    this.lcn = builder.lcn;
    this.devLocTime = builder.devLocTime;
    this.lrcRequestedRxDelay = builder.lrcRequestedRxDelay;
    this.lsc = builder.lsc;
    this.late = builder.late;
    this.rt = builder.rt;
    this.uid = builder.uid;
    this.devEUI = builder.devEUI;
    this.devEastVelocity = builder.devEastVelocity;
    this.lrrs = builder.lrrs;
    this.payloadDriverId = builder.payloadDriverId;
    this.nfCntDown = builder.nfCntDown;
    this.payloadDriverModel = builder.payloadDriverModel;
    this.distance = builder.distance;
    this.solvLAT = builder.solvLAT;
    this.dr = builder.dr;
    this.solvLON = builder.solvLON;
    this.rawMacCommands = builder.rawMacCommands;
    this.devLocRadius = builder.devLocRadius;
    this.macDecoded = builder.macDecoded;
    this.payloadDecoded = builder.payloadDecoded;
    this.resetTp = builder.resetTp;
    this.lcMU = builder.lcMU;
    this.adrAckReq = builder.adrAckReq;
    this.confAFCntDown = builder.confAFCntDown;
    this.ackRequested = builder.ackRequested;
    this.gwID = builder.gwID;
    this.devAcc = builder.devAcc;
    this.gwTk = builder.gwTk;
    this.payloadDecodingError = builder.payloadDecodingError;
    this.mType = builder.mType;
    this.mod = builder.mod;
    this.afCntDown = builder.afCntDown;
    this.fPort = builder.fPort;
    this.confNFCntDown = builder.confNFCntDown;
    this.rr = builder.rr;
    this.direction = builder.direction;
    this.freq = builder.freq;
    this.fCnt = builder.fCnt;
    this.dlFailedCause2 = builder.dlFailedCause2;
    this.devLON = builder.devLON;
    this.ts = builder.ts;
    this.lrrRSSI = builder.lrrRSSI;
    this.dlStatus = builder.dlStatus;
    this.lrrid = builder.lrrid;
    this.dlStatusText = builder.dlStatusText;
    this.payloadSize = builder.payloadSize;
    this.payloadHex = builder.payloadHex;
    this.lrrLAT = builder.lrrLAT;
    this.lrcId = builder.lrcId;
    this.clazz = builder.clazz;
    this.hasPayload = builder.hasPayload;
    this.lrrSNR = builder.lrrSNR;
    this.dlFailedCause1 = builder.dlFailedCause1;
    this.ismb = builder.ismb;
    this.payloadEncryption = builder.payloadEncryption;
    this.tags = builder.tags;
    this.rfRegion = builder.rfRegion;
    this.foreignOperatorNSID = builder.foreignOperatorNSID;
    this.asReportDeliveryID = builder.asReportDeliveryID;
    this.asRecipients = builder.asRecipients;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getfPort() {
    return fPort;
  }

  public String getfCnt() {
    return fCnt;
  }

  public String getMicHex() {
    return micHex;
  }

  public String getLrrRSSI() {
    return lrrRSSI;
  }

  public String getLrrSNR() {
    return lrrSNR;
  }

  public String getLrrESP() {
    return lrrESP;
  }

  public String getSpFact() {
    return spFact;
  }

  public String getAirTime() {
    return airTime;
  }

  public String getSubBand() {
    return subBand;
  }

  public String getChannel() {
    return channel;
  }

  public String getLrrLAT() {
    return lrrLAT;
  }

  public String getLrrLON() {
    return lrrLON;
  }

  public String getDevLrrCnt() {
    return devLrrCnt;
  }

  private static boolean isLatitudeValid(double latitude) {
    return Double.compare(latitude,0) != 0 && Double.compare(latitude,-54) > 0;
  }

  private static boolean isLongitudeValid(double longitude) {
    return Double.compare(longitude,0) != 0
            && Double.compare(longitude,-50) > 0
            && Double.compare(longitude,130) < 0;
  }

  public boolean isLocationValid() {
    if (!Direction.UPLINK.equals(direction)) {
      return false;
    }
    return isLatitudeValid(devLAT != null ? devLAT : 0)
        && isLongitudeValid(devLON != null ? devLON : 0);
  }

  public String getDevLocRadius() {
    return devLocRadius;
  }

  public String getDevLocTime() {
    return devLocTime;
  }

  public String getDevAlt() {
    return devAlt;
  }

  public String getDevAltRadius() {
    return devAltRadius;
  }

  public String getDevAcc() {
    return devAcc;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getCustomerData() {
    return customerData;
  }

  public String getRawMacCommands() {
    return rawMacCommands;
  }

  public String getLrcRequestedRxDelay() {
    return lrcRequestedRxDelay;
  }

  public String getAdRbit() {
    return adRbit;
  }

  public String getAdrAckReq() {
    return adrAckReq;
  }

  public String getAckRequested() {
    return ackRequested;
  }

  public String getAcKbit() {
    return acKbit;
  }

  public String getfPending() {
    return fPending;
  }

  public String getDlStatus() {
    return dlStatus;
  }

  public String getDlFailedCause1() {
    return dlFailedCause1;
  }

  public String getDlFailedCause2() {
    return dlFailedCause2;
  }

  public String getDlFailedCause3() {
    return dlFailedCause3;
  }

  public String getDistance() {
    return distance;
  }

  public List<Lrr> getLrrs() {
    return lrrs;
  }

  public String getLrrListAsHtmlTable(boolean passiveRoaming) {
    if (this.lrrs == null || this.lrrs.isEmpty()) {
      return "";
    }
    // 3) Build the LRR List
    StringBuilder table = new StringBuilder("<table class='chainList'>");
    // channel info is for EUI_location only
    if (Direction.LOCATION.equals(this.direction)) {
      // NFR 809
      if (passiveRoaming)
        table.append(
            "<thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}{channel}</td><td>ISM Band</td><td>RF Region</td><td>GWID</td><td>GWToken</td><td>DLAllowed</td><td>ForeignOperatorNetID</td><td>ForeignOperatorNSID</td></tr></thead>");
      else
        table.append(
            "<thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}{channel}</td><td>ISM Band</td><td>RF Region</td></tr></thead>");
    } else {
      // NFR 809
      if (passiveRoaming)
        table.append(
            "<thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}</td><td>ISM Band</td><td>RF Region</td><td>GWID</td><td>GWToken</td><td>DLAllowed</td><td>ForeignOperatorNetID</td><td>ForeignOperatorNSID</td></tr></thead>");
      else
        table.append(
            "<thead><tr><td>LRR</td><td>RSSI</td><td>SNR</td><td>ESP</td><td>CHAINS timestamp {GPS_RADIO|-}</td><td>ISM Band</td><td>RF Region</td></tr></thead>");
    }
    // Add the table
    return table
        .append("<tbody>")
        .append(
            lrrs.stream()
                .map(lrr -> lrr.getAsHtmlRow(direction, passiveRoaming))
                .collect(Collectors.joining()))
        .append("</tbody>")
        .append("</table>")
        .toString();
  }

  @Nullable
  public List<Element> getLrrListAsElements(boolean all, boolean passiveRoaming) {
    if (lrrs == null) {
      return null;
    }
    return lrrs.stream()
        .map(lrr -> lrr.getAsElement(all, passiveRoaming))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
  }

  public List<String> getLrrListAsCsv(boolean passiveRoaming, int lrrMax) {
    List<Lrr> lrrList = ofNullable(lrrs).orElse(new ArrayList<>());
    List<String> list =
        lrrList.stream()
            .map(lrr -> lrr.getAsCsv(passiveRoaming))
            .flatMap(List::stream)
            .collect(Collectors.toCollection(ArrayList::new));
    for (int k = lrrList.size(); k < lrrMax; k++) {
      list.addAll(Lrr.getEmptyCsv(passiveRoaming));
    }
    return list;
  }

  public String getHasMac() {
    return hasMac;
  }

  public String getMacDecoded() {
    return macDecoded;
  }

  public String getDlStatusText() {
    return dlStatusText;
  }

  public String getDlFailedCause1Text() {
    return dlFailedCause1Text;
  }

  public String getDlFailedCause2Text() {
    return dlFailedCause2Text;
  }

  public String getDlFailedCause3Text() {
    return dlFailedCause3Text;
  }

  public String getSolvLAT() {
    return solvLAT;
  }

  public String getSolvLON() {
    return solvLON;
  }

  public String getGwOp() {
    return gwOp;
  }

  public String getGwID() {
    return gwID;
  }

  public String getGwTk() {
    return gwTk;
  }

  public String getIsmb() {
    return ismb;
  }

  public String getMod() {
    return mod;
  }

  public String getDr() {
    return dr;
  }

  public String getDevNorthVelocity() {
    return devNorthVelocity;
  }

  public String getDevEastVelocity() {
    return devEastVelocity;
  }

  public String getMcc() {
    return mcc;
  }

  public String getTss() {
    return tss;
  }

  public String getLcn() {
    return lcn;
  }

  public String getLsc() {
    return lsc;
  }

  public String getLfc() {
    return lfc;
  }

  public String getRt() {
    return rt;
  }

  public String getRr() {
    return rr;
  }

  public String getAfCntDown() {
    return afCntDown;
  }

  public String getNfCntDown() {
    return nfCntDown;
  }

  public String getConfAFCntDown() {
    return confAFCntDown;
  }

  public String getConfNFCntDown() {
    return confNFCntDown;
  }

  public String getConfFCntUp() {
    return confFCntUp;
  }

  @Nullable
  public List<Element> getLfdAsElement() {
    if (lfd == null || lfd.isEmpty()) {
      return null;
    }
    return lfd.stream().map(Lfd::getAsElement).collect(toList());
  }

  public String getLfdAsJsonString() {
    if (lfd == null || lfd.isEmpty()) {
      return "";
    }
    JsonArray array = new JsonArray();
    lfd.stream().map(Lfd::getAsJson).forEach(array::add);
    return array.toString();
  }

  public String getLfdAsCsvString() {
    if (lfd == null || lfd.isEmpty()) {
      return "";
    }
    return lfd.stream().map(Lfd::getAsCsv).collect(Collectors.joining(","));
  }

  public String getTs() {
    return ts;
  }

  public String getRul() {
    return rul;
  }

  public String getFreq() {
    return freq;
  }

  public String getClazz() {
    return clazz;
  }

  public String getBper() {
    return bper;
  }

  public String getNotifTp() {
    return notifTp;
  }

  public String getResetTp() {
    return resetTp;
  }

  public String getPayloadDriverId() {
    return payloadDriverId;
  }

  public String getPayloadDriverModel() {
    return payloadDriverModel;
  }

  public String getPayloadDriverApplication() {
    return payloadDriverApplication;
  }

  public String getPayloadDecodingError() {
    return payloadDecodingError;
  }

  public String getLcMU() {
    return lcMU;
  }

  public String getPayloadEncryption() {
    return payloadEncryption;
  }

  public String getTags() { return tags;  }

  public String getRfRegion() { return rfRegion;  }

  public String getForeignOperatorNSID() { return foreignOperatorNSID;  }

  public String getAsReportDeliveryID() { return asReportDeliveryID;  }

  public Decoder getPayloadDecoder() {
    return payloadDecoder;
  }

  public void setPayloadDecoder(Decoder payloadDecoder) {
    this.payloadDecoder = payloadDecoder;
  }

  public WLWrapper getWrapper() {
    return wrapper;
  }

  public void setWrapper(WLWrapper wrapper) {
    this.wrapper = wrapper;
  }

  public static final class Builder {
    private String uid;
    private Direction direction;
    private Date timestamp;
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
    private Double devLAT;
    private Double devLON;
    private String devLocRadius;
    private String devLocTime;
    private String devAlt;
    private String devAltRadius;
    private String devAcc;
    private String customerId;
    private String customerData;
    private String lrcId;
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
    private List<Lrr> lrrs;
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
    private List<Lfd> lfd;
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
    private String rfRegion;
    private String foreignOperatorNSID;
    private String asReportDeliveryID;
    private List<Recipient> asRecipients;

    private Builder() {}

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

    public Builder withFPort(String fPort) {
      this.fPort = fPort;
      return this;
    }

    public Builder withFCnt(String fCnt) {
      this.fCnt = fCnt;
      return this;
    }

    public Builder withPayloadHex(String payloadHex) {
      this.payloadHex = payloadHex;
      return this;
    }

    public Builder withMicHex(String micHex) {
      this.micHex = micHex;
      return this;
    }

    public Builder withLrrRSSI(String lrrRSSI) {
      this.lrrRSSI = lrrRSSI;
      return this;
    }

    public Builder withLrrSNR(String lrrSNR) {
      this.lrrSNR = lrrSNR;
      return this;
    }

    public Builder withLrrESP(String lrrESP) {
      this.lrrESP = lrrESP;
      return this;
    }

    public Builder withSpFact(String spFact) {
      this.spFact = spFact;
      return this;
    }

    public Builder withAirTime(String airTime) {
      this.airTime = airTime;
      return this;
    }

    public Builder withSubBand(String subBand) {
      this.subBand = subBand;
      return this;
    }

    public Builder withChannel(String channel) {
      this.channel = channel;
      return this;
    }

    public Builder withLrrid(String lrrid) {
      this.lrrid = lrrid;
      return this;
    }

    public Builder withLrrLAT(String lrrLAT) {
      this.lrrLAT = lrrLAT;
      return this;
    }

    public Builder withLrrLON(String lrrLON) {
      this.lrrLON = lrrLON;
      return this;
    }

    public Builder withDevLrrCnt(String devLrrCnt) {
      this.devLrrCnt = devLrrCnt;
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

    public Builder withDevLocRadius(String devLocRadius) {
      this.devLocRadius = devLocRadius;
      return this;
    }

    public Builder withDevLocTime(String devLocTime) {
      this.devLocTime = devLocTime;
      return this;
    }

    public Builder withDevAlt(String devAlt) {
      this.devAlt = devAlt;
      return this;
    }

    public Builder withDevAltRadius(String devAltRadius) {
      this.devAltRadius = devAltRadius;
      return this;
    }

    public Builder withDevAcc(String devAcc) {
      this.devAcc = devAcc;
      return this;
    }

    public Builder withCustomerId(String customerId) {
      this.customerId = customerId;
      return this;
    }

    public Builder withCustomerData(String customerData) {
      this.customerData = customerData;
      return this;
    }

    public Builder withLrcId(String lrcId) {
      this.lrcId = lrcId;
      return this;
    }

    public Builder withRawMacCommands(String rawMacCommands) {
      this.rawMacCommands = rawMacCommands;
      return this;
    }

    public Builder withMType(String mType) {
      this.mType = mType;
      return this;
    }

    public Builder withLrcRequestedRxDelay(String lrcRequestedRxDelay) {
      this.lrcRequestedRxDelay = lrcRequestedRxDelay;
      return this;
    }

    public Builder withDevAddr(String devAddr) {
      this.devAddr = devAddr;
      return this;
    }

    public Builder withAdRbit(String adRbit) {
      this.adRbit = adRbit;
      return this;
    }

    public Builder withAdrAckReq(String adrAckReq) {
      this.adrAckReq = adrAckReq;
      return this;
    }

    public Builder withAckRequested(String ackRequested) {
      this.ackRequested = ackRequested;
      return this;
    }

    public Builder withAcKbit(String acKbit) {
      this.acKbit = acKbit;
      return this;
    }

    public Builder withFPending(String fPending) {
      this.fPending = fPending;
      return this;
    }

    public Builder withDlStatus(String dlStatus) {
      this.dlStatus = dlStatus;
      return this;
    }

    public Builder withDlFailedCause1(String dlFailedCause1) {
      this.dlFailedCause1 = dlFailedCause1;
      return this;
    }

    public Builder withDlFailedCause2(String dlFailedCause2) {
      this.dlFailedCause2 = dlFailedCause2;
      return this;
    }

    public Builder withDlFailedCause3(String dlFailedCause3) {
      this.dlFailedCause3 = dlFailedCause3;
      return this;
    }

    public Builder withDistance(String distance) {
      this.distance = distance;
      return this;
    }

    @Nonnull
    public Builder withLrrs(List<Lrr> lrrs) {
      this.lrrs = lrrs;
      return this;
    }

    public Builder withLrrList(String lrrList) {
      this.lrrList = lrrList;
      return this;
    }

    public Builder withMTypeText(String mTypeText) {
      this.mTypeText = mTypeText;
      return this;
    }

    public Builder withHasMac(String hasMac) {
      this.hasMac = hasMac;
      return this;
    }

    public Builder withMacDecoded(String macDecoded) {
      this.macDecoded = macDecoded;
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

    public Builder withDlStatusText(String dlStatusText) {
      this.dlStatusText = dlStatusText;
      return this;
    }

    public Builder withDlFailedCause1Text(String dlFailedCause1Text) {
      this.dlFailedCause1Text = dlFailedCause1Text;
      return this;
    }

    public Builder withDlFailedCause2Text(String dlFailedCause2Text) {
      this.dlFailedCause2Text = dlFailedCause2Text;
      return this;
    }

    public Builder withDlFailedCause3Text(String dlFailedCause3Text) {
      this.dlFailedCause3Text = dlFailedCause3Text;
      return this;
    }

    public Builder withLate(String late) {
      this.late = late;
      return this;
    }

    public Builder withSolvLAT(String solvLAT) {
      this.solvLAT = solvLAT;
      return this;
    }

    public Builder withSolvLON(String solvLON) {
      this.solvLON = solvLON;
      return this;
    }

    public Builder withPayloadSize(String payloadSize) {
      this.payloadSize = payloadSize;
      return this;
    }

    public Builder withGwOp(String gwOp) {
      this.gwOp = gwOp;
      return this;
    }

    public Builder withGwID(String gwID) {
      this.gwID = gwID;
      return this;
    }

    public Builder withGwTk(String gwTk) {
      this.gwTk = gwTk;
      return this;
    }

    public Builder withIsmb(String ismb) {
      this.ismb = ismb;
      return this;
    }

    public Builder withMod(String mod) {
      this.mod = mod;
      return this;
    }

    public Builder withDr(String dr) {
      this.dr = dr;
      return this;
    }

    public Builder withDevNorthVelocity(String devNorthVelocity) {
      this.devNorthVelocity = devNorthVelocity;
      return this;
    }

    public Builder withDevEastVelocity(String devEastVelocity) {
      this.devEastVelocity = devEastVelocity;
      return this;
    }

    public Builder withAsID(String asID) {
      this.asID = asID;
      return this;
    }

    public Builder withMcc(String mcc) {
      this.mcc = mcc;
      return this;
    }

    public Builder withTss(String tss) {
      this.tss = tss;
      return this;
    }

    public Builder withLcn(String lcn) {
      this.lcn = lcn;
      return this;
    }

    public Builder withLsc(String lsc) {
      this.lsc = lsc;
      return this;
    }

    public Builder withLfc(String lfc) {
      this.lfc = lfc;
      return this;
    }

    public Builder withRt(String rt) {
      this.rt = rt;
      return this;
    }

    public Builder withRr(String rr) {
      this.rr = rr;
      return this;
    }

    public Builder withAfCntDown(String afCntDown) {
      this.afCntDown = afCntDown;
      return this;
    }

    public Builder withNfCntDown(String nfCntDown) {
      this.nfCntDown = nfCntDown;
      return this;
    }

    public Builder withConfAFCntDown(String confAFCntDown) {
      this.confAFCntDown = confAFCntDown;
      return this;
    }

    public Builder withConfNFCntDown(String confNFCntDown) {
      this.confNFCntDown = confNFCntDown;
      return this;
    }

    public Builder withConfFCntUp(String confFCntUp) {
      this.confFCntUp = confFCntUp;
      return this;
    }

    public Builder withLfd(List<Lfd> lfd) {
      this.lfd = lfd;
      return this;
    }

    public Builder withTs(String ts) {
      this.ts = ts;
      return this;
    }

    public Builder withRul(String rul) {
      this.rul = rul;
      return this;
    }

    public Builder withFreq(String freq) {
      this.freq = freq;
      return this;
    }

    public Builder withClazz(String clazz) {
      this.clazz = clazz;
      return this;
    }

    public Builder withBper(String bper) {
      this.bper = bper;
      return this;
    }

    public Builder withNotifTp(String notifTp) {
      this.notifTp = notifTp;
      return this;
    }

    public Builder withResetTp(String resetTp) {
      this.resetTp = resetTp;
      return this;
    }

    public Builder withPayloadDriverId(String payloadDriverId) {
      this.payloadDriverId = payloadDriverId;
      return this;
    }

    public Builder withPayloadDriverModel(String payloadDriverModel) {
      this.payloadDriverModel = payloadDriverModel;
      return this;
    }

    public Builder withPayloadDriverApplication(String payloadDriverApplication) {
      this.payloadDriverApplication = payloadDriverApplication;
      return this;
    }

    public Builder withPayloadDecodingError(String payloadDecodingError) {
      this.payloadDecodingError = payloadDecodingError;
      return this;
    }

    public Builder withLcMU(String lcMU) {
      this.lcMU = lcMU;
      return this;
    }

    public Builder withPayloadEncryption(String payloadEncryption) {
      this.payloadEncryption = payloadEncryption;
      return this;
    }

    public Builder withTags(String tags) {
      this.tags = tags;
      return this;
    }

    public Builder withRfRegion(String rfRegion) {
      this.rfRegion = rfRegion;
      return this;
    }

    public Builder withForeignOperatorNSID(String foreignOperatorNSID) {
      this.foreignOperatorNSID = foreignOperatorNSID;
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

    public DecodedLoraHistory build() {
      return new DecodedLoraHistory(this);
    }
  }
}
