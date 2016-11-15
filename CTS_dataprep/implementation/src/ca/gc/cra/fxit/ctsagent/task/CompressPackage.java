package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.task.compression.CompressionHelper;
//import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class CompressPackage extends AbstractTask {
	private static Logger logger = Logger.getLogger(CompressPackage.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("CompressPackage executing");
		int status = new CompressionHelper(p).compress();		
		return status;
		
	}
	
}
