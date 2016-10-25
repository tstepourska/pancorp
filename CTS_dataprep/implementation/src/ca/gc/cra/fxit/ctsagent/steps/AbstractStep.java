package ca.gc.cra.fxit.ctsagent.steps;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public abstract class AbstractStep implements IStep {
	protected int resultCode = -1;
	protected String resultMessage;
	public final int execute(PackageInfo p){
		int status = invoke(p);
		
		this.setResultCode(status);
		return status;
	}
	
	protected abstract int invoke(PackageInfo p);
	
	public int getResultCode(){
		return this.resultCode;
	}
	
	public String getResultMsg(){
		return this.resultMessage;
	}
	
	public void setResultCode(int c){
		this.resultCode = c;
	}
	
	public void setResultMessage(String s){
		this.resultMessage = s;
	}
	
}
