package ca.gc.cra.fxit.xmlt.job;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Properties;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.util.AppProperties;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

/**
 * 
 * Called from the BatchController EJB or from the ActionServlet of the web application
 * 
 * @author Txs285
 */
public class TaskManager implements Serializable {
	private static Logger log = Logger.getLogger(TaskManager.class);
	private static final long serialVersionUID = -2920777761984522115L;

	/**
	 * Invokes this class logic
	 * 
	 * @param pInfo
	 * @param filename
	 * @return int 
	 */
	public int invoke(PackageInfo pInfo) { 
		log.debug("TaskManager invoked");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {
			//find appropriate job using package info
			LinkedList<ITask> job = resolveJob(pInfo); 
			//log.info("Executing job " + job);
		
			//all onJobStart routines here
			onJobStart();
			
			//do the job
			status = executeJob(job, pInfo);
					
			if(status==Constants.STATUS_CODE_CREATE_JOB_LOOP){
				//configure remaining tasks as a loop for more than one file
				//at this point splitted file pieces must be generated on a disc and 
				//named with <xml file name>.xml_<file count> 
				
				//initialize list of PackageInfos for each file
				PackageInfo[] list = this.initPackageList(pInfo);
				
				LinkedList<ITask> newjob; 				
				//for each part of the splitted file
				for(int i=0;i<list.length;i++){
					//make deep copy of a remaining job
					newjob = cloneJob(job);
					
					//do the job for a single file
					status = executeJob(newjob, pInfo);
					
					//TODO record status somewhere along with 
					//the knowledge of original (unsplitted) file 
					// name, split count etc
				}
			}
	
			//all onJobEnd routines here
			onJobEnd(status);
		}
		catch(Exception e){
			Utils.logError(log, e);
		}

		return status;
	}
	
	/**
	 * Iterates through the list of steps to complete the job
	 * 
	 * @param list
	 * @param p
	 * @return
	 */
	public int executeJob(LinkedList<ITask> list, PackageInfo p) {
		String fp = "executeJob: ";
		int status = Constants.STATUS_CODE_INCOMPLETE;
		ITask task = null;
		
		__job:
		while(!list.isEmpty()){
			status = Constants.STATUS_CODE_INCOMPLETE;
			task = list.removeFirst();// it.next();
			//log.debug(fp + task.toString());
			status = task.execute(p);
			//log.debug(fp + "status: " + status);
			
			if(status!=Constants.STATUS_CODE_SUCCESS){
				//some error, stop job execution and handle error(s)
				break __job;
			}
		}	
		log.info("Job execution stopped with status " + status);

		return status;
	}
	
	/**
	 * Creates a deep copy of a job to be able to run the same job 
	 * several times
	 * 
	 * @param j
	 * @return
	 * 
	 * @throws Exception
	 */
	private LinkedList<ITask> cloneJob(LinkedList<ITask> j) throws Exception {
		LinkedList<ITask> newjob = new LinkedList<>(); 
		for(ITask t : j){
			newjob.add(t.cloneTask());
		}
		return newjob;
	}
	
	/**
	 * Initializes list of PackageInfo objects, each corresponding to the chunk of 
	 * original file created by splitting
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */
	private PackageInfo[] initPackageList(PackageInfo p) throws Exception {
		int splitCnt = p.getSplitFileCount();
		PackageInfo[] pList = new PackageInfo[splitCnt];
		
		String origFileName = p.getOrigFilename();
		
		for(int i=1;i<=pList.length;i++){
			PackageInfo pi = p.clone();
			pi.setSplitFileCount(splitCnt);
			pi.setOrigFilename(origFileName+"_" + i);
			pList[i] = pi;
		}
		
		return pList;
	}
	    
	/**
	 * Loads appropriate job based on known job direction (IN - inbound or OUT - outbound) and 
	 * package type (DT - data or SM - status message)
	 * 
	 * @param p
	 * @param jobConfigFile
	 * @return
	 * @throws Exception
	 */
	public LinkedList<ITask> resolveJob(PackageInfo p) throws Exception {
		String fp = "resolveJob: ";
		String jobDirection = p.getJobDirection(); //in or out
		//String packageType  = p.getPackageType();  //data or status msg
		String dataProvider = p.getDataProvider(); 
		String jobSuffix    = p.getJobSuffix();
		//log.debug(fp + "Job direction=" + jobDirection + ";packageType="+packageType+";suffix="+jobSuffix+";dataProvider="+dataProvider);

		//build a key to find job
		StringBuilder sb = new StringBuilder();
		sb
		.append(jobDirection).append("_")
		//.append(packageType).append("_")
		.append(jobSuffix)
		;
		/*if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND)){
			sb.append(Constants.SUFFIX_PAYLOAD);
		}
		else{
			sb.append(Constants.SUFFIX_PRELIM);
		}*/
		String defaultKey = sb.toString();
		//log.debug(fp + "Job default key: " + defaultKey);
		String specificKey = defaultKey+"_" + dataProvider.toUpperCase();
		//log.debug(fp + "Job specific key: " + specificKey);
		
		//look for the job specific to data provider first
		LinkedList<ITask> job = AppProperties.getJob(specificKey);
		//log.debug(fp + "Got a job for specific key: " + job);
		//if specific job not found, use default
		if(job==null){
			job =  AppProperties.getJob(defaultKey);
			//log.debug(fp + "Got a job for default key: " + job);
		}
		
		log.debug(fp + "Got a job: " + job);
		
		if(job==null)
			throw new Exception("Configuration not found!");

	    return job;
	}
	
	/**
	 * Takes care of all on job start routines 
	 */
	private void onJobStart(){
		//Insert stats record into db
		String fp = "onJobStart: ";
		log.info(fp);
	}
	
	/**
	 * Takes care of all on job end routines 
	 */
	private void onJobEnd(int st){
		String fp = "onJobEnd: ";
		log.info(fp);
		//TODO handle success and any error cases here or pass it back to the calling class?
		switch(st){
		case Constants.STATUS_CODE_SUCCESS:
			break;
		default:
			
		///////////////
		//update database with stats
	}
	}
}