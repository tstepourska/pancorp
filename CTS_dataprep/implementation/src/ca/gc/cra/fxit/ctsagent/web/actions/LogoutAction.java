/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.web.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import ca.gc.cra.fxit.ctsagent.util.Constants;

/**
 * 
 * 
 * @author Txs285
 * @since  2016-11-03
 */
public class LogoutAction extends ActionSupport  implements ServletRequestAware { 
	
	private static final long serialVersionUID = -8377079203236431838L;

	private static Logger log = Logger.getLogger(LogoutAction.class);

	private HttpServletRequest request; 

	/**
	 * Invalidates session
	 * 
	 * @return String
	 */
	@Override
	public String execute() {		
        log.debug("Invalidating session ");       
        String user = ServletActionContext.getRequest().getRemoteUser();
		request.getSession().invalidate();
		
		log.info(Constants.AUDIT + "User " + user + " logged out");  
		
		return SUCCESS;
	}

	/**
	 * Sets the request object
	 * 
	 * @param request
	 */
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}