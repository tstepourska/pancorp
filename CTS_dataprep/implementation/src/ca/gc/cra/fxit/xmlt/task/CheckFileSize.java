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

	private String header = null;
	private String trailer = null;
	int cntHeader = 0;
	int cntFI=0;
	int cntSponsor=0;
	int cntSlip=0;
	int cntPerson=0;
	int cntAcctHolder=0;
	int cntTrailer=0;
	
	@Override
	public final int invoke(PackageInfo p) {
		//String fp = "invoke: ";
		lg.debug("CheckFileSize executing");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			if(lg.isDebugEnabled()){
				lg.debug("Package: " + p.toString());
			}
			String filename = p.getFileWorkingDir() + p.getOrigFilename();			// Globals.FILE_WORKING_DIR+
			String country = p.getReceivingCountry();
			lg.info("Checking size of file " + filename + " for " + country);
			//String xmlFilename = null;
			
			File file = new File(filename);
			long filesize = file.length();
			lg.info("file size: " + filesize);
			p.setOrigUncompressFileSizeKBQty(BigInteger.valueOf(filesize));

			//get allowed maximum file size from configuration
			Globals.FileSize fs = Globals.specificFileSizes.get(country);
			lg.info("FileSize object: " + fs);

			//estimate final file size and set split file count to package
			double splitFactor = estimateSize(filesize,p,  fs);
			lg.info("splitFactor: " + splitFactor);
			
			//estimated file size exceeds maximum allowed
			if(splitFactor>Constants.NO_SPLIT){
				//no splitting of XML files, reject
				if(filename.endsWith(Constants.FILE_EXT_XML)){
					status = Constants.STATUS_CODE_FILE_REJECTED_TOO_BIG;
					return status;
				}
				
				//split flat file into chunks and set split count to package info
				int splitCount = splitFile(filename, splitFactor, p);
				if(lg.isDebugEnabled())
					lg.debug("splitCount: " + splitCount);
				//set status for TaskManager
				status = Constants.STATUS_CODE_CREATE_JOB_LOOP;
			}
			else { //estimated file size is within the limit, no split
				//generate single XML file name and metadata file name and 
				//set it to the package info object
				p.setXmlFilename	(Utils.generateXMLFileName(p, false)+"_1.xml");
				p.setMetadataFilename(Utils.generateMetadataFilename(p, true));
				if(lg.isDebugEnabled()){
					lg.debug("XMLFile: " + p.getXmlFilename() + ", Metadata name: " + p.getMetadataFilename());
				}
				
				status = Constants.STATUS_CODE_SUCCESS;
			}
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
		
		lg.debug(fp + "filesize: " + filesize);
		
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
			estimatedSize = (filesize*Globals.txtToXmlFactor+Globals.fileSignatureSizeConstant)*Globals.payloadCompressionRatio + Globals.fileSizeConstant;
			//size of XML payload non-compressed with digital signature
			//estimatedSizeUncompressed = filesize*Globals.TxtToXmlFactor+Globals.FileSignatureSizeConstant;
			//estimatedSizeUncompressedNoSignature = estimatedSizeUncompressed-Globals.FileSignatureSizeConstant;
		}
		else{
			//final total size of XML payload non-compressed with digital signature
			estimatedSize = filesize*Globals.txtToXmlFactor + Globals.fileSignatureSizeConstant;
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
	private int splitFile(String fn, double splitFactor, PackageInfo p) throws Exception {
		String fp = "splitFile: ";
		//int splitCount =(int) Math.ceil(splitFactor);	
		int lineNum = 0;
		Exception err = null;
		String line;
		int code = 0;
		
		
		//first pass - count number of lines 
		try (BufferedReader reader = new BufferedReader(new FileReader(fn))) {
			while((line = reader.readLine())!=null){
				//parse the code
				code = Integer.parseInt(line.substring(0,4));
				switch(code){
				case Constants.LINE_CODE_HEADER:   //1001;
					cntHeader++;
					header = line;
					lg.info(fp + " header: " + header);
					break;
				case Constants.LINE_CODE_FI:   //1002;
					cntFI++;
					break;
				case Constants.LINE_CODE_SPONSOR:   //1003;
					cntSponsor++;
					break;
				case Constants.LINE_CODE_SLIP:   //1004;
					cntSlip++;
					break;
				case Constants.LINE_CODE_PERSON:   //1005;
					cntPerson++;
					break;
				case Constants.LINE_CODE_ACCOUNT_HOLDER:   //1006;
					cntAcctHolder++;
					break;
				case Constants.LINE_CODE_TRAILER:   //1007
					cntTrailer++;
					trailer = line;
					break;
					default:
						lg.error("Unsupported code: " + code);
				}
				
				//total line num
				lineNum++;
			}			
			
		}
		catch(Exception e){
			Utils.logError(lg, e);		
		}					
		lg.info(fp + "Original file counters: ");
			lg.info(fp + "lineNums: " + lineNum);
			lg.info(fp + "cntHeader: " + cntHeader);
			lg.info(fp + "cntFI: " + cntFI);		
			lg.info(fp + "cntSponsor: " + cntSponsor);
			lg.info(fp + "cntSlip: " + cntSlip);
			lg.info(fp + "cntPerson: " + cntPerson);
			lg.info(fp + "cntAcctHolder: " + cntAcctHolder);
			lg.info(fp + "cntTrailer: " + cntTrailer);
			/*
			cntHeader = 0;
			cntFI=0;
			cntSponsor=0;
			cntSlip=0;
			cntPerson=0;
			cntAcctHolder=0;
			cntTrailer=0;
			*/
		
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
			writer.write(header);
			//writer.newLine(); //not here
			lineCounter++;
			
			line = null;
			int returnChunkCounter = 0;
	
			lg.info(fp + "starting chunk #" + chunkCounter);
			while((line=reader.readLine())!=null){
				returnChunkCounter = writeLine(writer, line, chunkCounter, chunkNumLines, lineCounter, fiRecordBuffer);
				lineCounter++;
			//	lg.info(fp + "returning chunk #" + chunkCounter);
				
				if(returnChunkCounter>chunkCounter){			
					//output couters from completed file
					//TODO count lines in each file and check sum
					lg.info("Counted chunk#" + chunkCounter);
					lg.info(fp + "lineNums: " + lineNum);
					//lg.info(fp + "cntHeader: " + cntHeader);
					lg.info(fp + "cntFI: " + cntFI);		
					lg.info(fp + "cntSponsor: " + cntSponsor);
					lg.info(fp + "cntSlip: " + cntSlip);
					lg.info(fp + "cntPerson: " + cntPerson);
					lg.info(fp + "cntAcctHolder: " + cntAcctHolder);
					//lg.info(fp + "cntTrailer: " + cntTrailer);

					//reset and start new chunk
					chunkCounter = returnChunkCounter;		
									
					try {						
						writer.flush();
						writer.close();
					}catch(Exception ex){}
										
					lg.info(fp + "starting chunk #" + chunkCounter);
					
					lineCounter = 0;
					writer = new BufferedWriter(new FileWriter(fn+Constants.UNDERSCORE+chunkCounter));
					//writing header for each new file
					writer.write(header);
					lineCounter++;
				}
			}
			
			//flush remaining cache, if any
			if(!fiRecordBuffer.isEmpty()){
				writer.newLine();	//append new line before each reportingFI
				while(!fiRecordBuffer.isEmpty()){
					writer.write(fiRecordBuffer.removeFirst());
				}
			}
			
			p.setSplitFileCount(chunkCounter);
			
			//output couters from completed file
			//here the final counter value of each record type should be 0
			lg.info("Counted chunk#" + chunkCounter);
			lg.info(fp + "lineNums: " + lineNum);
			//lg.info(fp + "cntHeader: " + cntHeader);
			lg.info(fp + "cntFI: " + cntFI);		
			lg.info(fp + "cntSponsor: " + cntSponsor);
			lg.info(fp + "cntSlip: " + cntSlip);
			lg.info(fp + "cntPerson: " + cntPerson);
			lg.info(fp + "cntAcctHolder: " + cntAcctHolder);
			//lg.info(fp + "cntTrailer: " + cntTrailer);
			
			//String item = null;
			if(cntFI!=0 ||cntSponsor!= 0|| cntSlip!=0||cntPerson!=0||cntAcctHolder!=0)
				throw new Exception("Number of records in a splitted files does not match number of records in the original file! See logs for details");
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
		if(err!=null){
			//error splitting file
			throw err;
		}
		
		//add footer to each file
		for(int i=1;i<=chunkCounter;i++){
			try{
			writer = new BufferedWriter(new FileWriter(fn+Constants.UNDERSCORE+i, true));
			writer.newLine();
			writer.write(trailer);
			writer.flush();
			}
			catch(Exception e){
				lg.error(fp + "error appending footer: " + e.getMessage());
			}
			finally{
				try {
					writer.close();
				}catch(Exception ex){}
			}		
		}

		String originalFileName = p.getFileWorkingDir() +p.getOrigFilename();		//Globals.FILE_WORKING_DIR
		lg.info("originalFileName: " + originalFileName);
		
		try {
			/*FileWriter w = new FileWriter(originalFileName);
			w.write("");
			w.flush();
			w.close();*/
			
			//TODo uncomment
			Utils.deleteFile(originalFileName);
		}
		catch(Exception e){}
		
		return chunkCounter;
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
		int check = 0;
		
		//parse the code
		int code = Integer.parseInt(line.substring(0,4));
		switch(code){
		case Constants.LINE_CODE_HEADER:   //1001;
			cntHeader--;
			//DO NOT WRITE ANYTHING HERE, its taken care of in the parent method
			break;
		case Constants.LINE_CODE_FI:   //1002;
			cntFI--;
			//check if there is a space for a previous FI record plus a current line to be written
			check = firec.size()+lineCounter + 1;

			if(check<=chunkNumLines){
				//space ok
				if(!firec.isEmpty()){
					writer.newLine();	//append new line before each reportingFI
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
			firec.addLast(line);//add start of the new record line to the cache	
			break;
		case Constants.LINE_CODE_SPONSOR:   //1003;
			cntSponsor--;
			firec.addLast(line);	//add any subrecord that belongs to the current fi record to the cache
			break;
		case Constants.LINE_CODE_SLIP:   //1004;
			cntSlip--;
			firec.addLast(line);	//add any subrecord that belongs to the current fi record to the cache
			break;
		case Constants.LINE_CODE_PERSON:   //1005;
			cntPerson--;
			firec.addLast(line);	//add any subrecord that belongs to the current fi record to the cache
			break;
		case Constants.LINE_CODE_ACCOUNT_HOLDER:   //1006;
			cntAcctHolder--;
			firec.addLast(line);	//add any subrecord that belongs to the current fi record to the cache
			break;
		case Constants.LINE_CODE_TRAILER:   //1007
			cntTrailer--;
			//check if there is a space for a previous FI record plus a current line to be written
			check = firec.size()+lineCounter + 1;

			//if(lg.isDebugEnabled())
			//lg.debug(fp + "check==" + check + ", chunkNumLines==" + chunkNumLines);
			if(check<=chunkNumLines){
				//space ok				
				if(!firec.isEmpty()){
					writer.newLine();	//append new line before each reportingFI
					while(!firec.isEmpty()){
						writer.write(firec.removeFirst()); //write previous FI if any
						if(firec.size()>1)
						writer.newLine();						
					}
					writer.flush();
				}				
			}
			break;
			default:
				lg.error("Unsupported code: " + code);
		}

		return chunkCounter;
	}
	
}
