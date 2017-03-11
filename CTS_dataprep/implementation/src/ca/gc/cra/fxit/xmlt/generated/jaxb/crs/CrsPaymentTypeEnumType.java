//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:05:54 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.crs;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrsPaymentType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CrsPaymentType_EnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CRS501"/&gt;
 *     &lt;enumeration value="CRS502"/&gt;
 *     &lt;enumeration value="CRS503"/&gt;
 *     &lt;enumeration value="CRS504"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CrsPaymentType_EnumType")
@XmlEnum
public enum CrsPaymentTypeEnumType {


    /**
     * Dividends
     * 
     */
    @XmlEnumValue("CRS501")
    CRS_501("CRS501"),

    /**
     * Interest
     * 
     */
    @XmlEnumValue("CRS502")
    CRS_502("CRS502"),

    /**
     * Gross Proceeds/Redemptions
     * 
     */
    @XmlEnumValue("CRS503")
    CRS_503("CRS503"),

    /**
     * Other - CRS
     * 
     */
    @XmlEnumValue("CRS504")
    CRS_504("CRS504");
    private final String value;

    CrsPaymentTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CrsPaymentTypeEnumType fromValue(String v) {
        for (CrsPaymentTypeEnumType c: CrsPaymentTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
