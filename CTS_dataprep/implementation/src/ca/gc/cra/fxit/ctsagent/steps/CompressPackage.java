package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.steps.compression.CompressionHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class CompressPackage extends AbstractStep {
	private static Logger logger = Logger.getLogger(CompressPackage.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("CompressPackage executing");
		int status = new CompressionHelper(p).compress();		
		return status;
		
	}
}
