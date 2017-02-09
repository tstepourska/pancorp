package ca.gc.cra.fxit.xmlt.task.xml.metadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

//import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.BinaryEncodingSchemeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSSenderFileMetadataType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CountryCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.FileFormatCdType;

/**
 * JaxbMarshaller class is used to transform large flat files into International CRS XML format.
 * JaxbMarshaller reads and transforms records one by one and appends the resulting XML to an output CRS XML file.
 * Characters &<>"' are escaped where necessary in XML element, and attribute values by JAXB and StAX.
 * 
 */
public class JaxbMarshaller {
	//private static Logger lg = Logger.getLogger(JaxbMarshaller.class);
	private JAXBTransformerContext transformerContext = new JAXBTransformerContext();		
	private Marshaller fragmentMarshaller = null;
	
    private static final QName METADATA_QNAME 				= new QName("urn:oecd:ctssenderfilemetadata", "CTSSenderFileMetadata"		, "");	//1
 /*
   private static final QName SENDER_COUNTRY_QNAME 		= new QName("urn:oecd:ctssenderfilemetadata", "CTSSenderCountryCd"			, "");	
    private static final QName RECEIVER_COUNTRY_QNAME 		= new QName("urn:oecd:ctssenderfilemetadata", "CTSReceiverCountryCd"		, "");	
    private static final QName COMMUNICATION_TYPE_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "CTSCommunicationTypeCd"		, "");	
    private static final QName SENDER_FILE_ID_QNAME			= new QName("urn:oecd:ctssenderfilemetadata", "SenderFileId"				, "");
    private static final QName FORMAT_CD_TYPE_QNAME			= new QName("urn:oecd:ctssenderfilemetadata", "FileFormatCd"				, "");
    private static final QName BINARY_ENCODING_CD_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "BinaryEncodingSchemeCd"		, "");
    private static final QName FILE_CREATE_TIMESTAMP_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "FileCreateTs"				, "");
    private static final QName TAX_YEAR_QNAME 				= new QName("urn:oecd:ctssenderfilemetadata", "TaxYear"						, "");
    private static final QName FILE_REVISION_INDIC_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "FileRevisionInd"				, "");
    private static final QName ORIG_CTS_TRANS_ID_QNAME		= new QName("urn:oecd:ctssenderfilemetadata", "OriginalCTSTransmissionId"	, "");
    private static final QName CONTACT_EMAIL_ADDRESS_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "SenderContactEmailAddressTxt", "");
    */
  
    /**
     * Single JAXBContext instance for JaxbMarshaller
     */
    private class JAXBTransformerContext {
        private JAXBContext context = null;

        public JAXBTransformerContext() { }

        public JAXBContext getInstance() throws JAXBException{
            if (context == null) {
            	context = JAXBContext.newInstance(
            			CTSSenderFileMetadataType.class,
            			CountryCodeType.class,
            			CTSCommunicationTypeCdType.class,
            			String.class,
            			FileFormatCdType.class,
            			BinaryEncodingSchemeCdType.class,
            			XMLGregorianCalendar.class
            			);
            }
            return context;
        }
    }
    
    /**
     * Returns JaxbMarshaller object that will be used to convert a java content tree into XML data.
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
    
   // public void startDocument(CommonXMLStreamWriter writer) throws Exception {
    public void startDocument(MetadataWriter writer) throws Exception {
    	writer.writeStartDocument	("UTF-8","1.0");
    	writer.writeCharacters		("\n");
    	
    	//writer.writeStartElement	("", "CTSSenderFileMetadata", "urn:oecd:ctssenderfilemetadata");	
		//writer.writeAttribute		("version", "1.0");

		/*
		 *  xmlns="urn:oecd:ctssenderfilemetadata" 
		 *  xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
		 *  xmlns:xmime="http://www.w3.org/2005/05/xmlmime" 
		 *  xmlns:iso="urn:oecd:ties:isoctstypes:v1" 
		 */
    }
  /*  

    public void transformSenderCountry(CountryCodeType country, CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<CountryCodeType> el = new JAXBElement<CountryCodeType>(SENDER_COUNTRY_QNAME, CountryCodeType.class, country);			
		getFragmentMarshaller().marshal(el, writer);
		writer.writeCharacters		("\n");
    }
    
    public void transformReceiverCountry(CountryCodeType country, CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<CountryCodeType> el = new JAXBElement<CountryCodeType>(RECEIVER_COUNTRY_QNAME, CountryCodeType.class, country);		
		getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformCommunicationType(CTSCommunicationTypeCdType comtype, CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<CTSCommunicationTypeCdType> el = new JAXBElement<CTSCommunicationTypeCdType>(COMMUNICATION_TYPE_QNAME, CTSCommunicationTypeCdType.class, comtype);		
		getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformSenderFileId(String senderFileId, CommonXMLStreamWriter writer) throws Exception {
    	JAXBElement<String> el = new JAXBElement<String>(SENDER_FILE_ID_QNAME, String.class, senderFileId);		
    	getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformFileFormatCd(FileFormatCdType ft,CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<FileFormatCdType> el = new JAXBElement<FileFormatCdType>(FORMAT_CD_TYPE_QNAME, FileFormatCdType.class, ft);		
		getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType t,CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<BinaryEncodingSchemeCdType> el = new JAXBElement<BinaryEncodingSchemeCdType>(BINARY_ENCODING_CD_QNAME, BinaryEncodingSchemeCdType.class, t);		
		getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformFileCreateTs(String ts, CommonXMLStreamWriter writer) throws Exception {
    	JAXBElement<String> el = new JAXBElement<String>(FILE_CREATE_TIMESTAMP_QNAME, String.class, ts);		
    	getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformTaxYear(XMLGregorianCalendar cal,CommonXMLStreamWriter writer) throws Exception {
		JAXBElement<XMLGregorianCalendar> el = new JAXBElement<XMLGregorianCalendar>(TAX_YEAR_QNAME,XMLGregorianCalendar.class, cal);		
		getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformFileRevisionInd(String ind,CommonXMLStreamWriter writer) throws Exception {
    	JAXBElement<String> el = new JAXBElement<String>(FILE_REVISION_INDIC_QNAME, String.class, ind);		
    	getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformOriginalCTSTransmissionId(String id, CommonXMLStreamWriter writer) throws Exception {
    	JAXBElement<String> el = new JAXBElement<String>(ORIG_CTS_TRANS_ID_QNAME, String.class, id);		
    	getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
    
    public void transformSenderContactEmailAddressTxt(String email, CommonXMLStreamWriter writer) throws Exception {
    	JAXBElement<String> el = new JAXBElement<String>(CONTACT_EMAIL_ADDRESS_QNAME, String.class, email);		
    	getFragmentMarshaller().marshal(el, writer);
	    writer.writeCharacters		("\n");
    }
*/        
	/**
	 * This method will perform the transformation of the input parameters into one XML metadata  
	 *
	 * @param mdt
	 * @param writer
	 * 
	 * @throws Exception
	 */
	//public void transformMetadata(CTSSenderFileMetadataType mdt, CommonXMLStreamWriter writer) throws Exception {
	public void transformMetadata(CTSSenderFileMetadataType mdt, MetadataWriter writer) throws Exception {
		JAXBElement<CTSSenderFileMetadataType> el = new JAXBElement<CTSSenderFileMetadataType>(METADATA_QNAME, CTSSenderFileMetadataType.class, mdt);		
		getFragmentMarshaller().marshal(el, writer);
	}
}