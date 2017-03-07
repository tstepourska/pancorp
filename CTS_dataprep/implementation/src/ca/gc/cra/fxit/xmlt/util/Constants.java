package ca.gc.cra.fxit.xmlt.util;

import java.text.SimpleDateFormat;

import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.*;

public class Constants {
	public static final String JOB_OUTBOUND 							= "OUT";
	public static final String JOB_INBOUND 								= "IN";
	
	public static final String PKG_TYPE_DATA					 		= "DT";
	public static final String PKG_TYPE_STATUS					 		= "SM";
	
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	/*
	 * Careful with the two fields below!
	 * Used in the status message file name
	 */
	public static final String MSG_TYPE_MESSAGE_STATUS							= "MessageStatus";
	//used in the MessageSpec of the CRSStatusMessage as MessageTypeEnumType,
	//as per CRS Status Message User Guide - might be a typo!
	public static final String STATUS_MESSAGE									= "StatusMessage";
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	public static final String SUFFIX_PAYLOAD							= "PL"; //used for outbound, starts from building XML/validating  and ends after its validation and saving it, before CASD
	//public static final String SUFFIX_PACKAGE							= "PK"; //used for outbound starts from digital signature and ends sending it off to CTS
	//public static final String SUFFIX_PRELIM							= "PRE"; // used for inbound, saves, uncompresses and validates metadata - before CASD
	//public static final String SUFFIX_UNPAK								= "UPK"; // used for inbound, decrypts, uncompreses payload, validates dig signature etc
	
	public static final String JAVA_PKG_TASK							= "ca.gc.cra.fxit.xmlt.task.";
	public static final String RESOURCE_BASE_PKG						= "ca/gc/cra/fxit/xmlt/resources/";							
	
	public static final String CANADA								= "CA";
	
	public static final String OUTBOUND_UNPROCESSED_TOSEND_DIR 		= "outbound/unprocessed/";
	public static final String OUTBOUND_PROCESSED_TOSEND_DIR 		= "outbound/processed/";
	public static final String TEMP_DIR					 			= "temp/";
	
	
	//public static final String KEY_MAX_XML_FILE_SIZE				= "maxXmlFileSize";
	public static final String KEY_DEFAULT_MAX_PKG_SIZE				= "DefaultMaxPkgSize";
	public static final String KEY_SPECIFIC_MAX_FILE_SIZE			= "specificMaxFileSize";	
	public static final String KEY_SPECIFIC_MAX_SIZE_VALUE   		= "value";		//attribute name
	public static final String KEY_SPECIFIC_MAX_SIZE_DEST   		= "dest";		//attribute name
	public static final String KEY_SPECIFIC_MAX_SIZE_COMPRESSED   	= "compressed";		//attribute name
	
	/**
	 * Value in bytes corresponding to tested maximum size of digital signature,
	 * metadata file, key file
	 */
	public static final String KEY_PKG_SIZE_CONSTANT				= "PkgSizeConstant";
	/**
	 * Value in percents of package compression ratio
	 */
	//public static final String KEY_PKG_COMPRESSION_RATIO			= "PkgCompressionRatio";
	/**
	 *  Value in percents of XML payload compression ratio
	 */
	public static final String KEY_PAYLOAD_COMPRESSION_RATIO		= "PayloadCompressionRatio";
	public static final String KEY_TXT_TO_XML_FACTOR 				= "TxtToXmlFactor";
	public static final String KEY_FILE_SIGNATURE_SIZE_CONSTANT 	= "FileSignatureSizeConstant";
	public static final String KEY_FILE_SIZE_CONSTANT 				= "FileSizeConstant";
	
	public static final String KEY_JOB_CONFIG						= "JobConfig";
	public static final String KEY_DATA_PROVIDERS					= "DataProviders";
	
	public static final String KEY_LOG_LEVEL						= "log_level";
	
	public static final int NO_SPLIT								= 1;
	
	public static final String MAIN_SCHEMA_NAME						= "main_schema.xsd";
	
	public static final int STATUS_CODE_SUCCESS 					= 0;
	public static final int STATUS_CODE_ERROR	 					= 9;
	public static final int STATUS_CODE_INVALID_INPUT_FILE			= 90000;	
	//public static final int STATUS_CODE_FAILED_XML_TRANSFORMATION	= 90001;
	public static final int STATUS_CODE_NOT_FOUND					= 8;
	public static final int STATUS_CODE_CREATE_JOB_LOOP	    		= -98;
	public static final int STATUS_CODE_INCOMPLETE					= -99;
	public static final int STATUS_CODE_FILE_REJECTED_TOO_BIG		= -97;
	public static final int STATUS_CODE_FAILED_SCHEMA_VALIDATION	= 50007;
	public static final int STATUS_CODE_INVALID_MESSAGE_REF_ID		= 50008;
	public static final int STATUS_CODE_DUPLICATED_MESSAGE_REF_ID	= 50009;
	
	/**
	 * EEI Specific Status / Error Codes
	Outbound:
	10001		Transformation Failed   	Something fatal happened during transformation/file split /messageRefId generation, file cannot be processed 
	10002		Ready For Review			Outbound XML message is waiting for CASD to review and approve
	10003		Release Rejected			CASD rejected message for sending, file cannot be packaged for transmission
	10004		DataPrep Initiated			CASD triggered data prep 
	10005		DataPrep Failed				Something fatal happened during data prep, file cannot be processed
	10006		Package Sent				Data prep successfully completed and package has been pushed to the drop zone for transmitting to CTS
	10007		Accepted By OJ				Package has been accepted by the OJ without error
	10008 		Accepted By OJ with Error	Package has been accepted by the OJ with error (Record level error)
	10009 		Rejected By OJ  (file error)	Package has been rejected by the OJ (File level error)
	10010 		Rejected By OJ (other reasons)	Package has been rejected by the OJ (unknown reason)


	Inbound:
	10011		Package Received			Zipped package came from CTS
	10012		Ready for Review			After metadata validated, ready for CASD access
	10013		Delivery Approved			CASD triggered delivery to the data provider
	10014		Delivery Completed			After all data is in the drop zone for data provider and any additional info is provided  to them (such as MessageRefID/DocRefID mapping for accepted file)
	10015		Accepted By Canada			Package has been accepted by the data provider without error
	10016 		Accepted By Canada with Error		Package has been accepted by the Canadian data provider with error (Record level error)
	10017 		Rejected By Canada (File Error)  	Package has been rejected by the Canada (File level error)
	10018 		Rejected By Canada (Other Reason)  	Package has been rejected by the Canada (other reason)
	*/
	public static final int STATUS_CODE_TRANSOFRMATION_FAILED		 		= 10001;
	public static final int STATUS_CODE_OUT_READY_FOR_REVIEW		 		= 10002;
/*	public static final int STATUS_CODE_RELEASE_REJECTED		 			= 10003;
	public static final int STATUS_CODE_DATAPREP_INITIATED		 			= 10004;
	public static final int STATUS_CODE_DATAPREP_FAILED		 				= 10005;
	public static final int STATUS_CODE_PACKAGE_SENT		 				= 10006;
	public static final int STATUS_CODE_ACCEPTED_BY_OJ		 				= 10007;
	public static final int STATUS_CODE_ACCEPTED_BY_OJ_WITH_ERROR		 	= 10008;
	public static final int STATUS_CODE_REJECTED_BY_OJ		 				= 10009;
	public static final int STATUS_CODE_REJECTED_BY_OJ_OTHER		 		= 10010;
	public static final int STATUS_CODE_PACKAGE_RECEIVED		 			= 10011;
	public static final int STATUS_CODE_IN_READY_FOR_REVIEW		 			= 10012;
	public static final int STATUS_CODE_DELIVERY_APPROVED		 			= 10013;
	public static final int STATUS_CODE_DELIVERY_COMPLETED		 			= 10014;
	public static final int STATUS_CODE_ACCEPTED_BY_CANADA		 			= 10015;
	public static final int STATUS_CODE_ACCEPTED_BY_CANADA_WITH_ERROR		= 10016;
	public static final int STATUS_CODE_REJECTED_BY_CANADA_FILE_ERROR		= 10017;
	public static final int STATUS_CODE_REJECTED_BY_CANADA_OTHER		 	= 10018;
	*/
	
	public static final String STATUS_MESSAGE_SUCCESS 				= "success";
	public static final String STATUS_MESSAGE_ERROR					= "error";
	
	//public static final String AUDIT								= "AUDIT: ";
	
	public static final String DEFAULT_ENCODING 					= "ISO-8859-1";  
	
	public static final String FILE_EXT_XML							= ".xml";
	public static final String METADATA								= "Metadata";
	public static final String MSG_REF_ID_PLACEHOLDER				= "MESSAGEREFID";
	public static final int MIX_TAX_YEAR							= 1970;
	
	public static final String STATS_CFG							= "stats.cfg";
	
	public static final String CLEANUP_FILENAME						= "~";
	////////////////////////////////////////////
	// out bound create package constants

	public static final int maxAttempts 							= 5;

	public static int bufSize 										= 8 * 1024;
	
	public static final String UNDERSCORE		 					= "_";
	public static final String ENV_TEST								= "T";
	public static final String ENV_PROD			 					= "P";
	public static final String COMMA								= ",";

	public static SimpleDateFormat sdfFileName 				= new SimpleDateFormat("yyyyMMdd'T'HHmmssSSSz"); //'Z'
	public static SimpleDateFormat sdfFileCreateTs 			= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss_z");
	public static SimpleDateFormat sdfStatusMessageTs 		= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static SimpleDateFormat sdfMetadataTimestamp 	= new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.SSZ");
	public static SimpleDateFormat sdfMetadataYear 			= new SimpleDateFormat("YYYY");
	public static SimpleDateFormat sdfSweepTimeTs 			= new SimpleDateFormat("yyyyMMdd'T'HHmmss");
	public static SimpleDateFormat sdfReportTs 				= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	

	//////////////////////////////////////////////////////////////////////////////
	// end of outbound create package constants
	///////////////////////////////
	
	///////////////////////////////////
	//batch processisgn
	/**
     * Warning text for outgoing FATCA XML files. 
     * The exact wording of the warning text is provided by Canada's Competent Authority.
     */
	public static final String PACKAGE_WARNING_TEXT = "THIS INFORMATION IS FURNISHED UNDER THE PROVISIONS OF AN INCOME TAX TREATY WITH A FOREIGN GOVERNMENT. ITS USE AND DISCLOSURE MUST BE GOVERNED BY THE PROVISIONS OF THAT TREATY.";

	// end of batch processing
	//////////////////////////////////////
	
	/////////////////////////////////////

    /**
     * Value of TRANS-CD for PART XVIII Master header record: IP6PRTHD. Value {@value}.
     */
	//public static final int HDR_REC_TRANS_CD = 1001;
	public static final int LINE_CODE_HEADER = 1001;
    /**
     * Value of TRANS-CD for PART XVIII Summary record: IP6PRTSM. Value {@value}.
     */
	//public static final int FI_REC_TRANS_CD = 1002;
	public static final int LINE_CODE_FI = 1002;
    /**
     * Value of TRANS-CD for PART XVIII Sponsor record: IP6PRTSP. Value {@value}.
     */
	//public static final int SPONSOR_REC_TRANS_CD = 1003;
	public static final int LINE_CODE_SPONSOR = 1003;
    /**
     * Value of TRANS-CD for PART XVIII Slip record: IP6PRTSL. Value {@value}.
     */
	//public static final int SLIP_REC_TRANS_CD = 1004;
	public static final int LINE_CODE_SLIP = 1004;
    /**
     * Value of TRANS-CD for PART XVIII Controlling Person record: IP6PRTCP. Value {@value}.
     */
	//public static final int PERSON_REC_TRANS_CD = 1005;
	public static final int LINE_CODE_PERSON = 1005;
    /**
     * Value of TRANS-CD for PART XVIII Account Holder record: IP6PRTAC. Value {@value}.
     */
	//public static final int ACCOUNT_HOLDER_REC_TRANS_CD = 1006;
	public static final int LINE_CODE_ACCOUNT_HOLDER = 1006;
    /**
     * Value of TRANS-CD for PART XVIII Master trailer record: IP6PRTTR. Value {@value}.
     */
	//public static final int TLR_REC_TRANS_CD = 1007;
	public static final int LINE_CODE_TRAILER = 1007;

	//UPDATED MESSAGE REF ID - BACK TO INFODEC
    /**
     * Value of TRANS-CD for PACKAGE REFERENCE Master header record: IP6MSGHD. Value {@value}.
     */
	public static final int HDR_PKG_REF_REC_TRANS_CD = 1001;
    /**
     * Value of TRANS-CD for PACKAGE REFERENCE Summary record: IP6MSGSM. Value {@value}.
     */
	public static final int FI_PKG_REF_REC_TRANS_CD = 1002;
    /**
     * Value of TRANS-CD for PACKAGE REFERENCE Sponsor record: IP6MSGSP. Value {@value}.
     */
	public static final int SPONSOR_PKG_REF_REC_TRANS_CD = 1003;
    /**
     * Value of TRANS-CD for PACKAGE REFERENCE Slip record: IP6MSGSL. Value {@value}.
     */
	public static final int SLIP_PKG_REF_REC_TRANS_CD = 1004;
    /**
     * Value of TRANS-CD for PACKAGE REFERENCE Master trailer record: FATCATTR. Value {@value}.
     */
	public static final int TLR_PKG_REF_REC_TRANS_CD = 1007;

	/**
     * Value of RTN-SUMM-TCD for a file containing ORIGINAL reports. 
     * RTN-SUMM-TCD is in PART XVIII Master Header record: IP6PRTHD. Value {@value}.
     */
	public static final char ORIGINAL_RTN_SUMM_TCD = '1';
	/**
     * Value of RTN-SUMM-TCD for a file containing AMENDED reports. 
     * RTN-SUMM-TCD is in PART XVIII Master Header record: IP6PRTHD. Value {@value}.
     */
	public static final char AMENDED_RTN_SUMM_TCD = '2';
	/**
     * Value of RTN-SUMM-TCD for a file containing CANCELLED reports. 
     * RTN-SUMM-TCD is in PART XVIII Master Header record: IP6PRTHD. Value {@value}.
     */
	public static final char CANCELLED_RTN_SUMM_TCD = '5';
	/**
     * Defines the order in which files need to be processed. 
     * Process files containing ORIGINAL reports first, then AMENDED, then CANCELLED.
     */
	public static final char[] FILE_TYPES_IN_PROCESSING_ORDER = {ORIGINAL_RTN_SUMM_TCD, AMENDED_RTN_SUMM_TCD, CANCELLED_RTN_SUMM_TCD};

    /**
     * Minimum length of a cobol record. Use length of TRANS-CD field.
     */
	public static final int MIN_REC_LEN = new IP6PRTHD().getField("TransCd").getLength(); // 4 chars - digits
    /**
     * Expected length of PART XVIII Master header record: IP6PRTHD. Value {@value}.
     */
	//public static final int HDR_REC_LEN = new IP6PRTHD().length();
    /**
     * Expected length of PART XVIII Summary record: IP6PRTSM. Value {@value}.
     */
	//public static final int FI_REC_LEN = new IP6PRTSM().length();
    /**
     * Expected length of PART XVIII Sponsor record: IP6PRTSP. Value {@value}.
     */
	//public static final int SPONSOR_REC_LEN = new IP6PRTSP().length();
    /**
     * Expected length of PART XVIII Slip record: IP6PRTSL. Value {@value}.
     */
	//public static final int SLIP_REC_LEN = new IP6PRTSL().length();
    /**
     * Expected length of PART XVIII Controlling Person record: IP6PRTCP. Value {@value}.
     */
	//public static final int PERSON_REC_LEN = new IP6PRTCP().length();
    /**
     * Expected length of PART XVIII Account Holder record: IP6PRTAC. Value {@value}.
     */
	//public static final int ACCOUNT_HOLDER_REC_LEN = new IP6PRTAC().length();
    /**
     * Expected length of PART XVIII Master trailer record: IP6PRTTR. Value {@value}.
     */
	//public static final int TLR_REC_LEN = new IP6PRTTR().length();

    /**
     * Factor to apply to cobol record length to determine approximate length of XML. 
     */
	public static final long TXT2XML_FACTOR 			= 3L;
	

	
    /**
     * Maximum number of occurrences of Residence Country Code. Value {@value}.
     * This is the maximum number of occurrences in the input file. The output is unbounded. 
     */
	public static final int MAX_RESIDENCE_COUNTRY_CODES = 5;

    /**
     * Maximum number of occurrences of Controlling Person per Account Report. Value {@value}.
     * This is the maximum number of occurrences of Controlling Person per Account Report in the input file. The output is unbounded. 
     */
	public static final int MAX_CONTROLLING_PERSONS 	= 16;

	/**
     * Number of digits after the decimal place in account balance and payment amounts. Value {@value}. 
     */
	public static final int MAX_DECIMAL_PLACES 			= 2;

	/**
	 * If true, use test DocTypeIndic codes. If false, use production codes.
	 * Test codes should ONLY be used to create FATCA XML files to be sent to IRS during system testing periods, 
	 * and not for PRODUCTION files to be sent to IRS. 
	 */
	public boolean useTestDocTypeIndicCodes 			= false;
	
	/////////////////////////////////////////////////////////////////////////////
	// JNDI NAMES
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * JNDI name to lookup data directory.
	 */
	public static final String JNDI_BATCH_DATA_DIRECTORY 		= "ca/gc/cra/fxit/xmlt/env/basedir";  
	 /**
	  *  JNDI name to lookup directory name where unprocessed flat files will be picked up in the next batch run.
	  *  The directory will include files for the tax years for which original, amended, and cancel data is available.
	  */
	//public static final String JNDI_BATCH_UNPROCESSED_INPUT = "ca/gc/cra/fxit/ca2us/env/unprocessed";  
	 /**
	  *  JNDI name to lookup directory name where XML output files will be created.
	  */
	//public static final String JNDI_BATCH_XML_OUTPUT = "ca/gc/cra/fxit/ca2us/env/xml_output";  
	 /**
	  *  JNDI name to lookup directory name where flat file output files will be created.
	  */
	//public static final String JNDI_BATCH_FLAT_FILE_OUTPUT = "ca/gc/cra/fxit/ca2us/env/flat_file_output";  
	 /**
	  *  JNDI name to lookup directory name of local directory for file transfers.
	  */
	//public static final String JNDI_BATCH_FILE_TRANSFER_LOCALDIR = "ca/gc/cra/fxit/ca2us/env/file_transfer_localdir";  

	/**
	 * JNDI name to lookup log level. Valid Values: TRACE  DEBUG  INFO  WARN  ERROR  FATAL
	 * Refer to org.apache.commons.logging.Log interface for more information.
	 */
	public static final String JNDI_LOG_LEVEL 					= "ca/gc/cra/fxit/xmlt/env/log_level";

	/**
	 * JNDI name to lookup DocTypeIndic codes to use. Valid Values: TEST PRODUCTION
	 */
	public static final String JNDI_DOCTYPEINDIC_ENV 			= "ca/gc/cra/fxit/xmlt/env/docTypeIndicEnv";
	/**
	 * JNDI name to lookup host for file transfers from eBCI to mainframe.
	 */
	//public static final String JNDI_MAINFRAME_HOST = "ca/gc/cra/fxit/ca2us/env/mainframe/host";
	/**
	 * JNDI name to lookup userid for file transfers from eBCI to mainframe.
	 */
	//public static final String JNDI_MAINFRAME_USERID = "ca/gc/cra/fxit/ca2us/env/mainframe/userid";
	/**
	 * JNDI name to lookup encrypted password for file transfers from eBCI to mainframe.
	 */
	//public static final String JNDI_MAINFRAME_PASSWORD = "ca/gc/cra/fxit/ca2us/env/mainframe/password";
	
	/**
	 * JNDI name to lookup FXMT database environment: U (UT), S (SI), A (UA), O (OT), or P (PR)
	 */
	public final static String JNDI_FXMT_ENV   					= "ca/gc/cra/db/fxmt/environment";
	/**
	 * JNDI name to lookup EJB for FXMT database access.
	 */
	public final static String JNDI_FXMT_DATA_HOME   			= "ca/gc/cra/fxit/xmlt/data/RetrieveFxmtDataHome";
	
	/**
	 * JNDI name to lookup FXMT From address used for sending email.
	 */
	public final static String JNDI_FXIT_MAIL_FROM_ADDRESS   	= "ca/gc/cra/fxit/mail/FromAddress";
	
	/**
	 * JNDI name to lookup IRMS email address that will receive FTP confirmation from FXIT.
	 */
	public final static String JNDI_FXIT_MAIL_TO_ADDRESS   		= "ca/gc/cra/fxit/mail/ToAddressList";
	
	public final static String JNDI_FXIT_MAIL_SENDER_ADDRESS   	= "ca/gc/cra/fxit/mail/SenderAddressList";

	/**
	 * JNDI name to lookup flag that controls whether or not FXIT sends email.
	 */
	public final static String JNDI_FXIT_MAIL_SEND_FLAG   = "ca/gc/cra/fxit/mail/sendFlag";
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// END OF JNDI NAMES
	///////////////////////////////////////////////////////////////////////////////////////////////////

	//TODO
		public static final String getStatusMessage(int c){
			String msg;
			switch(c){
			case STATUS_CODE_SUCCESS:
				msg = STATUS_MESSAGE_SUCCESS;
				break;
			case STATUS_CODE_ERROR:
				msg= STATUS_MESSAGE_ERROR;
				break;
				default:
					msg= STATUS_MESSAGE_ERROR;
			}
			return msg;
		}
}
