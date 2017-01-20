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
 * <p>Java class for EtrExchangeReasonType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EtrExchangeReasonType_EnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ETR701"/>
 *     &lt;enumeration value="ETR702"/>
 *     &lt;enumeration value="ETR703"/>
 *     &lt;enumeration value="ETR704"/>
 *     &lt;enumeration value="ETR705"/>
 *     &lt;enumeration value="ETR706"/>
 *     &lt;enumeration value="ETR707"/>
 *     &lt;enumeration value="ETR708"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EtrExchangeReasonType_EnumType")
@XmlEnum
public enum EtrExchangeReasonTypeEnumType {


    /**
     * Ultimate parent
     * 
     */
    @XmlEnumValue("ETR701")
    ETR_701("ETR701"),

    /**
     * Immediate parent
     * 
     */
    @XmlEnumValue("ETR702")
    ETR_702("ETR702"),

    /**
     * Related party with which the taxpayer enters into a transaction for which a preferential treatment is granted or which gives rise to income benefiting from a preferential treatment
     * 
     */
    @XmlEnumValue("ETR703")
    ETR_703("ETR703"),

    /**
     * Related party with whom the taxpayer enters into a transaction covered by the ruling
     * 
     */
    @XmlEnumValue("ETR704")
    ETR_704("ETR704"),

    /**
     * Related party making payments to a conduit (directly or indirectly)
     * 
     */
    @XmlEnumValue("ETR705")
    ETR_705("ETR705"),

    /**
     * Ultimate beneficial owner of income from a conduit arrangement
     * 
     */
    @XmlEnumValue("ETR706")
    ETR_706("ETR706"),

    /**
     * Head office of permanent establishment/PE country
     * 
     */
    @XmlEnumValue("ETR707")
    ETR_707("ETR707"),

    /**
     * Exchange with EU Member State under Directive 2011/16/EU
     * 
     */
    @XmlEnumValue("ETR708")
    ETR_708("ETR708");
    private final String value;

    EtrExchangeReasonTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EtrExchangeReasonTypeEnumType fromValue(String v) {
        for (EtrExchangeReasonTypeEnumType c: EtrExchangeReasonTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
