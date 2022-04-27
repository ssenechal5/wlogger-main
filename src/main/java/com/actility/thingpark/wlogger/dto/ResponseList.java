package com.actility.thingpark.wlogger.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ResponseList
    extends ResponseSimple
{

    protected List<Element> data;

    /**
     * Default no-arg constructor
     * 
     */
    public ResponseList() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ResponseList(final String statusCode, final Boolean success, final String error, final String errorCode, final List<Element> data) {
        super(statusCode, success, error, errorCode);
        this.data = data;
    }

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * 
     * 
     */
    public List<Element> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return this.data;
    }

    public ResponseList withData(Element... values) {
        if (values!= null) {
            for (Element value: values) {
                getData().add(value);
            }
        }
        return this;
    }

    public ResponseList withData(Collection<Element> values) {
        if (values!= null) {
            getData().addAll(values);
        }
        return this;
    }

    @Override
    public ResponseList withStatusCode(String value) {
        setStatusCode(value);
        return this;
    }

    @Override
    public ResponseList withSuccess(Boolean value) {
        setSuccess(value);
        return this;
    }

    @Override
    public ResponseList withError(String value) {
        setError(value);
        return this;
    }

    @Override
    public ResponseList withErrorCode(String value) {
        setErrorCode(value);
        return this;
    }

}
