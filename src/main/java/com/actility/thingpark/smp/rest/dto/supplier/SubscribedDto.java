//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.11.29 à 11:57:33 AM CET 
//


package com.actility.thingpark.smp.rest.dto.supplier;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour SubscribedDto complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="SubscribedDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transactionID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="operation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="operator"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="supplier"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubscribedDto", propOrder = {
        "transactionID",
        "operation",
        "operator",
        "supplier",
        "href"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/supplier", name = "subscribed")
public class SubscribedDto {

  @XmlElement(required = true)
  protected String transactionID;
  @XmlElement(required = true)
  protected String operation;
  @XmlElement(required = true)
  protected Operator operator;
  @XmlElement(required = true)
  protected Supplier supplier;
  @XmlElement(required = true)
  protected String href;

  /**
   * Default no-arg constructor
   */
  public SubscribedDto() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public SubscribedDto(final String transactionID, final String operation, final Operator operator, final Supplier supplier, final String href) {
    this.transactionID = transactionID;
    this.operation = operation;
    this.operator = operator;
    this.supplier = supplier;
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
   * Obtient la valeur de la propriété operation.
   *
   * @return possible object is
   * {@link String }
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Définit la valeur de la propriété operation.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setOperation(String value) {
    this.operation = value;
  }

  /**
   * Obtient la valeur de la propriété operator.
   *
   * @return possible object is
   * {@link Operator }
   */
  public Operator getOperator() {
    return operator;
  }

  /**
   * Définit la valeur de la propriété operator.
   *
   * @param value allowed object is
   *              {@link Operator }
   */
  public void setOperator(Operator value) {
    this.operator = value;
  }

  /**
   * Obtient la valeur de la propriété supplier.
   *
   * @return possible object is
   * {@link Supplier }
   */
  public Supplier getSupplier() {
    return supplier;
  }

  /**
   * Définit la valeur de la propriété supplier.
   *
   * @param value allowed object is
   *              {@link Supplier }
   */
  public void setSupplier(Supplier value) {
    this.supplier = value;
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

  public SubscribedDto withTransactionID(String value) {
    setTransactionID(value);
    return this;
  }

  public SubscribedDto withOperation(String value) {
    setOperation(value);
    return this;
  }

  public SubscribedDto withOperator(Operator value) {
    setOperator(value);
    return this;
  }

  public SubscribedDto withSupplier(Supplier value) {
    setSupplier(value);
    return this;
  }

  public SubscribedDto withHref(String value) {
    setHref(value);
    return this;
  }


  /**
   * <p>Classe Java pour anonymous complex type.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
          "id"
  })
  public static class Operator {

    @XmlElement(name = "ID", required = true)
    protected String id;

    /**
     * Default no-arg constructor
     */
    public Operator() {
      super();
    }

    /**
     * Fully-initialising value constructor
     */
    public Operator(final String id) {
      this.id = id;
    }

    /**
     * Obtient la valeur de la propriété id.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
      return id;
    }

    /**
     * Définit la valeur de la propriété id.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
      this.id = value;
    }

    public Operator withID(String value) {
      setID(value);
      return this;
    }

  }


  /**
   * <p>Classe Java pour anonymous complex type.
   *
   * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
   *
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
          "id"
  })
  public static class Supplier {

    @XmlElement(name = "ID", required = true)
    protected String id;

    /**
     * Default no-arg constructor
     */
    public Supplier() {
      super();
    }

    /**
     * Fully-initialising value constructor
     */
    public Supplier(final String id) {
      this.id = id;
    }

    /**
     * Obtient la valeur de la propriété id.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
      return id;
    }

    /**
     * Définit la valeur de la propriété id.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
      this.id = value;
    }

    public Supplier withID(String value) {
      setID(value);
      return this;
    }

  }

}
