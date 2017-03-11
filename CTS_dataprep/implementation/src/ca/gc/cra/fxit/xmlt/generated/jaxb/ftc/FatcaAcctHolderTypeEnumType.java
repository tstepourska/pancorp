//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:22:58 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.ftc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FatcaAcctHolderType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FatcaAcctHolderType_EnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="FATCA101"/&gt;
 *     &lt;enumeration value="FATCA102"/&gt;
 *     &lt;enumeration value="FATCA103"/&gt;
 *     &lt;enumeration value="FATCA104"/&gt;
 *     &lt;enumeration value="FATCA105"/&gt;
 *     &lt;enumeration value="FATCA106"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FatcaAcctHolderType_EnumType")
@XmlEnum
public enum FatcaAcctHolderTypeEnumType {


    /**
     * Owner-Documented FFI with specified U.S. owner(s)
     * 
     */
    @XmlEnumValue("FATCA101")
    FATCA_101("FATCA101"),

    /**
     * Passive NFFE with substantial U.S. owner(s)
     * 
     */
    @XmlEnumValue("FATCA102")
    FATCA_102("FATCA102"),

    /**
     * Non-Participating FFI
     * 
     */
    @XmlEnumValue("FATCA103")
    FATCA_103("FATCA103"),

    /**
     * Specified U.S. Person
     * 
     */
    @XmlEnumValue("FATCA104")
    FATCA_104("FATCA104"),

    /**
     * Direct Reporting NFFE
     * 
     */
    @XmlEnumValue("FATCA105")
    FATCA_105("FATCA105"),

    /**
     * For U.S. Government Use Only (Attention to FIs and HCTAs: Restricted, Do Not Use)
     * 
     */
    @XmlEnumValue("FATCA106")
    FATCA_106("FATCA106");
    private final String value;

    FatcaAcctHolderTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FatcaAcctHolderTypeEnumType fromValue(String v) {
        for (FatcaAcctHolderTypeEnumType c: FatcaAcctHolderTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}