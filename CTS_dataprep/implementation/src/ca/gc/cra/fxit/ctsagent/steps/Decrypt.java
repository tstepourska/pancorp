package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.steps.encryption.EncryptionHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class Decrypt extends AbstractStep {
	private static Logger logger = Logger.getLogger(Decrypt.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("Decrypt executing");
		int status = new EncryptionHelper(p).decrypt();
		
		return status;
	}
}
