//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Document specification: Data identifying and describing the document, where
 * 'document' here means the part of a message that is to transmit the data about a single block of ETR information. 
 * 
 * <p>Java class for DocSpec_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocSpec_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocTypeIndic" type="{urn:oecd:ties:stf:v4}OECDDocTypeIndic_EnumType"/&gt;
 *         &lt;element name="DocRefId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CorrMessageRefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CorrDocRefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocSpec_Type", namespace = "urn:oecd:ties:stf:v4", propOrder = {
    "docTypeIndic",
    "docRefId",
    "corrMessageRefId",
    "corrDocRefId"
})
public class DocSpecType {

    @XmlElement(name = "DocTypeIndic", required = true)
    @XmlSchemaType(name = "string")
    protected OECDDocTypeIndicEnumType docTypeIndic;
    @XmlElement(name = "DocRefId", required = true)
    protected String docRefId;
    @XmlElement(name = "CorrMessageRefId")
    protected String corrMessageRefId;
    @XmlElement(name = "CorrDocRefId")
    protected String corrDocRefId;

    /**
     * Gets the value of the docTypeIndic property.
     * 
     * @return
     *     possible object is
     *     {@link OECDDocTypeIndicEnumType }
     *     
     */
    public OECDDocTypeIndicEnumType getDocTypeIndic() {
        return docTypeIndic;
    }

    /**
     * Sets the value of the docTypeIndic property.
     * 
     * @param value
     *     allowed object is
     *     {@link OECDDocTypeIndicEnumType }
     *     
     */
    public void setDocTypeIndic(OECDDocTypeIndicEnumType value) {
        this.docTypeIndic = value;
    }

    /**
     * Gets the value of the docRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocRefId() {
        return docRefId;
    }

    /**
     * Sets the value of the docRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocRefId(String value) {
        this.docRefId = value;
    }

    /**
     * Gets the value of the corrMessageRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrMessageRefId() {
        return corrMessageRefId;
    }

    /**
     * Sets the value of the corrMessageRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrMessageRefId(String value) {
        this.corrMessageRefId = value;
    }

    /**
     * Gets the value of the corrDocRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrDocRefId() {
        return corrDocRefId;
    }

    /**
     * Sets the value of the corrDocRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrDocRefId(String value) {
        this.corrDocRefId = value;
    }

}