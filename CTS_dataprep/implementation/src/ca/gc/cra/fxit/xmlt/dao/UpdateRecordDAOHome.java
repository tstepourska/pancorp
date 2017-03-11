package ca.gc.cra.fxit.xmlt.dao;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface UpdateRecordDAOHome extends EJBHome {

	public static final String jndiName = "ca.gc.cra.fxit.xmlt.dao.UpdateRecordDAOHome";

	public UpdateRecordDAOInterface create() throws RemoteException, CreateException;

}