//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:39:48 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageSpec" type="{urn:oecd:ties:csm:v1}MessageSpec_Type"/&gt;
 *         &lt;element name="CrsStatusMessage" type="{urn:oecd:ties:csm:v1}CrsMessageStatus_Type"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.0" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "messageSpec",
    "crsStatusMessage"
})
@XmlRootElement(name = "CRSStatusMessage_OECD")
public class CRSStatusMessageOECD {

    @XmlElement(name = "MessageSpec", required = true)
    protected MessageSpecType messageSpec;
    @XmlElement(name = "CrsStatusMessage", required = true)
    protected CrsMessageStatusType crsStatusMessage;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Gets the value of the messageSpec property.
     * 
     * @return
     *     possible object is
     *     {@link MessageSpecType }
     *     
     */
    public MessageSpecType getMessageSpec() {
        return messageSpec;
    }

    /**
     * Sets the value of the messageSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageSpecType }
     *     
     */
    public void setMessageSpec(MessageSpecType value) {
        this.messageSpec = value;
    }

    /**
     * Gets the value of the crsStatusMessage property.
     * 
     * @return
     *     possible object is
     *     {@link CrsMessageStatusType }
     *     
     */
    public CrsMessageStatusType getCrsStatusMessage() {
        return crsStatusMessage;
    }

    /**
     * Sets the value of the crsStatusMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrsMessageStatusType }
     *     
     */
    public void setCrsStatusMessage(CrsMessageStatusType value) {
        this.crsStatusMessage = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
