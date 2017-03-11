package ca.gc.cra.fxit.xmlt.util;

import java.util.Date;
//import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class SendEmail {
	private static Logger log = Logger.getLogger(SendEmail.class);
	
	public void sendEmail(//List<String> _targetDataSets, List<String> targetArchives
			String subject,
			String content
			) {
		if (!Globals.sendMailFlag){
			log.info("Suppressing email because sendFlag environment property is set to False.");
			return;
		}
		
		try {
				//String fromAddress = batchProperties.getMailFromAddress();
				//String toAddressList = batchProperties.getMailToAddressList();
				//String docTypeIndicEnv = Globals.docTypeIndicEnv;
				//String fxmtDatabaseEnv = batchProperties.getDatabaseEnvironment();
				
				InitialContext ic = new InitialContext();
				Session session = (Session) ic.lookup("ca.gc.cra.fxit.mail.Session");
				
				// create a message
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(Globals.mailFromAddress));
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Globals.mailToAddressList));
				
				msg.setSentDate(new Date());
				//String emailSubject = "FXIT transfered PRT18 " + Globals.docTypeIndicEnv + " Files to the mainframe";
				msg.setSubject(subject);
				
				//String newLine = System.lineSeparator();
			
				msg.setContent(content,"text/plain; charset=UTF-8");   

				log.info("Sending email");
				
				log.debug("Email Subject    : " + msg.getSubject());
				log.debug("Email Body       : " + msg.getContent().toString());
				
				Transport.send(msg);	    	
		}
		catch (Exception e) {
			String errMsg = "Unable to send email";
			log.error(errMsg);
			Utils.logError(log, e);
		}

	}

}
