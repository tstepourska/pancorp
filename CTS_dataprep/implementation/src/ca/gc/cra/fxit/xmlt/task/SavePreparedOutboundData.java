package ca.gc.cra.fxit.xmlt.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.IllegalFormatException;

import org.apache.commons.io.FileUtils;
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
	private static Logger lg = Logger.getLogger(SavePreparedOutboundData.class);
	
	@Override
	public SavePreparedOutboundData cloneTask(){
		SavePreparedOutboundData t = new SavePreparedOutboundData();
		t.setResultCode(this.resultCode);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		lg.info("SavePreparedOutboundData executing");
	
		int status = Constants.STATUS_CODE_INCOMPLETE;

		//zip XML adn metadata??
		//status = compressAll(p);
		
		// save into directory for further move to dataprep and CASD  to access
		//save in outbound/processed local directory
		status = saveForDropzone(p);
		lg.info("Saved for dropzone: " + status);
		//save metadata and messageRefID and docRefIDs(PSN Infodec numbers) in the database
		//status = saveMetadataAndStatus(p);
		
		cleanup(p);
		
		//TODO for wireframe testing only - to comment out!
		//status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
		
		return status;
	}
	
	private int saveForDropzone(PackageInfo p){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String sourcePathName = null;
		String targetPathName = null;
		
		try {
			String fileWorkingDir = p.getFileWorkingDir();		
			sourcePathName = fileWorkingDir + p.getXmlFilename();		//Globals.FILE_WORKING_DIR
			targetPathName = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getXmlFilename();
			boolean success = Utils.moveFile(sourcePathName, targetPathName);
			if(!success)				
				throw new Exception("Error moving file " + sourcePathName);
			
			sourcePathName = fileWorkingDir +p.getMetadataFilename();		//Globals.FILE_WORKING_DIR
			targetPathName = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getMetadataFilename();
			success = Utils.moveFile(sourcePathName, targetPathName);
			if(!success)				
				throw new Exception("Error moving file " + sourcePathName);
			
			sourcePathName = fileWorkingDir +p.getOrigFilename();		//Globals.FILE_WORKING_DIR
			targetPathName = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getOrigFilename();
			success = Utils.moveFile(sourcePathName, targetPathName);
			if(!success)				
				throw new Exception("Error moving file " + sourcePathName);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			lg.error("saveForDropzone: Error: " + e.getMessage());
		}
		return status;
	}
	
	private void cleanup(PackageInfo p){

		BufferedWriter w = null;
		BufferedReader r = null;
		boolean success = false;
		File file = null;
		
		
		//overwrite the old file with empty content
	/*	
		String originalFileName = fileWorkingDir +pkg.getOrigFilename();		//Globals.FILE_WORKING_DIR
		lg.info("originalFileName: " + originalFileName);
		
		try {
			FileWriter w = new FileWriter(originalFileName);
			w.write("");
			w.flush();
			w.close();
		}
		catch(Exception e){}
		*/
		String fileWorkingDir = null;
		try {
			fileWorkingDir = p.getFileWorkingDir();		
			String cleanupTarget = fileWorkingDir + Constants.CLEANUP_FILENAME;
			
			String sourcePathName = fileWorkingDir + p.getXmlFilename();		//Globals.FILE_WORKING_DIR
			//String target = fileWorkingDir + Constants.CLEANUP_FILENAME;
			if((new File(sourcePathName)).exists()){			
			w = new BufferedWriter(new FileWriter(sourcePathName,false));
			w.write("");
			w.flush();
			w.close();
			//success = Utils.moveFile(sourcePathName, cleanupTarget);		
			Utils.deleteFile(sourcePathName);
			}		
			sourcePathName = fileWorkingDir +p.getMetadataFilename();		//Globals.FILE_WORKING_DIR
			
			if((new File(sourcePathName)).exists()){
			w = new BufferedWriter(new FileWriter(sourcePathName,false));
			w.write("");
			w.flush();
			w.close();
			//success = Utils.moveFile(sourcePathName, cleanupTarget);
			Utils.deleteFile(sourcePathName);
			}
			
			sourcePathName = fileWorkingDir +p.getOrigFilename();
			if((new File(sourcePathName)).exists()){
				w = new BufferedWriter(new FileWriter(sourcePathName,false));
				w.write(" ");
				w.flush();
				w.close();
				//success = Utils.moveFile(sourcePathName, cleanupTarget);
				Utils.deleteFile(sourcePathName);
			}
		}
		catch(Exception e){
			//status = Constants.STATUS_CODE_ERROR;
			lg.error("cleanup: Error: " + e.getMessage());
		}
		finally{
			try {
				w.flush();
				w.close();
			} catch(Exception e){}
			
			try {
				r.close();
			} catch(Exception e){}
		}

	}
}
