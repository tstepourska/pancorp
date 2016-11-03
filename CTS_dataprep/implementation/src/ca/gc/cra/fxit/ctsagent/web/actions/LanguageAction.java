package ca.gc.cra.fxit.ctsagent.web.actions;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


/**
 * Toggles language between english and french.
 */
public class LanguageAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());
    
    @Override
	public String execute() throws Exception {
        log.debug("execute()");

        // Struts automatically knows what to do with the request_locale parameter.
        // We don't need to explicitly call actionContext.setLocale(); it has
        // already been set to the requested value.
        log.debug("struts locale has been set to " + getLocale());
        
        ActionContext actionContext = ActionContext.getContext();
        
        Map<String, Object> session = actionContext.getSession();
        
        // We have specified two results in struts.xml, both of
        // which point to the value of session.crrntPg.  If
        // crrntPg starts with ".", we assume we need to forward
        // to a tilesdef; otherwise we execute a regular action. 
        String crrntPg = (String)session.get("crrntPg");
        log.debug("crrntPage:  " + crrntPg);

        // If there's no current page value in the session, route to the
        // default.  This could be the application entry point, 
        // a session timeout page, or....
        if( crrntPg == null ) {
            return "default";
        }
        
        if( crrntPg.startsWith(".") ) {
            return "tiles";
        } 
        
        return "chain";       
    }
}
