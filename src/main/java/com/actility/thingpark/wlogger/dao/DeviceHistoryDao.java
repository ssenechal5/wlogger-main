package com.actility.thingpark.wlogger.dao;

import com.actility.thingpark.smp.rest.dto.DomainRestrictionDto;
import com.actility.thingpark.smp.rest.dto.DomainTypeDto;
import com.actility.thingpark.twa.entity.history.DeviceHistory;
import com.actility.thingpark.twa.entity.history.DomainMongo;
import com.actility.thingpark.wlogger.model.DeviceType;
import com.actility.thingpark.wlogger.model.Search;
import com.actility.thingpark.wlogger.utils.FilterUtils;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * MongoDB DAO for DeviceHistory.
 *
 * @author Samuel Liard
 */
@Singleton
public class DeviceHistoryDao {

    private static final Logger logger = Logger.getLogger(DeviceHistoryDao.class.getName());
    /**
     * Table name for deviceFrameHistory
     */
    public static final String TABLE_NAME = "DeviceHistory";

    private final MongoClient mongoClient;
    private final String database;

    @Inject
    public DeviceHistoryDao(final MongoClient mongoClient, @ConfigProperty(name = "quarkus.mongodb.database") String database) {
        this.mongoClient = mongoClient;
        this.database = database;
    }

    protected MongoCollection<DeviceHistory> getCollection() {
        return mongoClient.getDatabase(database).getCollection(TABLE_NAME, DeviceHistory.class);
    }

    public List<DeviceHistory> get(
            @Nonnull Search search,
            int offset,
            int pageSize) {
        MongoCursor<DeviceHistory> cursor = this.getIterable(search, offset, pageSize);
        if (cursor != null) {
//            ArrayList<DeviceHistory> list = Lists.newArrayList();
            try {
//                while (cursor.hasNext()) {
//                    list.add(cursor.next());
//                }
                return Lists.newArrayList(cursor);
            } finally {
                cursor.close();
            }
//            return list;
        } else
            return Collections.emptyList();
    }

    public MongoCursor<DeviceHistory> getIterable(
        @Nonnull Search search,
        int offset,
        int pageSize) {
        logger.finest(
                "DeviceHistoryDao.get : "
                        + search.getDeviceIDs().size()
                        + ","
                        + search.getDevADDRs().size()
                        + ","
                        + search.getFromDate()
                        + ","
                        + search.getToDate()
                        + ","
                        + search.getLRRID()
                        + ","
                        + search.getLRCID()
                        + ","
                        + search.getDecoder()
                        + ","
                        + search.getSubscriberID()
                        + ","
                        + (search.getDomainRestrictions() != null ? search.getDomainRestrictions().getAnds().size() + " domains restrictions" : "no restrictions")
                        + ","
                        + pageSize
                        + ","
                        + offset);
        if (pageSize < 1)
            return null;
        ArrayList<Bson> andList = new ArrayList<>();
        if (!search.getDeviceIDs().isEmpty()) {
            logger.finest("andList deviceEUI in " + search.getDeviceIDs().size());
            HashMap<String,Boolean> cleanedMap = FilterUtils.cleanSearchParams(search.getDeviceIDs(),search.getType(),true);
            logger.finest("andList deviceEUI in " + cleanedMap.size());
            if (cleanedMap.isEmpty()) {
                return null;
            } else {
                andList.add(equalOrBeginWith(DeviceHistory.DEVICE_EUI, cleanedMap));
            }
        }
        if (isNotBlank(search.getLRCID())) {
            logger.finest("andList lrcID " + search.getLRCID());
            andList.add(Filters.eq(DeviceHistory.LRC_ID, search.getLRCID()));
        }
        if (!search.getDevADDRs().isEmpty()) {
            logger.finest("andList deviceADDR in " + search.getDevADDRs().size());
            HashMap<String,Boolean> cleanedMap = FilterUtils.cleanSearchParams(search.getDevADDRs(),search.getType(),false);
            logger.finest("andList deviceADDR in " + cleanedMap.size());
            if (cleanedMap.isEmpty()) {
                return null;
            } else {
                andList.add(equalOrBeginWith(DeviceHistory.DEVICE_ADDRESS, cleanedMap));
            }
        }
        if (!search.getAsID().isEmpty()) {
            logger.finest("andList asID in " + search.getAsID().size());
            andList.add(Filters.all(DeviceHistory.APPLICATION_SERVERS_ID, search.getAsID()));
        }
        if (isNotBlank(search.getNetPartId())) {
            logger.finest("andList netPartID " + search.getNetPartId());
            andList.add(Filters.eq(DeviceHistory.NETWORK_PARTNER_ID, search.getNetPartId()));
        }
        if (search.getDirection() != null) {
            logger.finest("andList direction " + search.getDirection().getValue());
            andList.add(Filters.eq(DeviceHistory.TYPE_T, search.getDirection().getValue()));
        }
        if (isNotBlank(search.getSubscriberID())) {
            logger.finest("andList subId OR netPartID " + search.getSubscriberID());
            if (search.isSubscriberViewRoamingInTraffic()) {
                andList.add(
                  Filters.or(
                    Filters.eq(DeviceHistory.SUBSCRIBER_ID, search.getSubscriberID()),
                    Filters.and(
                        Filters.eq(DeviceHistory.NETWORK_PARTNER_ID, search.getSubscriberID()),
                        Filters.eq(DeviceHistory.ROAMING_TYPE, 0))));
            } else {
                andList.add(Filters.eq(DeviceHistory.SUBSCRIBER_ID, search.getSubscriberID()));
            }
        }
        if (search.getFromDate() != null) {
            logger.finest("andList start " + search.getFromDate());
            andList.add(Filters.gte(DeviceHistory.TIMESTAMP_T, search.getFromDate()));
        }
        if (search.getToDate() != null) {
            logger.finest("andList end " + search.getToDate());
            andList.add(Filters.lte(DeviceHistory.TIMESTAMP_T, search.getToDate()));
        }
        if (search.getStartUid() != null) {
            logger.finest("andList startId " + search.getStartUid());
            andList.add(Filters.gte(DeviceHistory.ID_ID, new ObjectId(search.getStartUid())));
        }
        if (search.getEndUid() != null) {
            logger.finest("andList endId " + search.getEndUid());
            andList.add(Filters.lte(DeviceHistory.ID_ID, new ObjectId(search.getEndUid())));
        }
        String gatewayField;
        switch (search.getType()) {
            case LORA:
                logger.finest("andList PGW_ID not exist ");
                andList.add(Filters.not(Filters.exists(DeviceHistory.PGW_ID)));
                gatewayField = DeviceHistory.LRR_ID;
                break;
            case LTE:
                logger.finest("andList PGW_ID exist ");
                andList.add(Filters.exists(DeviceHistory.PGW_ID));
                gatewayField = DeviceHistory.PGW_ID;
                break;
            default:
                throw new RuntimeException("Unknown device type: " + search.getType());
        }
        if (isNotBlank(search.getLRRID())) {
            logger.finest("andList " + gatewayField + " " + search.getLRRID());
            andList.add(Filters.eq(gatewayField, search.getLRRID()));
        }
        if (search.getSubtype().size() > 0) {
            logger.finest("andList subtype is " + search.getSubtype().get(0));
            andList.add(Filters.in(DeviceHistory.SUBTYPE_S, search.getSubtype()));
        }

        if (search.getType().equals(DeviceType.LORA) &&
                search.getDomainRestrictions() != null && search.getDomainRestrictions().getAnds() != null
                && search.getDomainRestrictions().getAnds().size() > 0) {
            List<DomainRestrictionDto> list = search.getDomainRestrictions().getAnds();
            List<Bson> domainsFilters = new ArrayList<>();
            for (DomainRestrictionDto and : list) {
                if (and.getType().equals(DomainTypeDto.FULL)) {
                    logger.finest("ADD FULL " + and.getType() + " " + and.getName() + " " + and.getGroup().getName());
                    domainsFilters.add(Filters.or(
                        Filters.elemMatch(DeviceHistory.DOMAINS_DEVICE,
                            Filters.and(Filters.eq(DomainMongo.NAME, and.getName()), Filters.eq(DomainMongo.GROUP, and.getGroup().getName()))),
                        Filters.elemMatch(DeviceHistory.DOMAINS_BS,
                            Filters.and(Filters.eq(DomainMongo.NAME, and.getName()), Filters.eq(DomainMongo.GROUP, and.getGroup().getName())))));
                } else {
                    logger.finest("ADD PREFIX " + and.getType() + " " + and.getName() + " " + and.getGroup().getName());
                    domainsFilters.add(Filters.or(
                        Filters.elemMatch(DeviceHistory.DOMAINS_DEVICE,
                            Filters.and(Filters.regex(DomainMongo.NAME, "^"+and.getName()+"\\/"), Filters.eq(DomainMongo.GROUP, and.getGroup().getName()))),
                        Filters.elemMatch(DeviceHistory.DOMAINS_BS,
                            Filters.and(Filters.regex(DomainMongo.NAME, "^"+and.getName()+"\\/"), Filters.eq(DomainMongo.GROUP, and.getGroup().getName())))));
                }
            }
            if (domainsFilters.size() > 0) {
                if (domainsFilters.size() == 1) {
                  logger.finest("ADD domainsFilters.size() " + domainsFilters.size());
                  andList.add(domainsFilters.get(0));
                } else {
                  logger.finest("ADDALL domainsFilters.size() " + domainsFilters.size());
                  andList.addAll(domainsFilters);
                }
            }
        }

        return getCollection()
                        .find(Filters.and(andList))
                        .sort(Sorts.descending(DeviceHistory.TIMESTAMP_T))
                        .skip(offset)
                        .limit(pageSize)
                        .iterator();
    }

    private Bson equalOrBeginWith(@Nonnull final String fieldName, @Nonnull HashMap<String,Boolean> map) {
        List<String> values = map.keySet().stream().collect(Collectors.toList());
        return Filters.or(
                values.stream().
                        map(v -> FilterUtils.beginWithOrEqual(fieldName, v, map)).
                        collect(Collectors.toList())
        );
    }

}