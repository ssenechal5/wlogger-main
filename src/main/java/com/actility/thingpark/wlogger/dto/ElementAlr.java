package com.actility.thingpark.wlogger.dto;

public class ElementAlr implements Element {

    protected String pro;
    protected String ver;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementAlr() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementAlr(final String pro, final String ver) {
        super();
        this.pro = pro;
        this.ver = ver;
    }

    /**
     * Gets the value of the pro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPro() {
        return pro;
    }

    /**
     * Sets the value of the pro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPro(String value) {
        this.pro = value;
    }

    /**
     * Gets the value of the ver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVer() {
        return ver;
    }

    /**
     * Sets the value of the ver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVer(String value) {
        this.ver = value;
    }

    public ElementAlr withPro(String value) {
        setPro(value);
        return this;
    }

    public ElementAlr withVer(String value) {
        setVer(value);
        return this;
    }

}
