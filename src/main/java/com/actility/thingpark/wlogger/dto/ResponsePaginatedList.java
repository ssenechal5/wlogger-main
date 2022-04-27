package com.actility.thingpark.wlogger.dto;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponsePaginatedList
    extends ResponseSimple
{

    protected List<Element> data;
    protected Boolean more;

    /**
     * Default no-arg constructor
     * 
     */
    public ResponsePaginatedList() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public ResponsePaginatedList(final String statusCode, final Boolean success, final String error, final String errorCode, final List<Element> data, final Boolean more) {
        super(statusCode, success, error, errorCode);
        this.data = data;
        this.more = more;
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

    /**
     * Gets the value of the more property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMore() {
        return more;
    }

    /**
     * Sets the value of the more property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMore(Boolean value) {
        this.more = value;
    }

    public ResponsePaginatedList withData(Element... values) {
        if (values!= null) {
            for (Element value: values) {
                getData().add(value);
            }
        }
        return this;
    }

    public ResponsePaginatedList withData(Collection<Element> values) {
        if (values!= null) {
            getData().addAll(values);
        }
        return this;
    }

    public ResponsePaginatedList withMore(Boolean value) {
        setMore(value);
        return this;
    }

    @Override
    public ResponsePaginatedList withStatusCode(String value) {
        setStatusCode(value);
        return this;
    }

    @Override
    public ResponsePaginatedList withSuccess(Boolean value) {
        setSuccess(value);
        return this;
    }

    @Override
    public ResponsePaginatedList withError(String value) {
        setError(value);
        return this;
    }

    @Override
    public ResponsePaginatedList withErrorCode(String value) {
        setErrorCode(value);
        return this;
    }

}
