package ca.gc.cra.fxit.xmlt.task.messagerefid;

import java.io.FileReader;
import java.io.FileWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

//import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import ca.gc.cra.fxit.xmlt.exception.InvalidMessageRefIdException;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CountryCodeType;
import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.task.AbstractTask;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public abstract class AbstractMessageRefId extends AbstractTask {
	private static Logger lg = Logger.getLogger(AbstractMessageRefId.class);
	
	public void validateMessageRefId(PackageInfo p) throws InvalidMessageRefIdException, Exception {
		String fp = "validateMessageRefID:";
		lg.info(fp + " started");
		//Metadata senderFileId:
		//<CountryCd Sender>_<CountryCd Receiver>_<Communication_type>_MessageRefID
		try{
		String s = p.getMessageRefId();
		String cSender = s.substring(0, 2);
		String cReceiver = s.substring(4, 6);
		if(lg.isDebugEnabled())
			lg.debug(fp + "Sender: " + cSender + ", receiver: " + cReceiver);
		
		CountryCodeType.fromValue(cSender);
		CountryCodeType.fromValue(cReceiver);
		if(lg.isDebugEnabled())
			lg.debug(fp + "Sender and receiver validated");
		
		//XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		/*Date curdate = new Date(System.currentTimeMillis());
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int currYear = Integer.parseInt(sdf.format(curdate));
		if(lg.isDebugEnabled())
		lg.debug(fp + "currYear: " + currYear);*/
		
		String tYear = s.substring(2,4);
		int yr = Integer.parseInt(tYear);
		if(lg.isDebugEnabled())
		lg.debug(fp + "tax year: " + yr);
		
		//if(yr<Constants.MIX_TAX_YEAR || yr >= currYear)
		//	throw new InvalidMessageRefIdException(Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID, "Tax year in MessageRefId is invalid!", this);

		//tYear = s.substring(2,4);
		//if(lg.isDebugEnabled())
		//	lg.debug(fp + "tax year: " + yr);
		
		Long.parseLong(s.substring(8));
		if(lg.isDebugEnabled())
			lg.debug(fp + "parsed id ok");
		}
		catch(Exception e){
			throw new InvalidMessageRefIdException(Constants.STATUS_CODE_INVALID_MESSAGE_REF_ID, "Tax year in MessageRefId is invalid!", this);
		}
	}
	
	/**
	 * Sets properties dependent on MessageRefId
	 * @param p
	 * @param update
	 */
	public void setMessageRefIDDependants(PackageInfo p, boolean update) throws Exception {
		//set messageRefID in the MessageSpec element, 
		//include messageRefID in the XML file name 
		// and rename the XML file
		//new SAXUpdateMessageRefID(p,update).invoke();
		updateXML(p,update);
		
		//set metadata file name
		String metadataFilename = Constants.METADATA + Constants.UNDERSCORE + p.getXmlFilename();
		if(lg.isDebugEnabled())
			lg.debug("metadata filename: " + metadataFilename);
		p.setMetadataFilename(metadataFilename);
	}

	private void updateXML(PackageInfo pkg, final boolean update) throws Exception {		
		final String messageRefID = pkg.getMessageRefId();
		
		String fileWorkingDir = pkg.getFileWorkingDir();	
		//old path with a placeholder
		String oldXmlFilePath 		=  fileWorkingDir  + pkg.getXmlFilename();   //Globals.FILE_WORKING_DIR
		lg.info("oldXmlFilePath: " + oldXmlFilePath);
		
		//include messageRefID in the xml filename
		String newXmlFilename = pkg.getXmlFilename().replaceAll(Constants.MSG_REF_ID_PLACEHOLDER, pkg.getMessageRefId());
		//lg.info("newXmlFilename: " + newXmlFilename);
		
		String xmlFilePathNew 	= fileWorkingDir +  newXmlFilename;		//Globals.FILE_WORKING_DIR
		lg.info("xmlFilePathNew: " + xmlFilePathNew);
		
		//set it to the PackageInfo
		pkg.setXmlFilename(newXmlFilename);
	
	   // String xml = "<users><user><name>user1</name></user></users>";
	    XMLReader xr = new XMLFilterImpl(XMLReaderFactory.createXMLReader()) {
	        private String tagName = "";
	        @Override
	        public void startElement(String uri, String localName, String qName, Attributes atts)
	                throws SAXException {
	            tagName = qName;
	           // lg.info("XMLReader: startElement: qName: "+qName);
	            super.startElement(uri, localName, qName, atts);
	        }
	        @Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
	            tagName = "";
	            super.endElement(uri, localName, qName);
	        }
	        @Override
	        public void characters(char[] ch, int start, int length) throws SAXException {
	        	// lg.debug("XMLReader: characters: update: "+update);
	            if (update && tagName.equals("crs:MessageRefId")) {
	            	lg.info("XMLReader: characters: found MessageRefId tag");
	                ch = messageRefID.toCharArray();
	                start = 0;
	                length = ch.length; 
	            }
	            super.characters(ch, start, length);
	        }
	    };

		Source src = new SAXSource(xr, new InputSource(new FileReader(oldXmlFilePath)));
		Result res = new StreamResult(new FileWriter(xmlFilePathNew));
		TransformerFactory.newInstance().newTransformer().transform(src, res);
		
		try (FileWriter w = new FileWriter(oldXmlFilePath,false);) {			
			w.write("");
			w.flush();
			w.close();
			
			Utils.deleteFile(oldXmlFilePath);
			}
			catch(Exception e){}
		
	}
}
