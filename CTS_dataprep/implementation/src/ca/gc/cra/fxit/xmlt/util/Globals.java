package ca.gc.cra.fxit.xmlt.util;

import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.xmlt.batch.BatchInitiatorBeanOutbound;
import ca.gc.cra.fxit.xmlt.task.ITask;

/**
 * Domain properties used by the transformation process.
 * 
 */
public class Globals {

	private static Logger log = Logger.getLogger(Globals.class);

	//public static String fileTransferLocalDirName = ""; 
	//public static String mainframeHost = "";
	//public static String mainframeUserid = "";
	//public static String mainframePassword = "";
	public static String databaseEnvironment = "";
	public static String docTypeIndicEnv = "";
	public static String mailFromAddress = "";
	public static String mailToAddressList = "";
	//for metadata
	public static String mailSenderAddressList = null; //"casd@cra.gc.ca";
	public static boolean sendMailFlag = false;

	//public static String configDir 				= null;//"C:/git/repository2/CTS_XMLT/CTS_dataprep/implementation/cfg/";
	//public static String schemaLocationBaseDir  = "C:/git/repository2/CTS_XMLT/CTS_dataprep/implementation/schema/";
	public static String baseFileDir;// 			= null; //"C:/git/repository2/CTS_XMLT/CTS_dataprep/test/testfiles/";
	//public static String baseFileDir 			= "/disk/data/weblogic/domains/wls-domain/applications/data/fxit/xmlt/outbound/unprocessed/";	
	public static String FILE_WORKING_DIR; //	= baseFileDir+ Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR + Constants.TEMP_DIR;	
			
	public static long defaultMaxPkgSize;
	public static boolean defaultPkgCompressed 			= false; //true;
	public static long fileSizeConstant 				= 0;//2000;
	//public static double PkgCompressionRatio 			= 0;// 0.01;
	public static double payloadCompressionRatio 		= 0;//0.2;
	public static double txtToXmlFactor 				= 1;//3.2;
	public static long fileSignatureSizeConstant 		= 0;//900;
	
	//public static Level loglevel;
	
	
	//public static double FileSizeSpareLinesPercent		= 0;//0.2;
	//public static long specificMaxXmlFileSize = -1;
	//public static String[] specificMaxSizeDest = null;
	public static HashMap<String, FileSize> specificFileSizes = null;
	
	public static String[] DATA_PROVIDERS;
	
	private static Properties map = new Properties();
	private static Map<String, LinkedList<ITask>> jobs = new HashMap<>();

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
	public static void loadDomainProperties() throws NamingException, Exception {
		log.debug("loadDomainProperties");
		InitialContext context = new InitialContext();		
		baseFileDir = context.lookup(Constants.JNDI_BATCH_DATA_DIRECTORY).toString();
		//set the working directory
		FILE_WORKING_DIR = baseFileDir+ Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR + Constants.TEMP_DIR;
	/*	
		String strLogLevel = context.lookup(Constants.KEY_LOG_LEVEL).toString();
		if(strLogLevel==null){}
		else if(strLogLevel.equalsIgnoreCase("debug"))
			loglevel = Level.DEBUG;
		else if(strLogLevel.equalsIgnoreCase("info"))
			loglevel = Level.INFO;
			*/
		//unprocessedInputDirName = context.lookup(JNDINames.JNDI_BATCH_UNPROCESSED_INPUT).toString();
		//xmlOutputDirName = context.lookup(JNDINames.JNDI_BATCH_XML_OUTPUT).toString();
		//flatFileOutputDirName = context.lookup(JNDINames.JNDI_BATCH_FLAT_FILE_OUTPUT).toString();
		//fileTransferLocalDirName = context.lookup(JNDINames.JNDI_BATCH_FILE_TRANSFER_LOCALDIR).toString();
		//mainframeHost = context.lookup(JNDINames.JNDI_MAINFRAME_HOST).toString();
		//mainframeUserid = context.lookup(JNDINames.JNDI_MAINFRAME_USERID).toString();
		//String password = context.lookup(JNDINames.JNDI_MAINFRAME_PASSWORD).toString();
		databaseEnvironment = context.lookup(Constants.JNDI_FXMT_ENV).toString();
		docTypeIndicEnv = context.lookup(Constants.JNDI_DOCTYPEINDIC_ENV).toString();
		mailFromAddress = context.lookup(Constants.JNDI_FXIT_MAIL_FROM_ADDRESS).toString();
		mailToAddressList = context.lookup(Constants.JNDI_FXIT_MAIL_TO_ADDRESS).toString();
		//for metadata
		mailSenderAddressList = context.lookup(Constants.JNDI_FXIT_MAIL_TO_ADDRESS).toString();
		sendMailFlag = Boolean.parseBoolean((String)context.lookup(Constants.JNDI_FXIT_MAIL_SEND_FLAG));
		log.info("Domain properties loaded");
	}
	
	/**
	 * Config file is in the resources directory
	 * @param batchCfgFile
	 * @throws Exception
	 */
	public static void loadBatchProperties() throws Exception {
		String fp = "loadBatchProperties: ";
		String batchCfgFile = Constants.RESOURCE_BASE_PKG + "fxit.xmlt.batch.xml";
		if(log.isDebugEnabled())
		log.debug(fp + "batchCfgFile: " + batchCfgFile);
		InputStream is = null;
       String batchJNDI = null;
        try {
        	//get instance of DocumentBuilderFactory
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        //get instance of XML DocumentBuilder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        //parse the document using DOM; at this point all doc is already sitting in memory!
	      //  Document doc = db.parse(new File(batchCfgFile));
	        is = Globals.class.getClassLoader().getResourceAsStream(batchCfgFile);
	        Document doc = db.parse(is);
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
                    	else if(propName.equalsIgnoreCase(Constants.KEY_DEFAULT_MAX_PKG_SIZE)&& propValue!=null &&propValue.trim().length()>0){
                    		defaultMaxPkgSize = Long.parseLong(propValue);
                    		compressed = custom.getAttributeNode("compressed");
                    		defaultPkgCompressed = (new Boolean(compressed.getNodeValue())).booleanValue();
                    	}
                    	else if(propName.equalsIgnoreCase(Constants.KEY_SPECIFIC_MAX_FILE_SIZE)){
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
                    	else if(propName.equalsIgnoreCase(Constants.KEY_FILE_SIGNATURE_SIZE_CONSTANT)&& propValue!=null &&propValue.trim().length()>0){
                    		fileSignatureSizeConstant = Long.parseLong(propValue);
                    	}
                    	else if(propName.equalsIgnoreCase(Constants.KEY_FILE_SIZE_CONSTANT)&& propValue!=null &&propValue.trim().length()>0){
                    		fileSizeConstant = Long.parseLong(propValue);
                    	}
                    	else if(propName.equalsIgnoreCase(Constants.KEY_TXT_TO_XML_FACTOR)&& propValue!=null &&propValue.trim().length()>0){
                    		txtToXmlFactor = Double.parseDouble(propValue);
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
            	Globals.jobs.put(jobId, job);
            }		//end of for each job            	
        } catch(Exception ex) {
            Utils.logError(log, ex);
            throw ex;
        }
        finally {
        	try {
        		is.close();
        	}catch(Exception e){}
        }
        
                /* for testing only - to remove
        	batchProperties = new Properties();     	
        	batchProperties.load(new FileInputStream(args));
        	batchProperties.list(System.out);
        		Globals.addProperties(batchProperties);
        	*/
	}
	
	/**
	 * Compiles a string to print with all properties
	 * 
	 * @return String
	 */
	public static String toStaticString(){
		StringBuilder sb = new StringBuilder();
		
		Enumeration<Object> keys = map.keys();
		String key;
		while(keys.hasMoreElements()){
			key = (String)keys.nextElement();
			sb.append("\n").append(key).append("=").append(map.get(key));
		}
		
		sb
		.append("\n").append("databaseEnvironment").append("=").append(databaseEnvironment).append(";")
		.append("\n").append("docTypeIndicEnv").append("=").append(docTypeIndicEnv).append(";")
		.append("\n").append("mailFromAddress").append("=").append(mailFromAddress).append(";")
		.append("\n").append("mailToAddressList").append("=").append(mailToAddressList).append(";")
		//for metadata
		.append("\n").append("mailSenderAddressList").append("=").append(mailSenderAddressList).append(";")
		.append("\n").append("sendMailFlag").append("=").append(sendMailFlag).append(";")
		//.append("\n").append(Constants.KEY_LOG_LEVEL).append("=").append(loglevel).append(";")
		.append("\n").append("baseFileDir").append("=").append(baseFileDir).append(";")
		.append("\n").append("FILE_WORKING_DIR")						.append("=").append(FILE_WORKING_DIR).append(";")			
		.append("\n").append(Constants.KEY_DEFAULT_MAX_PKG_SIZE)		.append("=").append(defaultMaxPkgSize).append(";")
		.append("\n").append("defaultPkgCompressed")					.append("=").append(defaultPkgCompressed).append(";")
		.append("\n").append(Constants.KEY_FILE_SIZE_CONSTANT)			.append("=").append(fileSizeConstant).append(";")
		//.append("\n").append(Constants.KEY_PKG_COMPRESSION_RATIO)		.append("=").append(PkgCompressionRatio).append(";")
		.append("\n").append(Constants.KEY_PAYLOAD_COMPRESSION_RATIO)	.append("=").append(payloadCompressionRatio).append(";")
		.append("\n").append(Constants.KEY_TXT_TO_XML_FACTOR)			.append("=").append(txtToXmlFactor).append(";")
		.append("\n").append(Constants.KEY_FILE_SIGNATURE_SIZE_CONSTANT).append("=").append(fileSignatureSizeConstant).append(";")
		;
		
		if(specificFileSizes!= null && specificFileSizes.size()>0){
			Iterator<String> it = specificFileSizes.keySet().iterator();
			while(it.hasNext()){
				key = it.next();
				sb.append("\n").append(key).append("=").append(specificFileSizes.get(key)).append(";");
			}
		}
		
		if(DATA_PROVIDERS!=null && DATA_PROVIDERS.length>0){
			for(String s : DATA_PROVIDERS){
				sb.append("\nData provider: ").append(s).append(";");
			}
		}
		
		sb.append("\n").append(Globals.getJobs());
		
		return sb.toString();
	}

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
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb
			.append("\ncountry=").append(countryCode)
			.append(",size=").append(size)
			.append(",compressed=").append(compressed);
			return sb.toString();
		}
	}
}
