package ca.gc.cra.fxit.xmlt.job;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ExternalJobInitiatorHome extends EJBHome {
	public static final String jndiName = "ca.gc.cra.fxit.xmlt.job.ExternalJobInitiatorHome";
	public IExternalJobInitiatorRemote create() throws RemoteException, CreateException;
}