/**
 * CRA ITB 2016-08-11
 */
package ca.gc.cra.fxit.ctsagent.steps;

import java.util.Map;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 * @author Txs285
 *
 */
public interface IStep {
	
	//public void executeTask(PackageInfo p);
	public int execute(PackageInfo p);
	//public void setExecutionParms(Map<String,Object> m) throws Exception;
	public int getResultCode();
	public String getResultMsg();
	public void setResultCode(int c);
	public void setResultMessage(String s);
}
