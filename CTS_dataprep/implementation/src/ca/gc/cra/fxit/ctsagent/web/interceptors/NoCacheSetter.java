package ca.gc.cra.fxit.ctsagent.web.interceptors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class NoCacheSetter implements Interceptor {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void destroy() {
		log.debug("destroy()");
	}

	@Override
	public void init() {
		log.debug("init()");
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		log.debug("intercept()");
		
		HttpServletResponse response = ServletActionContext.getResponse();

        response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        return actionInvocation.invoke();
	}

}