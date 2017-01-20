package ca.gc.cra.fxit.xmlt.batch;

import java.rmi.RemoteException;
import javax.ejb.Remote;

/**
 * 
 * @author Txs285
 *
 */

@Remote
public interface IBatchInitiatorRemote 
{
	public void execute(String args) throws RemoteException;
	public int getStatus();
}