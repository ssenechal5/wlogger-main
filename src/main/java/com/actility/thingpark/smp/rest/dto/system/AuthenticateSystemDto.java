//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2022.03.31 à 08:49:19 AM CEST 
//


package com.actility.thingpark.smp.rest.dto.system;

import com.actility.thingpark.smp.rest.dto.AuthenticateDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Classe Java pour AuthenticateSystemDto complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AuthenticateSystemDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.actility.com/smp/ws/common}AuthenticateDto"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="domain" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticateSystemDto", propOrder = {
    "domain"
})
public class AuthenticateSystemDto
    extends AuthenticateDto
{

    @XmlElement(required = true)
    protected String domain;

    /**
     * Default no-arg constructor
     * 
     */
    public AuthenticateSystemDto() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public AuthenticateSystemDto(final String login, final String password, final String thingParkID, final Admin admin, final Operator operator, final Supplier supplier, final Vendor vendor, final Subscriber subscriber, final User user, final String href, final GrantedPermissions grantedPermissions, final String domain) {
        super(login, password, thingParkID, admin, operator, supplier, vendor, subscriber, user, href, grantedPermissions);
        this.domain = domain;
    }

    /**
     * Obtient la valeur de la propriété domain.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Définit la valeur de la propriété domain.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    public AuthenticateSystemDto withDomain(String value) {
        setDomain(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withLogin(String value) {
        setLogin(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withPassword(String value) {
        setPassword(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withThingParkID(String value) {
        setThingParkID(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withAdmin(Admin value) {
        setAdmin(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withOperator(Operator value) {
        setOperator(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withSupplier(Supplier value) {
        setSupplier(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withVendor(Vendor value) {
        setVendor(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withSubscriber(Subscriber value) {
        setSubscriber(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withUser(User value) {
        setUser(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withHref(String value) {
        setHref(value);
        return this;
    }

    @Override
    public AuthenticateSystemDto withGrantedPermissions(GrantedPermissions value) {
        setGrantedPermissions(value);
        return this;
    }

}
