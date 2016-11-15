package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 * Saves data for manual interception
 * 
 * @author Txs285
 *
 */
public class SavePreparedInboundData extends AbstractTask {
	private static Logger log = Logger.getLogger(SavePreparedInboundData.class);
	@Override
	protected final int invoke(PackageInfo p) {
		log.debug("SavePreparedInboundData");

		return 0;
	}
}
