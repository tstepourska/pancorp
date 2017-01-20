package ca.gc.cra.fxit.xmlt.task.xml.etr;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger log = Logger.getLogger(Helper.class);
	
	@Override
	public int invoke(PackageInfo p){
		log.debug("ETR XmlHelper started");

		int status = this.validate(p, 
				   AppProperties.schemaLocationBaseDir + p.getDataProvider() + Constants.MAIN_SCHEMA_NAME, 
				   AppProperties.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR + p.getXmlFilename());
		
		return status;
	}
	
	@Override
	public int transform(PackageInfo p){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		return status;
	}
}