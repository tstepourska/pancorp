
package ca.gc.cra.fxit.xmlt.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.job.ExternalJobInitiator;
import ca.gc.cra.fxit.xmlt.util.Globals;

public class ExternalInitiatorTest extends TestCase{
	private static Logger lg = Logger.getLogger(ExternalInitiatorTest.class);
	
	ExternalJobInitiator job;
	
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
    	job = new ExternalJobInitiator();
    }
    
	@Override
	@After
    protected void tearDown(){
		job = null;
    }
	
    @Test
	public void createStatusMessageTest(){
		
		try {
			Globals.loadBatchProperties();//Globals.configDir + "fxit.ctsagent.batch.xml");

			//Globals.loadDomainProperties();
			lg.debug(Globals.toStaticString());
		}
		catch(Exception e){
			lg.error("Error creating status messages: " + e.getMessage());
		}
	}
}
