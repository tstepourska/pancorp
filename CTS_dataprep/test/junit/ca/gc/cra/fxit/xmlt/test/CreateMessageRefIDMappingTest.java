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

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.CreateMessageRefIdMapping;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class CreateMessageRefIDMappingTest extends TestCase{
	private static Logger lg = Logger.getLogger(CreateMessageRefIDMappingTest.class);

	private PackageInfo p;
	CreateMessageRefIdMapping helper;
	
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
    	initPackage();		
		helper 			= new CreateMessageRefIdMapping(); 
    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
	@Test
	public void testCheckFileSize(){
		int status = helper.invoke(p);
		lg.info("Helper completed with status " + status);
		assertEquals(Constants.STATUS_CODE_SUCCESS, status);
	}
 
    private void initPackage(){	
		try {
			p = new PackageInfo();

			p.setDataProvider("crs");
			//p.setDataProvider("ftc");
			//p.setDataProvider("etr");
			//p.setDataProvider("cbc");
		//	p.setDataProvider("crs");

			p.setSendingCountry(Constants.CANADA);
			p.setReceivingCountry("US");
			
			String fileWorkingDir = "C:/run/xmlt/outbound/unprocessed/temp/";
			String dir = "C:/run/xmlt/";
			Globals.baseFileDir = dir;
			p.setFileWorkingDir(fileWorkingDir);
			String origFilename = "IP.AIP5S182.CAUS.A14.S0000001";
			p.setOrigFilename(origFilename);
			p.setMessageRefId("CA2016FR123456789");
			//String filename = p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+".xml";
			p.setXmlFilename("CRS_CA2016FR123456789_20170216T154955_T_1.xml");
			
			
			//p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
		}
		catch(Exception e) {
			Utils.logError(lg, e);
		}	
    }
}