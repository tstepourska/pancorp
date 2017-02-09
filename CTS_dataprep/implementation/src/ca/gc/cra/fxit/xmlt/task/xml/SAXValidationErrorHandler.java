package ca.gc.cra.fxit.xmlt.task.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * SAX Validation error handler
 */
public class SAXValidationErrorHandler implements ErrorHandler {

	private SchemaValidationErrorHandler errorHandler;

	
	public SAXValidationErrorHandler(SchemaValidationErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	
	/**
	 * A ValidationError is added to the collection of errors in the SchemaValidationErrorHandler. 
	 * If the maximum number of errors is reached, a SAXException is thrown.
	 *  
	 */
	@Override
	public void error(SAXParseException exception) throws SAXException {
		
		errorHandler.addError(exception);	
		
		if (errorHandler.isMaxErrorsReached()) {
			errorHandler.addError("Maximum number of errors reached " + errorHandler.getMaxErrors());
			throw new SAXException("Maximum number of errors reached " + errorHandler.getMaxErrors());
		}
		
	}

	/**
	 * A fatal error normally stops the validation (throws a SAXException)
	 */
	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		
		errorHandler.addError(exception);
	}

	@Override
	public void warning(SAXParseException exception) {

	}
}
