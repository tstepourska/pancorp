//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:39:48 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageType_EnumType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageType_EnumType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CRSMessageStatus"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MessageType_EnumType")
@XmlEnum
public enum MessageTypeEnumType {

    @XmlEnumValue("CRSMessageStatus")
    CRS_MESSAGE_STATUS("CRSMessageStatus");
    private final String value;

    MessageTypeEnumType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageTypeEnumType fromValue(String v) {
        for (MessageTypeEnumType c: MessageTypeEnumType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}