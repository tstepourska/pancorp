package ca.gc.cra.fxit.xmlt.task.xml.crs;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.task.xml.CommonXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.*;
import ca.gc.cra.fxit.xmlt.model.wrapper.crs.*;
import ca.gc.cra.fxit.xmlt.util.*;

/**
 * 
 * @author Txs285
 *
 */

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);
	
	private PackageInfo p;
	//why we need more than one header?
	private List<MessageHeaderWrapper> headerRecList = null;
	//private List<FIWrapper> fiRecList 				= null;	
	private List<PersonWrapper> personRecList 		= null;	
	//private List<PersonWrapper> controllingPersonRecList = null;
	private CommonXMLStreamWriter writer 			= null;
	private JaxbMarshaller marshaller 				= null;
	private int nRecordsProcessed 					= 0;
	private int lineNum 							= 0;	
	//boolean hasSlip 								= false;
	//boolean hasAccountHolder 						= false;	
	private ObjectFactory factory = null;

	boolean firstReportingFI						= true;
	boolean isFirstAccountReport					= true;
	AccountReportSlipWrapper m_slipRec 							= null;
	AccountHolderWrapper accountHolderRec 			= null;
	ArrayList<String> docRefIdList					= null;

	int numoffis = 0;
	int numofaccreps = 0;
	
	//////////////////////////////////////////////////////////////////////////////
	 /////////////////////     PUBLIC METHODS      ////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public String[] getSchemas(){
		String[] xsdpaths = new String[] {
				  Globals.schemaLocationBaseDir +"crs/isocrstypes_v1.0.xsd",
				  Globals.schemaLocationBaseDir +"crs/oecdtypes_v4.1.xsd",
				  Globals.schemaLocationBaseDir +"crs/CommonTypesFatcaCrs_v1.1.xsd",
				  Globals.schemaLocationBaseDir +"crs/FatcaTypes_v1.1.xsd",
				  Globals.schemaLocationBaseDir + "crs/" + Constants.MAIN_SCHEMA_NAME};
		return xsdpaths;
	}
	
	/**
	 * Reads text from the inputStream and transforms it to the international XML format 
	 * writing the document to the outputStream. The input is read and processed in chunks 
	 * in order to be able to handle large flat files.
	 * 
	 * @return int status code
	 */
	@SuppressWarnings("resource")
	@Override

	public int transform(PackageInfo __p){
		String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//int splitCount = p.getSplitFileCount();
		this.p = __p;
		docRefIdList = new ArrayList<String>();
		
		String inputFile  = Globals.FILE_WORKING_DIR + p.getOrigFilename();		
		String outputFile = Globals.FILE_WORKING_DIR + p.getXmlFilename();
		if(lg.isDebugEnabled())
			lg.debug(fp + "original file name: " + inputFile);		
		
		//int totalAccountReportCount = 0;
		//int singleFileAccReportCount = 0;
		
		//if(splitCount>Constants.NO_SPLIT)
			//totalAccountReportCount = countReports(inputFile);

		headerRecList 	= new ArrayList<>();
		personRecList 	= new ArrayList<PersonWrapper>(Constants.MAX_CONTROLLING_PERSONS);
		
		//groupList = new ArrayList<>();
		
		marshaller 		= new JaxbMarshaller();
		factory = new ObjectFactory();
		//TODO
		//marshaller.setUseTestDocTypeIndicCodes (isTest);
		BufferedReader reader = null;
		
		try {
			String line;
			int code = 0;	
			reader = new BufferedReader(new FileReader(inputFile));			
			
			OutputStream outputStream = new FileOutputStream(outputFile);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(outputStream,"UTF-8");
			writer = new CommonXMLStreamWriter(xmlWriter);

			//read text file; each line starts with the code specifying record type
			//process each line according to the code
			while((line = reader.readLine())!=null){
				lineNum++;
				
				code = Integer.parseInt(line.substring(0,4));
				if(lg.isDebugEnabled())
					lg.debug(fp + "line code: " + code);
				
				switch(code){
				case Constants.LINE_CODE_HEADER:	//1001
					processHeader(line);
					break;
				case Constants.LINE_CODE_FI:     // 1002
					processFI(line);
					break;
				case Constants.LINE_CODE_SPONSOR:	//1003
				//	processSponsor(line);
					break;
				case Constants.LINE_CODE_SLIP:		//1004
					//processSlip(line);
					processAccountReport(line);
					break;
				case Constants.LINE_CODE_PERSON: 	//1005
					processPerson(line);
					break;
				case Constants.LINE_CODE_ACCOUNT_HOLDER:  //1006
					processAccountHolder(line);
					break;
				case Constants.LINE_CODE_TRAILER: 		//1007
					processTrailer(line);
					break;
				default:
					throw new Exception("Invalid line code!");
				}
			}
			if(lg.isDebugEnabled())
			lg.debug(fp + lineNum + " lines read");
			
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_INVALID_INPUT_FILE;
			Utils.logError(lg, e);		
		}
		finally{
			try {
				reader.close();
			}catch(Exception e){}
			
			try {
				writer.flush();
				writer.close();
			}catch(Exception ex){}
		}

		return status;
	}	//end of transform
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PUBLIC METHODS      //////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  PRIVATE METHODS      ///////////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 
	
/*
	private int countReports(String inputFile){
		String fp = "countReports: ";
		int cr = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			String line;

			//read text file; each line starts with the code specifying record type
			while((line = reader.readLine())!=null){
				if(line.startsWith(""+Constants.LINE_CODE_SLIP))	//1004
					cr++;
			}
			if(lg.isDebugEnabled())
			lg.debug(fp + cr + " account reports found");
		}
		catch(Exception e){
			Utils.logError(lg, e);		
		}

		return cr;
	}*/
	
	/**
	 * Processes Header line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processHeader(String line) throws Exception {
		String fp = "processHeader: ";

		MessageHeaderWrapper headerRec = new MessageHeaderWrapper(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "header rec created");
		headerRecList.add(headerRec);
		//nothing to write to XML
	}
	
	/**
	 * Processes FI line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processFI(String line) throws Exception {
		String fp = "processFI: ";
		
		FIWrapper reportingFIRec = new FIWrapper(line);
		
		//TODO to remove!
		reportingFIRec.setFilrCntryCd("FR");
		reportingFIRec.setFiRsdCntryCd("FR");
		//reportingFIRec.getRtnRcpntCntryCd();
		reportingFIRec.setRtnRcpntCntryCd("FR");
		reportingFIRec.setFiInfoDtyCd("OECD_1");
		//////////////////////////////////////////
		
		if(lg.isDebugEnabled())
			lg.debug(fp + "reportingFIRec set: " + reportingFIRec.toString());
		
		//after single FI ReportingGroup with a list of AccountReports should start
		//so isFirstAccountReport is reset
		isFirstAccountReport = true;
		numoffis++;

		//this block is executed only once per Document,
		//before the first Reporting FI
		if (firstReportingFI){					
			String  warning="", 
					contact="", 
					messageRefId="", 
					messageTypeIndic="";
			List<String> corrMessageRefId = null;
			MessageSpecType messageSpec = this.createMessageSpec(p, reportingFIRec, warning, contact, messageRefId, messageTypeIndic, corrMessageRefId);
			marshaller.startDocument(messageSpec, writer);
			marshaller.transformMessageSpec(messageSpec, writer);			
		}

		CorrectableOrganisationPartyType party = reportingFIRec.createCorrectableOrganisationParty(factory, this.docRefIdList);
		if(lg.isDebugEnabled())
			lg.debug(fp + "party created, marshalling...");
			
		marshaller.transformReportingFI(party,firstReportingFI, writer);
		
		//first reportingFI has been written, flip the flag
		firstReportingFI = false;
	}	

	/**
	 * Processes Slip line of the flat file
	 * @param line
	 * @throws Exception
	 */
	//private void processSlip(String line) throws Exception {
	private void processAccountReport(String line) throws Exception {
		String fp = "processSlip: ";
		
		if(isFirstAccountReport){
			;//hasSlip = true;
		}
		
		m_slipRec = new AccountReportSlipWrapper(line);
		numofaccreps++;
		if(lg.isDebugEnabled())
			lg.debug(fp + "slipRecord set: " + m_slipRec);
		//if (hasSlip && hasAccountHolder) {
			//writer.writeCharacters("\n");
			//CrsBodyType.ReportingGroup group = new CrsBodyType.ReportingGroup();
			//group.setIntermediary(null);
			//group.setSponsor(null);
			//group.getPoolReport().add(e)
			
			CorrectableAccountReportType report = createCorrectableAccountReport(m_slipRec, accountHolderRec, personRecList);
			marshaller.transformAccountReport(report, isFirstAccountReport, writer);
			
			//once firstAccountReport in a ReportingFI has been written,
			//flip the flag
			isFirstAccountReport = false;
			
			//nRecordsProcessed = nRecordsProcessed + 2 + personRecList.size();
			//hasSlip = false;
			//hasAccountHolder = false;			
			//personRecList.clear();
		//}
		/*
		else if (hasSlip) {
			lg.error("Line " + (lineNum) + " missing ACCOUNT HOLDER record for SLIP.");
			throw new Exception( "Line " + (lineNum) 
					+ " missing ACCOUNT HOLDER record for SLIP");
		}
		else if (hasAccountHolder) {
			lg.error("Line " + (lineNum) + " missing SLIP record for ACCOUNT HOLDER.");
			throw new Exception( "Line " + (lineNum) + " missing SLIP record for ACCOUNT HOLDER.");
		}*/
		
		//prt18RtnSize += SLIP_REC_LEN;
	}
	
	/**
	 * Processes Person line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processPerson(String line) throws Exception {
		String fp = "processPerson: ";

		PersonWrapper personRec = new PersonWrapper(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "personRec set");
		
		//prt18RtnSize += PERSON_REC_LEN;
		personRecList.add(personRec);
	}
	
	/**
	 * Processes Account Holder line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processAccountHolder(String line) throws Exception {
		String fp = "processAccountHolder: ";
		accountHolderRec = new AccountHolderWrapper(line);
		
		if(lg.isDebugEnabled())
			lg.debug(fp + "accountHolderRec set");
		
		//hasAccountHolder = true;		
		//prt18RtnSize += ACCOUNT_HOLDER_REC_LEN;
	}
	
	/**
	 * Processes Trailer line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processTrailer(String line) throws Exception {
		String fp = "processTrailer: ";
		lg.info(fp + "done");
	}
	
	/**
	 * Creates CorrectableAccountReportType element 
	 * 
	 * @param slipRec
	 * @param accountHolderRec
	 * @param personRecs
	 * 
	 * @return CorrectableAccountReportType
	 */
    private CorrectableAccountReportType createCorrectableAccountReport( 
			AccountReportSlipWrapper slipRec,
			AccountHolderWrapper accountHolderRec, 
			List<PersonWrapper> personRecs) 
    throws Exception {
    	
    	// create an empty CorrectableAccountReportType object                                             
    	CorrectableAccountReportType report 	= factory.createCorrectableAccountReportType(); // new CorrectableAccountReportType();	    
    	List<ControllingPersonType> controllingPerson 			= null;
    	//List<PersonPartyType> person 			= null;
    	try {
    		// 1. doc spec
    		DocSpecType docSpec 					= slipRec.createDocSpec(factory,this.docRefIdList); // createDocSpecFromIP6PRTSL(slipRec);	
    		
    		// 2. account number 
    		FIAccountNumberType accountNumber 		= factory.createFIAccountNumberType(); // new FIAccountNumberType();
    		accountNumber.setValue(slipRec.getFiCltaNbr().trim());// createAccountNumber(slipRec);
    		//TODO
    		//accountNumber.setAcctNumberType(slipRec.get
    		
    		// 3. account holder
    		AccountHolderType accountHolder 		= createAccountHolder(slipRec, accountHolderRec);	
    		
    		// 4. controlling person(s)
    		if (accountHolder != null && accountHolder.getOrganisation() != null) {
    		    controllingPerson = createControllingPersonList(personRecs);
    		}
    		
    		// 5. account balance
    		CurrCodeType currCode 			= CurrCodeType.fromValue(accountHolderRec.getFiAcctCrcyTcd()); 		
    		MonAmntType accountBalance 		= WrapperUtils.createMonAmntFromStr(accountHolderRec.getFiAbamt(), currCode);
    		
    		// 6. payment(s)
    		List<PaymentType> payment 		= accountHolderRec.createPaymentList(currCode);

    		// 1.
    		report.setDocSpec(docSpec);
    		
    		// 2. 
    		report.setAccountNumber(accountNumber);
    		
    		// 3.
    		report.setAccountHolder(accountHolder);
    		//TODO
    		// 4. 
    		report.getControllingPerson().addAll(controllingPerson);
    		
    		// 5. 
    		report.setAccountBalance(accountBalance);
    		
    		// 6. 
    		report.getPayment().addAll(payment);
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}

		// return it
    	return report;
    }    
    
    
    /**
     * Creates AccountHolderType element
     * 
     * @param slipRec
     * @param accountHolderRec
     * 
     * @return AccountHolderType
     */
    private AccountHolderType createAccountHolder(
    		AccountReportSlipWrapper slipRec, 
			AccountHolderWrapper accountHolderRec 
			) throws Exception {
		
    	AccountHolderType holder 							= factory.createAccountHolderType(); //new AccountHolderType();
		PersonPartyType person 								= null;
		OrganisationPartyType organisation 					= null;
		CrsAcctHolderTypeEnumType orgAccountHolderTypeEnum 	= null;
		try {
		try {
			orgAccountHolderTypeEnum = CrsAcctHolderTypeEnumType.fromValue(slipRec.getOaTcd()); 		
			organisation = createOrganisationParty(slipRec, accountHolderRec);
		}
		catch(IllegalArgumentException iae){
			//not an organizational account type value,
			// get an individual
			person = createPersonParty(slipRec, accountHolderRec);
		}
		
		// set holder content
		if (person != null) {
			holder.setIndividual(person);
		}
    	if (organisation != null) {
    		holder.setOrganisation(organisation);
   			holder.setAcctHolderType(orgAccountHolderTypeEnum);
    	}
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
    	
    	return holder;
    }
    
    
    /**
     * 
     * @param slipRec
     * @param accountHolderRec
     * @return
     */
    private OrganisationPartyType createOrganisationParty( //FromIP6PRTSL (
    		AccountReportSlipWrapper slipRec, //IP6PRTSL slipRec,
			AccountHolderWrapper accountHolder //IP6PRTAC accountHolderRec
			) throws Exception {
		
    	OrganisationPartyType organisation 		= factory.createOrganisationPartyType(); // new OrganisationPartyType();
    	
    	try {
    	List<CountryCodeType> resCountryCodes  = slipRec.createOrganisationResCountryCodeList();
		
		List<OrganisationINType> in = slipRec.createOrganisationINList();
		List<NameOrganisationType> name = slipRec.createNameOrganisationList();
		List<AddressType> address = accountHolderRec.createAddressList(factory);
		
    	if (resCountryCodes != null) {
    		organisation.getResCountryCode().addAll(resCountryCodes);
    	}
    	if (in != null && !in.isEmpty()) {
    		organisation.getIN().addAll(in);
    	}
		// One or more Name is required for schema validation, and can be blank
    	if (name != null && !name.isEmpty()) {
    		organisation.getName().addAll(name);
    	}
		// One or more Address is required for schema validation
    	if (address != null && !address.isEmpty()) {
    		organisation.getAddress().addAll(address);
    	}		
    	}
		catch(Exception e){
			//Utils.logError(lg, e);
		}
    	
    	return organisation;
    }  
  
    
    /**
     * 
     * @param personRecs
     * @return
     * @throws Exception
     */
 /*   private List<PersonPartyType> createPersonPartyList( //FromIP6PRTCP (
    		List<PersonWrapper> personRecs) throws Exception {
    	
    	List<PersonPartyType> personList = null;

    	if (personRecs != null) {
	    	for (PersonWrapper personRec : personRecs) {
	    		PersonPartyType person = personRec.createPersonParty();
	    		if (person != null) {
		    		if (personList == null) 
		    			personList = new ArrayList<PersonPartyType>();
		    		
		    		personList.add(person);
	    		}
	    	}
    	}
    	
		return personList;
    } */
    
    private List<ControllingPersonType> createControllingPersonList(
    		List<PersonWrapper> personRecs) throws Exception {
    	
    	List<ControllingPersonType> personList = null;

    	if (personRecs != null) {
	    	for (PersonWrapper personRec : personRecs) {
	    		ControllingPersonType person = personRec.createControllingPersonParty(factory);
	    		if (person != null) {
		    		if (personList == null) 
		    			personList = new ArrayList<ControllingPersonType>();
		    		
		    		personList.add(person);
	    		}
	    	}
    	}
    	
		return personList;
    } 

    private PersonPartyType createPersonParty( //FromIP6PRTSL(
    		AccountReportSlipWrapper slipRec, //IP6PRTSL slipRec,
			AccountHolderWrapper accountHolderRec) //IP6PRTAC accountHolderRec) 
    throws Exception {
		
    	PersonPartyType person = factory.createPersonPartyType(); // new PersonPartyType();

    	try {
    	List<CountryCodeType> resCountryCode =slipRec.createPersonResCountryCodeList();
		List<TINType> tin = slipRec.createPersonTINList();
		List<NamePersonType> name = slipRec.createNamePersonList();
		List<AddressType> address = accountHolderRec.createAddressList(factory);
		PersonPartyType.BirthInfo birthInfo = WrapperUtils.createBirthInfoFromStr(slipRec.getIndvAhBrthYr(), slipRec.getIndvAhBrthMo(), slipRec.getIndvAhBrthDy());

    	if (resCountryCode != null && !resCountryCode.isEmpty()) {
    		person.getResCountryCode().addAll(resCountryCode);
    	}
    	if (tin != null && !tin.isEmpty()) {
    		person.getTIN().addAll(tin);
    	}
		// One or more Name is required for schema validation, and can be blank
    	if (name != null && !name.isEmpty()) {
    		person.getName().addAll(name);
    	}
		// One or more Address is required for schema validation
    	if (address != null && !address.isEmpty()) {
    		person.getAddress().addAll(address);
    	}
    	if (birthInfo != null) {
    		person.setBirthInfo(birthInfo);
    	}
    	}
    	catch(Exception e){
    		Utils.logError(lg, e);
    	}
    	return person;
    }  
    
 
    /**
     * Creates MessageSpec element
     * 
     * @param sendingCompanyIN	--Optional. Not used for exchange between Competent Authority under CRS
     *                            (Mandatory for domestic reporting: Sending Company Identification 
     *                            Number identifies the FI reporting to Sending Tax Authority by Domestic TIN (IN)  
     * @param transmittingCountry--Two characters sending country code from the ISO 3166-1 Alpha 2 Standard
     * @param receivingCountry	 --Two characters receiving country code from the ISO 3166-1 Alpha 2 Standard
     * @param messageType		--Specifies the type of the message being sent. For CRS the only allowable value is "CRS"
     * @param warning			--Free text expressing the restrictions for use of the information this message 
     * 							  contains and the legal framework under which it is given
     * @param contact			--All necessary contact information about persons responsible for and involved 
     *                            in the processing of the data transmitted in this message, both legally and 
     *                            technically. Free text as this is not intended for automatic processing
     * @param messageRefId		--Sender's unique identifier for this message
     * @param messageTypeIndic  --Shows message type: new data / corrected data / no data to report
     * @param corrMessageRefId  --Sender's unique identifier that has to be corrected. Must point to 
     *                            one or more previous messages.
     *                            Use corrMessageRefId to cancel a complete message
     * @param reportingPeriod	--The reporting year for which information is transmitted in documents 
     *                            of the current message
     * @param timestamp
     * 
     * @return MessageSpecType
     */
    private MessageSpecType createMessageSpec(  
    		PackageInfo p,
    		FIWrapper reportingFIRec,
            String warning,
            String contact,
            String messageRefId,
            String messageTypeIndic,
            List<String> corrMessageRefId
            ) throws Exception {

		String fp = "createMessageSpec: ";
		String sendingCompanyIN = ""; // FATCA_ENTITY_SENDER_ID_CANADA;
		//for the purpose of this data transformation application sending country is always Canada (CA)
    	CountryCodeType transmittingCountry = CountryCodeType.fromValue(Constants.CANADA);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "transmittingCountry: " + transmittingCountry);
    	CountryCodeType receivingCountry = CountryCodeType.fromValue(reportingFIRec.getRtnRcpntCntryCd());
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "receivingCountry: " + receivingCountry);
    	//CountryCodeType receivingCountryCode = CountryCodeType.fromValue(receivingCountry);
    	//For CRS the only allowable value is "CRS"
    	MessageTypeEnumType messageType = MessageTypeEnumType.CRS; //fromValue(Constants.MESSAGE_TYPE_CRS_DATA);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageType: " + messageType);
    	//String reportingPeriod = Utils.createReportingPeriodFromStr(reportingFIRec.getRtnTxYr());
    	//if(lg.isDebugEnabled())
    	//	lg.debug(fp + "reportingPeriod: " + reportingPeriod);
    	
    	//TODO where  timestamp is coming from?
    	long timestamp = System.currentTimeMillis();
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "timestamp: " + timestamp);
    	//XMLGregorianCalendar reportingPeriodXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(reportingPeriod); 
    	XMLGregorianCalendar reportingPeriodXML = p.getReportingPeriod();
        XMLGregorianCalendar xmlCreationTimestamp = Utils.generateReportXMLTimestamp(timestamp); // Utils.generateMetadataXMLTimestamp(timestamp); // DatatypeFactory.newInstance().newXMLGregorianCalendar(""+timestamp);
        if(lg.isDebugEnabled())
    		lg.debug(fp + "XML calendars created");
    	
    	// create an empty MessageSpecType object                                             
    	MessageSpecType messageSpec = new MessageSpecType();
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "Empty messageSpec created");
		
		// set properties on it
    	messageSpec.setSendingCompanyIN		(sendingCompanyIN);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "sendingCompanyIN set: " + sendingCompanyIN);
    	messageSpec.setTransmittingCountry	(transmittingCountry);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "transmittingCountry set: " + transmittingCountry);
    	messageSpec.setReceivingCountry		(receivingCountry);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "receivingCountry set: " + receivingCountry);
    	messageSpec.setMessageType			(messageType);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageType set: " + messageType);
    	messageSpec.setWarning				(warning);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "warning set: " + warning);
    	messageSpec.setContact				(contact);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "contact set: " + contact);
    	messageSpec.setMessageRefId			(messageRefId);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageRefId set: " + messageRefId);
    	if(corrMessageRefId!=null){
    	messageSpec.getCorrMessageRefId().addAll(corrMessageRefId);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "corrMessageRefId set: " + corrMessageRefId);
    	}
    	else{
    		messageSpec.getCorrMessageRefId().add("");
    	}
    	messageSpec.setReportingPeriod		(reportingPeriodXML);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "reportingPeriodXML set: " + reportingPeriodXML);
    	messageSpec.setTimestamp			(xmlCreationTimestamp);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "xmlCreationTimestamp set: " + xmlCreationTimestamp);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + " messageSpec params set");
    	
		// return it
		return messageSpec;
    }
    
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PRIVATE METHODS      ////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 

	/**
	 * For unit test only. TODO to move to the JUnit
	 * @param args
	 */
/*	public static void main(String[] args){
		//String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		String filename = "IP.AIP5S182.CAUS.A14.S0000001";
		//<CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		//String outputDir = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/";
		
		PackageInfo p = new PackageInfo();
		p.setDataProvider("crs");
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setOrigFilename(filename);
		
		Helper h = new Helper();
		int status = h.invoke(p);
		lg.info("Helper completed with status " + status);
	}
*/
}