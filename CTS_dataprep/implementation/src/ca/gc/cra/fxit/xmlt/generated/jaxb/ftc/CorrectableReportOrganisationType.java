//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:22:58 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.ftc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Type that contains details about entity that can act as a filer of report ( e.g. Sponsor, Reporting FI or Intermediary)
 * 
 * <p>Java class for CorrectableReportOrganisation_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CorrectableReportOrganisation_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oecd:ties:stffatcatypes:v2}OrganisationParty_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FilerCategory" type="{urn:oecd:ties:fatca:v2}FatcaFilerCategory_EnumType" minOccurs="0"/&gt;
 *         &lt;element name="DocSpec" type="{urn:oecd:ties:fatca:v2}DocSpec_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrectableReportOrganisation_Type", propOrder = {
    "filerCategory",
    "docSpec"
})
public class CorrectableReportOrganisationType
    extends OrganisationPartyType
{

    @XmlElement(name = "FilerCategory")
    @XmlSchemaType(name = "string")
    protected FatcaFilerCategoryEnumType filerCategory;
    @XmlElement(name = "DocSpec", required = true)
    protected DocSpecType docSpec;

    /**
     * Gets the value of the filerCategory property.
     * 
     * @return
     *     possible object is
     *     {@link FatcaFilerCategoryEnumType }
     *     
     */
    public FatcaFilerCategoryEnumType getFilerCategory() {
        return filerCategory;
    }

    /**
     * Sets the value of the filerCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link FatcaFilerCategoryEnumType }
     *     
     */
    public void setFilerCategory(FatcaFilerCategoryEnumType value) {
        this.filerCategory = value;
    }

    /**
     * Gets the value of the docSpec property.
     * 
     * @return
     *     possible object is
     *     {@link DocSpecType }
     *     
     */
    public DocSpecType getDocSpec() {
        return docSpec;
    }

    /**
     * Sets the value of the docSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocSpecType }
     *     
     */
    public void setDocSpec(DocSpecType value) {
        this.docSpec = value;
    }

}