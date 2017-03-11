package ca.gc.cra.fxit.xmlt.dao;


import javax.ejb.*;

import java.util.IllegalFormatException;
import java.util.Random;
import java.rmi.RemoteException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import ca.gc.cra.db.framework.exceptions.DataException;
import org.apache.log4j.Logger;
//import ca.gc.cra.db.framework.exceptions.DataFinderException;
import ca.gc.cra.db.fxmt.ifaie.GeneratedIFAIEDataBroker;
import ca.gc.cra.db.fxmt.ifaie.GeneratedIFAIEDataBrokerFacade;
import ca.gc.cra.db.fxmt.ifaie.valueobject.IFAIEPackageData;
//import ca.gc.cra.db.fxmt.ifaie.valueobject.IFAIEPackageSequenceControlData;
import ca.gc.cra.db.fxmt.ifaie.valueobject.IFAIEPackageSequenceControlKey;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;

public class InsertStatsDAOBean implements SessionBean{

	private static final long serialVersionUID = 1L;
	protected SessionContext sessionContext;
	
	private static GeneratedIFAIEDataBroker fxmtDataBroker;

	Logger log = Logger.getLogger( InsertStatsDAOBean.class);

	public void ejbCreate() throws RemoteException, CreateException {
		try {
			log.debug("ejbCreate: called");
			fxmtDataBroker = new GeneratedIFAIEDataBrokerFacade();
			log.debug("ejbCreate: fxmtDataBroker created");
		}
		catch (Exception ex)
		{
           	log.fatal("ejbCreate: Failed to create FXMT data broker", ex);
		}
	}

	@Override
	public void ejbRemove() throws RemoteException {
		fxmtDataBroker = null;
		log.debug("ejbRemove: fxmtDataBroker created");
	}

	@Override
	public void ejbActivate() throws RemoteException {
	}

	@Override
	public void ejbPassivate() throws RemoteException {
	}

	@Override
	public void setSessionContext(SessionContext sessionContext) throws RemoteException {
		this.sessionContext = sessionContext;
	}

	
	/**
	 * Get next MessageRefID for combination of taxation year, and recipient country code. 
	 * First value for each combination of taxation year, and recipient country code is 0000001.
	 * 
	 * @param transmitterCountryCode
	 * @param recipientCountryCode
	 * @param taxYear
	 * @return
	 * @throws DataException, RemoteException, IllegalFormatException, IndexOutOfBoundsException  
	 */
	public String invoke(PackageInfo p
			) throws DataException, RemoteException, IllegalFormatException, IndexOutOfBoundsException {

		log.trace("Begin");
		//int status = Constants.STATUS_CODE_INCOMPLETE;
		String messageRefId = null;

				try {
					
				} 
				catch (IllegalFormatException e) {
					throw e;
				}
				catch (IndexOutOfBoundsException e) {
					throw e;
				}
				/*catch (DataException e) {
					throw e;
				}
				catch (RemoteException e) {
					throw e;
				}*/
				
	

		log.debug("Returning"); 
		
		return messageRefId;
	}
	
	/**
	 * Create package table entry to reflect mainframe file transformed to International FATCA XML format.
	 * 
	 * @param data
	 * @return
	 * @throws DataException, RemoteException  
	 */
	public IFAIEPackageData createPackageData(
			IFAIEPackageData data) throws DataException, RemoteException {
		
		log.trace("Begin createPackageData");
		
        log.debug("Create package data: " + data);
        data = fxmtDataBroker.createIFAIEPackage(data, null);

        log.trace("End createPackageData");

		return data;
	}

	
}