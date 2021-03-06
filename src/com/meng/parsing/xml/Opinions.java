//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.02 at 01:28:47 PM EST 
//


package com.meng.parsing.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}opinion" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="numReturned" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="start" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="totalNumFound" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "opinion"
})
@XmlRootElement(name = "opinions")
public class Opinions {

    @XmlElement(required = true)
    protected List<Opinion> opinion;
    @XmlAttribute(name = "numReturned", required = true)
    protected BigInteger numReturned;
    @XmlAttribute(name = "start", required = true)
    protected BigInteger start;
    @XmlAttribute(name = "totalNumFound", required = true)
    protected BigInteger totalNumFound;

    /**
     * Gets the value of the opinion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the opinion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOpinion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Opinion }
     * 
     * 
     */
    public List<Opinion> getOpinion() {
        if (opinion == null) {
            opinion = new ArrayList<Opinion>();
        }
        return this.opinion;
    }

    /**
     * Gets the value of the numReturned property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumReturned() {
        return numReturned;
    }

    /**
     * Sets the value of the numReturned property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumReturned(BigInteger value) {
        this.numReturned = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStart(BigInteger value) {
        this.start = value;
    }

    /**
     * Gets the value of the totalNumFound property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalNumFound() {
        return totalNumFound;
    }

    /**
     * Sets the value of the totalNumFound property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalNumFound(BigInteger value) {
        this.totalNumFound = value;
    }

}
