package ca.gc.cra.fxit.xmlt.job;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.model.PackageInfoFactory;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.task.ValidateMetadata;
import ca.gc.cra.fxit.xmlt.task.ValidateXML;
import ca.gc.cra.fxit.xmlt.util.*;

/**
 * @author Txs285
 */
public class ExternalJobInitiator {

	private static Logger lg = Logger.getLogger(ExternalJobInitiator.class);
	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	public int createStatusMessage(String dataProvider, 
									String origMessageRefId, 
									String fileAcceptanceStatus, 
									List<FileErrorType> fileErrors, 
									List<RecordErrorType> recordErrors, 
									String origCTSTransmissionId,
									String countryToSend,
									XMLGregorianCalendar origCTSSendingTimeStamp,
									String origSenderFileId,
									BigInteger origUncompressFileSizeKBQty,
									XMLGregorianCalendar reportingPeriod,
									String fileWorkingDir
									) {
		int status = Constants.STATUS_CODE_INCOMPLETE;		
		try {	
			//load configuration which includes jobs
			Globals.loadBatchProperties();
			//get the status message job from configuration
			LinkedList<ITask> job = Globals.getJob(Constants.JOB_OUTBOUND + Constants.UNDERSCORE + Constants.PKG_TYPE_STATUS);//"OUT_SM" createStatusMessageJob();
			//initialize the PackageInfo object
			PackageInfo p = PackageInfoFactory.createExternalSMPackageInfo(dataProvider, 
																			origMessageRefId, 
																			fileAcceptanceStatus,
																			fileErrors,
																			recordErrors,
																			origCTSTransmissionId,
																			countryToSend,
																			origCTSSendingTimeStamp,
																			origSenderFileId,
																			origUncompressFileSizeKBQty,
																			reportingPeriod,
																			fileWorkingDir);
			//create TaskManager object
			TaskManager tm = new TaskManager();
			//execute the job
			tm.executeJob(job, p);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		
		return status;
	}
	
	/**
	 * Invokes task of validating metadata file
	 * 
	 * @param dataProvider
	 * @param fileWorkingDir
	 * @param filename
	 * @return
	 */
	public int validateMetadataXML(String dataProvider, String fileWorkingDir, String filename) {
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			//initialize the PackageInfo object
			PackageInfo pi = PackageInfoFactory.createExtValidateMDPackageInfo(fileWorkingDir, filename, Constants.JOB_OUTBOUND);
			//create validating task
			ValidateMetadata v = new ValidateMetadata();		
			status = v.invoke(pi);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		return status;
	}

	/**
	 * Invokes task of validating of XML data file
	 * 
	 * @param dataProvider
	 * @param fileWorkingDir
	 * @param filename
	 * @return
	 */
	public int validateMessageXML(String dataProvider, String fileWorkingDir, String filename){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {
			//initialize the PackageInfo object
			PackageInfo pi = PackageInfoFactory.createPackageInfo(fileWorkingDir, filename, Constants.JOB_OUTBOUND);
			//create validating task
			ValidateXML v = new ValidateXML();
			status = v.invoke(pi);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		return status;
	}

	public int validateStatusMessageXML(String dataProvider, String fileWorkingDir, String filename){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {
			//initialize the PackageInfo object
			PackageInfo pi = PackageInfoFactory.createPackageInfo(fileWorkingDir, filename, Constants.JOB_OUTBOUND);
			//create validating task
			ValidateXML v = new ValidateXML();
			status = v.invoke(pi);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		return status;
	}

	public static void main(String[] args){
		//ExternalJobInitiator b = new ExternalJobInitiator();
		//String filename = "fxit.ctsagent.batch.xml";
		
		//String path = "C:/git/repository/CTS_dataprep/implementation/cfg/";
		
		//String xml = Utils.xmlToString(path, filename);
		//lg.info(xml);
		try {
			//b.execute(filename);
		//	Globals.loadBatchProperties();
			//b.loadBatchProperties(xml);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}
	
	
}
