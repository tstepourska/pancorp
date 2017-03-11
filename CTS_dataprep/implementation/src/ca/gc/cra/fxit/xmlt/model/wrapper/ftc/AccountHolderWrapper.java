package ca.gc.cra.fxit.xmlt.model.wrapper.ftc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.cob2java.crs.IP6PRTAC;
import ca.gc.cra.fxit.xmlt.generated.jaxb.ftc.*;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class AccountHolderWrapper extends IP6PRTAC {
	private static Logger lg = Logger.getLogger(AccountHolderWrapper.class);
	
	public AccountHolderWrapper(String line) throws Exception {
		super();
		if(line.length() != this.length()){
			lg.info("line: " + line);
			lg.error("Header record line length is not correct: " + line.length() + "!=" + this.length());
			//TODO throw exception?
		}
		this.setRec(line);
	}
	
    //public List<AddressType> createAddressListFromIP6PRTAC (IP6PRTAC accountHolderRec) {
    public List<AddressType> createAddressList() throws Exception{
    	List<AddressType> addressList = null;
    	
		AddressType address = createAddress();
		if (address != null) {
			addressList = new ArrayList<AddressType>();
			addressList.add(address);
		}
		return addressList;
    }
    
   // private AddressType createAddressFromIP6PRTAC (IP6PRTAC accountHolderRec) {
    private AddressType createAddress() throws Exception {
		AddressType address = new AddressType();
		ObjectFactory factory = new ObjectFactory(); 

		try {
		// read address content from input 
		CountryCodeType countryCode = CountryCodeType.fromValue(this.getFiAhCntryCd());
		String addressFree = WrapperUtils.createAddressFreeFromStr(this.getFiAhAddrL1Txt(), this.getFiAhAddrL2Txt());
		//AddressFixType addressFix = WrapperUtils.createAddressFixFromStr(this.getFiAhPstlZipCd(), this.getFiAhCtyNm(), this.getFiAhPvstNm());

		// set address content
		if (countryCode != null) {
			//JAXBElement<CountryCodeType> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
			//JAXBElement<CountryCodeType> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
			//address.getContent().add(jaxbCountryCode);
		}
		//if (addressFix != null) {
		//	JAXBElement<AddressFixType> jaxbAddressFix = factory.createAddressTypeAddressFix(addressFix);
		//	address.getContent().add(jaxbAddressFix);
		//}
		if (addressFree != null) {
			JAXBElement<String> jaxbAddressFree = factory.createAddressTypeAddressFree(addressFree);
			address.getContent().add(jaxbAddressFree);
		}
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}

		factory = null;
		
		return address;
    } 
    
   // public List<PaymentType> createPaymentListFromIP6PRTAC (IP6PRTAC accountHolderRec, CurrCodeType currCode) throws Exception {
    public List<PaymentType> createPaymentList(CurrCodeType currCode) throws Exception {
		
		List<PaymentType> paymentList = null;

		try {
    	/*
for (CrsPaymentTypeEnumType paymentType : CrsPaymentTypeEnumType.values()) {
    		String amount = null;
    		switch (paymentType) {
    		case CRS_501 : 
    			amount = this.getFiAhDvamt();
    			break;
			case CRS_502 : 
				amount = this.getFiAhIntamt();
				break;
    		case CRS_503 : 
    			amount = this.getFiAhGpramt();
    			break;
			case CRS_504 : 
				amount = this.getFiAhOamt();
				break;
			default : break;
			}
    		
        	PaymentType payment = WrapperUtils.createPaymentFromStr(amount, paymentType, currCode);
        	if (payment != null) {
	    		if (paymentList == null) {
	    			paymentList = new ArrayList<PaymentType>();
	    		}
	    		paymentList.add(payment);
    		}
    		*/
  		//}
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}

    	return paymentList;
    }
    
    /**
     * NEW!
     */
    public PersonPartyType createPersonPartyType(){
    	PersonPartyType person = new PersonPartyType();
    	
    	return person;
    }
}
