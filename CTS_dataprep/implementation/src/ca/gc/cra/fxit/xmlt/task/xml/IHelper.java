package ca.gc.cra.fxit.xmlt.task.xml;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;

public interface IHelper {
	public String[] getSchemas();
	int invoke(PackageInfo p);
	int transform(PackageInfo p);
	int validate(PackageInfo p,final String[] schemas, String xmlFileLocation) throws Exception;
}
