package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementDestinations;
import com.actility.thingpark.wlogger.dto.ElementRecipients;
import com.actility.thingpark.wlogger.entity.history.Utils;
import com.google.common.collect.Lists;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Optional.of;

public class Recipient {

    public static final String EMPTY = "";
    public static final String TD = "<td>";
    public static final String TR = "<tr>";
    public static final String TD1 = "</td>";

    private final String id;
    private final String status;
//    private final Boolean dropped;
    private final List<Destination> destinations;

    public String getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Recipient(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
//        this.dropped = builder.dropped;
        this.destinations = builder.destinations;
    }

    public static final class Builder {

        private String id;
        private String status;
//        private Boolean dropped;
        private List<Destination> destinations;

        private Builder() {}

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }
/*
        public Recipient.Builder withDropped(Boolean dropped) {
            this.dropped = dropped;
            return this;
        }
*/
        public Builder withDestinations(List<Destination> destinations) {
            this.destinations = destinations;
            return this;
        }

        public Recipient build() {
            return new Recipient(this);
        }
    }

    public Optional<ElementRecipients> getAsElement() {
        return of(
                new ElementRecipients(
                        Utils.getToString(id),
                        Utils.getToString(status),
//                        dropped,
                        getDestinationsAsElements()));
    }

    public List<String> getAsCsv() {
        return Lists.newArrayList(
            Utils.getToStringOrEmpty(id),
            Utils.getToStringOrEmpty(status),
//            Utils.getToStringOrEmpty(dropped),
            Utils.getEmptyIfNull(getDestinationsAsCsv()));
    }

    public static List<String> getEmptyCsv() {
        return Lists.newArrayList(
                EMPTY,
                EMPTY,
//                EMPTY,
                EMPTY);
    }

    @Nullable
    public List<Element> getDestinationsAsElements() {
        if (destinations == null || destinations.isEmpty()) {
            return null;
        }
        return destinations.stream().map(Destination::getAsElement).collect(Collectors.toList());
    }

    public String getDestinationsAsCsv() {
        if (destinations == null || destinations.isEmpty()) {
            return EMPTY;
        }
        JsonArray json = new JsonArray();
        destinations.stream().map(Destination::getAsJsonString).forEach(json::add);
        return json.toString();
    }

    public String getAsHtmlRow() {
        StringBuilder row =
                new StringBuilder()
                        .append(TR)
                        .append(TD)
                        .append(Utils.getToString(id))
                        .append(TD1)
                        .append(TD)
                        .append((Utils.getToString(status).equalsIgnoreCase("ok") ? Utils.getToString(status): "Error"))
                        .append(TD1)
                        .append(TD);
//        if (dropped) {
//            row.append("Discarded");
//        } else if (destinations == null || destinations.isEmpty()) {
        if (destinations == null || destinations.isEmpty()) {
            row.append("None");
        } else {
            row.append(destinations.stream().map(destination -> destination.getAsHtmlLine()).collect(Collectors.joining("</br>------------------------------------------------------------</br>")));
        }
        row.append(TD1);
        return row.append("</tr>").toString();
    }
    public static class Destination {
        private final Integer idx;
        private final String url;
        private final String status;
        private final String errorMessage;

        public static Builder builder() {
            return new Builder();
        }

        private Destination(Builder builder) {
            this.idx = builder.idx;
            this.url = builder.url;
            this.status = builder.status;
            this.errorMessage = builder.errorMessage;
        }

        public ElementDestinations getAsElement() {
            return new ElementDestinations(idx, url, status, errorMessage);
        }

        public String getAsHtmlLine() {
            if (errorMessage == null || errorMessage.isEmpty())
                return format(" Idx: %d<br/> Url: %s<br/> Status: %s", idx, url, getStatus(status));
            else
                return format(" Idx: %d<br/> Url: %s<br/> Status: %s<br/> Error: %s", idx, url, status, errorMessage);
        }

        private String getStatus(String status) {
            if (status.equals("BlackList")) {
                return "Blacklisted (Slow AS response)";
            } else if (status.equals("Overload")) {
                return "Blacklisted (Very slow AS response)";
            } else
                return status;
        }

        public JsonObject getAsJsonString() {
            return new JsonObject()
                    .put("idx", idx)
                    .put("url", url)
                    .put("status", status)
                    .put("errorMessage", errorMessage);
        }

        public static final class Builder {

          private Integer idx;
          private String url;
          private String status;
          private String errorMessage;

          private Builder() {}

          public Builder withIdx(Integer idx) {
            this.idx = idx;
            return this;
          }

          public Builder withUrl(String url) {
            this.url = url;
            return this;
          }

          public Builder withStatus(String status) {
            this.status = status;
            return this;
          }

          public Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
          }

          public Destination build() {
                return new Destination(this);
            }

        }
    }

}
