package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.decoder.Decoder;
import com.actility.thingpark.decoder.DecoderFactory;
import com.actility.thingpark.decoder.DecoderType;
import com.actility.thingpark.decoder.DecoderUtil;
import com.actility.thingpark.decoder.impl.IPV4Decoder;
import com.actility.thingpark.decoder.impl.MacDecoder;
import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.wlogger.engine.EngineClient;
import com.actility.thingpark.wlogger.engine.EngineException;
import com.actility.thingpark.wlogger.engine.model.*;
import com.actility.thingpark.wlogger.entity.history.Utils;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wlogger.model.*;
import com.actility.thingpark.wloggerwrap.WLWrapper;
import com.actility.thingpark.wloggerwrap.WLWrapperFactory;
import com.actility.thingpark.wloggerwrap.impl.IPV4WLWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Singleton
public class DecoderService {

  public static final String DECODER_RAW = "raw";
  public static final String DECODER_AUTOMATIC = "auto";
  private static final String DEFAULT = "~DEFAULT";

  private static final Logger LOGGER = Logger.getLogger(DecoderService.class.getName());
  public static final String DRIVER_ID = "driverId";
  public static final String DECODER_ERROR = "Decoder error : ";
  public static final String MINUS_ONE = "-1";
  public static final String ZERO = "0";

  public final SimpleDateFormat TIME_FORMAT_X = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  private final EngineClient engineClient;

  @Inject
  public DecoderService(final EngineClient engineClient) {
    this.engineClient = engineClient;
  }

  public static boolean isDeviceHistoryDecodable(DeviceHistory dev) {
    return (dev.type == 0 || dev.type == 1)
            && isNotEmpty(dev.payload)
            // RDTP-10309
            && !(dev.payloadEncrypted != null && dev.payloadEncrypted == 1)
//            && isNotEmpty(dev.getDriverModel())
            && isNotEmpty(dev.getDriverApplication())
            && !dev.isCustomerRestricted();
  }

  private static JsonObject decodeErrorAsMessage(ErrorInfo errorInfo) {
    return new JsonObject().put("code", errorInfo.code).put("message", errorInfo.message);
  }

  private static DecodeBatchInputItem convertToBatchItem(DeviceHistory deviceHistory) {

    ObjectNode lorawan = new ObjectMapper().createObjectNode();
    lorawan.putObject("lorawan").put("fPort", deviceHistory.fport);

    ThingDescription.Builder thing = ThingDescription.builder();
    if (isNotEmpty(deviceHistory.getDriverModel()))
      thing.model(ThingInfo.builder()
              .producerId(deviceHistory.getProducerId())
              .moduleId(deviceHistory.getModuleId())
              .version(deviceHistory.getVersion())
              .build());

    if (isNotEmpty(deviceHistory.getDriverApplication()))
      thing.application(ThingInfo.builder()
              .producerId(deviceHistory.getProducerIdApp())
              .moduleId(deviceHistory.getModuleIdApp())
              .version(deviceHistory.getVersionApp())
              .build());

    return DecodeBatchInputItem.builder()
            .id(deviceHistory.id.toHexString())
            .input(
                    DecodeInput.builder()
                            .direction(deviceHistory.type == 0
                                            ? com.actility.thingpark.wlogger.engine.model.Direction.UPLINK
                                            : com.actility.thingpark.wlogger.engine.model.Direction.DOWNLINK)
                            .sourceTime(deviceHistory.timestamp.toInstant().atOffset(ZoneOffset.UTC))
                            .meta(lorawan)
                            .thing(thing.build())
                            .raw(Raw.builder().binary(deviceHistory.payload).build())
                            .DevEUI(deviceHistory.deviceEUI)
                            // RDTP-18593 
                            //.ADRbit(deviceHistory.adrControlBit? 1 : 0)
                            .ADRbit(Boolean.TRUE.equals(deviceHistory.adrControlBit) ? 1 : 0)
                            .Frequency(deviceHistory.frequency)
                            .FCntUp(deviceHistory.loraFrameCounter)
                            .build())
            .build();
  }

  private List<DeviceHistory> getDecodedFrames(Search search, List<DeviceHistory> list,
                                               int pageSize, int maxAutomaticDecodedPackets, int exportSize,
                                               String subscriberId, String realmId) {
    if (!DECODER_AUTOMATIC.equals(search.getDecoder())) {
      LOGGER.log(Level.INFO, "No IOT Flow decoding");
      return list;
    }
    if (exportSize > maxAutomaticDecodedPackets) {
      LOGGER.log(Level.INFO, "No IOT Flow decoding : export is too large");
      return list;
    }
    if (pageSize > maxAutomaticDecodedPackets) {
      LOGGER.log(Level.INFO, "No IOT Flow decoding : page size is too large");
      return list;
    }

    // RDTP-16368 : Confidentiality issues in wireless logger
    if (search.getNetPartId() != null && search.getNetPartId().length()>0) {
      LOGGER.log(Level.INFO, "No IOT Flow decoding : NetPartnerID not null : Confidentiality issues in wireless logger");
      list.stream().forEach(dev -> dev.payload = "");
      return list;
    }

    try {
      LOGGER.log(Level.INFO, "IOT Flow decoding");
      return mergeDecodedFrames(list, this.engineClient.decodeBatch(getListForEngine(list, search), subscriberId, realmId));
    } catch (EngineException e) {
      LOGGER.log(Level.INFO, "IOT Flow exception", e);
      return mergeDecodeError(list, e.getError());
    }
  }

  private List<DeviceHistory> mergeDecodeError(List<DeviceHistory> list, ErrorInfo errorInfo) {
    list.stream()
            .filter(DecoderService::isDeviceHistoryDecodable)
            .forEach(dev -> dev.setPayloadDecodingError(decodeErrorAsMessage(errorInfo).toString()));
    return list;
  }

  private List<DeviceHistory> mergeDecodedFrames(
          List<DeviceHistory> list, List<DecodeBatchOutputItem> decodedFrames) {

    if (decodedFrames == null || decodedFrames.isEmpty()) {
      return list;
    }

    Map<String, DecodeBatchOutputItem> mapDecodedFrames = new HashMap<>();
    for (DecodeBatchOutputItem frame : decodedFrames) {
      mapDecodedFrames.put(frame.id, frame);
    }
    list.stream()
            .filter(DecoderService::isDeviceHistoryDecodable)
            .forEach(
                    dev -> {
                      DecodeBatchOutputItem frame = mapDecodedFrames.get(dev.id.toHexString());
                      if (frame.error != null) {
                        JsonObject decodeError = decodeErrorAsMessage(frame.error);
                        if (frame.error.data != null) {
                          dev.setPayloadDriverId(frame.error.data.get(DRIVER_ID).asText());
                          decodeError.put(
                                  "data", new JsonObject().put(DRIVER_ID, dev.getPayloadDriverId()));
                        }
                        dev.setPayloadDecodingError(decodeError.toString());
                      } else {
                        if (frame.output.message != null) {
                          dev.setPayloadDecoded(frame.output.message.toString());
                        }
                        if (isNotBlank(frame.output.meta.get(DRIVER_ID).asText())) {
                          dev.setPayloadDriverId(frame.output.meta.get(DRIVER_ID).asText());
                        }
                        if (frame.output.thing != null && frame.output.thing.points != null) {
                          dev.setGeolocDecoded(frame.output.thing.points);
                        }
                      }
                    });
    return list;
  }

  private List<DecodeBatchInputItem> getListForEngine(List<DeviceHistory> list, Search search) {
    return list.stream()
        .map(deviceHistory -> deviceHistory.fillModelApplication().fillCustomerRestrictions(search))
            .filter(DecoderService::isDeviceHistoryDecodable)
            .map(DecoderService::convertToBatchItem)
            .collect(toList());
  }

  public List<DecodedLoraHistory> decodeLoraFrames(List<DeviceHistory> deviceHistories, Search search, boolean passiveRoaming,
                                                   int pageSize, int maxAutomaticDecodedPackets, int exportSize,
                                                   String subscriberId, String realmId) {
    return getDecodedFrames(search, deviceHistories, pageSize, maxAutomaticDecodedPackets, exportSize, subscriberId, realmId).stream()
            .map(dev -> this.decodeLora(dev, search, passiveRoaming))
            .collect(toList());
  }

  public DecodedLteHistory decodeLte(DeviceHistory history, String decoder, String netPartID) {
    Double devLat = null; // getLatitude();
    Double devLon = null; // getLongitude();

    Optional<CustomerData> customerData = parseCustomerData(history.customerData);
    if (customerData.isPresent()) {
      if (customerData.get().devLat != null)
        devLat = customerData.get().devLat;
      if (customerData.get().devLon != null)
        devLon = customerData.get().devLon;
    }

    // RDTP-16368 : Confidentiality issues in wireless logger
    if (netPartID != null && netPartID.length()>0)
      history.payload = "";

    // 6) hasMAC flag
    String hasPayload = ZERO;
    int lteIPPacketSize = 0;
    String ipv4decoded = "";
    String decoded = "";
    String protocol = "";
    IPV4Decoder ipv4Decoder = null;
    IPV4WLWrapper ipv4Wrapper = null;

    try {
      ipv4Decoder = getIPV4Decoder();
      ipv4Wrapper = getIPV4WLWrapper();
    } catch (Exception ex) {
      LOGGER.log(Level.INFO, DECODER_ERROR + ex.getMessage(), ex);
    }

    if (history.payload != null
            && history.payload.length() > 0
            && !history.payload.equals("None")) {
      hasPayload = "1";
      lteIPPacketSize = history.payload.length() / 2;

      try {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(DecoderUtil.PROPERTY_DIRECTION, Utils.getToString(history.type));

        if (ipv4Decoder != null && ipv4Wrapper != null) {
          IPV4Decoder.Message message =
              (IPV4Decoder.Message) ipv4Decoder.decode(history.payload, properties);
          ipv4Wrapper.setDecodedMessage(message, properties);
          ipv4decoded = ipv4Wrapper.getLine();
          String data = message.getData();

          if (decoder != null && decoder.length() > 0 && data.length() > 0) {

            Decoder payloadDecoder = null;
            WLWrapper wrapper = null;
            try {
              payloadDecoder = DecoderFactory.getDecoder(decoder);
              wrapper = WLWrapperFactory.getWLWrapper(decoder);
            } catch (Exception ex) {
              LOGGER.log(Level.INFO, DECODER_ERROR + ex.getMessage(), ex);
            }

            properties.put(DecoderUtil.PROPERTY_PORT, ZERO /*String.valueOf(fport)*/);
            if (payloadDecoder != null && wrapper != null) {
              Object message2 = payloadDecoder.decode(data, properties);
              wrapper.setDecodedMessage(message2, properties);
              decoded = wrapper.getLine();
            }
          }
          protocol = message.getProtocolType();
        }
      } catch (Exception ex) {
        LOGGER.log(Level.INFO, "DeviceLteHistory error when decode payload : " + ex.getMessage(), ex);
      }
    }
    return DecodedLteHistory.builder()
            .withUid(history.id.toHexString())
            .withDirection(Direction.fromValue(history.type))
            .withTimestamp(history.timestamp)
            .withDevEUI(Utils.getToString(history.deviceEUI))
            .withDevAddr(Utils.getToString(history.deviceAddress))
            .withPayloadHex(history.payload)
            .withLrrid(history.pgwID /*lrrID*/)
            .withLrcId(history.lrcID)
            .withDevLAT(devLat)
            .withDevLON(devLon)
            .withMType(Utils.getToString(history.mtype))
            .withMTypeText((ipv4Decoder != null ? ipv4Decoder.getMTypeText(history.mtype) : null))
            .withHasPayload(hasPayload)
            .withPayloadDecoded(decoded)
            .withLate(Utils.getToStringOrNull(history.lateReporting))
            .withPayloadSize("" /*getToString(frameSize)*/)
            .withLteIPPacketSize(Utils.getToString(lteIPPacketSize))
            .withIpv4Decoded(ipv4decoded)
            .withLtePacketProtocol(protocol)
            .withLteCause(Utils.getToString(history.sessionDeletionCause))
            .withLteEBI(Utils.getToString(history.epsBearerID))
            .withLteAPN(Utils.getToString(history.apn))
            .withLteMSISDN(Utils.getToString(history.msisdnNumber))
            .withLteIMSI(Utils.getToString(history.imsi))
            .withLteRAT(Utils.getToString(history.radioAccessType))
            .withLteCELLID(Utils.getToString(history.cellID))
            .withLteCELLTAC(Utils.getToString(history.cellTrackingAreaNumber))
            .withLteSERVNET(Utils.getToString(history.servingNetworkMCCMNC))
            .withLteMCCMNC(Utils.getToString(history.cellMCCMNC))
            .withLteIMEI(Utils.getToString(history.imei))
            .withAsID(getToString(history.applicationServerIDs, ","))
            .withMfSzU(Utils.getToString(history.microflowUplinkSize))
            .withMfSzD(Utils.getToString(history.microflowDownlinkSize))
            .withMfDur(Utils.getToString(history.microflowDuration))
            .withAsReportDeliveryID(Utils.getToString(history.asReportDeliveryID))
            .withAsRecipients(mapRecipientList(history.asRecipients))
            .build();
  }

  public String getToString(List<String> list, String delim) {
    if (list == null)
      return "";
    else if (list.isEmpty())
      return "";
    else {
      ListIterator<String> it = list.listIterator();
      StringBuffer sb = new StringBuffer();
      while (it.hasNext()) {
        sb.append(it.next()).append(delim);
      }
      return sb.substring(0, sb.length() - 1);
    }
  }

  public class CustomerData {
    private Double devLat;
    private Double devLon;
    private String tags;

    public CustomerData(Double devLat, Double devLon, String tags) {
      this.devLat = devLat;
      this.devLon = devLon;
      this.tags = tags;
    }

    public void setTags(String tags) {
      this.tags = tags;
    }
  }

  public Optional<CustomerData> parseCustomerData(String customerData) {
    // 1) GPS DATA : si les customer data contiennent les coordonnées GPS, on remplace
    if (customerData != null
            && customerData.length() > 0
            && customerData.startsWith("{")) {
      try {
        JsonObject jObject = new JsonObject(customerData);
        JsonObject loc = jObject.getJsonObject("loc");
        JsonArray tags = jObject.getJsonArray("tags");

        CustomerData cd = null;

        if (loc != null) {
          String lat = loc.getString("lat");
          String lon = loc.getString("lon");

          if (lat != null && lat.length() > 0 && lon != null && lon.length() > 0) {
            double devLat = Double.parseDouble(lat);
            double devLon = Double.parseDouble(lon);
            cd = new CustomerData(devLat, devLon, null);
          }
        }
        if (tags != null) {
          ArrayList<String> list = new ArrayList<>();
          for (int i = 0 ; i < tags.size() ; i++){
            list.add(tags.getString(i));
          }
          String tagsString = list.stream().map(Object::toString).collect(Collectors.joining(","));
          if (cd != null)
            cd.setTags(tagsString);
          else
            cd = new CustomerData(null, null, tagsString);

        }

        if (cd != null)
          return Optional.of(cd);
        else
          return Optional.empty();

      } catch (ClassCastException | DecodeException ex) {
        LOGGER.log(Level.INFO,"parseCustomerData error : " + ex.getMessage(), ex);
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  public DecodedLoraHistory decodeLora(
          DeviceHistory history, Search search, boolean passiveRoamingActivated) {
    String devLocTimeD = "null";
    if (history.devLocTime != null) {
      devLocTimeD = TIME_FORMAT_X.format(history.devLocTime);
    }

    Double lrrLat = history.getLRRLatitude();
    Double lrrLon = history.getLRRLongitude();
    Double devLat = history.getLatitude();
    Double devLon = history.getLongitude();

    // -1) STORE initial solver info in SolvLAT and SolvLON
    String solvLat = "null";
    if (devLat != null) solvLat = devLat.toString();
    String solvLon = "null";
    if (devLon != null) solvLon = devLon.toString();

    String tagsString = "";
    Optional<CustomerData> customerData = parseCustomerData(history.customerData);
    if (customerData.isPresent()) {
      if (customerData.get().devLat != null)
        devLat = customerData.get().devLat;
      if (customerData.get().devLon != null)
        devLon = customerData.get().devLon;
      if (customerData.get().tags != null)
        tagsString = customerData.get().tags;
    }

    String distance = ZERO;
    // 0) LRC triangulation Data
    if (devLat != null && devLat != 0 && devLon != null && devLon != 0) {
      distance = calculDist(lrrLat, lrrLon, devLat, devLon, "M");
    }

    // RDTP-16368 : Confidentiality issues in wireless logger
    if (search.getNetPartId() != null && search.getNetPartId().length()>0)
      history.payload = "";

    Object message = null;
    Decoder payloadDecoder = null;
    WLWrapper wrapper = null;
    // 2) GPS DATA : si il existe un decodeur qui implemente les coordonnées GPS, on remplace
    // 0004101: [N3_QUEUE][#8919] Wlogger decodes downlink payload
    if (search.getDecoder() != null
            && search.getDecoder().length() > 0
            && !search.getDecoder().equals(DECODER_AUTOMATIC)
            && (history.type == 0 || history.type == 3)) {

      // JUST CHECK IF IT EXIST
      // Must create a decoder per packet

      try {
        payloadDecoder = DecoderFactory.getDecoder(search.getDecoder());
        wrapper = WLWrapperFactory.getWLWrapper(search.getDecoder());
      } catch (Exception ex) {
        LOGGER.log(Level.INFO, DECODER_ERROR + ex.getMessage(), ex);
      }

      if (payloadDecoder != null
          && wrapper != null
          && history.payload != null
          && history.payload.length() > 0
          // RDTP-10309
          && !(history.payloadEncrypted != null && history.payloadEncrypted == 1) ) {

        try {
          HashMap<String, String> properties = new HashMap<>();
          if (history.fport != null)
            properties.put(DecoderUtil.PROPERTY_PORT, String.valueOf(history.fport));
          properties.put(DecoderUtil.PROPERTY_PAYLOAD, history.payload);
          message = payloadDecoder.decode(history.payload, properties);
          wrapper.setDecodedMessage(message, properties);

          if (wrapper.isManagedLocation()) {
            Pair<String, String> decoded = wrapper.getLocation();
            // 0004213: Invalid GPS location (Lon=0, Lat=0) must not be taken into account when
            // displaying the location on the MAP.
            devLat = Double.parseDouble(decoded.getLeft());
            devLon = Double.parseDouble(decoded.getRight());
            if (devLat != 0 && devLon != 0) {
              distance = calculDist(lrrLat, lrrLon, devLat, devLon, "M");
            }
          }
        } catch (Exception ex) {
          LOGGER.log(Level.INFO,"DeviceHistory error when decode payload : " + ex.getMessage(), ex);
          payloadDecoder = null;
          wrapper = null;
          message = null;
        }
      }
    }
    if (search.getDecoder() != null && search.getDecoder().length() > 0 && search.getDecoder().equals(DECODER_RAW)) {
      if (history.getDecodedLatitude() != null && history.getDecodedLongitude() != null) {
        devLat = history.getDecodedLatitude();
        devLon = history.getDecodedLongitude();
        if (devLat != 0 && devLon != 0) {
          distance = calculDist(lrrLat, lrrLon, devLat, devLon, "M");
        }
      }
    }

    if (distance.equals(ZERO)) {
      distance = "";
    }

    // 4) Add the MTYPE
    // MacDecoder.getMTypeText(mtype) : appelé plus tard

    // 5) MAC / hasMaC field
    MacDecoder macDecoder = null;
    try {
      macDecoder = getMacDecoder();
    } catch (Exception ex) {
      LOGGER.log(Level.INFO,"MacDecoder error : " + ex.getMessage(), ex);
    }

    String hasMac = ZERO;
    String macDecoded = "";
    if (history.macCommands != null && history.macCommands.length() > 0 && macDecoder != null) {
      hasMac = "1";
      try {
        if (history.mtype == 0) { // Decode Join Request
          macDecoded = macDecoder.decode_MAC_Join(history.mtype, history.macCommands, 0);
        } else if (history.mtype == 1) { // Join response is encoded
          // Bug RDTP-1018
          // macDecoded = macDecoder.decode(type, macCommands);
          macDecoded = "Encrypted Content";
        } else {
          macDecoded = macDecoder.decode_MAC_Commands(history.type, history.macCommands);
        }
      } catch (Exception ex) {
        LOGGER.log(Level.INFO,"DeviceHistory error when decode payload : " + ex.getMessage(), ex);
        macDecoded = "Error: macCommands malformed";
      }
    }

    // 6) PAYLOAD management
    //   - hasPayload flag
    //   -  FOR up(down)link   : when payload_hex = null  (or None for compatibility < 3.6.1)
    //                           if  uplink(downlink)size > 0
    //                              then payload_hex must be set to "Hidden"
    //                              else payload_hex must be set to "None"
    //                           when payload_hex != null
    //                               then call decoder

    String hasPayload = ZERO;
    String payloadStr = history.payload;
    String spayloadDecoded = "";
    if (history.type == 0 || history.type == 1 || history.type == 3) {
      if (history.payload == null
          || history.payload.equals("None")
          || history.payload.length() == 0) {
        if (history.frameSize == null || history.frameSize == 0) {
          hasPayload = ZERO;
          payloadStr = "None";
        } else { // frameSize > 0
          hasPayload = "1";
          payloadStr = "Hidden";
        }
      } else {
        hasPayload = "1";
        // 0004101: [N3_QUEUE][#8919] Wlogger decodes downlink payload
        if (payloadDecoder != null && wrapper != null && message != null && history.type == 0) {
          spayloadDecoded = wrapper.getLine();
        } else if (DECODER_AUTOMATIC.equals(search.getDecoder())) {
          if (history.getPayloadDecoded() != null) spayloadDecoded = history.getPayloadDecoded();
        }
      }
    }

    // 7) decode downlink status and delivery
    String dlStatusText = "";
    String sdeliveryFailedCause1 = "";
    String sdeliveryFailedCause2 = "";
    String sdeliveryFailedCause3 = "";
    if (history.type == 1 || history.type == 3) {
      dlStatusText = decodeDeliveryStatus(history.deliveryStatus);
      sdeliveryFailedCause1 = decodeDeliveryFailedCause1(history.deliveryFailedCause1);
      sdeliveryFailedCause2 = decodeDeliveryFailedCause2(history.deliveryFailedCause2);
      sdeliveryFailedCause3 = decodeDeliveryFailedCause3(history.deliveryFailedCause3);
    }

    // NFR 809
    if (!passiveRoamingActivated) {
      history.foreignOperatorNetId = null;
      history.bestGWId = null;
      history.bestGWToken = null;
      history.roamingType = null;
      history.roamingResult = null;
    }

    // NFR 4978  '~DEFAULT' => '' on dvID and suID
    if (history.deviceEUI != null && history.deviceEUI.equals(DEFAULT)) {
      history.deviceEUI = "";
    }
    if (history.subscriberID != null && history.subscriberID.equals(DEFAULT)) {
      history.subscriberID = "";
    }

    // NFR 9170
    if (!DECODER_AUTOMATIC.equals(search.getDecoder())) {
      history.fillModelApplication().fillCustomerRestrictions(search);
    }

    // RDTP-8479 : customer domains restrictions
    if (history.isCustomerRestricted()) {
      // hide payload
      hasPayload = "1";
      payloadStr = "Hidden";
      spayloadDecoded = "";
    }

    DecodedLoraHistory decodedLoraHistory = DecodedLoraHistory.builder()
        .withUid(/*String.valueOf(uid)*/ history.id.toHexString())
        .withDirection(Direction.fromValue(history.type))
        .withTimestamp((history.timestamp))
        .withDevEUI(history.deviceEUI)
        .withFPort(Utils.getToStringOrNone(history.fport))
        .withFCnt(Utils.getToString(history.loraFrameCounter))
        .withPayloadHex(payloadStr)
        .withMicHex(history.mic)
        .withLrrRSSI(Utils.getToStringOrNull(history.rssi))
        .withLrrSNR(Utils.getToStringOrNull(history.snr))
        .withLrrESP(getInfoESPFromLRR(history.lrrs, history.lrrID))
        .withSpFact(Utils.getToString(history.sf))
        .withAirTime(Utils.getToString(history.airtime))
        .withSubBand(history.subBand)
        .withChannel(history.lrrChannel)
        .withLrrid(history.lrrID)
        .withLrrLAT(Utils.getToStringOrNull(lrrLat))
        .withLrrLON(Utils.getToStringOrNull(lrrLon))
        .withDevLrrCnt(Utils.getToStringOrNull(history.lrrCount))
        .withDevLAT(devLat)
        .withDevLON(devLon)
        .withDevLocRadius(Utils.getToStringOrNull(history.devLocRadius))
        .withDevLocTime(devLocTimeD)
        .withDevAlt(Utils.getToString(history.altitude))
        .withDevAltRadius(Utils.getToString(history.altitudeRadius))
        .withDevAcc(Utils.getToString(history.locationAccuracy))
        .withCustomerId(history.subscriberID)
        .withCustomerData(Utils.getToStringOrNone(history.customerData))
        .withLrcId(history.lrcID)
        .withRawMacCommands(history.macCommands)
        .withMType(Utils.getToString(history.mtype))
        .withLrcRequestedRxDelay(Utils.getToString(history.lrcRxDelayRequested))
        .withDevAddr(history.deviceAddress)
        .withAdRbit(Utils.getToString(history.adrControlBit))
        .withAdrAckReq(Utils.getToString(history.adrAckRequested))
        .withAckRequested(Utils.getToString(history.ackRequested))
        .withAcKbit(Utils.getToString(history.ackIndicator))
        .withFPending(Utils.getToString(history.pendingDownlinkFrameIndicator))
        .withDlStatus(Utils.getToStringOrNull(history.deliveryStatus))
        .withDlFailedCause1(history.deliveryFailedCause1)
        .withDlFailedCause2(history.deliveryFailedCause2)
        .withDlFailedCause3(history.deliveryFailedCause3)
        .withDistance(distance)
        .withLrrs(mapLrrList(history.lrrs))
        .withMTypeText(getMTypeText(history.mtype))
        .withHasMac(hasMac)
        .withMacDecoded(macDecoded)
        .withHasPayload(hasPayload)
        .withPayloadDecoded(spayloadDecoded)
        .withDlStatusText(dlStatusText)
        .withDlFailedCause1Text(sdeliveryFailedCause1)
        .withDlFailedCause2Text(sdeliveryFailedCause2)
        .withDlFailedCause3Text(sdeliveryFailedCause3)
        .withLate(Utils.getToStringOrNull(history.lateReporting))
        .withSolvLAT(solvLat)
        .withSolvLON(solvLon)
        .withPayloadSize(Utils.getToString(history.frameSize))
        .withGwOp(Utils.getToString(history.foreignOperatorNetId))
        .withGwID(Utils.getToString(history.bestGWId))
        .withGwTk(Utils.getToString(history.bestGWToken))
        .withIsmb(Utils.getToString(history.ismBand))
        .withMod(Utils.getToString(history.modulation))
        .withDr(Utils.getToString(history.dataRate))
        .withDevNorthVelocity(Utils.getToStringOrNull(history.velocityNorth))
        .withDevEastVelocity(Utils.getToStringOrNull(history.velocityEast))
        .withAsID(getToString(history.applicationServerIDs, ","))
        .withMcc(Utils.getToString(history.multiCastClass))
        .withTss(Utils.getToString(history.mccTransmissionStatus))
        .withLcn(Utils.getToString(history.mccLrrCount))
        .withLsc(Utils.getToString(history.mccLrrSuccessCount))
        .withLfc(Utils.getToString(history.mccLrrFailedCount))
        .withRt(Utils.getToString(history.roamingType))
        .withRr(Utils.getToString(history.roamingResult))
        .withAfCntDown(Utils.getToString(history.aFCntDown))
        .withNfCntDown(Utils.getToString(history.nFCntDown))
        .withConfAFCntDown(Utils.getToString(history.confAFCntDown))
        .withConfNFCntDown(Utils.getToString(history.confNFCntDown))
        .withConfFCntUp(Utils.getToString(history.confFCntUp))
        .withLfd(mapLfdList(history.lfd))
        .withTs(Utils.getToString(history.transmissionSlot))
        .withRul(Utils.getToString(history.forRepeatedUL))
        .withFreq(Utils.getToString(history.frequency))
        .withClazz(Utils.getToString(history.dynamicClass))
        .withBper(Utils.getToString(history.classBPeriodicity))
        .withNotifTp(Utils.getToString(history.notificationType))
        .withResetTp(Utils.getToString(history.deviceResetType))
        .withPayloadDriverId(history.getPayloadDriverId())
        .withPayloadDriverModel(history.getSdriverModel())
        .withPayloadDriverApplication(history.getSdriverApplication())
        .withPayloadDecodingError(history.getPayloadDecodingError())
        .withLcMU(Utils.getToString(history.locMethodUsed))
        .withPayloadEncryption(Utils.getToStringOrNull(history.payloadEncrypted))
        .withTags(tagsString)
        .withRfRegion(Utils.getToString(history.rfRegion))
        .withForeignOperatorNSID(Utils.getToString(history.foreignOperatorNSID))
        .withAsReportDeliveryID(Utils.getToString(history.asReportDeliveryID))
        .withAsRecipients(mapRecipientList(history.asRecipients))
        .build();

    decodedLoraHistory.setPayloadDecoder(payloadDecoder);
    decodedLoraHistory.setWrapper(wrapper);

    return decodedLoraHistory;
  }

  @Nonnull
  private List<Lrr> mapLrrList(@Nullable List<DeviceHistory.Lrr> lrrList) {
    if (lrrList == null) {
      return emptyList();
    }
    return lrrList.stream()
        .map(
            lrr ->
                Lrr.builder()
                    .withLrrId(lrr.lrrId)
                    .withRssi(lrr.rssi)
                    .withSnr(lrr.snr)
                    .withEsp(lrr.esp)
                    .withInstantPer(lrr.instantPer)
                    .withNetworkPartnerId(lrr.networkPartnerId)
                    .withForeignOperatorNetId(lrr.foreignOperatorNetId)
                    .withGwId(lrr.gwId)
                    .withGwToken(lrr.gwToken)
                    .withGwDLAllowed(lrr.gwDLAllowed)
                    .withIsmBand(lrr.ismBand)
                    .withRfRegion(lrr.rfRegion)
                    .withForeignOperatorNSID(lrr.foreignOperatorNSID)
                    .withChains(mapLrrChainList(lrr.chains))
                    .build())
        .collect(toList());
  }

  @Nonnull
  private List<Recipient> mapRecipientList(@Nullable List<DeviceHistory.Recipient> recipientList) {
    if (recipientList == null) {
      return emptyList();
    }
    return recipientList.stream()
            .map(
                    recipient ->
                            Recipient.builder()
                                    .withId(recipient.id)
                                    .withStatus(recipient.status)
//                                    .withDropped(recipient.dropped)
                                    .withDestinations(mapDestinationsList(recipient.destinations))
                                    .build())
            .collect(toList());
  }

  @Nonnull
  private List<Recipient.Destination> mapDestinationsList(@Nullable List<DeviceHistory.Recipient.Destination> destinationList) {
    if (destinationList == null) {
      return emptyList();
    }
    return destinationList.stream()
            .map(
                    destination ->
                            Recipient.Destination.builder()
                                    .withIdx(destination.idx)
                                    .withUrl(destination.url)
                                    .withStatus(destination.status)
                                    .withErrorMessage(destination.errorMessage)
                                    .build())
            .collect(toList());
  }

  @Nonnull
  private List<Lfd> mapLfdList(@Nullable List<DeviceHistory.Lfd> lfdList) {
    if (lfdList == null) {
      return emptyList();
    }
    return lfdList.stream()
        .map(lfd -> Lfd.builder().withCnt(lfd.cnt).withDfc(lfd.dfc).build())
        .collect(toList());
  }

  @Nonnull
  private List<Lrr.Chain> mapLrrChainList(@Nullable List<DeviceHistory.Lrr.Chain> chainList) {
    if (chainList == null) {
      return emptyList();
    }
    return chainList.stream()
        .map(
            chain ->
                Lrr.Chain.builder()
                    .withChain(chain.chain)
                    .withChannel(chain.channel)
                    .withTimestamp(chain.timestamp)
                    .withTimestampType(chain.timestampType)
                    .withNanoseconds(chain.nanoseconds)
                    .build())
        .collect(toList());
  }

  private String getInfoESPFromLRR(List<DeviceHistory.Lrr> lrrs, String lrrId) {
    if (lrrs == null || lrrs.isEmpty()) {
      return "null";
    }
    return lrrs.stream()
        .filter(lrr -> isNotBlank(lrr.lrrId))
        .filter(lrr -> lrr.lrrId.equals(lrrId))
        .findFirst()
        .map(lrr -> Utils.getToStringOrNull(lrr.esp))
        .orElse("null");
  }

  private static String calculDist(Double lat1, Double lon1, Double lat2, Double lon2, String unit) {
    if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
      if (lat1 == null && lon1 == null) {
        return MINUS_ONE;
      }
      return ZERO;
    }
    if ((Double.compare(lat1,lat2) == 0) && (Double.compare(lon1,lon2) == 0)) {
      return ZERO;
    }
    // protection for bad values
    Double zero = 0d;
    if (Double.compare(lat1,zero) == 0
            || Double.compare(lon1,zero) == 0
            || Double.compare(lat2,zero) == 0
            || Double.compare(lon2,zero) == 0) {
      return MINUS_ONE;
    }

    double rlat1 = Math.PI * lat1 / 180;
    double rlat2 = Math.PI * lat2 / 180;

    double theta = lon1 - lon2;
    double rtheta = Math.PI * theta / 180;

    double dist =
        Math.sin(rlat1) * Math.sin(rlat2) + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rtheta);
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.1515;

    double unitK = dist = dist * 1.609344;
    // distance impossible if > earth perimeter
    if (unitK > 40075) {
      return MINUS_ONE;
    }
    if (unit.equals("K")) {
      dist = unitK;
    }
    if (unit.equals("M")) {
      dist = dist * 1000;
    }
    if (unit.equals("N")) {
      dist = dist * 0.8684;
    }
    return String.valueOf((int) dist);
  }

  private IPV4WLWrapper getIPV4WLWrapper() throws Exception {
    return (IPV4WLWrapper) WLWrapperFactory.getWLWrapper(DecoderType.IPV4);
  }

  private IPV4Decoder getIPV4Decoder() throws Exception {
    return (IPV4Decoder) DecoderFactory.getDecoder(DecoderType.IPV4);
  }

  private MacDecoder getMacDecoder() throws Exception {
    return (MacDecoder) DecoderFactory.getDecoder(DecoderType.MAC);
  }

  // Just for test
  public Decoder getDecoder(String name) throws Exception {
    return DecoderFactory.getDecoder(name);
  }

  // Just for test
  public WLWrapper getWLWrapper(String name) throws Exception {
    return WLWrapperFactory.getWLWrapper(name);
  }

  public String getMTypeText(Integer mType) {

    if (mType == null)
      return "mType null";

    switch (mType) {
      case 0:
        return "JoinRequest";
      case 1:
        return "JoinAccept";
      case 2:
        return "UnconfirmedDataUp";
      case 3:
        return "UnconfirmedDataDown";
      case 4:
        return "ConfirmedDataUp";
      case 5:
        return "ConfirmedDataDown";
      case 6:
        return "RFU";
      case 7:
        return "Proprietary";
    }
    return null;
  }

  public String decodeDeliveryStatus(Integer status) {
    if (status == null)
      return "Unknown";
    switch (status) {
      case 0:
        return "Failed";
      case 1:
        return "Sent";
    }
    return "Unknown (" + status + ")";
  }

  public String decodeDeliveryFailedCause1(String deliveryFailedCause1) {
    if (deliveryFailedCause1 == null || deliveryFailedCause1.length() == 0)
      return "";
    if (deliveryFailedCause1.equals("00"))
      return "Success";
    if (deliveryFailedCause1.length() != 2)
      return "";
    String class_ = deliveryFailedCause1.substring(0, 1);
    String message = "";
    if (class_.equals("A"))
      message = "Class A: Transmission slot busy on RX1";
    else if (class_.equals("B"))
      message = "Class A: Too late for RX1";
    else if (class_.equals("C"))
      message = "Class A: LRC selects RX2";
    else if (class_.equals("D"))
      message = "Class A: DC or gateway constraint on RX1";
    else if (class_.equals("E"))
      message = "Class A: Frame expired before transmission";
    else
      message = "Unknown cause";
    message = message + " (" + deliveryFailedCause1 + ")";
    return message;
  }

  public String decodeDeliveryFailedCause2(String deliveryFailedCause2) {
    if (deliveryFailedCause2 == null || deliveryFailedCause2.length() == 0)
      return "";
    if (deliveryFailedCause2.equals("00"))
      return "Success";
    if (deliveryFailedCause2.length() != 2)
      return "";
    String class_ = deliveryFailedCause2.substring(0, 1);
    String message = "";
    if (class_.equals("A"))
      message = "Class A: Transmission slot busy on RX2";
    else if (class_.equals("B"))
      message = "Class A: Too late for RX2";
      //else if (class_.equals("C"))
      //message = "Class A: LRC selected RX2";
    else if (class_.equals("D"))
      message = "Class A: DC or gateway constraint on RX2";
    else if (class_.equals("E"))
      message = "Class C: Frame expired before transmission";
    else if (class_.equals("F"))
      message = "Class C: Multicast failure";
    else
      message = "Unknown cause";
    message = message + " (" + deliveryFailedCause2 + ")";
    return message;
  }

  public String decodeDeliveryFailedCause3(String deliveryFailedCause3) {
    if (deliveryFailedCause3 == null || deliveryFailedCause3.length() == 0)
      return "";
    if (deliveryFailedCause3.equals("00"))
      return "Success";
    if (deliveryFailedCause3.length() != 2)
      return "";
    String class_ = deliveryFailedCause3.substring(0, 1);
    String message = "";
    if (class_.equals("A"))
      message = "Class B: Transmission slot busy on ping slot";
    else if (class_.equals("B"))
      message = "Class B: Too late for ping slot";
    else if (class_.equals("D"))
      message = "Class B: DC or gateway constraint on ping slot";
    else if (class_.equals("F"))
      message = "Class B: Multicast failure";
    else
      message = "Unknown cause";
    message = message + " (" + deliveryFailedCause3 + ")";
    return message;
  }

}
