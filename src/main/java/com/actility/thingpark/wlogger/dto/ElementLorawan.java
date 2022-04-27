package com.actility.thingpark.wlogger.dto;

public class ElementLorawan implements Element {

    protected String fport;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementLorawan() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementLorawan(final String fport) {
        super();
        this.fport = fport;
    }

    /**
     * Gets the value of the fport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFport() {
        return fport;
    }

    /**
     * Sets the value of the fport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFport(String value) {
        this.fport = value;
    }

    public ElementLorawan withFport(String value) {
        setFport(value);
        return this;
    }

}
