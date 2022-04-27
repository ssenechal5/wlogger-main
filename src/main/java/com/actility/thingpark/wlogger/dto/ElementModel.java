package com.actility.thingpark.wlogger.dto;

public class ElementModel implements Element {

    protected String producerId;
    protected String moduleId;
    protected String version;

    /**
     * Default no-arg constructor
     * 
     */
    public ElementModel() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ElementModel(final String producerId, final String moduleId, final String version) {
        super();
        this.producerId = producerId;
        this.moduleId = moduleId;
        this.version = version;
    }

    /**
     * Gets the value of the producerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducerId() {
        return producerId;
    }

    /**
     * Sets the value of the producerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducerId(String value) {
        this.producerId = value;
    }

    /**
     * Gets the value of the moduleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * Sets the value of the moduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleId(String value) {
        this.moduleId = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    public ElementModel withProducerId(String value) {
        setProducerId(value);
        return this;
    }

    public ElementModel withModuleId(String value) {
        setModuleId(value);
        return this;
    }

    public ElementModel withVersion(String value) {
        setVersion(value);
        return this;
    }

}
