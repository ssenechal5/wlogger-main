//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2019.11.29 à 11:57:33 AM CET 
//


package com.actility.thingpark.smp.rest.dto.supplier;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour UpdatedDto complex type.
 *
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType name="UpdatedDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transactionID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="application"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="state" type="{http://www.actility.com/smp/ws/supplier}StateType"/&gt;
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
@XmlType(name = "UpdatedDto", propOrder = {
        "transactionID",
        "application",
        "href"
})
@XmlRootElement(namespace = "http://www.actility.com/smp/ws/supplier", name = "updated")
public class UpdatedDto {

  @XmlElement(required = true)
  protected String transactionID;
  @XmlElement(required = true)
  protected Application application;
  @XmlElement(required = true)
  protected String href;

  /**
   * Default no-arg constructor
   */
  public UpdatedDto() {
    super();
  }

  /**
   * Fully-initialising value constructor
   */
  public UpdatedDto(final String transactionID, final Application application, final String href) {
    this.transactionID = transactionID;
    this.application = application;
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
   * Obtient la valeur de la propriété application.
   *
   * @return possible object is
   * {@link Application }
   */
  public Application getApplication() {
    return application;
  }

  /**
   * Définit la valeur de la propriété application.
   *
   * @param value allowed object is
   *              {@link Application }
   */
  public void setApplication(Application value) {
    this.application = value;
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

  public UpdatedDto withTransactionID(String value) {
    setTransactionID(value);
    return this;
  }

  public UpdatedDto withApplication(Application value) {
    setApplication(value);
    return this;
  }

  public UpdatedDto withHref(String value) {
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
   *         &lt;element name="state" type="{http://www.actility.com/smp/ws/supplier}StateType"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
          "state"
  })
  public static class Application {

    @XmlElement(required = true)
    protected StateType state;

    /**
     * Default no-arg constructor
     */
    public Application() {
      super();
    }

    /**
     * Fully-initialising value constructor
     */
    public Application(final StateType state) {
      this.state = state;
    }

    /**
     * Obtient la valeur de la propriété state.
     *
     * @return possible object is
     * {@link StateType }
     */
    public StateType getState() {
      return state;
    }

    /**
     * Définit la valeur de la propriété state.
     *
     * @param value allowed object is
     *              {@link StateType }
     */
    public void setState(StateType value) {
      this.state = value;
    }

    public Application withState(StateType value) {
      setState(value);
      return this;
    }

  }

}
