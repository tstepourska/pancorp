//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:05:54 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.crs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrsBody_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrsBody_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ReportingFI" type="{urn:oecd:ties:crs:v1}CorrectableOrganisationParty_Type"/&gt;
 *         &lt;element name="ReportingGroup" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Sponsor" type="{urn:oecd:ties:crs:v1}CorrectableOrganisationParty_Type" minOccurs="0"/&gt;
 *                   &lt;element name="Intermediary" type="{urn:oecd:ties:crs:v1}CorrectableOrganisationParty_Type" minOccurs="0"/&gt;
 *                   &lt;element name="AccountReport" type="{urn:oecd:ties:crs:v1}CorrectableAccountReport_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="PoolReport" type="{urn:oecd:ties:fatca:v1}CorrectablePoolReport_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CrsBody_Type", propOrder = {
    "reportingFI",
    "reportingGroup"
})
public class CrsBodyType {

    @XmlElement(name = "ReportingFI", required = true)
    protected CorrectableOrganisationPartyType reportingFI;
    @XmlElement(name = "ReportingGroup", required = true)
    protected List<CrsBodyType.ReportingGroup> reportingGroup;

    /**
     * Gets the value of the reportingFI property.
     * 
     * @return
     *     possible object is
     *     {@link CorrectableOrganisationPartyType }
     *     
     */
    public CorrectableOrganisationPartyType getReportingFI() {
        return reportingFI;
    }

    /**
     * Sets the value of the reportingFI property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrectableOrganisationPartyType }
     *     
     */
    public void setReportingFI(CorrectableOrganisationPartyType value) {
        this.reportingFI = value;
    }

    /**
     * Gets the value of the reportingGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportingGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportingGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CrsBodyType.ReportingGroup }
     * 
     * 
     */
    public List<CrsBodyType.ReportingGroup> getReportingGroup() {
        if (reportingGroup == null) {
            reportingGroup = new ArrayList<CrsBodyType.ReportingGroup>();
        }
        return this.reportingGroup;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Sponsor" type="{urn:oecd:ties:crs:v1}CorrectableOrganisationParty_Type" minOccurs="0"/&gt;
     *         &lt;element name="Intermediary" type="{urn:oecd:ties:crs:v1}CorrectableOrganisationParty_Type" minOccurs="0"/&gt;
     *         &lt;element name="AccountReport" type="{urn:oecd:ties:crs:v1}CorrectableAccountReport_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="PoolReport" type="{urn:oecd:ties:fatca:v1}CorrectablePoolReport_Type" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sponsor",
        "intermediary",
        "accountReport",
        "poolReport"
    })
    public static class ReportingGroup {

        @XmlElement(name = "Sponsor")
        protected CorrectableOrganisationPartyType sponsor;
        @XmlElement(name = "Intermediary")
        protected CorrectableOrganisationPartyType intermediary;
        @XmlElement(name = "AccountReport")
        protected List<CorrectableAccountReportType> accountReport;
        @XmlElement(name = "PoolReport")
        protected List<CorrectablePoolReportType> poolReport;

        /**
         * Gets the value of the sponsor property.
         * 
         * @return
         *     possible object is
         *     {@link CorrectableOrganisationPartyType }
         *     
         */
        public CorrectableOrganisationPartyType getSponsor() {
            return sponsor;
        }

        /**
         * Sets the value of the sponsor property.
         * 
         * @param value
         *     allowed object is
         *     {@link CorrectableOrganisationPartyType }
         *     
         */
        public void setSponsor(CorrectableOrganisationPartyType value) {
            this.sponsor = value;
        }

        /**
         * Gets the value of the intermediary property.
         * 
         * @return
         *     possible object is
         *     {@link CorrectableOrganisationPartyType }
         *     
         */
        public CorrectableOrganisationPartyType getIntermediary() {
            return intermediary;
        }

        /**
         * Sets the value of the intermediary property.
         * 
         * @param value
         *     allowed object is
         *     {@link CorrectableOrganisationPartyType }
         *     
         */
        public void setIntermediary(CorrectableOrganisationPartyType value) {
            this.intermediary = value;
        }

        /**
         * Gets the value of the accountReport property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the accountReport property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAccountReport().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CorrectableAccountReportType }
         * 
         * 
         */
        public List<CorrectableAccountReportType> getAccountReport() {
            if (accountReport == null) {
                accountReport = new ArrayList<CorrectableAccountReportType>();
            }
            return this.accountReport;
        }

        /**
         * Gets the value of the poolReport property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the poolReport property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPoolReport().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CorrectablePoolReportType }
         * 
         * 
         */
        public List<CorrectablePoolReportType> getPoolReport() {
            if (poolReport == null) {
                poolReport = new ArrayList<CorrectablePoolReportType>();
            }
            return this.poolReport;
        }

    }

}