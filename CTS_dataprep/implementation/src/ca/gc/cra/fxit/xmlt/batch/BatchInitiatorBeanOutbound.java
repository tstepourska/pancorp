package ca.gc.cra.fxit.xmlt.batch;

import java.io.File;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
//import javax.naming.Context;
import javax.naming.InitialContext;

//import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.model.PackageInfoFactory;
import ca.gc.cra.fxit.xmlt.job.TaskManager;
import ca.gc.cra.fxit.xmlt.util.*;

/**
 * This class initiates process of collecting data files from 
 * data provider(s) (i.e. Infodec, CBC etc).
 * Process can be initiated ad_hoc or running at regular time intervals 
 * as a cron job
 * 
 * @author Txs285
 *
 */

public class BatchInitiatorBeanOutbound implements SessionBean { //, BatchInitiatorHome {

	private static final long serialVersionUID = 3780272573424999281L;
	private static Logger log = Logger.getLogger(BatchInitiatorBeanOutbound.class);
	
	@Resource
    SessionContext sessionContext;

	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	/**
	 * Triggers the batch process.
	 *  
	 */
	public void execute(String __args) throws RemoteException {
		
		String fp = "execute: ";
		//log.info(fp + "args:  " + args);
    
		//create task manager
		TaskManager taskman = new TaskManager();		
		PackageInfo p = null;
		String sendingRepPath = Globals.baseFileDir + Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR;
		if(log.isDebugEnabled())
			log.debug("sendingRepPath: " + sendingRepPath);
		final File sendingRepository 	= new File(sendingRepPath);
		
		try {		
			//////////////////////////////////////////////////
			// checking files to send 
			
			//loop through the folder of files to send
			for (final File file : sendingRepository.listFiles()) {
				if (!file.isFile())
					continue;

				String filename = file.getName().trim();
				log.info(fp + "found file to send: " + filename);
				
				String srcFilepath = sendingRepPath + filename; 
				if(log.isDebugEnabled())
					log.debug("srcFilepath: " + srcFilepath);
				String targetFilepath = Globals.FILE_WORKING_DIR+ filename;
				if(log.isDebugEnabled())
					log.debug("targetFilepath: " + targetFilepath);
				
				//move file from local unprocessed dir to the local temporary working directory
				boolean moved = Utils.moveFile(srcFilepath, targetFilepath);
				if(!moved){
					log.error("can't move the file " + filename + " into temporary location");
					continue;
				}
			
				p = PackageInfoFactory.createPackageInfo(Globals.FILE_WORKING_DIR,
														filename, 
														Constants.JOB_OUTBOUND);
				status = taskman.invoke(p);

				try {
					Thread.sleep(1000);
				} catch( InterruptedException ex ) {}			
			}
			
			log.info(fp + "Done file list");
		}
		catch(RemoteException re){
			Utils.logError(log, re);
			throw re;
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(log, e);
			throw new RemoteException();
		}
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	@PostConstruct
	public void ejbCreate() {
		log.debug("ejbCreate()");	
		// Get a reference to the RetrieveFxmtDataBean bean.
		try	{
			InitialContext context = new InitialContext();
			if(log.isDebugEnabled())
				log.debug("ejbCreate: created InitialContext: " + context);
			// 1. load configuration
			Globals.loadDomainProperties();
			Globals.loadBatchProperties();   
			log.info("ejbCreate: properties loaded: ");
			log.info(Globals.toStaticString());
			
			// Object objRef = context.lookup(JNDINames.JNDI_FXMT_DATA_HOME);
			//RetrieveFxmtDataHome home = (RetrieveFxmtDataHome) PortableRemoteObject.narrow(objRef, Class.forName(RetrieveFxmtDataHome.jndiName));
			//fxmtDataBean = home.create();
			log.debug("ejbCreate: done: context, objRef, home and fxmtDataBean created");
		}
		catch(Exception ex)	{
           	log.fatal("Failed to create FXMT data bean", ex);
		}
	}

	@Override
	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		log.debug("setSessionContext()");		
	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		log.debug("ejbRemove()");	
		log.info("     ");
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// will never get called, since this is a stateless session bean
		log.debug("ejbActivate()");				
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// will never get called, since this is a stateless session bean
		log.debug("ejbPassivate()");		
	}
}