package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.task.digitalsignature.DigitalSignatureHelper;
import ca.gc.cra.fxit.ctsagent.util.Utils;
import ca.gc.cra.fxit.ctsagent.util.Constants;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

public class ValidateDigitalSignature extends AbstractTask {
	private static Logger logger = Logger.getLogger(ValidateDigitalSignature.class);
	@Override
	protected final int invoke(PackageInfo p) {
		logger.debug("ValidateDigitalSignature executing" );

		int status = new DigitalSignatureHelper(p).validateDigitalSignature();		
		return status;
	}

}
