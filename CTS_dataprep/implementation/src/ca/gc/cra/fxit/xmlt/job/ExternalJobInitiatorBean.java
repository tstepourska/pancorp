package ca.gc.cra.fxit.xmlt.job;

import ca.gc.ccra.rccr.batch.BatchConfigParser;

/*
import ca.gc.cra.fxit.ca2us.batch.TransformationParameters;
import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6MSGSM;*/
/*import ca.gc.cra.fxit.xmlt.generated.cob2java.ftc.IP6PRTSM;
*/

import java.math.BigInteger;
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

import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
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

public class ExternalJobInitiatorBean implements SessionBean, IExternalJobInitiatorRemote {

	private static final long serialVersionUID = 3780272573424999281L;
	private static Logger lg = Logger.getLogger(ExternalJobInitiatorBean.class);
	
	@Resource
    SessionContext sessionContext;

	int status = Constants.STATUS_CODE_INCOMPLETE;
	
	@Override
	public int initStatusMessage(String dataProvider, 
									String messageRefId, 
									String fileAcceptanceStatus, 
									List<FileErrorType> fileErrors, 
									List<RecordErrorType> recordErrors, 
									String origCTSTransmissionId,
									String countryToSend,
									XMLGregorianCalendar origCTStimestamp,
									String origSenderFileId,
									BigInteger origFileSize,
									XMLGregorianCalendar repPeriod
									) {
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			InitStatusMessageJob m = new InitStatusMessageJob(
				dataProvider, 
				messageRefId, 
				fileAcceptanceStatus, 
				fileErrors, 
				recordErrors, 
				origCTSTransmissionId,
				countryToSend,
				origCTStimestamp,
				origSenderFileId,
				origFileSize,
				repPeriod);
		
			status = m.invoke();
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		
		return status;
	}
	
	@Override
	public int validateMetadata(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		
		return status;
	}
	
	@Override
	public int validateXML(){
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		
		return status;
	}


	
	@PostConstruct
    public void create() {
	lg.debug("create");

    Context context = null;
    try {
        context = new InitialContext();
        
      
        
    } catch( Exception ex ) {
        String msg = "problem initializing ejb";
        Utils.logError(lg, ex);
        throw new EJBException(msg);
    } finally {
        try { 
            context.close();  
        } catch( Exception ex ) {
            lg.warn("problem closing context", ex);
        }
    }
	}
	
	 @PreDestroy
	public void remove() {
	        try {
	            //crh.remove();
	        } catch( Exception ex ) {
	            lg.warn("problem removing crh");
	        }
	}
    
	public void ejbCreate() {
		lg.debug("ejbCreate(), instance");	
	}

	@Override
	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		lg.debug("setSessionContext()");		
	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		lg.debug("ejbRemove()");		
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// will never get called, since this is a stateless session bean
		lg.debug("ejbActivate()");				
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// will never get called, since this is a stateless session bean
		lg.debug("ejbPassivate()");		
	}

	public static void main(String[] args){
		ExternalJobInitiatorBean b = new ExternalJobInitiatorBean();
		String filename = "fxit.ctsagent.batch.xml";
		
		String path = "C:/git/repository/CTS_dataprep/implementation/cfg/";
		
		String xml = Utils.xmlToString(path, filename);
		lg.info(xml);
		try {
			//b.execute(filename);
			//Globals.loadBatchProperties(path + filename);
			//b.loadBatchProperties(xml);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}
	
	
}
