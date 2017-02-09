/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 */
package ca.gc.cra.fxit.xmlt.task.xml;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import ca.gc.cra.fxit.xmlt.model.PackageInfo;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Globals;
import ca.gc.cra.fxit.xmlt.util.Utils;

/**
 * @author Txs285
 */
public abstract class AbstractXmlHelper implements IHelper
{
	private static final Logger lg = Logger.getLogger(AbstractXmlHelper.class);
	
	@Override
	public final int invoke(PackageInfo p){
		lg.info("AbstractXmlHelper started");
		int status = Constants.STATUS_CODE_INCOMPLETE;
		
		try {
			String xmlFilename = this.generateXMLFilename(p);
			p.setXmlFilename(xmlFilename);
			//generate XML
			status = transform(p);
		}
		catch(Exception e){
			Utils.logError(lg, e);
			status = Constants.STATUS_CODE_ERROR;
		}
		//lg.info("Transformation completed with status " + status + ". " + nRecordsProcessed + " records have been processed");	
		//lg.info("numoffis: " + numoffis);
		//lg.info("numofaccreps: " + numofaccreps);
		//lg.info("number of docRefIds: " + docRefIdList.size());
		
		if(status==Constants.STATUS_CODE_SUCCESS){
			//reset status for validation
			status = Constants.STATUS_CODE_INCOMPLETE;
					String outputFile = Globals.FILE_WORKING_DIR+ p.getXmlFilename();
					String[] xsdpaths = getSchemas();
					
					try {
						status = this.validate(p, xsdpaths, outputFile);
						lg.info("Validation completed with status " + status);
					}
					catch(Exception e){
						Utils.logError(lg, e);
						status = Constants.STATUS_CODE_FAILED_SCHEMA_VALIDATION;
					}
		}
		
		//TODO to remove
		status= Constants.STATUS_CODE_SUCCESS;
		return status;
	}
		
	@Override
	public abstract String[] getSchemas();
	        	
	/**
	 * Reads text from the inputStream and transforms it to the international XML format 
	 * writing the document to the outputStream. The input is read and processed in chunks 
	 * in order to be able to handle large flat files.
	 * 
	 * @param PackageInfo p
	 * @return int status code
	 */
	 public abstract int transform(PackageInfo p);	        
	 
	 public void setFileSize(PackageInfo p){
		 String filename = Globals.FILE_WORKING_DIR + p.getOrigFilename();
		 File file = new File(filename);
		 p.setOrigUncompressFileSizeKBQty(BigInteger.valueOf(file.length()));
	 }
	 
	 @Override
	public String generateXMLFilename(PackageInfo p) throws Exception {
		 return Utils.generateXMLFileName(p);
	}

	 /**
	 * Validates XML file against the appropriate schema
	 * 
	 * @param PackageInfo p
	 * @return int status code
	 */
	 @SuppressWarnings("resource")
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
				StreamSource[] xsdSources = generateStreamSourcesFromXsdPaths(schemas);
				
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
		 * Validates XML against schema
		 * During schema validation, capture schema validation errors, and summary of XML file contents.
		 * 
		 * @param inputFileName
		 * @param outputFileName
		 * @param summaryFileName
		 * @param isTransformCompleted
		 * @return
	*/
	private void validateXmlSAX(
			String inputFileName,
			String xmlFileName//, 
			//	String schemaLocation//, 
			//	SchemaValidationErrorHandler errorHandler//,
				//SAXContentHandler contentHandler
				) throws Exception {
		boolean isValidationCompleted = false;
		
		
	    /*
	     * The US FATCA XML schema location in a format that the parser requires consisting of the  
	     * namespace, and the name and location of schema. Note that there is a custom implementation
	     * of the EntityResolver to resolve the location. 
	     */
		String FATCA_SCHEMA_LOCATION = "urn:oecd:ties:fatca:v1 FatcaXML_v1.1.xsd";
		
		String summaryFileName = "XMLErrorReport_" + inputFileName;

		SchemaValidationErrorHandler errorHandler = new SchemaValidationErrorHandler();
		errorHandler.setFlatFileName(inputFileName);
		errorHandler.setXmlFileName(xmlFileName);
		
		SAXContentHandler contentHandler = new SAXContentHandler();
		contentHandler.setInputFileName(inputFileName);
		contentHandler.setXmlFileName(xmlFileName);
		//contentHandler.setPackageRefIdFileName(packageRefIdFileName);
		
		FileInputStream inputStream = null;
		inputStream = new FileInputStream(xmlFileName);

				SAXParserFactory factory = SAXParserFactory.newInstance();

				factory.setNamespaceAware(true);
				factory.setValidating(true);

				SAXParser parser = factory.newSAXParser();

				XMLReader reader = parser.getXMLReader();

				reader.setEntityResolver(new CustomEntityResolver());

				reader.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", FATCA_SCHEMA_LOCATION);
				errorHandler.setSchemaLocation(FATCA_SCHEMA_LOCATION);

				SAXValidationErrorHandler validationErrorHandler = new SAXValidationErrorHandler(errorHandler);
				
				reader.setErrorHandler(validationErrorHandler);
				reader.setContentHandler(contentHandler);

				// set features
				try {
					reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
					reader.setFeature("http://apache.org/xml/features/validation/schema", true);
					reader.setFeature("http://xml.org/sax/features/namespaces", true);
				} catch (SAXNotRecognizedException e) {
					throw new Exception("SAXNotRecognizedException", e);
				} catch (SAXNotSupportedException e) {
					throw new Exception("SAXNotSupportedException", e);
				}

				InputSource input = new InputSource(inputStream);

				// Start parsing
				reader.parse(input);
				isValidationCompleted = true;

				try {
					if (isValidationCompleted || errorHandler.containsErrors()) {
						errorHandler.setValidity();
					} // else validity is still unknown since an exception occurred during validation, but no errors found yet
					
					errorHandler.setErrorReportFileName(summaryFileName);
					errorHandler.saveErrorReport();

				//	contentHandler.setSummaryReportFileName(summaryFileName);
					//contentHandler.saveSummaryReport();

				} catch (IOException e) {
					lg.error("Unable to save summary report: " + e.toString());
				}
		}

	 /** 
	  * Generates array of StreamSource instances representing XSDs 
	  * associated with the file paths/names
	  * 
	  * @param xsdFilesPaths String representations of paths/names  of XSD files. 
	  * @return StreamSource instances representing XSDs. 
	  */  
	 private StreamSource[] generateStreamSourcesFromXsdPaths(final String[] xsdFilesPaths)   {  
		 if(xsdFilesPaths==null||xsdFilesPaths.length<=0)
			 return null;
	
		 int count =0;
		 StreamSource[] xsds = new StreamSource[xsdFilesPaths.length];
	
		 for(String p : xsdFilesPaths){
			 xsds[count] = new StreamSource(p);	
			 count++;
		 }
	
		 return xsds;
	 }
	 
		
		/**
		 * This is a custom implementation of the EntityResolver.
		 * This CustomEntityResolver will fetch the XML Schema files (XSD files) from the bridge jar file.
		 *
		 */
		class CustomEntityResolver implements EntityResolver {
			final String RESOURCE_PATH = "resources/schema/v1.1/";

			@Override
			public InputSource resolveEntity(String publicID, String systemID) throws IOException, SAXException {

				if(systemID == null) {
					return null;
				}

				String newSystemID = null;
				
				if(systemID.indexOf("FatcaXML_v1.1.xsd") != -1) {
					newSystemID = "FatcaXML_v1.1.xsd";
				}
				if(systemID.indexOf("isofatcatypes_v1.0.xsd") != -1) {
					newSystemID = "isofatcatypes_v1.0.xsd";
				}
				if(systemID.indexOf("oecdtypes_v4.1.xsd") != -1) {
					newSystemID = "oecdtypes_v4.1.xsd";
				}
				if(systemID.indexOf("stffatcatypes_v1.1.xsd") != -1) {
					newSystemID = "stffatcatypes_v1.1.xsd";
				}
				
				if(newSystemID != null) {
					InputStream xslInputstream = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH + newSystemID);
					return new InputSource(xslInputstream);
				}
				//else {
					return null; // use the default behaviour
				//}
			}
		};
}