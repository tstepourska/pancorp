//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.10 at 03:45:42 PM EDT 
//


package ca.gc.cra.fxit.ctsagent.generated.jaxb.ftc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CorrectableOrganisationParty_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CorrectableOrganisationParty_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oecd:ties:stffatcatypes:v1}OrganisationParty_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocSpec" type="{urn:oecd:ties:fatca:v1}DocSpec_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrectableOrganisationParty_Type", namespace = "urn:oecd:ties:fatca:v1", propOrder = {
    "docSpec"
})
public class CorrectableOrganisationPartyType
    extends OrganisationPartyType
{

    @XmlElement(name = "DocSpec", required = true)
    protected DocSpecType docSpec;

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
