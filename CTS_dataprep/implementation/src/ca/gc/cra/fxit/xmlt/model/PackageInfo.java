package ca.gc.cra.fxit.xmlt.model;

import java.math.BigInteger;
import java.util.List;

import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileAcceptanceStatusEnumType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.util.Constants;

import javax.xml.datatype.XMLGregorianCalendar;

public class PackageInfo {
	
	private String testIndicator = Constants.ENV_TEST;
	
	private String OECDMessageType;
	
	/**
	 * Specfies direction of the transaction:
	 * inbound (I) or outbound (O)
	 */
	private String jobDirection;
	
	/**
	 * Additional job detail
	 */
	private String jobSuffix;
	
	private int splitFileCount = Constants.NO_SPLIT;
	private int splittedFileSeq = 0;

	/**
	 * Specifies type of the package processed:
	 * data (D) or status message (S)
	 */
	private String packageType;

	/**
	 * Tax year
	 * TODO   to merge tax year and reporting period
	 */
	private XMLGregorianCalendar reportingPeriod;
	private String taxYear; 
	
	private String sendingCountry;
	private String receivingCountry;

	//private String inputPathName;
	private String messageRefId;
	
	private String origFilename;
	
	//private String headerLine;
	
	private String dataProvider;
	
	/**
	 * Location of the XSD schema file
	 */
	//private String schemaPath;
	
	/**
	 * Location of the XML file
	 */
	private String xmlFilename;
	
	private String metadataFilename;
	
	private boolean fileRevisionIndic;
	
	/**
	 * Indicates CTS transmission type
	 * CRS
	 * CRSStatus
	 * ETR
	 * ETRStatus
	 * CBC
	 * CBCStatus
	 */
	private CTSCommunicationTypeCdType ctsCommunicationType;
	
	/**
	 * UTC_<MessageType>_<SenderCountryCd>.zip
	 */
	//private String senderFileId;
	
	//original messageRegID (1 item, for status message) 
	//or correctedMessageRefId(comma separated string for amendments or cancellations)
	private String origMessageRefId;
	private XMLGregorianCalendar origCTSSendingTimeStamp;
	// the CTS Transmission ID for the original transmission as sent by the sending Competent Authority
	private String origCTSTransmissionId;
	//the sender of the original transmission
	private String origSenderFileId;
	// the size of the decrypted, uncompressed CRS message
	private BigInteger origUncompressFileSizeKBQty;
	
	//Accepted or Rejected
	private FileAcceptanceStatusEnumType origFileAcceptanceStatus = null;
	
	List<FileErrorType> fileErrors = null;
	List<RecordErrorType> recordErrors = null;
	
	private boolean fileRevisionInd = false;
	
	private String sweepTime;
	
	/**
	 * @return the dataProvider
	 */
	public String getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(String dp) {
		this.dataProvider = dp;
	}

	/**
	 * @return the headerLine
	 */
/*	public String getHeaderLine() {
		return headerLine;
	}*/

	/**
	 * @param headerLine the headerLine to set
	 */
/*	public void setHeaderLine(String headerLine) {
		this.headerLine = headerLine;
	}*/

	/**
	 * @return the splitFileCount
	 */
	public int getSplitFileCount() {
		return splitFileCount;
	}

	/**
	 * @param splitFileCount the splitFileCount to set
	 */
	public void setSplitFileCount(int splitFileCount) {
		this.splitFileCount = splitFileCount;
	}


	/**
	 * @return the jobSuffix
	 */
	public String getJobSuffix() {
		return jobSuffix;
	}

	/**
	 * @param jobSuffix the jobSuffix to set
	 */
	public void setJobSuffix(String jobSuffix) {
		this.jobSuffix = jobSuffix;
	}


	/**
	 * @return the origFilename
	 */
	public String getOrigFilename() {
		return origFilename;
	}

	/**
	 * @param origFilename the origFilename to set
	 */
	public void setOrigFilename(String origFilename) {
		this.origFilename = origFilename;
	}

	/**
	 * @return the receivingCountry
	 */
	public String getReceivingCountry() {
		return receivingCountry;
	}

	/**
	 * @param receivingCountry the receivingCountry to set
	 */
	public void setReceivingCountry(String receivingCountry) {
		this.receivingCountry = receivingCountry;
	}

	/**
	 * @return the sendingCountry
	 */
	public String getSendingCountry() {
		return sendingCountry;
	}

	/**
	 * @param sendingCountry the sendingCountry to set
	 */
	public void setSendingCountry(String sendingCountry) {
		this.sendingCountry = sendingCountry;
	}
	
	
	/**
	 * @return the jobDirection
	 */
	public String getJobDirection() {
		return jobDirection;
	}

	/**
	 * @param jobDirection the jobDirection to set
	 */
	public void setJobDirection(String jobDirection) {
		this.jobDirection = jobDirection;
	}

	/**
	 * @return the taxYear
	 */
	public XMLGregorianCalendar getReportingPeriod() {
		return this.reportingPeriod;
	}

	/**
	 * @param taxYear the taxYear to set
	 */
	public void setReportingPeriod(XMLGregorianCalendar ty) {
		this.reportingPeriod = ty;
	}

	/**
	 * @return the taxYear
	 */
	public String getTaxYear() {
		return taxYear;
	}

	/**
	 * @param taxYear the taxYear to set
	 */
	public void setTaxYear(String taxYear) {
		this.taxYear = taxYear;
	}

	/**
	 * @return the xmlFilename
	 */
	public String getXmlFilename() {
		return xmlFilename;
	}

	/**
	 * @param xmlFilename the xmlFilename to set
	 */
	public void setXmlFilename(String xmlFName) {
		this.xmlFilename = xmlFName;
	}
	
	/**
	 * @return the schemaPath
	 */
/*	public String getSchemaPath() {
		return schemaPath;
	}
*/
	/**
	 * @param schemaPath the schemaPath to set
	 */
/*	public void setSchemaPath(String schemaPath) {
		this.schemaPath = schemaPath;
	}
*/
	public PackageInfo(){
		
	}	
	
	/**
	 * @return the pkType
	 */
	public String getPackageType() {
		return packageType;
	}

	/**
	 * @param pkType the pkType to set
	 */
	public void setPackageType(String pType) {
		this.packageType = pType;
	}

	public String getMessageRefId(){
		return this.messageRefId;
	}
	
	public void setMessageRefId(String s){
		this.messageRefId = s;
	}
	
	/*
public String getInputPathName(){
		return this.inputPathName;
	}
	
	public void setInputPathName(String s){
		this.inputPathName = s;
	}*/

	/**
	 * @return the ctsCommunicationType
	 */
	public CTSCommunicationTypeCdType getCtsCommunicationType() {
		return ctsCommunicationType;
	}

	/**
	 * @param ctsCommunicationType the ctsCommunicationType to set
	 */
	public void setCtsCommunicationType(
			CTSCommunicationTypeCdType ctsCommunicationType) {
		this.ctsCommunicationType = ctsCommunicationType;
	}

	/**
	 * @return the origCTSSendingTimeStamp
	 */
	public XMLGregorianCalendar getOrigCTSSendingTimeStamp() {
		return origCTSSendingTimeStamp;
	}

	/**
	 * @param origCTSSendingTimeStamp the origCTSSendingTimeStamp to set
	 */
	public void setOrigCTSSendingTimeStamp(XMLGregorianCalendar origCTSSendingTimeStamp) {
		this.origCTSSendingTimeStamp = origCTSSendingTimeStamp;
	}

	/**
	 * @return the origCTSTransmissionId
	 */
	public String getOrigCTSTransmissionId() {
		return origCTSTransmissionId;
	}

	/**
	 * @param origCTSTransmissionId the origCTSTransmissionId to set
	 */
	public void setOrigCTSTransmissionId(String origCTSTransmissionId) {
		this.origCTSTransmissionId = origCTSTransmissionId;
	}

	/**
	 * @return the origSenderFileId
	 */
	public String getOrigSenderFileId() {
		return origSenderFileId;
	}

	/**
	 * @param origSenderFileId the origSenderFileId to set
	 */
	public void setOrigSenderFileId(String origSenderFileId) {
		this.origSenderFileId = origSenderFileId;
	}

	/**
	 * @return the origUncompressFileSizeKBQty
	 */
	public BigInteger getOrigUncompressFileSizeKBQty() {
		return origUncompressFileSizeKBQty;
	}

	/**
	 * @param origUncompressFileSizeKBQty the origUncompressFileSizeKBQty to set
	 */
	public void setOrigUncompressFileSizeKBQty(BigInteger origUncompressFileSizeKBQty) {
		this.origUncompressFileSizeKBQty = origUncompressFileSizeKBQty;
	}

	/**
	 * @return the origMessageRefID
	 */
	public String getOrigMessageRefId() {
		return origMessageRefId;
	}

	/**
	 * @param origMessageRefID the origMessageRefID to set
	 */
	public void setOrigMessageRefId(String origMessageRefID) {
		this.origMessageRefId = origMessageRefID;
	}

	/**
	 * @return the origFileAcceptanceStatus
	 */
	public FileAcceptanceStatusEnumType getOrigFileAcceptanceStatus() {
		return origFileAcceptanceStatus;
	}

	/**
	 * @param origFileAcceptanceStatus the origFileAcceptanceStatus to set
	 */
	public void setOrigFileAcceptanceStatus(FileAcceptanceStatusEnumType origFileAcceptanceStatus) {
		this.origFileAcceptanceStatus = origFileAcceptanceStatus;
	}

	/**
	 * @return the senderFileId
	 */
/*	public String getSenderFileId() {
		return senderFileId;
	}*/

	/**
	 * @param senderFileId the senderFileId to set
	 */
/*	public void setSenderFileId(String senderFileId) {
		this.senderFileId = senderFileId;
	}*/

	/**
	 * @return the messageType
	 */
/*	public String getMessageType() {
		return messageType;
	}*/

	/**
	 * @return the splittedFileSeq
	 */
	public int getSplittedFileSeq() {
		return splittedFileSeq;
	}

	/**
	 * @param splittedFileSeq the splittedFileSeq to set
	 */
	public void setSplittedFileSeq(int splittedFileSeq) {
		this.splittedFileSeq = splittedFileSeq;
	}

	/**
	 * @return the fileErrors
	 */
	public List<FileErrorType> getFileErrors() {
		return fileErrors;
	}

	/**
	 * @param fileErrors the fileErrors to set
	 */
	public void setFileErrors(List<FileErrorType> fileErrors) {
		this.fileErrors = fileErrors;
	}

	/**
	 * @return the recordErrors
	 */
	public List<RecordErrorType> getRecordErrors() {
		return recordErrors;
	}

	/**
	 * @param recordErrors the recordErrors to set
	 */
	public void setRecordErrors(List<RecordErrorType> recordErrors) {
		this.recordErrors = recordErrors;
	}

	/**
	 * @return the fileRevisionIndic
	 */
	public boolean isFileRevisionIndic() {
		return fileRevisionIndic;
	}

	/**
	 * @param fileRevisionIndic the fileRevisionIndic to set
	 */
	public void setFileRevisionIndic(boolean fileRevisionIndic) {
		this.fileRevisionIndic = fileRevisionIndic;
	}

	/**
	 * @return the metadataFilename
	 */
	public String getMetadataFilename() {
		return metadataFilename;
	}

	/**
	 * @param metadataFilename the metadataFilename to set
	 */
	public void setMetadataFilename(String metadataFilename) {
		this.metadataFilename = metadataFilename;
	}

	/**
	 * @return the fileRevisionInd
	 */
	public boolean isFileRevisionInd() {
		return fileRevisionInd;
	}

	/**
	 * @param fileRevisionInd the fileRevisionInd to set
	 */
	public void setFileRevisionInd(boolean fileRevisionInd) {
		this.fileRevisionInd = fileRevisionInd;
	}

	/**
	 * @return the sweepTime
	 */
	public String getSweepTime() {
		return sweepTime;
	}

	/**
	 * @param sweepTime the sweepTime to set
	 */
	public void setSweepTime(String st) {
		this.sweepTime = st;
	}

	/**
	 * @return the testIndicator
	 */
	public String getTestIndicator() {
		return testIndicator;
	}

	/**
	 * @param testIndicator the testIndicator to set
	 */
	public void setTestIndicator(String testIndicator) {
		this.testIndicator = testIndicator;
	}

	/**
	 * @return the oECDMessageType
	 */
	public String getOECDMessageType() {
		return OECDMessageType;
	}

	/**
	 * @param oECDMessageType the oECDMessageType to set
	 */
	public void setOECDMessageType(String oECDMessageType) {
		OECDMessageType = oECDMessageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
/*	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}*/
	
	@Override
	public PackageInfo clone(){
		PackageInfo pi = new PackageInfo();
		
		pi.setCtsCommunicationType			(this.ctsCommunicationType);
		pi.setDataProvider					(this.dataProvider);
		//pi.setInputPathName					(this.inputPathName);
		pi.setJobDirection					(this.jobDirection);
		pi.setJobSuffix						(this.jobSuffix);
		pi.setMessageRefId					(this.messageRefId);
		pi.setOrigCTSSendingTimeStamp		(this.origCTSSendingTimeStamp);
		pi.setOrigCTSTransmissionId			(this.origCTSTransmissionId);
		pi.setOrigFileAcceptanceStatus		(this.origFileAcceptanceStatus);
		pi.setOrigFilename					(this.origFilename);
		pi.setOrigMessageRefId				(this.origMessageRefId);
		pi.setOrigSenderFileId				(this.origSenderFileId);
		pi.setOrigUncompressFileSizeKBQty	(this.origUncompressFileSizeKBQty);
		pi.setPackageType					(this.packageType);
		pi.setReceivingCountry				(this.receivingCountry);
		pi.setReportingPeriod				(this.reportingPeriod);
		pi.setSendingCountry				(this.sendingCountry);
		pi.setSplitFileCount				(this.splitFileCount);
		pi.setXmlFilename					(this.xmlFilename);
		pi.setSplittedFileSeq				(this.splittedFileSeq);
		
		pi.setOrigFileAcceptanceStatus		(this.origFileAcceptanceStatus);		
		pi.setFileErrors					(this.fileErrors);
		pi.setRecordErrors					(this.recordErrors);
		
		return pi;
	}
}
