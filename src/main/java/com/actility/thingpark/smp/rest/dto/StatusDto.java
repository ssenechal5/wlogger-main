//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.28 at 03:08:24 PM CET 
//


package com.actility.thingpark.smp.rest.dto;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for StatusDto complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StatusDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="state" type="{http://www.actility.com/smp/ws/common}Status"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusDto", propOrder = {
        "state"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/common", name = "status")
public class StatusDto {

  @XmlElement(required = true)
  @XmlSchemaType(name = "string")
  protected Status state;

  /**
   * Default no-arg constructor
   */
  public StatusDto() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public StatusDto(final Status state) {
    this.state = state;
  }

  /**
   * Gets the value of the state property.
   *
   * @return possible object is
   * {@link Status }
   */
  public Status getState() {
    return state;
  }

  /**
   * Sets the value of the state property.
   *
   * @param value allowed object is
   *              {@link Status }
   */
  public void setState(Status value) {
    this.state = value;
  }

  public StatusDto withState(Status value) {
    setState(value);
    return this;
  }

}
