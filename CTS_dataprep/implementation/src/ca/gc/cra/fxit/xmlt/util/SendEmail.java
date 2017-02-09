package ca.gc.cra.fxit.xmlt.util;

import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class SendEmail {
	private static Logger log = Logger.getLogger(SendEmail.class);
	
	public void sendConfirmationEmail(List<String> targetDataSets, List<String> targetArchives) {

		if ((targetDataSets == null || targetDataSets.size() == 0) && 
			(targetArchives == null || targetArchives.size() == 0)) {
			log.debug("Suppressing confirmation email because no files were transferred to the mainframe.");
		}

		String errMsg = "Unable to send FTP confirmation email to IRMS";
		try {
			//JNDINames batchProperties = new JNDINames();
	    	//batchProperties.loadProperties();
			
			if (Globals.sendMailFlag) {
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
				String emailSubject = "FXIT transfered PRT18 " + Globals.docTypeIndicEnv + " Files to the mainframe";
				msg.setSubject(emailSubject);
				
				String newLine = System.lineSeparator();
				StringBuilder emailBody = new StringBuilder();
				emailBody.append("FXIT " + Globals.docTypeIndicEnv + " file transfer completed.");
				emailBody.append(newLine);
				emailBody.append("FXIT Database Environment: " + Globals.databaseEnvironment);
				emailBody.append(newLine);
				emailBody.append(newLine);
				emailBody.append("The following " + targetDataSets.size() + " data sets were successfully transfered to the mainframe for IRMS to load.");
				emailBody.append(newLine);
				for (String sFileName : targetDataSets) {
					emailBody.append(sFileName);
					emailBody.append(newLine);
				}
				emailBody.append(newLine);
				emailBody.append("The following " + targetArchives.size() + " archive files were successfully transfered to the mainframe for FATCA XML file retention purposes.");
				emailBody.append(newLine);
				for (String sFileName : targetArchives) {
					emailBody.append(sFileName);
					emailBody.append(newLine);
				}
				emailBody.append(newLine);
				emailBody.append("\nThis email is sent automatically by the eBCI FXIT batch application. eApplid=FXIT, componentID=ca2us"); 
				
				String content = emailBody.toString();
				msg.setContent(content,"text/plain; charset=UTF-8");   

				log.info("Sending email to IRMS");
				
				log.debug("Email Subject    : " + msg.getSubject());
				log.debug("Email Body       : " + msg.getContent().toString());
				
				Transport.send(msg);
			}
			else {
				log.info("Suppressing confirmation email because FXIT sendFlag environment property is set to False.");
			}
	    	
		}
		catch (Exception e) {
			log.error(errMsg, e);
		}

	}

}
