package com.actility.thingpark.wlogger.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseSimple", propOrder = {
        "statusCode",
        "success",
        "error",
        "errorCode"
})
@XmlRootElement(namespace = "", name = "responseSimple")
public class ResponseSimple {

    protected String statusCode;
    protected Boolean success;
    protected String error;
    protected String errorCode;

    /**
     * Default no-arg constructor
     * 
     */
    public ResponseSimple() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ResponseSimple(final String statusCode, final Boolean success, final String error, final String errorCode) {
        this.statusCode = statusCode;
        this.success = success;
        this.error = error;
        this.errorCode = errorCode;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the success property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSuccess(Boolean value) {
        this.success = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    public ResponseSimple withStatusCode(String value) {
        setStatusCode(value);
        return this;
    }

    public ResponseSimple withSuccess(Boolean value) {
        setSuccess(value);
        return this;
    }

    public ResponseSimple withError(String value) {
        setError(value);
        return this;
    }

    public ResponseSimple withErrorCode(String value) {
        setErrorCode(value);
        return this;
    }

}
