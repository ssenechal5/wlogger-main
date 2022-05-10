//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2022.03.31 à 08:49:19 AM CEST 
//


package com.actility.thingpark.smp.rest.dto.application;

import com.actility.thingpark.smp.rest.dto.AuthenticateDto;
import com.actility.thingpark.smp.rest.dto.DomainRestrictionsDto;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Classe Java pour AuthenticateApplicationDto complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AuthenticateApplicationDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.actility.com/smp/ws/common}AuthenticateDto"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="application"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="grantedPermissions"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="permission" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="domainRestrictions" type="{http://www.actility.com/smp/ws/common}DomainRestrictionsDto"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticateApplicationDto", propOrder = {
    "application",
    "domainRestrictions"
})
public class AuthenticateApplicationDto
    extends AuthenticateDto
{

    @XmlElement(required = true)
    protected Application application;
    @XmlElement(required = true)
    protected DomainRestrictionsDto domainRestrictions;

    /**
     * Default no-arg constructor
     * 
     */
    public AuthenticateApplicationDto() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public AuthenticateApplicationDto(final String login, final String password, final String thingParkID, final Admin admin, final Operator operator, final Supplier supplier, final Vendor vendor, final Subscriber subscriber, final User user, final String href, final GrantedPermissions grantedPermissions, final Application application, final DomainRestrictionsDto domainRestrictions) {
        super(login, password, thingParkID, admin, operator, supplier, vendor, subscriber, user, href, grantedPermissions);
        this.application = application;
        this.domainRestrictions = domainRestrictions;
    }

    /**
     * Obtient la valeur de la propriété application.
     * 
     * @return
     *     possible object is
     *     {@link Application }
     *     
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Définit la valeur de la propriété application.
     * 
     * @param value
     *     allowed object is
     *     {@link Application }
     *     
     */
    public void setApplication(Application value) {
        this.application = value;
    }

    /**
     * Obtient la valeur de la propriété domainRestrictions.
     * 
     * @return
     *     possible object is
     *     {@link DomainRestrictionsDto }
     *     
     */
    public DomainRestrictionsDto getDomainRestrictions() {
        return domainRestrictions;
    }

    /**
     * Définit la valeur de la propriété domainRestrictions.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainRestrictionsDto }
     *     
     */
    public void setDomainRestrictions(DomainRestrictionsDto value) {
        this.domainRestrictions = value;
    }

    public AuthenticateApplicationDto withApplication(Application value) {
        setApplication(value);
        return this;
    }

    public AuthenticateApplicationDto withDomainRestrictions(DomainRestrictionsDto value) {
        setDomainRestrictions(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withLogin(String value) {
        setLogin(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withPassword(String value) {
        setPassword(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withThingParkID(String value) {
        setThingParkID(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withAdmin(Admin value) {
        setAdmin(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withOperator(Operator value) {
        setOperator(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withSupplier(Supplier value) {
        setSupplier(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withVendor(Vendor value) {
        setVendor(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withSubscriber(Subscriber value) {
        setSubscriber(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withUser(User value) {
        setUser(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withHref(String value) {
        setHref(value);
        return this;
    }

    @Override
    public AuthenticateApplicationDto withGrantedPermissions(GrantedPermissions value) {
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
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    @XmlType(name = "", propOrder = {
        "id",
        "name",
        "grantedPermissions"
    })
    public static class Application
    {

        @XmlElement(name = "ID", required = true)
        protected String id;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected GrantedPermissions grantedPermissions;

        /**
         * Default no-arg constructor
         * 
         */
        public Application() {
            super();
        }

        /**
         * Fully-initialising value constructor
         * 
         */
        public Application(final String id, final String name, final GrantedPermissions grantedPermissions) {
            this.id = id;
            this.name = name;
            this.grantedPermissions = grantedPermissions;
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
         * Obtient la valeur de la propriété name.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Définit la valeur de la propriété name.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
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

        public Application withID(String value) {
            setID(value);
            return this;
        }

        public Application withName(String value) {
            setName(value);
            return this;
        }

        public Application withGrantedPermissions(GrantedPermissions value) {
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

    }

}
