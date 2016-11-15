package ca.gc.cra.fxit.ctsagent.task;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.AppProperties;
import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.util.Utils;

/**
 * 
 * Called from the BatchController EJB or from the ActionServlet of the web tool
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
	public int invoke(PackageInfo pInfo) { //, String filename){
		log.debug("TaskManager invoked");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {
			//find appropriate job using package info
			LinkedList<ITask> job = resolveJob(pInfo); //, filename);
		
			//do the job
			status = executeJob(job, pInfo);
		
			//TODO handle success and any error cases here or pass it back to the calling class?
			switch(status){
				case Constants.STATUS_CODE_SUCCESS:
					break;
				default:
			}
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
	private int executeJob(LinkedList<ITask> list, PackageInfo p) {
		int status = Constants.STATUS_CODE_ERROR;
		Iterator<ITask> it = list.iterator();
		while(it.hasNext()){
			ITask task = it.next();
			it.next().execute(p);
			status = task.getResultCode();
			if(status!=Constants.STATUS_CODE_SUCCESS)
				break;
		}
		return status;
	}
	    
	/**
	 * Loads appropriate job based on known job direction (I - inbound or O - outbound) and 
	 * package type (D - data or SM - status message)
	 * 
	 * @param p
	 * @param jobConfigFile
	 * @return
	 * @throws Exception
	 */
	private LinkedList<ITask> resolveJob(PackageInfo p
			//, String jobConfigFile
			) throws Exception {

		String value = null;
		//String[] arr = null;
		
		String jobDirection = p.getJobDirection(); //in or out
		String packageType  = p.getPackageType();  //data or status msg
		//TODO get data owner ? maybe not here, later?
		
		//Properties jobs = new Properties();
		//jobs.load(new FileInputStream(jobConfigFile));

		//build a key to find job
		StringBuilder sb = new StringBuilder();
		sb.append(jobDirection + "_"+packageType);
		if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND))
			sb.append(Constants.SUFFIX_PAYLOAD);

		//get jobConfiguration
		LinkedList<ITask> jobs = AppProperties.getJob(sb.toString());			
		//arr 	= value.split(";");
		LinkedList<ITask> list = new LinkedList<>();

		//add tasks to the job
		ClassLoader classLoader = TaskManager.class.getClassLoader();
		//for(String s : arr){
		while(!jobs.isEmpty()){
			//TODO check job config loading, maybe the code below is not needed
			Class<?> myObjectClass = classLoader.loadClass(jobs.remove().getClass().getName());
			ITask task = (ITask) myObjectClass.newInstance();
			list.add(task);
		}
	    	
	    return list;
	}
}