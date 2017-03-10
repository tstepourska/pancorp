package ca.gc.cra.fxit.xmlt.job;

import java.rmi.RemoteException;
import java.util.IllegalFormatException;

import org.apache.log4j.Logger;

import ca.gc.cra.db.framework.exceptions.DataException;
import ca.gc.cra.fxit.xmlt.dao.InsertStatsDAOBean;
import ca.gc.cra.fxit.xmlt.dao.UpdateRecordDAOBean;
import ca.gc.cra.fxit.xmlt.exception.FileTransferException;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
//import ca.gc.cra.fxit.xmlt.util.FileTransfer;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class OnJobEnd {
	private static Logger lg = Logger.getLogger(OnJobEnd.class);
	
	public  void invoke(int status, PackageInfo p) {
		//update file processing status and save available statistics and mapping (or status message) 
		updateRec(status, p);

		//TODO tentative - to remove this method, as SSC script 
		// should be put in place to do this
		//status = archiveFiles(p);
		
		status = cleanup(p);
	}
	
	private void updateRec(int status, PackageInfo p) {
		String fp = "saveStats: ";
		//String messageRefID = null;
		try {
			//messageRefId
			UpdateRecordDAOBean dao = new UpdateRecordDAOBean();
			dao.invoke(status, p);
			lg.debug(fp + "record updated");
		} 
		catch (IllegalFormatException e) {
			lg.error(fp + "Caught IllegalFormatException: " + e.getMessage());
		}
		catch (IndexOutOfBoundsException e) {
			lg.error(fp + "Caught IndexOutOfBoundsException: " + e.getMessage());
		}
		catch (DataException e) {
			lg.error(fp + "Caught DataException: " + e.getMessage());
		}
		catch (RemoteException e) {
			lg.error(fp + "Caught RemoteException: " + e.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	
		//return status;
	}
	
	
	/**
	 * Write new flat file containing package reference identifier (MessageRefId in FATCA terminology)
	 * This file will be sent back to the mainframe to update each Part XVIII information return 
	 * in the InfoDec/IRMS database with the package reference identifier of the package 
	 * that the FATCA report (transformed from a Part XVIII information return) was included in.
	 * 
	 * @return
	 */
/*	private int archiveFiles(PackageInfo p) 	{
		
		
		long startTime = System.currentTimeMillis();
		log.debug("Begin transferFilesToMainframe");

		
		int nFilesTransferred = 0; 
		FileTransfer ft = new FileTransfer();
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {		
			nFilesTransferred = ft.doTransferAll();
		} catch (FileTransferException e) {
			Utils.logError(log, e);
		} catch (Throwable ex) {
			log.error("Exception occurred during the file transfer: " + ex.toString());
		}

		status = Constants.STATUS_CODE_SUCCESS;
		
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime; 
		
		log.debug("transferFilesToMainframe (number of files transferred: " + nFilesTransferred + ", elapsed time: " + elapsedTime + " milliseconds)");

		log.debug("End transferFilesToMainframe");
		
		return status;
	}*/
	
	private int cleanup(PackageInfo p){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		return status;
	}
}
