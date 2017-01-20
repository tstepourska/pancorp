package ca.gc.cra.fxit.xmlt.exception;

import ca.gc.cra.fxit.xmlt.task.ITask;

public class TaskException extends Exception 
{
	
	private static final long serialVersionUID = -5658594327215216578L;
	private int status;
	private String message;
	private ITask task;
	
	public TaskException(int s, String msg, ITask t){
		this.status = s;
		this.message = msg;
		this.task = t;
	}
	
	public int getErrorCode(){
		return this.status;
	}
	
	public String getTask(){
		return this.task.getClass().getName();
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}
}
