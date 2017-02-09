package ca.gc.cra.fxit.xmlt.task.xml;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;

public interface IHelper {
	public String generateXMLFilename(PackageInfo p) throws Exception;
	public String[] getSchemas();
	int invoke(PackageInfo p);
}
