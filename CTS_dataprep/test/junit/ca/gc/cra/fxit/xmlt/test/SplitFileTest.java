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
import ca.gc.cra.fxit.xmlt.task.CheckFileSize;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class SplitFileTest extends TestCase{
	private static Logger lg = Logger.getLogger(SplitFileTest.class);

	private PackageInfo p;
	CheckFileSize helper;
	
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
		helper 			= new CheckFileSize(); 

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
			p.setReceivingCountry("FR");
			
			String origFilename = "IP.AIP5S182.CAUS.A14.S0000009";
			p.setOrigFilename(origFilename);
					
			//String filename = p.getSendingCountry() + "_"+p.getDataProvider().toUpperCase()+".xml";
			//p.setXmlFilename("CRS_MESSAGEREFID_20170216T154955_T.xml");
			
			//p.setMessageRefId("CA2016FR123456789");
			p.setReportingPeriod(Utils.generateReportingPeriod("2016", null, null));
		}
		catch(Exception e) {
			Utils.logError(lg, e);
		}	
    }
}