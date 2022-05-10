package com.actility.thingpark.wlogger.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.entity.history.Utils;
import com.actility.thingpark.wlogger.model.DecodedLoraHistory;
import com.actility.thingpark.wlogger.model.DecodedLteHistory;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wloggerwrap.WLWrapper;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.actility.thingpark.wlogger.service.DecoderService.DECODER_AUTOMATIC;
import static com.actility.thingpark.wlogger.service.DecoderService.DECODER_RAW;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ApplicationScoped
public class CSVService {

  public static final int LRR_MAX = 10;
  public static final int RECIPIENT_MAX = 5;

  private final DecoderService decoderService;

  private static final Logger logger = Logger.getLogger(CSVService.class.getName());

  @Inject
  public CSVService(final DecoderService decoderService) {
    this.decoderService = decoderService;
  }

  @Nonnull
  static String[] getHeadersDeviceHistoryLte() {
    List<String> list =
            Lists.newArrayList(
      "Direction",
      "Timestamp",
      "IMEI",
      "IPV4",
      "LRC Id",
      "MTC",
      "Device Lat",
      "Device Long",
      "MType",
      "Payload(hex)",
      "Late",
      "Cause",
      "EBI",
      "APN",
      "MSISDN",
      "IMSI",
      "RAT",
      "CELLID",
      "CELLTAC",
      "SERVNET",
      "MCCMNC",
      // NFR 724
      "ASID",
      // NFR 1012
      "Mf uplink bytes",
      "Mf downlink bytes",
      "Duration of mf");

    list.add("asDeliveryStatus");
    for (int i = 0; i < RECIPIENT_MAX; i++) {
      list.addAll(getHeadersRecipient(i + 1));
    }

    return list.toArray(new String[0]);
  }

  @Nonnull
  static String getHeaderLrr(int i, String key) {
    return format("LRR[%d] %s", i, key);
  }

  @Nonnull
  static String getHeaderRecipient(int i, String key) {
    return format("asRecipients[%d] %s", i, key);
  }

  @Nonnull
  static List<String> getHeadersLrr(int i, boolean passiveRoamingActivated) {
    List<String> list =
        Lists.newArrayList(
            getHeaderLrr(i, "Id"),
            getHeaderLrr(i, "RSSI"),
            getHeaderLrr(i, "SNR"),
            getHeaderLrr(i, "ESP"),
            getHeaderLrr(i, "CHAINS"),
            getHeaderLrr(i, "ISM Band"),
            getHeaderLrr(i, "RF Region"));
    // NFR 809
    if (passiveRoamingActivated) {
      list.addAll(
          Lists.newArrayList(
              getHeaderLrr(i, "GWID"),
              getHeaderLrr(i, "GWToken"),
              getHeaderLrr(i, "DLAllowed"),
              getHeaderLrr(i, "ForeignOperatorNetID"),
              getHeaderLrr(i, "ForeignOperatorNSID")));
    }
    return list;
  }

  @Nonnull
  static List<String> getHeadersRecipient(int i) {
    return Lists.newArrayList(
                    getHeaderRecipient(i, "ID"),
                    getHeaderRecipient(i, "status"),
//                    getHeaderRecipient(i, "dropped"),
                    getHeaderRecipient(i, "destinations"));
  }

  @Nonnull
  String[] getHeadersDeviceHistoryLora(@Nullable String decoder, boolean passiveRoamingActivated) {
    List<String> list =
        Lists.newArrayList(
            "Direction",
            "Timestamp",
            "Timestamp(ISO)",
            "DevEUI",
            "DevAddr",
            "FPort",
            "FCnt",
            "LRR RSSI",
            "LRR SNR",
            "LRR ESP",
            "SpFact",
            "SubBand",
            "Channel",
            "LRC Id",
            "LRR Id",
            "LRR Lat",
            "LRR Long",
            "LRR Count",
            "Device Lat",
            "Device Long",
            "LoS distance",
            "Device Loc radius",
            "Device Loc Time",
            "Device Alt",
            "Device Alt radius",
            "Device Acc");
    for (int i = 0; i < LRR_MAX; i++) {
      list.addAll(getHeadersLrr(i + 1, passiveRoamingActivated));
    }
    list.addAll(
        Lists.newArrayList(
            "MAC(hex)",
            "MType",
            "ADR",
            "ADRACkReq",
            "Ack",
            "FPending(downlink)",
            "AirTime",
            "MIC",
            "Payload(hex)",
            "FCntUp",
            "FCntDn",
            "DownlinkStatus",
            "DownlinkFailedCauseOnRX1",
            "DownlinkFailedCauseOnRX2",
            "DownlinkFailedCauseOnPingSlot",
            "Late",
            "SolvLAT",
            "SolvLON",
            "PayloadSize"));
    // NFR 809 312 948
    if (passiveRoamingActivated) {
      list.addAll(
          Lists.newArrayList(
              "BestGWID", "BestGWToken", "ForeignOperatorNetID", "RoamingType", "RoamingResult"));
    }
    list.addAll(
        Lists.newArrayList(
            "ISMBand",
            // NFR 887
            "Modulation",
            "DataRate",
            "SubID",
            // NFR 791
            "Device North velocity",
            "Device East velocity",
            // NFR 724
            "ASID",
            // NFR 1036
            "Multicast class",
            "Transmission status",
            "LRRs count",
            "LRRs successfully transmitted",
            "LRRs failed transmitted",
            // NFR 2215
            "Delivery Failed Cause / Number of occurrences",
            // NFR 1061 1062
            "AF count down",
            "NF count down",
            "Conf AF count down",
            "Conf NF count down",
            "Conf F count up",
            // NFR 2543
            "Transmission slot",
            // NFR 5787
            "Repeated UL",
            // NFR 7009
            "Frequency",
            // NFR 2210
            "Class",
            "Ping-slot period",
            // NFR 6024
            "Type of notification",
            "Type of device reset",
            // NFR 3156
            "Device location method used",
            // RDTP-10309
            "Payload encryption",
            // RDTP-2274
            "Tags",
            "ForeignOperatorNSID",
            "RF Region"));

    list.add("asDeliveryStatus");
    for (int i = 0; i < RECIPIENT_MAX; i++) {
      list.addAll(getHeadersRecipient(i + 1));
    }

    if (decoder != null && !decoder.equals(DECODER_RAW)) {
      if (decoder.equals(DECODER_AUTOMATIC)) {
        list.addAll(
            Lists.newArrayList(
                "Decoded payload",
                "Driver ID",
                "Driver model",
                "Driver application",
                "Decoding error"));
      } else {
        // JUST CHECK IF IT EXIST
        WLWrapper wrap = null;
        try {
          wrap = decoderService.getWLWrapper(decoder);
        } catch (Exception ex) {
          logger.log(Level.INFO, "Decoder error : " + ex.getMessage(), ex);
        }

        if (wrap != null) {
          list.addAll(
              Arrays.stream(wrap.getCSVHeader("/#/").split("/#/")).collect(Collectors.toList()));
        }
      }
    }
    return list.toArray(new String[0]);
  }

  static String[] convertDeviceHistoryLteToCsvLine(@Nonnull DecodedLteHistory deviceLteHistory) {
    List<String> list =
            Lists.newArrayList(
      Direction.getValueOrEmpty(deviceLteHistory.getDirection()),
      deviceLteHistory.getTimestampAsD(),
      deviceLteHistory.getLteIMEI(),
      deviceLteHistory.getDevAddr(),
      deviceLteHistory.getLrcId(),
      deviceLteHistory.getLrrid(),
      Utils.getToString(deviceLteHistory.getDevLAT()),
      Utils.getToString(deviceLteHistory.getDevLON()),
      deviceLteHistory.getmType(),
      Utils.getToStringOrNull(deviceLteHistory.getPayloadHex()),
      Utils.getEmptyIfNull(deviceLteHistory.getLate()),
      deviceLteHistory.getLteCause(),
      deviceLteHistory.getLteEBI(),
      deviceLteHistory.getLteAPN(),
      deviceLteHistory.getLteMSISDN(),
      deviceLteHistory.getLteIMSI(),
      deviceLteHistory.getLteRAT(),
      deviceLteHistory.getLteCELLID(),
      deviceLteHistory.getLteCELLTAC(),
      deviceLteHistory.getLteSERVNET(),
      deviceLteHistory.getLteMCCMNC(),
      // NFR 724
      deviceLteHistory.getAsID(),
      // NFR 1012
      deviceLteHistory.getMfSzU(),
      deviceLteHistory.getMfSzD(),
      deviceLteHistory.getMfDur());

    list.add(deviceLteHistory.getDeliveryStatus());
    list.addAll(deviceLteHistory.getRecipientListAsCsv(RECIPIENT_MAX));

    return list.toArray(new String[0]);
  }

  String[] convertDeviceHistoryLoraToCsvLine(
      @Nonnull DecodedLoraHistory deviceLoraHistory,
      @Nullable String decoder,
      boolean passiveRoaming) {
    List<String> list =
        Lists.newArrayList(
            Direction.getValueOrEmpty(deviceLoraHistory.getDirection()),
            deviceLoraHistory.getTimestampAsD(),
            deviceLoraHistory.getTimestampAsISO(),
            deviceLoraHistory.getDevEUI(),
            deviceLoraHistory.getDevAddr(),
            deviceLoraHistory.getfPort(),
            deviceLoraHistory.getfCnt(),
            Utils.getEmptyIfNull(deviceLoraHistory.getLrrRSSI()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLrrSNR()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLrrESP()),
            Utils.getToStringOrNull(deviceLoraHistory.getSpFact()),
            Utils.getToStringOrNull(deviceLoraHistory.getSubBand()),
            Utils.getToStringOrNull(deviceLoraHistory.getChannel()),
            Utils.getToStringOrNull(deviceLoraHistory.getLrcId()),
            Utils.getToStringOrNull(deviceLoraHistory.getLrrid()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLrrLAT()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLrrLON()),
            Utils.getEmptyIfNull(deviceLoraHistory.getDevLrrCnt()),
            Utils.getToString(deviceLoraHistory.getDevLAT()),
            Utils.getToString(deviceLoraHistory.getDevLON()),
            deviceLoraHistory.getDistance(),
            Utils.getEmptyIfNull(deviceLoraHistory.getDevLocRadius()),
            Utils.getEmptyIfNull(deviceLoraHistory.getDevLocTime()),
            deviceLoraHistory.getDevAlt(),
            deviceLoraHistory.getDevAltRadius(),
            deviceLoraHistory.getDevAcc());
    list.addAll(deviceLoraHistory.getLrrListAsCsv(passiveRoaming, LRR_MAX));
    list.addAll(
            Arrays.asList(
                    Utils.getToStringOrNull(deviceLoraHistory.getRawMacCommands()),
                    deviceLoraHistory.getmType(),
                    deviceLoraHistory.getAdRbit(),
                    deviceLoraHistory.getAdrAckReq(),
                    // 0004196: [N3 Project Queue][9119]: Wrong value of ACK bits in Wlogger export
                    deviceLoraHistory.getAcKbit()));
    boolean directionZero = Direction.UPLINK.equals(deviceLoraHistory.getDirection());
    if (directionZero) {
      list.addAll(
          Arrays.asList(
              "-",
              deviceLoraHistory.getAirTime(),
              Utils.getToStringOrNull(deviceLoraHistory.getMicHex()),
              Utils.getToStringOrNull(deviceLoraHistory.getPayloadHex()),
              deviceLoraHistory.getfCnt(),
              "-",
              "-",
              "-",
              "-",
              "-",
              Utils.getEmptyIfNull(deviceLoraHistory.getLate()),
              Utils.getEmptyIfNull(deviceLoraHistory.getSolvLAT()),
              Utils.getEmptyIfNull(deviceLoraHistory.getSolvLON())));
    } else {
      list.addAll(
          Arrays.asList(
              deviceLoraHistory.getfPending(),
              deviceLoraHistory.getAirTime(),
              Utils.getToStringOrNull(deviceLoraHistory.getMicHex()),
              Utils.getToStringOrNull(deviceLoraHistory.getPayloadHex()),
              "-",
              deviceLoraHistory.getfCnt(),
              Utils.getToStringOrNull(deviceLoraHistory.getDlStatus()),
              Utils.getToStringOrNull(deviceLoraHistory.getDlFailedCause1()),
              Utils.getToStringOrNull(deviceLoraHistory.getDlFailedCause2()),
              Utils.getToStringOrNull(deviceLoraHistory.getDlFailedCause3()),
              "-",
              "-",
              "-"));
    }
    list.add(deviceLoraHistory.getPayloadSize());
    // NFR 809 312 948
    if (passiveRoaming) {
      list.addAll(
          Arrays.asList(
              Utils.getEmptyIfNull(deviceLoraHistory.getGwID()),
              Utils.getEmptyIfNull(deviceLoraHistory.getGwTk()),
              Utils.getEmptyIfNull(deviceLoraHistory.getGwOp()),
              Utils.getEmptyIfNull(deviceLoraHistory.getRt()),
              Utils.getEmptyIfNull(deviceLoraHistory.getRr())));
    }
    list.addAll(
        Arrays.asList(
            Utils.getEmptyIfNull(deviceLoraHistory.getIsmb()),
            // NFR 887
            Utils.getEmptyIfNull(deviceLoraHistory.getMod()),
            Utils.getEmptyIfNull(deviceLoraHistory.getDr()),
            Utils.getEmptyIfNull(deviceLoraHistory.getCustomerId()),
            // NFR 791
            Utils.getEmptyIfNull(deviceLoraHistory.getDevNorthVelocity()),
            Utils.getEmptyIfNull(deviceLoraHistory.getDevEastVelocity()),
            // NFR 724
            Utils.getEmptyIfNull(deviceLoraHistory.getAsID()),
            // NFR 1036
            Utils.getEmptyIfNull(deviceLoraHistory.getMcc()),
            Utils.getEmptyIfNull(deviceLoraHistory.getTss()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLcn()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLsc()),
            Utils.getEmptyIfNull(deviceLoraHistory.getLfc()),
            // NFR 2215
            Utils.getEmptyIfNull(deviceLoraHistory.getLfdAsCsvString()),
            // NFR 1061 1062
            Utils.getEmptyIfNull(deviceLoraHistory.getAfCntDown()),
            Utils.getEmptyIfNull(deviceLoraHistory.getNfCntDown()),
            Utils.getEmptyIfNull(deviceLoraHistory.getConfAFCntDown()),
            Utils.getEmptyIfNull(deviceLoraHistory.getConfNFCntDown()),
            Utils.getEmptyIfNull(deviceLoraHistory.getConfFCntUp()),
            // NFR 2543
            Utils.getEmptyIfNull(deviceLoraHistory.getTs()),
            // NFR 5787
            Utils.getEmptyIfNull(deviceLoraHistory.getRul()),
            // NFR 7009
            Utils.getEmptyIfNull(deviceLoraHistory.getFreq()),
            // NFR 2210
            Utils.getEmptyIfNull(deviceLoraHistory.getClazz()),
            Utils.getEmptyIfNull(deviceLoraHistory.getBper()),
            // NFR 6024
            Utils.getEmptyIfNull(deviceLoraHistory.getNotifTp()),
            Utils.getEmptyIfNull(deviceLoraHistory.getResetTp()),
            // NFR 3156
            Utils.getEmptyIfNull(deviceLoraHistory.getLcMU()),
            // RDTP-10309
            Utils.getEmptyIfNull(deviceLoraHistory.getPayloadEncryption()),
            // RDTP-2274
            Utils.getEmptyIfNull(deviceLoraHistory.getTags()),
            Utils.getEmptyIfNull(deviceLoraHistory.getForeignOperatorNSID()),
            Utils.getEmptyIfNull(deviceLoraHistory.getRfRegion())
        ));

    list.add(deviceLoraHistory.getDeliveryStatus());
    list.addAll(deviceLoraHistory.getRecipientListAsCsv(RECIPIENT_MAX));

    if (decoder != null && !decoder.equals(DECODER_RAW)) {
      if (decoder.equals(DECODER_AUTOMATIC)) {
        list.addAll(
            Arrays.asList(
                Utils.getEmptyIfNull(deviceLoraHistory.getPayloadDecoded()),
                Utils.getEmptyIfNull(deviceLoraHistory.getPayloadDriverId()),
                Utils.getEmptyIfNull(deviceLoraHistory.getPayloadDriverModel()),
                Utils.getEmptyIfNull(deviceLoraHistory.getPayloadDriverApplication()),
                Utils.getEmptyIfNull(deviceLoraHistory.getPayloadDecodingError())));
      } else {

        if (deviceLoraHistory.getPayloadDecoder() != null
                && deviceLoraHistory.getWrapper() != null
                && directionZero
                && isNotBlank(deviceLoraHistory.getPayloadHex())) {
          list.addAll(Arrays.asList(deviceLoraHistory.getWrapper().getCSVLine("/#/").split("/#/")));
        }
      }
    }
    return list.toArray(new String[0]);
  }

  private void writeCsv(Writer writer, char delimiter, String[] headers, List<String[]> content) throws IOException {
      CSVWriter csvWriter = new CSVWriter(writer, delimiter);
      try {
        csvWriter.writeNext(headers);
        content.forEach(csvWriter::writeNext);
      } finally {
        csvWriter.close();
      }
  }

  private void writeCsv(Writer writer, char delimiter, String[] headers, MongoCursor<DeviceHistory> cursor,
                        Search search,
                        boolean passiveRoaming,
                        String netPartID) throws IOException {
    CSVWriter csvWriter = new CSVWriter(writer, delimiter);
    try {
      csvWriter.writeNext(headers);
      if (cursor != null) {
        while (cursor.hasNext()) {
          DeviceHistory devHistory = cursor.next();
          DecodedLoraHistory dev =
              this.decoderService.decodeLora(devHistory, search, passiveRoaming);
          String[] content = convertDeviceHistoryLoraToCsvLine(dev, search.getDecoder(), passiveRoaming);
          csvWriter.writeNext(content);
        }
      }
    } finally {
      csvWriter.close();
    }
  }

  public void writeLTECsv(Writer writer, char delimiter, List<DecodedLteHistory> deviceLteHistory) throws IOException {
    this.writeCsv(
        writer,
        delimiter,
        getHeadersDeviceHistoryLte(),
        deviceLteHistory.stream()
            .map(CSVService::convertDeviceHistoryLteToCsvLine)
            .collect(Collectors.toList()));
  }

  public void writeLoraCsv(
      Writer writer,
      char delimiter,
      List<DecodedLoraHistory> deviceLoraHistory,
      String decoder,
      boolean passiveRoaming) throws IOException {
    this.writeCsv(
        writer,
        delimiter,
        getHeadersDeviceHistoryLora(decoder, passiveRoaming),
        deviceLoraHistory.stream()
            .map(
                devHistory ->
                    convertDeviceHistoryLoraToCsvLine(devHistory, decoder, passiveRoaming))
            .collect(Collectors.toList()));
  }

  public void writeLoraCsv(
          Writer writer,
          char delimiter,
          MongoCursor<DeviceHistory> cursor,
          Search search,
          boolean passiveRoaming) throws IOException {
    this.writeCsv(
        writer,
        delimiter,
        getHeadersDeviceHistoryLora(search.getDecoder(), passiveRoaming),
        cursor,
        search,
        passiveRoaming,
        search.getNetPartId());
  }

}
