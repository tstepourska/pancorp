package ca.gc.cra.fxit.xmlt.dao;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import ca.gc.cra.db.framework.exceptions.DataException;
import ca.gc.cra.db.fxmt.ifaie.valueobject.IFAIEPackageData;

import java.util.IllegalFormatException;

public interface UpdateRecordDAOInterface extends EJBObject {

	// Get next MessageRefID for combination of taxation year, and recipient country code.
	// The MessageRefID value is the transmitter's unique identifier that identifies a package (which is a FATCA XML file).
	public int invoke(

			) throws DataException, RemoteException, IllegalFormatException, IndexOutOfBoundsException;

	// Create a package of information for International Financial Account Information Exchange.
	// The package data only contains identification and other information about the creation of the package 
	// (which is an XML file) and does not include the XML content.
	public IFAIEPackageData createPackageData(
			IFAIEPackageData data) throws DataException, RemoteException;

}
