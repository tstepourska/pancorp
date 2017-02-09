package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.xmlt.exception.InvalidMessageRefIdException;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.messagerefid.AbstractMessageRefId;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class ValidateMessageRefId extends AbstractMessageRefId {
	private static Logger lg = Logger.getLogger(GenerateMessageRefId.class);
	
	@Override
	public ValidateMessageRefId cloneTask(){
		ValidateMessageRefId t = new ValidateMessageRefId();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
	
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		lg.debug("ValidateMessageRefID executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			validateMessageRefId(p);		
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(InvalidMessageRefIdException e){
			status = Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID;
			Utils.logError(lg, e);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		//for wireframe testing only - to comment out!
		//status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
		
		return status;	
	}
}