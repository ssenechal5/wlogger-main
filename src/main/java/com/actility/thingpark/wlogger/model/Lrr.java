package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementChains;
import com.actility.thingpark.wlogger.dto.ElementLrrs;
import com.actility.thingpark.wlogger.entity.history.Utils;
import com.google.common.collect.Lists;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Lrr {
  public static final String EMPTY = "";
  public static final String TD = "<td>";
  public static final String TR = "<tr>";
  public static final String TD1 = "</td>";
  public static final String STRING = "-";

  private final String lrrId;
  private final Float rssi;
  private final Float snr;
  private final Float esp;
  private final Float instantPer;
  private final String networkPartnerId;
  private final String foreignOperatorNetId;
  private final String gwId;
  private final String gwToken;
  private final Boolean gwDLAllowed;
  private final String ismBand;
  private final String rfRegion;
  private final String foreignOperatorNSID;
  private final List<Chain> chains;

  private Lrr(Builder builder) {
    this.rssi = builder.rssi;
    this.esp = builder.esp;
    this.foreignOperatorNetId = builder.foreignOperatorNetId;
    this.gwId = builder.gwId;
    this.lrrId = builder.lrrId;
    this.networkPartnerId = builder.networkPartnerId;
    this.chains = builder.chains;
    this.instantPer = builder.instantPer;
    this.snr = builder.snr;
    this.gwToken = builder.gwToken;
    this.gwDLAllowed = builder.gwDLAllowed;
    this.ismBand = builder.ismBand;
    this.rfRegion = builder.rfRegion;
    this.foreignOperatorNSID = builder.foreignOperatorNSID;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Optional<ElementLrrs> getAsElement(boolean all, boolean passiveRoamingActivated) {
    if (isBlank(lrrId) && isBlank(gwId)) {
      return empty();
    }
    if (!all) {
      return of(
          new ElementLrrs(
              Utils.getToString(lrrId),
              EMPTY,
              EMPTY,
              EMPTY,
              getChainsAsElements(),
              EMPTY,
              EMPTY,
              EMPTY,
              Boolean.FALSE,
              Utils.getToString(foreignOperatorNSID),
              Utils.getToString(rfRegion),
              Utils.getToString(ismBand)));
    }
    return of(
        new ElementLrrs(
            Utils.getToString(lrrId),
            Utils.getToString(rssi),
            Utils.getToString(snr),
            Utils.getToString(esp),
            getChainsAsElements(),
            passiveRoamingActivated ? Utils.getToString(foreignOperatorNetId) : EMPTY,
            passiveRoamingActivated ? Utils.getToString(gwId) : EMPTY,
            passiveRoamingActivated ? Utils.getToString(gwToken) : EMPTY,
                (gwDLAllowed != null ? passiveRoamingActivated && gwDLAllowed : Boolean.FALSE),
            Utils.getToString(foreignOperatorNSID),
            Utils.getToString(rfRegion),
            Utils.getToString(ismBand)));
  }

  public List<String> getAsCsv(boolean passiveRoaming) {
    List<String> list =
        Lists.newArrayList(
            Utils.getToStringOrEmpty(lrrId),
            Utils.getToStringOrEmpty(rssi),
            Utils.getToStringOrEmpty(snr),
            Utils.getToStringOrEmpty(esp),
            Utils.getEmptyIfNull(getChainsAsCsv()),
            Utils.getToStringOrEmpty(ismBand),
            Utils.getToStringOrEmpty(rfRegion));
    if (passiveRoaming) {
      list.add(Utils.getEmptyIfNull(gwId));
      list.add(Utils.getEmptyIfNull(gwToken));
      list.add(Utils.getEmptyIfNull(Utils.getToStringOrNull(gwDLAllowed)));
      list.add(Utils.getEmptyIfNull(foreignOperatorNetId));
      list.add(Utils.getEmptyIfNull(foreignOperatorNSID));
    }
    return list;
  }

  public static List<String> getEmptyCsv(boolean passiveRoaming) {
    String emptyString = EMPTY;
    List<String> list =
            Lists.newArrayList(
                    emptyString,
                    emptyString,
                    emptyString,
                    emptyString,
                    emptyString,
                    emptyString,
                    emptyString);
    if (passiveRoaming) {
      list.add(emptyString);
      list.add(emptyString);
      list.add(emptyString);
      list.add(emptyString);
      list.add(emptyString);
    }
    return list;
  }

  @Nullable
  public List<Element> getChainsAsElements() {
    if (chains == null || chains.isEmpty()) {
      return null;
    }
    return chains.stream().map(Chain::getAsElement).collect(Collectors.toList());
  }

  public String getChainsAsCsv() {
    if (chains == null || chains.isEmpty()) {
      return EMPTY;
    }
    JsonArray json = new JsonArray();
    chains.stream().map(Chain::getAsJsonString).forEach(json::add);
    return json.toString();
  }

  public String getAsHtmlRow(Direction direction, boolean prActivated) {
    if (isBlank(lrrId) && isBlank(gwId)) {
      return EMPTY;
    }
    StringBuilder row =
        new StringBuilder()
            .append(TR)
            .append(TD)
            .append(Utils.getToString(lrrId))
            .append(TD1)
            .append(TD)
            .append(Utils.getToString(rssi))
            .append(TD1)
            .append(TD)
            .append(Utils.getToString(snr))
            .append(TD1)
            .append(TD);
    if (esp != null) {
      row.append(esp);
    } else {
      row.append(STRING);
    }
    row.append(TD1);
    row.append(TD);
    if (chains != null && chains.size() > 0) {
      row.append(
          chains.stream().map(chain -> chain.getAsHtmlLine(direction)).collect(Collectors.joining()));
    } else {
      row.append(STRING);
    }
    row.append(TD1);
    row.append(TD).append(Utils.getIsmBand(ismBand)).append(TD1);
    row.append(TD).append(Utils.getToString(rfRegion)).append(TD1);
    // NFR 809
    if (prActivated) {
      row.append(TD)
          .append(Utils.getToString(gwId))
          .append(TD1)
          .append(TD)
          .append(Utils.getToString(gwToken))
          .append(TD1)
          .append(TD)
          .append(Utils.getToStringOrEmpty(gwDLAllowed))
          .append(TD1)
          .append(TD)
          .append(Utils.getToString(foreignOperatorNetId))
          .append(TD1)
          .append(TD)
          .append(Utils.getToString(foreignOperatorNSID))
          .append(TD1);
    }
    return row.append("</tr>").toString();
  }

  public static class Chain {
    private final Integer chain;
    private final String channel;
    private final Date timestamp;
    private final Integer timestampType;
    private final Integer nanoseconds;

    private Chain(Builder builder) {
      this.chain = builder.chain;
      this.channel = builder.channel;
      this.timestamp = builder.timestamp;
      this.timestampType = builder.timestampType;
      this.nanoseconds = builder.nanoseconds;
    }

    public static Builder builder() {
      return new Builder();
    }

    public String getChainTimeType(Integer type, boolean orNull) {
      if (type == null) {
        if (orNull)
          return "null";
        else
          return STRING;
      }
      switch (type) {
        case 3:
          return "GPS_RADIO";
        default:
          return STRING;
      }
    }

    public String complementTo2(int nanos) {
      return String.format("%09d", nanos);
    }

    public final SimpleDateFormat TIME_FORMAT_X = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
    public final static String TIME_ZONE = "UTC";

    public ElementChains getAsElement() {
      String timestampD = EMPTY;
      String sttype;
      if (timestamp != null) {
        TIME_FORMAT_X.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        timestampD = TIME_FORMAT_X.format(timestamp);
      }
      if (timestampD != null && timestampD.length() > 23 && nanoseconds != null) {
        timestampD =
            timestampD.substring(0, 20)
                + complementTo2(nanoseconds)
                + timestampD.substring(23);
      }

      sttype = null;
      if (timestampType != null) {
        sttype = getChainTimeType(timestampType, true);
      }

      return new ElementChains(Utils.getToString(chain), channel, timestampD, sttype);
    }

    public String getAsHtmlLine(Direction direction) {
      String timestampD = getTimestampD();
      // channel info is for EUI_location only
      String chan = EMPTY;
      if (Direction.LOCATION.equals(direction)) {
        chan = format("{%s}", ofNullable(channel).filter(StringUtils::isNotBlank).orElse(STRING));
      }
      return format(
          " CHAIN[%d]:%s {%s}%s<br/>",
          chain, timestampD, getChainTimeType(timestampType, false), chan);
    }

    public JsonObject getAsJsonString() {
      String timestampD = getTimestampD();
      return new JsonObject()
          .put("chain", (chain == null?chain:chain.toString()))
          .put("channel", channel)
          .put("time", timestampD)
          .put("ttype", (timestampType == null?timestampType:getChainTimeType(timestampType, true)));
    }

    private String getTimestampD() {
      String timestampD = EMPTY;
      if (timestamp != null) {
        TIME_FORMAT_X.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        timestampD = TIME_FORMAT_X.format(timestamp);
      }
      if (timestampD.length() > 23 && nanoseconds != null) {
        timestampD =
            timestampD.substring(0, 20)
                + complementTo2(nanoseconds)
                + timestampD.substring(23);
      }
      return timestampD;
    }

    public static final class Builder {
      private Integer chain;
      private String channel;
      private Date timestamp;
      private Integer timestampType;
      private Integer nanoseconds;

      public Builder withChain(Integer chain) {
        this.chain = chain;
        return this;
      }

      public Builder withChannel(String channel) {
        this.channel = channel;
        return this;
      }

      public Builder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
      }

      public Builder withTimestampType(Integer timestampType) {
        this.timestampType = timestampType;
        return this;
      }

      public Builder withNanoseconds(Integer nanoseconds) {
        this.nanoseconds = nanoseconds;
        return this;
      }

      public Chain build() {
        return new Chain(this);
      }
    }
  }

  public static final class Builder {
    private String lrrId;
    private Float rssi;
    private Float snr;
    private Float esp;
    private Float instantPer;
    private String networkPartnerId;
    private String foreignOperatorNetId;
    private String gwId;
    private String gwToken;
    private Boolean gwDLAllowed;
    private String ismBand;
    private String rfRegion;
    private String foreignOperatorNSID;
    private List<Chain> chains;

    private Builder() {}

    public Builder withLrrId(String lrrId) {
      this.lrrId = lrrId;
      return this;
    }

    public Builder withRssi(Float rssi) {
      this.rssi = rssi;
      return this;
    }

    public Builder withSnr(Float snr) {
      this.snr = snr;
      return this;
    }

    public Builder withEsp(Float esp) {
      this.esp = esp;
      return this;
    }

    public Builder withInstantPer(Float instantPer) {
      this.instantPer = instantPer;
      return this;
    }

    public Builder withNetworkPartnerId(String networkPartnerId) {
      this.networkPartnerId = networkPartnerId;
      return this;
    }

    public Builder withForeignOperatorNetId(String foreignOperatorNetId) {
      this.foreignOperatorNetId = foreignOperatorNetId;
      return this;
    }

    public Builder withGwId(String gwId) {
      this.gwId = gwId;
      return this;
    }

    public Builder withGwToken(String gwToken) {
      this.gwToken = gwToken;
      return this;
    }

    public Builder withGwDLAllowed(Boolean gwDLAllowed) {
      this.gwDLAllowed = gwDLAllowed;
      return this;
    }

    public Builder withChains(List<Chain> chains) {
      this.chains = chains;
      return this;
    }

    public Builder withIsmBand(String ismBand) {
      this.ismBand = ismBand;
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

    public Lrr build() {
      return new Lrr(this);
    }
  }
}
