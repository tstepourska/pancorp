package ca.gc.cra.fxit.xmlt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConstants;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.*;

public class Utils {
	private static Logger log = Logger.getLogger(Utils.class);
	
	public static void logError(Logger lg, Exception e){
		lg.error("Error: " + e.getMessage());
  	  	if(lg.isDebugEnabled()){
  		  StackTraceElement[] trace = e.getStackTrace();
  		  if(trace!=null){
  			  for(StackTraceElement tLine : trace){
  				  lg.error(tLine);
  			  }
  		  }
  	  	}
	}
	
	public static void logError(Logger lg, String s){
		lg.error("Error: " + s);
	}
	
	/**
	 * @param year
	 * @return possible object is {@link XMLGregorianCalendar }
	 */
/*	public static XMLGregorianCalendar genTaxYear(int year) {
		XMLGregorianCalendar taxyear = null;
		try {
			taxyear = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar());
			taxyear.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setTime(DatatypeConstants.FIELD_UNDEFINED,
					DatatypeConstants.FIELD_UNDEFINED,
					DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setDay(DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setMonth(DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setYear(year);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taxyear;
	}*/
	
	public static XMLGregorianCalendar generateReportingPeriod(String year, String month, String day) throws Exception {
		//year must be valid
		int iYear = Integer.parseInt(year);
		//month and day might be null, then set defaults
		int iMon = 12;
		int iDay = 31;
		try { iMon = Integer.parseInt(month); }
		catch(Exception e){ iMon = 12; }
		
		try{ iDay = Integer.parseInt(day); }
		catch(Exception e){ iDay = 31; }
		
		XMLGregorianCalendar taxyear = null;
			taxyear = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			
			taxyear.setTime(DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED);
			
			taxyear.setDay  (iDay);
			taxyear.setMonth(iMon);
			taxyear.setYear (iYear);

		return taxyear;
	}
	
	public static XMLGregorianCalendar generateXMLTimestamp(long ts) throws Exception {
		String fp  = "generateXMLTimestamp: ";
		//TODO - to correct timestamp string
		XMLGregorianCalendar cal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(""+ts);
		if(log.isDebugEnabled())
		    log.debug(fp + "XML calelndar created: " + cal);

		return cal;
	}
	
	/*
	public static String createReportingPeriodFromStr (String taxYear) throws Exception {
			
		String reportingPeriod = null;
		   	
				//String taxationYearStr = taxYear.trim();
				if (!taxYear.isEmpty()) {
					// reporting period is the end of the year to which the data relates in the format YYYY-MM-DD
					reportingPeriod = taxYear + "-12-31";  
				}
				return reportingPeriod;
	}*/
	
	/**
	 * @param filename
	 */
	public static void deleteFile(String filename) {
		if (filename!=null && filename.trim().length()>0) {
			File file = new File(filename);
			int attempts = Constants.maxAttempts;
			while (file.exists() && !file.delete() && attempts-- > 0)
				Thread.yield();
		}
	}
	
	/**
	 * Delete file.
	 * Called by BatchProcessBean for removing processed input files
	 * 
	 * @param path
	 * @return
	 */
	public static boolean removeFile(
			File inputFile) {

		boolean isDeleted = false; 

		try {
			isDeleted = inputFile.isFile() && inputFile.delete();
			if (isDeleted) {
				log.info("File deleted: '" + inputFile.getPath() + "'");
			}
			else {
				log.error("File not deleted: '" + inputFile.getPath() + "'");
			}
		}
		catch (SecurityException ex) {
			log.error("File not deleted. Insufficient permissions to delete the file. Exception: " + ex.toString());
		}
		catch (Throwable ex) {
			log.error("File not deleted. An exception occurred: " + ex.toString());
		}
		
		return isDeleted;
	}
	
	/**
	 * Delete files.
	 * Called by BatchProcessBean for removing temporary and interim output files
	 * 
	 * @param filesToDelete
	 * @return
	 */
	public static void removeFiles(List<String> filesToDelete) {
	 
		for (String pathName : filesToDelete) {
			File file = new File(pathName);
			if (file != null) {
				removeFile(file);
			}
		}
	}

	
	/**
	 * 
	 * @param metadataFile
	 * @param payloadXML
	 * @throws Exception
	 */

	public static void renameFile(PackageInfo parameters,
			String metadataFile, String payloadXML) throws Exception {


	}
	
	/**
	 * 
	 * @param filename
	 * @return possible object is {@link String }
	 */

	public static String getFileName(String filename) {
		File f = new File(filename);
		return f.getName();
	}
	

	/**
	 *
	 */
	public static String getFileName()
			throws Exception {
		String xmlfilename = null;
		

			return xmlfilename;
		//}
	}

	

	/**
	 * @param fileDir
	 * @param senderGiin
	 * @return possible object is {@link String }
	 * @throws Exception
	 */

	public static String getIDESFileName(String fileDir, String senderGiin)
			throws Exception {
		String outfile = null;
		
		/*
		synchronized (fileId) {
			logger.debug("--> getIDESFileName(): senderGiin=" + senderGiin);
			Date date = Calendar.getInstance().getTime();
			// set the time zone to UTC
			sdfFileName.setTimeZone(TimeZone.getTimeZone("UTC"));
			String outfile = sdfFileName.format(date) + "_" + senderGiin
					+ ".zip";
			File file = new File(fileDir + outfile);
			int attempts = maxAttempts;
			while (!file.createNewFile() && attempts-- > 0) {
				outfile = sdfFileName.format(new Date()) + "_" + senderGiin
						+ ".zip";
				file = new File(fileDir + outfile);
			}
			if (attempts <= 0)
				throw new Exception("Unable to getFileName() - file="
						+ file.getAbsolutePath());
			logger.debug("<-- getIDESFileName()");
			*/
			return outfile;
		//}
	}


	/**
	 * Create a copy of the source file in the target location.
	 * 
	 * @param path
	 * @return
	 */
	public static boolean copyFile(String sourcePathName, String targetPathName) {
	 
		boolean isCopied = false;
		try {
			File source = new File(sourcePathName);
			File target = new File(targetPathName);
			FileUtils.copyFile(source, target);
			log.info("File copied. Source: " + sourcePathName + ", Target: " + targetPathName);
		}
		catch (IOException ex) {
			log.error("File not copied. An exception occurred: " + ex.toString());
		}
		
		return isCopied;
	}
	
	
	/**
	 * Create a data set file name for the PackageRefIdUpdate File that corresponds to the inputFileName 
	 * 
	 */	
	public static String createZipFileName(String inputFileName) {
		
		// replace IP.?IP?S182.CAUS with FX.?FX?S182.CAUS
		// append .zip
		String zipFileName =  inputFileName.replaceAll("IP", "FX") + ".zip";
		
		return zipFileName;
	}

	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	public static void sendEmail(String fromAddress,
								String toAddressList,
								String docTypeIndicEnv,
								String fxmtDatabaseEnv,
								StringBuilder emailBody){
	try {
		InitialContext ic = new InitialContext();
		Session session = (Session) ic.lookup("ca.gc.cra.fxit.mail.Session");
		
		// create a message
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(fromAddress));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddressList));
		
		msg.setSentDate(new Date());
		String emailSubject = "FXIT transfered PRT18 " + docTypeIndicEnv + " Files to the mainframe";
		msg.setSubject(emailSubject);
		
		String newLine = System.lineSeparator();
		//StringBuilder emailBody = new StringBuilder();
		/*emailBody.append("FXIT " + docTypeIndicEnv + " file transfer completed.");
		emailBody.append(newLine);
		emailBody.append("FXIT Database Environment: " + fxmtDatabaseEnv);
		emailBody.append(newLine);
		emailBody.append(newLine);
		emailBody.append("The following " + targetDataSets.size() + " data sets were successfully transfered to the mainframe for IRMS to load.");
		emailBody.append(newLine);
		for (String sFileName : targetDataSets) {
			emailBody.append(sFileName);
			emailBody.append(newLine);
		}
		emailBody.append(newLine);
		emailBody.append("The following " + targetArchives.size() + " archive files were successfully transfered to the mainframe for FATCA XML file retention purposes.");
		emailBody.append(newLine);
		for (String sFileName : targetArchives) {
			emailBody.append(sFileName);
			emailBody.append(newLine);
		}
		emailBody.append(newLine);
		emailBody.append("\nThis email is sent automatically by the eBCI FXIT batch application. eApplid=FXIT, componentID=ca2us"); 
		*/
		
		String content = emailBody.toString();
		msg.setContent(content,"text/plain; charset=UTF-8");   

		log.info("Sending email to " + toAddressList);
		
		log.debug("Email Subject    : " + msg.getSubject());
		log.debug("Email Body       : " + msg.getContent().toString());
		
		Transport.send(msg);
	}
	catch(Exception e){
		Utils.logError(log, e.getMessage());
	}
	}
	
	public static void cleanXmlFile(String path, String origFileName){
	
		String line = null;
		File file = null;
		File newfile = null;
		
		try {
			file = new File(path+origFileName);
			newfile = new File(path+origFileName +".OUTPUT");
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
		
		try (BufferedReader bReader= new BufferedReader(new FileReader(file));
			FileWriter writer = new FileWriter(newfile);
		){
			while((line = bReader.readLine())!=null){
				line = line.replaceAll("[^\\x20-\\x7e]", "");
				writer.write(line);
				writer.flush();
			}
		//String XString = writer.toString();
		//XString = XString.replaceAll("[^\\x20-\\x7e]", "");
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
	}
	
	public static String xmlToString(String path, String filename){
		
		String line = null;
		File file = null;
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		
		try {
			file = new File(path+filename);
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
		
		try (BufferedReader bReader= new BufferedReader(new FileReader(file));
		){
			while((line = bReader.readLine())!=null){
				sb.append(line);			
			}
			sb.append("|");
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		String filename = "fxit.ctsagent.batch.xml";	
		String path = "C:/git/repository/CTS_dataprep/implementation/cfg/";
		Utils.cleanXmlFile(path, filename);
	}
}