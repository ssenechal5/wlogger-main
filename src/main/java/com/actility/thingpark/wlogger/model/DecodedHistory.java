package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public abstract class DecodedHistory {

    public static final String OK = "Ok";

    protected String uid;
    protected Direction direction;
    protected Date timestamp;
    protected String devEUI;
    protected String devAddr;
    protected String payloadHex;
    protected String lrrid;
    protected String lrcId;
    protected Double devLAT;
    protected Double devLON;
    protected String mType;
    protected String mTypeText;
    protected String hasPayload;
    protected String payloadDecoded;
    protected String late;
    protected String payloadSize;
    protected String asID;

    protected String asReportDeliveryID;
    protected List<Recipient> asRecipients;

    public String getDeliveryStatus() {
        if (this.asRecipients == null || this.asRecipients.isEmpty())
            return OK;
        String status = OK;
        for (Recipient r : this.asRecipients) {
            if (!(OK.equals(r.getStatus())))
                status = "Error";
        }
        return status;
    }

    public String getAsReportDeliveryID() {
        return asReportDeliveryID;
    }

    public List<Recipient> getAsRecipients() {
        return asRecipients;
    }

    public String getUid() {
        return uid;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public String getPayloadHex() {
        return payloadHex;
    }

    public String getLrrid() {
        return lrrid;
    }

    public String getLrcId() {
        return lrcId;
    }

    public Double getDevLAT() {
        return devLAT;
    }

    public Double getDevLON() {
        return devLON;
    }

    public String getmType() {
        return mType;
    }

    public String getmTypeText() {
        return mTypeText;
    }

    public String getHasPayload() {
        return hasPayload;
    }

    public String getPayloadDecoded() {
        return payloadDecoded;
    }

    public String getLate() {
        return late;
    }

    public String getPayloadSize() {
        return payloadSize;
    }

    public String getAsID() {
        return asID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final SimpleDateFormat TIME_FORMAT_X_ISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public String getTimestampAsD() {
        if (timestamp == null) {
            return "";
        }
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return TIME_FORMAT.format(timestamp);
    }

    public String getTimestampAsUTC() {
        if (timestamp == null) {
            return "";
        }
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return TIME_FORMAT.format(timestamp);
    }

    public String getTimestampAsISO() {
        if (timestamp == null) {
            return "";
        }
        TIME_FORMAT_X_ISO.setTimeZone(TimeZone.getTimeZone("ISO"));
        return TIME_FORMAT_X_ISO.format(timestamp);
    }

    public String getDevEUI() {
        return devEUI;
    }

    public String getRecipientListAsHtmlTable() {
        if (this.asRecipients == null || this.asRecipients.isEmpty()) {
            return "";
        }
        // 3) Build the LRR List
        StringBuilder table = new StringBuilder("<table class='chainList'>");
        // Add the table
        return table
                .append("<thead><tr><td>AS ID</td><td>Status</td><td>Transmission errors</td></tr></thead>")
                .append("<tbody>")
                .append(asRecipients.stream()
                        .map(recipient -> recipient.getAsHtmlRow())
                        .collect(Collectors.joining()))
                .append("</tbody>")
                .append("</table>")
                .toString();
    }

    @Nullable
    public List<Element> getRecipientListAsElements() {
        if (this.asRecipients == null) {
            return null;
        }
        return this.asRecipients.stream()
                .map(recipient -> recipient.getAsElement())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public List<String> getRecipientListAsCsv(int recipientMax) {
        List<Recipient> recipientList = ofNullable(asRecipients).orElse(new ArrayList<>());
        List<String> list =
                recipientList.stream()
                        .map(recipient -> recipient.getAsCsv())
                        .flatMap(List::stream)
                        .collect(Collectors.toCollection(ArrayList::new));
        for (int k = recipientList.size(); k < recipientMax; k++) {
            list.addAll(Recipient.getEmptyCsv());
        }
        return list;
    }

}
