package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElementRecipients implements Element {

    @JsonProperty("ID")
    protected String id;
    protected String status;
//    protected Boolean dropped;
    @JsonProperty("destinations")
    protected List<Element> destinations;

    public ElementRecipients() {
        super();
    }

    public ElementRecipients(String id, String status, /*Boolean dropped,*/ List<Element> destinations) {
        super();
        this.id = id;
        this.status = status;
//        this.dropped = dropped;
        this.destinations = destinations;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

//    public Boolean getDropped() {
//        return dropped;
//    }

    public List<Element> getDestinations() {
        return destinations;
    }
}
