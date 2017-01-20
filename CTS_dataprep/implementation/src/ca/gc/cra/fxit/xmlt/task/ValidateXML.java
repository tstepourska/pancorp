package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.IHelper;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class ValidateXML extends AbstractTask{
	private static Logger log = Logger.getLogger(ValidateXML.class);
	
	@Override
	public ValidateXML cloneTask(){
		ValidateXML t = new ValidateXML();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
	
	
	@Override
	protected final int invoke(PackageInfo p) {
	log.debug("ValidateXML executing");

	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	try {			
		String dataProvider 		= p.getDataProvider();
		log.debug("dataProvider: " + dataProvider);
		//load appropriate helper 
		ClassLoader classLoader = ValidateXML.class.getClassLoader();
		Class<?> myObjectClass 	= classLoader.loadClass(Constants.JAVA_PKG_TASK + "xml." + dataProvider+ ".Helper");
		IHelper helper 			= (IHelper) myObjectClass.newInstance();
		status 					= helper.invoke(p);
	}
	catch(Exception e){
		status = Constants.STATUS_CODE_ERROR;
		Utils.logError(log, e);
	}
	
		//for wireframe testing only - to comment out!
		status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
			
	return status;
	}
}
