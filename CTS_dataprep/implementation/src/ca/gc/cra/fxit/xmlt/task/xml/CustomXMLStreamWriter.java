package ca.gc.cra.fxit.xmlt.task.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


/**
 * This class replaces combinations of characters that are prohibited in FATCA XML data values with blank spaces.
 * Double dash (--), slash asterisk (/*), and ampersand hash (&#) are each replaced with two blank spaces. 
 * Each of these characters are replaced with a blank space: < > & " ' #.
 *  
 * Replacing characters that are not allowed in XML with their entity references is done automatically by XMLStreamWriter.
 * 
 *  As of August 2015, these are allowed &lt, &gt, >, &amp, &quot, ", &apos, ', and #, and these are not allowed --, /*, &#, <, &.  
 *  Prior to August 2015 each of these characters were prohibited by IRS in FATCA data values: < > & " ' #.
 *  Data values in voided account reports should exactly match what was sent in the referenced original, or amended version; 
 *  thus, no change has been made to the character substitution that was in place for the Sept 2015 file transfer.
 */
public class CustomXMLStreamWriter implements XMLStreamWriter {

	private final XMLStreamWriter writer;
	
    public CustomXMLStreamWriter(XMLStreamWriter writer) {

        this.writer = writer;
    }
    
	private String escapedValue(String value)
    {
        StringBuilder  buf = new StringBuilder();
		int i = 0;
        int len = value.length();
        do {
            int start = i;
            char c = '\u0000';

            for (; i < len; ++i) {
                c = value.charAt(i);
                if (c == '<' || c == '>' || c == '&' || c == '"' || c == '\'' || c == '#'  || c == '-' || c == '/') {
                    break;
                }
            }
            int outLen = i - start;
            if (outLen > 0) {
                buf.append(value, start, i);
            }
            if (i < len) {
            	if (c == '<' || c == '>' || c == '&' || c == '"' || c == '\'' || c == '#') {
                	buf.append(" "); 
            	} else if (c == '-') {
            		if (i+1 < len && value.charAt(i+1) == '-') {
            			++i; 
            			// DOUBLE DASH is prohibited in FATCA XML; write two blank space characters instead
            			buf.append("  "); 
            		}
            		else {
	                    buf.append("-");
            		}
            	} else if (c == '/') {
            		if (i+1 < len && value.charAt(i+1) == '*') {
            			++i; 
            			// SLASH ASTERISK is prohibited in FATCA XML; write two blank space characters instead
            			buf.append("  "); 
            		}
            		else {
	                    buf.append("/");
            		}
	            }
        	}
        } while (++i < len);
        
        return buf.toString();
    }
	
	@Override
	public void close() throws XMLStreamException {
		writer.close();
	}

	@Override
	public void flush() throws XMLStreamException {
		writer.flush();
	}

	@Override
	public NamespaceContext getNamespaceContext() {
		return writer.getNamespaceContext();
	}

	@Override
	public String getPrefix(String uri) throws XMLStreamException {
		return writer.getPrefix(uri);
	}

	@Override
	public Object getProperty(String name) throws IllegalArgumentException {
		return writer.getProperty(name);
	}

	@Override
	public void setDefaultNamespace(String uri) throws XMLStreamException {
		writer.setDefaultNamespace(uri);
	}

	@Override
	public void setNamespaceContext(NamespaceContext context)
			throws XMLStreamException {
		writer.setNamespaceContext(context);
		
	}

	@Override
	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		writer.setPrefix(prefix, uri);
	}

	@Override
	public void writeAttribute(String localName, String value)
			throws XMLStreamException {
		writer.writeAttribute(localName, escapedValue(value));
		
	}

	@Override
	public void writeAttribute(String namespaceURI, String localName, String value)
			throws XMLStreamException {
		writer.writeAttribute(namespaceURI, localName, escapedValue(value));
	}

	@Override
	public void writeAttribute(String prefix, String namespaceURI, String localName,
			String value) throws XMLStreamException {
		writer.writeAttribute(prefix, namespaceURI, localName, escapedValue(value));
	}

	@Override
	public void writeCData(String data) throws XMLStreamException {
		writer.writeCData(escapedValue(data));
	}

	@Override
	public void writeCharacters(String text) throws XMLStreamException {
		writer.writeCharacters(escapedValue(text));
	}

	@Override
	public void writeCharacters(char[] text, int start, int len)
			throws XMLStreamException {
		writer.writeCharacters(escapedValue(new String(text, start, len)));
	}

	@Override
	public void writeComment(String data) throws XMLStreamException {
		writer.writeComment(data);
	}

	@Override
	public void writeDTD(String dtd) throws XMLStreamException {
		writer.writeDTD(dtd);
	}

	/**
	 * Does nothing intentionally
	 */
	@Override
	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		//writer.writeDefaultNamespace(namespaceURI);
	}

	@Override
	public void writeEmptyElement(String localName) throws XMLStreamException {
		writer.writeEmptyElement(localName);
	}

	@Override
	public void writeEmptyElement(String namespaceURI, String localName)
			throws XMLStreamException {
		writer.writeEmptyElement(namespaceURI, localName);
	}

	@Override
	public void writeEmptyElement(String prefix, String localName, String namespaceURI)
			throws XMLStreamException {
		writer.writeEmptyElement(prefix, localName, namespaceURI);
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		writer.writeEndDocument();
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		writer.writeEndElement();
	}

	@Override
	public void writeEntityRef(String name) throws XMLStreamException {
		writer.writeEntityRef(name);
	}

	/**
	 * Intentionally does nothing, to block writing namespace URLs 
	 * in Jaxb elements, use writeCustomNamespace instead
	 */
	@Override
	public void writeNamespace(String prefix, String namespaceURI)
			throws XMLStreamException {
		//block writing namespace URLs in JaxbElements
		//writer.writeNamespace(prefix, namespaceURI);
	}
	
	/**
	 * Writes namespace URL in the Jaxb element.
	 * 
	 * @param prefix
	 * @param namespaceURI
	 * @throws XMLStreamException
	 */
	public void writeCustomNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		writer.writeNamespace(prefix, namespaceURI);
	}

	@Override
	public void writeProcessingInstruction(String target)
			throws XMLStreamException {
		writer.writeProcessingInstruction(target);
	}

	@Override
	public void writeProcessingInstruction(String target, String data)
			throws XMLStreamException {
		writer.writeProcessingInstruction(target, data);
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		writer.writeStartDocument();
	}

	@Override
	public void writeStartDocument(String version) throws XMLStreamException {
		writer.writeStartDocument(version);
	}

	@Override
	public void writeStartDocument(String encoding, String version)
			throws XMLStreamException {
		writer.writeStartDocument(encoding, version);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		writer.writeStartElement(localName);
	}

	@Override
	public void writeStartElement(String encoding, String version)
			throws XMLStreamException {
		writer.writeStartDocument(encoding, version);
		
	}

	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI)
			throws XMLStreamException {
		writer.writeStartElement(prefix, localName, namespaceURI);
	}
	
}