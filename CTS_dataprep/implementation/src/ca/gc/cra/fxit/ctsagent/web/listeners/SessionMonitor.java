package ca.gc.cra.fxit.ctsagent.web.listeners;

import javax.servlet.http.*;

import org.apache.commons.logging.*;

public class SessionMonitor implements HttpSessionListener {

	Log log = LogFactory.getLog(getClass());
	
	@Override
    public void sessionCreated(HttpSessionEvent se) { 
    	log.debug("sessionCreated()");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) { 
        log.debug("sessionDestroyed()");
        if( log.isDebugEnabled() ) {
            StringBuffer sb = new StringBuffer();
            sb.append("============= session attributes ==============\n");
            java.util.Enumeration<String> en = se.getSession().getAttributeNames();
            while( en.hasMoreElements() ) {
                String key = en.nextElement();
                Object value = se.getSession().getAttribute(key);
                sb.append(key + " = " + value + "\n");
            }
            sb.append("=========== end session attributes ============\n");
            log.debug(sb.toString());
        }
    }
}

