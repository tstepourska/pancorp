package ca.gc.cra.fxit.xmlt.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class ExtractStatistics extends AbstractTask {
	private static Logger lg = Logger.getLogger(ExtractStatistics.class);
	
	@Override
	protected int invoke(PackageInfo p) {
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String[] fileArray = new String[2];
		
		try {
			String[] tags = this.getStatsConfig(p.getDataProvider());
			HashMap<String,Integer> results = new HashMap<String,Integer>();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(new File(fileArray[0].toString())));
			NodeList nn = null;
			int num = -1;
			
			for(String tag : tags) { 
				nn = doc.getElementsByTagName(tag);
				num = nn.getLength();
				results.put(tag, num);
			}
			
			status = insertStats(results);

			///XPathFactory factory = XPathFactory.newInstance();
		    //XPath xpath = factory.newXPath();
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		return status;
	}
	
	private int insertStats(HashMap<String,Integer> m){
		int status = -1;
		
		//TODO
		
		
		return status;
	}
	
	/**
	 * Inserts stats into database
	 * 
	 * @param dataProvider
	 * @return
	 * @throws Exception
	 */
	public String[] getStatsConfig(String dataProvider) throws Exception {
		String s 					= Constants.RESOURCE_BASE_PKG + Constants.STATS_CFG;
		Properties pp 				= new Properties();		 
		ClassLoader classLoader 	= ExtractStatistics.class.getClassLoader();				 
		InputStream is 				= classLoader.getResourceAsStream(s);
		pp.load(is);
		 
		String stList = pp.getProperty(dataProvider);
		String[] arr = stList.split(Constants.COMMA);		 
		 
		return arr;
	}
	
	@Override
	public ITask cloneTask() throws Exception {
		ExtractStatistics t = new ExtractStatistics();
		
		return t;
	}
}
