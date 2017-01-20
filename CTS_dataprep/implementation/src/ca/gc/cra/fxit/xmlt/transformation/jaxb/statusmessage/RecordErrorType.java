//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.12 at 03:19:34 PM EST 
//


package ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecordError_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecordError_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{urn:oecd:ties:csm:v1}StringMax200Type"/>
 *         &lt;element name="Details" type="{urn:oecd:ties:csm:v1}ErrorDetail_Type" minOccurs="0"/>
 *         &lt;element name="DocRefIDInError" type="{urn:oecd:ties:csm:v1}StringMax200Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FieldsInError" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="FieldPath" type="{urn:oecd:ties:csm:v1}StringMax400Type"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecordError_Type", propOrder = {
    "code",
    "details",
    "docRefIDInError",
    "fieldsInError"
})
public class RecordErrorType {

    @XmlElement(name = "Code", required = true)
    protected String code;
    @XmlElement(name = "Details")
    protected ErrorDetailType details;
    @XmlElement(name = "DocRefIDInError")
    protected List<String> docRefIDInError;
    @XmlElement(name = "FieldsInError")
    protected List<RecordErrorType.FieldsInError> fieldsInError;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the details property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorDetailType }
     *     
     */
    public ErrorDetailType getDetails() {
        return details;
    }

    /**
     * Sets the value of the details property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorDetailType }
     *     
     */
    public void setDetails(ErrorDetailType value) {
        this.details = value;
    }

    /**
     * Gets the value of the docRefIDInError property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docRefIDInError property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocRefIDInError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDocRefIDInError() {
        if (docRefIDInError == null) {
            docRefIDInError = new ArrayList<String>();
        }
        return this.docRefIDInError;
    }

    /**
     * Gets the value of the fieldsInError property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldsInError property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldsInError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecordErrorType.FieldsInError }
     * 
     * 
     */
    public List<RecordErrorType.FieldsInError> getFieldsInError() {
        if (fieldsInError == null) {
            fieldsInError = new ArrayList<RecordErrorType.FieldsInError>();
        }
        return this.fieldsInError;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="FieldPath" type="{urn:oecd:ties:csm:v1}StringMax400Type"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "fieldPath"
    })
    public static class FieldsInError {

        @XmlElement(name = "FieldPath", required = true)
        protected String fieldPath;

        /**
         * Gets the value of the fieldPath property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFieldPath() {
            return fieldPath;
        }

        /**
         * Sets the value of the fieldPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFieldPath(String value) {
            this.fieldPath = value;
        }

    }

}
