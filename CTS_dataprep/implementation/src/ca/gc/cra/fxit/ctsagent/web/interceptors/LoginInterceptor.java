/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.web.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import ca.gc.cra.fxit.ctsagent.util.Constants;

/**
 * 
 * 
 * @author sxd156
 * @since 2016-11-03
 */
public class LoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -2401140254163562684L;
	private static Logger log = Logger.getLogger(LoginInterceptor.class);

	/**
	 * 
	 * 
	 * @parameter actionInvocation
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();

		if (request.getRemoteUser() != null	&& request.isUserInRole("casd")) {
			log.info(Constants.AUDIT + "Successfully login user: " + request.getRemoteUser());
			return actionInvocation.invoke();
		} 
			
		//return Action.SUCCESS;	
		return Action.ERROR;
	}
}
