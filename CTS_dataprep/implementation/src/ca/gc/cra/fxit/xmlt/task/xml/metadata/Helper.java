package ca.gc.cra.fxit.xmlt.task.xml.metadata;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.task.xml.CustomXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.metadata.*;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);

	private CustomXMLStreamWriter writer 			= null;
	private JaxbMarshaller marshaller 				= null;
	
	//////////////////////////////////////////////////////////////////////////////
	 /////////////////////     PUBLIC METHODS      ////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////
	@Override
	public int invoke(PackageInfo p){
		lg.info("SM XmlHelper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		//generate XML
		status = transform(p);
		lg.info("Transformation completed with status " + status);
		
		//if transformation successful, validate XML
		String outputFile = AppProperties.baseFileDir + AppProperties.outboundProcessed + p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+"_Metadata.xml";
		
		if(status==Constants.STATUS_CODE_SUCCESS)
			status = this.validate(p, AppProperties.schemaLocationBaseDir +"metadata/" + Constants.MAIN_SCHEMA_NAME, outputFile);
		lg.info("Validation completed with status " + status);
		
		return status;
	}
	
	/**
	 * Generates metadata XML file
	 * 
	 * @return int status code
	 */
	@SuppressWarnings("resource")
	@Override
	public int transform(PackageInfo p){
		String fp = "transform: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
	
		marshaller 		= new JaxbMarshaller();

		String outputFile = AppProperties.baseFileDir + AppProperties.outboundProcessed + p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+"_Metadata.xml";
		try {
			OutputStream outputStream = new FileOutputStream(outputFile);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(outputStream,"UTF-8");
			writer = new CustomXMLStreamWriter(xmlWriter);

			createMetadata(p);
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
		String fp = "createMetadata: ";
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
		mdt.setFileCreateTs((new Timestamp(System.currentTimeMillis())).toString());
		//XMLGregorianCalendar taxYear =  DatatypeFactory.newInstance().newXMLGregorianCalendar(""+p.getTaxYear()+"-12-31");
		mdt.setTaxYear(p.getReportingPeriod());
		mdt.setFileRevisionInd(false); //true if this is a revised data
		
		//TODO get to database and get value for original CTS transmission ID
		mdt.setOriginalCTSTransmissionId("value?");
		mdt.setSenderContactEmailAddressTxt(AppProperties.mailSenderAddressList);

		marshaller.startDocument(writer);
		marshaller.transformMetadata(mdt, writer);		
	}
	
	/*
	private void generateMetadataFileFromSchema(){
		ObjectFactory factory = new ObjectFactory(); 
        UserT user = factory.createUserT(); 
        user.setUserName("Sanaulla"); 
        ItemT item = factory.createItemT(); 
        item.setItemName("Seagate External HDD"); 
        item.setPurchasedOn("August 24, 2010"); 
        item.setAmount(new BigDecimal("6776.5")); 
        ItemListT itemList = factory.createItemListT(); 
        itemList.getItem().add(item); 
        ExpenseT expense = factory.createExpenseT(); 
        expense.setUser(user); 
        expense.setItems(itemList); 
*/
   /*     JAXBContext context = JAXBContext.newInstance("generated"); 
        JAXBElement<ExpenseT> element = factory.createExpenseReport(expense); 
        Marshaller marshaller = context.createMarshaller(); 
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE); 
        marshaller.marshal(element,System.out);
        */ 
//}


    
	//////////////////////////////////////////////////////////////////////////////
	 ///////////////////  END OF PRIVATE METHODS      ////////////////////////////
	 ////////////////////////////////////////////////////////////////////////////// 

	/**
	 * For unit test only. TODO to move to the JUnit
	 * @param args
	 */
	public static void main(String[] args){
		//String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		String filename = "IP.AIP5S182.CAUS.A14.S0000001";
		/*
		 * <CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		 */
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
	}
}