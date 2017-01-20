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
 * <p>Java class for EtrRulingCategory_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EtrRulingCategory_EnumType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ETR601"/>
 *     &lt;enumeration value="ETR602"/>
 *     &lt;enumeration value="ETR603"/>
 *     &lt;enumeration value="ETR604"/>
 *     &lt;enumeration value="ETR605"/>
 *     &lt;enumeration value="ETR606"/>
 *     &lt;enumeration value="ETR607"/>
 *     &lt;enumeration value="ETR608"/>
 *     &lt;enumeration value="ETR609"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EtrRulingCategory_EnumType")
@XmlEnum
public enum EtrRulingCategoryEnumType {


    /**
     * Relating to preferential regime
     * 
     */
    @XmlEnumValue("ETR601")
    ETR_601("ETR601"),

    /**
     * Unilateral APA or other TP ruling
     * 
     */
    @XmlEnumValue("ETR602")
    ETR_602("ETR602"),

    /**
     * Bilateral or multilateral APA
     * 
     */
    @XmlEnumValue("ETR603")
    ETR_603("ETR603"),

    /**
     * Exchange of summary information on request for bilateral or multilateral APA
     * 
     */
    @XmlEnumValue("ETR604")
    ETR_604("ETR604"),

    /**
     * Downward adjustment ruling
     * 
     */
    @XmlEnumValue("ETR605")
    ETR_605("ETR605"),

    /**
     * PE ruling
     * 
     */
    @XmlEnumValue("ETR606")
    ETR_606("ETR606"),

    /**
     * Conduit ruling
     * 
     */
    @XmlEnumValue("ETR607")
    ETR_607("ETR607"),

    /**
     * Hybrid entity ruling
     * 
     */
    @XmlEnumValue("ETR608")
    ETR_608("ETR608"),

    /**
     * Other
     * 
     */
    @XmlEnumValue("ETR609")
    ETR_609("ETR609");
    private final String value;

    EtrRulingCategoryEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EtrRulingCategoryEnumType fromValue(String v) {
        for (EtrRulingCategoryEnumType c: EtrRulingCategoryEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
