//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:39:48 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValidationErrors_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidationErrors_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FileError" type="{urn:oecd:ties:csm:v1}FileError_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="RecordError" type="{urn:oecd:ties:csm:v1}RecordError_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValidationErrors_Type", propOrder = {
    "fileError",
    "recordError"
})
public class ValidationErrorsType {

    @XmlElement(name = "FileError")
    protected List<FileErrorType> fileError;
    @XmlElement(name = "RecordError")
    protected List<RecordErrorType> recordError;

    /**
     * Gets the value of the fileError property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fileError property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFileError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileErrorType }
     * 
     * 
     */
    public List<FileErrorType> getFileError() {
        if (fileError == null) {
            fileError = new ArrayList<FileErrorType>();
        }
        return this.fileError;
    }

    /**
     * Gets the value of the recordError property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recordError property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecordError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecordErrorType }
     * 
     * 
     */
    public List<RecordErrorType> getRecordError() {
        if (recordError == null) {
            recordError = new ArrayList<RecordErrorType>();
        }
        return this.recordError;
    }

}
