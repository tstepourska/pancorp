package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;

public class ArchiveData extends AbstractTask {
	private static Logger log = Logger.getLogger(ArchiveData.class);
	
	@Override
	public ArchiveData cloneTask(){
		ArchiveData t = new ArchiveData();
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
		
		//TODO
	/*	int status = transferFilesToMainframe(
				localDir,
				mainframeHost, 
				mainframeUserId,
				mainframePassword
				);*/
		//log.debug("Archived : " + status);
		return 0;
	}
	
	/**
	 * Write new flat file containing package reference identifier (MessageRefId in FATCA terminology)
	 * This file will be sent back to the mainframe to update each Part XVIII information return 
	 * in the InfoDec/IRMS database with the package reference identifier of the package 
	 * that the FATCA report (transformed from a Part XVIII information return) was included in.
	 * 
	 * @param packageRefIdFileName
	 * @param rtnTxYr
	 * @param prt18SeqNbr
	 * @param prt18RtnSummTcd
	 * @param runDateTime
	 * @param packageRecs
	 * @return
	 */
/*	private boolean transferFilesToMainframe(
			String localDir,
			String mainframeHost, 
			String mainframeUserId,
			String mainframePassword) 
	{
		
		long startTime = System.currentTimeMillis();
		log.debug("Begin transferFilesToMainframe");

		int nFilesTransferred = 0; 
		FileTransfer ft = new FileTransfer(localDir, mainframeHost, mainframeUserId, mainframePassword);
		boolean isCompleted = false;
		try {
			
			nFilesTransferred = ft.doTransferAll();
		} catch (FileTransferException e) {
			log.error(translateDetailedMessage(e.getType(), e));
		} catch (Throwable ex) {
			log.error("Exception occurred during the file transfer: " + ex.toString());
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime; 
		
		log.debug("transferFilesToMainframe (number of files transferred: " + nFilesTransferred + ", elapsed time: " + elapsedTime + " milliseconds)");

		log.debug("End transferFilesToMainframe");
		
		return isCompleted;
	}*/
	
/*	private String translateDetailedMessage(String key, Throwable exc) {
		
		if(BridgeException.TRANSFORM_FAILED.equals(key)) {
			return "Transform to XML failed: " + getRootCauseMessage(exc);
		} else if(BridgeException.INPUT_FLAT_FILE_NOT_VALID.equals(key)) {
			return "The input file format is not valid: " + getRootCauseMessage(exc);
		} else if(BridgeException.CONFIGURATION_EXCEPTION.equals(key)) {
			return "Your environment doesn't support features required for this tool: " + getRootCauseMessage(exc);
		} else if(BridgeException.VALIDATION_FAILED.equals(key)) {
			return "The FATCA XML document is not valid: " + getRootCauseMessage(exc);
		} else if(BridgeException.OUTPUT_FLAT_FILE_ERROR.equals(key)) {
			return "Error encountered while creating output file containing PKG-REF-ID: " + getRootCauseMessage(exc);
		} else if(BridgeException.UNKNOWN_PACKAGEREFID.equals(key)) {
			return "Error encountered while creating package reference identifier: " + getRootCauseMessage(exc);
			
		} else if(FileTransferException.FTP_CONNECT_FAILED.equals(key)) {
			return "FTP server refused connection: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_LOGIN_FAILED.equals(key)) {
			return "FTP login failed: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_SET_FILE_TYPE_FAILED.equals(key)) {
			return "FTP set file type failed: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_CONNECT_LOGIN_EXCEEDED_RETRIES.equals(key)) {
			return "FTP login retries exceeded maximum retry attempts: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_CONNECT_LOGIN_INTERRUPTED.equals(key)) {
			return "FTP login retries exceeded maximum retry attempts: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_LOCALDIR_DOES_NOT_EXIST.equals(key)) {
			return "FTP local directory does not exist: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_FILE_NOT_FOUND.equals(key)) {
			return "FTP File not found: " + getRootCauseMessage(exc);
		} else if(FileTransferException.FTP_TRANSFER_FAILED.equals(key)) {
			return "FTP store file on mainframe failed: " + getRootCauseMessage(exc);
		}

		return "Unknown error";
	}*/
	
	private String getRootCauseMessage(Throwable exc) {
		String message = "";
		if( exc.getMessage() != null) message += exc.getMessage();
		
		if(exc.getCause() == null) return message;

		while(exc.getCause() != null) {
			exc = exc.getCause();
		}
		message += ":" + exc.getMessage();
		
		return message;
	}
	
}
