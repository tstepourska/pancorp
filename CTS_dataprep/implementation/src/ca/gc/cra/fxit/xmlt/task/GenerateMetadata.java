package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.metadata.Helper;
import ca.gc.cra.fxit.xmlt.util.Constants;
public class GenerateMetadata extends AbstractTask {
	private static Logger logger = Logger.getLogger(GenerateMetadata.class);
	
	@Override
	public GenerateMetadata cloneTask(){
		GenerateMetadata t = new GenerateMetadata();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("GenerateMetadata executing");
		
		int status = Constants.STATUS_CODE_INCOMPLETE;
		Helper h = new Helper();
		status = h.invoke(p);
		//for wireframe testing only - to comment out!
		//status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
		
		return status;
	}	
}
