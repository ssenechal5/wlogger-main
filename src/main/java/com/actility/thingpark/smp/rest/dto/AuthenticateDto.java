//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2022.03.31 à 08:49:19 AM CEST 
//


package com.actility.thingpark.smp.rest.dto;

import com.actility.thingpark.smp.rest.dto.application.AuthenticateApplicationDto;
import com.actility.thingpark.smp.rest.dto.system.AuthenticateSystemDto;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>Classe Java pour AuthenticateDto complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AuthenticateDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="login" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="thingParkID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="admin"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="operator"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="vendor"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="subscriber"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="extID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="domainsUsed" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="user"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="grantedPermissions"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="permission" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticateDto", propOrder = {
    "login",
    "password",
    "thingParkID",
    "admin",
    "operator",
    "supplier",
    "vendor",
    "subscriber",
    "user",
    "href",
    "grantedPermissions"
})
@XmlSeeAlso({
    AuthenticateApplicationDto.class,
    AuthenticateSystemDto.class,
})
public class AuthenticateDto
{

    @XmlElement(required = true)
    protected String login;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected String thingParkID;
    @XmlElement(required = true)
    protected Admin admin;
    @XmlElement(required = true)
    protected Operator operator;
    @XmlElement(required = true)
    protected Supplier supplier;
    @XmlElement(required = true)
    protected Vendor vendor;
    @XmlElement(required = true)
    protected Subscriber subscriber;
    @XmlElement(required = true)
    protected User user;
    @XmlElement(required = true)
    protected String href;
    @XmlElement(required = true)
    protected GrantedPermissions grantedPermissions;

    /**
     * Default no-arg constructor
     * 
     */
    public AuthenticateDto() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public AuthenticateDto(final String login, final String password, final String thingParkID, final Admin admin, final Operator operator, final Supplier supplier, final Vendor vendor, final Subscriber subscriber, final User user, final String href, final GrantedPermissions grantedPermissions) {
        this.login = login;
        this.password = password;
        this.thingParkID = thingParkID;
        this.admin = admin;
        this.operator = operator;
        this.supplier = supplier;
        this.vendor = vendor;
        this.subscriber = subscriber;
        this.user = user;
        this.href = href;
        this.grantedPermissions = grantedPermissions;
    }

    /**
     * Obtient la valeur de la propriété login.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Définit la valeur de la propriété login.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Obtient la valeur de la propriété password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit la valeur de la propriété password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Obtient la valeur de la propriété thingParkID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThingParkID() {
        return thingParkID;
    }

    /**
     * Définit la valeur de la propriété thingParkID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThingParkID(String value) {
        this.thingParkID = value;
    }

    /**
     * Obtient la valeur de la propriété admin.
     * 
     * @return
     *     possible object is
     *     {@link Admin }
     *     
     */
    public Admin getAdmin() {
        return admin;
    }

    /**
     * Définit la valeur de la propriété admin.
     * 
     * @param value
     *     allowed object is
     *     {@link Admin }
     *     
     */
    public void setAdmin(Admin value) {
        this.admin = value;
    }

    /**
     * Obtient la valeur de la propriété operator.
     * 
     * @return
     *     possible object is
     *     {@link Operator }
     *     
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Définit la valeur de la propriété operator.
     * 
     * @param value
     *     allowed object is
     *     {@link Operator }
     *     
     */
    public void setOperator(Operator value) {
        this.operator = value;
    }

    /**
     * Obtient la valeur de la propriété supplier.
     * 
     * @return
     *     possible object is
     *     {@link Supplier }
     *     
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Définit la valeur de la propriété supplier.
     * 
     * @param value
     *     allowed object is
     *     {@link Supplier }
     *     
     */
    public void setSupplier(Supplier value) {
        this.supplier = value;
    }

    /**
     * Obtient la valeur de la propriété vendor.
     * 
     * @return
     *     possible object is
     *     {@link Vendor }
     *     
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * Définit la valeur de la propriété vendor.
     * 
     * @param value
     *     allowed object is
     *     {@link Vendor }
     *     
     */
    public void setVendor(Vendor value) {
        this.vendor = value;
    }

    /**
     * Obtient la valeur de la propriété subscriber.
     * 
     * @return
     *     possible object is
     *     {@link Subscriber }
     *     
     */
    public Subscriber getSubscriber() {
        return subscriber;
    }

    /**
     * Définit la valeur de la propriété subscriber.
     * 
     * @param value
     *     allowed object is
     *     {@link Subscriber }
     *     
     */
    public void setSubscriber(Subscriber value) {
        this.subscriber = value;
    }

    /**
     * Obtient la valeur de la propriété user.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getUser() {
        return user;
    }

    /**
     * Définit la valeur de la propriété user.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setUser(User value) {
        this.user = value;
    }

    /**
     * Obtient la valeur de la propriété href.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Définit la valeur de la propriété href.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Obtient la valeur de la propriété grantedPermissions.
     * 
     * @return
     *     possible object is
     *     {@link GrantedPermissions }
     *     
     */
    public GrantedPermissions getGrantedPermissions() {
        return grantedPermissions;
    }

    /**
     * Définit la valeur de la propriété grantedPermissions.
     * 
     * @param value
     *     allowed object is
     *     {@link GrantedPermissions }
     *     
     */
    public void setGrantedPermissions(GrantedPermissions value) {
        this.grantedPermissions = value;
    }

    public AuthenticateDto withLogin(String value) {
        setLogin(value);
        return this;
    }

    public AuthenticateDto withPassword(String value) {
        setPassword(value);
        return this;
    }

    public AuthenticateDto withThingParkID(String value) {
        setThingParkID(value);
        return this;
    }

    public AuthenticateDto withAdmin(Admin value) {
        setAdmin(value);
        return this;
    }

    public AuthenticateDto withOperator(Operator value) {
        setOperator(value);
        return this;
    }

    public AuthenticateDto withSupplier(Supplier value) {
        setSupplier(value);
        return this;
    }

    public AuthenticateDto withVendor(Vendor value) {
        setVendor(value);
        return this;
    }

    public AuthenticateDto withSubscriber(Subscriber value) {
        setSubscriber(value);
        return this;
    }

    public AuthenticateDto withUser(User value) {
        setUser(value);
        return this;
    }

    public AuthenticateDto withHref(String value) {
        setHref(value);
        return this;
    }

    public AuthenticateDto withGrantedPermissions(GrantedPermissions value) {
        setGrantedPermissions(value);
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
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "href"
    })
    public static class Admin
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String href;

        /**
         * Default no-arg constructor
         * 
         */
        public Admin() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Admin(final String id, final String href) {
            this.id = id;
            this.href = href;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        public Admin withID(String value) {
            setID(value);
            return this;
        }

        public Admin withHref(String value) {
            setHref(value);
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
     *         &lt;element name="permission" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "permission"
    })
    public static class GrantedPermissions
    {

        protected List<String> permission;

        /**
         * Default no-arg constructor
         * 
         */
        public GrantedPermissions() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public GrantedPermissions(final List<String> permission) {
            this.permission = permission;
        }

        /**
         * Gets the value of the permission property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the permission property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPermission().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPermission() {
            if (permission == null) {
                permission = new ArrayList<String>();
            }
            return this.permission;
        }

        public GrantedPermissions withPermission(String... values) {
            if (values!= null) {
                for (String value: values) {
                    getPermission().add(value);
                }
            }
            return this;
        }

        public GrantedPermissions withPermission(Collection<String> values) {
            if (values!= null) {
                getPermission().addAll(values);
            }
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
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "href"
    })
    public static class Operator
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String href;

        /**
         * Default no-arg constructor
         * 
         */
        public Operator() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Operator(final String id, final String href) {
            this.id = id;
            this.href = href;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }


        public Operator withID(String value) {
            setID(value);
            return this;
        }

        public Operator withHref(String value) {
            setHref(value);
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
     *         &lt;element name="extID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="domainsUsed" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "extID",
        "href",
        "domainsUsed"
    })
    public static class Subscriber
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String extID;
        @XmlElement(required = true)
        protected String href;
        protected boolean domainsUsed;

        /**
         * Default no-arg constructor
         * 
         */
        public Subscriber() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Subscriber(final String id, final String extID, final String href, final boolean domainsUsed) {
            this.id = id;
            this.extID = extID;
            this.href = href;
            this.domainsUsed = domainsUsed;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété extID.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExtID() {
            return extID;
        }

        /**
         * Définit la valeur de la propriété extID.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExtID(String value) {
            this.extID = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        /**
         * Obtient la valeur de la propriété domainsUsed.
         * 
         */
        public boolean isDomainsUsed() {
            return domainsUsed;
        }

        /**
         * Définit la valeur de la propriété domainsUsed.
         * 
         */
        public void setDomainsUsed(boolean value) {
            this.domainsUsed = value;
        }

        public Subscriber withID(String value) {
            setID(value);
            return this;
        }

        public Subscriber withExtID(String value) {
            setExtID(value);
            return this;
        }

        public Subscriber withHref(String value) {
            setHref(value);
            return this;
        }

        public Subscriber withDomainsUsed(boolean value) {
            setDomainsUsed(value);
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
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "href"
    })
    public static class Supplier
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String href;

        /**
         * Default no-arg constructor
         * 
         */
        public Supplier() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Supplier(final String id, final String href) {
            this.id = id;
            this.href = href;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        public Supplier withID(String value) {
            setID(value);
            return this;
        }

        public Supplier withHref(String value) {
            setHref(value);
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
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "href"
    })
    public static class User
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String href;

        /**
         * Default no-arg constructor
         * 
         */
        public User() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public User(final String id, final String href) {
            this.id = id;
            this.href = href;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        public User withID(String value) {
            setID(value);
            return this;
        }

        public User withHref(String value) {
            setHref(value);
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
     *         &lt;element name="href" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "href"
    })
    public static class Vendor
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String href;

        /**
         * Default no-arg constructor
         * 
         */
        public Vendor() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Vendor(final String id, final String href) {
            this.id = id;
            this.href = href;
        }

        /**
         * Obtient la valeur de la propriété id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Définit la valeur de la propriété id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Obtient la valeur de la propriété href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Définit la valeur de la propriété href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        public Vendor withID(String value) {
            setID(value);
            return this;
        }

        public Vendor withHref(String value) {
            setHref(value);
            return this;
        }

    }

}
