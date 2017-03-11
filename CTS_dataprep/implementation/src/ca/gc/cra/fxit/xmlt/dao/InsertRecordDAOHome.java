package ca.gc.cra.fxit.xmlt.dao;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface InsertRecordDAOHome extends EJBHome {

	public static final String jndiName = "ca.gc.cra.fxit.xmlt.dao.InsertRecordDAOHome";

	public InsertRecordDAOInterface create() throws RemoteException, CreateException;

}