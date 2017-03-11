//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:05:54 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.crs;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageSpec" type="{urn:oecd:ties:crs:v1}MessageSpec_Type"/&gt;
 *         &lt;element name="CrsBody" type="{urn:oecd:ties:crs:v1}CrsBody_Type" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
    "crsBody"
})
@XmlRootElement(name = "CRS_OECD")
public class CRSOECD {

    @XmlElement(name = "MessageSpec", required = true)
    protected MessageSpecType messageSpec;
    @XmlElement(name = "CrsBody", required = true)
    protected List<CrsBodyType> crsBody;
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
     * Gets the value of the crsBody property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the crsBody property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCrsBody().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CrsBodyType }
     * 
     * 
     */
    public List<CrsBodyType> getCrsBody() {
        if (crsBody == null) {
            crsBody = new ArrayList<CrsBodyType>();
        }
        return this.crsBody;
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
        return version;
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