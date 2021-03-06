//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.27 at 11:19:17 AM EST 
//


package ca.gc.cra.fxit.xmlt.generated.jaxb.etr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ca.gc.cra.fxit.xmlt.generated.jaxb.etr package. 
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

    private final static QName _AddressTypeCountryCode_QNAME = new QName("urn:oecd:ties:etr:v1", "CountryCode");
    private final static QName _AddressTypeAddressFree_QNAME = new QName("urn:oecd:ties:etr:v1", "AddressFree");
    private final static QName _AddressTypeAddressFix_QNAME = new QName("urn:oecd:ties:etr:v1", "AddressFix");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ca.gc.cra.fxit.xmlt.generated.jaxb.etr
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RulingInfoType }
     * 
     */
    public RulingInfoType createRulingInfoType() {
        return new RulingInfoType();
    }

    /**
     * Create an instance of {@link RulingInfoType.EUInfo }
     * 
     */
    public RulingInfoType.EUInfo createRulingInfoTypeEUInfo() {
        return new RulingInfoType.EUInfo();
    }

    /**
     * Create an instance of {@link ETROECD }
     * 
     */
    public ETROECD createETROECD() {
        return new ETROECD();
    }

    /**
     * Create an instance of {@link MessageSpecType }
     * 
     */
    public MessageSpecType createMessageSpecType() {
        return new MessageSpecType();
    }

    /**
     * Create an instance of {@link EtrBodyType }
     * 
     */
    public EtrBodyType createEtrBodyType() {
        return new EtrBodyType();
    }

    /**
     * Create an instance of {@link StringMax4000WithLangType }
     * 
     */
    public StringMax4000WithLangType createStringMax4000WithLangType() {
        return new StringMax4000WithLangType();
    }

    /**
     * Create an instance of {@link EtrNameOrganisationType }
     * 
     */
    public EtrNameOrganisationType createEtrNameOrganisationType() {
        return new EtrNameOrganisationType();
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
     * Create an instance of {@link TINType }
     * 
     */
    public TINType createTINType() {
        return new TINType();
    }

    /**
     * Create an instance of {@link CorrectableRulingReportType }
     * 
     */
    public CorrectableRulingReportType createCorrectableRulingReportType() {
        return new CorrectableRulingReportType();
    }

    /**
     * Create an instance of {@link OrganisationINType }
     * 
     */
    public OrganisationINType createOrganisationINType() {
        return new OrganisationINType();
    }

    /**
     * Create an instance of {@link OrganisationPartyType }
     * 
     */
    public OrganisationPartyType createOrganisationPartyType() {
        return new OrganisationPartyType();
    }

    /**
     * Create an instance of {@link TaxPayerType }
     * 
     */
    public TaxPayerType createTaxPayerType() {
        return new TaxPayerType();
    }

    /**
     * Create an instance of {@link CorrectableTaxPayerType }
     * 
     */
    public CorrectableTaxPayerType createCorrectableTaxPayerType() {
        return new CorrectableTaxPayerType();
    }

    /**
     * Create an instance of {@link DocSpecType }
     * 
     */
    public DocSpecType createDocSpecType() {
        return new DocSpecType();
    }

    /**
     * Create an instance of {@link RulingInfoType.Validity }
     * 
     */
    public RulingInfoType.Validity createRulingInfoTypeValidity() {
        return new RulingInfoType.Validity();
    }

    /**
     * Create an instance of {@link RulingInfoType.RulingType }
     * 
     */
    public RulingInfoType.RulingType createRulingInfoTypeRulingType() {
        return new RulingInfoType.RulingType();
    }

    /**
     * Create an instance of {@link RulingInfoType.EUInfo.EUAPAInfo }
     * 
     */
    public RulingInfoType.EUInfo.EUAPAInfo createRulingInfoTypeEUInfoEUAPAInfo() {
        return new RulingInfoType.EUInfo.EUAPAInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountryCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:etr:v1", name = "CountryCode", scope = AddressType.class)
    public JAXBElement<CountryCodeType> createAddressTypeCountryCode(CountryCodeType value) {
        return new JAXBElement<CountryCodeType>(_AddressTypeCountryCode_QNAME, CountryCodeType.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:etr:v1", name = "AddressFree", scope = AddressType.class)
    public JAXBElement<String> createAddressTypeAddressFree(String value) {
        return new JAXBElement<String>(_AddressTypeAddressFree_QNAME, String.class, AddressType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressFixType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oecd:ties:etr:v1", name = "AddressFix", scope = AddressType.class)
    public JAXBElement<AddressFixType> createAddressTypeAddressFix(AddressFixType value) {
        return new JAXBElement<AddressFixType>(_AddressTypeAddressFix_QNAME, AddressFixType.class, AddressType.class, value);
    }

}
