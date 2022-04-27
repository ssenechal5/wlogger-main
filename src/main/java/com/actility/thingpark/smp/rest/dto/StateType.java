//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.28 at 03:08:24 PM CET 
//


package com.actility.thingpark.smp.rest.dto;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for StateType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StateType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="info1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StateType", propOrder = {
        "value",
        "timestamp",
        "info1"
})
public class StateType {

  @XmlElement(required = true)
  protected String value;
  @XmlElement(required = true)
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar timestamp;
  @XmlElement(required = true)
  protected String info1;

  /**
   * Default no-arg constructor
   */
  public StateType() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public StateType(final String value, final XMLGregorianCalendar timestamp, final String info1) {
    this.value = value;
    this.timestamp = timestamp;
    this.info1 = info1;
  }

  /**
   * Gets the value of the value property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the value of the timestamp property.
   *
   * @return possible object is
   * {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the timestamp property.
   *
   * @param value allowed object is
   *              {@link XMLGregorianCalendar }
   */
  public void setTimestamp(XMLGregorianCalendar value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the info1 property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getInfo1() {
    return info1;
  }

  /**
   * Sets the value of the info1 property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setInfo1(String value) {
    this.info1 = value;
  }

  public StateType withValue(String value) {
    setValue(value);
    return this;
  }

  public StateType withTimestamp(XMLGregorianCalendar value) {
    setTimestamp(value);
    return this;
  }

  public StateType withInfo1(String value) {
    setInfo1(value);
    return this;
  }

}