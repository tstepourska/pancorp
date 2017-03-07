package ca.gc.cra.fxit.xmlt.batch;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

//import ca.gc.ccra.rccr.batch.integration.BatchProcess;
//import ca.gc.ccra.rccr.batch.integration.BatchProcessHome;

public interface BatchInitiatorHome extends EJBHome { //, BatchProcessHome {
	public static final String jndiName = "ca.gc.cra.fxit.xmlt.batch.BatchInitiatorHome";
	public IBatchInitiatorRemote create() throws RemoteException, CreateException;
	
	//@Override
	//public BatchProcess create() throws RemoteException, CreateException;
}