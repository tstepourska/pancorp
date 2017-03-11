//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EtrAPAMethod_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EtrAPAMethod_EnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ETR1101"/&gt;
 *     &lt;enumeration value="ETR1102"/&gt;
 *     &lt;enumeration value="ETR1103"/&gt;
 *     &lt;enumeration value="ETR1104"/&gt;
 *     &lt;enumeration value="ETR1105"/&gt;
 *     &lt;enumeration value="ETR1106"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EtrAPAMethod_EnumType")
@XmlEnum
public enum EtrAPAMethodEnumType {


    /**
     * Comparable Uncontrolled Price Method (CUP)
     * 
     */
    @XmlEnumValue("ETR1101")
    ETR_1101("ETR1101"),

    /**
     * Resale Price Method (RSM)
     * 
     */
    @XmlEnumValue("ETR1102")
    ETR_1102("ETR1102"),

    /**
     * Cost Plus Method
     * 
     */
    @XmlEnumValue("ETR1103")
    ETR_1103("ETR1103"),

    /**
     * Transactional Net Margin Method (TNMM)
     * 
     */
    @XmlEnumValue("ETR1104")
    ETR_1104("ETR1104"),

    /**
     * Transactional Profit Split Method (PSM)
     * 
     */
    @XmlEnumValue("ETR1105")
    ETR_1105("ETR1105"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("ETR1106")
    ETR_1106("ETR1106");
    private final String value;

    EtrAPAMethodEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EtrAPAMethodEnumType fromValue(String v) {
        for (EtrAPAMethodEnumType c: EtrAPAMethodEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
