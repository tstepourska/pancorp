/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.test;

import java.math.BigInteger;

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
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;
import ca.gc.cra.fxit.xmlt.task.xml.statusmessage.Helper;

public class XmlStatusMessageHelperTest extends TestCase{
	private static Logger lg = Logger.getLogger(XmlStatusMessageHelperTest.class);

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
    	Globals.loadBatchProperties();//Globals.configDir + "fxit.xmlt.batch.xml");
    	
    	initPackage();
    	
    	String dataProvider 		= p.getDataProvider();
		lg.debug("dataProvider: " + dataProvider);

		helper = new Helper();
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
			
			//for outbound status message
			p.setPackageType(Constants.PKG_TYPE_STATUS);
			//for OECD status message
			p.setOECDMessageType(p.getDataProvider().toUpperCase()+Constants.MSG_TYPE_MESSAGE_STATUS);			
			
			p.setJobDirection(Constants.JOB_OUTBOUND);
			p.setSendingCountry(Constants.CANADA);

			p.setJobDirection(Constants.JOB_OUTBOUND);
			p.setReceivingCountry("FR");
			p.setSendingCountry(Constants.CANADA);
			
			/*String filename = p.getSendingCountry() + "_CRSMessageStatus.xml";
			p.setOrigFilename(filename);
			//p.setXmlFilename(filename);
			 * 
			 */
			
			p.setMessageRefId("CA2016FR123456789");
			p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
			p.setOrigFileAcceptanceStatus(FileAcceptanceStatusEnumType.ACCEPTED);
			p.setOrigMessageRefId("CA2016FR2345678");
			lg.debug("messageRefId set");
			p.setOrigCTSSendingTimeStamp(Utils.generateStatusMessageXMLTimestamp(System.currentTimeMillis()));
			lg.debug("orig CTS sending time stamp set");
			p.setSweepTime(Utils.generateSweepTimestamp(System.currentTimeMillis()));
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