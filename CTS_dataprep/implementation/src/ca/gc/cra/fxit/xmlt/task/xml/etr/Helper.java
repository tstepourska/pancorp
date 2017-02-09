package ca.gc.cra.fxit.xmlt.task.xml.etr;

//import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
import ca.gc.cra.fxit.xmlt.util.*;

public class Helper extends AbstractXmlHelper {
	//private static Logger lg = Logger.getLogger(Helper.class);
	
	@Override
	public int transform(PackageInfo p){
		
		return Constants.STATUS_CODE_SUCCESS;
	}

	@Override
	public String[] getSchemas() {
		String[] xsdpaths = new String[] {
				  Globals.schemaLocationBaseDir +"etr/isoetrtypes_v1.0.xsd",
				  Globals.schemaLocationBaseDir +"etr/oecdtypes_v4.1.xsd",
				  Globals.schemaLocationBaseDir + "etr/" + Constants.MAIN_SCHEMA_NAME};
		return xsdpaths;
	}
}