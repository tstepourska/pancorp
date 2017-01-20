package ca.gc.cra.fxit.xmlt.task.xml.metadata;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.task.xml.CustomXMLStreamWriter;
import ca.gc.cra.fxit.xmlt.transformation.jaxb.metadata.CTSSenderFileMetadataType;

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
	
    private final static QName METADATA_QNAME 	= new QName("urn:oecd:ctssenderfilemetadata", "CTSSenderFileMetadata", 		"");	//1
  
    /**
     * Single JAXBContext instance for JaxbMarshaller
     */
    private class JAXBTransformerContext {
        private JAXBContext context = null;

        public JAXBTransformerContext() { }

        public JAXBContext getInstance() throws JAXBException{
            if (context == null) {
            	context = JAXBContext.newInstance(
            			CTSSenderFileMetadataType.class
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

		/*
		 *  xmlns="urn:oecd:ctssenderfilemetadata" 
		 *  xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
		 *  xmlns:xmime="http://www.w3.org/2005/05/xmlmime" 
		 *  xmlns:iso="urn:oecd:ties:isoctstypes:v1" 
		 */
    }
        
	/**
	 * This method will perform the transformation of the input parameters into one XML metadata  
	 *
	 * @param mdt
	 * @param writer
	 * 
	 * @throws Exception
	 */
	public void transformMetadata(CTSSenderFileMetadataType mdt, CustomXMLStreamWriter writer) throws Exception {
		String fp = "transformMetadata: ";
		if(lg.isDebugEnabled())
		lg.debug(fp + "transforming metadata");

		Marshaller marshaller = getFragmentMarshaller();
		JAXBElement<CTSSenderFileMetadataType> el = new JAXBElement<CTSSenderFileMetadataType>(METADATA_QNAME, CTSSenderFileMetadataType.class, mdt);		
		if(lg.isDebugEnabled())
			lg.debug(fp + "created JAXBElement CTSSenderFileMetadataType: " + el);

	    marshaller.marshal(el, writer);
	}
}