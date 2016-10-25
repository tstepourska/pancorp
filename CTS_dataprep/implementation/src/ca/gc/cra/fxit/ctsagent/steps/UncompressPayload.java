package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.steps.compression.CompressionHelper;
import ca.gc.cra.fxit.ctsagent.util.AppProperties;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class UncompressPayload extends AbstractStep {
	private static Logger log = Logger.getLogger(UncompressPayload.class);
	@Override
	protected final int invoke(PackageInfo p) {
		log.debug("UncompressPayload executing");
		int status = new CompressionHelper(p).uncompress();		
		return status;
	}
}
