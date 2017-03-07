package ca.gc.cra.fxit.xmlt.task.xml.cbc;

import org.apache.log4j.Logger;

//import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.Constants;
//import ca.gc.cra.fxit.xmlt.util.Utils;
//import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);	
	
	@Override
	public final int invoke(PackageInfo p){
		lg.info("CBC Helper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		String outputFile = p.getFileWorkingDir() + p.getXmlFilename();		//Globals.FILE_WORKING_DIR
		lg.debug("outputFile: " + outputFile);
		String[] xsdpaths = getSchemas();
					
		try {
			status = this.validate(p, xsdpaths, outputFile);
			lg.info("Validation completed with status " + status);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
		}
		
		//TODO to remove
		//status= Constants.STATUS_CODE_SUCCESS;
		
		return status;
	}
	
	/**
	 * Does not do anything as CBC provides their own XML file
	 * It is here for the interface only
	 */
	@Override
	public int transform(PackageInfo p){
		//p.setXmlFilename(xmlFilename);
		return Constants.STATUS_CODE_SUCCESS;
	}
	
	@Override
	public String[] getSchemas(){
		String[] xsdpaths = new String[] {
				Constants.RESOURCE_BASE_PKG +"schema/cbc/isocbctypes_v1.0.xsd",
				Constants.RESOURCE_BASE_PKG +"schema/cbc/oecdtypes_v4.1.xsd",
				Constants.RESOURCE_BASE_PKG +"schema/cbc/" + Constants.MAIN_SCHEMA_NAME
				  };
		return xsdpaths;
	}
}