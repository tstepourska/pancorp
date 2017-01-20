package ca.gc.cra.fxit.xmlt.task.xml.statusmessage;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;


import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.task.xml.CustomXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.CountryCodeType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.FileAcceptanceStatusEnumType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.FileMetaDataType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.MessageSpecType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.MessageTypeEnumType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.NoErrorFoundEnumType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.ObjectFactory;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.OriginalMessageType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.ValidationErrorsType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.ValidationResultType;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);

	private CustomXMLStreamWriter writer 			= null;
	private JaxbMarshaller marshaller 				= null;
	ObjectFactory objFactory = null;

	//////////////////////////////////////////////////////////////////////////////
	 /////////////////////     PUBLIC METHODS      ////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////
	@Override
	public int invoke(PackageInfo p){
		lg.info("SM XmlHelper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		//generate XML
		status = transform(p);
		lg.info("Transformation completed with status " + status);

		//if transformation successful, validate XML
		String outputFile = AppProperties.baseFileDir + AppProperties.outboundProcessed + p.getXmlFilename();
				
		if(status==Constants.STATUS_CODE_SUCCESS)
			status = this.validate(p, AppProperties.schemaLocationBaseDir +"statusmessage/" + Constants.MAIN_SCHEMA_NAME, outputFile);
		lg.info("Validation completed with status " + status);
		
		return status;
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
	public int transform(PackageInfo p){
		String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//String inputFile  = AppProperties.baseFileDir + AppProperties.outboundUnprocessed + p.getOrigFilename();
		String outputFile = AppProperties.baseFileDir + AppProperties.outboundProcessed + p.getDataProvider().toUpperCase() + "StatusMessage.xml";
		if(lg.isDebugEnabled())
			lg.debug(fp + "output file name: " + outputFile);		
		
		boolean isTest = true;
		marshaller 		= new JaxbMarshaller();
		//TODO
		//marshaller.setUseTestDocTypeIndicCodes (isTest);

		try {
			objFactory = new ObjectFactory();
			
			OutputStream outputStream = new FileOutputStream(outputFile);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(outputStream,"UTF-8");
			writer = new CustomXMLStreamWriter(xmlWriter);

			String warning = null;
			
			marshaller.startDocument(writer);
			
			MessageSpecType messageSpec = createMessageSpec(p, warning);		
			marshaller.transformMessageSpec(messageSpec, writer);
			
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
            String warning
            ) throws Exception {

		String fp = "createMessageSpec: ";
		String sendingCompanyIN = ""; // FATCA_ENTITY_SENDER_ID_CANADA;
		//for the purpose of this data transformation application sending country is always Canada (CA)
    	CountryCodeType transmittingCountry = CountryCodeType.fromValue(Constants.TRANSMITTING_COUNTRY_CODE);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "transmittingCountry: " + transmittingCountry);
    	CountryCodeType receivingCountry = CountryCodeType.fromValue(p.getReceivingCountry());
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "receivingCountry: " + receivingCountry);
    	//CountryCodeType receivingCountryCode = CountryCodeType.fromValue(receivingCountry);
    	//For CRS the only allowable value is "CRS"
    	MessageTypeEnumType messageType = MessageTypeEnumType.fromValue(p.getDataProvider().toUpperCase() + Constants.MESSAGE_TYPE_SM);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageType: " + messageType);
    	//String reportingPeriod = Utils.createReportingPeriodFromStr(""+p.getTaxYear());
    	//if(lg.isDebugEnabled())
    	//	lg.debug(fp + "reportingPeriod: " + reportingPeriod);
    	
    	//TODO where  timestamp is coming from?
    	long timestamp = System.currentTimeMillis();
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "timestamp: " + timestamp);
    	XMLGregorianCalendar reportingPeriodXML = p.getReportingPeriod();// DatatypeFactory.newInstance().newXMLGregorianCalendar(reportingPeriod); 
        XMLGregorianCalendar xmlCreationTimestamp = Utils.generateXMLTimestamp(timestamp);// DatatypeFactory.newInstance().newXMLGregorianCalendar(""+timestamp);
        if(lg.isDebugEnabled())
    		lg.debug(fp + "XML calelndars created");
    	
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
    	messageSpec.setContact				(AppProperties.mailSenderAddressList);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "contact set: " + messageSpec.getContact());
    	messageSpec.setMessageRefId			(p.getMessageRefId());
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "messageRefId set: " + messageSpec.getMessageRefId());
    	
    	String strOrigMessRefId = p.getOrigMessageRefId();
    	if(strOrigMessRefId==null)
    		messageSpec.getCorrMessageRefId().add("");
    	else{
    	String[] arr = p.getOrigMessageRefId().split(",");
    	List<String> corrMessageRefId = new ArrayList<>();
    	for(int i=0;i<arr.length;i++)
    		corrMessageRefId.add(arr[i]);
    		
    	messageSpec.getCorrMessageRefId().addAll(corrMessageRefId);
    	if(lg.isDebugEnabled())
    		lg.debug(fp + "corrMessageRefId set: " + corrMessageRefId);
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
    
    private OriginalMessageType createOriginalMessageType(PackageInfo p) throws Exception {
    	OriginalMessageType omt = new OriginalMessageType();
    	omt.setOriginalMessageRefID(p.getOrigMessageRefId());
    	
    	FileMetaDataType fmd = objFactory.createFileMetaDataType();
    	/*
    	 * The File Meta Data element contains information about the original 
    	 * transmission of the CRS message through the CTS. 
    	 * This data includes:  	 */
    	//the date and time the transmission was sent through the CTS
    	fmd.setCTSSendingTimeStamp(p.getOrigCTSSendingTimeStamp());
    	// the CTS Transmission ID for the original transmission as sent by the sending Competent Authority
    	fmd.setCTSTransmissionID(p.getOrigCTSTransmissionId());
    	//the sender of the original transmission
    	fmd.setSenderFileID(p.getOrigSenderFileId());
    	// the size of the decrypted, uncompressed CRS message
    	fmd.setUncompressedFileSizeKBQty(p.getOrigUncompressFileSizeKBQty());
    	
    	omt.setFileMetaData(fmd);
    	
    	return omt;
    }
	
    private ValidationErrorsType createValidationErrorsType(PackageInfo p){
    	ValidationErrorsType vet = objFactory.createValidationErrorsType();
    	vet.getFileError()  .addAll(createFileErrorList());
    	vet.getRecordError().addAll(createRecordErrorList());
    	
    	if(vet.getFileError().isEmpty() && vet.getRecordError().isEmpty())
    		vet.setNoErrorFound(NoErrorFoundEnumType.NO_ERROR);
    	
    	return vet;
    }
    
    private ArrayList<FileErrorType> createFileErrorList(){
    	ArrayList<FileErrorType> list = new ArrayList<>();
    	
    	//TODO
    	
    	
    	return list;
    }
    
    
    private ArrayList<RecordErrorType> createRecordErrorList(){
    	ArrayList<RecordErrorType> list = new ArrayList<>();
    	
    	//TODO
    	
    	
    	return list;
    }
    
    private ValidationResultType createValidationResultType(PackageInfo p){
    	ValidationResultType vrt = objFactory.createValidationResultType();
    	vrt.setStatus(FileAcceptanceStatusEnumType.fromValue(p.getOrigFileAcceptanceStatus()));
    	vrt.getValidatedBy().addAll(createValidatedBy());
    	
    	return vrt;
    }
    
    private ArrayList<String> createValidatedBy(){
    	ArrayList<String> vb = new ArrayList<>();
    	
    	//TODO
    	
    	return vb;
    }
    //////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PRIVATE METHODS      ////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 

	/**
	 * For unit test only. TODO to move to the JUnit
	 * @param args
	 */

	public static void main(String[] args){
		//String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		String filename = "IP.AIP5S182.CAUS.A14.S0000001";
		/*
		 * <CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		 */
		//String outputDir = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/";
		
		PackageInfo p = new PackageInfo();
		p.setDataProvider("crs");
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setOrigFilename(filename);
		p.setReceivingCountry("FR");
		p.setMessageRefId("CA2016FR123456789");
		
		try {
		p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
		p.setOrigFileAcceptanceStatus("Accepted");
		p.setOrigMessageRefId("CA2016FR2345678,CA2016FR123467895");
		
		//XMLGregorianCalendar cal = new XMLGregorianCalendar("2017-01-15");
		p.setOrigCTSSendingTimeStamp(Utils.generateXMLTimestamp(System.currentTimeMillis()));
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
	}
}