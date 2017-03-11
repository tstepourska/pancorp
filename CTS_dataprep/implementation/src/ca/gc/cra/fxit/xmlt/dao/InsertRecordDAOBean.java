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

public class InsertRecordDAOBean implements SessionBean{

	private static final long serialVersionUID = 1L;
	protected SessionContext sessionContext;
	
	private static GeneratedIFAIEDataBroker fxmtDataBroker;

	Logger log = Logger.getLogger( InsertRecordDAOBean.class);

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
			//String transmitterCountryCode,
			//String recipientCountryCode,
			//String taxYear
			) throws DataException, RemoteException, IllegalFormatException, IndexOutOfBoundsException {
		String fp = "getNextMessageRefID: "; 
		log.trace("Begin" + fp);
		//int status = Constants.STATUS_CODE_INCOMPLETE;
		String messageRefId = null;
	//	int nextID = 1;
		//messageRefId = "CA2016FR123456789";
				try {
					//for testing only
					Random r = new Random(System.currentTimeMillis());
					long pkgid = Math.abs(r.nextLong());
					String yr = (""+p.getReportingPeriod().getYear()).substring(2);
					messageRefId = p.getSendingCountry()+yr+p.getReceivingCountry() + pkgid;
					//status = Constants.STATUS_CODE_SUCCESS;
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
				
		//	}
		//}

		log.debug(fp + "Returning"); // next MessageRefID : " + messageRefID);
		
		//log.trace(fp + "End getNextMessageRefID");
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