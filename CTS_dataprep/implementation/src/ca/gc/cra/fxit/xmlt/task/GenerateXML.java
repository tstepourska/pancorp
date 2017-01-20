package ca.gc.cra.fxit.xmlt.task;

import org.apache.log4j.Logger;


import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.IHelper;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class GenerateXML extends AbstractTask {

	private static Logger log = Logger.getLogger(GenerateXML.class);
	
	@Override
	public GenerateXML cloneTask(){
		GenerateXML t = new GenerateXML();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}

	@Override
	protected final int invoke(PackageInfo p) {
		String fp = "invoke: ";
		log.debug(fp + "GenerateXML executing");

		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {			
			String dataProvider		= p.getDataProvider();
			//log.debug("dataProvider: " + dataProvider);
			
			String messageType = p.getPackageType();
			//log.debug("messageType: " + messageType);
			
			//load appropriate helper 
			ClassLoader classLoader = GenerateXML.class.getClassLoader();
			Class<?> myObjectClass 	= null;
			
			if(messageType.equalsIgnoreCase(Constants.PKG_TYPE_DATA)){
				myObjectClass 	= classLoader.loadClass(Constants.JAVA_PKG_TASK + "xml." + dataProvider+ ".Helper");
			}
			else if(messageType.equalsIgnoreCase(Constants.PKG_TYPE_STATUS_MESSAGE)){
				myObjectClass 	= classLoader.loadClass(Constants.JAVA_PKG_TASK + "xml.sm.Helper");
			}
			IHelper helper 			= (IHelper) myObjectClass.newInstance();
			status 					= helper.invoke(p);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(log, e);
		}
		
		//for wireframe testing only - to comment out!
		//status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
		log.debug(fp + "status: " + status);
		return status;
	}
}
