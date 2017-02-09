package ca.gc.cra.fxit.xmlt.task.xml.cbc;

//import org.apache.log4j.Logger;

//import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.xml.AbstractXmlHelper;
//import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;
//import ca.gc.cra.fxit.xmlt.util.Utils;
import ca.gc.cra.fxit.xmlt.util.Globals;

public class Helper extends AbstractXmlHelper {
	//private static Logger log = Logger.getLogger(Helper.class);
	
	/**
	 * Does not do anything as CBC provides their own XML file
	 * It is here for the interface only
	 */
	@Override
	public int transform(PackageInfo p){
		return Constants.STATUS_CODE_SUCCESS;
	}
	
	@Override
	public String[] getSchemas(){
		String[] xsdpaths = new String[] {
				  Globals.schemaLocationBaseDir +"cbc/isocbctypes_v1.0.xsd",
				  Globals.schemaLocationBaseDir +"cbc/oecdtypes_v4.1.xsd",
				  Globals.schemaLocationBaseDir + "cbc/" + Constants.MAIN_SCHEMA_NAME};
		return xsdpaths;
	}
}