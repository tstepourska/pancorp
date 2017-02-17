package ca.gc.cra.fxit.xmlt.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.*;
import org.apache.commons.net.ftp.parser.*;
import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.exception.FileTransferException;

/**
 * FileTransfer copies files to mainframe. 
 */
public class FileTransfer {
	//Log log = LogFactory.getLog(FileTransfer.class);
	private static Logger log = Logger.getLogger(FileTransfer.class);

	/**
	 * Maximum number of connection attempts.
	 */
	public static final int MAX_CONNECT_LOGIN_RETRIES = 5;
	
	/**
	 * The rest period before a new login after the previous attempt failed.
	 */
	public static final int MILLISECONDS_BETWEEN_LOGIN_RETRIES = 30000; // 30 seconds

	/**
	 * FTP server timeout in milliseconds
	 */
	public static final int DATA_SERVER_TIMEOUT = 1000000;

	/**
	 * Maximum size of file that can be stored as disk file on the mainframe.
	 * Larger file will be stored as "virtual" tape file.
	 */
	public static final long MAX_DISK_FILE_SIZE = 50 * 1024 * 1024; // 50 MB

	/**
	 * Number of days that the mainframe would retain the packageRefId data file (2 years).
	 */
	public static final int PACKAGEREFID_RETENTION_PERIOD = 730; // days
	/**
	 * Number of days that the mainframe would retain the zip file containing the FATCA XML (5 years).
	 */
	public static final int XML_RETENTION_PERIOD = 1825; // days

	/**
	 * Maximum length, in bytes, of a record stored in the mainframe file. 
	 * IRMS team requested that the maximum record length be set to 1004 to match a common buffer length used in their COBOL programs.
	 * 
	 */
	public static final int MAX_LRECL = 1004; 
	/**
	 * Regular expression to match filenames of data sets containing the package reference identifier.
	 */
	public static final String TRANSFER_PACKAGEREFID_REGEX = "^\\s*IP.*X[0-9]{7}$";
	/**
	 * Regular expression to match filenames of zip archive files containing the FATCA XML.
	 */
	public static final String TRANSFER_ARCHIVE_REGEX = "^\\s*FX.*zip$";

	protected FTPClient ftpClient;   // Data FTP server
	

	public FileTransfer() {

	}

	/**
	 * Transfers all of the files which have been copied to the local directory, 
	 * and deletes the copy when the transfer has been successfully completed.  
	 *
	 * @throws     FileTransferException
	 */
	public int doTransferAll() throws FileTransferException {
	
		/// Globals.fileTransferLocalDirName);
		String localDirStr = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR;
		log.debug("localDirname : " + localDirStr); 
		File localDir = new File(localDirStr);
		if (localDir == null || !localDir.exists()) {
			throw new FileTransferException(FileTransferException.FTP_LOCALDIR_DOES_NOT_EXIST);
		}
		
		int nPackagesTransferred = 0;
		int nArchivesTransferred = 0;
		
		File[] packageRefIdfiles = localDir.listFiles(new FileTransferFilenameFilter(TRANSFER_PACKAGEREFID_REGEX));
		File[] archiveFiles = localDir.listFiles(new FileTransferFilenameFilter(TRANSFER_ARCHIVE_REGEX));
		
		List<String> targetDataSets = new ArrayList<String>();
		List<String> targetArchives = new ArrayList<String>();

		if (packageRefIdfiles != null) {
			log.debug("packageRefIdfiles.length : " + packageRefIdfiles.length);
		}
		else {
			log.debug("packageRefIdfiles is null ");
		}

		if (archiveFiles != null) {
			log.debug("archiveFiles.length : " + archiveFiles.length);
		}
		else {
			log.debug("archiveFiles is null ");
		}

		if ((packageRefIdfiles != null && packageRefIdfiles.length > 0) || (archiveFiles != null && archiveFiles.length > 0)) {
		    try {
				if (connectAndLoginFTPServer(Globals.mainframeHost, Globals.mainframeUserid, Globals.mainframePassword)) {
					
					for (int i = 0; i < packageRefIdfiles.length; i++) {
						String packageRefIdfilename = packageRefIdfiles[i].getName();
						packageRefIdfilename = packageRefIdfilename.trim();
						if (doTransfer(packageRefIdfiles[i].getAbsolutePath(), packageRefIdfilename, PACKAGEREFID_RETENTION_PERIOD, false)) {
							nPackagesTransferred++;
							targetDataSets.add(packageRefIdfilename);
							removeLocalFile(packageRefIdfiles[i]);
						}
					}
					setFileType(FTPClient.BINARY_FILE_TYPE);
					for (int i = 0; i < archiveFiles.length; i++) {
						String archiveFilename = archiveFiles[i].getName();
						archiveFilename = archiveFilename.trim();
						if (doTransfer(archiveFiles[i].getAbsolutePath(), archiveFilename, XML_RETENTION_PERIOD, true)) {
							nArchivesTransferred++;
							targetArchives.add(archiveFilename);
							removeLocalFile(archiveFiles[i]);
						}
					}
					
					//new SendEmail().sendEmail(subject, content); //targetDataSets, targetArchives);
				}
				
				
			} catch (FileTransferException e) {
				disconnectFTPServer(true);
				throw e;
			}
		}
		
		return nPackagesTransferred + nArchivesTransferred;
	}
	
	/**
	 * Creates an <code>FTPClient</code> object that connects to a remote server
	 * specified by the given parameters.  FTP transfer will be performed by the <code>FTPClient</code> object.
	 *
	 * @param  host       IP address of the FTP server
	 * @param  userId     userID to login the FTP server
	 * @param  password   password to login the FTP server
	 * @return            true if successfully connected and logged in, and false if not
	 */
	protected boolean connectAndLoginFTPServer(String host, String userId, String password) throws FileTransferException {
		boolean bLoggedIn = false;

		for (int i = 0; i < MAX_CONNECT_LOGIN_RETRIES; i++) {
		
			// Connect and login to remote server
			try {
				ftpClient = new FTPClient();
				ftpClient.connect(host);
	
				int replyCode = ftpClient.getReplyCode();   
	      		if (!FTPReply.isPositiveCompletion(replyCode)) {
	        		disconnectFTPServer(false);
	        		throw new FileTransferException(FileTransferException.FTP_CONNECT_FAILED, ftpClient.getReplyString());
	      		}
	      		
	      		bLoggedIn = ftpClient.login(userId, password);
				if (!bLoggedIn) {
	        		disconnectFTPServer(false);
					throw new FileTransferException(FileTransferException.FTP_LOGIN_FAILED);	
				}
				
				ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
				ftpClient.setDataTimeout(DATA_SERVER_TIMEOUT);
				break;	      					
			} catch (IOException e ) {
	      		disconnectFTPServer(false);
	      		if (i == MAX_CONNECT_LOGIN_RETRIES - 1) {
	       			throw new FileTransferException(FileTransferException.FTP_CONNECT_LOGIN_EXCEEDED_RETRIES, e);
	       		}
			}
			try { 
				Thread.sleep(MILLISECONDS_BETWEEN_LOGIN_RETRIES);
			} catch (InterruptedException e) {
	      		disconnectFTPServer(false);
				throw new FileTransferException(FileTransferException.FTP_CONNECT_LOGIN_INTERRUPTED, e);
			}
		}
		
		log.debug("bLoggedIn is: " + bLoggedIn);
		
		return bLoggedIn;
	}
	
	protected void setFileType( int fileType) throws FileTransferException {
		
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.setFileType(fileType);
			} catch (IOException e) {
				throw new FileTransferException(FileTransferException.FTP_SET_FILE_TYPE_FAILED);
			}
		}
	}
	
	/**
	 * Disconnects the remote FTP file server
	 */
	public void disconnectFTPServer(boolean bLogout) {
		if (ftpClient != null) {
			if (ftpClient.isConnected()) {
				try {
					if (bLogout) {
						ftpClient.logout();
					}
					ftpClient.disconnect();
				} catch (IOException e) {
					// Ignore
				}
			}
			ftpClient = null;
		}

	}
	
	/**
	 * Transfers the file. The target file name should already be named using the mainframe standard naming convention.
	 *
	 * @param  sourcePathName     full path name of the file to transfer
	 * @param  targetFileName     target name of the file on the mainframe 
	 * @param  retentionPeriod    number of days to retain the file
	 * @param  bIsZipArchive      true if the target file is a zip archive (binary file type); false for a dataset (ascii file type) 
	 * @throws FileTransferException
	 */
	protected boolean doTransfer(
			String sourcePathName, 
			String targetFileName, 
			int retentionPeriod,
			boolean bIsZipArchive) throws FileTransferException {

		// Set the remote system type
		ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_MVS));
		ftpClient.setParserFactory(new DefaultFTPFileEntryParserFactory());

		// Copy the source file to the mainframe
		File file = new File(sourcePathName);
		if (file == null || !file.exists()) {
			throw new FileTransferException(FileTransferException.FTP_FILE_NOT_FOUND);
		}
			
		StringBuffer siteCmd = new StringBuffer();
		if (bIsZipArchive) {
			// U = Undefined length (Archive file is in Zip (binary) format)
			// BLKSIZE = max bytes per block
			siteCmd.append("CYLINDER RECFM=U BLKSIZE=32760");
		}
		else {
			// VB: Variable length, blocked record.
			// Note: leave it to the mainframe to figure out the BLKSIZE, therefore, BLKSIZE is not set.
			siteCmd.append("CYLINDER RECFM=VB").append(" LRECL=").append(MAX_LRECL);
		}

		
		// Check the size of the file.  By default, mainframe file is stored on
		// disk, but if it's larger than certain size, we have to send it
		// to "virtual" tape (Agency mainframe policy).
		if (file.length() > MAX_DISK_FILE_SIZE) {
			// Set MVS tape file characteristics
			siteCmd.append(" UNIT=TAPE RETPD=").append(retentionPeriod);
		}
		else {
			// PRI = PRIMARY allocation size
			// SEC = SECONDARY allocation size; could be repeated 15 times
			// There are 56664 bytes per track, and 15 tracks per CYLINDER. We get approximately 849,960 bytes per CYLINDER.
			if (bIsZipArchive) {
				siteCmd.append(" PRI=30 SEC=5"); // primary allocation for a zip archive up to 25MB
			}
			else {
				siteCmd.append(" PRI=60 SEC=5"); // primary allocation for a dataset up to 50MB
			}
		}
		try {
			ftpClient.sendSiteCommand(siteCmd.toString());
		}
		catch (IOException e) {
			throw new FileTransferException(FileTransferException.FTP_TRANSFER_FAILED, e);
		}

		return storeFile(sourcePathName, toMVSFileName(targetFileName));
	}

	/**
	 * Copies the specified file to a remote server and renames it at the destination.
	 * Note: the MVS disk characteristics must be set (if necessary) prior to calling
	 * this method.
	 *
	 * @param  source     name of local file to be transferred
	 * @param  target     name of target file
	 * @return 
	 * @throws            FileTransferException
	 */
	protected boolean storeFile(String source, String target) throws FileTransferException {

		boolean bResult = false;
		
		log.debug("Source is " + source + ". Target is " + target);
		FileInputStream sourceStream = null;
		try {
			sourceStream = new FileInputStream(source);
		} catch (FileNotFoundException e) {
			throw new FileTransferException(FileTransferException.FTP_FILE_NOT_FOUND, e);
		}

		// Copy the file to remote server
		try {
			bResult = ftpClient.storeFile(target, sourceStream);
		} catch (IOException e) {
			throw new FileTransferException(FileTransferException.FTP_TRANSFER_FAILED, e);
		} finally {
			if(sourceStream != null) {
				try {
					sourceStream.close();
					sourceStream = null;
				} catch (IOException ex) {
					// ignore
				}
			}

		}

		if (bResult) {
			log.info("File transfer to mainframe successful. Source: " + source + ", Target: " + target);
		} else {
			log.error("File transfer to mainframe failed. Source: " + source + ", Target: " + target + ", Reply: " + ftpClient.getReplyString());
		}
		
		return bResult;
	}

	/**
	 * Converts the given file name to MVS format.
	 * The mainframe filename must follow the standard naming convention and
	 * the entire string must be enclosed in single quotes.
	 *
	 * @param  fileName  name of file to convert
	 * @return           file name in MVS format.
	 */
	protected String toMVSFileName(String fileName) {

		StringBuffer mvsFileName = new StringBuffer();
		mvsFileName.append("'").append(fileName).append("'");

		return mvsFileName.toString();
	}
	
	/**
	 * Move local file, which has been successfully transferred, to the output folder.
	 * 
	 * @param path
	 * @return
	 */
	private boolean removeLocalFile(File localFile) {

		boolean isDeleted = false; 

		try {
			isDeleted = localFile.isFile() && localFile.delete();
			if (isDeleted) {
				log.info("Local file deleted: '" + localFile.getPath() + "'");
			}
			else {
				log.error("Local file not deleted.");
			}
		}
		catch (SecurityException ex) {
			log.error("Local file not deleted. Insufficient permissions to delete the file. Exception: " + ex.toString());
		}
		catch (Throwable ex) {
			log.error("Local file not deleted. An exception occurred: " + ex.toString());
		}
		
		return isDeleted;
	}


}
