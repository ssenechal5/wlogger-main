package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElementLrrs implements Element {

    @JsonProperty("Lrrid")
    protected String lrrid;
    @JsonProperty("LrrRSSI")
    protected String lrrRSSI;
    @JsonProperty("LrrSNR")
    protected String lrrSNR;
    @JsonProperty("LrrESP")
    protected String lrrESP;
    @JsonProperty("LrrChains")
    protected List<Element> lrrChains;
    protected String gwOp;
    protected String gwID;
    protected String gwTk;
    protected Boolean gwDL;
    protected String foreignOperatorNSID;
    protected String rfRegion;
    protected String ismBand;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementLrrs() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementLrrs(final String lrrid, final String lrrRSSI, final String lrrSNR, final String lrrESP, final List<Element> lrrChains, final String gwOp, final String gwID, final String gwTk, final Boolean gwDL,
            final String foreignOperatorNSID, final String rfRegion, final String ismBand) {
        super();
        this.lrrid = lrrid;
        this.lrrRSSI = lrrRSSI;
        this.lrrSNR = lrrSNR;
        this.lrrESP = lrrESP;
        this.lrrChains = lrrChains;
        this.gwOp = gwOp;
        this.gwID = gwID;
        this.gwTk = gwTk;
        this.gwDL = gwDL;
        this.foreignOperatorNSID = foreignOperatorNSID;
        this.rfRegion = rfRegion;
        this.ismBand = ismBand;
    }

    /**
     * Gets the value of the lrrid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLrrid() {
        return lrrid;
    }

    /**
     * Sets the value of the lrrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLrrid(String value) {
        this.lrrid = value;
    }

    public String getRfRegion() {
        return rfRegion;
    }

    public String getForeignOperatorNSID() {
        return foreignOperatorNSID;
    }

    public String getIsmBand() {
        return ismBand;
    }

    /**
     * Gets the value of the gwDL property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGwDL() {
        return gwDL;
    }

    public String getLrrRSSI() {
        return lrrRSSI;
    }

    public String getLrrSNR() {
        return lrrSNR;
    }

    public String getLrrESP() {
        return lrrESP;
    }

    public List<Element> getLrrChains() {
        return lrrChains;
    }

    public String getGwOp() {
        return gwOp;
    }

    public String getGwID() {
        return gwID;
    }

    public String getGwTk() {
        return gwTk;
    }

    public Boolean getGwDL() {
        return gwDL;
    }
}
