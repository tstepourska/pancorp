package ca.gc.cra.fxit.xmlt.task;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.IllegalFormatException;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.dao.InsertRecordDAOBean;
//import ca.gc.cra.db.framework.exceptions.DataException;
//import ca.gc.cra.fxit.ca2us.batch.BridgeException;
//import ca.gc.cra.fxit.ca2us.data.RetrieveFxmtDataInterface;
//import ca.gc.cra.fxit.ca2us.integration.cobol.IP6PRTHD;
//import ca.gc.cra.fxit.xmlt.exception.TaskException;
//import ca.gc.cra.fxit.xmlt.task.compression.CompressionHelper;
import ca.gc.cra.fxit.xmlt.exception.InvalidMessageRefIdException;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.messagerefid.AbstractMessageRefId;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class GenerateMessageRefId extends AbstractMessageRefId {
	private static Logger lg = Logger.getLogger(GenerateMessageRefId.class);
	
	@Override
	public GenerateMessageRefId cloneTask(){
		GenerateMessageRefId t = new GenerateMessageRefId();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
	
		return t;
	}
	
	@Override
	protected final int invoke(PackageInfo p) {
		lg.debug("GenerateMessageRefID executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String messageRefId = null;
		
		try {
			messageRefId = p.getMessageRefId();
			if(messageRefId==null){
				//generate message Ref ID if not available
				messageRefId = generateMessageRefID(p);
			}
			lg.info("messageRefId: " + messageRefId);
			
			//validate message Ref ID
			validateMessageRefId(p);		

			//add message Ref ID to the xml file name
			String origXmlFileName = p.getXmlFilename();
			String finalXmlFileName = origXmlFileName.replaceAll(Constants.MSG_REF_ID_PLACEHOLDER, messageRefId);
			if(lg.isDebugEnabled())
				lg.debug("finalXmlFileName: " + finalXmlFileName);
			p.setXmlFilename(finalXmlFileName);
			boolean renamed = Utils.renameFile(Globals.FILE_WORKING_DIR + origXmlFileName,Globals.FILE_WORKING_DIR + finalXmlFileName);
			
			if(!renamed)
				throw new Exception("Could not attach messageRefID to the file name");
			
			String metadataFilename = Constants.METADATA + Constants.UNDERSCORE + finalXmlFileName;
			if(lg.isDebugEnabled())
				lg.debug("metadata filename: " + metadataFilename);
			p.setMetadataFilename(metadataFilename);
			
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(InvalidMessageRefIdException e){
			status = Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID;
		}
		catch(SQLException sqle){
			status = Constants.STATUS_CODE_ERROR;
			lg.error("SQL Error: " + sqle.getErrorCode());
			Utils.logError(lg, sqle);
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(lg, e);
		}
		
		//for wireframe testing only - to comment out!
		//status = Constants.STATUS_CODE_SUCCESS;
		// end of to comment out
		
		return status;	
	}
	
	/**
	 * Invokes DAO to insert new record, either generating new MessageRefId, 
	 * or inserting available one.
	 * 
	 * @param p
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	private String generateMessageRefID(PackageInfo p) throws SQLException, Exception {
		String fp = "generateMessageRefID:";
		lg.debug("Begin " + fp);
		
		int status = Constants.STATUS_CODE_INCOMPLETE;
	
		//RetrieveFxmtDataInterface fxmtDataBean;
		//messageRefID = fxmtDataBean.getNextMessageRefID(TRANSMITTING_COUNTRY_CODE, RECEIVING_COUNTRY_CODE, headerRec.getRtnTxYr());
		InsertRecordDAOBean dao = new InsertRecordDAOBean();
		String messageRefId = dao.invoke(p);
	
		// TODO: Interim solution to hard-code value "US" until recipient country code is validated in InfoDec. 
		// RtnRcpntCntryCd is currently not validated in InfoDec, and can be any 2 characters.
					
		if(lg.isDebugEnabled())
			lg.debug(fp + "messageRefID: " + messageRefId);
		p.setMessageRefId(messageRefId);

		return messageRefId;
	}
}