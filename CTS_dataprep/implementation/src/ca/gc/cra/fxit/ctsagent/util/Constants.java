package ca.gc.cra.fxit.ctsagent.util;

import java.text.SimpleDateFormat;

import ca.gc.cra.fxit.ctsagent.generated.cob2java.ftc.*;

public class Constants {
	public static final String JOB_OUTBOUND 							= "O";
	public static final String JOB_INBOUND 								= "I";
	
	public static final String PKG_TYPE_DATA					 = "D";
	public static final String PKG_TYPE_STATUS					 = "S";
	
	public static final String SUFFIX_PAYLOAD							= "_PL"; //starts from building XML and ends after its validation and saving it for CASD
	public static final String SUFFIX_PACKAGE							= "_PK"; // starts from digital signature and ends sending it off to CTS
	public static final String JAVA_PKG_STEP							= "ca.gc.cra.fxit.ctsagent.steps.";
	
	public static final String CANADA						= "CA";
	
	public static final String KEY_MAX_XML_FILE_SIZE		= "maxXmlFileSize";
	
	
	public static final int STATUS_CODE_SUCCESS 		= 0;
	public static final int STATUS_CODE_ERROR	 	= 9;
	public static final int STATUS_CODE_NOT_FOUND	= 8;
	public static final int STATUS_CODE_INCOMPLETE			= -99;
	public static final String STATUS_MESSAGE_SUCCESS 		= "success";
	public static final String STATUS_MESSAGE_ERROR			= "error";
	
	////////////////////////////////////////////
	// out bound create package constants
	
	///////////////////////////
	/** Outbound FATCA payload xml file name */
	public static final String CANADA_FATCA_PAYLOAD_FILE = "000000.00000.TA.124_0_Payload.xml";

	/** AES cryptographic Cipher transformation algorithm */
	// public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

	public static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

	/** RSA transformation algorithm */
	public static final String RSA_TRANSFORMATION = "RSA";

	/** Symmetric key algorithm */
	public static final String SECRET_KEY_ALGO = "AES";

	/** Symmetric key size */
	public static final int SECRET_KEY_SIZE = 256;

	public static final int maxAttempts = 5;

	public static int bufSize = 8 * 1024;

	//public Long fileId = 0L;
	//private ObjectFactory objFMetadata;
	public static SimpleDateFormat sdfFileName = new SimpleDateFormat(
			"yyyyMMdd'T'HHmmssSSS'Z'");
	public static SimpleDateFormat sdfFileCreateTs = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
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

	
	
	
	
	
	// end of b atch pr ocessing
	//////////////////////////////////////
	
	/////////////////////////////////////
    /**
     * The US FATCA XML schema location in a format that the parser requires consisting of the  
     * namespace, and the name and location of schema. Note that there is a custom implementation
     * of the EntityResolver to resolve the location. 
     */
	public static String FATCA_SCHEMA_LOCATION = "urn:oecd:ties:fatca:v1 FatcaXML_v1.1.xsd";

    /**
     * Value of TRANS-CD for PART XVIII Master header record: IP6PRTHD. Value {@value}.
     */
	public static final String HDR_REC_TRANS_CD = "1001";
    /**
     * Value of TRANS-CD for PART XVIII Summary record: IP6PRTSM. Value {@value}.
     */
	public static final String FI_REC_TRANS_CD = "1002";
    /**
     * Value of TRANS-CD for PART XVIII Sponsor record: IP6PRTSP. Value {@value}.
     */
	public static final String SPONSOR_REC_TRANS_CD = "1003";
    /**
     * Value of TRANS-CD for PART XVIII Slip record: IP6PRTSL. Value {@value}.
     */
	public static final String SLIP_REC_TRANS_CD = "1004";
    /**
     * Value of TRANS-CD for PART XVIII Controlling Person record: IP6PRTCP. Value {@value}.
     */
	public static final String PERSON_REC_TRANS_CD = "1005";
    /**
     * Value of TRANS-CD for PART XVIII Account Holder record: IP6PRTAC. Value {@value}.
     */
	public static final String ACCOUNT_HOLDER_REC_TRANS_CD = "1006";
    /**
     * Value of TRANS-CD for PART XVIII Master trailer record: IP6PRTTR. Value {@value}.
     */
	public static final String TLR_REC_TRANS_CD = "1007";

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
	public static final int HDR_REC_LEN = new IP6PRTHD().length();
    /**
     * Expected length of PART XVIII Summary record: IP6PRTSM. Value {@value}.
     */
	public static final int FI_REC_LEN = new IP6PRTSM().length();
    /**
     * Expected length of PART XVIII Sponsor record: IP6PRTSP. Value {@value}.
     */
	public static final int SPONSOR_REC_LEN = new IP6PRTSP().length();
    /**
     * Expected length of PART XVIII Slip record: IP6PRTSL. Value {@value}.
     */
	public static final int SLIP_REC_LEN = new IP6PRTSL().length();
    /**
     * Expected length of PART XVIII Controlling Person record: IP6PRTCP. Value {@value}.
     */
	public static final int PERSON_REC_LEN = new IP6PRTCP().length();
    /**
     * Expected length of PART XVIII Account Holder record: IP6PRTAC. Value {@value}.
     */
	public static final int ACCOUNT_HOLDER_REC_LEN = new IP6PRTAC().length();
    /**
     * Expected length of PART XVIII Master trailer record: IP6PRTTR. Value {@value}.
     */
	public static final int TLR_REC_LEN = new IP6PRTTR().length();

    /**
     * Factor to apply to cobol record length to determine approximate length of XML. 
     */
	public static final long TXT2XML_FACTOR = 3L;
	
	public static final SimpleDateFormat runDateFormatter = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat runTimeFormatter = new SimpleDateFormat("HHmmssSSS");
	
	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		

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
