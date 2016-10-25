package ca.gc.cra.fxit.ctsagent.steps;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.steps.encryption.EncryptionHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class Encrypt extends AbstractStep {
	private static Logger logger = Logger.getLogger(Encrypt.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("Encrypt executing");
		int status = new EncryptionHelper(p).encrypt();		
		return status;
	}
}
