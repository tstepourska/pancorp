package ca.gc.cra.fxit.xmlt.model.wrapper.crs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.cob2java.crs.IP6PRTCP;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.AddressFixType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.AddressType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.ControllingPersonType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CountryCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CrsCtrlgPersonTypeEnumType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.NamePersonType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.ObjectFactory;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.PersonPartyType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.TINType;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class PersonWrapper extends IP6PRTCP {
	private static Logger lg = Logger.getLogger(PersonWrapper.class);
	
	public PersonWrapper(String line) throws Exception {
		super();
		if(line.length() != this.length()){
			//lg.info("line: " + line);
			//lg.error("Header record line length is not correct: " + line.length() + "!=" + this.length());
			//TODO throw exception?
		}
		this.setRec(line);
	}
	   
    /**
     * 
     * @param personRec
     * @return
     * @throws Exception
     */
   // public PersonPartyType createPersonPartyFromIP6PRTCP(IP6PRTCP personRec) throws Exception {
	/*public PersonPartyType createPersonParty(ObjectFactory factory) throws Exception {
    	PersonPartyType person = new PersonPartyType();

    	List<CountryCodeType> resCountryCode = createPersonResCountryCodeList();
		List<TINType> tin 					= createPersonTINList(factory);
		List<NamePersonType> name = createNamePersonList();
		List<AddressType> address = createAddressList(factory);
		PersonPartyType.BirthInfo birthInfo = WrapperUtils.createBirthInfoFromStr(this.getOaCpBrthYr(),this.getOaCpBrthMo(), this.getOaCpBrthDy());

		//TODO
		person.getResCountryCode().addAll(resCountryCode);
    	person.getTIN().addAll(tin);
    	person.getName().addAll(name);
    	person.getAddress().addAll(address);
    	person.setBirthInfo(birthInfo);
    	
    	return person;
    }  */
	
	public ControllingPersonType createControllingPersonParty(ObjectFactory factory) throws Exception {
		ControllingPersonType person = factory.createControllingPersonType();//  new ControllingPersonType();

    	List<CountryCodeType> resCountryCode = createPersonResCountryCodeList();
		List<TINType> tin 					= createPersonTINList(factory);
		List<NamePersonType> name = createNamePersonList();
		List<AddressType> address = createAddressList(factory);
		
		//TODO nationality 0...oo
		
		PersonPartyType.BirthInfo birthInfo = WrapperUtils.createBirthInfoFromStr(this.getOaCpBrthYr(),this.getOaCpBrthMo(), this.getOaCpBrthDy());
		
		
		
		//TODO
		if(resCountryCode!=null && !resCountryCode.isEmpty())
			person.getIndividual().getResCountryCode().addAll(resCountryCode);
		
		if(tin!=null&& !tin.isEmpty())
			person.getIndividual().getTIN().addAll(tin);
		
		if(name!=null && !name.isEmpty())
			person.getIndividual().getName().addAll(name);
		
		if(address!=null && !address.isEmpty())
			person.getIndividual().getAddress().addAll(address);
		
		if(birthInfo!=null)
			person.getIndividual().setBirthInfo(birthInfo);
		
		//TODO controlling person type    1 only
		//person.setCtrlgPersonType(CrsCtrlgPersonTypeEnumType.fromValue(this.))
    	
    	return person;
    }  
	
   // private List<CountryCodeType> createPersonResCountryCodeListFromIP6PRTCP (IP6PRTCP personRec) {
	private List<CountryCodeType> createPersonResCountryCodeList() throws Exception {	
    	List<CountryCodeType> countryCodeList = null;
    	
    	for (int i = 0; i < Constants.MAX_RESIDENCE_COUNTRY_CODES; i++) {
	    	//CountryCodeType countryCode = createCountryCode(input, beginIndex);
    		CountryCodeType countryCode = CountryCodeType.fromValue(this.getOaCpRsdCntryCd(i));
			if (countryCode != null) {
				//countryCodeList = new ArrayList<CountryCodeType>();
				if (countryCodeList == null) {
					countryCodeList = new ArrayList<CountryCodeType>();
				}
				countryCodeList.add(countryCode);
			}
    	}
    	
		return countryCodeList;
    }
  
    
   // private List<TINType> createPersonTINListFromIP6PRTCP (IP6PRTCP personRec) {
	 private List<TINType> createPersonTINList (ObjectFactory factory) throws Exception {
    	List<TINType> tinList = null;
    	
    	TINType tin = createPersonTIN(factory);
		if (tin != null) {
			tinList = new ArrayList<TINType>();
			tinList.add(tin);
		}
		return tinList;
    }
    
   // private TINType createPersonTINFromIP6PRTCP (IP6PRTCP personRec) {
	 private TINType createPersonTIN(ObjectFactory factory) throws Exception {
		TINType tin = null;

		// read TIN element value from input
		String tinStr = this.getOaCpFgnTin().trim();

		CountryCodeType issuedBy = CountryCodeType.fromValue(this.getOaCpFgnTinCntryCd());

		// create TIN, and set content of TIN
		if (!tinStr.isEmpty()) {
			tin = factory.createTINType(); //new TINType();
			tin.setValue(tinStr);
			if (issuedBy != null) {
				tin.setIssuedBy(issuedBy);
			}
		}
		return tin;
    }

    //private List<NamePersonType> createNamePersonListFromIP6PRTCP (IP6PRTCP personRec) {
	 private List<NamePersonType> createNamePersonList () throws Exception {
    	List<NamePersonType> nameList = null;
    	
    	NamePersonType name = WrapperUtils.createNamePersonFromStr(this.getOaCpGvnNm(),this.getOaCpMidNm(),this.getOaCpSnm());
		if (name != null) {
			nameList = new ArrayList<NamePersonType>();
			nameList.add(name);
		}
		return nameList;
    }
 
	 private List<AddressType> createAddressList (ObjectFactory factory) throws Exception {
    	List<AddressType> addressList = null;
    	
		AddressType address = createAddress(factory);
		if (address != null) {
			addressList = new ArrayList<AddressType>();
			addressList.add(address);
		}
		return addressList;
    }

	 private AddressType createAddress(ObjectFactory factory) throws Exception {
		AddressType address = factory.createAddressType(); //new AddressType();
		//ObjectFactory factory = new ObjectFactory(); 

		// read address content from input 
		CountryCodeType countryCode = CountryCodeType.fromValue(this.getOaCpCntryCd());
		String addressFree = WrapperUtils.createAddressFreeFromStr(this.getOaCpAddrL1Txt(), this.getOaCpAddrL2Txt());
		AddressFixType addressFix = WrapperUtils.createAddressFixFromStr(this.getOaCpPstlZipCd(), this.getOaCpCtyNm(),this.getOaCpPvstNm());

		// set address content
		if (countryCode != null) {
			//JAXBElement<CountryCodeType> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
			JAXBElement<CountryCodeType> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
			address.getContent().add(jaxbCountryCode);
		}
		if (addressFix != null) {
			JAXBElement<AddressFixType> jaxbAddressFix = factory.createAddressTypeAddressFix(addressFix);
			address.getContent().add(jaxbAddressFix);
		}
		if (addressFree != null) {
			JAXBElement<String> jaxbAddressFree = factory.createAddressTypeAddressFree(addressFree);
			address.getContent().add(jaxbAddressFree);
		}

		//factory = null;
		
		return address;
    }   
}