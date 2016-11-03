package ca.gc.cra.fxit.ctsagent.web.actions;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action that returns a text response indicating whether the session
 * is still valid and has been successfully extended. This action is meant to keep
 * the http session alive in response to a request from the 
 * WET3 session timeout warning dialog.
 */
public class RefreshSessionAction extends ActionSupport implements ServletRequestAware {

    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(getClass());

	private InputStream responseStream;
	private HttpServletRequest request; 
	
	public InputStream getInputStream() {
	    return responseStream;
	} 

	@Override
	public String execute(){
	    
        HttpServletResponse response = ServletActionContext.getResponse();

        response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
	    
        if ( request.isRequestedSessionIdValid() ) {
            log.debug("session successfully extended");
            responseStream = new ByteArrayInputStream("true".getBytes());
        } else {
            log.debug("session already expired; returning false");
            responseStream = new ByteArrayInputStream("false".getBytes());
        }
        return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public InputStream getResponseStream() {
		return responseStream;
	}

}
