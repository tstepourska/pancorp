package ca.gc.cra.fxit.xmlt.task.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
/*
import ca.gc.cra.fxit.ca2us.integration.xml.FatcaDocTypeIndicEnumType;
import ca.gc.cra.fxit.ca2us.integration.xml.MessageSpecType;
import ca.gc.cra.fxit.ca2us.integration.xml.MessageTypeEnumType;
*/

/**
 * SAX content handler. Read outgoing FATCA XML and produce a summary to facilitate view and validate, 
 * and capture outgoing package identification.
 */
public class SAXContentHandler extends DefaultHandler {

	private StringBuffer buffer;
	//private MessageSpecType messageSpec;
    private long FATCACount = 0;
    private long reportingFICount = 0;
    private long sponsorCount = 0;
    private long accountReportCount = 0;
    private long docRefIdCount = 0;
	private String summaryReportFileName;
	private String inputFileName;
	private String xmlFileName;
	private String packageRefIdFileName;
	private long inputFileSize;
	private long xmlFileSize;
	private boolean reportingFI = false;
	private boolean sponsor = false;
	private boolean accountReport = false;
	private Map<String, Integer> totalReportingFIByGIIN;
//	private Map<FatcaDocTypeIndicEnumType, Integer> totalReportingFIByDocTypeIndic;
//	private Map<FatcaDocTypeIndicEnumType, Integer> totalSponsorByDocTypeIndic;
	//private Map<FatcaDocTypeIndicEnumType, Integer> totalAccountReportByDocTypeIndic;
	
	SAXContentHandler () {
		buffer = new StringBuffer();
		//messageSpec = new MessageSpecType();
		totalReportingFIByGIIN = new HashMap<String, Integer>();
		//totalReportingFIByDocTypeIndic = new HashMap<FatcaDocTypeIndicEnumType, Integer>();
	//	totalSponsorByDocTypeIndic = new HashMap<FatcaDocTypeIndicEnumType, Integer>();
	//	totalAccountReportByDocTypeIndic = new HashMap<FatcaDocTypeIndicEnumType, Integer>();
	}
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	
    	buffer.setLength(0); 
    	
    	if (localName.equals("FATCA")) {  
    		FATCACount++;
    	}
    	else if (localName.equals("ReportingFI")) {  
    		reportingFICount++;
    		reportingFI = true;
    		sponsor = false;
    		accountReport = false;
    	}
    	else if (localName.equals("Sponsor")) {  
    		sponsorCount++;
    		reportingFI = false;
    		sponsor = true;
    		accountReport = false;
    	}
    	else if (localName.equals("AccountReport")) {  
    		accountReportCount++;
    		reportingFI = false;
    		sponsor = false;
    		accountReport = true;
    	}
    }

    public void endElement(String uri, String localName, String qName)throws SAXException { 
    	if (localName.equals("TransmittingCountry")) {  
    		//messageSpec.setTransmittingCountry(buffer.toString());
    	}
    	else if (localName.equals("ReceivingCountry")) {  
        	//messageSpec.setReceivingCountry(buffer.toString());
    	}
    	else if (localName.equals("MessageType")) {
    		//MessageTypeEnumType messageType;
    		try {
    			//messageType = MessageTypeEnumType.fromValue(buffer.toString());
    		} 
    		catch (IllegalArgumentException ex) {
    			//messageType = null;
    		}
    		//if (messageType != null) {
    		//	messageSpec.setMessageType(messageType);
    		//}
    	}
    	else if (localName.equals("Warning")) {  
        	//messageSpec.setWarning(buffer.toString());
    	}
    	else if (localName.equals("Contact")) {  
        	//messageSpec.setContact(buffer.toString());
    	}
    	else if (localName.equals("MessageRefId")) {  
        	//messageSpec.setMessageRefId(buffer.toString());
    	}
    	else if (localName.equals("CorrMessageRefId")) {  
        	//messageSpec.getCorrMessageRefId().add(buffer.toString());
    	}
    	else if (localName.equals("ReportingPeriod")) {
	    	XMLGregorianCalendar reportingPeriodXML;
			try {
				reportingPeriodXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(buffer.toString());
			} catch (DatatypeConfigurationException e) {
				reportingPeriodXML = null;
			} 
			if (reportingPeriodXML != null) {
				//messageSpec.setReportingPeriod(reportingPeriodXML);
			}
    	}
    	else if (localName.equals("Timestamp")) {
	    	XMLGregorianCalendar timestampXML;
			try {
				timestampXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(buffer.toString());
			} catch (DatatypeConfigurationException e) {
				timestampXML = null;
			} 
			if (timestampXML != null) {
				//messageSpec.setTimestamp(timestampXML);
			}
    	}
    	else if (localName.equals("DocTypeIndic")) {
    		//FatcaDocTypeIndicEnumType docTypeIndic;
    		try {
    			//docTypeIndic = FatcaDocTypeIndicEnumType.fromValue(buffer.toString());
    		} 
    		catch (IllegalArgumentException ex) {
    			//docTypeIndic = null;
    		}
    	/*
	if (docTypeIndic != null) {
				int count = 1;
        		if (reportingFI == true) {
    				Integer value = totalReportingFIByDocTypeIndic.get(docTypeIndic);
    				if (value != null)
    					count = value.intValue() + 1;
       				totalReportingFIByDocTypeIndic.put(docTypeIndic, count);
        		}
        		if (sponsor == true) {
    				Integer value = totalSponsorByDocTypeIndic.get(docTypeIndic);
    				if (value != null)
    					count = value.intValue() + 1;
       				totalSponsorByDocTypeIndic.put(docTypeIndic, count);
        		}
        		if (accountReport == true) {
    				Integer value = totalAccountReportByDocTypeIndic.get(docTypeIndic);
    				if (value != null)
    					count = value.intValue() + 1;
       				totalAccountReportByDocTypeIndic.put(docTypeIndic, count);
        		}
    		}*/
    	}
    	else if (localName.equals("DocRefId")) {
    		docRefIdCount++;
    	}
    	else if (localName.equals("TIN")) {
    		if (reportingFI == true) {
				Integer value = totalReportingFIByGIIN.get(buffer.toString());
				int count = 1;
				if (value != null)
					count = value.intValue() + 1;
	   			totalReportingFIByGIIN.put(buffer.toString(), count);
    		}
    	}
    }
    
    @Override 

    public void characters(char[] ch, int start, int length) {  
    	
    	buffer.append(ch, start, length);  
    }  

	/*public MessageSpecType getMessageSpec() {
		return messageSpec;
	}*/
    
	public long getReportingFICount() {
		return reportingFICount;
	}

	public long getSponsorCount() {
		return sponsorCount;
	}

	public long getAccountReportCount() {
		return accountReportCount;
	}

	public String getSummaryReportFileName() {
		return summaryReportFileName;
	}

	public void setSummaryReportFileName(String summaryReportFileName) {
		this.summaryReportFileName = summaryReportFileName;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
		File file = new File(inputFileName);
		this.inputFileSize = file.length();
		file = null;
	}		

	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
		File file = new File(xmlFileName);
		this.xmlFileSize = file.length();
		file = null;
	}		

	public String getPackageRefIdFileName() {
		return packageRefIdFileName;
	}

	public void setPackageRefIdFileName(String packageRefIdFileName) {
		this.packageRefIdFileName = packageRefIdFileName;
	}

	/**
	 * Save the summary report to a file.
	 * 
	 */
	/*public void saveSummaryReport() throws IOException {

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(getSummaryReportFileName(), true));
			writer.newLine();
			
			writer.write("SUMMARY");
			writer.newLine();
			writer.write("Input File          : " + inputFileName);
			writer.newLine();
			writer.write("Input File Size     : " + inputFileSize + " bytes");
			writer.newLine();
			writer.write("FATCA XML File      : " + xmlFileName);
			writer.newLine();
			writer.write("FATCA XML File Size : " + xmlFileSize + " bytes");
			writer.newLine();
			writer.write("PKG-REF-ID Update File  : " + packageRefIdFileName);
			writer.newLine();
			writer.write("TransmittingCountry : " + messageSpec.getTransmittingCountry());
			writer.newLine();
			writer.write("ReceivingCountry    : " + messageSpec.getReceivingCountry());
			writer.newLine();
			writer.write("MessageType         : " + ((messageSpec.getMessageType() != null) ? messageSpec.getMessageType().toString() : ""));
			writer.newLine();
			writer.write("Warning             : " + messageSpec.getWarning());
			writer.newLine();
			writer.write("Contact             : " + messageSpec.getContact());
			writer.newLine();
			writer.write("MessageRefID        : " + messageSpec.getMessageRefId());
			writer.newLine();
			writer.write("CorrMessageRefID    : " + messageSpec.getCorrMessageRefId().toString());
			writer.newLine();
			writer.write("ReportingPeriod     : " + ((messageSpec.getReportingPeriod() != null) ? messageSpec.getReportingPeriod().toString() : ""));
			writer.newLine();
			writer.write("Timestamp           : " + ((messageSpec.getTimestamp() != null) ? messageSpec.getTimestamp().toString() : ""));
			writer.newLine();
			writer.newLine();
			writer.write("Number of FATCA elements         : " + FATCACount);
			writer.newLine();
			writer.write("Number of ReportingFI elements   : " + reportingFICount + " " + totalReportingFIByDocTypeIndic.toString());
			writer.newLine();
			writer.write("Number of non-blank, unique ReportingFI GIIN : " + totalReportingFIByGIIN.size());
			writer.newLine();
			writer.write("Number of Sponsor elements       : " + sponsorCount + " " + totalSponsorByDocTypeIndic.toString());
			writer.newLine();
			writer.write("Number of AccountReport elements : " + accountReportCount + " " + totalAccountReportByDocTypeIndic.toString());
			writer.newLine();
			writer.write("Number of DocRefId elements      : " + docRefIdCount);
			writer.newLine();
		} catch (IOException e) {
			throw e;
		}
		finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				// ignore
			}
		}
	}*/
}
