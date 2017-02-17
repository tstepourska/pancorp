package ca.gc.cra.fxit.xmlt.task.messagerefid;

import java.io.FileReader;
import java.io.FileWriter;
//import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
//import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;

public class SAXUpdateMessageRefID {
	private static Logger lg = Logger.getLogger(SAXUpdateMessageRefID.class);
	PackageInfo pkg;
	boolean update;
	
	public SAXUpdateMessageRefID(PackageInfo p, boolean updateID){
		pkg = p;
		update = updateID;
	}

	public void invoke() throws Exception {		
		final String messageRefID = pkg.getMessageRefId();
		String xmlFilePath 		= Globals.FILE_WORKING_DIR + pkg.getXmlFilename();
		lg.info("xmlFilePath: " + xmlFilePath);
		//include messageRefID in the xml filename
		String newXmlFilename = pkg.getXmlFilename().replaceAll(Constants.MSG_REF_ID_PLACEHOLDER, pkg.getMessageRefId());
		//lg.info("newXmlFilename: " + newXmlFilename);
		String xmlFilePathNew 	= Globals.FILE_WORKING_DIR + newXmlFilename;
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

		Source src = new SAXSource(xr, new InputSource(new FileReader(xmlFilePath)));
		Result res = new StreamResult(new FileWriter(xmlFilePathNew));
		TransformerFactory.newInstance().newTransformer().transform(src, res);
	}
	
	//orig src
/*	public static void main(String[] args) throws Exception {
	    String xml = "<users><user><name>user1</name></user></users>";
	    XMLReader xr = new XMLFilterImpl(XMLReaderFactory.createXMLReader()) {
	        private String tagName = "";
	        @Override
	        public void startElement(String uri, String localName, String qName, Attributes atts)
	                throws SAXException {
	            tagName = qName;
	            super.startElement(uri, localName, qName, atts);
	        }
	        @Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
	            tagName = "";
	            super.endElement(uri, localName, qName);
	        }
	        @Override
	        public void characters(char[] ch, int start, int length) throws SAXException {
	            if (tagName.equals("name")) {
	                ch = "user2".toCharArray();
	                start = 0;
	                length = ch.length; 
	            }
	            super.characters(ch, start, length);
	        }
	    };
	    Source src = new SAXSource(xr, new InputSource(new StringReader(xml)));
	    Result res = new StreamResult(System.out);
	    TransformerFactory.newInstance().newTransformer().transform(src, res);
	}*/
}
