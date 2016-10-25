//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.10 at 03:45:42 PM EDT 
//


package ca.gc.cra.fxit.ctsagent.generated.jaxb.ftc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FatcaPaymentType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FatcaPaymentType_EnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FATCA501"/&gt;
 *     &lt;enumeration value="FATCA502"/&gt;
 *     &lt;enumeration value="FATCA503"/&gt;
 *     &lt;enumeration value="FATCA504"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FatcaPaymentType_EnumType", namespace = "urn:oecd:ties:fatca:v1")
@XmlEnum
public enum FatcaPaymentTypeEnumType {


    /**
     * Dividends
     * 
     */
    @XmlEnumValue("FATCA501")
    FATCA_501("FATCA501"),

    /**
     * Interest
     * 
     */
    @XmlEnumValue("FATCA502")
    FATCA_502("FATCA502"),

    /**
     * Gross Proceeds/Redemptions
     * 
     */
    @XmlEnumValue("FATCA503")
    FATCA_503("FATCA503"),

    /**
     * Other - FATCA
     * 
     */
    @XmlEnumValue("FATCA504")
    FATCA_504("FATCA504");
    private final String value;

    FatcaPaymentTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FatcaPaymentTypeEnumType fromValue(String v) {
        for (FatcaPaymentTypeEnumType c: FatcaPaymentTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
