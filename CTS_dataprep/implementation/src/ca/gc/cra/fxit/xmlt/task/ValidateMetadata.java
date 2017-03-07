package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.metadata.Helper;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class ValidateMetadata extends AbstractTask {
	private static Logger logger = Logger.getLogger(ValidateMetadata.class);
	
	@Override
	public final int invoke(PackageInfo p) {
		logger.debug("ValidateMetadata executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
		String validatedFile = p.getFileWorkingDir() + p.getMetadataFilename();		//Globals.FILE_WORKING_DIR
		Helper h = new Helper();
		String[] xsdpaths = h.getSchemas();
		
		status = h.validate(p, xsdpaths, validatedFile);
		}
		
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
		}
		return status;
	}
	
	@Override
	public ValidateMetadata cloneTask(){
		ValidateMetadata t = new ValidateMetadata();
		t.setResultCode(this.resultCode);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
}
