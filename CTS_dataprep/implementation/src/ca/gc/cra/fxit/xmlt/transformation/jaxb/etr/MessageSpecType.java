//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b63 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.29 at 10:30:00 AM EST 
//


package ca.gc.cra.fxit.xmlt.transformation.jaxb.etr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Information in the message header identifies the Tax Administration that is sending the message.  It specifies when the message was created, what period (normally a year) the report is for, and the nature of the report (original, corrected, supplemental, etc).
 * 
 * <p>Java class for MessageSpec_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageSpec_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransmittingCountry" type="{urn:oecd:ties:isoetrtypes:v1}CountryCode_Type"/>
 *         &lt;element name="ReceivingCountry" type="{urn:oecd:ties:etr:v1}CountryCodeList"/>
 *         &lt;element name="MessageType" type="{urn:oecd:ties:etr:v1}MessageType_EnumType"/>
 *         &lt;element name="Language" type="{urn:oecd:ties:isoetrtypes:v1}LanguageCode_Type"/>
 *         &lt;element name="Warning" type="{urn:oecd:ties:etr:v1}StringMax4000_Type" minOccurs="0"/>
 *         &lt;element name="Contact" type="{urn:oecd:ties:etr:v1}StringMax4000_Type" minOccurs="0"/>
 *         &lt;element name="MessageRefId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageTypeIndic" type="{urn:oecd:ties:etr:v1}EtrMessageTypeIndic_EnumType" minOccurs="0"/>
 *         &lt;element name="CorrMessageRefId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageSpec_Type", propOrder = {
    "transmittingCountry",
    "receivingCountry",
    "messageType",
    "language",
    "warning",
    "contact",
    "messageRefId",
    "messageTypeIndic",
    "corrMessageRefId",
    "timestamp"
})
public class MessageSpecType {

    @XmlElement(name = "TransmittingCountry", required = true)
    protected CountryCodeType transmittingCountry;
    @XmlList
    @XmlElement(name = "ReceivingCountry", required = true)
    protected List<CountryCodeType> receivingCountry;
    @XmlElement(name = "MessageType", required = true)
    protected MessageTypeEnumType messageType;
    @XmlElement(name = "Language", required = true)
    protected LanguageCodeType language;
    @XmlElement(name = "Warning")
    protected String warning;
    @XmlElement(name = "Contact")
    protected String contact;
    @XmlElement(name = "MessageRefId", required = true)
    protected String messageRefId;
    @XmlElement(name = "MessageTypeIndic")
    protected EtrMessageTypeIndicEnumType messageTypeIndic;
    @XmlElement(name = "CorrMessageRefId")
    protected List<String> corrMessageRefId;
    @XmlElement(name = "Timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;

    /**
     * Gets the value of the transmittingCountry property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeType }
     *     
     */
    public CountryCodeType getTransmittingCountry() {
        return transmittingCountry;
    }

    /**
     * Sets the value of the transmittingCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeType }
     *     
     */
    public void setTransmittingCountry(CountryCodeType value) {
        this.transmittingCountry = value;
    }

    /**
     * Gets the value of the receivingCountry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receivingCountry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceivingCountry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CountryCodeType }
     * 
     * 
     */
    public List<CountryCodeType> getReceivingCountry() {
        if (receivingCountry == null) {
            receivingCountry = new ArrayList<CountryCodeType>();
        }
        return this.receivingCountry;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link MessageTypeEnumType }
     *     
     */
    public MessageTypeEnumType getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageTypeEnumType }
     *     
     */
    public void setMessageType(MessageTypeEnumType value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link LanguageCodeType }
     *     
     */
    public LanguageCodeType getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link LanguageCodeType }
     *     
     */
    public void setLanguage(LanguageCodeType value) {
        this.language = value;
    }

    /**
     * Gets the value of the warning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarning() {
        return warning;
    }

    /**
     * Sets the value of the warning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarning(String value) {
        this.warning = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContact(String value) {
        this.contact = value;
    }

    /**
     * Gets the value of the messageRefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageRefId() {
        return messageRefId;
    }

    /**
     * Sets the value of the messageRefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageRefId(String value) {
        this.messageRefId = value;
    }

    /**
     * Gets the value of the messageTypeIndic property.
     * 
     * @return
     *     possible object is
     *     {@link EtrMessageTypeIndicEnumType }
     *     
     */
    public EtrMessageTypeIndicEnumType getMessageTypeIndic() {
        return messageTypeIndic;
    }

    /**
     * Sets the value of the messageTypeIndic property.
     * 
     * @param value
     *     allowed object is
     *     {@link EtrMessageTypeIndicEnumType }
     *     
     */
    public void setMessageTypeIndic(EtrMessageTypeIndicEnumType value) {
        this.messageTypeIndic = value;
    }

    /**
     * Gets the value of the corrMessageRefId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the corrMessageRefId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrMessageRefId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCorrMessageRefId() {
        if (corrMessageRefId == null) {
            corrMessageRefId = new ArrayList<String>();
        }
        return this.corrMessageRefId;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

}
