package com.actility.thingpark.wlogger.dto;

public class ElementLoc implements Element {

    protected String lat;
    protected String lon;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementLoc() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementLoc(final String lat, final String lon) {
        super();
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Gets the value of the lat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLat() {
        return lat;
    }

    /**
     * Sets the value of the lat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLat(String value) {
        this.lat = value;
    }

    /**
     * Gets the value of the lon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLon() {
        return lon;
    }

    /**
     * Sets the value of the lon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLon(String value) {
        this.lon = value;
    }

    public ElementLoc withLat(String value) {
        setLat(value);
        return this;
    }

    public ElementLoc withLon(String value) {
        setLon(value);
        return this;
    }

}
