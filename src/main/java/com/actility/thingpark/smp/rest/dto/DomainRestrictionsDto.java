//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0 
// Voir <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2022.03.31 à 08:49:19 AM CEST 
//


package com.actility.thingpark.smp.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>Classe Java pour DomainRestrictionsDto complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DomainRestrictionsDto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="and"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="domainRestriction" type="{http://www.actility.com/smp/ws/common}DomainRestrictionDto" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "DomainRestrictionsDto", propOrder = {
    "ands"
})
public class DomainRestrictionsDto
{

    @XmlElementWrapper(name = "and", required = true)
    @XmlElement(name = "domainRestriction")
    @JsonProperty("and")
    protected List<DomainRestrictionDto> ands = new ArrayList<DomainRestrictionDto>();

    /**
     * Default no-arg constructor
     * 
     */
    public DomainRestrictionsDto() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public DomainRestrictionsDto(final List<DomainRestrictionDto> ands) {
        this.ands = ands;
    }

    public List<DomainRestrictionDto> getAnds() {
        return ands;
    }

    public void setAnds(List<DomainRestrictionDto> ands) {
        this.ands = ands;
    }

    public DomainRestrictionsDto withAnds(DomainRestrictionDto... values) {
        if (values!= null) {
            for (DomainRestrictionDto value: values) {
                getAnds().add(value);
            }
        }
        return this;
    }

    public DomainRestrictionsDto withAnds(Collection<DomainRestrictionDto> values) {
        if (values!= null) {
            getAnds().addAll(values);
        }
        return this;
    }

    public DomainRestrictionsDto withAnds(List<DomainRestrictionDto> ands) {
        setAnds(ands);
        return this;
    }

}
