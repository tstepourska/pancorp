/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.test;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileAcceptanceStatusEnumType;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.GenerateXML;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class XmlReportHelperTest extends TestCase{
	private static Logger lg = Logger.getLogger(XmlReportHelperTest.class);

	private PackageInfo p;
	AbstractXmlHelper helper;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	/**
     * @throws Exception
     */
	@Override
	@Before
    protected void setUp() throws Exception { 		
    	Globals.loadBatchProperties(Globals.configDir + "fxit.xmlt.batch.xml");
    	
    	initPackage();
    	
    	String dataProvider 		= p.getDataProvider();
		lg.debug("dataProvider: " + dataProvider);
		
		String messageType = p.getPackageType();
		//log.debug("messageType: " + messageType);
		
		//load appropriate helper 
		ClassLoader classLoader = GenerateXML.class.getClassLoader();
		Class<?> myObjectClass 	= null;
		
		if(messageType.equalsIgnoreCase(Constants.PKG_TYPE_DATA)){
			myObjectClass 	= classLoader.loadClass(Constants.JAVA_PKG_TASK + "xml." + dataProvider+ ".Helper");
		}
		
		helper 			= (AbstractXmlHelper) myObjectClass.newInstance();

    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
	@Test
	public void testGenerateXml(){
		int status = helper.invoke(p);
		lg.info("Helper completed with status " + status);
		assertEquals(Constants.STATUS_CODE_SUCCESS, status);
	}
	
	//@Test
/*	@Ignore
	public void testValidateXML(){
		String outputFile = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+"_Metadata.xml";
		lg.info("testValidateXML: outputFile: " + outputFile);
		String schema = Globals.schemaLocationBaseDir +"metadata/" + Constants.MAIN_SCHEMA_NAME;
		lg.info("testValidateXML: schema: " + schema);
		
		int status = helper.validate(p, schema, outputFile);	
		lg.info("Validation completed with status " + status);
	}*/
 
    private void initPackage(){	
		try {
			p = new PackageInfo();

			p.setDataProvider("crs");
			//p.setDataProvider("ftc");
			//p.setDataProvider("etr");
			//p.setDataProvider("cbc");
			p.setDataProvider("crs");
			
			p.setJobDirection(Constants.JOB_OUTBOUND);
			
			//for outbound status message
			p.setPackageType(Constants.PKG_TYPE_DATA);		
			//OECD message type data
			p.setOECDMessageType(p.getDataProvider().toUpperCase()); // + Constants.MSG_TYPE_STATUS);
			
			p.setJobDirection(Constants.JOB_OUTBOUND);
			p.setSendingCountry(Constants.CANADA);


			p.setReceivingCountry("FR");
			p.setSendingCountry(Constants.CANADA);
					
			//String filename = p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+".xml";
			String origFilename = "IP.AIP5S182.CAUS.A14.S0000001";
			p.setOrigFilename(origFilename);
			//p.setXmlFilename(filename);
			
			p.setMessageRefId("CA2016FR123456789");
			p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
			p.setOrigFileAcceptanceStatus(FileAcceptanceStatusEnumType.ACCEPTED);
			p.setOrigMessageRefId("CA2016FR2345678");
			p.setSweepTime(Utils.generateSweepTimestamp(System.currentTimeMillis()));
			p.setOrigCTSSendingTimeStamp(DatatypeFactory.newInstance().newXMLGregorianCalendar());
			p.setOrigUncompressFileSizeKBQty(BigInteger.valueOf(12345678));
			// the CTS Transmission ID for the original transmission as sent by the sending Competent Authority
    		p.setOrigCTSTransmissionId("asdf89asdfgdfiua0");
    		//the sender of the original transmission
    		p.setOrigSenderFileId("CA2016FR234567891");
    		// the size of the decrypted, uncompressed CRS message
    		p.setOrigUncompressFileSizeKBQty(new BigInteger("26542374"));
		}
		catch(Exception e) {
			Utils.logError(lg, e);
		}	
    }
}