package ca.gc.cra.fxit.xmlt.task.xml.statusmessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.task.xml.CustomXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.MessageSpecType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.OriginalMessageType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.ValidationErrorsType;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.statusmessage.ValidationResultType;


/**
 * JaxbMarshaller class is used to transform large flat files into International CRS XML format.
 * JaxbMarshaller reads and transforms records one by one and appends the resulting XML to an output CRS XML file.
 * Characters &<>"' are escaped where necessary in XML element, and attribute values by JAXB and StAX.
 * 
 */
public class JaxbMarshaller {
	private static Logger lg = Logger.getLogger(JaxbMarshaller.class);
	private JAXBTransformerContext transformerContext = new JAXBTransformerContext();		
	private Marshaller fragmentMarshaller = null;
	
	//ROOT:  CRSStatusMessage_OECD  attributes  version="1.0"
    private final static QName MESSAGE_SPEC_QNAME 		= new QName("urn:oecd:ties:csm:v1", "MessageSpec", 			"csm");	//1
    private final static QName ORIGINAL_MSG_QNAME 		= new QName("urn:oecd:ties:csm:v1", "OriginalMessageType", 	"csm");	//   
    private final static QName VALIDATION_ERRORS_QNAME 	= new QName("urn:oecd:ties:csm:v1", "ValidationErrorsType", "csm");	//   
    private final static QName VALIDATION_RESULT_QNAME 	= new QName("urn:oecd:ties:csm:v1", "ValidationResultType", "csm");	// 
    
    /**
     * Single JAXBContext instance for JaxbMarshaller
     */
    private class JAXBTransformerContext {
        private JAXBContext context = null;

        public JAXBTransformerContext() { }

        public JAXBContext getInstance() throws JAXBException{
            if (context == null) {
            	context = JAXBContext.newInstance(
            			MessageSpecType.class,
            			OriginalMessageType.class,
            			ValidationErrorsType.class,
            			ValidationResultType.class
            			);
            }
            return context;
        }
    }
    
    /**
     * Get JaxbMarshaller object that will be used to convert a java content tree into XML data.
	 * The JAXB_FRAGMENT property is set. This is necessary to enable marshalling of XML fragments to 
	 * require a fragment of the content tree in memory as opposed to the entire java content tree in memory at once.
	 * 
     * @return
     * @throws JAXBException
     * @throws SAXException
     */
    private Marshaller getFragmentMarshaller() throws JAXBException, SAXException {
    	if (fragmentMarshaller == null) {
		    JAXBContext context = transformerContext.getInstance();
			fragmentMarshaller = context.createMarshaller();
			fragmentMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			fragmentMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			fragmentMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
    	}

	    return fragmentMarshaller;
    }
    
    public void startDocument(CustomXMLStreamWriter writer) throws Exception {
    	writer.writeStartDocument	("UTF-8","1.0");
    	writer.writeCharacters		("\n");
		writer.writeStartElement	("csm", "CRSStatusMessage_OECD", "urn:oecd:ties:csm:v1");	
		writer.writeAttribute		("version", "1.1");

		/*
		 * xmlns:csm="urn:oecd:ties:csm:v1" 
		 * xmlns:iso="urn:oecd:ties:isocsmtypes:v1" 
		 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		 */

		writer.writeCustomNamespace		("csm", "urn:oecd:ties:csm:v1");
		writer.writeCustomNamespace		("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeCustomNamespace		("iso", "urn:oecd:ties:isocsmtypes:v1");
		
		writer.writeCharacters		("\n");
    }
        
	/**
	 * This method will perform the transformation of the input parameters into one XML MessageSpec.  
	 *
	 * @param reportingFIRec
	 * @param warning
	 * @param contact
	 * @param messageRefId
	 * @param corrMessageRefId
	 * @param timestamp
	 * @param writer
	 * 
	 * @throws Exception
	 */
	public void transformMessageSpec(
			MessageSpecType messageSpec,
            CustomXMLStreamWriter writer
	) throws Exception {
			String fp = "transformMessageSpec: ";
			if(lg.isDebugEnabled()){
		lg.debug(fp + "transforming messageSpec: " + messageSpec);
		lg.debug(fp + "using writer: " + writer);
			}
		Marshaller marshaller = getFragmentMarshaller();
		JAXBElement<MessageSpecType> el = new JAXBElement<MessageSpecType>(MESSAGE_SPEC_QNAME,MessageSpecType.class, messageSpec);		
		if(lg.isDebugEnabled())
			lg.debug(fp + "created JAXBElement MessageSpecType: " + el);

	    marshaller.marshal(el, writer);
	}
		
	/**
	 * Transforms an AccountReport record - one SLIP, one ACCOUNT HOLDER, and zero or more 
	 * CONTROLLING PERSON records - into one XML AccountReport
	 * 
	 * @param slipRec
	 * @param accountHolderRec
	 * @param personRecs
	 * @param writer
	 * 
	 * @throws Exception
	 */
	public void transformOriginalMessageType(
			OriginalMessageType omt, 
		CustomXMLStreamWriter writer) throws Exception {
		
		
		writer.writeCharacters("\n");
		writer.flush();
		
		writer.writeStartElement("csm", "crsStatusMessage", null);//namespace uri "urn:oecd:ties:crs:v1"
		writer.writeCharacters		("\n");
		writer.flush();		
		
		Marshaller marshaller = getFragmentMarshaller();
	    marshaller.marshal(new JAXBElement<OriginalMessageType>(
	        						ORIGINAL_MSG_QNAME,
	        						OriginalMessageType.class, omt),  writer);
	}
	
	/**
	 * This method will perform the transformation of one SUMMARY record into one XML ReportingFI.
	 * 
	 * @param reportingFIRec	input is read from this record
	 * @param writer			the output is written to this writer
	 * @throws BridgeException
	 */
	public void transformValidationErrorsType(ValidationErrorsType vet,
			CustomXMLStreamWriter writer) throws Exception {

		writer.writeCharacters("\n");	
		writer.flush();
	    
		Marshaller marshaller = getFragmentMarshaller();
        marshaller.marshal(new JAXBElement<ValidationErrorsType>(
        		VALIDATION_ERRORS_QNAME,
        		ValidationErrorsType.class, vet), writer);
	}
	
	public void transformValidationResultType(ValidationResultType vrt, 
			CustomXMLStreamWriter writer) throws Exception {
		writer.writeCharacters("\n");	
		writer.flush();
	    
		Marshaller marshaller = getFragmentMarshaller();
        marshaller.marshal(new JAXBElement<ValidationResultType>(
        		VALIDATION_RESULT_QNAME,
        		ValidationResultType.class, vrt), writer);
        
        writer.writeCharacters("\n");
		writer.writeEndElement(); //end of CrsStatusMessage
		writer.writeCharacters("\n");
		writer.flush();
		
	}
}