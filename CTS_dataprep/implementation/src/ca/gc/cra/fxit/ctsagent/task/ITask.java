/**
 * CRA ITB 2016-08-11
 */
package ca.gc.cra.fxit.ctsagent.task;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 * @author Txs285
 *
 */
public interface ITask extends Comparable<ITask> {
	
	public int execute(PackageInfo p);
	public int getResultCode();
	public String getResultMessage();
	public void setResultCode(int c);
	public void setResultMessage(String s);
	
	public String getId();
	public void setId(String id);
	public int getSequence();
	public void setSequence(int sequence);

	public String getJobId();
	public void setJobId(String jobId);
}
