package ca.gc.cra.fxit.ctsagent.batch;

import ca.gc.ccra.rccr.cics.integration.CICSRequest;
import ca.gc.ccra.rccr.cics.integration.CICSRequestHandler;
import ca.gc.ccra.rccr.cics.integration.CICSRequestHandlerHome;
import ca.gc.ccra.rccr.security.crypto.business.CryptoService;
import ca.gc.ccra.rccr.security.crypto.business.CryptoServiceHome;
import ca.gc.ccra.rccr.util.resource.MessageRetriever;
import ca.gc.ccra.rccr.util.resource.ResourceBundleRetriever;
import ca.gc.ccra.rccr.util.validation.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.util.Utils;

/**
 */
@Stateless 
public class SampleInterfaceBean implements SampleInterface {
    
	private static Logger log = Logger.getLogger(SampleInterfaceBean.class);
	
    @Resource
    SessionContext sessionContext;
 
    static {

    }
    

    //private CICSRequestHandler crh = null;
    
    
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

}
