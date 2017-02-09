package ca.gc.cra.fxit.xmlt.model.wrapper.ftc;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.cob2java.crs.IP6PRTHD;

public class MessageHeaderWrapper extends IP6PRTHD {
	private static Logger lg = Logger.getLogger(MessageHeaderWrapper.class);
	
	public MessageHeaderWrapper(String line){
		super();
		if(line.length() != this.length()){
			lg.info("line: " + line);
			lg.error("Header record line length is not correct: " + line.length() + "!=" + this.length());
			//TODO throw exception?
		}
		this.setRec(line);
		if(lg.isDebugEnabled())
			lg.debug("header rec created");
	}
	
}
