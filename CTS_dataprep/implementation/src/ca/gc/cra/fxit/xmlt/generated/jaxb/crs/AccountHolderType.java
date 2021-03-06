//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:05:54 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.crs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountHolder_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountHolder_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="Individual" type="{urn:oecd:ties:crs:v1}PersonParty_Type"/&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="Organisation" type="{urn:oecd:ties:crs:v1}OrganisationParty_Type"/&gt;
 *             &lt;element name="AcctHolderType" type="{urn:oecd:ties:crs:v1}CrsAcctHolderType_EnumType"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountHolder_Type", propOrder = {
    "individual",
    "organisation",
    "acctHolderType"
})
public class AccountHolderType {

    @XmlElement(name = "Individual")
    protected PersonPartyType individual;
    @XmlElement(name = "Organisation")
    protected OrganisationPartyType organisation;
    @XmlElement(name = "AcctHolderType")
    @XmlSchemaType(name = "string")
    protected CrsAcctHolderTypeEnumType acctHolderType;

    /**
     * Gets the value of the individual property.
     * 
     * @return
     *     possible object is
     *     {@link PersonPartyType }
     *     
     */
    public PersonPartyType getIndividual() {
        return individual;
    }

    /**
     * Sets the value of the individual property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonPartyType }
     *     
     */
    public void setIndividual(PersonPartyType value) {
        this.individual = value;
    }

    /**
     * Gets the value of the organisation property.
     * 
     * @return
     *     possible object is
     *     {@link OrganisationPartyType }
     *     
     */
    public OrganisationPartyType getOrganisation() {
        return organisation;
    }

    /**
     * Sets the value of the organisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganisationPartyType }
     *     
     */
    public void setOrganisation(OrganisationPartyType value) {
        this.organisation = value;
    }

    /**
     * Gets the value of the acctHolderType property.
     * 
     * @return
     *     possible object is
     *     {@link CrsAcctHolderTypeEnumType }
     *     
     */
    public CrsAcctHolderTypeEnumType getAcctHolderType() {
        return acctHolderType;
    }

    /**
     * Sets the value of the acctHolderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrsAcctHolderTypeEnumType }
     *     
     */
    public void setAcctHolderType(CrsAcctHolderTypeEnumType value) {
        this.acctHolderType = value;
    }

}
