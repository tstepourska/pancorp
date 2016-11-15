package ca.gc.cra.fxit.ctsagent.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.task.xml.IHelper;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.util.Utils;

public class ValidateXML extends AbstractTask{
	private static Logger log = Logger.getLogger(ValidateXML.class);
	
	protected final int invoke(PackageInfo p) {
	log.debug("ValidateXML executing");

	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	try {			
		String dataProvider 		= p.getDataProviderPrefix();
		log.debug("dataProvider: " + dataProvider);
		//load appropriate helper 
		ClassLoader classLoader = ValidateXML.class.getClassLoader();
		Class<?> myObjectClass 	= classLoader.loadClass(Constants.JAVA_PKG_STEP + "xml." + dataProvider+ ".XmlHelper");
		IHelper helper 			= (IHelper) myObjectClass.newInstance();
		status 					= helper.invoke(p);
	}
	catch(Exception e){
		status = Constants.STATUS_CODE_ERROR;
		Utils.logError(log, e);
	}
	return status;
	}
}
