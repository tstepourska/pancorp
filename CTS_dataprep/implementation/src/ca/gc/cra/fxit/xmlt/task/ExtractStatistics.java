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

import ca.gc.cra.fxit.xmlt.dao.InsertStatsDAOBean;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class ExtractStatistics extends AbstractTask {
	private static Logger lg = Logger.getLogger(ExtractStatistics.class);
	
	@Override
	public int invoke(PackageInfo p) {
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			//get tags from configuration
			String[] tags = this.getStatsConfig(p.getDataProvider());
			lg.debug("got tags");
			HashMap<String,Integer> results = extract(p,tags);
		
			status = insertStats(results,p);
			lg.info("stats inserted with status: " + status);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		//this task is not critical for the process, 
		// set status to success in any case not to prevent from sending file out
		status = Constants.STATUS_CODE_SUCCESS;
		
		return status;
	}
	
	private HashMap<String,Integer> extract(PackageInfo p, String[] tags) throws Exception {
		HashMap<String,Integer> results = new HashMap<String,Integer>();
		String xmlFile = p.getFileWorkingDir() + p.getXmlFilename();
		lg.debug("xmlFile: " + xmlFile);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new FileInputStream(new File(xmlFile)));
		NodeList nn = null;
		int num = -1;
		
		for(String tag : tags) { 
			nn = doc.getElementsByTagName(tag);
			num = nn.getLength();
			lg.debug("Stats: " + tag + "::"+num);
			results.put(tag, num);
		}
		
		return results;
	}
	
	private int insertStats(HashMap<String,Integer> m, PackageInfo p) throws Exception {
		int status = -1;
		
		//TODO
		InsertStatsDAOBean dao = new InsertStatsDAOBean();
		dao.invoke(p);
		
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
		String fp = "getStatsConfig: ";
		String s 					= Constants.RESOURCE_BASE_PKG + Constants.STATS_CFG;
		if(lg.isDebugEnabled())
				lg.debug(fp + "s: " + s);
		Properties pp 				= new Properties();		 
		ClassLoader classLoader 	= ExtractStatistics.class.getClassLoader();				 
		InputStream is 				= classLoader.getResourceAsStream(s);
		if(lg.isDebugEnabled())
			lg.debug(fp + "inputStream: " + is);
		pp.load(is);
		 
		String stList = pp.getProperty(dataProvider);
		if(lg.isDebugEnabled())
			lg.debug(fp + "stList: " + stList);
		String[] arr = stList.split(Constants.COMMA);		 
		 
		return arr;
	}
	
	@Override
	public ITask cloneTask() throws Exception {
		ExtractStatistics t = new ExtractStatistics();
		
		return t;
	}
}
