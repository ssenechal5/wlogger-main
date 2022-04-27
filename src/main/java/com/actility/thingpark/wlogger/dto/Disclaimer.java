package com.actility.thingpark.wlogger.dto;

public class Disclaimer {

    protected String who;
    protected String previousConnection;
    protected String thingparkLastUnsuccessfulLogin;
    protected Integer thingparkPreviousBadConsecutivePwd;
    protected String disclaimerMessage;
    protected Boolean disclaimerRequired;

    /**
     * Default no-arg constructor
     * 
     */
    public Disclaimer() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public Disclaimer(final String who, final String previousConnection, final String thingparkLastUnsuccessfulLogin, final Integer thingparkPreviousBadConsecutivePwd, final String disclaimerMessage, final Boolean disclaimerRequired) {
        this.who = who;
        this.previousConnection = previousConnection;
        this.thingparkLastUnsuccessfulLogin = thingparkLastUnsuccessfulLogin;
        this.thingparkPreviousBadConsecutivePwd = thingparkPreviousBadConsecutivePwd;
        this.disclaimerMessage = disclaimerMessage;
        this.disclaimerRequired = disclaimerRequired;
    }

    /**
     * Gets the value of the who property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWho() {
        return who;
    }

    /**
     * Sets the value of the who property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWho(String value) {
        this.who = value;
    }

    /**
     * Gets the value of the previousConnection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousConnection() {
        return previousConnection;
    }

    /**
     * Sets the value of the previousConnection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousConnection(String value) {
        this.previousConnection = value;
    }

    /**
     * Gets the value of the thingparkLastUnsuccessfulLogin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThingparkLastUnsuccessfulLogin() {
        return thingparkLastUnsuccessfulLogin;
    }

    /**
     * Sets the value of the thingparkLastUnsuccessfulLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThingparkLastUnsuccessfulLogin(String value) {
        this.thingparkLastUnsuccessfulLogin = value;
    }

    /**
     * Gets the value of the thingparkPreviousBadConsecutivePwd property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThingparkPreviousBadConsecutivePwd() {
        return thingparkPreviousBadConsecutivePwd;
    }

    /**
     * Sets the value of the thingparkPreviousBadConsecutivePwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThingparkPreviousBadConsecutivePwd(Integer value) {
        this.thingparkPreviousBadConsecutivePwd = value;
    }

    /**
     * Gets the value of the disclaimerMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisclaimerMessage() {
        return disclaimerMessage;
    }

    /**
     * Sets the value of the disclaimerMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisclaimerMessage(String value) {
        this.disclaimerMessage = value;
    }

    /**
     * Gets the value of the disclaimerRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisclaimerRequired() {
        return disclaimerRequired;
    }

    /**
     * Sets the value of the disclaimerRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisclaimerRequired(Boolean value) {
        this.disclaimerRequired = value;
    }

    public Disclaimer withWho(String value) {
        setWho(value);
        return this;
    }

    public Disclaimer withPreviousConnection(String value) {
        setPreviousConnection(value);
        return this;
    }

    public Disclaimer withThingparkLastUnsuccessfulLogin(String value) {
        setThingparkLastUnsuccessfulLogin(value);
        return this;
    }

    public Disclaimer withThingparkPreviousBadConsecutivePwd(Integer value) {
        setThingparkPreviousBadConsecutivePwd(value);
        return this;
    }

    public Disclaimer withDisclaimerMessage(String value) {
        setDisclaimerMessage(value);
        return this;
    }

    public Disclaimer withDisclaimerRequired(Boolean value) {
        setDisclaimerRequired(value);
        return this;
    }

}
