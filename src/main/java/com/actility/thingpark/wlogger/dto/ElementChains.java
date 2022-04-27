package com.actility.thingpark.wlogger.dto;

public class ElementChains implements Element {

    protected String chain;
    protected String channel;
    protected String time;
    protected String ttype;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementChains() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementChains(final String chain, final String channel, final String time, final String ttype) {
        super();
        this.chain = chain;
        this.channel = channel;
        this.time = time;
        this.ttype = ttype;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

    public String getChain() {
        return chain;
    }

    public String getTtype() {
        return ttype;
    }
}
