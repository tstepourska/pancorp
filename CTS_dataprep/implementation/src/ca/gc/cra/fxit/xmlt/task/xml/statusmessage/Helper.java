/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.task.xml.statusmessage;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.List;

//import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.task.xml.CommonXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.CountryCodeType;
//import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.CrsMessageStatusType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ErrorDetailType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileMetaDataType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.LanguageCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.MessageSpecType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.MessageTypeEnumType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ObjectFactory;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.OriginalMessageType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ValidationErrorsType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ValidationResultType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType.FieldsInError;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);

	private CommonXMLStreamWriter writer 			= null;
	private JaxbMarshaller marshaller 				= null;
	ObjectFactory objFactory = null;

	//////////////////////////////////////////////////////////////////////////////
	 /////////////////////     PUBLIC METHODS      ////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////
	
	@Override
	public final int invoke(PackageInfo p){
		lg.info("Status Message Helper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		try {
			//generate XML
			status = transform(p);
			if(lg.isDebugEnabled())
			lg.debug("status: " + status);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		
		//TODO for wireframe testing only, to remove!
		//status=Constants.STATUS_CODE_SUCCESS;
		if(status==Constants.STATUS_CODE_SUCCESS){
			//reset status for validation
			status = Constants.STATUS_CODE_INCOMPLETE;
			String outputFile = p.getFileWorkingDir() + p.getXmlFilename();		//Globals.FILE_WORKING_DIR
			lg.debug("outputFile: " + outputFile);
			String[] xsdpaths = getSchemas();
					
			try {
				status = this.validate(p, xsdpaths, outputFile);
				lg.info("Validation completed with status " + status);
			}
			catch(Exception e){
				Utils.logError(lg, e);
				status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
			}
		}
		
		//TODO to remove
		status= Constants.STATUS_CODE_SUCCESS;
		
		return status;
	}
	
	@Override
	public String[] getSchemas(){
		String[] xsdpaths = new String[] {
				Constants.RESOURCE_BASE_PKG +"schema/statusmessage/isocsmtypes_v1.0.xsd",
				Constants.RESOURCE_BASE_PKG + "schema/statusmessage/" + Constants.MAIN_SCHEMA_NAME
				 };
		
		return xsdpaths;
	}
	
	/**
	 * Reads text from the inputStream and transforms it to the international XML format 
	 * writing the document to the outputStream. The input is read and processed in chunks 
	 * in order to be able to handle large flat files.
	 * 
	 * @return int status code
	 */
	//@SuppressWarnings("resource")
	@Override
	public int transform(PackageInfo p){
		String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//String inputFile  = Globals.baseFileDir + Globals.outboundUnprocessed + p.getOrigFilename();
		long timestamp = System.currentTimeMillis();
		//p.setXmlFilename(xmlFilename);
		String outputFile =  p.getFileWorkingDir() + p.getXmlFilename();
		if(lg.isDebugEnabled())
			lg.debug(fp + "output file name: " + outputFile);				
		
		marshaller 		= new JaxbMarshaller();
		//TODO
		String testIndicator = p.getTestIndicator();
		//marshaller.setUseTestDocTypeIndicCodes (testIndicator);

		try {
			objFactory = new ObjectFactory();
			
			OutputStream outputStream = new FileOutputStream(outputFile);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(outputStream,"UTF-8");
			writer = new CommonXMLStreamWriter(xmlWriter);

			String warning = null;
			
			marshaller.startDocument(writer);
			
			MessageSpecType messageSpec = createMessageSpec(p, warning,timestamp);		
			marshaller.transformMessageSpec(messageSpec, writer);
			/*
			CrsMessageStatusType mst = createCrsMessageStatusType(p);
			marshaller.transformCrsMessageStatusType(mst, writer);
*/
			
			OriginalMessageType mt = createOriginalMessageType(p);
			marshaller.transformOriginalMessageType(mt, writer);
			
			ValidationErrorsType vet = createValidationErrorsType(p);
			marshaller.transformValidationErrorsType(vet,writer);
			
			ValidationResultType vrt = createValidationResultType(p);
			marshaller.transformValidationResultType(vrt, writer);
			
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_INVALID_INPUT_FILE;
			Utils.logError(lg, e);		
		}
		finally{
			try{
				writer.flush();
				writer.close();
			}catch(Exception e){}
		}

		return status;
	}	//end of transform
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PUBLIC METHODS      //////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  PRIVATE METHODS      ///////////////////////////////////
	 //////////////////////////////////////////////////////////////////////////////  
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
            String warning,
            long timestamp
            ) throws Exception {

		String fp = "createMessageSpec: ";
		
		//this field is NOT used in international data exchange (only for domestic exchanges)
		String sendingCompanyIN = null; 

		/*
		 * This data element identifies the jurisdiction of the Competent Authority transmitting the message, 
		 * which is the Competent Authority that has received the initial CRS message 
		 * to which the Status Message relates. It uses the 2-character alphabetic country code 
		 * and country name list1 based on the ISO 3166-1 Alpha 2 standard.
		 * 
		 * Validation
		 */
		//for the purpose of this data transformation application sending country is always Canada (CA)
    	CountryCodeType transmittingCountry = CountryCodeType.fromValue(Constants.CANADA);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "transmittingCountry: " + transmittingCountry);
    	
    	/*
    	 * This data element identifies the jurisdiction of the Competent Authority receiving the message, 
    	 * which is the Competent Authority that has sent the initial CRS message to which 
    	 * the Status Message relates. This data element identifies the jurisdiction(s) of the 
    	 * Competent Authority or Authorities that are the intended recipient(s) of the message. 
    	 * It uses the 2-character alphabetic country code based on the ISO 3166-1 Alpha 2 standard.
    	 * 
    	 * Validation
    	 */
    	CountryCodeType receivingCountry = CountryCodeType.fromValue(p.getReceivingCountry());
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "receivingCountry: " + receivingCountry);
    	
    	//CountryCodeType receivingCountryCode = CountryCodeType.fromValue(receivingCountry);
    	/*
    	 * This data element specifies the type of message being sent. The only allowable entry in this field is “CRSStatusMessage”.
    	 * Attention: as per schema, it is "CRSMessageStatus"
    	 */
    	MessageTypeEnumType messageType = MessageTypeEnumType.fromValue(p.getDataProvider().toUpperCase() + Constants.MSG_TYPE_MESSAGE_STATUS);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageType: " + messageType);
    	
    	// create an empty MessageSpecType object                                             
    	MessageSpecType messageSpec = new MessageSpecType();
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "Empty messageSpec created");
		
		// set properties on it
    	if(sendingCompanyIN!=null)
    		messageSpec.setSendingCompanyIN		(sendingCompanyIN);
    	
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "sendingCompanyIN set: " + sendingCompanyIN);
    	messageSpec.setTransmittingCountry	(transmittingCountry);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "transmittingCountry set: " + transmittingCountry);
    	messageSpec.setReceivingCountry		(receivingCountry);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "receivingCountry set: " + receivingCountry);
    	
    	/* Validation */
    	messageSpec.setMessageType			(messageType);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageType set: " + messageType);
    	
    	/*
    	 * This data element is a free text field allowing input of specific cautionary 
    	 * instructions about use of the CRS Status Message.
    	 * 
    	 * Optional
    	 */
    	messageSpec.setWarning				(warning);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "warning set: " + warning);
    	
    	/*
    	 * This data element is a free text field allowing input of specific contact information 
    	 * for the sender of the message (i.e. the Competent Authority sending the CRS Status Message).
    	 * 
    	 * Optional.
    	 */
    	messageSpec.setContact				(Globals.mailSenderAddressList);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "contact set: " + messageSpec.getContact());
    	
    	/* This data element is a free text field capturing the sender’s unique identifying number 
    	 * (created by the sender) that identifies the particular CRS Status Message being sent. 
    	 * The identifier allows both the sender and receiver to identify the specific message later 
    	 * if questions or corrections arise. The first part should be the country code of the jurisdiction 
    	 * of the sending Competent Authority, and the second part a unique identifying number 
    	 * created by the sending jurisdiction.
    	 * It should be noted that the unique identifier for the CRS Status Message to be entered here 
    	 * is not to be confused with OriginalMessageRefID which indicates the MessageRefID of the 
    	 * original CRS message, in relation to which the CRS Status Message is provided.
    	 * The MessageRefID identifier can contain whatever information the sender of the 
    	 * CRS Status Message uses to allow identification of the particular CRS Status Message 
    	 * but should start with the word “Status”, followed by the sender country code (i.e. 
    	 * the Competent Authority receiving the original CRS message) as the first element for 
    	 * Competent Authority to Competent Authority transmission, then the year to which the data relates, 
    	 * then the receiver country code (i.e. the sender of the original CRS message) before a unique identifier.
    	 * 
    	 * Example: StatusFR2013CA123456789
    	 * 
    	 * 200 chars, validation 
    	 */
    	messageSpec.setMessageRefId			(p.getMessageRefId());
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageRefId set: " + messageSpec.getMessageRefId());

    	/*
    	 * This data element identifies the date and time when the message was compiled. 
    	 * It is anticipated this element will be automatically populated by the host system. 
    	 * The format for use is YYYY-MM-DD’T’hh:mm:ss. Fractions of seconds may be used. 
    	 * Example: 2018-02-15T14:37:40
    	 */
    	XMLGregorianCalendar xmlCreationTimestamp =p.getOrigCTSSendingTimeStamp(); // Utils.generateMetadataXMLTimestamp(timestamp);// DatatypeFactory.newInstance().newXMLGregorianCalendar(""+timestamp);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "xmlCreationTimestamp set: " + xmlCreationTimestamp);
    	
    	messageSpec.setTimestamp(xmlCreationTimestamp);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + " messageSpec params set");
    	
		// return it
		return messageSpec;
    }

    /*

    private CrsMessageStatusType createCrsMessageStatusType(PackageInfo p) throws Exception {
    	CrsMessageStatusType ms = objFactory.createCrsMessageStatusType();
    	ms.setOriginalMessage(createOriginalMessageType(p));
    	ms.setValidationErrors(createValidationErrorsType(p));
    	ms.setValidationResult(createValidationResultType(p));
    	
    	return ms;
    }*/
    
    /**
     * Creates OriginalMessageType. The Original Message element indicates the original 
     * CRS message (i.e. which CRS XML file) for which a CRS Status Message is provided. 
     * It specifies the MessageRefID of the original CRS message and the File Meta Data 
     * information.
     * 
     * @param p
     * @return
     * @throws Exception
     */
    private OriginalMessageType createOriginalMessageType(PackageInfo p) throws Exception {
    	OriginalMessageType omt = new OriginalMessageType();
    	omt.setOriginalMessageRefID(p.getOrigMessageRefId());
    	
    	FileMetaDataType fmd = objFactory.createFileMetaDataType();
    	/*
    	 * The File Meta Data element contains information about the original 
    	 * transmission of the CRS message through the CTS. 
    	 * This data includes:  	 */
    	//the date and time the transmission was sent through the CTS - YYYY-MM-DD’T’hh:mm:ss.
    	fmd.setCTSSendingTimeStamp(p.getOrigCTSSendingTimeStamp());
    	// the CTS Transmission ID for the original transmission as sent by the sending Competent Authority
    	fmd.setCTSTransmissionID(p.getOrigCTSTransmissionId());
    	//the sender of the original transmission
    	//fmd.setSenderFileID(p.getOrigSenderFileId());
    	// the size of the decrypted, uncompressed CRS message
    	fmd.setUncompressedFileSizeKBQty(p.getOrigUncompressFileSizeKBQty());
    	
    	omt.setFileMetaData(fmd);
    	
    	return omt;
    }
	
    /**
     * The Validation Errors element indicates if the Competent Authority that has received 
     * the initial CRS message has found errors with respect to that original CRS message,
     *  with the result being either file errors found, record errors found or no error found.
     * @param p
     * @return
     */
    private ValidationErrorsType createValidationErrorsType(PackageInfo p){
    	ValidationErrorsType vet = objFactory.createValidationErrorsType();
    	//ValidationErrorsWrapper vew = new ValidationErrorsWrapper();
    	
    	//TODO create lists - pass parameters
    	List<FileErrorType> fe = createFileErrorTypeList(); 
    	List<RecordErrorType> re = createRecordErrorTypeList();
    	/*if(fe==null  && re==null)  {
    		//vet.
    		//vet.setNoErrorFound(NoErrorFoundEnumType.NO_ERROR);
    	}
    	else{*/
    		if(fe!=null&&fe.size()>0)
    			vet.getFileError().addAll(fe);

    		if(re!=null&&re.size()>0)
    			vet.getRecordError().addAll(re);    	
    //	}
    	
    	return vet;
    }
    
	private List<FileErrorType> createFileErrorTypeList(){
		List<FileErrorType> list = new ArrayList<>();
		String code = null;
		String detail = null;
		//TODO extract codes and details
		
		LanguageCodeType lg = LanguageCodeType.EN;
		FileErrorType err = createFileErrorType(code,detail, lg);
		if(err!=null)
			list.add(err);
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	public List<RecordErrorType> createRecordErrorTypeList(){
		List<RecordErrorType> list = new ArrayList<>();
		String code = null;
		String detail = null;
		//TODO extract codes and details
		
		LanguageCodeType lg = LanguageCodeType.EN;
		RecordErrorType err = createRecordErrorType(code,detail, lg);
		if(err!=null)
			list.add(err);
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	private FileErrorType createFileErrorType(String c, String d, LanguageCodeType lang){
		if(c==null)
			return null;
		
		FileErrorType err = objFactory.createFileErrorType();
		err.setCode(c);
		ErrorDetailType detail = objFactory.createErrorDetailType();
		if(d!=null)
			detail.setValue(d);
		detail.setLanguage(lang);
		
		err.setDetails(detail);
		
		return err;
	}
	
	public RecordErrorType createRecordErrorType(String c, String d, LanguageCodeType lang){
		if(c==null)
			return null;
		
		RecordErrorType err = objFactory.createRecordErrorType();
		err.setCode(c);
		ErrorDetailType detail = objFactory.createErrorDetailType();
		detail.setValue(d);
		detail.setLanguage(lang);
	
		err.setDetails(detail);
		
		//TODO
		List<String> docRefIDInErrList = createDocRefIDInErrorList();
		if(docRefIDInErrList!=null)
		err.getDocRefIDInError().addAll(docRefIDInErrList);
		
		List<FieldsInError> fieldsInErrList = createFieldsInErrorList();
		
		if(fieldsInErrList!=null)
		err.getFieldsInError().addAll(fieldsInErrList);
		
		return err;
	}
	
	public List<String> createDocRefIDInErrorList(){
		List<String> list = new ArrayList<>();
		//TODO extract docRefIds
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	public List<FieldsInError> createFieldsInErrorList(){
		List<FieldsInError> list = new ArrayList<>();
		//TODO extract fields in error
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
    
    private ValidationResultType createValidationResultType(PackageInfo p){
    	ValidationResultType vrt = objFactory.createValidationResultType();
    	vrt.setStatus(p.getOrigFileAcceptanceStatus());
    	
    	ArrayList<String> vb = createValidatedBy();
    	
    	if(vb==null){
    		vb = new ArrayList<String>();
    		vb.add(" ");		
    	}
    	
    	vrt.getValidatedBy().addAll(vb);
    		
    	return vrt;
    }
    
    private ArrayList<String> createValidatedBy(){
    	ArrayList<String> vb = new ArrayList<>();
    	vb.add("javax.xml.validation.Validator Java 1.7");
    	//TODO
    	if(vb.isEmpty())
    		vb = null;
    	
    	return vb;
    }
    //////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PRIVATE METHODS      ////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 

	/**
	 * See JUnit ca.gc.cra.fxit.xmlt.test.XmlStatusMessageHelperTest
	 * @param args
	 */
/*	public static void main(String[] args){
		//String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		String filename = null;// "IP.AIP5S182.CAUS.A14.S0000001";
		// <CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		//String outputDir = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/";
		
		PackageInfo p = new PackageInfo();
		p.setDataProvider("crs");
		p.setJobDirection(Constants.JOB_OUTBOUND);
		filename = "CRSStatusMessage.xml";
		p.setOrigFilename(filename);
		p.setXmlFilename(filename);
		p.setReceivingCountry("FR");
		p.setSendingCountry(Constants.CANADA);
		p.setMessageRefId("CA2016FR123456789");
		
		
		try {
		p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
		p.setOrigFileAcceptanceStatus(FileAcceptanceStatusEnumType.ACCEPTED);
		p.setOrigMessageRefId("CA2016FR2345678");
		//XMLGregorianCalendar cal = new XMLGregorianCalendar("2017-01-15");
		p.setOrigCTSSendingTimeStamp(Utils.generateXMLTimestamp(System.currentTimeMillis()));
		p.setOrigUncompressFileSizeKBQty(BigInteger.valueOf(12345678));
		}
		catch(Exception e) {
			Utils.logError(lg, e);
		}
    	// the CTS Transmission ID for the original transmission as sent by the sending Competent Authority
    	p.setOrigCTSTransmissionId("asdf89asdfgdfiua0");
    	//the sender of the original transmission
    	p.setOrigSenderFileId("CA2016FR234567891");
    	// the size of the decrypted, uncompressed CRS message
    	p.setOrigUncompressFileSizeKBQty(new BigInteger("26542374"));
		
		Helper h = new Helper();
		int status = h.invoke(p);
		lg.info("Helper completed with status " + status);
	}*/
}