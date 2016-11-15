package ca.gc.cra.fxit.ctsagent.task.xml.ftc;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.ctsagent.util.*;

public class XmlHelper extends AbstractXmlHelper {
	private static Logger log = Logger.getLogger(XmlHelper.class);
	public int invoke(PackageInfo p){
		log.debug("started");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		return status;
	}
}