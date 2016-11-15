package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.task.compression.CompressionHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
//import ca.gc.cra.fxit.ctsagent.util.Constants;

public class CompressPayload extends AbstractTask {
	private static Logger logger = Logger.getLogger(CompressPayload.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("CompressPayload executing");
		int status = new CompressionHelper(p).compress();		
		return status;
	}

}
