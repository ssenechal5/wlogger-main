//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.28 at 03:08:24 PM CET 
//


package com.actility.thingpark.smp.rest.dto.application;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for HrefDto complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="HrefDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.actility.com/smp/ws/common}HrefType"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HrefDto", propOrder = {
        "href"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/application", name = "id")
public class HrefDto {


  @XmlElement(required = true)
  protected String href;

  /**
   * Default no-arg constructor
   */
  public HrefDto() {
  }

  /**
   * Fully-initialising value constructor
   */
  public HrefDto(final String href) {
    this.href = href;
  }

  /**
   * Gets the value of the href property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the value of the href property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setHref(String value) {
    this.href = value;
  }

  public HrefDto withHref(String value) {
    setHref(value);
    return this;
  }

}