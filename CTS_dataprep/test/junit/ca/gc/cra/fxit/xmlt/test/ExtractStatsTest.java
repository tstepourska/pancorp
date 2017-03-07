
package ca.gc.cra.fxit.xmlt.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.task.ExtractStatistics;
import ca.gc.cra.fxit.xmlt.util.Globals;

public class ExtractStatsTest extends TestCase{
	private static Logger lg = Logger.getLogger(ExtractStatsTest.class);
	
	ExtractStatistics helper;
	
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
    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
    @Test
	public void testStatsConfig(){
		
		try {
			String[] tags = helper.getStatsConfig("ftc");
			for(String s : tags){
			lg.debug("tag: "+ s);
			}
		}
		catch(Exception e){
			lg.error("Error loading properties: " + e.getMessage());
		}
	}
}
