/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.task.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

/**
 * @author Txs285
 */
public abstract class AbstractXmlHelper implements IHelper
{
	private static final Logger lg = Logger.getLogger(AbstractXmlHelper.class);

	 /**
	 * Validates XML file against the appropriate schema
	 * 
	 * @param PackageInfo p
	 * @return int status code
	 */
	@Override
	public int validate(PackageInfo p, final String[] schemas, String xmlFileLocation) throws Exception {
		 if(schemas==null || schemas.length<=0)
			 throw new Exception("No schema(s) to validate against");
		 
			lg.info("got " + schemas.length + " schemas");
			lg.info("xmlFileLocation: " + xmlFileLocation);
			int status = Constants.STATUS_CODE_INCOMPLETE;		
			FileInputStream xmlfile = null;
			
			try {				
				xmlfile = new FileInputStream(xmlFileLocation);
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);				
				StreamSource[] xsdSources = generateStreamSourcesFromResourceAsStream(schemas);
				
				final Schema schema = factory.newSchema(xsdSources);
				final Validator validator = schema.newValidator();
				validator.validate(new StreamSource(xmlfile));				
				
				status = Constants.STATUS_CODE_SUCCESS;
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
			finally{
				try {
					xmlfile.close();
				}catch(Exception e){}
			}
			
			return status;
	}
	 /**
	  * Creates array of StreamSources comprised of schemas to validate against
	  * 
	  * @param xsdResourcePaths
	  * @return
	  * @throws Exception
	  */
	 private StreamSource[] generateStreamSourcesFromResourceAsStream(final String[] xsdResourcePaths)  throws Exception {  
		 if(xsdResourcePaths==null||xsdResourcePaths.length<=0)
			 return null;
		 String fp = "generateStreamSourcesFromResourceAsStream: ";
		 InputStream is = null;
		 int count = 0;
		 StreamSource[] xsds = new StreamSource[xsdResourcePaths.length];
		 ClassLoader classLoader = AbstractXmlHelper.class.getClassLoader();		
		 
		 //getting schema(s) to validate against	 	
		 for(String s : xsdResourcePaths){
			is = classLoader.getResourceAsStream(s);
			//is =  AbstractXmlHelper.class.getResourceAsStream("/"+s);
			xsds[count] = new StreamSource(is);
			lg.debug(fp + "xsds[" + count + "]: " + xsds[count]);
			count++;
		 }
		 return xsds;
	 } 
}