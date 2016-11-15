/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.task.pushdata;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 * 
 * 
 * <a href="PushDataAbstractHelper.java.html"> <em>View Source</em> </a>
 * 
 * @author Txs285
 */
public abstract class PushDataAbstractHelper 
{

	private static final Logger log = Logger.getLogger(PushDataAbstractHelper.class);
	
	protected PackageInfo packageInfo;

	/**
	 * Pushes data intended for CRA, coming from other jurisdictions
	 */
	public abstract int pushInbound();

	/**
	 * Pushes data intended for other jurisdictions to CTS/SSC
	 */
	public int pushOutbound() 
					{
		int status = -1;
		//TODO
		
		return status;
	}

}
