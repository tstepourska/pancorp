package ca.gc.cra.fxit.ctsagent.web.actions;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.task.TaskManager;
import ca.gc.cra.fxit.ctsagent.util.Constants;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author Txs285
 * @since 2016-11-04
 */
public class StartPackagingJobAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());
    
    @Override
	public String execute() throws Exception {
        log.debug("execute()");

        ActionContext actionContext = ActionContext.getContext();
        
        Map<String, Object> session = actionContext.getSession();
        
        // We have specified two results in struts.xml, both of
        // which point to the value of session.crrntPg.  If
        // crrntPg starts with ".", we assume we need to forward
        // to a tilesdef; otherwise we execute a regular action. 
        String crrntPg = (String)session.get("crrntPg");
        log.debug("crrntPage:  " + crrntPg);
        
        PackageInfo p = new PackageInfo();
		p.setPackageType(Constants.PKG_TYPE_DATA);
		p.setJobDirection(Constants.JOB_OUTBOUND);
		p.setJobSuffix(Constants.SUFFIX_PACKAGE);
		
		//create job controller  //TODO
		//String jobConfigFile = ""; //batchProperties.getProperty(Constants.KEY_JOB_CONFIG);
		TaskManager taskman = new TaskManager();
		int status = taskman.invoke(p);//, jobConfigFile);
		log.info("Executed with status: " + status);

        

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
