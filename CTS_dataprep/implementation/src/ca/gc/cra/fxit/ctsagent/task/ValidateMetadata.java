package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;

public class ValidateMetadata extends AbstractTask {
	private static Logger logger = Logger.getLogger(ValidateMetadata.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("ValidateMetadata executing");
		int status = Constants.STATUS_CODE_ERROR;
		
		return 0;
	}
}
