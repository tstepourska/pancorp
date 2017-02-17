package ca.gc.cra.fxit.xmlt.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
//import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;

//import ca.gc.cra.fxit.xmlt.exception.EndOfChunkEvent;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class CheckFileSize extends AbstractTask{
	private static Logger lg = Logger.getLogger(CheckFileSize.class);

	@Override
	public final int invoke(PackageInfo p) {
		String fp = "invoke: ";
		lg.debug("CheckFileSize executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			String filename = Globals.FILE_WORKING_DIR+ p.getOrigFilename();			
			String country = p.getReceivingCountry();
			lg.info(fp+"Checking size of file " + filename + " for " + country);
			
			File file = new File(filename);
			long filesize = file.length();
			lg.info(fp + "file size: " + filesize);
			p.setOrigUncompressFileSizeKBQty(BigInteger.valueOf(filesize));

			//get allowed maximum file size from configuration
			Globals.FileSize fs =Globals.specificFileSizes.get(country);

			//estimate final file size and set split file count to package
			double splitFactor = estimateSize(filesize,p,  fs);
			//lg.info(fp + "splitFileCount: " + splitFileCount);
			//p.setSplitFileCount(splitFileCount);			
			
			if(splitFactor>Constants.NO_SPLIT){
				//no splitting of XML files, reject
				if(filename.endsWith(Constants.FILE_EXT_XML)){
					status = Constants.STATUS_CODE_FILE_REJECTED_TOO_BIG;
					return status;
				}
				
				//split flat file
				splitFile(filename, splitFactor, p);			
			}

			status = Constants.STATUS_CODE_SUCCESS;
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
	

	@Override
	public CheckFileSize cloneTask(){
		CheckFileSize t = new CheckFileSize();
		t.setResultCode(this.resultCode);
		t.setResultMessage(this.resultMessage);
		t.setId(this.id);
		t.setSequence(this.sequence);
		t.setJobId(this.jobId);

		return t;
	}

	///////////////////////////////////////////////////
	// Private methods
	///////////////////////////////////////////////////
	/**
	 * TODO! test calculations of:
	 * 				==>flat to XML factor for each data provider 
	 * 				==>compression ratio
	 * 
	 * @param filesize
	 * @param p
	 * @return
	 */
	private double estimateSize(long filesize, PackageInfo p, Globals.FileSize fs){
		String fp = "estimateSize: ";
		//int splitCount = Constants.NO_SPLIT;
		boolean compressed = true;
		long maxSize = -1;
		double estimatedSize = -1;
		//double estimatedSizeUncompressed = -1;
		//double estimatedSizeUncompressedNoSignature = -1;
		//int xmlChunkSize = 0;
		
		if(fs==null){
			maxSize = Globals.defaultMaxPkgSize;
			compressed = Globals.defaultPkgCompressed;
		}
		else {
			maxSize = fs.getSize();
			compressed = fs.isCompressed();
		}
		if(lg.isDebugEnabled())
			lg.debug(fp + "maxSize=" + maxSize + ", compressed: " + compressed);
		//						actual			with spare		compress ratio
		//signature - 			839 			900
		//compressed payload	4096
		//payload								0%
		
		//payload non-compr		326,205,440 b  
		//payload compr			  1,896,448 b					99% (0.58% actual size)
		
		//metadata				630				650				51%
		//key 					261 			270				0%
		//all compressed pack	4096
		
		//total size: sign+meta+key:	1820		2000			*0.01=20 b
		
		//250 Mb compressed = 1%
		// *100 = 25 000 Mb = 25 Gb
		
		//byte[] mybytes = System.Text.Encoding.UTF8.GetBytes(data);
		
		if(compressed){
			//final size of the package
			estimatedSize = (filesize*Globals.TxtToXmlFactor+Globals.FileSignatureSizeConstant)*Globals.PayloadCompressionRatio + Globals.FileSizeConstant;
			//size of XML payload non-compressed with digital signature
			//estimatedSizeUncompressed = filesize*Globals.TxtToXmlFactor+Globals.FileSignatureSizeConstant;
			//estimatedSizeUncompressedNoSignature = estimatedSizeUncompressed-Globals.FileSignatureSizeConstant;
		}
		else{
			//final total size of XML payload non-compressed with digital signature
			estimatedSize = filesize*Globals.TxtToXmlFactor + Globals.FileSignatureSizeConstant;
			//size of XML payload non-compressed with digital signature
			//estimatedSizeUncompressed = estimatedSize;
			//estimatedSizeUncompressedNoSignature = estimatedSizeUncompressed-Globals.FileSignatureSizeConstant;
		}
		if(lg.isDebugEnabled())
			lg.debug(fp + "estimatedSize=" + estimatedSize);
		
		double splitFactor = estimatedSize/maxSize;
		if(lg.isDebugEnabled())
			lg.debug(fp + "splitFactor=" + splitFactor);

		return splitFactor;
	}
	
	//@SuppressWarnings("resource")
	private void splitFile(String fn, double splitFactor, PackageInfo p) throws Exception {
		String fp = "splitFile: ";
		//int splitCount =(int) Math.ceil(splitFactor);	
		int lineNum = 0;
		Exception err = null;
		
		//first pass - count number of lines 
		try (BufferedReader reader = new BufferedReader(new FileReader(fn))) {
			while((reader.readLine())!=null)
				lineNum++;
		}
		catch(Exception e){
			Utils.logError(lg, e);		
		}					
		if(lg.isDebugEnabled())
			lg.debug(fp + "lineNums: " + lineNum);
		
		//calculate max number of lines in a chunk
		int chunkNumLines = (int) Math.floor(lineNum / splitFactor);
		if(lg.isDebugEnabled())
			lg.debug(fp + "chunkNumLines: " + chunkNumLines);

		int chunkCounter = 1;
		int lineCounter = 0;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		//keeps the last fi record with all corresponding elements here
		LinkedList<String> fiRecordBuffer = new LinkedList<>();
		
		try {	
			reader = new BufferedReader(new FileReader(fn));
			writer = new BufferedWriter(new FileWriter(fn+Constants.UNDERSCORE+chunkCounter));
			
			String line;
			int returnChunkCounter = 0;
	
			lg.info(fp + "starting chunk #" + chunkCounter);
			while((line=reader.readLine())!=null){
				returnChunkCounter = writeLine(writer, line, chunkCounter, chunkNumLines, lineCounter, fiRecordBuffer);
				lineCounter++;
			//	lg.info(fp + "returning chunk #" + chunkCounter);
				
				if(returnChunkCounter>chunkCounter){
					//reset and start new chunk
					chunkCounter = returnChunkCounter;		
					lg.info(fp + "starting chunk #" + chunkCounter);
					lineCounter = 0;
					try {
						writer.flush();
						writer.close();
					}catch(Exception ex){}
					writer = new BufferedWriter(new FileWriter(fn+Constants.UNDERSCORE+chunkCounter));
				}
			}
			
			//flush remaining cache, if any
			while(!fiRecordBuffer.isEmpty()){
				writer.write(fiRecordBuffer.removeFirst());
			}
			
			p.setSplitFileCount(chunkCounter);
		}
		catch(Exception e){
			Utils.logError(lg, e);	
			err = e;
		}
		finally{
			try{
				reader.close();
			}	catch(Exception e){}
			
			try{
				writer.flush();
				writer.close();
			}	catch(Exception e){}
		}
		if(err!=null)
			throw err;
	}
	
	/**
	 * Writes a single line. When required, dumps the queue, and/or resets chunkCounter
	 * 
	 * @param writer
	 * @param line
	 * @param chunkCounter
	 * @param chunkNumLines
	 * @param lineCounter
	 * @param firec
	 * @return
	 * @throws IOException
	 */
	private int writeLine(BufferedWriter writer, String line, int chunkCounter, int chunkNumLines, int lineCounter, LinkedList<String> firec) throws IOException {
		String fp = "writeLine: ";
		int code = Integer.parseInt(line.substring(0,4));

		if(code==Constants.LINE_CODE_FI || code==Constants.LINE_CODE_TRAILER) {//start of the new FI record
			//check if there is a space for a previous FI record plus a current line to be written
			int check = firec.size()+lineCounter + 1;
			lg.debug(fp + "check==" + check + ", chunkNumLines==" + chunkNumLines);
			if(firec.size()+lineCounter + 1<=chunkNumLines){
				//space ok
				if(lg.isDebugEnabled()){
					lg.debug(fp + check + "<=" + chunkNumLines);
					lg.debug(fp + "line code: " + code + ", flushing old FI, starting new");
				}
				
				if(!firec.isEmpty()){
					while(!firec.isEmpty()){
						writer.write(firec.removeFirst()); //write previous FI if any
						if(firec.size()>1)
						writer.newLine();						
					}
					writer.flush();
				}				
			}
			else {
				//not enough space
				
				//set counter for a new chunk
				chunkCounter++;
			}
			firec.addLast(line);//add start of the new record line (or trailer line) to the cache			
		}
		else if(code==Constants.LINE_CODE_HEADER){
			if(lineCounter<=chunkNumLines){
				writer.write(line);
				if(lineCounter<chunkNumLines)
					writer.newLine();
				
				//lineCounter++;
			}
		}
		else {
			firec.addLast(line);	//add any subrecord that belongs to the current fi record to the cache
		}
	
		return chunkCounter;
	}
		
	/*
	private void countLines(String fn, PackageInfo p){
		String fp = "splitFile: ";
		int code = 0;
		int splitCount = p.getSplitFileCount();
		String fi = "";
		
		int lineNum = 0;

		int cHd = 0;
		int cFi = 0;
		int cSp = 0;
		int cAr = 0;
		int cPer = 0;
		int cAccHold = 0;
		int cTrail = 0;
		
		int cTotSp = 0;
		int cTotAr = 0;
		int cTotPer = 0;
		int cTotAccHold = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fn))) {
			String line;
			while((line = reader.readLine())!=null){
				lineNum++;
			code = Integer.parseInt(line.substring(0,4));
			//if(lg.isDebugEnabled())
			//	lg.debug(fp + "line code: " + code);
			
			switch(code){
			case Constants.LINE_CODE_HEADER:	//1001
				cHd++;
				break;
			case Constants.LINE_CODE_FI:     // 1002
				//process
				fi = "fi="+cFi + ",cSp=" + cSp + ",cAr=" + cAr + ",cPer=" + cPer + ",cAcctHold=" + cAccHold;
				lg.info(fi);
				
				//reset
				fi = "";
				cSp = 0;
				cAr = 0;
				cPer = 0;
				cAccHold = 0;
				
				//start new reporting group
				cFi++;
				
				break;
			case Constants.LINE_CODE_SPONSOR:	//1003
				cSp++;
				cTotSp++;
				break;
			case Constants.LINE_CODE_SLIP:		//1004
				cAr++;
				cTotAr++;
				break;
			case Constants.LINE_CODE_PERSON: 	//1005
				cPer++;
				cTotPer++;
				break;
			case Constants.LINE_CODE_ACCOUNT_HOLDER:  //1006
				cAccHold++;
				cTotAccHold++;
				break;
			case Constants.LINE_CODE_TRAILER: 		//1007
				cTrail++;
				
				//process last
				fi = "fi="+cFi + ",cSp=" + cSp + ",cAr=" + cAr + ",cPer=" + cPer + ",cAcctHold=" + cAccHold;
				lg.info(fi);
				break;
			default:
				//throw new Exception("Invalid line code!");
			}
			}
			
			if(lg.isDebugEnabled()){
				lg.debug("Lines: " + lineNum);
				lg.debug("Headers: " + cHd);
				lg.debug("FIs: " + cFi);
				lg.debug("Total Sponsors: " + cTotSp);
				lg.debug("Total Acct Reports: " + cTotAr);
				lg.debug("Total Persons: " + cTotPer);
				lg.debug("Total Acct Holders: " + cTotAccHold);
				lg.debug("Total Trailers: " + cTrail);
			}
		}
		catch(Exception e){
			Utils.logError(lg, e);		
		}			
	}
	*/
	
	public static void main(String[] args){
		CheckFileSize c = new CheckFileSize();
		PackageInfo p = new PackageInfo();
		p.setSplitFileCount(3);
		//int status = c.invoke(p);
		String filename = "C:/git/repository/CTS_dataprep/test/testfiles/outbound/unprocessed/IP.AIP5S182.CAUS.A14.S0000001";
		
		try {
		//c.splitFile(filename, p);
		}
		catch(Exception e){
			
		}
	}
}
