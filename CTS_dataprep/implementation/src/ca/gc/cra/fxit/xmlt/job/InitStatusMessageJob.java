package ca.gc.cra.fxit.xmlt.job;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.model.PackageInfoFactory;
import ca.gc.cra.fxit.xmlt.task.GenerateMessageRefId;
import ca.gc.cra.fxit.xmlt.task.GenerateMetadata;
import ca.gc.cra.fxit.xmlt.task.GenerateXML;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.task.SavePreparedOutboundData;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class InitStatusMessageJob {

	private String fileAcceptanceStatus;
	private List<FileErrorType> fileErrors;
	private List<RecordErrorType> recordErrors;
	private String origMessageRefId;
	private String dataProvider;
	private String origCTSTransmissionId;
	String countryToSend;
	XMLGregorianCalendar origCTSSendingTimeStamp;
	String origSenderFileId;
	BigInteger origUncompressFileSizeKBQty;
	XMLGregorianCalendar reportingPeriod;
	 
	public InitStatusMessageJob(String dp, 
							String mri, 
							String fas, 
							List<FileErrorType> fe, 
							List<RecordErrorType> re, 
							String tid,
							String country,
							XMLGregorianCalendar origCTSts,
							String oSenderFileId,
							BigInteger fSize,
							XMLGregorianCalendar repPeriod
							) throws Exception {
		this.fileAcceptanceStatus = fas;
		this.fileErrors = fe;
		this.recordErrors = re;
		this.origMessageRefId = mri;
		this.dataProvider = dp.toLowerCase();
		this.origCTSTransmissionId = tid;
		this.countryToSend = country;
		this.origCTSSendingTimeStamp = origCTSts;
		this.origSenderFileId = oSenderFileId;
		this.origUncompressFileSizeKBQty = fSize;
		this.reportingPeriod = repPeriod;
	}
	
	public int invoke(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		LinkedList<ITask> job = createStatusMessageJob();
		
		//TODO
		PackageInfo p = PackageInfoFactory.createStatusMessagePackageInfo(this.dataProvider, 
																		  this.origMessageRefId, 
																		  this.fileAcceptanceStatus,
																		  this.fileErrors,
																		  this.recordErrors,
																		  this.origCTSTransmissionId,
																		  this.countryToSend,
																		  this.origCTSSendingTimeStamp,
																		  this.origSenderFileId,
																		  this.origUncompressFileSizeKBQty,
																		  this.reportingPeriod);
		TaskManager tm = new TaskManager();
		tm.executeJob(job, p);
		return status;
	}
	
	private LinkedList<ITask> createStatusMessageJob(){
		LinkedList<ITask> job = new LinkedList<>();
		String jobId = "OUT_SM";
		ITask t;
		
		t= new GenerateXML();
		t.setId("generateStatusMessageXML");
		t.setJobId(jobId);
		t.setSequence(1);		
		job.add(t);
		
		t= new GenerateMessageRefId();
		t.setId("generateStatusMessageRefId");
		t.setJobId(jobId);
		t.setSequence(2);		
		job.add(t);
		
		t= new GenerateMetadata();
		t.setId("generateStatusMetadata");
		t.setJobId(jobId);
		t.setSequence(3);		
		job.add(t);
		
		t= new SavePreparedOutboundData();
		t.setId("savePreparedOutboundData");
		t.setJobId(jobId);
		t.setSequence(4);		
		job.add(t);
		
		return job;
	}
}
