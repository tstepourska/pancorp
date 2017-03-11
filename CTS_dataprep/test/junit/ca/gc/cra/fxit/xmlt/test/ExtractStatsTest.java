
package ca.gc.cra.fxit.xmlt.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.ExtractStatistics;
import ca.gc.cra.fxit.xmlt.util.Globals;

public class ExtractStatsTest extends TestCase{
	private static Logger lg = Logger.getLogger(ExtractStatsTest.class);
	
	ExtractStatistics helper;
	PackageInfo p;
	
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
    	helper = new ExtractStatistics();
    	p = new PackageInfo();
    	p.setDataProvider("crs");
    	String fileWorkingDir = "C:/run/xmlt/outbound/unprocessed/temp/";
		p.setFileWorkingDir(fileWorkingDir);
		p.setXmlFilename("CRS_CA14US8069340882965089820_20170308T124338_T.xml");
    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
    @Test
	public void testStatsConfig(){
		int status = -99;
		try {
			//String[] tags = helper.getStatsConfig("crs");
			//for(String s : tags){
			//lg.debug("tag: "+ s);
			//}
			status = helper.invoke(p);
		}
		catch(Exception e){
			lg.error("Error loading properties: " + e.getMessage());
		}
	}
}
