package ca.gc.cra.fxit.xmlt.task.xml.etr;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	private static Logger lg = Logger.getLogger(Helper.class);
	
	@Override
	public final int invoke(PackageInfo p){
		lg.info("ETR Helper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;

		try {
			//generate XML
			status = transform(p);
			if(lg.isDebugEnabled())
			lg.debug("status: " + status);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		
		//TODO for wireframe testing only, to remove!
		//status=Constants.STATUS_CODE_SUCCESS;
		if(status==Constants.STATUS_CODE_SUCCESS){
			//reset status for validation
			status = Constants.STATUS_CODE_INCOMPLETE;
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
		}
		
		//TODO to remove
		status= Constants.STATUS_CODE_SUCCESS;
		
		return status;
	}
	
	@Override
	public int transform(PackageInfo p){
		//p.setXmlFilename(xmlFilename);
		return Constants.STATUS_CODE_SUCCESS;
	}

	@Override
	public String[] getSchemas() {
		String[] xsdpaths = new String[] {
				Constants.RESOURCE_BASE_PKG +"schema/etr/isoetrtypes_v1.0.xsd",
				Constants.RESOURCE_BASE_PKG +"schema/etr/oecdtypes_v4.1.xsd",
				Constants.RESOURCE_BASE_PKG +"schema/etr/" + Constants.MAIN_SCHEMA_NAME
		};
		return xsdpaths;
	}
}