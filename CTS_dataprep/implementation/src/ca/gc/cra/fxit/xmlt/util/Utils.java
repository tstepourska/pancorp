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

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CountryCodeType;
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
	
	public static String getDataProvider(String filename) throws Exception {
		String dp = null;
		
		for(int i=0;i<Globals.DATA_PROVIDERS.length;i++){
			if(filename.indexOf(Globals.DATA_PROVIDERS[i])>-1){
				dp = Globals.DATA_PROVIDERS[i];
				break;
			}
		}
			
		return dp;
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
		try { 
			iMon = Integer.parseInt(month); 
			try{ 
				iDay = Integer.parseInt(day); 
			}
			catch(Exception e){ 
				iDay = -1; 
			}
		}
		catch(Exception e){ 
			iMon = 12; 
			iDay = 31;
		}
	
		XMLGregorianCalendar taxyear = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			
			taxyear.setTime(DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			
			if(iDay>0)
				taxyear.setDay  (iDay);
			else
				taxyear.setDay(DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setMonth(iMon);
			taxyear.setYear (iYear);

		return taxyear;
	}
	
/*	public static XMLGregorianCalendar generateMetadataTaxYear(String year) throws Exception {
		log.debug("generateMetadataTaxYear: year: " + year);
		//year must be valid
		int iYear = Integer.parseInt(year);
		
		XMLGregorianCalendar taxyear = null;
			taxyear = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
			
			taxyear.setTime(DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			
			taxyear.setDay  (DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setMonth(DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setYear (iYear);

		return taxyear;
	}*/
	
	public static XMLGregorianCalendar generateMetadataTaxYear(XMLGregorianCalendar reportingPeriod) throws Exception {
		log.debug("generateMetadataTaxYear");
		//year must be valid
		//int iYear = Integer.parseInt(year);
		
		XMLGregorianCalendar taxyear = (XMLGregorianCalendar)reportingPeriod.clone(); // DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		log.debug("generateMetadataTaxYear: cloned reportingPeriod");
			taxyear.setTime(DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED,
							DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			
			taxyear.setDay  (DatatypeConstants.FIELD_UNDEFINED);
			taxyear.setMonth(DatatypeConstants.FIELD_UNDEFINED);
			//taxyear.setYear (iYear);

		return taxyear;
	}
	
	/**
	 * Creates XMLGregorianCalendar object which produces timestamp in a format 
	 * YYYY-MM-DD'T'hh:mm:ss.SSZ
	 * 
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public static XMLGregorianCalendar generateMetadataXMLTimestamp(long ts) throws Exception {
		Date dt = new Date(ts);
		log.debug("generateMetadataXMLTimestamp: date: " + dt);
		String strdate = Constants.sdfMetadataTimestamp.format(dt);
		log.debug("generateMetadataXMLTimestamp: stringdate: " + strdate);
		 XMLGregorianCalendar cal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(strdate);
		 log.debug("generateMetadataXMLTimestamp: cal: " + cal);
		 
		return cal;
	}
	
	/**
	 * Creates XMLGregorianCalendar object which produces timestamp in a format 
	 * YYYY-MM-DD'T'hh:mm:ss
	 * 
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public static XMLGregorianCalendar generateStatusMessageXMLTimestamp(long ts) throws Exception {
		Date dt = new Date(ts);
		log.debug("generateStatusMessageXMLTimestamp: date: " + dt);
		String strdate = Constants.sdfStatusMessageTs.format(dt);
		log.debug("generateStatusMessageXMLTimestamp: stringdate: " + strdate);
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(strdate);
		log.debug("generateStatusMessageXMLTimestamp: cal: " + cal);
		return cal;
	}
	
	/**
	 * Creates XMLGregorianCalendar object which produces timestamp in a format 
	 * YYYY-MM-DD'T'hh:mm:ss
	 * 
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public static XMLGregorianCalendar generateReportXMLTimestamp(long ts) throws Exception {
		Date dt = new Date(ts);
		log.debug("generateReportXMLTimestamp: date: " + dt);
		String strdate = Constants.sdfStatusMessageTs.format(dt);
		log.debug("generateReportXMLTimestamp: stringdate: " + strdate);
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(strdate);
		log.debug("generateReportXMLTimestamp: cal: " + cal);
		return cal;
	}
	
	/**
	 * Creates XMLGregorianCalendar object which produces timestamp in a format 
	 * YYYYMMDD'T'hhmmss
	 * 
	 * @param ts
	 * @return
	 * @throws Exception
	 */
	public static String generateSweepTimestamp(long ts) throws Exception {
		Date dt = new Date(ts);
		log.debug("generateSweepTimestamp: date: " + dt);
		String strdate = Constants.sdfSweepTimeTs.format(dt);
		log.debug("generateSweepTimestamp: stringdate: " + strdate);

		return strdate;
	}
		
	/**
	 * Creates XML filename in a format 
	 * 	<CRSStatus_CA16FR1234567890_2017022T3145012_U>
	 * If parameter appendExtention is true, appends .xml to the file name string
	 * 
	 * appendExtention is set to false when original file is split, and 
	 * a counter needs to be appended to each generated XML file
	 * 
	 * @param p
	 * @param appendExtention
	 * 
	 * @return String
	 * 
	 * @throws Exception
	 */
	public static String generateXMLFileName(PackageInfo p, boolean appendExtention) throws Exception {
		String fp = "generateXMLFileName: ";
		StringBuilder sb = new StringBuilder();
		//senderFileId format
		//CountryCdSender_CountryCdReceiver_CommunicationType_MessageRefID
		
		//messageRefId format (CRS):
		//<senderCountryCOde><TaxYear><receiverCountryCode><uniqueIdendifier>
		
		//CountryCodeType c = CountryCodeType.fromValue(p.getSendingCountry());
		//sb.append(c.value()).append(Constants.UNDERSCORE);
		
		//c = CountryCodeType.fromValue(p.getReceivingCountry());
		//sb.append(c.value()).append(Constants.UNDERSCORE);

		if(p.getOECDMessageType()!=null)
			sb.append(p.getOECDMessageType()).append(Constants.UNDERSCORE);
		else
			throw new Exception("Message type is not set!");
		
		//sb.append(p.getReportingPeriod().getYear()).append(Constants.UNDERSCORE);
		//placeholder for messageRefID
		sb.append(Constants.MSG_REF_ID_PLACEHOLDER).append(Constants.UNDERSCORE);
	
		if(p.getSweepTime()!=null)
			sb.append(p.getSweepTime()).append(Constants.UNDERSCORE);
		else
			throw new Exception("sweep time is not set!");
		
		if(p.getTestIndicator().equals(Constants.ENV_PROD))
			sb.append(Constants.ENV_PROD);
		else
			sb.append(Constants.ENV_TEST);
		
		if(appendExtention)
			sb.append(Constants.FILE_EXT_XML);
	
		return sb.toString();
	}
	
	public static String generateMetadataFilename(PackageInfo p, boolean appendExtention) throws Exception {
		String name = Constants.METADATA + Constants.UNDERSCORE+ generateXMLFileName(p,false);
		if(appendExtention)
			name = name + Constants.FILE_EXT_XML;
		
		return name;
	}
	
	
/*	public static String formatTimestamp(java.util.Date ts, SimpleDateFormat sdf){
		return sdf.format(ts);
	}*/
	
	public static String toUTC(java.util.Date ts){
		// that's for desktop application
	    // for web application one needs to detect Locale
	    Locale locale = Locale.CANADA; //getDefault();
	    // again, this one works for desktop application
	    // for web application it is more complicated
	    TimeZone currentTimeZone = TimeZone.getTimeZone("EST");//.getDefault();
	    // in fact I could skip this line and get just DateTime instance,
	    // but I wanted to show how to do that correctly for
	    // any time zone and locale
	    DateFormat formatter = DateFormat.getDateTimeInstance(
	            DateFormat.DEFAULT,
	            DateFormat.DEFAULT,
	            locale);
	    formatter.setTimeZone(currentTimeZone);
	    
	//    formatter.f

	    // Dates "conversion"
	  //  Date currentDate = new Date();
	   // long sixMonths = 180L * 24 * 3600 * 1000;
	  //  Date inSixMonths = new Date(currentDate.getTime() + sixMonths);

	    System.out.println(formatter.format(ts));
	   // System.out.println(formatter.format(inSixMonths));
	    // for me it prints
	    // 2011-05-14 16:11:29
	    // 2011-11-10 15:11:29

	    // now for "UTC"
	    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	    String utcTimestamp = formatter.format(ts);
	    System.out.println(formatter.format(ts));
	   // System.out.println(formatter.format(inSixMonths));
	    // 2011-05-14 14:13:50
	    // 2011-11-10 14:13:50
	    
	    return utcTimestamp;
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
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static boolean renameFile(String srcFile, String destFile) throws Exception {
		File source = new File(srcFile);
		File target = new File(destFile);
		FileUtils.copyFile(new File(srcFile),target);
		if(target.exists()){
			source.delete();
			if(source.exists())
				FileUtils.deleteQuietly(source);		
			return true;
		}
		
		return false;
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
	 * Move the source file into the target location.
	 * 
	 * @param path
	 * @return
	 */
	public static boolean moveFile(String sourcePathName, String targetPathName) {
	 
		boolean isMoved = false;
		try {
			File source = new File(sourcePathName);
			File target = new File(targetPathName);
			//FileUtils.moveFile(source, target);
			FileUtils.copyFile(source, target);

			if(target.exists()){
				if(log.isDebugEnabled())
				log.debug("moveFile: File created in the new destination. Source: " + sourcePathName + ", Target: " + targetPathName);
				isMoved = true;
			}
			else
				throw new Exception("moveFile: file not found in the target location"); 
			
			boolean deleted = FileUtils.deleteQuietly(source);
			log.debug("moveFile: source file deleted: " + deleted);
		}
		catch (IOException ex) {
			log.error("moveFile: File not copied. An IOException occurred: " + ex.toString());
		}
		catch (Exception ex) {
			log.error("moveFile: File not copied. An exception occurred: " + ex.toString());
		}	
		
		return isMoved;
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
		//Utils.cleanXmlFile(path, filename);
		//String ts = Utils.toUTC(new Date(System.currentTimeMillis()));
		String year = "2016";
		String month = null;
		String day = null;
		try {
		XMLGregorianCalendar cal = generateReportingPeriod(year,  month,  day);
		int yr = cal.getYear();
		log.info("year: " + yr);
		}
		catch(Exception e){
			
		}
	}
}