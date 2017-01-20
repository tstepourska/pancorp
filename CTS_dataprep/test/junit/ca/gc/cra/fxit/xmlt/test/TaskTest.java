package ca.gc.cra.fxit.xmlt.test;

import java.util.LinkedList;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.task.ITask;

public class TaskTest extends TestCase{
	private static Logger log = Logger.getLogger(TaskTest.class);
	
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
	public void test(){
    	try {
    		
    	}
    	catch(Exception e){
    		fail(e.getMessage());
    	}
	}
}
