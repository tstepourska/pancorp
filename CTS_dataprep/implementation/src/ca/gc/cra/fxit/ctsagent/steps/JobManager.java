package ca.gc.cra.fxit.ctsagent.steps;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.util.Utils;

/**
 * 
 * Called from the BatchController EJB or from the ActionServlet of the web tool
 * 
 * @author Txs285
 */
public class JobManager implements Serializable {
	private static Logger log = Logger.getLogger(JobManager.class);
	private static final long serialVersionUID = -2920777761984522115L;

	/**
	 * Invokes this class logic
	 * 
	 * @param pInfo
	 * @param filename
	 * @return int 
	 */
	public int invoke(PackageInfo pInfo, String filename){
		log.debug("JobManager invoked");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		try {
			//find appropriate job using package info
			LinkedList<IStep> job = resolveJob(pInfo, filename);
		
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
	private int executeJob(LinkedList<IStep> list, PackageInfo p) {
		int status = Constants.STATUS_CODE_ERROR;
		Iterator<IStep> it = list.iterator();
		while(it.hasNext()){
			IStep step = it.next();
			it.next().execute(p);
			status = step.getResultCode();
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
	private LinkedList<IStep> resolveJob(PackageInfo p, String jobConfigFile) throws Exception {

		String value = null;
		String[] arr = null;
		
		String jobDirection = p.getJobDirection(); //in or out
		String packageType  = p.getPackageType();  //data or status msg
		//TODO get data owner ? maybe not here, later?
		
		Properties jobs = new Properties();
		jobs.load(new FileInputStream(jobConfigFile));

		//build a key to find job
		StringBuilder sb = new StringBuilder();
		sb.append(jobDirection + "_"+packageType);
		if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND))
			sb.append(Constants.SUFFIX_PAYLOAD);

		//get jobConfiguration
		value 	= jobs.getProperty(sb.toString());			
		arr 	= value.split(";");
		LinkedList<IStep> list = new LinkedList<>();

		//add tasks to the job
		ClassLoader classLoader = JobManager.class.getClassLoader();
		for(String s : arr){
			Class<?> myObjectClass = classLoader.loadClass(Constants.JAVA_PKG_STEP + s);
			IStep task = (IStep) myObjectClass.newInstance();
			list.add(task);
		}
	    	
	    return list;
	}
}