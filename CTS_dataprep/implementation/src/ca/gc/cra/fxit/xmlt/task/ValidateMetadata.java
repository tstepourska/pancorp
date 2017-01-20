package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class ValidateMetadata extends AbstractTask {
	private static Logger logger = Logger.getLogger(ValidateMetadata.class);
	
	@Override
	public ValidateMetadata cloneTask(){
		ValidateMetadata t = new ValidateMetadata();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("ValidateMetadata executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		
		return 0;
	}
}
