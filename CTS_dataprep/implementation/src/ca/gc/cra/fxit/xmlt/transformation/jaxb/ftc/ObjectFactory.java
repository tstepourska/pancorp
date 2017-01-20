//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.10 at 03:45:42 PM EDT 
//


package ca.gc.cra.fxit.xmlt.transformation.jaxb.ftc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ca.gc.cra.fxit.xmlt.task.xml.integration.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddressTypeCountryCode_QNAME = new QName("urn:oecd:ties:stffatcatypes:v1", "CountryCode");
    private final static QName _AddressTypeAddressFree_QNAME = new QName("urn:oecd:ties:stffatcatypes:v1", "AddressFree");
    private final static QName _AddressTypeAddressFix_QNAME = new QName("urn:oecd:ties:stffatcatypes:v1", "AddressFix");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ca.gc.cra.fxit.xmlt.task.xml.integration.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersonPartyType }
     * 
     */
    public PersonPartyType createPersonPartyType() {
        return new PersonPartyType();
    }

    /**
     * Create an instance of {@link PersonPartyType.BirthInfo }
     * 
     */
    public PersonPartyType.BirthInfo createPersonPartyTypeBirthInfo() {
        return new PersonPartyType.BirthInfo();
    }

    /**
     * Create an instance of {@link NamePersonType }
     * 
     */
    public NamePersonType createNamePersonType() {
        return new NamePersonType();
    }

    /**
     * Create an instance of {@link FatcaType }
     * 
     */
    public FatcaType createFatcaType() {
        return new FatcaType();
    }

    /**
     * Create an instance of {@link FATCAOECD }
     * 
     */
    public FATCAOECD createFATCAOECD() {
        return new FATCAOECD();
    }

    /**
     * Create an instance of {@link MessageSpecType }
     * 
     */
    public MessageSpecType createMessageSpecType() {
        return new MessageSpecType();
    }

    /**
     * Create an instance of {@link DocSpecType }
     * 
     */
    public DocSpecType createDocSpecType() {
        return new DocSpecType();
    }

    /**
     * Create an instance of {@link PaymentType }
     * 
     */
    public PaymentType createPaymentType() {
        return new PaymentType();
    }

    /**
     * Create an instance of {@link AccountHolderType }
     * 
     */
    public AccountHolderType createAccountHolderType() {
        return new AccountHolderType();
    }

    /**
     * Create an instance of {@link CorrectableOrganisationPartyType }
     * 
     */
    public CorrectableOrganisationPartyType createCorrectableOrganisationPartyType() {
        return new CorrectableOrganisationPartyType();
    }

    /**
     * Create an instance of {@link CorrectablePoolReportType }
     * 
     */
    public CorrectablePoolReportType createCorrectablePoolReportType() {
        return new CorrectablePoolReportType();
    }

    /**
     * Create an instance of {@link CorrectableAccountReportType }
     * 
     */
    public CorrectableAccountReportType createCorrectableAccountReportType() {
        return new CorrectableAccountReportType();
    }

    /**
     * Create an instance of {@link AddressFixType }
     * 
     */
    public AddressFixType createAddressFixType() {
        return new AddressFixType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link MonAmntType }
     * 
     */
    public MonAmntType createMonAmntType() {
        return new MonAmntType();
    }

    /**
     * Create an instance of {@link NameOrganisationType }
     * 
     */
    public NameOrganisationType createNameOrganisationType() {
        return new NameOrganisationType();
    }

    /**
     * Create an instance of {@link TINType }
     * 
     */
    public TINType createTINType() {
        return new TINType();
    }

    /**
     * Create an instance of {@link OrganisationPartyType }
     * 
     */
    public OrganisationPartyType createOrganisationPartyType() {
        return new OrganisationPartyType();
    }

    /**
     * Create an instance of {@link PersonPartyType.BirthInfo.CountryInfo }
     * 
     */
    public PersonPartyType.BirthInfo.CountryInfo createPersonPartyTypeBirthInfoCountryInfo() {
        return new PersonPartyType.BirthInfo.CountryInfo();
    }

    /**
     * Create an instance of {@link NamePersonType.FirstName }
     * 
     */
    public NamePersonType.FirstName createNamePersonTypeFirstName() {
        return new NamePersonType.FirstName();
    }

    /**
     * Create an instance of {@link NamePersonType.MiddleName }
     * 
     */
    public NamePersonType.MiddleName createNamePersonTypeMiddleName() {
        return new NamePersonType.MiddleName();
    }

    /**
     * Create an instance of {@link NamePersonType.NamePrefix }
     * 
     */
    public NamePersonType.NamePrefix createNamePersonTypeNamePrefix() {
        return new NamePersonType.NamePrefix();
    }

    /**
     * Create an instance of {@link NamePersonType.LastName }
     * 
     */
    public NamePersonType.LastName createNamePersonTypeLastName() {
        return new NamePersonType.LastName();
    }

    /**
     * Create an instance of {@link FatcaType.ReportingGroup }
     * 
     */
    public FatcaType.ReportingGroup createFatcaTypeReportingGroup() {
        return new FatcaType.ReportingGroup();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:stffatcatypes:v1", name = "CountryCode", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeCountryCode(String value) {
        return new JAXBElement<String>(_AddressTypeCountryCode_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:stffatcatypes:v1", name = "AddressFree", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeAddressFree(String value) {
        return new JAXBElement<String>(_AddressTypeAddressFree_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressFixType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:stffatcatypes:v1", name = "AddressFix", scope = AddressType.class)
    public JAXBElement<AddressFixType> createAddressTypeAddressFix(AddressFixType value) {
        return new JAXBElement<AddressFixType>(_AddressTypeAddressFix_QNAME, AddressFixType.class, AddressType.class, value);
    }

}
