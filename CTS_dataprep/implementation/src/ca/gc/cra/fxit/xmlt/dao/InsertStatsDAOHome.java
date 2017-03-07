package ca.gc.cra.fxit.xmlt.dao;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface InsertStatsDAOHome extends EJBHome {

	public static final String jndiName = "ca.gc.cra.fxit.xmlt.dao.InsertStatsDAOHome";

	public InsertStatsDAOInterface create() throws RemoteException, CreateException;

}