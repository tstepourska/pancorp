package ca.gc.cra.fxit.xmlt.task.xml.ftc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.sql.Timestamp;
//import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
//import org.apache.openjpa.lib.log.Log;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.task.xml.CommonXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.task.xml.ftc.JAXBTransformer;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTAC;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTCP;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTHD;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTSL;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTSM;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTSP;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);
	
	//why we need more than one header?
	private List<IP6PRTHD> headerRecList 		= null; 	
	private List<IP6PRTSM> fiRecList 			= null;	
//	private List<IP6PRTSL> slipRecList 			= null;
//	private List<IP6PRTAC> accountHolderRecList = null;
	//private List<Long> prt18RtnSizeList = null;
	//private long prt18RtnSize = 0L;
	private List<IP6PRTCP> personRecList 		= null;
	
	private CommonXMLStreamWriter writer = null;
	private JAXBTransformer transformer = null;
	private int nRecordsProcessed = 0;
	private int lineNum = 0;
	
	boolean hasSlip = false;
	boolean hasAccountHolder = false;
	
	//IP6PRTSM reportingFIRec = null;
	//IP6PRTSP sponsorRec = null;
	IP6PRTSL slipRec = null;
	IP6PRTAC accountHolderRec = null;

	
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
		String inputFile  = Globals.FILE_WORKING_DIR + p.getOrigFilename();
		String outputFile = Globals.FILE_WORKING_DIR + new Timestamp(System.currentTimeMillis()) + p.getOrigFilename() + ".xml";
		if(lg.isDebugEnabled())
			lg.debug(fp + "original file name: " + inputFile);
		
		String testIndicator = p.getTestIndicator();
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
					processSponsor(line);
					break;
				case Constants.LINE_CODE_SLIP:		//1004
					processSlip(line);
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
	
	/**
	 * Processes Header line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processHeader(String line) throws Exception {
		String fp = "processHeader: ";

		IP6PRTHD headerRec = new IP6PRTHD();
		if(line.length() != headerRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"Header record line length is not correct: " + line.length() + "!=" + headerRec.length());
		}
			
		headerRec.setRec(line);
		if(lg.isDebugEnabled())
		lg.debug(fp + "headerRec set");

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

		IP6PRTSM reportingFIRec = new IP6PRTSM();
		if(line.length()!=reportingFIRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"FI record line length is not correct: " + line.length() + "!=" + reportingFIRec.length());
		}
		
		reportingFIRec.setRec(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "reportingFIRec set");
		
		fiRecList.add(reportingFIRec);
		
		if (hasSlip && hasAccountHolder) { 
			writer.writeCharacters("\n");
			transformer.transformAccountReport(slipRec, accountHolderRec, personRecList, writer);
			nRecordsProcessed = nRecordsProcessed + 2 + personRecList.size();
			hasSlip = false;
			hasAccountHolder = false;
			
			//createIP6MSGSL(packageRecs, reportingFIRec, slipRec);
		}
		/*else if (hasSlip) {
			lg.error(fp+"Line " + (lineNum) + " missing ACCOUNT HOLDER record for SLIP.");
			throw new Exception("Line " + (lineNum) + " missing ACCOUNT HOLDER record for SLIP");
		}
		else if (hasAccountHolder) {
			lg.error(fp+"Line " + (lineNum) + " missing SLIP record for ACCOUNT HOLDER.");
			throw new Exception("Line " + (lineNum) 	+ " missing SLIP record for ACCOUNT HOLDER");
		}*/
		
		//prt18RtnSizeList.add(prt18RtnSize);
		//prt18RtnSize = FI_REC_LEN;
	}
	
	/**
	 * Processes Sponsor line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processSponsor(String line) throws Exception {
		String fp = "processSponsor: ";
		IP6PRTSP sponsorRec = new IP6PRTSP();
		if(line.length()!=sponsorRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"Sponsor record line length is not correct: " + line.length() + "!=" + sponsorRec.length());
		}
		
		sponsorRec.setRec(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "sponsorRec set");
		
		//prt18RtnSize += SPONSOR_REC_LEN;
		
		writer.writeCharacters("\n");
		transformer.transformSponsor(sponsorRec, writer);
		nRecordsProcessed++;
	}
	
	/**
	 * Processes Slip line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processSlip(String line) throws Exception {
		String fp = "processSlip: ";
		//IP6PRTSL slipRec = new IP6PRTSL();
		if(line.length()!=slipRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"Slip record line length is not correct: " + line.length() + "!=" + slipRec.length());
		}
		
		slipRec.setRec(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "slipRec set");
		
		if (hasSlip && hasAccountHolder) {
			writer.writeCharacters("\n");
			transformer.transformAccountReport(slipRec, accountHolderRec, personRecList, writer);
			nRecordsProcessed = nRecordsProcessed + 2 + personRecList.size();
			hasSlip = false;
			hasAccountHolder = false;
			
			personRecList.clear();
		}
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
		IP6PRTCP personRec = new IP6PRTCP();
		if(line.length()!=personRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"Person record line length is not correct: " + line.length() + "!=" + personRec.length());
		}
		
		personRec.setRec(line);
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
		//IP6PRTAC accountHolderRec = new IP6PRTAC();
		if(line.length()!=accountHolderRec.length()){
			lg.info(fp + "line: " + line);
			lg.error(fp+"AccountHolder record line length is not correct: " + line.length() + "!=" + accountHolderRec.length());
		}
		
		accountHolderRec.setRec(line);
		if(lg.isDebugEnabled())
			lg.debug(fp + "accountHolderRec set");
		
		hasAccountHolder = true;
		
		//prt18RtnSize += ACCOUNT_HOLDER_REC_LEN;
	}
	
	/**
	 * Processes Trailer line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void processTrailer(String line) throws Exception {
		String fp = "processTrailer: ";
		
		if (hasSlip && hasAccountHolder) { 
			writer.writeCharacters("\n");
			transformer.transformAccountReport(slipRec, accountHolderRec, personRecList, writer);
			nRecordsProcessed = nRecordsProcessed + 2 + personRecList.size();
			hasSlip = false;
			hasAccountHolder = false;
			
			//createIP6MSGSL(packageRecs, reportingFIRec, slipRec);
		}
		else if (hasSlip) {
			lg.error("Line " + (lineNum) + " missing ACCOUNT HOLDER record for SLIP.");
			throw new Exception("Line " + (lineNum) + " missing ACCOUNT HOLDER record for SLIP");
		}
		else if (hasAccountHolder) {
			lg.error("Line " + (lineNum) + " missing SLIP record for ACCOUNT HOLDER.");
			throw new Exception("Line " + (lineNum) 	+ " missing SLIP record for ACCOUNT HOLDER" );
		}
		
		//prt18RtnSizeList.add(prt18RtnSize);
	}
	
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
		p.setDataProvider("ftc");
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setOrigFilename(filename);
		
		Helper h = new Helper();
		int status = h.invoke(p);
		lg.info("Helper completed with status " + status);
	}

	@Override
	public String[] getSchemas() {
		String[] xsdpaths = new String[] {
				  Globals.schemaLocationBaseDir +"ftc/isofatcatypes_v1.1.xsd",
				  Globals.schemaLocationBaseDir +"ftc/oecdtypes_v4.2.xsd",
				  Globals.schemaLocationBaseDir +"ftc/stffatcatypes_v2.0.xsd",
				  Globals.schemaLocationBaseDir + "crs/" + Constants.MAIN_SCHEMA_NAME};
		return xsdpaths;
	}
}