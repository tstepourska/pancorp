package ca.gc.cra.fxit.xmlt.task.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXParseException;

public class SchemaValidationErrorHandler {

	/**
	 * Limit the number of schema validation errors that are retained for logging to a file. Value {@value}.
	 */
	private static final int MAX_ERRORS = 10000;
	public enum SchemaValidationResultType {
		VALID,
	    INVALID,
	    UNKNOWN
	};
	private String schemaLocation;
	private String errorReportFileName;
	private String flatFileName;
	private String xmlFileName;
	private SchemaValidationResultType validity = SchemaValidationResultType.UNKNOWN; 
	
	List<String> errors = new ArrayList<String>();
	
	public void addError(SAXParseException exception) {
		errors.add(formatError(exception));
	}

	public void addError(String message) {
		errors.add(message);
	}
	
	public int getMaxErrors() {
		return MAX_ERRORS;
	}
	
	public boolean isMaxErrorsReached() {
		if(errors.size() >= MAX_ERRORS) {
			return true;
		}
		return false;
	}
	
	public boolean containsErrors() {
		if(errors.size() > 0) {
			return true;
		}
		return false;
	}
	
	
	private String formatError(SAXParseException e) {
		return "Line: " + e.getLineNumber() + " Column: " + e.getColumnNumber() + " " + e.getMessage();
	}
	
	public List<String> getErrors() {
		return Collections.unmodifiableList(errors);
	}
	
	public void setValidity () {
		validity = SchemaValidationResultType.UNKNOWN;
		if (containsErrors()) {
			validity = SchemaValidationResultType.INVALID;
		}
		else {
			validity = SchemaValidationResultType.VALID;
		}
	}
	
	public SchemaValidationResultType getValidity () {
		return validity;
	}
	
	public void cleanErrorList() {
		errors.clear();
		errors = null;
		validity = SchemaValidationResultType.UNKNOWN;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	
	public String getErrorReportFileName() {
		return errorReportFileName;
	}

	public void setErrorReportFileName(String errorReportFileName) {
		this.errorReportFileName = errorReportFileName;
	}

	public String getFlatFileName() {
		return flatFileName;
	}

	public void setFlatFileName(String flatFileName) {
		this.flatFileName = flatFileName;
	}	
	
	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}	
	
	/**
	 * Save the list of schema validation errors to a file.
	 */
	public void saveErrorReport(
			//boolean bIsFirstIndex,
			//boolean bIsLastIndex
			) throws IOException {

		List<String> validationErrors = new ArrayList<String>(getErrors());
		
		//boolean append = !(bIsFirstIndex);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(getErrorReportFileName(), false));//append));
		writer.newLine();
		
		writer.write("Flat File                : " + getFlatFileName());
		writer.newLine();
		writer.newLine();
		writer.write("XML SCHEMA VALIDATION");
		writer.newLine();
		writer.write("FATCA Schema             : " + schemaLocation);
		writer.newLine();
		writer.write("FATCA XML File           : " + getXmlFileName());
		writer.newLine();
		writer.write("Schema validation result : " + validity.toString());
		writer.newLine();
		if (containsErrors()) {
			writer.write(" Schema validation errors for file : " + getXmlFileName());
			writer.newLine();
			writer.newLine();
		} 
		Iterator<String> it = validationErrors.iterator();
		while (it.hasNext()) {
			String validationError = it.next();
			writer.write(validationError);
			writer.newLine();
		}
		writer.close();

		validationErrors = null;
	}
	
}
