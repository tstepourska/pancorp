package ca.gc.cra.fxit.xmlt.job;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;
import javax.xml.datatype.XMLGregorianCalendar;

import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;

/**
 * 
 * @author Txs285
 *
 */

@Remote
public interface IExternalJobInitiatorRemote 
{
	public int initStatusMessage(String dataProvider, 
			String messageRefId, 
			String fileAcceptanceStatus, 
			List<FileErrorType> fileErrors, 
			List<RecordErrorType> recordErrors, 
			String origCTSTransmissionId,
			String countryToSend,
			XMLGregorianCalendar origCTStimestamp,
			String origSenderFileId,
			BigInteger origFileSize,
			XMLGregorianCalendar repPeriod);

	public int validateMetadata();
	public int validateXML();
}