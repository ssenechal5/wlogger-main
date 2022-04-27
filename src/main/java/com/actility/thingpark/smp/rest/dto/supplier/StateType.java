//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.11.29 à 11:57:33 AM CET 
//


package com.actility.thingpark.smp.rest.dto.supplier;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour StateType complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
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
   * Obtient la valeur de la propriété value.
   *
   * @return possible object is
   * {@link String }
   */
  public String getValue() {
    return value;
  }

  /**
   * Définit la valeur de la propriété value.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Obtient la valeur de la propriété timestamp.
   *
   * @return possible object is
   * {@link XMLGregorianCalendar }
   */
  public XMLGregorianCalendar getTimestamp() {
    return timestamp;
  }

  /**
   * Définit la valeur de la propriété timestamp.
   *
   * @param value allowed object is
   *              {@link XMLGregorianCalendar }
   */
  public void setTimestamp(XMLGregorianCalendar value) {
    this.timestamp = value;
  }

  /**
   * Obtient la valeur de la propriété info1.
   *
   * @return possible object is
   * {@link String }
   */
  public String getInfo1() {
    return info1;
  }

  /**
   * Définit la valeur de la propriété info1.
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
