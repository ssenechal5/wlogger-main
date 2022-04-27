package com.actility.thingpark.wlogger.dto;

public class ElementCustomerData implements Element {

    protected Element loc;
    protected Element alr;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementCustomerData() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementCustomerData(final Element loc, final Element alr) {
        super();
        this.loc = loc;
        this.alr = alr;
    }

    /**
     * Gets the value of the loc property.
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     
     */
    public Element getLoc() {
        return loc;
    }

    /**
     * Sets the value of the loc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     
     */
    public void setLoc(Element value) {
        this.loc = value;
    }

    /**
     * Gets the value of the alr property.
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     
     */
    public Element getAlr() {
        return alr;
    }

    /**
     * Sets the value of the alr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     
     */
    public void setAlr(Element value) {
        this.alr = value;
    }

    public ElementCustomerData withLoc(Element value) {
        setLoc(value);
        return this;
    }

    public ElementCustomerData withAlr(Element value) {
        setAlr(value);
        return this;
    }

}
