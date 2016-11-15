package ca.gc.cra.fxit.ctsagent.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.util.AppProperties;
import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.util.Utils;

public class CheckFileSize extends AbstractTask{
	private static Logger log = Logger.getLogger(CheckFileSize.class);
	
	@Override
	protected final int invoke(PackageInfo p) {
		String fp = "invoke: ";
		log.debug(fp + "CheckFileSize executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		//String headerLine = null;
		//String lastSplitToken = null;
		
		try {
			String filename = p.getInputPathName();
			File file = new File(filename);
			long filesize = file.length();
			log.info(fp + "file size: " + filesize);
			long maxXmlFileSize = Long.parseLong(AppProperties.getProperty(Constants.KEY_MAX_XML_FILE_SIZE));
		
			if(filesize*Constants.TXT2XML_FACTOR<=maxXmlFileSize){
				log.info(fp + "file size ok, no splitting");
				return Constants.STATUS_CODE_SUCCESS;
			}
			
			int filecount = this.splitFile(file, maxXmlFileSize);	
			p.setSplitFileCount(filecount);
			int numOfFiles = filecount + 1;
			log.info(fp + "filecount is " + filecount + ", split into " +numOfFiles+" files");
		}
		catch(Exception e){
			status = Constants.STATUS_CODE_ERROR;
			Utils.logError(log, e);
		}
		
		return 0;
	}

	
	/**
	 * Splits original file into smaller files sizes that fit under threshold
	 * 
	 * @return int --number of files 
	 */
	private int splitFile(File file, long maxXmlFileSize){
		String fp = "splitFile: ";
	
		int filecount 		= 0 ;
		long inputLength 	= 0;
		long lineLength 	= 0;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			writer = new BufferedWriter(new FileWriter(file+"_SPLIT_"+filecount));
		String line;
		while((line =reader.readLine())!=null){
			if(line.startsWith(Constants.HDR_REC_TRANS_CD)){
				continue;			
			}
		
			lineLength = line.length();
			
			if((inputLength + lineLength)*Constants.TXT2XML_FACTOR<=maxXmlFileSize){
				//under the file size threshold			
				inputLength = inputLength + lineLength;
				log.debug(fp + "inputLength: " + inputLength);			
			}
			else{
				//exceeded the file size threshold
				//reset the writer
				filecount++;
				writer.flush();
				writer.close();
				writer = null;
				
				writer = new BufferedWriter(new FileWriter(file+"_SPLIT_"+filecount));
				
				//reset input length
				inputLength = 0;
				inputLength = inputLength + lineLength;
			}			
			writer.write(line);
			writer.flush();
		}
		}
		catch(Exception e){
			
		}
		finally{
			try {
				reader.close();
			}catch(Exception e){}
			try {
				writer.flush();
				writer.close();
			}catch(Exception e){}
		}
		return filecount;
	}
}
