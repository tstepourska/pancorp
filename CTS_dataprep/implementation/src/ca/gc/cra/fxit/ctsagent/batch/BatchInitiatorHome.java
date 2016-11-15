package ca.gc.cra.fxit.ctsagent.batch;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BatchInitiatorHome extends EJBHome {
	public static final String jndiName = "ca.gc.cra.fxit.ctsagent.batch.BatchInitiatorHome";
	public IBatchInitiatorRemote create() throws RemoteException, CreateException;
}