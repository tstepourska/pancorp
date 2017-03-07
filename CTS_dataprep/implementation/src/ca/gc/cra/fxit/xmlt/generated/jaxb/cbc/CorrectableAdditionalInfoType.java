//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:16:39 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.cbc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CorrectableAdditionalInfo_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CorrectableAdditionalInfo_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocSpec" type="{urn:oecd:ties:stf:v4}DocSpec_Type"/&gt;
 *         &lt;element name="OtherInfo" type="{urn:oecd:ties:cbc:v1}StringMaxLengthForLongText_Type"/&gt;
 *         &lt;element name="ResCountryCode" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="SummaryRef" type="{urn:oecd:ties:cbc:v1}CbcSummaryListElementsType_EnumType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrectableAdditionalInfo_Type", propOrder = {
    "docSpec",
    "otherInfo",
    "resCountryCode",
    "summaryRef"
})
public class CorrectableAdditionalInfoType {

    @XmlElement(name = "DocSpec", required = true)
    protected DocSpecType docSpec;
    @XmlElement(name = "OtherInfo", required = true)
    protected String otherInfo;
    @XmlElement(name = "ResCountryCode")
    protected List<Object> resCountryCode;
    @XmlElement(name = "SummaryRef")
    @XmlSchemaType(name = "string")
    protected List<CbcSummaryListElementsTypeEnumType> summaryRef;

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
     * Gets the value of the otherInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherInfo() {
        return otherInfo;
    }

    /**
     * Sets the value of the otherInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherInfo(String value) {
        this.otherInfo = value;
    }

    /**
     * Gets the value of the resCountryCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resCountryCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResCountryCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getResCountryCode() {
        if (resCountryCode == null) {
            resCountryCode = new ArrayList<Object>();
        }
        return this.resCountryCode;
    }

    /**
     * Gets the value of the summaryRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the summaryRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSummaryRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CbcSummaryListElementsTypeEnumType }
     * 
     * 
     */
    public List<CbcSummaryListElementsTypeEnumType> getSummaryRef() {
        if (summaryRef == null) {
            summaryRef = new ArrayList<CbcSummaryListElementsTypeEnumType>();
        }
        return this.summaryRef;
    }

}
