package ca.gc.cra.fxit.ctsagent.util;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

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
	 * Load properties:
	 * 		- directory names for transform input/output
	 * 		- mainframe login parameters to use for file transfers from eBCI to mainframe
	 * 
	 */	
	public void loadProperties() throws NamingException {

		log.debug("loadProperties");

		InitialContext context = new InitialContext();
		//TODO
	}

	//no instantiating
	private AppProperties(){}
	
	//no cloning
	@Override
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException("Cloning is not supported!");
	}
}
