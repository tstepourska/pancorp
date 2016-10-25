/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.steps.pushdata.etr;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.steps.pushdata.PushDataAbstractHelper;

/**
 * 
 * 
 * <a href="PushDataHelper.java.html"> <em>View Source</em> </a>
 * 
 * @author Txs285
 */
public class PushDataHelper extends PushDataAbstractHelper 
{

	private static final Logger log = Logger.getLogger(PushDataHelper.class);
	
	public PushDataHelper(PackageInfo p){
		this.packageInfo = p;
	}

	/**
	 *
	 */
	public int pushInbound() {
		int result = -1;
		//TODO
		return result;
	}
}
