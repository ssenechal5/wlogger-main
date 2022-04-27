package com.actility.thingpark.wlogger.dto;

public class ElementLfd implements Element {

    protected String dfc;
    protected String cnt;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementLfd() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementLfd(final String dfc, final String cnt) {
        super();
        this.dfc = dfc;
        this.cnt = cnt;
    }

    /**
     * Gets the value of the dfc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDfc() {
        return dfc;
    }

    /**
     * Sets the value of the dfc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDfc(String value) {
        this.dfc = value;
    }

    /**
     * Gets the value of the cnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnt() {
        return cnt;
    }

    /**
     * Sets the value of the cnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnt(String value) {
        this.cnt = value;
    }

    public ElementLfd withDfc(String value) {
        setDfc(value);
        return this;
    }

    public ElementLfd withCnt(String value) {
        setCnt(value);
        return this;
    }

}
