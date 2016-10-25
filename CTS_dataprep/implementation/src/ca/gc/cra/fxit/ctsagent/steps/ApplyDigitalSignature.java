package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.steps.digitalsignature.DigitalSignatureHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class ApplyDigitalSignature extends AbstractStep {
	private static Logger logger = Logger.getLogger(ApplyDigitalSignature.class);
	
	protected final int invoke(PackageInfo p) {
		logger.debug("ApplyDigitalSignature executing");

		int status = new DigitalSignatureHelper(p).applyDigitalSignature();		
		return status;
	}
}