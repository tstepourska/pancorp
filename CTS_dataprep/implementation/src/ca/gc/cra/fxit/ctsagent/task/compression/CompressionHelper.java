/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.task.compression;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;

/**
 * EncryptionHelper zips signed XML payload preparing for further encryption 
 * Then it packages returned encrypted file and metadata into a single zip file as per IRS
 * requirement.
 * 
 * @author 
 */

public class CompressionHelper
{

	private static final Logger log = Logger.getLogger(CompressionHelper.class);
	private PackageInfo packageInfo;
	
	public CompressionHelper(PackageInfo p){
		this.packageInfo = p;
	}
	public int compress(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//TODO
		
		return status;
	}
	
	public int uncompress(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//TODO
		
		return status;
	}
}