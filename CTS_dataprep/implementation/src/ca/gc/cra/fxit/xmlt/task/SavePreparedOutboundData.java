package ca.gc.cra.fxit.xmlt.task;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.IllegalFormatException;

import org.apache.log4j.Logger;

import ca.gc.cra.db.framework.exceptions.DataException;
import ca.gc.cra.fxit.xmlt.dao.UpdateRecordDAOBean;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

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
	
		int status = Constants.STATUS_CODE_INCOMPLETE;

		//zip XML adn metadata??
		//status = compressAll(p);
		
		// save into directory for further move to dataprep and CASD  to access
		//save in outbound/processed local directory
		//status = saveForDropzone(p);
		
		//save metadata and messageRefID and docRefIDs(PSN Infodec numbers) in the database
		//status = saveMetadataAndStatus(p);
		
		return status;
	}
	
	private int saveForDropzone(PackageInfo p){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String sourcePathName = null;
		String targetPathName = null;
		
		try {
			sourcePathName = Globals.FILE_WORKING_DIR+p.getXmlFilename();
			targetPathName = Globals.baseFileDir + Globals.OUTBOUND_PROCESSED_TOSEND_DIR + p.getXmlFilename();
			boolean success = Utils.moveFile(sourcePathName, targetPathName);
			if(!success)				
				throw new Exception("Error moving file " + sourcePathName);
			
			sourcePathName = Globals.FILE_WORKING_DIR+p.getMetadataFilename();
			targetPathName = Globals.baseFileDir + Globals.OUTBOUND_PROCESSED_TOSEND_DIR + p.getMetadataFilename();
			success = Utils.moveFile(sourcePathName, targetPathName);
			if(!success)				
				throw new Exception("Error moving file " + sourcePathName);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			log.error("saveForDropzone: Error: " + e.getMessage());
		}
		return status;
	}
}
