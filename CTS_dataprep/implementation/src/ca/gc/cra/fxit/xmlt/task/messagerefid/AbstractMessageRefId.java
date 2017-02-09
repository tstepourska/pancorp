package ca.gc.cra.fxit.xmlt.task.messagerefid;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.exception.InvalidMessageRefIdException;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CountryCodeType;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.AbstractTask;
import ca.gc.cra.fxit.xmlt.util.Constants;

public abstract class AbstractMessageRefId extends AbstractTask {
	private static Logger lg = Logger.getLogger(AbstractMessageRefId.class);
	
	public void validateMessageRefId(PackageInfo p) throws InvalidMessageRefIdException, Exception {
		String fp = "validateMessageRefID:";

		//Metadata senderFileId:
		//<CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		try{
		String s = p.getMessageRefId();
		String cSender = s.substring(0, 2);
		String cReceiver = s.substring(6, 8);
		CountryCodeType.fromValue(cSender);
		CountryCodeType.fromValue(cReceiver);
		
		//XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		Date curdate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int currYear = Integer.parseInt(sdf.format(curdate));
		if(lg.isDebugEnabled())
		lg.debug(fp + "currYear: " + currYear);
		
		String tYear = s.substring(2,6);
		int yr = Integer.parseInt(tYear);
		if(lg.isDebugEnabled())
		lg.debug(fp + "tax year: " + yr);
		
		if(yr<Constants.MIX_TAX_YEAR || yr >= currYear)
			throw new InvalidMessageRefIdException(Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID, "Tax year in MessageRefId is invalid!", this);

		Long.parseLong(s.substring(8));
		
		}
		catch(Exception e){
			throw new InvalidMessageRefIdException(Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID, "Tax year in MessageRefId is invalid!", this);
		}
	}
}
