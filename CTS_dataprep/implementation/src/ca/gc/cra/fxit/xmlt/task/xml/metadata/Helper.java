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
	public String generateXMLFilename(PackageInfo p) throws Exception {
		 return Utils.generateMetadataFilename(p);
	 }
	
	/**
	 * Generates metadata XML file
	 * 
	 * @return int status code
	 */
	@SuppressWarnings("resource")
	@Override
	public int transform(PackageInfo p){
		//String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
	
		marshaller 		= new JaxbMarshaller();

		String outputFile = Globals.FILE_WORKING_DIR + p.getMetadataFilename(); 
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
		mdt.setSenderFileId(senderCon + "_" + receiverCon + "_" + comType + "_" + p.getMessageRefId());//("UTC_"+p.getCtsCommunicationType()+ p.getSendingCountry() + ".zip");
		mdt.setFileFormatCd(FileFormatCdType.XML);
		mdt.setBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType.NONE);
		mdt.setFileCreateTs(Constants.sdfMetadataTimestamp.format(new Date(System.currentTimeMillis())));
		//XMLGregorianCalendar taxYear =  DatatypeFactory.newInstance().newXMLGregorianCalendar(""+p.getTaxYear()+"-12-31");
		mdt.setTaxYear(Utils.generateMetadataTaxYear(p.getTaxYear())); //p.getReportingPeriod());
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
				  Globals.schemaLocationBaseDir +"metadata/isoctstypes_v1.0.xsd",
				 Globals.schemaLocationBaseDir +"metadata/" + Constants.MAIN_SCHEMA_NAME};
		
		return xsdpaths;
	}
	
/*	private void createContent(PackageInfo p) throws Exception {
		marshaller.startDocument(writer);
		
		String senderCon = p.getSendingCountry();
		String receiverCon = p.getReceivingCountry();
		String comType = p.getCtsCommunicationType().toString();

		marshaller.transformSenderCountry(CountryCodeType.fromValue(senderCon),writer);
		marshaller.transformReceiverCountry(CountryCodeType.fromValue(receiverCon),writer);
		marshaller.transformCommunicationType(p.getCtsCommunicationType(),writer);
		
		String messageRefId = p.getMessageRefId();
		
		if(p.getMessageRefId()!=null){
		String senderFileId = senderCon + "_" + receiverCon + "_" + comType + "_" + messageRefId;
		marshaller.transformSenderFileId(senderFileId,writer);
		}
		marshaller.transformFileFormatCd(FileFormatCdType.XML,writer);
		marshaller.transformBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType.NONE,writer);
		marshaller.transformFileCreateTs(Constants.sdfMetadataTimestamp.format(new Date(System.currentTimeMillis())),writer);
		marshaller.transformTaxYear(p.getReportingPeriod(),writer);
		marshaller.transformFileRevisionInd(Boolean.toString(p.isFileRevisionInd()),writer); //true if this is a revised data
		
		//should be null for outbound, get from file name for inbound
		String tid = p.getOrigCTSTransmissionId();
		if(tid!=null)
		marshaller.transformOriginalCTSTransmissionId(tid,writer);
		if(Globals.mailSenderAddressList!=null)
			marshaller.transformSenderContactEmailAddressTxt(Globals.mailSenderAddressList,writer);	
	}*/
	
/*	private void generateMetadataFileFromSchema(PackageInfo p, CommonXMLStreamWriter writer){
		 
		try {
        ObjectFactory factory = new ObjectFactory();
 
        CTSSenderFileMetadataType mdt = factory.createCTSSenderFileMetadataType();
        mdt.setCTSSenderCountryCd(CountryCodeType.fromValue(p.getSendingCountry()));
        mdt.setCTSReceiverCountryCd(CountryCodeType.fromValue(p.getReceivingCountry()));
        mdt.setCTSCommunicationTypeCd(p.getCtsCommunicationType());
        mdt.setSenderFileId(p.getOrigSenderFileId());
        mdt.setFileFormatCd(FileFormatCdType.XML);
        mdt.setBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType.NONE);
        mdt.setFileCreateTs(Utils.toUTC(new Date(System.currentTimeMillis())));      
        mdt.setTaxYear(Utils.generateReportingPeriod("2016", null, null));      
        mdt.setFileRevisionInd(p.isFileRevisionIndic());
        mdt.setOriginalCTSTransmissionId("");
        mdt.setSenderContactEmailAddressTxt(Globals.mailSenderAddressList);

        JAXBContext  context = JAXBContext.newInstance(CTSSenderFileMetadataType.class);
        JAXBElement <CTSSenderFileMetadataType> element = factory.createCTSSenderFileMetadata(mdt);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
        marshaller.marshal(element,writer);//System.out);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}
*/

    
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PRIVATE METHODS      ////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 

	/**
	 * For unit test only.  moved to the JUnit
	 * @param args
	 */
/*	public static void main(String[] args){
		//String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		String filename = "IP.AIP5S182.CAUS.A14.S0000001";
		//<CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		//String outputDir = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/";
		
		PackageInfo p = new PackageInfo();
		p.setDataProvider("crs");
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setOrigFilename(filename);
		p.setSendingCountry("CA");
		p.setReceivingCountry("FR");
		try {
		p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
		}
		catch(Exception e){Utils.logError(lg, e);}
		p.setCtsCommunicationType(CTSCommunicationTypeCdType.fromValue("CRS"));
		
		Helper h = new Helper();
		int status = h.invoke(p);
		lg.info("Helper completed with status " + status);
	}*/
}