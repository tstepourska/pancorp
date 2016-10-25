package ca.gc.cra.fxit.ctsagent.util;

import java.io.File;
import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

//import com.bea.xbean.piccolo.xml.XMLStreamReader;

import ca.gc.cra.fxit.ctsagent.generated.metadata.FATCAIDESSenderFileMetadataType;
import ca.gc.cra.fxit.ctsagent.model.*;

public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class);
	
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
	public static XMLGregorianCalendar genTaxYear(int year) {
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
	}
	
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
				logger.info("File deleted: '" + inputFile.getPath() + "'");
			}
			else {
				logger.error("File not deleted: '" + inputFile.getPath() + "'");
			}
		}
		catch (SecurityException ex) {
			logger.error("File not deleted. Insufficient permissions to delete the file. Exception: " + ex.toString());
		}
		catch (Throwable ex) {
			logger.error("File not deleted. An exception occurred: " + ex.toString());
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
			logger.info("File copied. Source: " + sourcePathName + ", Target: " + targetPathName);
		}
		catch (IOException ex) {
			logger.error("File not copied. An exception occurred: " + ex.toString());
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
	
	
	
}