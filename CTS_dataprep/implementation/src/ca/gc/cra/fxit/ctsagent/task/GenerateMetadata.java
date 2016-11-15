package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class GenerateMetadata extends AbstractTask {
	private static Logger logger = Logger.getLogger(GenerateMetadata.class);
	
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("GenerateMetadata executing");
		
		return 0;
	}
	
}
