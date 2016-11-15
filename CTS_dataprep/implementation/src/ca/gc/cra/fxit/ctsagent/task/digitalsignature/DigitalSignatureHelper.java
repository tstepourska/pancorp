package ca.gc.cra.fxit.ctsagent.task.digitalsignature;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 * 
 */
public class DigitalSignatureHelper
{
	private static Logger logger = Logger.getLogger(DigitalSignatureHelper.class); 
	private PackageInfo packageInfo;
	
	public DigitalSignatureHelper(PackageInfo p){
		this.packageInfo = p;
	}

	public int applyDigitalSignature() { 
		
		String fp = "applyDigitalSignature: ";
		logger.debug(fp + "started");
		int result = -1;
		return result;
	}
 
	public int validateDigitalSignature(){
		String fp = "validateDigitalSignature: ";
		logger.debug(fp + "started");
		int result = -1;
		return result;
	}
}