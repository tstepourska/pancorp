package ca.gc.cra.fxit.xmlt.model.wrapper.statusmessage;

import java.util.ArrayList;
import java.util.List;

import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ErrorDetailType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.FileErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.LanguageCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ObjectFactory;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.RecordErrorType.FieldsInError;
import ca.gc.cra.fxit.xmlt.generated.jaxb.statusmessage.ValidationErrorsType;

public class __ValidationErrorsWrapper extends ValidationErrorsType {
	public List<FileErrorType> createFileErrorTypeList(){
		List<FileErrorType> list = new ArrayList<>();
		ObjectFactory factory = new ObjectFactory();
		String code = null;
		String detail = null;
		//TODO extract codes and details
		
		LanguageCodeType lg = LanguageCodeType.EN;
		FileErrorType err = createFileErrorType(factory,code,detail, lg);
		if(err!=null)
			list.add(err);
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	public FileErrorType createFileErrorType(ObjectFactory f, String c, String d, LanguageCodeType lang){
		if(c==null)
			return null;
		
		FileErrorType err = f.createFileErrorType();
		err.setCode(c);
		ErrorDetailType detail = f.createErrorDetailType();
		if(d!=null)
			detail.setValue(d);
		detail.setLanguage(lang);
		
		err.setDetails(detail);
		
		return err;
	}
	
	public List<RecordErrorType> createRecordErrorTypeList(){
		List<RecordErrorType> list = new ArrayList<>();
		ObjectFactory factory = new ObjectFactory();
		String code = null;
		String detail = null;
		//TODO extract codes and details
		
		LanguageCodeType lg = LanguageCodeType.EN;
		RecordErrorType err = createRecordErrorType(factory,code,detail, lg);
		if(err!=null)
			list.add(err);
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	public RecordErrorType createRecordErrorType(ObjectFactory f, String c, String d, LanguageCodeType lang){
		if(c==null)
			return null;
		
		RecordErrorType err = f.createRecordErrorType();
		err.setCode(c);
		ErrorDetailType detail = f.createErrorDetailType();
		detail.setValue(d);
		detail.setLanguage(lang);
	
		err.setDetails(detail);
		
		//TODO
		List<String> docRefIDInErrList = createDocRefIDInErrorList();
		if(docRefIDInErrList!=null)
		err.getDocRefIDInError().addAll(docRefIDInErrList);
		
		List<FieldsInError> fieldsInErrList = createFieldsInErrorList();
		
		if(fieldsInErrList!=null)
		err.getFieldsInError().addAll(fieldsInErrList);
		
		return err;
	}
	
	public List<String> createDocRefIDInErrorList(){
		List<String> list = new ArrayList<>();
		//TODO extract docRefIds
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
	
	public List<FieldsInError> createFieldsInErrorList(){
		List<FieldsInError> list = new ArrayList<>();
		//TODO extract fields in error
		
		if(list.isEmpty())
			return null;
		
		return list;
	}
}
