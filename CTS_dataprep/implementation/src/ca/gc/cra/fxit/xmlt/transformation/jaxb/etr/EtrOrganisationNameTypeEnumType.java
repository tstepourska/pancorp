//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b63 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.29 at 10:30:00 AM EST 
//


package ca.gc.cra.fxit.xmlt.transformation.jaxb.etr;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EtrOrganisationNameType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EtrOrganisationNameType_EnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ETR901"/>
 *     &lt;enumeration value="ETR902"/>
 *     &lt;enumeration value="ETR903"/>
 *     &lt;enumeration value="ETR904"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EtrOrganisationNameType_EnumType")
@XmlEnum
public enum EtrOrganisationNameTypeEnumType {


    /**
     * Legal
     * 
     */
    @XmlEnumValue("ETR901")
    ETR_901("ETR901"),

    /**
     * Trading
     * 
     */
    @XmlEnumValue("ETR902")
    ETR_902("ETR902"),

    /**
     * Permanent Establishment
     * 
     */
    @XmlEnumValue("ETR903")
    ETR_903("ETR903"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("ETR904")
    ETR_904("ETR904");
    private final String value;

    EtrOrganisationNameTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EtrOrganisationNameTypeEnumType fromValue(String v) {
        for (EtrOrganisationNameTypeEnumType c: EtrOrganisationNameTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
