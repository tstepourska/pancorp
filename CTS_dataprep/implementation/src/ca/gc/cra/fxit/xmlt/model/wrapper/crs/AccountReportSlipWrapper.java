package ca.gc.cra.fxit.xmlt.model.wrapper.crs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.cob2java.crs.IP6PRTSL;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.*;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class AccountReportSlipWrapper extends IP6PRTSL {
	private static Logger lg = Logger.getLogger(AccountReportSlipWrapper.class);
	
	public AccountReportSlipWrapper(String line) throws Exception {
		super();
		if(line.length() != this.length()){
			lg.info("line: " + line);
			lg.error("Header record line length is not correct: " + line.length() + "!=" + this.length());
			//TODO throw exception?
		}
		this.setRec(line);
	}
	
    /**
     * Creates DocSpecType for 
     * @param slipRec
     * @return
     */
    public DocSpecType createDocSpec(ObjectFactory factory, ArrayList<String> driList) throws Exception {
		
		DocSpecType docSpec = factory.createDocSpecType(); //new DocSpecType();

		try {
			
		//format 'FR201312345'
		String docRefId 		= this.getIfaieSlpDocRefId().trim();
		String corrDocRefId 	= this.getIfaieSlpCorrDocId().trim();

		docSpec.setDocRefId		(docRefId);
		driList.add(docRefId);
		if(lg.isDebugEnabled())
			lg.debug("createDocSpec: docRefId added: " + docRefId);
		docSpec.setCorrDocRefId	(corrDocRefId);
		
			// read docSpec content from input 
		// set docSpec content
			OECDDocTypeIndicEnumType oecdDocTypeIndicEnum = OECDDocTypeIndicEnumType.fromValue(this.getIfaieRtnSlpDtyCd()); //createFatcaDocTypeIndicEnumFromStr(slipRec.getIfaieRtnSlpDtyCd());
			docSpec.setDocTypeIndic	(oecdDocTypeIndicEnum);
		//TODO
		//docSpec.setCorrMessageRefId(this.getCorrMessageRrefId);
		}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		
		return docSpec;
    } 
 
    ////////////////////////////////////////////////////////////////////////////

    public List<CountryCodeType> createPersonResCountryCodeList () throws Exception {
    	
    	List<CountryCodeType> countryCodeList = new ArrayList<>(); //null;
    	//List<String> countryCodeList = null;
    	try {
    		for (int i = 0; i < Constants.MAX_RESIDENCE_COUNTRY_CODES; i++) {
    			CountryCodeType countryCode = CountryCodeType.fromValue(this.getFiAhRsdCntryCd(i));
    			if (countryCode != null) 
    				countryCodeList.add(countryCode);
    		}
    	}
    	catch(Exception e){
		//Utils.logError(lg, e);
    	}
    	
		return countryCodeList;
    }
    
    public List<OrganisationINType> createOrganisationINList() throws Exception {
    	
    	List<OrganisationINType> inList = null;
    	try{
    	OrganisationINType in = createOrganisationIN();
		if (in != null) {
			inList = new ArrayList<OrganisationINType>();
			inList.add(in);
		}
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return inList;
    }
    
    /**
     * 
     * @param slipRec
     * @return
     */
    public OrganisationINType createOrganisationIN() throws Exception {	
    	OrganisationINType tin = null;
    	
    	try {
		// read TIN element value from input
		String tinStr = this.getOaFgnTin().trim();

		CountryCodeType issuedBy = CountryCodeType.fromValue(this.getIndvAhFtinCntryCd());
		// create IN, and set content of IN
		if (!tinStr.isEmpty()) {
			tin = new OrganisationINType();
			tin.setValue(tinStr);
			if (issuedBy != null) {
				tin.setIssuedBy(issuedBy);
			}
		}
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return tin;
    }
    
    /**
     * 
     * @param slipRec
     * @return
     */
    public List<NameOrganisationType> createNameOrganisationList() throws Exception {
    	
		// One or more name is required for schema validation, and can be blank
    	List<NameOrganisationType> nameList = new ArrayList<NameOrganisationType>();
    	try {
   		NameOrganisationType name = WrapperUtils.createNameOrganisationFromStr(this.getOaNmL1Txt(),this.getOaNmL2Txt());
    	if (name != null) {
        	nameList.add(name);
    	}
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
    	
		return nameList;
    }

    public List<NamePersonType> createNamePersonList() throws Exception {
    	
    	List<NamePersonType> nameList = null;   	
    	try {
    	NamePersonType name = WrapperUtils.createNamePersonFromStr(this.getIndvAhGvnNm(), this.getIndvAhMidNm(), this.getIndvAhSnm());

		if (name != null) {
			nameList = new ArrayList<NamePersonType>();
			nameList.add(name);
		}
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return nameList;
    } 
    
    public List<TINType> createPersonTINList() throws Exception {
    	
    	List<TINType> tinList = null;
    	try{
    	TINType tin = createPersonTIN();
		if (tin != null) {
			tinList = new ArrayList<TINType>();
			tinList.add(tin);
		}
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return tinList;
    }
    
    private TINType createPersonTIN () throws Exception {
		
		TINType tin = null;
		try {
		// read TIN element value from input
		String tinStr = this.getIndvAhFgnTin().trim();

		CountryCodeType issuedBy = CountryCodeType.fromValue(this.getIndvAhFtinCntryCd());

		// create TIN, and set content of TIN
		if (!tinStr.isEmpty()) {
			tin = new TINType();
			tin.setValue(tinStr);
			if (issuedBy != null) {
				tin.setIssuedBy(issuedBy);
			}
		}
		}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return tin;
    }
    
    /**
     * Creates a list of CountryCodeType elements 
     * 
     * @param slipRec
     * 
     * @return List<CountryCodeType>
     */
    public List<CountryCodeType> createOrganisationResCountryCodeList () throws Exception {

    	List<CountryCodeType> countryCodeList = null;
    	try {
    	for (int i = 0; i < Constants.MAX_RESIDENCE_COUNTRY_CODES; i++) {
    		CountryCodeType countryCode = CountryCodeType.fromValue(this.getAhRsdCntryCd(i));

			if (countryCode == null)
				continue;			
				
			if (countryCodeList == null) 
				countryCodeList = new ArrayList<CountryCodeType>();
				
			countryCodeList.add(countryCode);			
    	}
    	if(countryCodeList.isEmpty())
    		countryCodeList = null;
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
		return countryCodeList;
    }
  
}
