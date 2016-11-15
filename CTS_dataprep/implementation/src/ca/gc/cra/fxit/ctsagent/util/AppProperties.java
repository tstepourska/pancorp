package ca.gc.cra.fxit.ctsagent.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.ctsagent.batch.BatchInitiatorBean;
import ca.gc.cra.fxit.ctsagent.task.ITask;

//import ca.gc.ccra.rccr.security.crypto.business.*;

/**
 * Domain properties used by the transformation process.
 * 
 */
public class AppProperties {

	private static Logger log = Logger.getLogger(AppProperties.class);

	//values from configuration file
	public static String baseDir = "";
	public static String unprocessedInputDirName = "";
	public static String xmlOutputDirName = ""; 
	public static String flatFileOutputDirName = ""; 
	public static String fileTransferLocalDirName = ""; 
	public static String mainframeHost = "";
	public static  String mainframeUserid = "";
	public static  String mainframePassword = "";
	public static String databaseEnvironment = "";
	public static String docTypeIndicEnv = "";
	public static String mailFromAddress = "";
	public static String mailToAddressList = "";
	public static Boolean sendMailFlag = false;
    		
	
	private static Properties map = new Properties();
	private static Map<String, LinkedList<ITask>> jobs = new HashMap<>();
	
	/**
	 * Adds provided properties to the map, overriding values for existing keys.
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public static void addProperties(Properties pp) throws Exception {
		Enumeration<Object> en = pp.keys();
		
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			map.put(key, pp.getProperty(key));
		}
	}
	
	/**
	 * Adds properties from provided property file to the map, overriding values for existing keys.
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public static void loadPropertiesFromFile(String filename) throws Exception {		
		map.load(new FileInputStream(filename));
	}
	
	/**
	 * Resets map and loads provided properties to the map.
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public static void resetProperties(Properties pp) throws Exception {
		map.clear();
		map = new Properties();
	}
	
	public static String getProperty(String key){
		return map.getProperty(key);
	}
	
	public static void setProperty(String key, String value){
		map.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static LinkedList<ITask> getJob(String key){
		return jobs.get(key);
	}
	
	/**
	 * Load properties:
	 * 		- directory names for transform input/output
	 * 		- mainframe login parameters to use for file transfers from eBCI to mainframe
	 * 
	 */	
	public static void loadProperties() throws NamingException {

		log.debug("loadProperties");

		InitialContext context = new InitialContext();
		//TODO
	}
	
	
	public static void loadBatchProperties(String batchCfgFile) throws Exception {
		String fp = "loadJobs: ";
		log.debug(fp + "attempting to parse incoming doc");

       String batchJNDI = null;
        try {
        	//get instance of DocumentBuilderFactory
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        //get instance of XML DocumentBuilder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        //parse the document using DOM; at this point all doc is already sitting in memory!
	        Document doc = db.parse(new File(batchCfgFile));
            log.debug(fp + "Document parsed");
           
            //getting jndi lookup name
            NodeList nodes = doc.getElementsByTagName("jndi-name");
            nodes = nodes.item(0).getChildNodes();
            batchJNDI = nodes.item(0).getNodeValue();
            log.debug(fp + "batch JNDI name extracted: " + batchJNDI);
            
            //get properties
            NodeList ppNodes = doc.getElementsByTagName("ctsagent");
            if (ppNodes==null || ppNodes.getLength() <= 0 || !ppNodes.item(0).hasChildNodes())
            	log.info(fp + "ctsagent batch configuration has no properties");
            
            NodeList agentNodeList = ppNodes.item(0).getChildNodes();
            if (agentNodeList==null || agentNodeList.getLength() <= 0 || !agentNodeList.item(0).hasChildNodes())
            	log.info(fp + "ctsagent batch configuration has no properties");

            for (int i = 0; i < agentNodeList.getLength(); ++i) {
                Node node = agentNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)   {
                		Element custom = (Element) node;
                    	String propName = custom.getNodeName();
                    	String propValue = custom.getFirstChild().getNodeValue();
                    	log.debug(fp + "name: " + propName + ", value: " + propValue);
                    	
                    	map.put(propName, propValue);
                }
            }                   

            //get list of job configurations
            NodeList jNodes = doc.getElementsByTagName("job");
           
            if (jNodes==null || jNodes.getLength() <= 0 || !jNodes.item(0).hasChildNodes()){
            	log.info(fp + "ctsagent batch configuration has no jobs");
            	return;
            }
            log.debug(fp + "got list of " + jNodes.getLength() + " jobs");
            
            //for each tag with name job
            for(int i=0;i<jNodes.getLength();i++){
            	Node jobNode = jNodes.item(i);
            	//get job id to use as a key
            	NamedNodeMap attrMap = jobNode.getAttributes();
            	String jobId = (attrMap.getNamedItem("id")).getTextContent();
            	log.info(fp + "jobId: " + jobId);
            	
            	//get task nodes, convert them to POJO and compile task list (job)
            	NodeList taskNodes = jobNode.getChildNodes();
            	
            	if(taskNodes==null || taskNodes.getLength()<=0){
            		log.info(fp + "jobId " + jobId + " has no tasks, continue..");
            		continue;
            	}
            	int numOfTaskForJob = taskNodes.getLength();
            	log.debug(fp + "got list of " + numOfTaskForJob + " child nodes for job");
            	
            	//prepare class loader for loading tasks
            	ClassLoader classLoader = BatchInitiatorBean.class.getClassLoader();
            	LinkedList<ITask> job = new LinkedList<>();
            	
            	//for each tag with name task
            	for(int j=0;j<numOfTaskForJob;j++){
            		Node taskNode = taskNodes.item(j);
            		//log.info(fp + "got taskNode: " + taskNode);
            		if(taskNode==null || !taskNode.getNodeName().equalsIgnoreCase("task")){
            			continue;
            		}
            		//log.debug(fp + "child node name: " + taskNode.getNodeName());
            		NamedNodeMap taskPropMap = taskNode.getAttributes();
            		//log.info(fp + "got task attributes: " + taskPropMap);
            		String taskId 			= (taskPropMap.getNamedItem("id")).getTextContent();
            		//log.info(fp + "taskId: " + taskId);
            		String taskSequence 	= (taskPropMap.getNamedItem("sequence")).getTextContent();
            		//log.info(fp + "taskSequence: " + taskSequence);
            		String taskClass 		= (taskPropMap.getNamedItem("class")).getTextContent();
            		//log.info(fp + "taskClass: " + taskClass);

            		//load task based on class from the configuration 
            		Class<?> myObjectClass = classLoader.loadClass(taskClass);
        			ITask task = (ITask) myObjectClass.newInstance();
        			task.setId(taskId);
        			task.setJobId(jobId);
        			task.setSequence(Integer.parseInt(taskSequence));
        			
        			job.add(task);
        			
            	}      	//end of for each task
            	
            	Collections.sort(job);
            	
            	log.info(fp + "job "+jobId+":");
            	log.info(job);
            	
            	AppProperties.jobs.put(jobId, job);
            }		//end of for each job            	
        } catch(Exception ex) {
            log.warn("Failed to parse baych config to load jobs", ex);
        }
        
                /* for testing only - to remove
        	batchProperties = new Properties();     	
        	batchProperties.load(new FileInputStream(args));
        	batchProperties.list(System.out);
        		AppProperties.addProperties(batchProperties);
        	*/
	}


	//no instantiating allowed
	private AppProperties(){}
	
	//no cloning
	@Override
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("Cloning is not supported!");
	}
}
