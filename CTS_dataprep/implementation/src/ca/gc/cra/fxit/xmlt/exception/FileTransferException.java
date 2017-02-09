package ca.gc.cra.fxit.xmlt.exception;


/**
 * This BridgeException is used by the CA2USBridge to report problems.
 *
 */
public class FileTransferException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String FTP_CONNECT_FAILED = "FTP_CONNECT_FAILED";
	public static final String FTP_LOGIN_FAILED = "FTP_LOGIN_FAILED";
	public static final String FTP_SET_FILE_TYPE_FAILED = "FTP_SET_FILE_TYPE_FAILED";
	public static final String FTP_CONNECT_LOGIN_EXCEEDED_RETRIES = "FTP_CONNECT_LOGIN_EXCEEDED_RETRIES";
	public static final String FTP_CONNECT_LOGIN_INTERRUPTED = "FTP_CONNECT_LOGIN_INTERRUPTED";
	public static final String FTP_LOCALDIR_DOES_NOT_EXIST = "FTP_LOCALDIR_DOES_NOT_EXIST";
	public static final String FTP_FILE_NOT_FOUND = "FTP_FILE_NOT_FOUND";
	public static final String FTP_TRANSFER_FAILED = "FTP_TRANSFER_FAILED";
	

	String transfer_exception_type;

	/**
	 * 
	 * @param type The type of the exception, is one of the define constants in this class
	 */
	public FileTransferException(String type) {
		super();
		transfer_exception_type = type;
	}

	public FileTransferException(String type, String message) {
		super(message);
		transfer_exception_type = type;
	}

	public FileTransferException(String type, String message, Throwable cause) {
		super(message, cause);
		transfer_exception_type = type;
	}

	public FileTransferException(String type, Throwable cause) {
		super(cause);
		transfer_exception_type = type;
	}
	
	public String getType() {
		return transfer_exception_type;
	}
	
}
