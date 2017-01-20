package ca.gc.cra.fxit.xmlt.batch;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BatchInitiatorHome extends EJBHome {
	public static final String jndiName = "ca.gc.cra.fxit.xmlt.batch.BatchInitiatorHome";
	public IBatchInitiatorRemote create() throws RemoteException, CreateException;
}