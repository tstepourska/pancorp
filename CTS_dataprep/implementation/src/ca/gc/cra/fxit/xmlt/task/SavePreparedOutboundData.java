package ca.gc.cra.fxit.xmlt.task;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;

/**
 * Saves data for manual interception
 * 
 * @author Txs285
 *
 */
public class SavePreparedOutboundData extends AbstractTask {
	private static Logger log = Logger.getLogger(SavePreparedOutboundData.class);
	
	@Override
	public SavePreparedOutboundData cloneTask(){
		SavePreparedOutboundData t = new SavePreparedOutboundData();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		log.info("SavePreparedOutboundData executing");

		//save metadata and messageRefID and docRefIDs(PSN Infodec numbers) in the database
		
		//zip XML adn metadata
		
		// save into directory for dataprep and CASD  to access
		
		return 0;
	}
	
	private int saveData(PackageInfo p) throws SQLException, Exception {
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		//TODO
		//int sqlStatus = DataFactory.saveTransformedData();
		return status;
	}
}
