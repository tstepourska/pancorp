package ca.gc.cra.fxit.xmlt.job;

import java.util.LinkedList;
import java.util.List;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class InitStatusMessage {

	private String fileAcceptanceStatus;
	private List<FileErrorType> fileErrors;
	private List<RecordErrorType> recordErrors;
	 
	public InitStatusMessage(String fas, List<FileErrorType> fe, List<RecordErrorType> re){
		this.fileAcceptanceStatus = fas;
		this.fileErrors = fe;
		this.recordErrors = re;
	}
	
	public int invoke(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		TaskManager tm = new TaskManager();
		LinkedList<ITask> job = createStatusMessageJob();
		
		//TODO
		PackageInfo p = null; //tm.invoke(pInfo)
		
		tm.executeJob(job, p);
		return status;
	}
	
	private LinkedList<ITask> createStatusMessageJob(){
		LinkedList<ITask> job = new LinkedList<>();
		
		//TODO
		
		return job;
	}
}
