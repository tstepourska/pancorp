package ca.gc.cra.fxit.xmlt.batch;

import ca.gc.ccra.rccr.batch.BatchConfigParser;

/*
import ca.gc.cra.fxit.ca2us.batch.TransformationParameters;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6MSGSM;*/
/*import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTSM;
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import ca.gc.cra.fxit.ca2us.batch.JNDINames;
//import ca.gc.cra.fxit.ca2us.data.RetrieveFxmtDataHome;
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
 * Each 
 * CountryCodeSender_CRS_Payload.xml
 * CountryCodeSender_CRS_Metadata.xml
 * @author Txs285
 *
 */

public class BatchInitiatorBeanOutbound implements SessionBean {

	private static final long serialVersionUID = 3780272573424999281L;
	private static Logger log = Logger.getLogger(BatchInitiatorBeanOutbound.class);
	
	@Resource
    SessionContext sessionContext;

	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	/**
	 * Triggers the batch process.
	 *  
	 */
	public void execute(String args) throws RemoteException, Exception {
		
		String fp = "execute: ";
		log.info(fp + "args:  " + args);	
		// 1. load configuration
		Globals.loadBatchProperties(args);
                
		//create task manager
		TaskManager taskman = new TaskManager();		
		PackageInfo p = null;
		String sendingRepPath = Globals.baseFileDir + Globals.OUTBOUND_UNPROCESSED_TOSEND_DIR;
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
				String targetFilepath = Globals.FILE_WORKING_DIR+ filename;
				
				//move file from local unprocessed dir to the local temporary working directory
				boolean moved = Utils.moveFile(srcFilepath, targetFilepath);
				if(!moved){
					log.error("can't move the file " + filename + " into temporary location");
					continue;
				}
			
				p = PackageInfoFactory.createPackageInfo(//sendingRepPath, 
														Globals.FILE_WORKING_DIR,
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
			throw e;
		}
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	@PostConstruct
    public void create() {
	log.debug("create");

    Context context = null;
    try {
        context = new InitialContext();
        
      
        
    } catch( Exception ex ) {
        String msg = "problem initializing ejb";
        Utils.logError(log, ex);
        throw new EJBException(msg);
    } finally {
        try { 
            context.close();  
        } catch( Exception ex ) {
            log.warn("problem closing context", ex);
        }
    }
	}
	
	 @PreDestroy
	public void remove() {
	        try {
	            //crh.remove();
	        } catch( Exception ex ) {
	            log.warn("problem removing crh");
	        }
	}
    
	public void ejbCreate() {
		log.debug("ejbCreate(), instance");	
		// Get a reference to the RetrieveFxmtDataBean bean.
		try
		{
			log.debug("ejbCreate: called");
		  InitialContext context = new InitialContext();
		 // Object objRef = context.lookup(JNDINames.JNDI_FXMT_DATA_HOME);
		  //RetrieveFxmtDataHome home = (RetrieveFxmtDataHome) PortableRemoteObject.narrow(objRef, Class.forName(RetrieveFxmtDataHome.jndiName));
		  //fxmtDataBean = home.create();
		  log.debug("ejbCreate: done: context, objRef, home and fxmtDataBean created");
		}
		catch(Exception ex)
		{
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

	/**
	 * For testing only TODO to move to JUnit
	 * @param args
	 */
	public static void main(String[] args){
		BatchInitiatorBeanOutbound b = new BatchInitiatorBeanOutbound();
		String filename = "fxit.xmlt.batch.xml";
		
		String path = "C:/git/repository/CTS_dataprep/implementation/cfg/";
		
		String xml = Utils.xmlToString(path, filename);
		log.info(xml);
		try {
			b.execute(filename);
			//Globals.loadBatchProperties(path + filename);
			//b.loadBatchProperties(xml);
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
	}
	
	
}
