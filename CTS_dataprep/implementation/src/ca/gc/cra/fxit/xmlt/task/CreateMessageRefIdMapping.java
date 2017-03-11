package ca.gc.cra.fxit.xmlt.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.model.wrapper.crs.FIWrapper;
import ca.gc.cra.fxit.xmlt.task.xml.CommonXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class CreateMessageRefIdMapping  extends AbstractTask {
	private static Logger lg = Logger.getLogger(CreateMessageRefIdMapping.class);
	
	@Override
	public final int invoke(PackageInfo p) {
		lg.info("CreateMessageRefIDMapping executing");
		
		int status = Constants.STATUS_CODE_INCOMPLETE;
		String fwDir = p.getFileWorkingDir();
		String dir = Globals.baseFileDir + Constants.OUTBOUND_PROCESSED_TOSEND_DIR;
		String origFilename = p.getOrigFilename();
		String inputFile  = fwDir + origFilename;	
		String outputFile = createMappingFileName(dir, origFilename, p);
		String messageRefID = p.getMessageRefId();
		
		if(lg.isDebugEnabled()){
			lg.debug("original file name: " + inputFile);	
			lg.debug("mapping file name: " + outputFile);	
			lg.debug("messageRefID: " + messageRefID);
		}
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try {
			String line;
			int code = 0;	
			reader = new BufferedReader(new FileReader(inputFile));			
			//File file = new File(outputFile);
			//if(!file.exists())
			//	file.createNewFile();
			writer = new BufferedWriter(new FileWriter(outputFile));
	
			//read text file; each line starts with the code specifying record type
			//process each line according to the code
			while((line = reader.readLine())!=null){
				//lineNum++;
				//lg.debug("line #" + lineNum);
				
				code = Integer.parseInt(line.substring(0,4));
				//if(lg.isDebugEnabled())
					//lg.debug(fp + "line code: " + code);
				
				switch(code){
				case Constants.LINE_CODE_HEADER:	//1001
					writer.write(line.substring(0,33));
					writer.newLine();
					break;
				case Constants.LINE_CODE_FI:     // 1002
					writeFI(line, messageRefID, writer);
					break;
				case Constants.LINE_CODE_SPONSOR:	//1003
					;
					break;
				case Constants.LINE_CODE_SLIP:		//1004
					writeAcctReport(line,writer);
					break;
				case Constants.LINE_CODE_PERSON: 	//1005
					;
					break;
				case Constants.LINE_CODE_ACCOUNT_HOLDER:  //1006
					;
					break;
				case Constants.LINE_CODE_TRAILER: 		//1007
					writer.write(line);
					break;
				default:
					throw new Exception("Invalid line code!");
				}
			}
			//if(lg.isDebugEnabled())
			//lg.debug(fp + lineNum + " lines read");
			
			status = Constants.STATUS_CODE_SUCCESS;
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_INVALID_INPUT_FILE;
			Utils.logError(lg, e);		
		}
		finally{
			try {
				reader.close();
			}catch(Exception e){}
			
			try {
				writer.flush();
				writer.close();
			}catch(Exception ex){}
		}
		
		return status;
	}
	
	private void writeFI(String line, String mrid, BufferedWriter w) throws Exception {
		String code = line.substring(0,4);
		String psn = line.substring(6,19);
		w.write(code);
		w.write(psn);
		w.write(mrid);
		w.newLine();
	}
	
	private void writeAcctReport(String line, BufferedWriter w) throws Exception {
		String code = line.substring(0,4);
		String seq = line.substring(5,14);
		w.write(code);
		w.write(seq);

		w.newLine();
	}
	
	/**
	 * This version created filename from XML filename
	 * @param fwDir
	 * @param origFilename
	 * @param p
	 * @return
	 */
	private String createMappingFileName(String fwDir, String __origFilename, PackageInfo p){
		StringBuilder sb = new StringBuilder();
		String xmlFilename = p.getXmlFilename();
		int idx = xmlFilename.lastIndexOf(".");
		String xmlFilenamePart = xmlFilename.substring(0,idx+1);
		sb.append(fwDir).append(Constants.PREFIX_MAPPING).append(xmlFilenamePart);

		return sb.toString();
	}
	
	/**
	 * This version created filename from original filename
	 * @param fwDir
	 * @param origFilename
	 * @param p
	 * @return
	 */
/*	private String createMappingFileName(String fwDir, String origFilename, PackageInfo p){
		StringBuilder sb = new StringBuilder();
		
		sb.append(fwDir).append(origFilename).append(".X");
		int splitCount = p.getSplitFileCount();
		if(splitCount==Constants.NO_SPLIT)
			sb.append("0000001");
		else{
			//current file is a part of splitted original file
			String xmlFilename = p.getXmlFilename();
			int idxDot = xmlFilename.lastIndexOf(".");
			int idxUnderscore = xmlFilename.lastIndexOf(Constants.UNDERSCORE);
			String stFileCnt = xmlFilename.substring(idxUnderscore+1, idxDot);
			int fileCountLength = stFileCnt.length();
			//int fileCnt = Integer.parseInt(stFileCnt);

			for(int i=0;i<(Constants.FILE_SEQUENCE_NUM_SIZE-fileCountLength);i++){
				sb.append(0);
			}	
			sb.append(stFileCnt);
		}
		return sb.toString();
	}*/
	
	@Override
	public CreateMessageRefIdMapping cloneTask(){
		CreateMessageRefIdMapping t = new CreateMessageRefIdMapping();
		t.setResultCode(this.resultCode);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);
		
		return t;
	}
}
