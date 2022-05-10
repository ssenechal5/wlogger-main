package com.actility.thingpark.twa.entity.history;


import com.actility.thingpark.smp.rest.dto.DomainRestrictionDto;
import com.actility.thingpark.smp.rest.dto.DomainTypeDto;
import com.actility.thingpark.wlogger.engine.model.Point;
import com.actility.thingpark.wlogger.model.Search;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.JsonObject;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Device Frame history. All uplink and downlink frames are kept. Frames are sent by the LoRA infrastructure
 *
 * @author Samuel Liard
 */
@RegisterForReflection
public class DeviceHistory {

    @BsonId
    public static final String ID_ID = "_id";
    @BsonProperty(ID_ID)
    public ObjectId id;

    /** Device EUI */
    public static final String DEVICE_EUI = "dvID";
    @BsonProperty(DEVICE_EUI)
    public String deviceEUI;

    /** Subscriber ID */
    public static final String SUBSCRIBER_ID = "suID";
    @BsonProperty(SUBSCRIBER_ID)
    public String subscriberID;

    /** Operator UID (SQL database UID) of the subscriber. Applicable both for uplink/downlink packets. */
    public static final String OPERATOR_UID = "opUID";
    @BsonProperty(OPERATOR_UID)
    public Long operatorUID;

    /** Owner UID (SQL database UID) of the SELECTED base station used to route the packet. Applicable both for uplink/downlink
     * packets. */
    public static final String OWNER_UID = "npUID";
    @BsonProperty(OWNER_UID)
    public Long ownerUID;

    /** Connectivity Supplier UID (SQL database UID) used to route the packet. Applicable both for uplink/downlink packets. */
    public static final String CONNECTIVITY_SUPPLIER_UID = "csUID";
    @BsonProperty(CONNECTIVITY_SUPPLIER_UID)
    public Long connectivitySupplierUID;

    /** Connectivity Plan UID (SQL database UID) used to route the packet. Applicable both for uplink/downlink packets. */
    public static final String CONNECTIVITY_PLAN_UID = "cpUID";
    @BsonProperty(CONNECTIVITY_PLAN_UID)
    public Long connectivityPlanUID;

    /** Network Partner ID */
    public static final String NETWORK_PARTNER_ID = "npId";
    @BsonProperty(NETWORK_PARTNER_ID)
    public String networkPartnerId;

    /** Uplink (0), downlink (1), location (2) or multicast summary (3) frame indicator */
    public static final String TYPE_T = "tp";
    @BsonProperty(TYPE_T)
    public Integer type;

	/**
	 * Frame subtype:
		-	LoRaWAN Uplink: 00xx
				Data only: 0000
				MAC + Data: 0001
				MAC only: 0002
				No MAC / No Data: 0003
				Join: 0004
		-	Cellular Uplink: 01xx
				Data: 0100
				Session: 0101
		-	LoRaWAN Downlink: 10xx
				Unicast Data only: 1000
				Unicast MAC + Data: 1001
				Unicast MAC only: 1002
				Unicast No MAC / No Data: 1003
				Unicast Join: 1004
				Multicast Data only: 1005
		-	Cellular Downlink: 11xx
				Unicast Data: 1100
		-	LoRaWAN Location: 2000
		-	LoRaWAN Multicast Summary: 3000
		-	Cellular Microflow: 4000
		-	LoRaWAN Device Reset: 5000	 */
	public static final String SUBTYPE_S = "stp";
	@BsonProperty(SUBTYPE_S)
	public Integer subtype;

    /** Timestamp of the frame */
    public static final String TIMESTAMP_T = "tm";
    @BsonProperty(TIMESTAMP_T)
    public Date timestamp;

    /** Timestamp indicator: GPS (0) or NTP (1) or NTP (2) */
    public static final String TIMESTAMP_TYPE = "tmTp";
    @BsonProperty(TIMESTAMP_TYPE)
    public Integer timestampType;

    /** Expiration Timestamp */
    public static final String EXPIRATION_TIMESTAMP = "dTm";
    @BsonProperty(EXPIRATION_TIMESTAMP)
    public Date expirationTimestamp;

    /** Uplink or downlink counter */
    public static final String FRAME_COUNTER = "fmCn";
    @BsonProperty(FRAME_COUNTER)
    public Long frameCounter;

    /** Payload size (Byte) */
    public static final String FRAME_SIZE = "fmSz";
    @BsonProperty(FRAME_SIZE)
    public Integer frameSize;

    /** Bitrate */
    public static final String FRAME_BITRATE = "fmBt";
    @BsonProperty(FRAME_BITRATE)
    public Float bitrate;

    /** LRC ID */
    public static final String LRC_ID = "lc";
    @BsonProperty(LRC_ID)
    public String lrcID;

    /** Ingress or egress LRR ID */
    public static final String LRR_ID = "lr";
    @BsonProperty(LRR_ID)
    public String lrrID;

    /** Ingress or egress chain */
    public static final String LRR_CHAIN = "lrCn";
    @BsonProperty(LRR_CHAIN)
    public Integer lrrChain;

    /** Ingress or egress channel */
    public static final String LRR_CHANNEL = "lrCl";
    @BsonProperty(LRR_CHANNEL)
    public String lrrChannel;

    /** The uplink or downlink token bucket limitation has been exceeded for this frame. The frame has been sent anyway (the
     * subscriber will be charged). */
    public static final String TOKEN_BUCKET_OVERFLOW = "ovr";
    @BsonProperty(TOKEN_BUCKET_OVERFLOW)
    public Boolean tokenBucketOverflow;

    /** ADR control bit received in the uplink frame. */
    public static final String ADR_CONTROL_BIT = "abit";
    @BsonProperty(ADR_CONTROL_BIT)
    public Boolean adrControlBit;

    /** MOD received */
    public static final String MOD = "mod";
    @BsonProperty(MOD)
    public Integer modulation;

    /** Uplink SF received (UPLINK only) */
    public static final String SF_R = "sf";
    @BsonProperty(SF_R)
    public Integer sf;

    /** Expected SF as requested by the LRC (UPLINK only) */
    public static final String EXPECTED_SF = "exSf";
    @BsonProperty(EXPECTED_SF)
    public Integer expectedSF;

    /** Uplink DR received (UPLINK only) */
    public static final String DR = "dr";
    @BsonProperty(DR)
    public Integer dataRate;

    /** Received Signal Strength Indicator (UPLINK only) */
    public static final String RSSI_R = "ri";
    @BsonProperty(RSSI_R)
    public Float rssi;

    /** Signal Noise Ratio (UPLINK only) */
    public static final String SNR_S = "sr";
    @BsonProperty(SNR_S)
    public Float snr;

    /** Last instantaneous PER without consideration for LRRs */
    public static final String INSTANTANEOUS_PER = "iPr";
    @BsonProperty(INSTANTANEOUS_PER)
    public Float instantPER;

    /** Last mean PER without consideration for LRRs */
    public static final String MEAN_PER = "mPr";
    @BsonProperty(MEAN_PER)
    public Float meanPER;

    /** Last instantaneous PER on the best LRR */
    public static final String BEST_LRR_INSTANTANEOUS_PER = "lIPr";
    @BsonProperty(BEST_LRR_INSTANTANEOUS_PER)
    public Float lrrInstantPER;

    /** Second LRR receiving the uplink frame (UPLINK only): RSSI or SNR */
    public static final String LRR_2_ID = "l2";
    @BsonProperty(LRR_2_ID)
    public String lrr2ID;

    /** RSSI */
    public static final String LRR_2_RSSI = "l2Ri";
    @BsonProperty(LRR_2_RSSI)
    public Float lrr2RSSI;

    /** SNR */
    public static final String LRR_2_SNR = "l2Sr";
    @BsonProperty(LRR_2_SNR)
    public Float lrr2SNR;

    /** Last instantaneous PER on the LRR2 */
    public static final String LRR_2_INSTANTANEOUS_PER = "l2IPr";
    @BsonProperty(LRR_2_INSTANTANEOUS_PER)
    public Float lrr2InstantPER;

    /** Third LRR receiving the uplink frame (UPLINK only): RSSI or SNR */
    public static final String LRR_3_ID = "l3";
    @BsonProperty(LRR_3_ID)
    public String lrr3ID;

    /** RSSI */
    public static final String LRR_3_RSSI = "l3Ri";
    @BsonProperty(LRR_3_RSSI)
    public Float lrr3RSSI;

    /** SNR */
    public static final String LRR_3_SNR = "l3Sr";
    @BsonProperty(LRR_3_SNR)
    public Float lrr3SNR;

    /** Last instantaneous PER on the LRR2 */
    public static final String LRR_3_INSTANTANEOUS_PER = "l3IPr";
    @BsonProperty(LRR_3_INSTANTANEOUS_PER)
    public Float lrr3InstantPER;

    /** Battery level when available (UPLINK only) */
    public static final String BATTERY_LEVEL = "baLv";
    @BsonProperty(BATTERY_LEVEL)
    public Integer batteryLevel;

    /** Battery level reporting timestamp (UPLINK only) */
    public static final String BATTERY_TIME = "baTm";
    @BsonProperty(BATTERY_TIME)
    public Date batteryTime;

    public static final String LOCATION_LOC = "loc";
    @BsonProperty(LOCATION_LOC)
    public List<Double> location;

    /** Trusting indicator on the location calculation performed by the LRR - meter (UPLINK only) */
    public static final String LOC_RADIUS = "rad";
    @BsonProperty(LOC_RADIUS)
    public Double devLocRadius;

    /** Localization reporting timestamp (UPLINK only) */
    public static final String LOC_TIME = "lcTm";
    @BsonProperty(LOC_TIME)
    public Date devLocTime;

    /** True when an ACK has been requested by the Device (UPLINK only) */
    public static final String ACK_REQUESTED = "ack";
    @BsonProperty(ACK_REQUESTED)
    public Boolean ackRequested;

    /** True when the uplink packet ACK a downlink packet (UPLINK only) */
    public static final String ACK_INDICATOR = "aInd";
    @BsonProperty(ACK_INDICATOR)
    public Boolean ackIndicator;

    public static final String FPORT_F = "fp";
    @BsonProperty(FPORT_F)
    public Integer fport;

    public static final String MTYPE_M = "mt";
    @BsonProperty(MTYPE_M)
    public Integer mtype;

    public static final String DEVICE_ADDRESS = "adr";
    @BsonProperty(DEVICE_ADDRESS)
    public String deviceAddress;

    public static final String AIRTIME_A = "atm";
    @BsonProperty(AIRTIME_A)
    public Double airtime;

    public static final String DELIVERY_STATUS = "ds";
    @BsonProperty(DELIVERY_STATUS)
    public Integer deliveryStatus;

    public static final String DELIVERY_FAILED_CAUSE_1 = "dfc1";
    @BsonProperty(DELIVERY_FAILED_CAUSE_1)
    public String deliveryFailedCause1;

    public static final String DELIVERY_FAILED_CAUSE_2 = "dfc2";
    @BsonProperty(DELIVERY_FAILED_CAUSE_2)
    public String deliveryFailedCause2;

    /** Delivery Failure Cause 3 (DOWNLINK only) Only set when delivery failed on ping slot Ax: Class B: Transmission slot busy
     * Bx: Class B: Too late for ping slot Dx: Class A: DC constraint on ping slot */
    public static final String DELIVERY_FAILED_CAUSE_3 = "dfc3";
    @BsonProperty(DELIVERY_FAILED_CAUSE_3)
    public String deliveryFailedCause3;

    public static final String MAC_REQUEST_PRESENT = "mrqp";
    @BsonProperty(MAC_REQUEST_PRESENT)
    public Boolean macRequestPresent;

    public static final String MAC_RESPONSE_PRESENT = "mrpp";
    @BsonProperty(MAC_RESPONSE_PRESENT)
    public Boolean macResponsePresent;

    public static final String ALTITUDE_A = "alt";
    @BsonProperty(ALTITUDE_A)
    public Double altitude;

    public static final String ALTITUDE_RADIUS = "aRad";
    @BsonProperty(ALTITUDE_RADIUS)
    public Double altitudeRadius;

    /** Velocity (NORTH axis) expressed in m/s. */
    public static final String VELOCITY_NORTH = "vnor";
    @BsonProperty(VELOCITY_NORTH)
    public Double velocityNorth;

    /** Velocity (EAST axis) expressed in m/s. */
    public static final String VELOCITY_EAST = "veas";
    @BsonProperty(VELOCITY_EAST)
    public Double velocityEast;

    /** Uplink or downlink payload in hexa */
    public static final String PAYLOAD_P = "pld";
    @BsonProperty(PAYLOAD_P)
    public String payload;

    /** Uplink or downlink MIC in hexa ASCII */
    public static final String MIC_M = "mic";
    @BsonProperty(MIC_M)
    public String mic;

    /** Ingress or egress sub-band (e.g. G1) */
    public static final String SUB_BAND = "sb";
    @BsonProperty(SUB_BAND)
    public String subBand;

    /** LRR latitude/longitude (UPLINK only) */
    public static final String LRR_LOCATION = "lrLoc";
    @BsonProperty(LRR_LOCATION)
    public List<Double> lrrLocation;

    /** Number of LRR that received the signal */
    public static final String LRR_COUNT = "lrCnt";
    @BsonProperty(LRR_COUNT)
    public Integer lrrCount;

    /** Customer Data (UPLINK only) */
    public static final String CUSTOMER_DATA = "cuDa";
    @BsonProperty(CUSTOMER_DATA)
    public String customerData;

    /** Uplink or downlink MAC commands in */
    public static final String MAC_COMMANDS = "maCo";
    @BsonProperty(MAC_COMMANDS)
    public String macCommands;

    /** RX delay requested by the LRC */
    public static final String LRC_RX_DELAY_REQUESTED = "lcRxDy";
    @BsonProperty(LRC_RX_DELAY_REQUESTED)
    public Integer lrcRxDelayRequested;

    /** ADR acknowledgment request bit */
    public static final String ADR_ACK_REQUESTED_BIT = "aAcRq";
    @BsonProperty(ADR_ACK_REQUESTED_BIT)
    public Boolean adrAckRequested;

    /** RX delay requested by the LRC */
    public static final String PENDING_DOWNLINK_FRAME_INDICATOR = "fPg";
    @BsonProperty(PENDING_DOWNLINK_FRAME_INDICATOR)
    public Boolean pendingDownlinkFrameIndicator;

    /** The magnitude of one standard deviation of the tolerance of geolocation estimation in units of meters */
    public static final String LOCATION_ACCURACY = "acc";
    @BsonProperty(LOCATION_ACCURACY)
    public Double locationAccuracy;

    /** LORA uplink, downlink or location counter */
    public static final String LORA_FRAME_COUNTER = "fCnt";
    @BsonProperty(LORA_FRAME_COUNTER)
    public Integer loraFrameCounter;

    /** Order list of LRRs */
    public static final String LRRS_L = "lrrs";
    @BsonProperty(LRRS_L)
    public List<Lrr> lrrs;

    /** Indicate that this is a late reporting of an uplink frame from the long term queue (UPLINK only) */
    public static final String LATE_REPORTING = "late";
    @BsonProperty(LATE_REPORTING)
    public Boolean lateReporting;

    /** Class B bit (UPLINK only) */
    public static final String CLASS_B_BIT = "bbit";
    @BsonProperty(CLASS_B_BIT)
    public Boolean classBBit;

    /** Best GW ID */
    public static final String BEST_GW_ID = "gwID";
    @BsonProperty(BEST_GW_ID)
    public String bestGWId;

    /** Best GW Token */
    public static final String BEST_GW_TOKEN = "gwTk";
    @BsonProperty(BEST_GW_TOKEN)
    public String bestGWToken;

    /** Foreign Operator Net ID */
    public static final String FOREIGN_OPERATOR_NET_ID = "gwOp";
    @BsonProperty(FOREIGN_OPERATOR_NET_ID)
    public String foreignOperatorNetId;

    /** ISM band reported by the LRR */
    public static final String ISM_BAND = "ism";
    @BsonProperty(ISM_BAND)
    public String ismBand;

    /** UPLINK: list of Application Server IDs that received the uplink frame (regardless of the result of the sending of the
     * uplink frame to the AS)
     *
     * DOWNLINK: Application Server that submitted the downlink payload (only applicable when the downlink frame contains an
     * applicative payload) */
    public static final String APPLICATION_SERVERS_ID = "asID";
    @BsonProperty(APPLICATION_SERVERS_ID)
    public List<String> applicationServerIDs;

    /** P-GW ID */
    public static final String PGW_ID = "mc";
    @BsonProperty(PGW_ID)
    public String pgwID;

    /** Session deletion cause (applicable when mt=2) */
    public static final String SESSION_DELETION_CAUSE = "cse";
    @BsonProperty(SESSION_DELETION_CAUSE)
    public Integer sessionDeletionCause;

    /** EPS Bearer ID */
    public static final String EPS_BEARER_ID = "ebi";
    @BsonProperty(EPS_BEARER_ID)
    public Integer epsBearerID;

    /** APN */
    public static final String APN_A = "apn";
    @BsonProperty(APN_A)
    public String apn;

    /** MSISDN number E.164 */
    public static final String MSISDN_NUMBER = "msn";
    @BsonProperty(MSISDN_NUMBER)
    public String msisdnNumber;

    /** IMEI terminal identifier */
    public static final String IMEI_I = "imei";
    @BsonProperty(IMEI_I)
    public String imei;

    /** IMSI of the SIM card (16 digits) */
    public static final String IMSI_I = "imsi";
    @BsonProperty(IMSI_I)
    public String imsi;

    /** Radio Access Type */
    public static final String RADIO_ACCESS_TYPE = "rat";
    @BsonProperty(RADIO_ACCESS_TYPE)
    public String radioAccessType;

    /** Cell ID */
    public static final String CELL_ID = "ci";
    @BsonProperty(CELL_ID)
    public String cellID;

    /** Cell MCC/MNC */
    public static final String CELL_MCC_MNC = "cm";
    @BsonProperty(CELL_MCC_MNC)
    public String cellMCCMNC;

    /** Cell Tracking Area Number */
    public static final String CELL_TRACKING_AREA_NUMBER = "ct";
    @BsonProperty(CELL_TRACKING_AREA_NUMBER)
    public String cellTrackingAreaNumber;

    /** Serving Network MCC/MNC */
    public static final String SERVING_NETWORK_MCC_MNC = "sn";
    @BsonProperty(SERVING_NETWORK_MCC_MNC)
    public String servingNetworkMCCMNC;

    /** MulticastClass MCC */
    public static final String MULTI_CAST_CLASS = "mcc";
    @BsonProperty(MULTI_CAST_CLASS)
    public String multiCastClass;

    /** Multicast downlink transmission status: PARTIAL/SUCCESS/ABORTED */
    public static final String MCC_TRANSMISSION_STATUS = "tss";
    @BsonProperty(MCC_TRANSMISSION_STATUS)
    public String mccTransmissionStatus;

    /** Number of LRRs associated with the multicast group */
    public static final String MCC_LRR_COUNT = "lcn";
    @BsonProperty(MCC_LRR_COUNT)
    public Integer mccLrrCount;

    /** Number of LRRs having successfully transmitted the downlink frame over the air */
    public static final String MCC_LRR_SUCCESS_COUNT = "lsc";
    @BsonProperty(MCC_LRR_SUCCESS_COUNT)
    public Integer mccLrrSuccessCount;

    /** Number of LRRs that failed in transmitting the downlink frame over the air */
    public static final String MCC_LRR_FAILED_COUNT = "lfc";
    @BsonProperty(MCC_LRR_FAILED_COUNT)
    public Integer mccLrrFailedCount;

    /** Roaming type */
    public static final String ROAMING_TYPE = "rt";
    @BsonProperty(ROAMING_TYPE)
    public Integer roamingType;

    /** Roaming result */
    public static final String ROAMING_RESULT = "rr";
    @BsonProperty(ROAMING_RESULT)
    public String roamingResult;

    /** TRUE if the device or the multicast group is associated with the managed customer network (copied from DeviceReport)
     * (UPLINK/DOWNLINK only) */
    public static final String MULTI_CAST_GROUP_ACTIVATION = "mcn";
    @BsonProperty(MULTI_CAST_GROUP_ACTIVATION)
    public Boolean multiCastGroupActivation;

    /** Private LRR Group ID matching the best LRR (UPLINK/DOWNLINK only) */
    public static final String PRIVATE_LRR_GROUP_ID = "plg";
    @BsonProperty(PRIVATE_LRR_GROUP_ID)
    public String privateLrrGroupID;

    /** Microflow uplink bytes (MICROFLOW only) */
    public static final String MICROFLOW_UPLINK_SIZE = "mfSzU";
    @BsonProperty(MICROFLOW_UPLINK_SIZE)
    public Integer microflowUplinkSize;

    /** Microflow downlink bytes (MICROFLOW only) */
    public static final String MICROFLOW_DOWNLINK_SIZE = "mfSzD";
    @BsonProperty(MICROFLOW_DOWNLINK_SIZE)
    public Integer microflowDownlinkSize;

    /** Duration of the Microflow (in seconds) (MICROFLOW only) */
    public static final String MICROFLOW_DURATION = "mfDur";
    @BsonProperty(MICROFLOW_DURATION)
    public Long microflowDuration;

    /** NFR 1061 */
    public static final String AFCOUNT_DOWN = "afCnt";
    @BsonProperty(AFCOUNT_DOWN)
    public Integer aFCntDown;

    public static final String NFCOUNT_DOWN = "nfCnt";
    @BsonProperty(NFCOUNT_DOWN)
    public Integer nFCntDown;

    /** NFR 1062 */
    public static final String CONF_AFCOUNT_DOWN = "cAfCntD";
    @BsonProperty(CONF_AFCOUNT_DOWN)
    public Integer confAFCntDown;

    public static final String CONF_NFCOUNT_DOWN = "cNfCntD";
    @BsonProperty(CONF_NFCOUNT_DOWN)
    public Integer confNFCntDown;

    public static final String CONF_FCOUNT_UP = "cfCntU";
    @BsonProperty(CONF_FCOUNT_UP)
    public Integer confFCntUp;

    /** List of LFDs */
    public static final String LFD_L = "lfd";
    @BsonProperty(LFD_L)
    public List<Lfd> lfd;

	/**
	 * Slot used for downlink frame transmission: 0 (Unknown), 1 (RX1), 2 (RX2) or 3 (Ping Slot) (DOWNLINK only)
	 */
	public static final String TRANSMISSION_SLOT = "ts";
	@BsonProperty(TRANSMISSION_SLOT)
	public Integer transmissionSlot;

	/**
	 * UL repeated
	 */
	public static final String FOR_REPEATED_UL = "rul";
	@BsonProperty(FOR_REPEATED_UL)
	public Boolean forRepeatedUL;

    /** the uplink radio frequency in MHz */
    public static final String FREQUENCY_F = "freq";
    @BsonProperty(FREQUENCY_F)
    public Double frequency;

	public static final String DOWNLINK_DC = "dcLC";
	@BsonProperty(DOWNLINK_DC)
	public Double downlinkDC;

	public static final String DOWNLINK_DC_SUBBAND = "dcSB";
	@BsonProperty(DOWNLINK_DC_SUBBAND)
	public Double downlinkDCSubBand;

    /** LoRaWAN class currently used by the Device (UPLINK only) */
    public static final String DYNAMIC_CLASS = "class";
    @BsonProperty(DYNAMIC_CLASS)
    public String dynamicClass;

    /** Class B ping-slot period in seconds requested by the device (UPLINK only) */
    public static final String CLASS_B_PERIODICITY = "bper";
    @BsonProperty(CLASS_B_PERIODICITY)
    public Integer classBPeriodicity;

    /** Type of notification: "reset" (NOTIFICATION only) */
    public static final String TYPE_OF_NOTIF = "notifTp";
    @BsonProperty(TYPE_OF_NOTIF)
    public String notificationType;

    /** Type of device reset: "automatic_reset", "resetind" or "admin_reset" (NOTIFICATION only) */
    public static final String TYPE_OF_DEVICE_RESET = "resetTp";
    @BsonProperty(TYPE_OF_DEVICE_RESET)
    public String deviceResetType;

    /** Driver selection metadata: (UPLINK/DOWNLINK only)
         - Model: { Producer ID, Module ID, Version }
         - Application: { Producer ID, Module ID, Version } */
    public static final String DRIVER_METADATA = "dc";
    @BsonProperty(DRIVER_METADATA)
    public String driverMetadata;

	/**
	 * Localization method used by the location solver: Either "TIME" (for TDoA) or "RSSI"
	 */
	public static final String LOC_METHOD_USED = "lcMU";
	@BsonProperty(LOC_METHOD_USED)
	public String locMethodUsed;

    /** Encryption status of applicative payload: 0 (clear), 1 (encrypted) (UPLINK/DOWNLINK only) Only applicable to LoRaWAN device */
    public static final String PAYLOAD_ENCRYPTED = "pldE";
    @BsonProperty(PAYLOAD_ENCRYPTED)
    public Integer payloadEncrypted;

    /** RF Region ID (UPLINK/DOWNLINK only)  */
    public final static String RF_REGION = "rfrg";
    @BsonProperty(RF_REGION)
    public String rfRegion;

    /** ForeignOperatorNSID (UPLINK/DOWNLINK only)    */
    public final static String FOREIGN_OPERATOR_NSID = "gwNSID";
    @BsonProperty(FOREIGN_OPERATOR_NSID)
    public String foreignOperatorNSID;

    /** Unique ID assigned by the LRC to the delivery process of the AS report */
    public final static String AS_REPORT_DELIVERY_ID = "asRDID";
    @BsonProperty(AS_REPORT_DELIVERY_ID)
    public String asReportDeliveryID;

    /** list of Recipients */
    public final static String AS_RECIPIENTS = "asR";
    @BsonProperty(AS_RECIPIENTS)
    public List<Recipient> asRecipients;

    /**
     * List of domains associated with the Device
     */
    public final static String DOMAINS_DEVICE = "devDoms";
    @BsonProperty(DOMAINS_DEVICE)
    public List<DomainMongo> deviceDomains = new ArrayList<>();

    /**
     * List of domains associated with the best/selected GW
     *  (UPLINK/DOWNLINK only)
     */
    public final static String DOMAINS_BS = "bsDoms";
    @BsonProperty(DOMAINS_BS)
    public List<DomainMongo> bsDomains = new ArrayList<>();

    private String driverModel;
    private String driverApplication;
    private String sdriverModel;
    private String sdriverApplication;
    private String producerId;
    private String moduleId;
    private String version;
    private String producerIdApp;
    private String moduleIdApp;
    private String versionApp;

    private String payloadDecoded;
    private String payloadDriverId;
    private String payloadDecodingError;
    private Double decodedLongitude;
    private Double decodedLatitude;

    private List<DomainRestrictionDto> customerRestrictions;

    public List<DomainRestrictionDto> getCustomerRestrictions() {
        return customerRestrictions;
    }

    public void setCustomerRestrictions(List<DomainRestrictionDto> customerRestrictions) {
        this.customerRestrictions = customerRestrictions;
    }

    public String getDriverModel() {
        return driverModel;
    }

    public void setDriverModel(String driverModel) {
        this.driverModel = driverModel;
    }

    public String getDriverApplication() {
        return driverApplication;
    }

    public void setDriverApplication(String driverApplication) {
        this.driverApplication = driverApplication;
    }

    public String getSdriverModel() {
        return sdriverModel;
    }

    public void setSdriverModel(String sdriverModel) {
        this.sdriverModel = sdriverModel;
    }

    public String getSdriverApplication() {
        return sdriverApplication;
    }

    public void setSdriverApplication(String sdriverApplication) {
        this.sdriverApplication = sdriverApplication;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProducerIdApp() {
        return producerIdApp;
    }

    public void setProducerIdApp(String producerIdApp) {
        this.producerIdApp = producerIdApp;
    }

    public String getModuleIdApp() {
        return moduleIdApp;
    }

    public void setModuleIdApp(String moduleIdApp) {
        this.moduleIdApp = moduleIdApp;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getPayloadDecoded() {
        return payloadDecoded;
    }

    public void setPayloadDecoded(String payloadDecoded) {
        this.payloadDecoded = payloadDecoded;
    }

    public String getPayloadDriverId() {
        return payloadDriverId;
    }

    public void setPayloadDriverId(String payloadDriverId) {
        this.payloadDriverId = payloadDriverId;
    }

    public String getPayloadDecodingError() {
        return payloadDecodingError;
    }

    public void setPayloadDecodingError(String payloadDecodingError) {
        this.payloadDecodingError = payloadDecodingError;
    }

    public Double getDecodedLongitude() {
        return decodedLongitude;
    }

    public void setDecodedLongitude(Double decodedLongitude) {
        this.decodedLongitude = decodedLongitude;
    }

    public Double getDecodedLatitude() {
        return decodedLatitude;
    }

    public void setDecodedLatitude(Double decodedLatitude) {
        this.decodedLatitude = decodedLatitude;
    }

    public boolean isCustomerRestricted() {
        if (this.getCustomerRestrictions() != null && this.getCustomerRestrictions().size()>0) {
            for (DomainRestrictionDto and : this.getCustomerRestrictions()) {
                DomainMongo domainMongo = this.getDomainFromDevDoms(and.getGroup().getName());
                if (domainMongo == null) {
                  return true;
                } else {
                    if (and.getType() != null && and.getType().equals(DomainTypeDto.FULL)) {
                        if (!domainMongo.name.equals(and.getName()))
                            return true;
                    } else {
                        if (!domainMongo.name.startsWith(and.getName()+"/"))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public DomainMongo getDomainFromDevDoms(String name) {
        List<DomainMongo> list = this.deviceDomains.stream().filter(domain -> domain.group.equals(name)).collect(Collectors.toList());
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /** Recipient */
    @RegisterForReflection
    public static class Recipient {

        public final static String ID = "ID";
        @BsonProperty(ID)
        public String id;

        public final static String STATUS = "st";
        @BsonProperty(STATUS)
        public String status;

//        public final static String DROPPED = "dr";
//        @BsonProperty(DROPPED)
//        public Boolean dropped;

        /** Order list of Chains */
        public final static String DESTINATIONS = "des";
        @BsonProperty(DESTINATIONS)
        public List<Destination> destinations;

        /** Destination */
        @RegisterForReflection
        public static class Destination {

            public final static String IDX = "idx";
            @BsonProperty(IDX)
            public Integer idx;

            public final static String URL = "url";
            @BsonProperty(URL)
            public String url;

            public final static String STATUS = "st";
            @BsonProperty(STATUS)
            public String status;

            public final static String ERROR_MESSAGE = "er";
            @BsonProperty(ERROR_MESSAGE)
            public String errorMessage;
        }
    }

    /** Lfd */
    @RegisterForReflection
    public static class Lfd {

        public static final String DFC_D = "dfc";
        @BsonProperty(DFC_D)
        public String dfc;

        public static final String CNT_C = "cnt";
        @BsonProperty(CNT_C)
        public Integer cnt;
    }

    /** Lrr */
    @RegisterForReflection
    public static class Lrr {

        public static final String LRR_ID = "lr";
        @BsonProperty(LRR_ID)
        public String lrrId;

        public static final String RSSI_R = "ri";
        @BsonProperty(RSSI_R)
        public Float rssi;

        public static final String SNR_S = "sr";
        @BsonProperty(SNR_S)
        public Float snr;

        public static final String ESP_E = "esp";
        @BsonProperty(ESP_E)
        public Float esp;

        public static final String INSTANT_PER = "iPr";
        @BsonProperty(INSTANT_PER)
        public Float instantPer;

        public static final String NETWORK_PARTNER_ID = "npId";
        @BsonProperty(NETWORK_PARTNER_ID)
        public String networkPartnerId;

        public static final String FOREIGN_OPERATOR_NET_ID = "gwOp";
        @BsonProperty(FOREIGN_OPERATOR_NET_ID)
        public String foreignOperatorNetId;

        public static final String GW_ID = "gwID";
        @BsonProperty(GW_ID)
        public String gwId;

        public static final String GW_TOKEN = "gwTk";
        @BsonProperty(GW_TOKEN)
        public String gwToken;

        public static final String GW_DL_ALLOWED = "gwDL";
        @BsonProperty(GW_DL_ALLOWED)
        public Boolean gwDLAllowed;

        public final static String ISM = "ism";
        @BsonProperty(ISM)
        public String ismBand;

        public final static String RF_REGION = "rfrg";
        @BsonProperty(RF_REGION)
        public String rfRegion;

        public final static String FOREIGN_OPERATOR_NSID = "gwNSID";
        @BsonProperty(FOREIGN_OPERATOR_NSID)
        public String foreignOperatorNSID;

        /** Order list of Chains */
        public static final String CHAINS_C = "cns";
        @BsonProperty(CHAINS_C)
        public List<Chain> chains;

        /** Chain */
        @RegisterForReflection
        public static class Chain {

            public static final String CHAIN_C = "cn";
            @BsonProperty(CHAIN_C)
            public Integer chain;

            public static final String CHANNEL_C = "cl";
            @BsonProperty(CHANNEL_C)
            public String channel;

            public static final String TIMESTAMP_T = "tm";
            @BsonProperty(TIMESTAMP_T)
            public Date timestamp;

            public static final String TIMESTAMP_TYPE = "tmTp";
            @BsonProperty(TIMESTAMP_TYPE)
            public Integer timestampType;

            public static final String NANOSECONDS_N = "ns";
            @BsonProperty(NANOSECONDS_N)
            public Integer nanoseconds;
        }
    }

    @BsonIgnore
    public Double getLatitude() {
        if ((this.location != null) && (this.location.size() == 2)) {
            return this.location.get(0);
        } else {
            return null;
        }
    }

    @BsonIgnore
    public Double getLongitude() {
        if ((this.location != null) && (this.location.size() == 2)) {
            return this.location.get(1);
        } else {
            return null;
        }
    }

    @BsonIgnore
    public Double getLRRLatitude() {
        if ((this.lrrLocation != null) && (this.lrrLocation.size() == 2)) {
            return this.lrrLocation.get(0);
        } else {
            return null;
        }
    }

    @BsonIgnore
    public Double getLRRLongitude() {
        if ((this.lrrLocation != null) && (this.lrrLocation.size() == 2)) {
            return this.lrrLocation.get(1);
        } else {
            return null;
        }
    }

    public DeviceHistory fillCustomerRestrictions(Search search) {
        if (search.getDomainRestrictions() != null && search.getDomainRestrictions().getAnds() != null
                && search.getDomainRestrictions().getAnds().size() > 0) {
            setCustomerRestrictions(search.getDomainRestrictions().getAnds());
        }
        return this;
    }

    public DeviceHistory fillModelApplication() {
        if (driverMetadata != null && driverMetadata.length() > 0 && driverMetadata.startsWith("{")) {
          JsonObject jObject = new JsonObject(driverMetadata);
          JsonObject mod = jObject.getJsonObject("model");
          JsonObject app = jObject.getJsonObject("application");
          if (mod != null) {
            setDriverModel(mod.toString());
            setProducerId(mod.getString("producerId"));
            setModuleId(mod.getString("moduleId"));
            setVersion(mod.getString("version"));
            setSdriverModel(producerId + ":" + moduleId + ":" + version);
          }
          if (app != null) {
            setDriverApplication(app.toString());
            setProducerIdApp(app.getString("producerId"));
            setModuleIdApp(app.getString("moduleId"));
            setVersionApp(app.getString("version"));
            setSdriverApplication(producerIdApp + ":" + moduleIdApp + ":" + versionApp);
          }
        }
        return this;
    }

    public void setGeolocDecoded(Map<String, Point> points) {
        if (points != null) {
            points.values().stream()
                    .filter(point -> point.records != null)
                    .map(point -> point.records)
                    .flatMap(Collection::stream)
                    .filter(record -> record.coordinates != null && record.coordinates.size() > 1)
                    .map(record -> record.coordinates)
                    .forEach(
                            coordinates -> {
                                this.decodedLongitude = coordinates.get(0);
                                this.decodedLatitude = coordinates.get(1);
                            });
        }
    }
}