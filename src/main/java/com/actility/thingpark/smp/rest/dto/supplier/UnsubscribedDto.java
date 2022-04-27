//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.11.29 à 11:57:33 AM CET 
//


package com.actility.thingpark.smp.rest.dto.supplier;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour UnsubscribedDto complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="UnsubscribedDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transactionID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsubscribedDto", propOrder = {
        "transactionID",
        "href"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/supplier", name = "unsubscribed")
public class UnsubscribedDto {

  @XmlElement(required = true)
  protected String transactionID;
  @XmlElement(required = true)
  protected String href;

  /**
   * Default no-arg constructor
   */
  public UnsubscribedDto() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public UnsubscribedDto(final String transactionID, final String href) {
    this.transactionID = transactionID;
    this.href = href;
  }

  /**
   * Obtient la valeur de la propriété transactionID.
   *
   * @return possible object is
   * {@link String }
   */
  public String getTransactionID() {
    return transactionID;
  }

  /**
   * Définit la valeur de la propriété transactionID.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setTransactionID(String value) {
    this.transactionID = value;
  }

  /**
   * Obtient la valeur de la propriété href.
   *
   * @return possible object is
   * {@link String }
   */
  public String getHref() {
    return href;
  }

  /**
   * Définit la valeur de la propriété href.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setHref(String value) {
    this.href = value;
  }

  public UnsubscribedDto withTransactionID(String value) {
    setTransactionID(value);
    return this;
  }

  public UnsubscribedDto withHref(String value) {
    setHref(value);
    return this;
  }

}
