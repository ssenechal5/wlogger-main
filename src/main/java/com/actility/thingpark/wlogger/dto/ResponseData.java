package com.actility.thingpark.wlogger.dto;

public class ResponseData
    extends ResponseSimple
{

    protected Element data;

    /**
     * Default no-arg constructor
     * 
     */
    public ResponseData() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ResponseData(final String statusCode, final Boolean success, final String error, final String errorCode, final Element data) {
        super(statusCode, success, error, errorCode);
        this.data = data;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     
     */
    public Element getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     
     */
    public void setData(Element value) {
        this.data = value;
    }

    public ResponseData withData(Element value) {
        setData(value);
        return this;
    }

    @Override
    public ResponseData withStatusCode(String value) {
        setStatusCode(value);
        return this;
    }

    @Override
    public ResponseData withSuccess(Boolean value) {
        setSuccess(value);
        return this;
    }

    @Override
    public ResponseData withError(String value) {
        setError(value);
        return this;
    }

    @Override
    public ResponseData withErrorCode(String value) {
        setErrorCode(value);
        return this;
    }

}
