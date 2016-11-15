package ca.gc.cra.fxit.ctsagent.batch;

import ca.gc.ccra.rccr.batch.BatchConfigParser;

/*
import ca.gc.cra.fxit.ca2us.batch.TransformationParameters;
import ca.gc.cra.tstest.generated.cob2java.ftc.IP6MSGSM;*/
import ca.gc.cra.fxit.ctsagent.generated.cob2java.ftc.IP6PRTHD;
/*import ca.gc.cra.tstest.generated.cob2java.ftc.IP6PRTSM;
*/
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.task.ITask;
import ca.gc.cra.fxit.ctsagent.task.TaskManager;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.gc.cra.fxit.ctsagent.util.*;

public class BatchInitiatorBean implements SessionBean {

	private static final long serialVersionUID = 3780272573424999281L;
	private static Logger log = Logger.getLogger(BatchInitiatorBean.class);
	
	@Resource
    SessionContext sessionContext;
	 //private CICSRequestHandler crh = null;
   // private String cicsUser = null;
   // private String cicsPassword;
    //private String cicsTranid;
    

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
		
		try {		
			//////////////////////////////////////////////////
			// checking files to send 
			final File sendingRepository = new File(Constants.unprocessedInputToSendDir);
		
			//loop through the folder of files to send
			for (final File file : sendingRepository.listFiles()) {
			if (!file.isFile())
				continue;
			
			String filename = file.getName().trim();
			log.debug(fp + "filename: " + filename);
			//String inputPathName = Constants.unprocessedInputToSendDir + "/" + filename;
			log.info(fp + "found file to send: " + filename);
			
			p = initPackage( Constants.unprocessedInputToSendDir + "/", filename, Constants.JOB_OUTBOUND);
			status = taskman.invoke(p);//, jobConfigFile);
			
				// ?Insert a delay to demonstrate that long-running processes
				// ?won't overlap.
				try {
					Thread.sleep(1000);
				} catch( InterruptedException ex ) {}
			
			}
			
			log.info(fp + "No more files to send");
			
			/////////////////////////////////////////
			// chekcing files to receive
			final File receivingRepository = new File(Constants.unprocessedInputToReceiveDir);
			
			//loop through the folder of files to receive
			for (final File file : receivingRepository.listFiles()) {
			if (!file.isFile())
				continue;
			
			String filename = file.getName().trim();
			log.debug(fp + "filename: " + filename);
			//String inputPathName = Constants.unprocessedInputToReceiveDir + "/" + filename;
			log.debug(fp + "found file to receive: " + filename);
			
			p = initPackage(Constants.unprocessedInputToReceiveDir + "/", filename, Constants.JOB_INBOUND);
			status = taskman.invoke(p);//, jobConfigFile);
			
				// ?Insert a delay to demonstrate that long-running processes
				// ?won't overlap.
				try {
					Thread.sleep(1000);
				} catch( InterruptedException ex ) {}
			
			}
			
			log.info(fp + "No more files to send");
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
			p.setDataProviderPrefix("crs");
			
			// figure out suffix:
			if(jobDirection.equalsIgnoreCase(Constants.JOB_OUTBOUND)){
				p.setJobSuffix(Constants.SUFFIX_PAYLOAD);
			}
			else {
				//inbound
				p.setJobSuffix(Constants.SUFFIX_PRELIM);
			}
			/*if(sendingCountry.equalsIgnoreCase(Constants.CANADA)){
				//if outbound, set job suffix (from the batch outbound will be payload
				p.setJobSuffix(Constants.SUFFIX_PAYLOAD);			
			}*/
			
			

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
					IP6PRTHD headerRec = new IP6PRTHD();
					headerRec.setRec(line);
					//log.debug(fp + "headerRec set");
					String taxYear = headerRec.getRtnTxYr();;
					log.debug(fp + "tax year from header: " + taxYear);				
					//initialize tax year
					p.setTaxYear(Integer.parseInt(taxYear));
					//set header line
					p.setHeaderLine(line);
					break;
				/*case Constants.FI_PKG_REF_REC_TRANS_CD: //1002:
					log.debug(fp + "case 1002");
					//log.debug(fp + "line: " + line);
					IP6MSGSM fiRec = new IP6MSGSM();
					fiRec.setRec(line);
					String ifaePkgRefId = fiRec.getIfaePkgRefId();
					String infodecPsn = fiRec.getInfodecPsn();
					String prt18SummInfo = fiRec.getPrt18SummInfo();
					int tcd = fiRec.getTransCd();
					log.debug(fp + "ifaePkgRefId: " + ifaePkgRefId + ",infodecPsn: "+infodecPsn + ", prt18SummInfo: " + prt18SummInfo + ", transCD: " + tcd);
					//TODO figure out which part of which line will carry this value for CRS
					//if(sendingCountry==null){
						//sendingCountry = infodecPsn.substring(0,2);
						//log.debug(fp + "sending country: " + sendingCountry);
						//receivingCountry = 
						//break outer;
					//}
					break;
					*/
					//SPONSOR_REC_TRANS_CD
				/*case Constants.SPONSOR_PKG_REF_REC_TRANS_CD:// 1003:
					//log.debug(fp + "case 1003");
					break;					
				case Constants.SLIP_PKG_REF_REC_TRANS_CD: //1004:
					//log.debug(fp + "case 1004");
					break;
				case Constants.PERSON_PKG_REF_REC_TRANS_CD: //1005:
					//log.debug(fp + "case 1005");
					break;
				case 1006:
					//log.debug(fp + "case 1006");
					break;
				case Constants.TLR_PKG_REF_REC_TRANS_CD: //1007:
					//log.debug(fp + "case 1007");
					break;*/
				default:
					log.debug(fp + "case default: " + transCD);
				}
				}
				catch(Exception e){
					//no transCd therefore status message:
					if(line.toLowerCase().indexOf("status")>-1){
						p.setPackageType(Constants.PKG_TYPE_STATUS);
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
		}
		
		return p;
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
		BatchInitiatorBean b = new BatchInitiatorBean();
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
