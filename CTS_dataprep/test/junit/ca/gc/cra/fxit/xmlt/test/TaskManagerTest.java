
package ca.gc.cra.fxit.xmlt.test;

import java.util.LinkedList;
import java.util.Map;
//import java.util.Map;

//import javax.ejb.embeddable.EJBContainer;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.job.TaskManager;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class TaskManagerTest extends TestCase{
	private static Logger lg = Logger.getLogger(TaskManagerTest.class);
	
	private TaskManager taskman;
	private PackageInfo p;
	LinkedList<ITask> job;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Globals.resetBatchProperties();
	}
	
	/**
     * @throws Exception
     */
	@Override
	@Before
    protected void setUp() throws Exception { 		
    	Globals.loadBatchProperties();//Globals.configDir + "fxit.ctsagent.batch.xml");
    	//log.debug(Globals.getJobs());
    	
    	taskman = new TaskManager();
    	initPackage();
    }
    
	@Override
	@After
    protected void tearDown(){

    }
	
   // @Test
	@Ignore
	public void testLoadJobs(){
    	try {    		
    		Map<String, LinkedList<ITask>> jobs = Globals.getJobs();
    		//log.debug(jobs);
    		if(jobs==null || jobs.isEmpty())
    			throw new Exception("No jobs configured!");
    	}
    	catch(Exception e){
    		fail(e.getMessage());
    	}
	}
    
  //  @Test
   // @Ignore
/*	public void testResolveJob(){
    	try {    		
    		job = taskman.resolveJob(p);
    		//log.debug(job);
    		if(job==null || job.isEmpty())
    			throw new Exception("This job is not configured!");
    	}
    	catch(Exception e){
    		fail(e.getMessage());
    	}
	}*/
    
    @Test
    //@Ignore
    public void testInvoke(){
    	//testResolveJob();
    	
    	
    	try {
    		job = taskman.resolveJob(p);
    		taskman.executeJob(job, p);
    	}
    	catch(Exception e){
    		fail(e.getMessage());
    	}
    }
    
    private void initPackage(){
    	p = new PackageInfo();

		//p.setDataProvider("crs");
		//p.setDataProvider("ftc");
		//p.setDataProvider("etr");
		p.setDataProvider("cbc");
		
		/*
		//for outbound data payload prep job
		p.setPackageType(Constants.PKG_TYPE_DATA);
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setJobSuffix(Constants.SUFFIX_PAYLOAD);
		*/
		
		
		//for outbound data package prep job
		p.setPackageType(Constants.PKG_TYPE_DATA);
		p.setJobDirection(Constants.JOB_OUTBOUND);
	//	p.setJobSuffix(Constants.SUFFIX_PACKAGE);
		
		
		/*
		//for inbound data prelim job
		p.setPackageType(Constants.PKG_TYPE_DATA);
		p.setJobDirection(Constants.JOB_INBOUND);
		p.setJobSuffix(Constants.SUFFIX_PRELIM);
		*/
		/*
		//for inbound data unpackage job
		p.setPackageType(Constants.PKG_TYPE_DATA);
		p.setJobDirection(Constants.JOB_INBOUND);
		p.setJobSuffix(Constants.SUFFIX_UNPAK);    	
		*/
    }
}
