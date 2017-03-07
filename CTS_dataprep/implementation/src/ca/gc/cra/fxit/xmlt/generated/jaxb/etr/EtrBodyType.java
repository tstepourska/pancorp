//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EtrBody_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EtrBody_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TaxPayer" type="{urn:oecd:ties:etr:v1}CorrectableTaxPayer_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="RulingReports" type="{urn:oecd:ties:etr:v1}CorrectableRulingReport_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EtrBody_Type", propOrder = {
    "taxPayer",
    "rulingReports"
})
public class EtrBodyType {

    @XmlElement(name = "TaxPayer")
    protected List<CorrectableTaxPayerType> taxPayer;
    @XmlElement(name = "RulingReports")
    protected List<CorrectableRulingReportType> rulingReports;

    /**
     * Gets the value of the taxPayer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxPayer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxPayer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrectableTaxPayerType }
     * 
     * 
     */
    public List<CorrectableTaxPayerType> getTaxPayer() {
        if (taxPayer == null) {
            taxPayer = new ArrayList<CorrectableTaxPayerType>();
        }
        return this.taxPayer;
    }

    /**
     * Gets the value of the rulingReports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rulingReports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRulingReports().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrectableRulingReportType }
     * 
     * 
     */
    public List<CorrectableRulingReportType> getRulingReports() {
        if (rulingReports == null) {
            rulingReports = new ArrayList<CorrectableRulingReportType>();
        }
        return this.rulingReports;
    }

}
