package com.actility.thingpark.wlogger;

import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.twa.entity.history.DomainMongo;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Direction;
import org.bson.types.ObjectId;

import java.util.*;

public class MongoDocumentFactory {

    public List<DeviceHistory> newDeviceHistoriesList(DeviceType deviceType, Integer size) {
        List<DeviceHistory> deviceHistoryList = new ArrayList<DeviceHistory>();
        for (int i = 0; i < size; i++) {
            deviceHistoryList.add(newDeviceHistory(deviceType, ((i % 2 == 0)? 0:1), ((i % 2 == 0)? 0:1), randomHex(24)));
        }
        return deviceHistoryList;
    }

    private static String randomHex(int size) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < size){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, size);
    }

    public DeviceHistory newDeviceHistory(DeviceType deviceType, int direction, int subtype, String id) {
        DeviceHistory history = new DeviceHistory();

        history.id = new ObjectId(id);
        // Frame type
        history.type = direction;

        // Frame subtype
        int stype;
        if (deviceType.equals(DeviceType.LTE)) {
          if (Direction.UPLINK.getValue() == direction)
              stype = 0;
          else
              stype = 1000;
        } else {
            if (Direction.UPLINK.getValue() == direction)
                stype = 100;
            else
                stype = 1100;
        }
        history.subtype = stype + subtype;

        // location
        ArrayList<Double> location = new ArrayList<Double>();
        location.add(123d);
        location.add(125d);
        history.location = location;
        history.lrrLocation = location;
        history.setDecodedLatitude(123d);
        history.setDecodedLongitude(125d);

        // Objects links to network partner
        history.ownerUID = 100L;
        history.networkPartnerId = "100";

        // Objects links to device report
        history.subscriberID ="100";
        history.operatorUID = 100L;
        history.connectivitySupplierUID = 100L;
        history.connectivityPlanUID = 100L;

        history.bitrate = 0.0f;

        history.multiCastGroupActivation = Boolean.TRUE;

        // Generic information
        history.deviceEUI = "DEVEUI";
        history.deviceAddress = "DEVADDR";
        history.lrcID = "LRCID";
        history.timestamp = new Date();
        history.expirationTimestamp = new Date();
        history.lateReporting = Boolean.FALSE;

        // Generic radio information
        history.frameCounter = 1L;
        history.confAFCntDown = 1;
        history.confNFCntDown = 1;
        history.tokenBucketOverflow = Boolean.TRUE;

        history.frameSize = 0;
        history.payload = "";

        // Miscellaneous
        history.mtype = 0;
        history.customerData = "{\"alr\":{\"pro\":\"SMTC/LoRaMote\",\"ver\":\"1\"},\"loc\":{\"lat\":\"123\",\"lon\":\"125\"},\"tags\":[\"tag1\",\"tag2\"]}";

        // App servers
        history.applicationServerIDs = Arrays.asList("");

        // Managed customer network
        history.privateLrrGroupID = "";

        // Driver config
        history.driverMetadata =
            "{\"model\":{\"producerId\":\"producerId\",\"moduleId\":\"moduleId\",\"version\":\"version\"},\"application\":{\"producerId\":\"producerId\",\"moduleId\":\"moduleId\",\"version\":\"version\"}}";

        if (deviceType.equals(DeviceType.LTE)) {
            // CELLULAR SPECIFIC FIELDS
            history.pgwID = "pgwid";
            history.sessionDeletionCause = 1;
            history.epsBearerID = 1;
            history.apn = "apn";
            history.msisdnNumber = "msisdnNumber";
            history.imei = "imei";
            history.imsi = "imsi";
            history.radioAccessType = "radioAccessType";
            history.cellID = "cellID";
            history.cellMCCMNC = "cellMCCMNC";
            history.cellTrackingAreaNumber = "cellTrackingAreaNumber";
            history.servingNetworkMCCMNC = "servingNetworkMCCMNC";
        } else {
          // LORAWAN SPECIFIC FIELDS

          // Generic LORAWAN information
          history.lrrChain = 0;
          history.lrrChannel = "channel";

          // LORAWAN radio information
          history.ismBand = "us915";
          history.airtime = 0.0d;
          history.subBand = "subBand";
          history.instantPER = 0.0f;
          history.meanPER = 0.0f;
          history.modulation = 1;
          history.sf = 1;
          history.dataRate = 1;
          history.expectedSF = 1;
          history.frequency = 123.0d;
          // Indicators

          // In case of mType == 1 (Join Request case) don't set a default value
          history.adrAckRequested = false;

          // In case of mType == 1 (Join Request case) don't set a default value
          history.adrControlBit = false;

          // In case of mType == 1 (Join Request case) don't set a default value
          history.ackRequested = false;

          // In case of mType == 1 (Join Request case) don't set a default value
          history.ackIndicator = false;

          history.mic = "micHexa";
          history.loraFrameCounter = 1;

          // In case of mType == 1 (Join Request case) don't set a default value
          history.classBBit = true;

          history.fport = 5;

          history.macCommands = "macCommands";
          history.macRequestPresent = true;
          history.macResponsePresent = false;

          // Gateway info
          history.bestGWId = "bestGW";
          history.foreignOperatorNetId = "foreignOperatorNetID";

          // LRRs
          history.lrrCount = 3;

          history.lrrID = "lrrId";
          history.rssi = 0.0f;
          history.snr = 0.0f;
          history.lrrInstantPER = 0.0f;

          history.lrr2ID = "lrrId2";
          history.lrr2RSSI = 0.0f;
          history.lrr2SNR = 0.0f;
          history.lrr2InstantPER = 0.0f;

          history.lrr3ID = "lrrId3";
          history.lrr3RSSI = 0.0f;
          history.lrr3SNR = 0.0f;
          history.lrr3InstantPER = 0.0f;

          history.batteryLevel = 50;

          history.expirationTimestamp = new Date();

          // Device Location
          history.locationAccuracy = 0.0d;
          history.devLocTime = new Date();
          history.altitude = 0.0d;
          history.devLocRadius = 0.0d;
          history.altitudeRadius = 0.0d;
          history.velocityEast = 0.0d;
          history.velocityNorth = 0.0d;
          history.locMethodUsed = "devLocMethodUsed";

          // Roaming fields
          history.roamingResult = "roamingResult";
          history.roamingType = 1;

          history.dynamicClass = "dynamicClass";
          history.classBPeriodicity = 1;

          history.payloadEncrypted = null;

          history.rfRegion = "rfRegion";
          history.foreignOperatorNSID = "foreignOperatorNSID";

          DeviceHistory.Lrr lrr = new DeviceHistory.Lrr();
          lrr.lrrId = "lrrId";
          lrr.esp = 0f;
          lrr.chains = new ArrayList<>();
          DeviceHistory.Lrr.Chain chain = new DeviceHistory.Lrr.Chain();
          chain.chain = 1;
          chain.channel = "channel";
          chain.nanoseconds = 0;
          chain.timestamp = new Date();
          chain.timestampType = 0;
          lrr.chains.add(chain);
          lrr.foreignOperatorNetId = "foreignOperatorNetId";
          lrr.foreignOperatorNSID = "foreignOperatorNSID";
          lrr.gwId = "gwId";
          lrr.gwDLAllowed = true;
          lrr.gwToken = "gwToken";
          lrr.instantPer = 0f;
          lrr.ismBand = "us915";
          lrr.rfRegion = "rfRegion";
          lrr.networkPartnerId = "networkPartnerId";
          lrr.rssi = 0f;
          lrr.snr = 0f;

          history.lrrs = new ArrayList<>();
          history.lrrs.add(lrr);

          // RDTP-8479
          history.deviceDomains = new ArrayList<>();
          history.deviceDomains.add(new DomainMongo("France","Site"));

          history.bsDomains = new ArrayList<>();
          history.bsDomains.add(new DomainMongo("Energy","Vertical"));

        }
        return history;
    }
}
