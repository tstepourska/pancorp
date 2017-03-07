/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.task.xml.metadata;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.*;
import ca.gc.cra.fxit.xmlt.util.*;

/**
 * @author Txs285
 */
public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);

	private MetadataWriter writer 			= null;
	private JaxbMarshaller marshaller 				= null;
	
	//////////////////////////////////////////////////////////////////////////////
	 /////////////////////     PUBLIC METHODS      ////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////
	
	@Override
	public final int invoke(PackageInfo p){
		lg.info("AbstractXmlHelper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		try {
			//do not set xml file name here, do it in the subclass
			//generate XML
			status = transform(p);
			if(lg.isDebugEnabled())
			lg.debug("status: " + status);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		
		//TODO for wireframe testing only, to remove!
		//status=Constants.STATUS_CODE_SUCCESS;
		if(status==Constants.STATUS_CODE_SUCCESS){
			//reset status for validation
			status = Constants.STATUS_CODE_INCOMPLETE;
			String outputFile = p.getFileWorkingDir() + p.getMetadataFilename();		//Globals.FILE_WORKING_DIR
			lg.debug("outputFile: " + outputFile);
			String[] xsdpaths = getSchemas();
					
			try {
				status = this.validate(p, xsdpaths, outputFile);
				lg.info("Validation completed with status " + status);
			}
			catch(Exception e){
				Utils.logError(lg, e);
				status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
			}
		}
		
		//TODO to remove
		//status= Constants.STATUS_CODE_SUCCESS;
		
		return status;
	}
	
	/**
	 * Generates metadata XML file
	 * 
	 * @return int status code
	 */
	@Override
	public int transform(PackageInfo p){
		//String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
	
		marshaller 		= new JaxbMarshaller();
		String outputFile =  p.getFileWorkingDir() + p.getMetadataFilename(); 
		lg.debug("Metadata file name: " + outputFile);
		try {
			OutputStream outputStream = new FileOutputStream(outputFile);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(outputStream,"UTF-8");
			//writer = new CommonXMLStreamWriter(xmlWriter);
			writer = new MetadataWriter(xmlWriter);
			
			//createContent(p);
			createMetadata(p);
			//generateMetadataFileFromSchema(p,writer);
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_INVALID_INPUT_FILE;
			Utils.logError(lg, e);		
		}
		finally{
			try {
				//reader.close();
			}catch(Exception e){}
			
			try {
				writer.flush();
				writer.close();
			}catch(Exception ex){}
		}

		return status;
	}	//end of transform
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PUBLIC METHODS      //////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 
	
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  PRIVATE METHODS      ///////////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 
	/**
	 * Processes Header line of the flat file
	 * @param line
	 * @throws Exception
	 */
	private void createMetadata(PackageInfo p) throws Exception {
		//String fp = "createMetadata: ";
		String senderCon = p.getSendingCountry();
		String receiverCon = p.getReceivingCountry();
		String comType = p.getCtsCommunicationType().toString();
		
		CTSSenderFileMetadataType mdt = new CTSSenderFileMetadataType();

		mdt.setCTSSenderCountryCd(CountryCodeType.fromValue(senderCon));
		mdt.setCTSReceiverCountryCd(CountryCodeType.fromValue(receiverCon));
		mdt.setCTSCommunicationTypeCd(p.getCtsCommunicationType());
		//String xmlFilename = p.getXmlFilename();
		
		//CountryCdSender_CountryCdReceiver_CommunicationType_MessageRefID
		String senderFileId = p.getSendingCountry() + Constants.UNDERSCORE + 
				p.getReceivingCountry()+Constants.UNDERSCORE + 
				p.getCtsCommunicationType() + Constants.UNDERSCORE + 
				p.getMessageRefId();	
		mdt.setSenderFileId(senderFileId);
		
		mdt.setFileFormatCd(FileFormatCdType.XML);
		mdt.setBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType.NONE);
		mdt.setFileCreateTs(Constants.sdfMetadataTimestamp.format(new Date(System.currentTimeMillis())));
		//XMLGregorianCalendar taxYear =  DatatypeFactory.newInstance().newXMLGregorianCalendar(""+p.getTaxYear()+"-12-31");
		//mdt.setTaxYear(Utils.generateMetadataTaxYear(p.getTaxYear())); 
		mdt.setTaxYear(Utils.generateMetadataTaxYear(p.getReportingPeriod()));
		
		mdt.setFileRevisionInd(false); //true if this is a revised data
		
		//TODO get to database and get value for original CTS transmission ID
		mdt.setOriginalCTSTransmissionId(p.getOrigCTSTransmissionId());
		mdt.setSenderContactEmailAddressTxt(Globals.mailSenderAddressList);

		marshaller.startDocument(writer);
		marshaller.transformMetadata(mdt, writer);		
	}

	@Override
	public String[] getSchemas() {
		String[] xsdpaths = new String[] {
				Constants.RESOURCE_BASE_PKG +"schema/metadata/isoctstypes_v1.0.xsd",
				Constants.RESOURCE_BASE_PKG +"schema/metadata/" + Constants.MAIN_SCHEMA_NAME
		};
		
		return xsdpaths;
	}
}