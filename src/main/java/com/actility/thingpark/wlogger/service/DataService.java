package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.config.PaginationConfig;
import com.actility.thingpark.wlogger.config.WloggerConfig;
import com.actility.thingpark.wlogger.dao.DeviceHistoryDao;
import com.actility.thingpark.wlogger.dto.*;
import com.actility.thingpark.wlogger.entity.history.Utils;
import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.model.DecodedLoraHistory;
import com.actility.thingpark.wlogger.model.DecodedLteHistory;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wlogger.response.ResponseFactory;
import com.mongodb.client.MongoCursor;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ApplicationScoped
public class DataService {

  private static final Logger logger = Logger.getLogger(DataService.class.getName());
  private final DeviceHistoryDao deviceHistoryDao;
  private final CSVService csvService;
  private final DecoderService decoderService;
  private final PaginationConfig paginationConfig;
  private final WloggerConfig wloggerConfig;

  @Inject
  public DataService(
          final DeviceHistoryDao deviceHistoryDao,
          final CSVService csvService,
          final DecoderService decoderService,
          final PaginationConfig paginationConfig,
          final WloggerConfig wloggerConfig) {
    this.deviceHistoryDao = deviceHistoryDao;
    this.csvService = csvService;
    this.decoderService = decoderService;
    this.paginationConfig = paginationConfig;
    this.wloggerConfig = wloggerConfig;
  }

  private boolean hasMore(List<?> list, int pageSize, int pageIndex) {
    if (list.size() <= pageSize) {
      return false;
    }
    while (list.size() > pageSize) {
      list.remove(list.size() - 1);
    }
    //    list = list.subList(0, pageSize);
    return pageIndex <= this.paginationConfig.maxPages();
  }

  private ResponsePaginatedList getResponse(
      Search search, String apiType, boolean forDevicesLocation, int pageSize, String subscriberId, String realmId) throws WloggerException {
    List<DeviceHistory> list =
        this.deviceHistoryDao.get(search, (search.getPageIndex() - 1) * pageSize, pageSize + 1);
    logger.warning("Search count : " + list.size());
    ResponsePaginatedList datas =
        ResponseFactory.createSuccessResponseDatas()
            .withMore(hasMore(list, pageSize, search.getPageIndex()));
    logger.warning("Search count limit : " + list.size());

    switch (search.getType()) {
      case LTE:
        return datas.withData(this.getLteResponse(search.getDecoder(), list, apiType, search.getNetPartId()));
      case LORA:
        return datas.withData(
            this.getLoraResponse(search, list, apiType, forDevicesLocation, subscriberId, realmId));
      default:
        throw new IllegalArgumentException("unknown device type: " + search.getType());
    }
  }

  private List<Element> getLteResponse(String decoder, List<DeviceHistory> list, String apiType, String netPartID) {
    return list.stream()
        .map(deviceHistory -> this.decoderService.decodeLte(deviceHistory, decoder, netPartID))
        .map(
            decodedHistory -> {
              if (apiType.equals("api")) {
                return apiExtract(decodedHistory);
              }
              return standardExtract(decodedHistory);
            })
        .collect(toList());
  }

  private List<Element> getLoraResponse(
      Search search, List<DeviceHistory> list, String apiType, boolean forDevicesLocation,
      String subscriberId, String realmId) {
    return this.decoderService.decodeLoraFrames(list, search, this.wloggerConfig.passiveRoaming(),
            this.paginationConfig.pageSize(), this.wloggerConfig.maxAutomaticDecodedPackets(), 0, subscriberId, realmId).stream()
        .filter(decodedLoraHistory -> !forDevicesLocation || decodedLoraHistory.isLocationValid())
        .map(
            decodedLoraHistory -> {
              if (apiType.equals("api")) {
                return apiExtract(decodedLoraHistory);
              }
              return standardExtract(decodedLoraHistory);
            })
        .collect(toList());
  }

  public ResponseSimple data(Search search, String subscriberId, String realmId) throws WloggerException {
    logger.warning(search.toString());
    return getResponse(search, "gui", false, this.paginationConfig.pageSize(), subscriberId, realmId);
  }

  private int getPageSize(Search search) {
    int confPageSize = this.paginationConfig.pageSize();
    int maxPages = this.paginationConfig.maxPages();
    int pageSize = confPageSize;
    if (isNotBlank(search.getLast())) {
      try {
        pageSize = Integer.parseInt(search.getLast());
        if (pageSize >= confPageSize * maxPages) {
          pageSize = confPageSize * maxPages;
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return pageSize;
  }

  public ResponseSimple extract(Search search, String subscriberId, String realmId) throws WloggerException {
    // control last
    logger.warning(search.toString());
    return getResponse(search, "api", false, getPageSize(search), subscriberId, realmId);
  }

  public Response export(Search search, String subscriberId, String realmId) throws WloggerException {
    logger.warning(search.toString());
    // control last
    int pageSize = this.getPageSize(search);

    StreamingOutput stream;
    List<DeviceHistory> list;
    switch (search.getType()) {
      case LTE:
        list = this.deviceHistoryDao.get(search, (search.getPageIndex() - 1) * pageSize, pageSize);
        logger.warning("Search count : " + list.size());
        stream = this.exportLte(search.getDecoder(), list, search.getNetPartId());
        break;
      case LORA:
        // Automatic Decoder
        if (this.decoderService.DECODER_AUTOMATIC.equals(search.getDecoder())) {

          if (pageSize > this.wloggerConfig.maxAutomaticDecodedPackets() || this.paginationConfig.pageSize() > this.wloggerConfig.maxAutomaticDecodedPackets()) {

            logger.log(Level.INFO, "No IOT Flow decoding : export is too large or page size is too large");
            MongoCursor<DeviceHistory> cursor = this.deviceHistoryDao.getIterable(search, (search.getPageIndex() - 1) * pageSize, pageSize);
            stream = this.exportLora(search, cursor);

          } else {

            list = this.deviceHistoryDao.get(search, (search.getPageIndex() - 1) * pageSize, pageSize);
            logger.warning("Search count : " + list.size());
            stream = this.exportLora(search, list, pageSize, subscriberId, realmId);

          }

        } else { // Others decoder

          logger.log(Level.INFO, "No IOT Flow decoding : other decoder");
          MongoCursor<DeviceHistory> cursor = this.deviceHistoryDao.getIterable(search, (search.getPageIndex() - 1) * pageSize, pageSize);
          stream = this.exportLora(search, cursor);

        }

        break;
      default:
        throw new IllegalArgumentException("unknown device type: " + search.getType());
    }
    return Response.ok(stream)
        .header("Content-Disposition", "attachment; filename=export.csv")
        .build();
  }

  private StreamingOutput exportLora(Search search, MongoCursor<DeviceHistory> cursor) {
    return os -> {
      Writer writer = new BufferedWriter(new OutputStreamWriter(os));
      try {
        csvService.writeLoraCsv(
            writer,
            this.wloggerConfig.csvExportDelimiter().charAt(0),
            cursor,
            search,
            this.wloggerConfig.passiveRoaming());
      } finally {
        writer.flush();
        writer.close();
        if (cursor != null) {
          cursor.close();
        }
      }
    };
  }

    private StreamingOutput exportLora(Search search, List<DeviceHistory> list, int exportSize,
                                     String subscriberId, String realmId) {
    return os -> {
      Writer writer = new BufferedWriter(new OutputStreamWriter(os));
      try {
        csvService.writeLoraCsv(
            writer,
            this.wloggerConfig.csvExportDelimiter().charAt(0),
            this.decoderService.decodeLoraFrames(
                list,
                search,
                this.wloggerConfig.passiveRoaming(),
                this.paginationConfig.pageSize(),
                this.wloggerConfig.maxAutomaticDecodedPackets(),
                exportSize,
                subscriberId,
                realmId),
            search.getDecoder(),
            this.wloggerConfig.passiveRoaming());
      } finally {
        writer.flush();
        writer.close();
      }
    };
  }

  private StreamingOutput exportLte(String decoder, List<DeviceHistory> list, String netPartID) {
    return os -> {
      Writer writer = new BufferedWriter(new OutputStreamWriter(os));
      try {
        csvService.writeLTECsv(
            writer,
            this.wloggerConfig.csvExportDelimiter().charAt(0),
            list.stream()
                .map(history -> this.decoderService.decodeLte(history, decoder, netPartID))
                .collect(toList()));
      } finally {
        writer.flush();
        writer.close();
      }
    };
  }

  public ResponseSimple devicesLocations(Search search, String subscriberId, String realmId) throws WloggerException {
    logger.warning(search.toString());
    return getResponse(search, "gui", true, this.paginationConfig.pageSize(), subscriberId, realmId);
  }

  private ElementDeviceHistoryLteExtract apiExtract(DecodedLteHistory decodedLteHistory) {
    ElementDeviceHistoryLteExtract.Builder builder =
        ElementDeviceHistoryLteExtract.builder()
            .timestampUTC(decodedLteHistory.getTimestampAsISO())
            .direction(Direction.getValueOrEmpty(decodedLteHistory.getDirection()))
            .mType(decodedLteHistory.getmType())
            .mTypeText(decodedLteHistory.getmTypeText());
    if (decodedLteHistory.getDevAddr().length() > 0) {
      builder.ipv4(decodedLteHistory.getDevAddr());
    }
    if (decodedLteHistory.getLteIMEI().length() > 0) {
      builder.imei(decodedLteHistory.getLteIMEI());
    }
    builder
        .payloadHex(decodedLteHistory.getPayloadHex())
        // .PayloadSize(data.getPayloadSize());
        .mtc(decodedLteHistory.getLrrid())
        .lrcId(decodedLteHistory.getLrcId());
    if (!decodedLteHistory.getLate().equals("null")) {
      builder.late(decodedLteHistory.getLate());
    }
    if (decodedLteHistory.getLteEBI().length() > 0) {
      builder.ebi(decodedLteHistory.getLteEBI());
    }
    if (decodedLteHistory.getLteAPN().length() > 0) {
      builder.apn(decodedLteHistory.getLteAPN());
    }
    if (decodedLteHistory.getLteMSISDN().length() > 0) {
      builder.msisdn(decodedLteHistory.getLteMSISDN());
    }
    if (decodedLteHistory.getLteIMSI().length() > 0) {
      builder.imsi(decodedLteHistory.getLteIMSI());
    }
    if (decodedLteHistory.getLteRAT().length() > 0) {
      builder.rat(decodedLteHistory.getLteRAT());
    }
    if (decodedLteHistory.getLteCELLID().length() > 0) {
      builder.cellid(decodedLteHistory.getLteCELLID());
    }
    if (decodedLteHistory.getLteCELLTAC().length() > 0) {
      builder.celltac(decodedLteHistory.getLteCELLTAC());
    }
    if (decodedLteHistory.getLteSERVNET().length() > 0) {
      builder.servnet(decodedLteHistory.getLteSERVNET());
    }
    if (decodedLteHistory.getLteMCCMNC().length() > 0) {
      builder.mccmnc(decodedLteHistory.getLteMCCMNC());
    }

    if (Direction.UPLINK.equals(decodedLteHistory.getDirection())) {
      if (decodedLteHistory.getDevLAT() != null)
        builder.devLAT(decodedLteHistory.getDevLAT().toString());
      if (decodedLteHistory.getDevLON() != null)
        builder.devLON(decodedLteHistory.getDevLON().toString());
    }
    if (decodedLteHistory.getmType().equals("2")) {
      builder.cause(decodedLteHistory.getLteCause());
    }
    // NFR 724
    builder
        .asID(decodedLteHistory.getAsID())
        // NFR 1012
        .mfSzU(decodedLteHistory.getMfSzU())
        .mfSzD(decodedLteHistory.getMfSzD())
        .mfDur(decodedLteHistory.getMfDur())

        .asReportDeliveryID(decodedLteHistory.getAsReportDeliveryID())
        .asRecipients(decodedLteHistory.getRecipientListAsElements())
        .asDeliveryStatus(decodedLteHistory.getDeliveryStatus());

    return builder.build();
  }

  private ElementDeviceHistoryLte standardExtract(DecodedLteHistory decodedLteHistory) {
    return ElementDeviceHistoryLte.builder()
        .uid(decodedLteHistory.getUid())
        .direction(Direction.getValueOrEmpty(decodedLteHistory.getDirection()))
        .timestamp(decodedLteHistory.getTimestampAsD())
        .timestampUTC(decodedLteHistory.getTimestampAsUTC())
        .devEUI(decodedLteHistory.getDevEUI())
        .devAddr(decodedLteHistory.getDevAddr())
        .payloadHex(decodedLteHistory.getPayloadHex())
        .lrrid(decodedLteHistory.getLrrid())
        .lrcId(decodedLteHistory.getLrcId())
        .devLAT(Utils.getToStringOrNull(decodedLteHistory.getDevLAT()))
        .devLON(Utils.getToStringOrNull(decodedLteHistory.getDevLON()))
        .mType(decodedLteHistory.getmType())
        .mTypeText(decodedLteHistory.getmTypeText())
        .hasPayload(decodedLteHistory.getHasPayload())
        .payloadDecoded(decodedLteHistory.getPayloadDecoded())
        .late(decodedLteHistory.getLate())
        .payloadSize(decodedLteHistory.getPayloadSize())
        .lteIPPacketSize(decodedLteHistory.getLteIPPacketSize())
        .ipv4Decoded(decodedLteHistory.getIpv4Decoded())
        .ltePacketProtocol(decodedLteHistory.getLtePacketProtocol())
        .lteCause(decodedLteHistory.getLteCause())
        .lteEBI(decodedLteHistory.getLteEBI())
        .lteAPN(decodedLteHistory.getLteAPN())
        .lteMSISDN(decodedLteHistory.getLteMSISDN())
        .lteIMSI(decodedLteHistory.getLteIMSI())
        .lteRAT(decodedLteHistory.getLteRAT())
        .lteCELLID(decodedLteHistory.getLteCELLID())
        .lteCELLTAC(decodedLteHistory.getLteCELLTAC())
        .lteSERVNET(decodedLteHistory.getLteSERVNET())
        .lteMCCMNC(decodedLteHistory.getLteMCCMNC())
        .lteIMEI(decodedLteHistory.getLteIMEI())
        .asID(decodedLteHistory.getAsID())
        .mfSzU(decodedLteHistory.getMfSzU())
        .mfSzD(decodedLteHistory.getMfSzD())
        .mfDur(decodedLteHistory.getMfDur())
        .asReportDeliveryID(decodedLteHistory.getAsReportDeliveryID())
        .asRecipients(decodedLteHistory.getRecipientListAsHtmlTable())
        .asDeliveryStatus(decodedLteHistory.getDeliveryStatus())
        .build();
  }

  private ElementDeviceHistoryExtract apiExtract(DecodedLoraHistory decodedLoraHistory) {
    ElementDeviceHistoryExtract.Builder builder =
        ElementDeviceHistoryExtract.builder()
            .timestampUTC(decodedLoraHistory.getTimestampAsISO())
            .direction(Direction.getValueOrEmpty(decodedLoraHistory.getDirection()))
            .devAddr(decodedLoraHistory.getDevAddr())
            .devEUI(decodedLoraHistory.getDevEUI())
            .lrcId(decodedLoraHistory.getLrcId())
            // NFR 791 (0,1,2)
            .fCnt(decodedLoraHistory.getfCnt());

    if (!Direction.LOCATION.equals(decodedLoraHistory.getDirection())) {
      builder
          .acKbit(decodedLoraHistory.getAcKbit())
          .adrAckReq(decodedLoraHistory.getAdrAckReq())
          .adRbit(decodedLoraHistory.getAdRbit())
          .ackRequested(decodedLoraHistory.getAckRequested())
          .airTime(decodedLoraHistory.getAirTime())
          .mType(decodedLoraHistory.getmType())
          .mTypeText(decodedLoraHistory.getmTypeText())
          .fPort(decodedLoraHistory.getfPort())
          .payloadHex(decodedLoraHistory.getPayloadHex())
          .payloadSize(decodedLoraHistory.getPayloadSize());
      // RDTP-10309
      if (!decodedLoraHistory.getPayloadEncryption().equals("null"))
        builder.payloadEncryption(decodedLoraHistory.getPayloadEncryption());

      if (decodedLoraHistory.getPayloadDecoded() != null
          && decodedLoraHistory.getPayloadDecoded().length() > 0) {
        builder.payloadDecoded(decodedLoraHistory.getPayloadDecoded());
      }
      builder.rawMacCommands(decodedLoraHistory.getRawMacCommands());
      if (decodedLoraHistory.getMacDecoded() != null
          && decodedLoraHistory.getMacDecoded().length() > 0) {
        builder.macDecoded(decodedLoraHistory.getMacDecoded());
      }

      builder
          .micHex(decodedLoraHistory.getMicHex())
          .spFact(decodedLoraHistory.getSpFact())
          .subBand(decodedLoraHistory.getSubBand())
          .channel(decodedLoraHistory.getChannel())
          .lrrid(decodedLoraHistory.getLrrid());
      if (!decodedLoraHistory.getLrrRSSI().equals("null"))
        builder.lrrRSSI(decodedLoraHistory.getLrrRSSI());
      if (!decodedLoraHistory.getLrrSNR().equals("null"))
        builder.lrrSNR(decodedLoraHistory.getLrrSNR());
      if (!decodedLoraHistory.getLrrESP().equals("null"))
        builder.lrrESP(decodedLoraHistory.getLrrESP());
    }

    if (Direction.DOWNLINK.equals(decodedLoraHistory.getDirection())
        || Direction.MULTICAST_SUMMARY.equals(decodedLoraHistory.getDirection())) { // DOWN
      builder
          .fPending(decodedLoraHistory.getfPending())
          .rx1RX2Delay(decodedLoraHistory.getLrcRequestedRxDelay());
      if (StringUtils.isNotEmpty(decodedLoraHistory.getDlStatus()) && !decodedLoraHistory.getDlStatus().equals("null")) {
        builder
            .dlStatus(decodedLoraHistory.getDlStatus())
            .dlStatusText(decodedLoraHistory.getDlStatusText());

        if (!decodedLoraHistory.getDlStatus().equals("1")) {
          if (StringUtils.isNotEmpty(decodedLoraHistory.getDlFailedCause1())) {
            builder
                .dlFailedCause1(decodedLoraHistory.getDlFailedCause1())
                .dlFailedCause1Text(decodedLoraHistory.getDlFailedCause1Text());
          }
          if (StringUtils.isNotEmpty(decodedLoraHistory.getDlFailedCause2())) {
            builder
                .dlFailedCause2(decodedLoraHistory.getDlFailedCause2())
                .dlFailedCause2Text(decodedLoraHistory.getDlFailedCause2Text());
          }
          if (StringUtils.isNotEmpty(decodedLoraHistory.getDlFailedCause3())) {
            builder
                .dlFailedCause3(decodedLoraHistory.getDlFailedCause3())
                .dlFailedCause3Text(decodedLoraHistory.getDlFailedCause3Text());
          }
        }
      }
      // NFR 1036
      builder.mcc(decodedLoraHistory.getMcc());
    } else { // UP or Location
      if (!decodedLoraHistory.getLrrLAT().equals("null"))
        builder.lrrLAT(decodedLoraHistory.getLrrLAT());
      if (!decodedLoraHistory.getLrrLON().equals("null"))
        builder.lrrLON(decodedLoraHistory.getLrrLON());
      if (!decodedLoraHistory.getSolvLAT().equals("null"))
        builder.solvLAT(decodedLoraHistory.getSolvLAT());
      if (!decodedLoraHistory.getSolvLON().equals("null"))
        builder.solvLON(decodedLoraHistory.getSolvLON());
      if (decodedLoraHistory.getDevLAT() != null)
        builder.devLAT(decodedLoraHistory.getDevLAT().toString());
      if (decodedLoraHistory.getDevLON() != null)
        builder.devLON(decodedLoraHistory.getDevLON().toString());
      if (!decodedLoraHistory.getDevLocRadius().equals("null"))
        builder.devLocRadius(decodedLoraHistory.getDevLocRadius());
      if (!decodedLoraHistory.getDevLocTime().equals("null"))
        builder.devLocTime(decodedLoraHistory.getDevLocTime());
      if (decodedLoraHistory.getDevAlt().length() > 0)
        builder.devAlt(decodedLoraHistory.getDevAlt());
      if (decodedLoraHistory.getDevAltRadius().length() > 0)
        builder.devAltRadius(decodedLoraHistory.getDevAltRadius());
      if (decodedLoraHistory.getDevAcc().length() > 0)
        builder.devAcc(decodedLoraHistory.getDevAcc());
      if (!decodedLoraHistory.getDevNorthVelocity().equals("null"))
        builder.devNorthVelocity(decodedLoraHistory.getDevNorthVelocity());
      if (!decodedLoraHistory.getDevEastVelocity().equals("null"))
        builder.devEastVelocity(decodedLoraHistory.getDevEastVelocity());
      // NFR 3156
      builder.lcMU(decodedLoraHistory.getLcMU());
    }
    if (Direction.UPLINK.equals(decodedLoraHistory.getDirection())) { // UP
      builder.gwCnt(decodedLoraHistory.getDevLrrCnt());
      builder.lrrs(decodedLoraHistory.getLrrListAsElements(true, this.wloggerConfig.passiveRoaming()));
      // builder.CustomerData(decodedLoraHistory.getCustomerData());
      if (decodedLoraHistory.getCustomerData() != null
          && decodedLoraHistory.getCustomerData().length() > 0
          && decodedLoraHistory.getCustomerData().startsWith("{")) {
        JsonObject jObject = new JsonObject(decodedLoraHistory.getCustomerData());
        ElementLoc elmtLoc = null;
        ElementAlr elmtAlr = null;

        JsonObject loc = jObject.getJsonObject("loc");
        JsonObject alr = jObject.getJsonObject("alr");
        if (loc != null) {
          elmtLoc = new ElementLoc(loc.getString("lat"), loc.getString("lon"));
        }
        if (alr != null) {
          elmtAlr = new ElementAlr(alr.getString("pro"), alr.getString("ver"));
        }
        if (elmtLoc != null || elmtAlr != null) {
          builder.customerData(new ElementCustomerData(elmtLoc, elmtAlr));
        }
        // RDTP 2274
        builder.tags(decodedLoraHistory.getTags());
      }
      builder.late(decodedLoraHistory.getLate());
    }

    if (Direction.LOCATION.equals(decodedLoraHistory.getDirection())) { // Location
      builder.lrrs(decodedLoraHistory.getLrrListAsElements(false, this.wloggerConfig.passiveRoaming()));
    }

    // NFR 809 312 948
    if (this.wloggerConfig.passiveRoaming()) {
      builder
          .gwID(decodedLoraHistory.getGwID())
          .gwTk(decodedLoraHistory.getGwTk())
          .gwOp(decodedLoraHistory.getGwOp())
          .rt(decodedLoraHistory.getRt())
          .rr(decodedLoraHistory.getRr());
    }

    builder.ismb(decodedLoraHistory.getIsmb());

    // NFR 887
    builder.mod(decodedLoraHistory.getMod());
    builder.dr(decodedLoraHistory.getDr());

    // NFR 724
    builder.asID(decodedLoraHistory.getAsID());

    // NFR 965
    builder.subscriberID(decodedLoraHistory.getCustomerId());

    // NFR 1036
    if (Direction.MULTICAST_SUMMARY.equals(decodedLoraHistory.getDirection())) { // MultiCast
      builder.tss(decodedLoraHistory.getTss());
      builder.lcn(decodedLoraHistory.getLcn());
      builder.lsc(decodedLoraHistory.getLsc());
      builder.lfc(decodedLoraHistory.getLfc());
      // NFR 2215
      builder.lfd(decodedLoraHistory.getLfdAsElement());
    }

    // NFR 1061 1062
    builder
        .afCntDown(decodedLoraHistory.getAfCntDown())
        .nfCntDown(decodedLoraHistory.getNfCntDown())
        .confAFCntDown(decodedLoraHistory.getConfAFCntDown())
        .confNFCntDown(decodedLoraHistory.getConfNFCntDown())
        .confFCntUp(decodedLoraHistory.getConfFCntUp())
        // NFR 2543
        .ts(decodedLoraHistory.getTs())
        // NFR 5787
        .rul(decodedLoraHistory.getRul())
        // NFR 7009
        .freq(decodedLoraHistory.getFreq())
        // NFR 2210
        .clazz(decodedLoraHistory.getClazz())
        .bper(decodedLoraHistory.getBper());

    // NFR 6024
    if (Direction.DEVICE_RESET.equals(decodedLoraHistory.getDirection())) {
      builder.notifTp(decodedLoraHistory.getNotifTp()).resetTp(decodedLoraHistory.getResetTp());
    }

    // NFR 9170
    builder
        .payloadDriverId(decodedLoraHistory.getPayloadDriverId())
        .payloadDriverModel(decodedLoraHistory.getPayloadDriverModel())
        .payloadDriverApplication(decodedLoraHistory.getPayloadDriverApplication())
        .payloadDecodingError(decodedLoraHistory.getPayloadDecodingError())

        .rfRegion(decodedLoraHistory.getRfRegion())
        .foreignOperatorNSID(decodedLoraHistory.getForeignOperatorNSID())

        .asReportDeliveryID(decodedLoraHistory.getAsReportDeliveryID())
        .asRecipients(decodedLoraHistory.getRecipientListAsElements())
        .asDeliveryStatus(decodedLoraHistory.getDeliveryStatus());

    return builder.build();
  }

  private ElementDeviceHistory standardExtract(DecodedLoraHistory decodedLoraHistory) {
    return ElementDeviceHistory.builder()
        .uid(decodedLoraHistory.getUid())
        .direction(Direction.getValueOrEmpty(decodedLoraHistory.getDirection()))
        .timestamp(decodedLoraHistory.getTimestampAsD())
        .devEUI(decodedLoraHistory.getDevEUI())
        .fPort(decodedLoraHistory.getfPort())
        .fCnt(decodedLoraHistory.getfCnt())
        .payloadHex(decodedLoraHistory.getPayloadHex())
        .micHex(decodedLoraHistory.getMicHex())
        .lrrRSSI(decodedLoraHistory.getLrrRSSI())
        .lrrSNR(decodedLoraHistory.getLrrSNR())
        .lrrESP(decodedLoraHistory.getLrrESP())
        .spFact(decodedLoraHistory.getSpFact())
        .airTime(decodedLoraHistory.getAirTime())
        .subBand(decodedLoraHistory.getSubBand())
        .channel(decodedLoraHistory.getChannel())
        .lrrid(decodedLoraHistory.getLrrid())
        .lrrLAT(decodedLoraHistory.getLrrLAT())
        .lrrLON(decodedLoraHistory.getLrrLON())
        .devLrrCnt(decodedLoraHistory.getDevLrrCnt())
        .devLAT(Utils.getToStringOrNull(decodedLoraHistory.getDevLAT()))
        .devLON(Utils.getToStringOrNull(decodedLoraHistory.getDevLON()))
        .devLocRadius(decodedLoraHistory.getDevLocRadius())
        .devLocTime(decodedLoraHistory.getDevLocTime())
        .devAlt(decodedLoraHistory.getDevAlt())
        .devAltRadius(decodedLoraHistory.getDevAltRadius())
        .devAcc(decodedLoraHistory.getDevAcc())
        .customerId(decodedLoraHistory.getCustomerId())
        .customerData(decodedLoraHistory.getCustomerData())
        .lrcId(decodedLoraHistory.getLrcId())
        .timestampUTC(decodedLoraHistory.getTimestampAsUTC())
        .rawMacCommands(decodedLoraHistory.getRawMacCommands())
        .mType(decodedLoraHistory.getmType())
        .lrcRequestedRxDelay(decodedLoraHistory.getLrcRequestedRxDelay())
        .devAddr(decodedLoraHistory.getDevAddr())
        .adRbit(decodedLoraHistory.getAdRbit())
        .adrAckReq(decodedLoraHistory.getAdrAckReq())
        .ackRequested(decodedLoraHistory.getAckRequested())
        .acKbit(decodedLoraHistory.getAcKbit())
        .fPending(decodedLoraHistory.getfPending())
        .dlStatus(decodedLoraHistory.getDlStatus())
        .dlFailedCause1(decodedLoraHistory.getDlFailedCause1())
        .dlFailedCause2(decodedLoraHistory.getDlFailedCause2())
        .dlFailedCause3(decodedLoraHistory.getDlFailedCause3())
        .distance(decodedLoraHistory.getDistance())
        .lrrList(decodedLoraHistory.getLrrListAsHtmlTable(this.wloggerConfig.passiveRoaming()))
        .mTypeText(decodedLoraHistory.getmTypeText())
        .hasMac(decodedLoraHistory.getHasMac())
        .macDecoded(decodedLoraHistory.getMacDecoded())
        .hasPayload(decodedLoraHistory.getHasPayload())
        .payloadDecoded(decodedLoraHistory.getPayloadDecoded())
        .dlStatusText(decodedLoraHistory.getDlStatusText())
        .dlFailedCause1Text(decodedLoraHistory.getDlFailedCause1Text())
        .dlFailedCause2Text(decodedLoraHistory.getDlFailedCause2Text())
        .dlFailedCause3Text(decodedLoraHistory.getDlFailedCause3Text())
        .late(decodedLoraHistory.getLate())
        .solvLAT(decodedLoraHistory.getSolvLAT())
        .solvLON(decodedLoraHistory.getSolvLON())
        .payloadSize(decodedLoraHistory.getPayloadSize())
        .gwOp(decodedLoraHistory.getGwOp())
        .gwID(decodedLoraHistory.getGwID())
        .gwTk(decodedLoraHistory.getGwTk())
        .ismb(decodedLoraHistory.getIsmb())
        .mod(decodedLoraHistory.getMod())
        .dr(decodedLoraHistory.getDr())
        .devNorthVelocity(decodedLoraHistory.getDevNorthVelocity())
        .devEastVelocity(decodedLoraHistory.getDevEastVelocity())
        .asID(decodedLoraHistory.getAsID())
        .mcc(decodedLoraHistory.getMcc())
        .tss(decodedLoraHistory.getTss())
        .lcn(decodedLoraHistory.getLcn())
        .lsc(decodedLoraHistory.getLsc())
        .lfc(decodedLoraHistory.getLfc())
        .rt(decodedLoraHistory.getRt())
        .rr(decodedLoraHistory.getRr())
        .afCntDown(decodedLoraHistory.getAfCntDown())
        .nfCntDown(decodedLoraHistory.getNfCntDown())
        .confAFCntDown(decodedLoraHistory.getConfAFCntDown())
        .confNFCntDown(decodedLoraHistory.getConfNFCntDown())
        .confFCntUp(decodedLoraHistory.getConfFCntUp())
        .lfd(decodedLoraHistory.getLfdAsJsonString())
        .ts(decodedLoraHistory.getTs())
        .rul(decodedLoraHistory.getRul())
        .freq(decodedLoraHistory.getFreq())
        .clazz(decodedLoraHistory.getClazz())
        .bper(decodedLoraHistory.getBper())
        .notifTp(decodedLoraHistory.getNotifTp())
        .resetTp(decodedLoraHistory.getResetTp())
        .payloadDriverId(decodedLoraHistory.getPayloadDriverId())
        .payloadDriverModel(decodedLoraHistory.getPayloadDriverModel())
        .payloadDriverApplication(decodedLoraHistory.getPayloadDriverApplication())
        .payloadDecodingError(decodedLoraHistory.getPayloadDecodingError())
        .lcMU(decodedLoraHistory.getLcMU())
        .payloadEncryption(decodedLoraHistory.getPayloadEncryption())
        .tags(decodedLoraHistory.getTags())
        .rfRegion(decodedLoraHistory.getRfRegion())
        .foreignOperatorNSID(decodedLoraHistory.getForeignOperatorNSID())
        .asReportDeliveryID(decodedLoraHistory.getAsReportDeliveryID())
        .asRecipients(decodedLoraHistory.getRecipientListAsHtmlTable())
        .asDeliveryStatus(decodedLoraHistory.getDeliveryStatus())
        .build();
  }
}
