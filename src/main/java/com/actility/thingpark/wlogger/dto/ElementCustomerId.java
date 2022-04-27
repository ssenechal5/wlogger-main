package com.actility.thingpark.wlogger.dto;

public class ElementCustomerId implements Element {

    protected String customerId;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementCustomerId() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementCustomerId(final String customerId) {
        super();
        this.customerId = customerId;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

    public ElementCustomerId withCustomerId(String value) {
        setCustomerId(value);
        return this;
    }

}
