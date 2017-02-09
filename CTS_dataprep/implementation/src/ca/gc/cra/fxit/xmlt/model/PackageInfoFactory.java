package ca.gc.cra.fxit.xmlt.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileAcceptanceStatusEnumType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.model.wrapper.crs.MessageHeaderWrapper;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class PackageInfoFactory {
	private static Logger log = Logger.getLogger(PackageInfoFactory.class);
	/**
	 * Initializes new package info object and sets variables 
	 * necessary to find a job appropriate for processing this package: 
	 * job direction, package type (data or status message), sending and receiving country, 
	 * data owner prefix - crs, cbc, etr, ftc
	 * 
	 * For report message from Infodec - mainframe
	 */
	public static PackageInfo createPackageInfo(String unprocessedPath, String filename, String jobDirection) throws Exception{	
		String fp = "initPackage: ";
		PackageInfo p = new PackageInfo();			
		String dataProvider = null;
		
		//TODO assuming here that the header contains info required to determine:
		// sender country
		// data or status message content
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(unprocessedPath + filename), Constants.DEFAULT_ENCODING));){
			String line;
			int transCD = -1;
			String[] arr = filename.split("\\.");
			int len = arr.length;
			log.debug(fp + "len: " + len);
			String cc = arr[2];
			
			//!!! TODO: determine dataProviderPrefix: cbc, crs, etr, ftc
			
			dataProvider = Utils.getDataProvider(filename);
			//TODO to remove/////
			dataProvider = "crs";
			/////////////////////
			if( dataProvider==null)
				throw new Exception("Unsupported data provider!");
			p.setDataProvider( dataProvider);
	
			String sendingCountry = cc.substring(0,2);
			String receivingCountry = cc.substring(2);
			log.debug(fp + "sending from " + sendingCountry + " to " + receivingCountry);
			p.setSendingCountry(sendingCountry);
			p.setReceivingCountry(receivingCountry);
			
			//TODO set sweep time from the flat file here!!
			//extract sweep time
			p.setSweepTime(Utils.generateSweepTimestamp(System.currentTimeMillis()));
			
			p.setOrigFilename(filename);
			//p.setInputPathName(unprocessedPath);
			p.setJobDirection(jobDirection);
			// !!! TODO - determine type of package
			//p.setPackageType(Constants.PKG_TYPE_STATUS);
		//}
		//else{
			p.setPackageType(Constants.PKG_TYPE_DATA); // or Status message?
			p.setOECDMessageType(p.getDataProvider().toUpperCase()); // + Constants.MSG_TYPE_MESSAGE_STATUS); //fo status
			
			
			//set based on data provider and message/package type
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CRS);
			/*p.setCtsCommunicationType(CTSCommunicationTypeCdType.CRS_STATUS);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CBC);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CBC_STATUS);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.ETR);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.ETR_STATUS);
			*/

			// figure out suffix:
			if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND)){
				p.setJobSuffix(Constants.SUFFIX_PAYLOAD);
			}			

	//outer:
			while( (line=reader.readLine()) != null) {		
				//reset
				transCD = -1;
				//log.debug(fp + "line: " + line);
				
				try {
					transCD = Integer.parseInt(line.substring(0,4));
				//log.debug(fp + "transCD: " + transCD);
			
				
				switch(transCD){
				case Constants.HDR_PKG_REF_REC_TRANS_CD: //1001:
					log.debug(fp + "case 1001: header");
					//log.debug(fp + "line: " + line);
					MessageHeaderWrapper header = new MessageHeaderWrapper(line);

					//initialize reporting period field, month and day are null
					XMLGregorianCalendar cal = Utils.generateReportingPeriod(header.getRtnTxYr(),null,null);
					log.debug(fp + "tax year calendar from header: " + cal);				
					//initialize tax year
					p.setReportingPeriod(cal);
					p.setTaxYear(header.getRtnTxYr());
					
					break;
				default:
					log.debug(fp + "case default: " + transCD);
				}
				}
				catch(Exception e){
					//no transCd therefore status message:
					if(line.toLowerCase().indexOf("status")>-1){
						p.setPackageType(Constants.PKG_TYPE_STATUS);
					
					}
					else{
						p.setPackageType(Constants.PKG_TYPE_DATA);
					}
					//Utils.logError(log, e);
				}
					
				break;
			}
		}
		catch(Exception e){
			Utils.logError(log, e);
			throw e;
		}
		
		return p;
	}

	public static PackageInfo createStatusMessagePackageInfo(String dataProvider, 
															String origMessageRefID,
															String fileAcceptanceStatus,
															List<FileErrorType> fileErrors,
															List<RecordErrorType> recordErrors,
															String origCTSTransmissionId,
															String countryToSend,
															XMLGregorianCalendar origCTSSendingTimeStamp,
															String origSenderFileId,
															BigInteger origUncompressFileSizeKBQty,
															XMLGregorianCalendar reportingPeriod){
		PackageInfo p = new PackageInfo();
		p.setOrigMessageRefId(origMessageRefID);
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setDataProvider(dataProvider);	
		p.setSendingCountry(Constants.CANADA);
		
		p.setCtsCommunicationType(CTSCommunicationTypeCdType.fromValue(dataProvider.toUpperCase() + "Status"));
		p.setOrigCTSTransmissionId(origCTSTransmissionId);
		p.setReceivingCountry(countryToSend);
		p.setOrigCTSSendingTimeStamp(origCTSSendingTimeStamp);
		p.setOrigSenderFileId(origSenderFileId);
		p.setOrigUncompressFileSizeKBQty(origUncompressFileSizeKBQty);
		p.setPackageType(Constants.PKG_TYPE_STATUS);
		p.setOECDMessageType(dataProvider.toUpperCase() + Constants.MSG_TYPE_MESSAGE_STATUS);
		p.setReportingPeriod(reportingPeriod);
		
		p.setOrigFileAcceptanceStatus(FileAcceptanceStatusEnumType.fromValue(fileAcceptanceStatus)); //fileAcceptanceStatus);
		p.setFileErrors(fileErrors);
		p.setRecordErrors(recordErrors);
		
		return p;
	}
}