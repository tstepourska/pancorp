package ca.gc.cra.fxit.ctsagent.steps;
//import java.util.Map;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.steps.compression.CompressionHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;

public class CompressPayload extends AbstractStep {
	private static Logger logger = Logger.getLogger(CompressPayload.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("CompressPayload executing");
		int status = new CompressionHelper(p).compress();		
		return status;
	}
}
