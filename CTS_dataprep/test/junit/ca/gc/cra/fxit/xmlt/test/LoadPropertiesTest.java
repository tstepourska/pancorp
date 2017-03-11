
package ca.gc.cra.fxit.xmlt.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.util.Globals;

public class LoadPropertiesTest extends TestCase{
	private static Logger lg = Logger.getLogger(LoadPropertiesTest.class);
	
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
    	
    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
    @Test
	public void testLoadProperties(){
		
		try {
			Globals.loadBatchProperties();//Globals.configDir + "fxit.ctsagent.batch.xml");

			//Globals.loadDomainProperties();
			lg.debug(Globals.toStaticString());
		}
		catch(Exception e){
			lg.error("Error loading properties: " + e.getMessage());
		}
	}
}
