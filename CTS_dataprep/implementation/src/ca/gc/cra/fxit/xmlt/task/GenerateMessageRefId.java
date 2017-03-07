package ca.gc.cra.fxit.xmlt.task;

import java.sql.SQLException;
import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.dao.InsertRecordDAOBean;
import ca.gc.cra.fxit.xmlt.exception.InvalidMessageRefIdException;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.messagerefid.AbstractMessageRefId;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class GenerateMessageRefId extends AbstractMessageRefId {
	private static Logger lg = Logger.getLogger(GenerateMessageRefId.class);
	
	@Override
	public GenerateMessageRefId cloneTask(){
		GenerateMessageRefId t = new GenerateMessageRefId();
		t.setResultCode(this.resultCode);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
	
		return t;
	}
	
	@Override
	public final int invoke(PackageInfo p) {
		lg.debug("GenerateMessageRefID executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String messageRefId = null;
		boolean update = false;
		
		try {
			messageRefId = p.getMessageRefId();
			if(messageRefId==null){
				lg.info("messageRefId is not available, generating...");
				//generate message Ref ID if not available
				messageRefId = generateMessageRefID(p);
				if(messageRefId==null)
					throw new Exception("Failed to generate messageRefId");
				//set the flag to update MessageSpec element in the XML file
				update = true;
			}
			lg.info("messageRefId: " + messageRefId);
			
			//validate message Ref ID
			validateMessageRefId(p);		

			//messageRefIDd is valid, do stuff dependent on it
			setMessageRefIDDependants(p, update); 
			
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
		
		//TODO for wireframe testing only - to comment out!
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
		
	//	int status = Constants.STATUS_CODE_INCOMPLETE;
	
		//RetrieveFxmtDataInterface fxmtDataBean;
		//messageRefID = fxmtDataBean.getNextMessageRefID(TRANSMITTING_COUNTRY_CODE, RECEIVING_COUNTRY_CODE, headerRec.getRtnTxYr());
		InsertRecordDAOBean dao = new InsertRecordDAOBean();
		String messageRefId = dao.invoke(p);
			
		if(lg.isDebugEnabled())
			lg.debug(fp + "messageRefID: " + messageRefId);
		p.setMessageRefId(messageRefId);

		return messageRefId;
	}
}