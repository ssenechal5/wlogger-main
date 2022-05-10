package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.smp.rest.dto.DomainRestrictionsDto;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class Search {
  private final DeviceType type;
  private final String last;
  private final List<String> deviceIDs;
  private final String subtype;
  private final String decoder;
  private final String lrrId;
  private final String lrcId;
  private final Date fromDate;
  private final Date toDate;
  private final Direction direction;
  private final List<String> devADDRs;
  private final String netPartId;
  private final int pageIndex;
  private final String startUid;
  private final String endUid;
  private final String subscriberID;
  private final String asID;
  private final boolean subscriberViewRoamingInTraffic;
  private final DomainRestrictionsDto domainRestrictions;

  private Search(Builder builder) {
    this.type = builder.type;
    this.deviceIDs = builder.deviceIDs;
    this.subtype = builder.subtype;
    this.decoder = builder.decoder;
    this.lrrId = builder.lrrId;
    this.lrcId = builder.lrcId;
    this.fromDate = builder.fromDate;
    this.toDate = builder.toDate;
    this.direction = builder.direction;
    this.devADDRs = builder.devADDRs;
    this.netPartId = builder.netPartId;
    this.pageIndex = builder.pageIndex;
    this.startUid = builder.startUid;
    this.asID = builder.asID;
    this.endUid = builder.endUid;
    this.subscriberID = builder.subscriberID;
    this.last = builder.last;
    this.subscriberViewRoamingInTraffic = builder.subscriberViewRoamingInTraffic;
    this.domainRestrictions = builder.domainRestrictions;
  }

  public static Builder builder() {
    return new Builder();
  }

  private static int getPageIndex(Integer pageIndex) {
    // First pageIndex is equal to 1 (as specified in API specs)
    if (pageIndex == null || pageIndex == 0)
      return 1;
    else
      return pageIndex;
  }

  public DeviceType getType() {
    return type;
  }

  public String getLast() {
    return last;
  }

  public List<String> getDeviceIDs() {
    return deviceIDs;
  }

  @Nonnull
  public List<Integer> getSubtype() {
    if (isBlank(subtype)) {
      return emptyList();
    }
    return Arrays.stream(subtype.split(","))
        .filter(StringUtils::isNotBlank)
        .map(Integer::valueOf)
        .collect(Collectors.toList());
  }

  public String getDecoder() {
    return decoder;
  }

  public String getLRRID() {
    return lrrId;
  }

  public String getLRCID() {
    return lrcId;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public Direction getDirection() {
    return direction;
  }

  public List<String> getDevADDRs() {
    return devADDRs;
  }

  public String getNetPartId() {
    return netPartId;
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public String getStartUid() {
    return startUid;
  }

  public String getEndUid() {
    return endUid;
  }

  public String getSubscriberID() {
    return subscriberID;
  }

  public boolean isSubscriberViewRoamingInTraffic() {
    return subscriberViewRoamingInTraffic;
  }

  public DomainRestrictionsDto getDomainRestrictions() {
    return domainRestrictions;
  }

  @Nonnull
  public List<String> getAsID() {
    if (isBlank(asID)) {
      return emptyList();
    }
    return singletonList(asID);
  }

  @Override
  public String toString() {
    return format(
        "Search{type='%s', last='%s', deviceID='%s', decoder='%s', LRRID='%s', LRCID='%s', fromDate='%s', toDate='%s', devADDR='%s', netPartId='%s', startUid='%s', endUid='%s', pageIndex='%s', subscriberID='%s', asId='%s', subtype='%s', direction='%s', subscriberViewRoamingInTraffic='%s', restrictions='%s' }",
        type,
        last,
        deviceIDs,
        decoder,
        lrrId,
        lrcId,
        fromDate,
        toDate,
        devADDRs,
        netPartId,
        startUid,
        endUid,
        pageIndex,
        subscriberID,
        asID,
        subtype,
        direction,
        subscriberViewRoamingInTraffic,
        (domainRestrictions != null && domainRestrictions.getAnds() != null ?
               domainRestrictions.getAnds().stream().map(and -> and.getGroup().getName() + ":" + and.getName()).collect(Collectors.joining(",")) : "NO"));
  }

  public static final class Builder {
    protected final SimpleDateFormat TIME_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    protected final SimpleDateFormat TIME_FORMAT_TRUNCATED =
        new SimpleDateFormat("yyyy-MM-dd'T'HH");
    protected final SimpleDateFormat TIME_FORMAT_TRUNCATED_SS =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final Logger LOGGER = Logger.getLogger(Builder.class.getName());

    private DeviceType type;
    private String last;
    private List<String> deviceIDs = Collections.emptyList();
    private String subtype;
    private String decoder;
    private String lrrId;
    private String lrcId;
    private Date fromDate;
    private Date toDate;
    private Direction direction;
    private List<String> devADDRs = Collections.emptyList();
    private String netPartId;
    private int pageIndex;
    private String startUid;
    private String endUid;
    private String subscriberID;
    private String asID;
    private boolean subscriberViewRoamingInTraffic;
    private DomainRestrictionsDto domainRestrictions;

    private Builder() {}

    @Nullable
    private Date parseDate(String date) {
      if (isBlank(date)) {
        return null;
      }
      SimpleDateFormat format = TIME_FORMAT;
      if (date.length() < 14) {
        format = TIME_FORMAT_TRUNCATED;
      } else if (date.length() <= 19) {
        format = TIME_FORMAT_TRUNCATED_SS;
      }
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      try {
        return format.parse(date);
      } catch (ParseException e) {
        LOGGER.log(Level.INFO,e.getMessage(),e);
        return null;
      }
    }

    public Builder withType(DeviceType type) {
      this.type = type;
      return this;
    }


    public Builder withLast(String last) {
      this.last = last;
      return this;
    }

    public Builder withDeviceIDs(List<String> deviceIDs) {
      this.deviceIDs = defaultIfNull(deviceIDs, Collections.emptyList());
      return this;
    }

    public Builder withSubtype(String subtype) {
      this.subtype = subtype;
      return this;
    }

    public Builder withDecoder(String decoder) {
      this.decoder = decoder;
      return this;
    }

    public Builder withLRRID(String lrrId) {
      this.lrrId = upperCase(lrrId);
      return this;
    }

    public Builder withLRCID(String lrcId) {
      this.lrcId = upperCase(lrcId);
      return this;
    }

    public Builder withFromDate(String fromDate) {
      this.fromDate = parseDate(fromDate);
      return this;
    }

    public Builder withToDate(String toDate) {
      this.toDate = parseDate(toDate);
      return this;
    }

    public Builder withDirection(Direction direction) {
      this.direction = direction;
      return this;
    }

    public Builder withDevADDRs(List<String> devADDRs) {
      this.devADDRs = defaultIfNull(devADDRs, Collections.emptyList());
      return this;
    }

    public Builder withNetPartId(String netPartId) {
      this.netPartId = netPartId;
      return this;
    }

    public Builder withPageIndex(Integer pageIndex) {
      this.pageIndex = getPageIndex(pageIndex);
      return this;
    }

    public Builder withStartUid(String startUid) {
      this.startUid = startUid;
      return this;
    }

    public Builder withEndUid(String endUid) {
      this.endUid = endUid;
      return this;
    }

    public Builder withSubscriberID(String subscriberID) {
      this.subscriberID = subscriberID;
      return this;
    }

    public Builder withDomainRestrictions(DomainRestrictionsDto domainRestrictions) {
      this.domainRestrictions = domainRestrictions;
      return this;
    }

    public Builder withAsID(String asID) {
      this.asID = upperCase(asID);
      return this;
    }

    public Builder withSubscriberViewRoamingInTraffic(int subscriberViewRoamingInTraffic) {
      this.subscriberViewRoamingInTraffic = (subscriberViewRoamingInTraffic == 1);
      return this;
    }

    public Search build() {
      return new Search(this);
    }
  }
}
