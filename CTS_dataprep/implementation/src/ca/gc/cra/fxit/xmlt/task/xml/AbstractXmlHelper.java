/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.xmlt.task.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.AppProperties;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

/**
 *
 * 
 * @author Txs285
 */
public abstract class AbstractXmlHelper implements IHelper
{

	private static final Logger lg = Logger.getLogger(AbstractXmlHelper.class);
	        	
	/**
	 * Reads text from the inputStream and transforms it to the international XML format 
	 * writing the document to the outputStream. The input is read and processed in chunks 
	 * in order to be able to handle large flat files.
	 * 
	 * @param PackageInfo p
	 * @return int status code
	 */
	 public abstract int transform(PackageInfo p);	        

	 /**
	 * Validates XML file against the appropriate schema
	 * 
	 * @param PackageInfo p
	 * @return int status code
	 */
	 public int validate(PackageInfo p, String schemaLocation, String xmlFileLocation){
		  // String dataProvider = p.getDataProviderPrefix();
			lg.info("schemaLocation: " + schemaLocation);
			lg.info("xmlFileLocation: " + xmlFileLocation);
			int status = Constants.STATUS_CODE_INCOMPLETE;
			
			try {
				status = validateAgainstXSD(xmlFileLocation, schemaLocation);
				//TODO any other validation needed?
			} 
			catch(NullPointerException e){
				status = Constants.STATUS_CODE_ERROR;
				Utils.logError(lg, e);
			}
			catch(SAXException e){
				status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
				Utils.logError(lg, e);
			}
			catch (IOException e) {
				status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
				Utils.logError(lg, e);
			}
			catch (Exception e) {
				status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
				Utils.logError(lg, e);
			}
			return status;
	   }
	   
	 /**
	 * Validates XML file against the appropriate schema
	 * 
	 * @param PackageInfo p
	 * 
	 * @return int status code
	 * 
	 * @throws SAXException, IOException, NullPointerException,Exception
	 */
	private int validateAgainstXSD(String xmlDataFileLocation, String schemaLocation) throws SAXException, IOException, NullPointerException,Exception {
		   
		InputStream xml = null;
		InputStream xsd = null;

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = factory.newSchema(new StreamSource(xsd));
	    Validator validator = schema.newValidator();
	    validator.validate(new StreamSource(xml));
	        
	    return Constants.STATUS_CODE_SUCCESS;
	}
}
