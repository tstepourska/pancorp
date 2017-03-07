//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Name of organisation
 * 
 * <p>Java class for EtrNameOrganisation_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EtrNameOrganisation_Type"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="nameType" type="{urn:oecd:ties:etr:v1}EtrOrganisationNameType_EnumType" /&gt;
 *       &lt;attribute name="language" type="{urn:oecd:ties:isoetrtypes:v1}LanguageCode_Type" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EtrNameOrganisation_Type", propOrder = {
    "value"
})
public class EtrNameOrganisationType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "nameType")
    protected EtrOrganisationNameTypeEnumType nameType;
    @XmlAttribute(name = "language")
    protected LanguageCodeType language;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the nameType property.
     * 
     * @return
     *     possible object is
     *     {@link EtrOrganisationNameTypeEnumType }
     *     
     */
    public EtrOrganisationNameTypeEnumType getNameType() {
        return nameType;
    }

    /**
     * Sets the value of the nameType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EtrOrganisationNameTypeEnumType }
     *     
     */
    public void setNameType(EtrOrganisationNameTypeEnumType value) {
        this.nameType = value;
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

}
