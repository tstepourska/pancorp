package ca.gc.cra.fxit.xmlt.model.wrapper.ftc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.cob2java.crs.IP6PRTSM;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.*;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class FIWrapper extends IP6PRTSM {
	private static Logger lg = Logger.getLogger(FIWrapper.class);
	
	public FIWrapper(String line) throws Exception {
		if(line.length() != this.length()){
			lg.info("line: " + line);
			lg.error("Header record line length is not correct: " + line.length() + "!=" + this.length());
			//TODO throw exception?
		}
		this.setRec(line);
	}
    
	public CorrectableOrganisationPartyType createCorrectableOrganisationParty() throws Exception{
	    	String fp = "createCorrectableOrganisationParty: ";
	    	// create an empty CorrectableOrganisationPartyType object                                             
	    	CorrectableOrganisationPartyType party = new CorrectableOrganisationPartyType();

	    	try {
	    	List<CountryCodeType> resCountryCode = createCountryCodeList();
	    	if(lg.isDebugEnabled())
	    		lg.debug(fp + " resCountryCode created");
	    	//List<String> resCountryCode = createCountryCodeListFromIP6PRTSM(reportingFIRec);
			List<TINType> tin = createTINList();
			if(lg.isDebugEnabled())
	    		lg.debug(fp + " tin list created");
			List<NameOrganisationType> name = createNameOrganisationList();
			if(lg.isDebugEnabled())
	    		lg.debug(fp + " name created");
			List<AddressType> address = createAddressList();
			if(lg.isDebugEnabled())
	    		lg.debug(fp + " address created");
			DocSpecType docSpec = createDocSpec();
			if(lg.isDebugEnabled())
	    		lg.debug(fp + " docSpec created");
			
	    	if (resCountryCode != null && !resCountryCode.isEmpty()) {
	    		party.getResCountryCode().addAll(resCountryCode);
	    	}
	    	if (tin != null && !tin.isEmpty()) {
	    		//party.getTIN().addAll(tin);
	    	}
			// One or more Name is required for schema validation, and can be blank
	    	if (name != null && !name.isEmpty()) {
	    		party.getName().addAll(name);
	    	}
			// One or more Address is required for schema validation
	    	if (address != null && !address.isEmpty()) {
	    		party.getAddress().addAll(address);
	    	}
	    	if (docSpec != null) {
	    		party.setDocSpec(docSpec);
	    	}
	    	}
			catch(Exception e){
				Utils.logError(lg, e);
			}

			// return it
	    	return party;
	    }
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb
		.append("\nCorrDocRefId=" + this.getFiCorrDocRefId())
		.append("\nDocRefId=" + this.getFiDocRefId())
		.append("\nGiin=" + this.getFiGiin())
		.append("\nInfoDtyCd=" + this.getFiInfoDtyCd())
		.append("\nlrAddrFrstTxt=" + this.getFilrAddrFrstTxt())
		.append("\nlrAddrSecTxt=" + this.getFilrAddrSecTxt())
		.append("\nlrCntryCd=" + this.getFilrCntryCd())
		.append("\nlrCtyNm=" + this.getFilrCtyNm())
		.append("\nlrNmFrstTxt=" + this.getFilrNmFrstTxt())
		.append("\nlrNmSecTxt=" + this.getFilrNmSecTxt())
		.append("\nlrPstlZipCd=" + this.getFilrPstlZipCd())
		.append("\nPvstNm=" + this.getFiPvstNm())
		.append("\nRsdCntryCd=" + this.getFiRsdCntryCd())
		;
		
		return sb.toString();
	}
	    
	    private List<CountryCodeType> createCountryCodeList () throws Exception {
	    	
	    	List<CountryCodeType> countryCodeList = null;
	    	String country = this.getFiRsdCntryCd();
	    	if(lg.isDebugEnabled())
	    		lg.debug("createCountryCodeList: country: "+ country);
	    	try {
	    	CountryCodeType countryCode = CountryCodeType.fromValue(country);
			if (countryCode != null) {
				countryCodeList = new ArrayList<CountryCodeType>();
				//countryCodeList = new ArrayList<String>();
				countryCodeList.add(countryCode);
			}
	    	}
	    	catch(Exception e){
	    		Utils.logError(lg, e);
	    		
	    	}
			return countryCodeList;
	    }
	    
	    private DocSpecType createDocSpec () throws Exception {
			
			DocSpecType docSpec = new DocSpecType();
			OECDDocTypeIndicEnumType oecdDocTypeIndicEnum = null;
			String docRefIdStr 		= null;
			String corrDocRefIdStr = null;
			
			try  {
			// read docSpec content from input 
			oecdDocTypeIndicEnum = OECDDocTypeIndicEnumType.fromValue(this.getFiInfoDtyCd());//getIfaieRtnSlpDtyCd()); //createFatcaDocTypeIndicEnumFromStr(slipRec.getIfaieRtnSlpDtyCd());
			docRefIdStr 		= this.getFiDocRefId().trim();
			corrDocRefIdStr 	= this.getFiCorrDocRefId().trim();
			}
			catch(Exception  e){
				Utils.logError(lg, e);
			}

			// set docSpec content
			docSpec.setDocTypeIndic	(oecdDocTypeIndicEnum);
			docSpec.setDocRefId		(docRefIdStr);
			docSpec.setCorrDocRefId	(corrDocRefIdStr);
			
			return docSpec;
	    }  
	    
	    private List<AddressType> createAddressList() throws Exception {
	    	
	    	List<AddressType> addressList = null;
	    	
			AddressType address = createAddress();
			if (address != null) {
				addressList = new ArrayList<AddressType>();
				addressList.add(address);
			}
			return addressList;
	    }
	    
	    /**
	     * @param reportingFIRec
	     * @return
	     */    
	    private AddressType createAddress() throws Exception {
	    		
	    		AddressType address = new AddressType();
	    		ObjectFactory factory = new ObjectFactory(); 

	    		// read address content from input 
	    		//CountryCodeType countryCode = createCountryCode(input, countryCodeBeginIndex);
	    		CountryCodeType countryCode = CountryCodeType.fromValue(this.getFiRsdCntryCd());//getFilrCntryCd());
	    		String addressFree = WrapperUtils.createAddressFreeFromStr(this.getFilrAddrFrstTxt(),this.getFilrAddrSecTxt());
	    		AddressFixType addressFix = WrapperUtils.createAddressFixFromStr(this.getFilrPstlZipCd(), this.getFilrCtyNm(), this.getFiPvstNm());

	    		// set address content
	    		if (countryCode != null) {
	    			JAXBElement<CountryCodeType> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
	    			//JAXBElement<String> jaxbCountryCode = factory.createAddressTypeCountryCode(countryCode);
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

	    		factory = null;
	    		
	    		return address;
	    }    
	        
	    private List<NameOrganisationType> createNameOrganisationList () throws Exception {
	        	
	    	// One or more name is required for schema validation, and can be blank
	        List<NameOrganisationType> nameList = new ArrayList<NameOrganisationType>();
	       	NameOrganisationType name = WrapperUtils.createNameOrganisationFromStr(this.getFilrNmFrstTxt(), this.getFilrNmSecTxt());
	        if (name != null) {
	            nameList.add(name);
	        }
	        	
	    	return nameList;
	   }
	        
	   private TINType createTIN() throws Exception {
	    		
	    		TINType tin = null;

	    		// read TIN element value from input
	    		String tinStr = this.getFiGiin().trim();

	    		// create TIN, and set content of TIN
	    		if (!tinStr.isEmpty()) {
	    			tin = new TINType();
	    			tin.setValue(tinStr);
	    		}
	    		return tin;
	   }
	        

	  private List<TINType> createTINList() throws Exception {
	        	
	        	List<TINType> tinList = null;
	        	
	        	TINType tin = createTIN();
	    		if (tin != null) {
	    			tinList = new ArrayList<TINType>();
	    			tinList.add(tin);
	    		}
	    		return tinList;
	  }        
	  
}