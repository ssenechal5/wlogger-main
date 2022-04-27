//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.11.29 à 11:57:33 AM CET 
//


package com.actility.thingpark.rest.dto;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour ErrorTO complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="ErrorTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="info1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="info2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errors" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorTO", propOrder = {
        "code",
        "message",
        "info1",
        "info2",
        "errors"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/common", name = "error")
public class ErrorTO {

  protected int code;
  @XmlElement(required = true)
  protected String message;
  @XmlElement(required = true)
  protected String info1;
  @XmlElement(required = true)
  protected String info2;
  protected Errors errors;

  /**
   * Default no-arg constructor
   */
  public ErrorTO() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public ErrorTO(final int code, final String message, final String info1, final String info2, final Errors errors) {
    this.code = code;
    this.message = message;
    this.info1 = info1;
    this.info2 = info2;
    this.errors = errors;
  }

  /**
   * Obtient la valeur de la propriété code.
   */
  public int getCode() {
    return code;
  }

  /**
   * Définit la valeur de la propriété code.
   */
  public void setCode(int value) {
    this.code = value;
  }

  /**
   * Obtient la valeur de la propriété message.
   *
   * @return possible object is
   * {@link String }
   */
  public String getMessage() {
    return message;
  }

  /**
   * Définit la valeur de la propriété message.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setMessage(String value) {
    this.message = value;
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

  /**
   * Obtient la valeur de la propriété info2.
   *
   * @return possible object is
   * {@link String }
   */
  public String getInfo2() {
    return info2;
  }

  /**
   * Définit la valeur de la propriété info2.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setInfo2(String value) {
    this.info2 = value;
  }

  /**
   * Obtient la valeur de la propriété errors.
   *
   * @return possible object is
   * {@link Errors }
   */
  public Errors getErrors() {
    return errors;
  }

  /**
   * Définit la valeur de la propriété errors.
   *
   * @param value allowed object is
   *              {@link Errors }
   */
  public void setErrors(Errors value) {
    this.errors = value;
  }

  public ErrorTO withCode(int value) {
    setCode(value);
    return this;
  }

  public ErrorTO withMessage(String value) {
    setMessage(value);
    return this;
  }

  public ErrorTO withInfo1(String value) {
    setInfo1(value);
    return this;
  }

  public ErrorTO withInfo2(String value) {
    setInfo2(value);
    return this;
  }

  public ErrorTO withErrors(Errors value) {
    setErrors(value);
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
   *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
          "csv"
  })
  public static class Errors {

    @XmlElement(required = true)
    protected String csv;

    /**
     * Default no-arg constructor
     */
    public Errors() {
      super();
    }

    /**
     * Fully-initialising value constructor
     */
    public Errors(final String csv) {
      this.csv = csv;
    }

    /**
     * Obtient la valeur de la propriété csv.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCsv() {
      return csv;
    }

    /**
     * Définit la valeur de la propriété csv.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCsv(String value) {
      this.csv = value;
    }

    public Errors withCsv(String value) {
      setCsv(value);
      return this;
    }

  }

}
