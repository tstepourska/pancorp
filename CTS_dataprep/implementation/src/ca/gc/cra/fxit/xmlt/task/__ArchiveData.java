package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.exception.FileTransferException;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.FileTransfer;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class __ArchiveData extends AbstractTask {
	private static Logger log = Logger.getLogger(__ArchiveData.class);
	
	@Override
	public __ArchiveData cloneTask(){
		__ArchiveData t = new __ArchiveData();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);

		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		log.debug("ArchiveData");
		
		String localDir=null;
		String mainframeHost=null;
		String mainframeUserId=null;
		String mainframePassword=null;
		
        // FTP files to mainframe
       //	transferFilesToMainframe(fileTransferLocalDir, host, userId, password);
		
		//TODO
		int status = transferFilesToMainframe();
		//log.debug("Archived : " + status);
		return 0;
	}
	
	/**
	 * Write new flat file containing package reference identifier (MessageRefId in FATCA terminology)
	 * This file will be sent back to the mainframe to update each Part XVIII information return 
	 * in the InfoDec/IRMS database with the package reference identifier of the package 
	 * that the FATCA report (transformed from a Part XVIII information return) was included in.
	 * 
	 * @return
	 */
	private int transferFilesToMainframe() 	{
		
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
	}

}