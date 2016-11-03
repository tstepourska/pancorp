package ca.gc.cra.fxit.ctsagent.web.actions;

import java.util.Map;
//import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.util.ValueStack;

import org.apache.struts2.interceptor.SessionAware;

//import ca.gc.ccra.rccr.util.validation.ValidationException;
//import ca.gc.ccra.rccr.util.validation.ValidationError;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.Constants;

/**
 * 
 */
public class PackageInfoAction extends ActionSupport implements ModelDriven<PackageInfo>, Preparable, SessionAware, ValidationAware {

    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());
  //  private SampleInterface handler = null;
    
    private PackageInfo packageInfo = null;
    
    private Map<String,Object> session = null; 

    @Override
	public void prepare() throws Exception {
        log.debug("prepare()");
        Context context = new InitialContext();
       // handler = (SampleInterface)context.lookup("ca.gc.cra.tstest.business.SampleInterface");
        context.close();
    }
    
    @Override
	public PackageInfo getModel() {
        //log.debug("getModel()");
        if( packageInfo == null ) {
        	packageInfo = (PackageInfo)session.get("packageInfo");
            if( packageInfo == null ) {
            	packageInfo = new PackageInfo();
                session.put("packageInfo", packageInfo);
            }
        }
        return packageInfo;
    }
    
    public String prompt() throws Exception {
        log.debug("prompt()");
        reset();
        
        return SUCCESS;
    }

    
    public String retrieve() throws Exception {
        log.debug("retrieve()");
        
        //try {
           // packageInfo = handler.getPackageInfo(packageInfo.getId());
        	
        //for testing only, use handler
        packageInfo = new PackageInfo(); 
        packageInfo.setDataOwnerPrefix("crs");
        packageInfo.setJobDirection(Constants.JOB_OUTBOUND);
        packageInfo.setPackageType(Constants.PKG_TYPE_DATA);
        	//end of for testing only
        	
            session.put("packageInfo", packageInfo);
            return SUCCESS;
        /*} catch( ValidationException e ) {
            // transfer validation errors into struts ActionErrors
            ValidationError validationErrors[] = e.getErrors();
            for( int i = 0; i < validationErrors.length; i++ ) {
            	log.debug("id: " + validationErrors[i].getFieldId());
                addFieldError(validationErrors[i].getFieldId(), getText(validationErrors[i].getErrorId()));
            }
            return INPUT;
        } */
    }
    
    
    public String delete() throws Exception {
        log.debug("delete()");
        log.debug( "packageInfo:  " + this.packageInfo );
       // handler.deletePackageInfo(packageInfo);
        //for testing only, use handler
        session.put("packageInfo", packageInfo);
        //end of for testing only
        reset();
        return "list";
    }
    
    public String add() throws Exception {
        log.debug("add()");

      // try {
            log.debug( "packageInfo:  " + this.packageInfo);
           //packageInfo = handler.addPackageInfo(packageInfo);
            
            //for testing only, use handler
            packageInfo = new PackageInfo(); 
            packageInfo.setDataOwnerPrefix("crs");
            packageInfo.setJobDirection(Constants.JOB_OUTBOUND);
            packageInfo.setPackageType(Constants.PKG_TYPE_DATA);
        	//end of for testing only
        	
            session.put("packageInfo", packageInfo);                
            return SUCCESS;
       /* } catch( ValidationException e ) {
            // transfer validation errors into struts ActionErrors
            ValidationError validationErrors[] = e.getErrors();
            for( int i = 0; i < validationErrors.length; i++ ) {
                addFieldError(validationErrors[i].getFieldId(), getText(validationErrors[i].getErrorId()));
            }
            
            return INPUT;
        } */
    }

    public String modify() throws Exception {
        log.debug("modify()");

       // try {
            log.debug( "packageInfo:  " + this.packageInfo);
            //handler.modifyPackageInfo(packageInfo);
            return SUCCESS;
       /* } catch( ValidationException e ) {
            // transfer validation errors into struts ActionErrors
            ValidationError validationErrors[] = e.getErrors();
            for( int i = 0; i < validationErrors.length; i++ ) {
                addFieldError(validationErrors[i].getFieldId(), getText(validationErrors[i].getErrorId()));
            }
            
            return INPUT;
        } */
    }
    
    public PackageInfo getPackageInfo() {
        log.debug("getPackageInfo()");
        return packageInfo;
    }
    
    public void setPackageInfo(PackageInfo pi) {
        log.debug("setPackageInfo: " + packageInfo);
        this.packageInfo = pi;
    }
    
    @Override
	public void setSession(Map<String,Object> session) {
        this.session = session;
    }

    public String cancel() {
        if( packageInfo.getMessageRefId() == null ) {
            return "ntr";
        } 
        
        packageInfo = (PackageInfo)session.get("packageInfo");
        return "vw";
    }
    
    private void reset() {
        log.debug("reset()");

        
        packageInfo = new PackageInfo();
        
        // push the new entity onto the valuestack to override
        // any currently-outstanding instances that happen to
        // be sitting there.
        ValueStack vs = ActionContext.getContext().getValueStack();
        vs.push(packageInfo);
        
        session.put("packageInfo", packageInfo);
    }
    /*
    public List<Dept> getValidDepts() {
        log.debug("getValidDepts()");
        return Dept.asList();
    }
    
    public List<Job> getValidJobs() {
        log.debug("getValidJobs()");
        return Job.asList();
    }*/


}
