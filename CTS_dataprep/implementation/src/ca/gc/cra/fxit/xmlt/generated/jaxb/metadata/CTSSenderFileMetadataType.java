//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:25:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;Component xmlns="urn:oecd:ctssenderfilemetadata" xmlns:iso="urn:oecd:ties:isoctstypes:v1" xmlns:xmime="http://www.w3.org/2005/05/xmlmime" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;
 * 					&lt;DictionaryEntryNm&gt;CTS Sender File Metadata Type&lt;/DictionaryEntryNm&gt;
 * 					&lt;MajorVersionNum&gt;1&lt;/MajorVersionNum&gt;
 * 					&lt;MinorVersionNum&gt;0&lt;/MinorVersionNum&gt;
 * 					&lt;VersionEffectiveBeginDt&gt;2016-09-01&lt;/VersionEffectiveBeginDt&gt;
 * 					&lt;VersionDescriptionTxt&gt;Initial Version&lt;/VersionDescriptionTxt&gt;
 * 					&lt;Description&gt;Type for a group that defines the information contained in the CTS Sender File Metadata&lt;/Description&gt;
 * 				&lt;/Component&gt;
 * </pre>
 * 
 * 			
 * 
 * <p>Java class for CTSSenderFileMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CTSSenderFileMetadataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}CTSSenderCountryCd"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}CTSReceiverCountryCd"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}CTSCommunicationTypeCd"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}SenderFileId" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}FileFormatCd" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}BinaryEncodingSchemeCd" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}FileCreateTs" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}TaxYear" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}FileRevisionInd" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}OriginalCTSTransmissionId" minOccurs="0"/&gt;
 *         &lt;element ref="{urn:oecd:ctssenderfilemetadata}SenderContactEmailAddressTxt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CTSSenderFileMetadataType", propOrder = {
    "ctsSenderCountryCd",
    "ctsReceiverCountryCd",
    "ctsCommunicationTypeCd",
    "senderFileId",
    "fileFormatCd",
    "binaryEncodingSchemeCd",
    "fileCreateTs",
    "taxYear",
    "fileRevisionInd",
    "originalCTSTransmissionId",
    "senderContactEmailAddressTxt"
})
public class CTSSenderFileMetadataType {

    @XmlElement(name = "CTSSenderCountryCd", required = true)
    @XmlSchemaType(name = "string")
    protected CountryCodeType ctsSenderCountryCd;
    @XmlElement(name = "CTSReceiverCountryCd", required = true)
    @XmlSchemaType(name = "string")
    protected CountryCodeType ctsReceiverCountryCd;
    @XmlElement(name = "CTSCommunicationTypeCd", required = true)
    @XmlSchemaType(name = "string")
    protected CTSCommunicationTypeCdType ctsCommunicationTypeCd;
    @XmlElement(name = "SenderFileId")
    protected String senderFileId;
    @XmlElement(name = "FileFormatCd")
    @XmlSchemaType(name = "string")
    protected FileFormatCdType fileFormatCd;
    @XmlElement(name = "BinaryEncodingSchemeCd")
    @XmlSchemaType(name = "string")
    protected BinaryEncodingSchemeCdType binaryEncodingSchemeCd;
    @XmlElement(name = "FileCreateTs")
    protected String fileCreateTs;
    @XmlElement(name = "TaxYear")
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar taxYear;
    @XmlElement(name = "FileRevisionInd")
    protected Boolean fileRevisionInd;
    @XmlElement(name = "OriginalCTSTransmissionId")
    protected String originalCTSTransmissionId;
    @XmlElement(name = "SenderContactEmailAddressTxt")
    protected String senderContactEmailAddressTxt;

    /**
     * Gets the value of the ctsSenderCountryCd property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeType }
     *     
     */
    public CountryCodeType getCTSSenderCountryCd() {
        return ctsSenderCountryCd;
    }

    /**
     * Sets the value of the ctsSenderCountryCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeType }
     *     
     */
    public void setCTSSenderCountryCd(CountryCodeType value) {
        this.ctsSenderCountryCd = value;
    }

    /**
     * Gets the value of the ctsReceiverCountryCd property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeType }
     *     
     */
    public CountryCodeType getCTSReceiverCountryCd() {
        return ctsReceiverCountryCd;
    }

    /**
     * Sets the value of the ctsReceiverCountryCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeType }
     *     
     */
    public void setCTSReceiverCountryCd(CountryCodeType value) {
        this.ctsReceiverCountryCd = value;
    }

    /**
     * Gets the value of the ctsCommunicationTypeCd property.
     * 
     * @return
     *     possible object is
     *     {@link CTSCommunicationTypeCdType }
     *     
     */
    public CTSCommunicationTypeCdType getCTSCommunicationTypeCd() {
        return ctsCommunicationTypeCd;
    }

    /**
     * Sets the value of the ctsCommunicationTypeCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTSCommunicationTypeCdType }
     *     
     */
    public void setCTSCommunicationTypeCd(CTSCommunicationTypeCdType value) {
        this.ctsCommunicationTypeCd = value;
    }

    /**
     * Gets the value of the senderFileId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderFileId() {
        return senderFileId;
    }

    /**
     * Sets the value of the senderFileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderFileId(String value) {
        this.senderFileId = value;
    }

    /**
     * Gets the value of the fileFormatCd property.
     * 
     * @return
     *     possible object is
     *     {@link FileFormatCdType }
     *     
     */
    public FileFormatCdType getFileFormatCd() {
        return fileFormatCd;
    }

    /**
     * Sets the value of the fileFormatCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileFormatCdType }
     *     
     */
    public void setFileFormatCd(FileFormatCdType value) {
        this.fileFormatCd = value;
    }

    /**
     * Gets the value of the binaryEncodingSchemeCd property.
     * 
     * @return
     *     possible object is
     *     {@link BinaryEncodingSchemeCdType }
     *     
     */
    public BinaryEncodingSchemeCdType getBinaryEncodingSchemeCd() {
        return binaryEncodingSchemeCd;
    }

    /**
     * Sets the value of the binaryEncodingSchemeCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryEncodingSchemeCdType }
     *     
     */
    public void setBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType value) {
        this.binaryEncodingSchemeCd = value;
    }

    /**
     * Gets the value of the fileCreateTs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileCreateTs() {
        return fileCreateTs;
    }

    /**
     * Sets the value of the fileCreateTs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileCreateTs(String value) {
        this.fileCreateTs = value;
    }

    /**
     * Gets the value of the taxYear property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTaxYear() {
        return taxYear;
    }

    /**
     * Sets the value of the taxYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTaxYear(XMLGregorianCalendar value) {
        this.taxYear = value;
    }

    /**
     * Gets the value of the fileRevisionInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFileRevisionInd() {
        return fileRevisionInd;
    }

    /**
     * Sets the value of the fileRevisionInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFileRevisionInd(Boolean value) {
        this.fileRevisionInd = value;
    }

    /**
     * Gets the value of the originalCTSTransmissionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalCTSTransmissionId() {
        return originalCTSTransmissionId;
    }

    /**
     * Sets the value of the originalCTSTransmissionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalCTSTransmissionId(String value) {
        this.originalCTSTransmissionId = value;
    }

    /**
     * Gets the value of the senderContactEmailAddressTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderContactEmailAddressTxt() {
        return senderContactEmailAddressTxt;
    }

    /**
     * Sets the value of the senderContactEmailAddressTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderContactEmailAddressTxt(String value) {
        this.senderContactEmailAddressTxt = value;
    }

}
