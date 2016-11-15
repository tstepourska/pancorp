package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.task.digitalsignature.DigitalSignatureHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class ApplyDigitalSignature extends AbstractTask {
	private static Logger logger = Logger.getLogger(ApplyDigitalSignature.class);
	
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("ApplyDigitalSignature executing");

		int status = new DigitalSignatureHelper(p).applyDigitalSignature();		
		return status;
	}

}