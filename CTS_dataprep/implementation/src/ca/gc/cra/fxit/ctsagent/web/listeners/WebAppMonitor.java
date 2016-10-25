package ca.gc.cra.fxit.ctsagent.web.listeners;

import ca.gc.ccra.rccr.util.PackageVersioning;
import ca.gc.ccra.rccr.util.logging.LogFilter;

import javax.servlet.*;
import javax.naming.*; 

import org.apache.commons.logging.*;

public class WebAppMonitor implements ServletContextListener {

	Log log = LogFactory.getLog(getClass());
	
   /** 
    * Global web app initialization.
    * 
    * Dynamically configure the log levels based on
    * jndi properties.  This allows us to run different
    * log levels in different states with the same ear 
    * file.
    * 
    * Also transfers frequently-used jndi values to application 
    * scope for convenient access. 
    */
    public void contextInitialized( ServletContextEvent ce ) {
    	log.info("contextInitialized()");

        Context context = null;
        try {
            context = new InitialContext();
            
            String logLevel = (String)context.lookup("ca.gc.cra.xxxx.struts2base.env.log_level");
            
            if( logLevel == null ) {
            	log.warn("log_level property not found.  defaulting to INFO.");
                logLevel = "INFO";
            } else {
                LogFilter.setLogLevel("ca.gc.cra.xxxx.struts2base", logLevel);
                log.info("logLevel set to " + logLevel);
            }
            
            // store the sessionErrorPage value at application scope so that
            // it can be conveniently accessed by the sessionTimeout.jsp. 
            String sessionErrorPage = (String)context.lookup("ca.gc.cra.xxxx.struts2base.env.web.sessionErrorPage");
	    	log.info("sessionErrorPage:  " + sessionErrorPage);
	    	ce.getServletContext().setAttribute("sessionErrorPage", sessionErrorPage);
	    	
        } catch( Exception ex ) {
        	log.error("problem retrieving properties during app initialization", ex);
        } finally {
        	if( context != null ) {
        		try {
	        		context.close();
        		} catch( Exception ex ) {
        			log.error("problem closing context", ex);
        		}
        	}
        }
        
    	PackageVersioning.dumpManifest(getClass(), log);        
    }


    /**
     * global web app cleanup. 
     */
    public void contextDestroyed(ServletContextEvent ce) {
    	log.info("contextDestroyed()");
    }
}

