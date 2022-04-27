package com.actility.thingpark.wlogger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ElementAuth implements Element {

    @JsonProperty("ID")
    protected String id;
    protected String href;
    protected List<Element> customerIds;
    protected String sessionId;
    protected Disclaimer disclaimer;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementAuth() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementAuth(final String id, final String href, final List<Element> customerIds, final String sessionId, final Disclaimer disclaimer) {
        super();
        this.id = id;
        this.href = href;
        this.customerIds = customerIds;
        this.sessionId = sessionId;
        this.disclaimer = disclaimer;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the customerIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * 
     * 
     */
    public List<Element> getCustomerIds() {
        if (customerIds == null) {
            customerIds = new ArrayList<>();
        }
        return this.customerIds;
    }

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the disclaimer property.
     * 
     * @return
     *     possible object is
     *     {@link Disclaimer }
     *     
     */
    public Disclaimer getDisclaimer() {
        return disclaimer;
    }

    /**
     * Sets the value of the disclaimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Disclaimer }
     *     
     */
    public void setDisclaimer(Disclaimer value) {
        this.disclaimer = value;
    }

    public ElementAuth withID(String value) {
        setID(value);
        return this;
    }

    public ElementAuth withHref(String value) {
        setHref(value);
        return this;
    }

    public ElementAuth withCustomerIds(Element... values) {
        if (values!= null) {
            for (Element value: values) {
                getCustomerIds().add(value);
            }
        }
        return this;
    }

    public ElementAuth withCustomerIds(Collection<Element> values) {
        if (values!= null) {
            getCustomerIds().addAll(values);
        }
        return this;
    }

    public ElementAuth withSessionId(String value) {
        setSessionId(value);
        return this;
    }

    public ElementAuth withDisclaimer(Disclaimer value) {
        setDisclaimer(value);
        return this;
    }

}
