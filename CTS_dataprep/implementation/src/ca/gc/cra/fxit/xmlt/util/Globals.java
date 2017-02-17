package ca.gc.cra.fxit.xmlt.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
//import java.util.Enumeration;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.xmlt.batch.BatchInitiatorBeanOutbound;
import ca.gc.cra.fxit.xmlt.task.ITask;

//import ca.gc.ccra.rccr.security.crypto.business.*;

/**
 * Domain properties used by the transformation process.
 * 
 */
public class Globals {

	private static Logger log = Logger.getLogger(Globals.class);

	//values from configuration file
	//public static String baseDir = "";
	//public static String unprocessedInputDirName = "";
	//public static String xmlOutputDirName = ""; 
	//public static String flatFileOutputDirName = ""; 
	public static String fileTransferLocalDirName = ""; 
	public static String mainframeHost = "";
	public static  String mainframeUserid = "";
	public static  String mainframePassword = "";
	public static String databaseEnvironment = "";
	public static String docTypeIndicEnv = "";
	public static String mailFromAddress = "";
	public static String mailToAddressList = "";
	//for metadata
	public static String mailSenderAddressList = "casd@cra.gc.ca";
	public static boolean sendMailFlag = false;
	
	//public static String configDir = "C:/git/repository/CTS_dataprep/implementation/cfg/";
	//public static String schemaLocationBaseDir  = "C:/git/repository/CTS_dataprep/implementation/schema/";
	//public static String baseFileDir = "C:/git/repository/CTS_dataprep/test/testfiles/";
	
	public static String configDir 				= "C:/git/repository2/CTS_XMLT/CTS_dataprep/implementation/cfg/";
	public static String schemaLocationBaseDir  = "C:/git/repository2/CTS_XMLT/CTS_dataprep/implementation/schema/";
	public static String baseFileDir 			= "C:/git/repository2/CTS_XMLT/CTS_dataprep/test/testfiles/";
	
	public static final String FILE_WORKING_DIR			= baseFileDir+ Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR + Constants.TEMP_DIR;
	
			
	public static long defaultMaxPkgSize;
	public static boolean defaultPkgCompressed 			= true;
	public static long FileSizeConstant 				= 2000;
	public static double PkgCompressionRatio 			= 0.01;
	public static double PayloadCompressionRatio 		= 0.2;
	public static double TxtToXmlFactor 				= 3.2;
	public static long FileSignatureSizeConstant 		= 900;
	public static double FileSizeSpareLinesPercent		= 0.2;
	//public static long specificMaxXmlFileSize = -1;
	//public static String[] specificMaxSizeDest = null;
	public static HashMap<String, FileSize> specificFileSizes = null;
	
	public static String[] DATA_PROVIDERS;
	
	private static Properties map = new Properties();
	private static Map<String, LinkedList<ITask>> jobs = new HashMap<>();
	
	/**
	 * Adds provided properties to the map, overriding values for existing keys.
	 * 
	 * @param pp
	 * @throws Exception
	 */
/*	public static void addProperties(Properties pp) throws Exception {
		Enumeration<Object> en = pp.keys();
		
		while(en.hasMoreElements()){
			String key = (String)en.nextElement();
			map.put(key, pp.getProperty(key));
		}
	}*/
	
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
	
	public static Map<String, LinkedList<ITask>> getJobs(){
		return jobs;
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
	
	/**
	 * 
	 * @param batchCfgFile
	 * @throws Exception
	 */
	public static void loadBatchProperties(String batchCfgFile) throws Exception {
		String fp = "loadBatchProperties: ";
		log.debug(fp + "batchCfgFile: " + batchCfgFile);

       String batchJNDI = null;
        try {
        	//get instance of DocumentBuilderFactory
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        //get instance of XML DocumentBuilder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        //parse the document using DOM; at this point all doc is already sitting in memory!
	        Document doc = db.parse(new File(//Globals.configDir + 
	        		batchCfgFile));
           // log.debug(fp + "Document parsed");
           
            //getting jndi lookup name
            NodeList nodes = doc.getElementsByTagName("jndi-name");
            nodes = nodes.item(0).getChildNodes();
            batchJNDI = nodes.item(0).getNodeValue();
           // log.debug(fp + "batch JNDI name extracted: " + batchJNDI);
            
            //get properties
            NodeList ppNodes = doc.getElementsByTagName("xmlt");
            if (ppNodes==null || ppNodes.getLength() <= 0 || !ppNodes.item(0).hasChildNodes()){
            	;//log.info(fp + "ctsagent batch configuration has no properties");
            }
            
            NodeList agentNodeList = ppNodes.item(0).getChildNodes();
            if (agentNodeList==null || agentNodeList.getLength() <= 0 || !agentNodeList.item(0).hasChildNodes())
            	//log.info(fp + "ctsagent batch configuration has no properties");

            for (int i = 0; i < agentNodeList.getLength(); ++i) {
                Node node = agentNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)   {
                		Element custom = (Element) node;
                    	String propName = custom.getNodeName();
                    	String propValue = custom.getFirstChild().getNodeValue();
                    //	log.debug(fp + "name: " + propName + ", value: " + propValue);
                    	Attr compressed = null;
                    	Attr dest = null;
                    	String compressedValue = null;
                    	String destValue = null;
                    	long sizeValue = -1;
                    	
                    	if(propName.equalsIgnoreCase(Constants.KEY_DATA_PROVIDERS)&& propValue!=null &&propValue.trim().length()>0){
                    		DATA_PROVIDERS = propValue.split(",");
                    	}                    	
                    	else if(propName.equalsIgnoreCase(Constants.DEFAULT_MAX_PKG_SIZE_KEY)&& propValue!=null &&propValue.trim().length()>0){
                    		defaultMaxPkgSize = Long.parseLong(propValue);
                    		compressed = custom.getAttributeNode("compressed");
                    		defaultPkgCompressed = (new Boolean(compressed.getNodeValue())).booleanValue();
                    	}
                    	else if(propName.equalsIgnoreCase(Constants.SPECIFIC_MAX_FILE_SIZE_KEY)){
                    		if(specificFileSizes==null)
                    			specificFileSizes = new HashMap<>();
                    			
                    		try {
                    			sizeValue = Long.parseLong(propValue);
                    			
                    			compressed = custom.getAttributeNode("compressed");
                    			compressedValue = compressed.getNodeValue();
                    			dest = custom.getAttributeNode("dest");
                    			destValue = dest.getNodeValue();
                    			
                    			if(sizeValue<=0 ||destValue==null)
                    				throw new IllegalArgumentException();
                    			
                    			specificFileSizes.put(destValue, new FileSize(sizeValue,destValue,(new Boolean(compressedValue)).booleanValue()));
                    		}
                    		catch(Exception e){
                    			Utils.logError(log, e);
                    		}                	                 		
                    	}
                    	else {
                    	map.put(propName, propValue);
                    	}
                }
            }                   

            //get list of job configurations
            NodeList jNodes = doc.getElementsByTagName("job");
           
            if (jNodes==null || jNodes.getLength() <= 0 || !jNodes.item(0).hasChildNodes()){
            	//log.info(fp + "ctsagent batch configuration has no jobs");
            	return;
            }
          //  log.debug(fp + "got list of " + jNodes.getLength() + " jobs");
            
            //for each tag with name job
            for(int i=0;i<jNodes.getLength();i++){
            	Node jobNode = jNodes.item(i);
            	//get job id to use as a key
            	NamedNodeMap attrMap = jobNode.getAttributes();
            	String jobId = (attrMap.getNamedItem("id")).getTextContent();
            	//log.info(fp + "jobId: " + jobId);
            	
            	//get task nodes, convert them to POJO and compile task list (job)
            	NodeList taskNodes = jobNode.getChildNodes();
            	
            	if(taskNodes==null || taskNodes.getLength()<=0){
            		//log.info(fp + "jobId " + jobId + " has no tasks, continue..");
            		continue;
            	}
            	int numOfTaskForJob = taskNodes.getLength();
            	//log.debug(fp + "got list of " + numOfTaskForJob + " child nodes for job");
            	
            	//prepare class loader for loading tasks
            	ClassLoader classLoader = BatchInitiatorBeanOutbound.class.getClassLoader();
            	LinkedList<ITask> job = new LinkedList<>();
            	
            	//for each tag with name task
            	for(int j=0;j<numOfTaskForJob;j++){
            		Node taskNode = taskNodes.item(j);
            		//log.info(fp + "got taskNode: " + taskNode);
            		if(taskNode==null || !taskNode.getNodeName().equalsIgnoreCase("step")){
            			continue;
            		}
            		//log.debug(fp + "child node name: " + taskNode.getNodeName());
            		NamedNodeMap taskPropMap = taskNode.getAttributes();
            		//log.info(fp + "got task attributes: " + taskPropMap);
            		String taskId 			= (taskPropMap.getNamedItem("id")).getTextContent();
            		//log.info(fp + "taskId: " + taskId);
            		//String taskSequence 	= (taskPropMap.getNamedItem("sequence")).getTextContent();
            		//log.info(fp + "taskSequence: " + taskSequence);
            		String taskClass 		= (taskPropMap.getNamedItem("class")).getTextContent();
            		//log.info(fp + "taskClass: " + taskClass);

            		//load task based on class from the configuration 
            		Class<?> myObjectClass = classLoader.loadClass(taskClass);
        			ITask task = (ITask) myObjectClass.newInstance();
        			task.setId(taskId);
        			task.setJobId(jobId);
        			//task.setSequence(Integer.parseInt(taskSequence));
        			
        			job.add(task);
        			
            	}      	//end of for each task
            	
            	Collections.sort(job);
            	
            	//log.info(fp + "job "+jobId+":");
            	//log.info(job);
            	
            	Globals.jobs.put(jobId, job);
            }		//end of for each job            	
        } catch(Exception ex) {
            Utils.logError(log, ex);//("Failed to parse baych config to load jobs", ex);
            throw ex;
        }
        
                /* for testing only - to remove
        	batchProperties = new Properties();     	
        	batchProperties.load(new FileInputStream(args));
        	batchProperties.list(System.out);
        		Globals.addProperties(batchProperties);
        	*/
	}

/*	public static void resetBatchProperties(){
		jobs.clear();
	}*/

	//no instantiating allowed
	private Globals(){}
	
	//no cloning
	@Override
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("Cloning is not supported!");
	}
	
	public static class FileSize {
		private long size;
		private String countryCode;
		private boolean compressed;
		
		public FileSize(long s, String cc, boolean c){
			this.size = s;
			this.countryCode = cc;
			this.compressed = c;
		}
		
		public long getSize(){
			return this.size;
		}
		
		public boolean isCompressed(){
			return this.compressed;
		}
		
		public String getCountryCode(){
			return this.countryCode;
		}
	}
}
