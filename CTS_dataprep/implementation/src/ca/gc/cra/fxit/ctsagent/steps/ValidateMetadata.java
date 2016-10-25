package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;

public class ValidateMetadata extends AbstractStep {
	private static Logger logger = Logger.getLogger(ValidateMetadata.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("ValidateMetadata executing");
		int status = Constants.STATUS_CODE_ERROR;
		
		return 0;
	}
}
