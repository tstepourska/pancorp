package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;


import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public abstract class AbstractTask implements ITask, Comparable<ITask> {
	private static Logger log = Logger.getLogger(AbstractTask.class);
	protected int resultCode = Constants.STATUS_CODE_INCOMPLETE;
	//protected String resultMessage="";
	protected String id;
	protected int sequence;
	protected String jobId;
	
	/**
	 * @return the jobId
	 */
	@Override
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	@Override
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * Allows to sort tasks by sequence number
	 */
	@Override
	public int compareTo(ITask t) throws NullPointerException, ClassCastException{
		int seq = t.getSequence();
		
		if(this.sequence==seq)
			return 0;
		else if(this.sequence<seq)
			return -1;
		else
			return 1;
	}
	
	/**
	 * Overrides object equals() method to highlight the differences in comparison criteria 
	 * between equals and compareTo methods, 
	 * so task.compareTo(anotherTask)==0 does not always mean task.equals(anotherTask)==true
	 */
	@Override
	public boolean equals(Object t) throws NullPointerException{
		if(this.sequence==((ITask)t).getSequence() && 
			this.getClass().getName().compareTo(t.getClass().getName())==0 &&
		   (this.id).compareTo(((ITask)t).getId())==0		)
			return true;
		
		return false;
	}
	
	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sequence
	 */
	@Override
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	@Override
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets called from TaskManager
	 */
	@Override
	public final int execute(PackageInfo p){
		//must set a status message/code describing the task in progress
		//begin();
		int status = invoke(p);
		
		//must set a status message/code describing the task completed and with what result
		//end(status);
		
		return status;
	}
	
	protected abstract int invoke(PackageInfo p);
	
	@Override
	public int getResultCode(){
		return this.resultCode;
	}
	
/*	@Override
	public String getResultMessage(){
		return this.resultMessage;
	}*/
	
	@Override
	public void setResultCode(int c){
		this.resultCode = c;
	}
	
/*	@Override
	public void setResultMessage(String s){
		this.resultMessage = s;
	}*/

	/**
	 * Overrides Object hashCode() method.
	 * For this application it is not used / important, however 
	 * should be kept in mind for future just in case
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		try {
		String clazz = this.getClass().getName();
		
		sb.append("\n").append(this.jobId).append(":")
		//.append(this.id).append(".")
		.append(this.sequence).append(".")
		.append(clazz.substring(clazz.lastIndexOf(".")+1));
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
		
		return sb.toString();
	}
}
