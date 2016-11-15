package ca.gc.cra.fxit.ctsagent.web.actions;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Txs285
 * @since 2016-11-03
 */

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 8105750925871272482L;
	String j_username, j_password;

    public String getJ_password() {
        return j_password;
    }

    public void setJ_password(String j_password) {
        this.j_password = j_password;
    }

    public String getJ_username() {
        return j_username;
    }

    public void setJ_username(String j_username) {
        this.j_username = j_username;
    }

    @Override
    public String execute() throws Exception {
    	//TODO add credentials validation here!
        if (getJ_username().equals(getJ_password())) {
            return SUCCESS;
        } 
        
        this.addActionError("Error..!");
        return ERROR;
    }
    
    @Override
    public void validate() {
    if ((getJ_username() == null) || (getJ_username().length() == 0)) {
        this.addActionError("Username Empty");
    }
    if ((getJ_password() == null) || (getJ_password().length() == 0)) {
        this.addActionError("Password Empty");
    }
    }
}
