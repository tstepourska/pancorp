package ca.gc.cra.fxit.xmlt.task.xml.metadata;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ca.gc.cra.fxit.xmlt.task.xml.AbstractXMLStreamWriterAdapter;

public class MetadataWriter extends AbstractXMLStreamWriterAdapter {

	public MetadataWriter(XMLStreamWriter w){
		super(w);
	}
     
 	@Override
 	public void writeNamespace(String prefix, String namespaceURI)
 			throws XMLStreamException {
 		//do NOT block writing namespace URLs in JaxbElements
 		writer.writeNamespace(prefix, namespaceURI);
 	} 	

	@Override
	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		writer.writeDefaultNamespace(namespaceURI);
	}
}