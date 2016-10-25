package ca.gc.cra.fxit.ctsagent.test;

import java.util.LinkedList;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.ctsagent.steps.IStep;
import ca.gc.cra.fxit.ctsagent.workflowmanager.WorkflowManager;

public class StepTest extends TestCase{
	private static Logger log = Logger.getLogger(StepTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	/**
     * @throws Exception
     */
	@Before
    protected void setUp() throws Exception {

    }
    
	@After
    protected void tearDown(){

    }
    
    @Test
	public void test(){
    	try {
    		
    	}
    	catch(Exception e){
    		fail(e.getMessage());
    	}
	}
}
