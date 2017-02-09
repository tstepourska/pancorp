/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;
import ca.gc.cra.fxit.xmlt.task.xml.metadata.Helper;

public class XmlMetadataHelperTest extends TestCase{
	private static Logger lg = Logger.getLogger(XmlMetadataHelperTest.class);

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
			
			p.setJobDirection(Constants.JOB_OUTBOUND);
			p.setReceivingCountry("FR");
			p.setSendingCountry(Constants.CANADA);
			p.setSweepTime(Utils.generateSweepTimestamp(System.currentTimeMillis()));
			//msg type for data file
			p.setPackageType(Constants.PKG_TYPE_DATA);
			p.setOECDMessageType(p.getDataProvider().toUpperCase());
			
			p.setPackageType(Constants.PKG_TYPE_STATUS);
			p.setOECDMessageType(p.getDataProvider().toUpperCase() + Constants.MSG_TYPE_MESSAGE_STATUS);
			try {
				p.setReportingPeriod(Utils.generateMetadataTaxYear("2016"));
				}
				catch(Exception e){Utils.logError(lg, e);}
			
			p.setXmlFilename(Utils.generateXMLFileName(p));
			String filename = p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+"_Metadata.xml";
			//String filename = p.getSendingCountry() + "_CRSMessageStatus.xml";
			//p.setOrigFilename(filename);
			//p.setXmlFilename(filename);
			//p.setOrigFilename(filename);
			p.setMetadataFilename(filename);
			p.setSendingCountry("CA");
			p.setReceivingCountry("FR");
			p.setMessageRefId("234678990");
			
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.fromValue("CRS"));
		}
		catch(Exception e) {
			Utils.logError(lg, e);
		}	
    }
}