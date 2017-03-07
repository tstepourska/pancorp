//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CorrectableRulingReport_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CorrectableRulingReport_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocSpec" type="{urn:oecd:ties:stf:v4}DocSpec_Type"/&gt;
 *         &lt;element name="RulingInfo" type="{urn:oecd:ties:etr:v1}RulingInfo_Type"/&gt;
 *         &lt;element name="ExchangeReason" type="{urn:oecd:ties:etr:v1}EtrExchangeReasonType_EnumType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="AffectedEntities" type="{urn:oecd:ties:etr:v1}OrganisationParty_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrectableRulingReport_Type", propOrder = {
    "docSpec",
    "rulingInfo",
    "exchangeReason",
    "affectedEntities"
})
public class CorrectableRulingReportType {

    @XmlElement(name = "DocSpec", required = true)
    protected DocSpecType docSpec;
    @XmlElement(name = "RulingInfo", required = true)
    protected RulingInfoType rulingInfo;
    @XmlElement(name = "ExchangeReason", required = true)
    @XmlSchemaType(name = "string")
    protected List<EtrExchangeReasonTypeEnumType> exchangeReason;
    @XmlElement(name = "AffectedEntities")
    protected List<OrganisationPartyType> affectedEntities;

    /**
     * Gets the value of the docSpec property.
     * 
     * @return
     *     possible object is
     *     {@link DocSpecType }
     *     
     */
    public DocSpecType getDocSpec() {
        return docSpec;
    }

    /**
     * Sets the value of the docSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocSpecType }
     *     
     */
    public void setDocSpec(DocSpecType value) {
        this.docSpec = value;
    }

    /**
     * Gets the value of the rulingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link RulingInfoType }
     *     
     */
    public RulingInfoType getRulingInfo() {
        return rulingInfo;
    }

    /**
     * Sets the value of the rulingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link RulingInfoType }
     *     
     */
    public void setRulingInfo(RulingInfoType value) {
        this.rulingInfo = value;
    }

    /**
     * Gets the value of the exchangeReason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exchangeReason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExchangeReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EtrExchangeReasonTypeEnumType }
     * 
     * 
     */
    public List<EtrExchangeReasonTypeEnumType> getExchangeReason() {
        if (exchangeReason == null) {
            exchangeReason = new ArrayList<EtrExchangeReasonTypeEnumType>();
        }
        return this.exchangeReason;
    }

    /**
     * Gets the value of the affectedEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the affectedEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAffectedEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrganisationPartyType }
     * 
     * 
     */
    public List<OrganisationPartyType> getAffectedEntities() {
        if (affectedEntities == null) {
            affectedEntities = new ArrayList<OrganisationPartyType>();
        }
        return this.affectedEntities;
    }

}
