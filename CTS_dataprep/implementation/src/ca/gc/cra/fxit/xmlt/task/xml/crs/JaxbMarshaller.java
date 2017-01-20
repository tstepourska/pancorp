package ca.gc.cra.fxit.xmlt.task.xml.crs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.task.xml.CustomXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.crs.*;
import ca.gc.cra.fxit.xmlt.transformation.wrapper.crs.FIWrapper;

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
	
	//ROOT:  CRS_OECD  attributes  version="1.0"
    private final static QName MESSAGE_SPEC_QNAME 	= new QName("urn:oecd:ties:crs:v1", "MessageSpec", 		"crs");	//1
   // private final static QName CRS_BODY_QNAME 		= new QName("urn:oecd:ties:crs:v1", "CrsBody", 			"crs");	//1...oo
    private final static QName REPORTING_FI_QNAME 	= new QName("urn:oecd:ties:crs:v1", "ReportingFI", 		"crs");	//1 per CrsBody
   // private final static QName REPORTING_GROUP_QNAME= new QName("urn:oecd:ties:crs:v1", "ReportingGroup", 	"crs");	//1 per CrsBody
    private final static QName ACCOUNT_REPORT_QNAME = new QName("urn:oecd:ties:crs:v1", "AccountReport", 	"crs");	//0...oo per CrsBody	   
    //Sponsor, Intermediary and Pool Reporting are not used for CRS - as per CRS Schema User Guide
    //private final static QName SPONSOR_QNAME 		= new QName("urn:oecd:ties:crs:v1", "Sponsor", 			"crs");		// not used for CRS

    /**
     * Single JAXBContext instance for JaxbMarshaller
     */
    private class JAXBTransformerContext {
        private JAXBContext context = null;

        public JAXBTransformerContext() { }

        public JAXBContext getInstance() throws JAXBException{
            if (context == null) {
            	context = JAXBContext.newInstance(
            			CorrectableOrganisationPartyType.class, 
            			MessageSpecType.class,
            			CorrectableAccountReportType.class,
            			CrsBodyType.class
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
    
    public void startDocument(MessageSpecType messageSpec, CustomXMLStreamWriter writer) throws Exception {
    	writer.writeStartDocument	("UTF-8","1.0");
    	writer.writeCharacters		("\n");
		writer.writeStartElement	("crs", "CRS_OECD", "urn:oecd:ties:crs:v1");	
		writer.writeAttribute		("version", "1.1");

		/*
		 * xmlns:crs="urn:oecd:ties:crs:v1" 
			xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
			xmlns:ftc="urn:oecd:ties:fatca:v1" 
			xmlns:cfc="urn:oecd:ties:commontypesfatcacrs:v1" 
			xmlns:stf="urn:oecd:ties:stf:v4" 
			xmlns:iso="urn:oecd:ties:isocrstypes:v1" 
			targetNamespace="urn:oecd:ties:crs:v1" 
		 */
		// TODO: IRS advised to avoid using default namespaces, and use namespace prefixes for all elements instead.
		// This guidance is temporary, and the restriction will be lifted. 
		//writer.writeDefaultNamespace("urn:oecd:ties:crs:v1");

		writer.writeCustomNamespace		("crs", "urn:oecd:ties:crs:v1");
		writer.writeCustomNamespace		("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeCustomNamespace		("sfa", "urn:oecd:ties:stffatcatypes:v1");
		//crs from schema
		writer.writeCustomNamespace		("ftc", "urn:oecd:ties:fatca:v1");
		writer.writeCustomNamespace		("cfc","urn:oecd:ties:commontypesfatcacrs:v1"); 
		writer.writeCustomNamespace		("stf","urn:oecd:ties:stf:v4");
		writer.writeCustomNamespace		("iso","urn:oecd:ties:isocrstypes:v1");
		
		writer.writeCharacters		("\n");
		
		//transformMessageSpec(messageSpec, writer);
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

		//writer.setPrefix("crs", "urn:oecd:ties:crs:v1");//does nothing
	    marshaller.marshal(el, writer);
	    
	    writer.writeCharacters		("\n");
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
	public void transformAccountReport(
		CorrectableAccountReportType report, boolean isFirst,
		CustomXMLStreamWriter writer) throws Exception {
		
			Marshaller marshaller = getFragmentMarshaller();
	        marshaller.marshal(new JAXBElement<CorrectableAccountReportType>(
	        						ACCOUNT_REPORT_QNAME,
	        						CorrectableAccountReportType.class, report),  writer);
	        
	        writer.writeCharacters		("\n");
	        writer.flush();
	}
	
	/**
	 * This method will perform the transformation of one SUMMARY record into one XML ReportingFI.
	 * 
	 * @param reportingFIRec	input is read from this record
	 * @param writer			the output is written to this writer
	 * @throws BridgeException
	 */
	public void transformReportingFI(
			CorrectableOrganisationPartyType party, boolean isFirst,
			CustomXMLStreamWriter writer) throws Exception {

		if(!isFirst){
			writer.writeEndElement(); //end of crs:ReportingGroup after last crs:AccountReport in crs:ReportingFI
			writer.writeCharacters("\n");
			writer.writeEndElement(); //end of CrsBody
			writer.writeCharacters("\n");
			writer.flush();
		}
		
	    writer.writeStartElement("crs", "crsBody", null);//namespace uri "urn:oecd:ties:crs:v1"
	    writer.writeCharacters		("\n");
	    writer.flush();		
	    
		Marshaller marshaller = getFragmentMarshaller();
        marshaller.marshal(new JAXBElement<CorrectableOrganisationPartyType>(
        		REPORTING_FI_QNAME,
        		CorrectableOrganisationPartyType.class, party), writer);
        writer.writeCharacters("\n");
        writer.flush();

        //write the start of crs:ReportingGroup element - for CRS only 1(one) group per CrsBody
		writer.writeStartElement("crs", "ReportingGroup", null);//namespaceURI			
		writer.writeCharacters		("\n");
		writer.flush();
	}
	
/*	public void transformTrailer(CustomXMLStreamWriter writer) throws Exception {
		writer.writeEndElement();
	}*/
}