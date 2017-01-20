package ca.gc.cra.fxit.xmlt.batch;

import ca.gc.ccra.rccr.batch.BatchConfigParser;

/*
import ca.gc.cra.fxit.ca2us.batch.TransformationParameters;
import ca.gc.cra.fxit.xmlt.transformation.cob2java.ftc.IP6MSGSM;*/
/*import ca.gc.cra.fxit.xmlt.transformation.cob2java.ftc.IP6PRTSM;
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
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.ITask;
import ca.gc.cra.fxit.xmlt.job.TaskManager;
import ca.gc.cra.fxit.xmlt.transformation.cob2java.ftc.IP6PRTHD;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.transformation.wrapper.crs.MessageHeaderWrapper;
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
		AppProperties.loadBatchProperties(args);
                
		//create task manager
		TaskManager taskman = new TaskManager();		
		PackageInfo p = null;
		final File sendingRepository 	= new File(Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR);
		
		try {		
			//////////////////////////////////////////////////
			// checking files to send 
			
			//loop through the folder of files to send
			for (final File file : sendingRepository.listFiles()) {
				if (!file.isFile())
					continue;
			
				String filename = file.getName().trim();
				log.info(fp + "found file to send: " + filename);
			
				p = initPackage(Constants.OUTBOUND_UNPROCESSED_TOSEND_DIR, filename, Constants.JOB_OUTBOUND);
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

	/**
	 * Initializes new package info object and sets variables 
	 * necessary to find a job appropriate for processing this package: 
	 * job direction, package type (data or status message), sending and receiving country, data owner prefix - crs, cbc, etr, ftc
	 * 
	 */
	private PackageInfo initPackage(String unprocessedPath, String filename, String jobDirection) throws Exception{	
		String fp = "initPackage: ";
		PackageInfo p = new PackageInfo();			
		String dataProvider = null;
		
		//TODO assuming here that the header contains info required to determine:
		// sender country
		// data or status message content
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(unprocessedPath + filename), Constants.DEFAULT_ENCODING));){
			String line;
			int transCD = -1;
			String[] arr = filename.split("\\.");
			int len = arr.length;
			log.debug(fp + "len: " + len);
			String cc = arr[2];
			
			dataProvider = getDataProvider(filename);
			if( dataProvider==null)
				throw new Exception("Unsupported data provider!");
			p.setDataProvider( dataProvider);
	
			String sendingCountry = cc.substring(0,2);
			String receivingCountry = cc.substring(2);
			log.debug(fp + "sending from " + sendingCountry + " to " + receivingCountry);
			p.setSendingCountry(sendingCountry);
			p.setReceivingCountry(receivingCountry);
			
			p.setOrigFilename(filename);
			p.setInputPathName(unprocessedPath);
			p.setJobDirection(jobDirection);
			// !!! TODO - determine type of package
			//p.setPackageType(Constants.PKG_TYPE_STATUS);
		//}
		//else{
			p.setPackageType(Constants.PKG_TYPE_DATA); // or Status message?
			
			//!!! TODO: determine dataProviderPrefix: cbc, crs, etr, ftc
			//p.setDataProvider("crs");
			//set based on data provider and message/package type
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CRS);
			/*p.setCtsCommunicationType(CTSCommunicationTypeCdType.CRS_STATUS);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CBC);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.CBC_STATUS);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.ETR);
			p.setCtsCommunicationType(CTSCommunicationTypeCdType.ETR_STATUS);
			*/

			// figure out suffix:
			if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND)){
				p.setJobSuffix(Constants.SUFFIX_PAYLOAD);
			}			

	//outer:
			while( (line=reader.readLine()) != null) {		
				//reset
				transCD = -1;
				//log.debug(fp + "line: " + line);
				
				try {
					transCD = Integer.parseInt(line.substring(0,4));
				//log.debug(fp + "transCD: " + transCD);
			
				
				switch(transCD){
				case Constants.HDR_PKG_REF_REC_TRANS_CD: //1001:
					log.debug(fp + "case 1001: header");
					//log.debug(fp + "line: " + line);
					MessageHeaderWrapper header = new MessageHeaderWrapper(line);

					//initialize reporting period field, month and day are null
					XMLGregorianCalendar cal = Utils.generateReportingPeriod(header.getRtnTxYr(),null,null);
					log.debug(fp + "tax year calendar from header: " + cal);				
					//initialize tax year
					p.setReportingPeriod(cal);
					
					break;
				default:
					log.debug(fp + "case default: " + transCD);
				}
				}
				catch(Exception e){
					//no transCd therefore status message:
					if(line.toLowerCase().indexOf("status")>-1){
						p.setPackageType(Constants.PKG_TYPE_STATUS_MESSAGE);
					}
					else{
						p.setPackageType(Constants.PKG_TYPE_DATA);
					}
					//Utils.logError(log, e);
				}
					
				break;
			}
		}
		catch(Exception e){
			Utils.logError(log, e);
			throw e;
		}
		
		return p;
	}
	
	private String getDataProvider(String filename) throws Exception {
		String dp = null;
		
		for(int i=0;i<AppProperties.DATA_PROVIDERS.length;i++){
			if(filename.indexOf(AppProperties.DATA_PROVIDERS[i])>-1){
				dp = AppProperties.DATA_PROVIDERS[i];
				break;
			}
		}
			
		return dp;
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

	public static void main(String[] args){
		BatchInitiatorBeanOutbound b = new BatchInitiatorBeanOutbound();
		String filename = "fxit.ctsagent.batch.xml";
		
		String path = "C:/git/repository/CTS_dataprep/implementation/cfg/";
		
		String xml = Utils.xmlToString(path, filename);
		log.info(xml);
		try {
			b.execute(filename);
			//AppProperties.loadBatchProperties(path + filename);
			//b.loadBatchProperties(xml);
		}
		catch(Exception e){
			Utils.logError(log, e);
		}
	}
	
	
}
