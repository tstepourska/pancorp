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
	public static final String STATUS_MESSAGE							= "StatusMessage";
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	public static final String SUFFIX_PAYLOAD							= "PL"; //used for outbound, starts from building XML/validating  and ends after its validation and saving it, before CASD
	//public static final String SUFFIX_PACKAGE							= "PK"; //used for outbound starts from digital signature and ends sending it off to CTS
	//public static final String SUFFIX_PRELIM							= "PRE"; // used for inbound, saves, uncompresses and validates metadata - before CASD
	//public static final String SUFFIX_UNPAK								= "UPK"; // used for inbound, decrypts, uncompreses payload, validates dig signature etc
	
	public static final String JAVA_PKG_TASK							= "ca.gc.cra.fxit.xmlt.task.";
	
	public static final String CANADA						= "CA";
	
	public static final String OUTBOUND_UNPROCESSED_TOSEND_DIR = "outbound/unprocessed/";
	public static final String OUTBOUND_PROCESSED_TOSEND_DIR = "outbound/processed/";
	//public static final String INBOUND_UNPROCESSED_DIR = "inbound/unprocessed/";
	//public static final String INBOUND_PROCESSED_DIR = "inbound/unprocessed/";
	public static final String TEMP_DIR					 = "temp/";
	
	
	//public static final String KEY_MAX_XML_FILE_SIZE		= "maxXmlFileSize";
	public static final String DEFAULT_MAX_PKG_SIZE_KEY		= "DefaultMaxPkgSize";
	public static final String SPECIFIC_MAX_FILE_SIZE_KEY	= "specificMaxFileSize";	
	public static final String SPECIFIC_MAX_SIZE_VALUE_KEY   = "value";		//attribute name
	public static final String SPECIFIC_MAX_SIZE_DEST_KEY   = "dest";		//attribute name
	public static final String SPECIFIC_MAX_SIZE_COMPRESSED_KEY   = "compressed";		//attribute name
	
	/**
	 * Value in bytes corresponding to tested maximum size of digital signature,
	 * metadata file, key file
	 */
	public static final String PKG_SIZE_CONSTANT_KEY			= "PkgSizeConstant";
	/**
	 * Value in percents of package compression ratio
	 */
	public static final String PKG_COMPRESSION_RATIO_KEY		= "PkgCompressionRatio";
	/**
	 *  Value in percents of XML payload compression ratio
	 */
	public static final String PAYLOAD_COMPRESSION_RATIO_KEY	= "PayloadCompressionRatio";
	public static final String TXT_TO_XML_FACTOR_KEY 			= "TxtToXmlFactor";
	public static final String FILE_SIGNATURE_SIZE_CONSTANTS_KEY = "FileSignatureSizeConstant";
	
	public static final int NO_SPLIT							= 1;
	
	public static final String MAIN_SCHEMA_NAME					= "main_schema.xsd";
	
	public static final String KEY_JOB_CONFIG					= "JobConfig";
	public static final String KEY_DATA_PROVIDERS				= "DataProviders";
	
	public static final int STATUS_CODE_SUCCESS 					= 0;
	public static final int STATUS_CODE_ERROR	 					= 9;
	public static final int STATUS_CODE_INVALID_INPUT_FILE			= 90000;	
	public static final int STATUS_CODE_FAILED_XML_TRANSFORMATION	= 90001;
	public static final int STATUS_CODE_NOT_FOUND					= 8;
	public static final int STATUS_CODE_CREATE_JOB_LOOP	    		= -98;
	public static final int STATUS_CODE_INCOMPLETE					= -99;
	public static final int STATUS_CODE_FILE_REJECTED_TOO_BIG		= -97;
	public static final int STATUS_CODE_FAILED_SCHEMA_VALIDATION	= 50007;
	public static final int STATUS_CODE_INVALID_MESSAGE_REF_ID		= 50008;
	public static final int STATUS_CODE_DUPLICATED_MESSAGE_REF_ID	= 50009;
	
	public static final String STATUS_MESSAGE_SUCCESS 				= "success";
	public static final String STATUS_MESSAGE_ERROR					= "error";
	
	//public static final String AUDIT								= "AUDIT: ";
	
	public static final String DEFAULT_ENCODING = "ISO-8859-1";  
	
	public static final String FILE_EXT_XML							= ".xml";
	public static final String METADATA								= "Metadata";
	public static final String MSG_REF_ID_PLACEHOLDER				= "MESSAGEREFID";
	public static final int MIX_TAX_YEAR							= 1970;
	////////////////////////////////////////////
	// out bound create package constants
	
	///////////////////////////
	/** Outbound FATCA payload xml file name */
	//public static final String CANADA_FATCA_PAYLOAD_FILE = "000000.00000.TA.124_0_Payload.xml";

	/** AES cryptographic Cipher transformation algorithm */
	// public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

	//public static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

	/** RSA transformation algorithm */
	//public static final String RSA_TRANSFORMATION = "RSA";

	/** Symmetric key algorithm */
	//public static final String SECRET_KEY_ALGO = "AES";

	/** Symmetric key size */
	//public static final int SECRET_KEY_SIZE = 256;

	public static final int maxAttempts = 5;

	public static int bufSize = 8 * 1024;
	
	public static final String UNDERSCORE		 = "_";
	public static final String ENV_TEST			 = "T";
	public static final String ENV_PROD			 = "P";

	//public Long fileId = 0L;
	//private ObjectFactory objFMetadata;
	public static SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSSz"); //'Z'
	public static SimpleDateFormat sdfFileCreateTs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss_z");
	public static SimpleDateFormat sdfStatusMessageTs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static SimpleDateFormat sdfMetadataTimestamp = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss.SSZ");
	public static SimpleDateFormat sdfMetadataYear = new SimpleDateFormat("YYYY");
	public static SimpleDateFormat sdfSweepTimeTs = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
	public static SimpleDateFormat sdfReportTs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	//public static final SimpleDateFormat runDateFormatter = new SimpleDateFormat("yyyyMMdd");
	//public final static SimpleDateFormat runTimeFormatter = new SimpleDateFormat("HHmmssSSS");
	
	//public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
//////////////////////////////////////////////////////////////////////////////
	// endo f outbound create package constants
	///////////////////////////////
	
	///////////////////////////////////
	//batch processisgn
	
	/**
     * FATCA Entity Sender ID for Canada. Value {@value}.
     */
	//public static final String FATCA_ENTITY_SENDER_ID_CANADA = "000000.00000.TA.124";
	/**
     * Transmitting country code. Use 2-character country code for Canada. Value {@value}.
     */
	//public static final String TRANSMITTING_COUNTRY_CODE = "CA";
	/**
     * Receiving country code. Use 2-character country code for United States. Value {@value}.
     */
	//public static final String RECEIVING_COUNTRY_CODE = "US";
	/**
     * PackageInfo direction code.
     */
	//public static final String OUTGOING_DIRECTION_CODE = "01";
	/**
     * Warning text for outgoing FATCA XML files. 
     * The exact wording of the warning text is provided by Canada's Competent Authority.
     */
	public static final String PACKAGE_WARNING_TEXT = "THIS INFORMATION IS FURNISHED UNDER THE PROVISIONS OF AN INCOME TAX TREATY WITH A FOREIGN GOVERNMENT. ITS USE AND DISCLOSURE MUST BE GOVERNED BY THE PROVISIONS OF THAT TREATY.";


	// end of batch processing
	//////////////////////////////////////
	
	/////////////////////////////////////
    /**
     * The US FATCA XML schema location in a format that the parser requires consisting of the  
     * namespace, and the name and location of schema. Note that there is a custom implementation
     * of the EntityResolver to resolve the location. 
     */
	//public static String FATCA_SCHEMA_LOCATION = "urn:oecd:ties:fatca:v1 FatcaXML_v1.1.xsd";

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
	public static final long TXT2XML_FACTOR = 3L;
	

	
    /**
     * Maximum number of occurrences of Residence Country Code. Value {@value}.
     * This is the maximum number of occurrences in the input file. The output is unbounded. 
     */
	public static final int MAX_RESIDENCE_COUNTRY_CODES = 5;

    /**
     * Maximum number of occurrences of Controlling Person per Account Report. Value {@value}.
     * This is the maximum number of occurrences of Controlling Person per Account Report in the input file. The output is unbounded. 
     */
	public static final int MAX_CONTROLLING_PERSONS = 16;

	/**
     * Number of digits after the decimal place in account balance and payment amounts. Value {@value}. 
     */
	public static final int MAX_DECIMAL_PLACES = 2;

	/**
	 * If true, use test DocTypeIndic codes. If false, use production codes.
	 * Test codes should ONLY be used to create FATCA XML files to be sent to IRS during system testing periods, 
	 * and not for PRODUCTION files to be sent to IRS. 
	 */
	public boolean useTestDocTypeIndicCodes = false;
    
	/**
     * FATCA Entity Sender ID for Canada. Value {@value}.
     */
	//public static final String FATCA_ENTITY_SENDER_ID_CANADA = "000000.00000.TA.124";
	/**
     * Transmitting country code. Use 2-character country code for Canada. Value {@value}.
     */
	//public static final String TRANSMITTING_COUNTRY_CODE = "CA";
	/**
     * Receiving country code. Use 2-character country code for United States. Value {@value}.
     */
	//public static final String RECEIVING_COUNTRY_CODE = "US";
	
	/**
     * FATCA form type in InfoDec. Value {@value}.
     */
	//public static final String FATCA_FORM_TYPE = "31";
	
	//public static final String MESSAGE_TYPE_CRS_DATA	= "CRS";
		

	// end of bridge
	//////////////////////////////////
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
