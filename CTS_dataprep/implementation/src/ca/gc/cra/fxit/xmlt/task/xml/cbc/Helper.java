package ca.gc.cra.fxit.xmlt.task.xml.cbc;

import org.apache.log4j.Logger;

//import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.AppProperties;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class Helper extends AbstractXmlHelper {
	private static Logger log = Logger.getLogger(Helper.class);
	
	@Override
	public int invoke(PackageInfo p){
		log.info("CBC XmlHelper started");
		int status = this.validate(p, 
								   AppProperties.schemaLocationBaseDir + p.getDataProvider() + Constants.MAIN_SCHEMA_NAME, 
								   AppProperties.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getXmlFilename());
		return status;
	}
	
	/**
	 * Does not do anything as CBC provides their own XML file
	 * It is here for the interface only
	 */
	@Override
	public int transform(PackageInfo p){
		return 0;
	}
}