package ca.gc.cra.fxit.xmlt.model.wrapper.crs;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.AddressFixType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CrsPaymentTypeEnumType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.CurrCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.MonAmntType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.NameOrganisationType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.NamePersonType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.PaymentType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.crs.PersonPartyType;
import ca.gc.cra.fxit.xmlt.util.Constants;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class WrapperUtils {
	private static Logger lg = Logger.getLogger(WrapperUtils.class); 
    
	public static PersonPartyType.BirthInfo createBirthInfoFromStr(String birthYear, String birthMonth, String birthDay) {
		
    	PersonPartyType.BirthInfo birthInfo = null;
    	
      String birthDateStr = birthYear.trim() + birthMonth.trim()
                   + birthDay.trim();
      if (birthDateStr != null && !birthDateStr.isEmpty()) {
            // parse birth date from input string. Throws ParseException if
            // date cannot be parsed
           
            try {
                   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                   sdf.setLenient(false);
                   sdf.parse(birthDateStr);
            } catch (ParseException ex) {
 
                   birthDateStr = null;
            }                          
      }
      if (birthDateStr != null && !birthDateStr.isEmpty()) {
            String birthDateXMLStr = birthYear + "-" + birthMonth + "-" + birthDay;
            birthInfo = new PersonPartyType.BirthInfo();
            if (birthInfo != null) {
            	try {
            	XMLGregorianCalendar birthDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(birthDateXMLStr); 
    	        //XMLGregorianCalendar xmlCreationTimestamp =  DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp);
                   //birthInfo.setBirthDate(birthDateXMLStr);
            	birthInfo.setBirthDate(birthDate);
            	}
            	catch(Exception e){
            		Utils.logError(lg, e);
            	}
            }
      }
		
		return birthInfo;
    }
    
    public static NamePersonType createNamePersonFromStr (String firstName, String middleName, String lastName) {
		
    	NamePersonType name = new NamePersonType();

		String firstNameStr = firstName.trim();
		String middleNameStr = middleName.trim();
		String lastNameStr = lastName.trim();
		
		// first name is required for schema validation, and can be blank
		NamePersonType.FirstName first = new NamePersonType.FirstName();
		first.setValue(firstNameStr);
		name.setFirstName(first);
		
		// middeName is optional
		if (!middleNameStr.isEmpty()) {
			NamePersonType.MiddleName middle = new NamePersonType.MiddleName();
			middle.setValue(middleNameStr);
			name.getMiddleName().add(middle);
		}
		
		// last name is required for schema validation, and can be blank
		NamePersonType.LastName last = new NamePersonType.LastName();
		last.setValue(lastNameStr);
		name.setLastName(last);

		return name;
    }
    
   public static String createAddressFreeFromStr (String addressLine1Txt, String addressLine2Txt) {
		
    	// Combine Address Line 1 Text and Address Line 2 Text
    	String line1 = addressLine1Txt.trim();
    	String line2 = addressLine2Txt.trim();
		String addressFreeStr = "";
		if (!line1.isEmpty() && !line2.isEmpty())
			addressFreeStr = line1 + " " + line2;
		else if (!line1.isEmpty())
			addressFreeStr = line1;
		else if (!line2.isEmpty())
			addressFreeStr = line2;
	
		return addressFreeStr;
    }
   
   public static AddressFixType createAddressFixFromStr (String postCode, String city, String countrySubEntity) {
		
		AddressFixType addressFix = new AddressFixType();
   	
		// read addressFix content from input 
		String postCodeStr = postCode.trim();
		String cityStr = city.trim();
		String countrySubEntityStr = countrySubEntity.trim();

		// set addressFix content
		if (!postCodeStr.isEmpty()) {
			addressFix.setPostCode(postCodeStr);
		}
		// City is required for schema validation, and can be blank
		addressFix.setCity(cityStr);
		if (!countrySubEntityStr.isEmpty()) {
			addressFix.setCountrySubentity(countrySubEntityStr);
		}

		return addressFix;
   }    

   public static MonAmntType createMonAmntFromStr (
   		String amount,
   		CurrCodeType currCode) throws Exception {
		
   	MonAmntType monAmnt = null;
   	
   	if (amount != null) {
	    	int len = amount.length();
	    	String amountWithDecimal = amount.substring(0, len-Constants.MAX_DECIMAL_PLACES) + "." + amount.substring(len-Constants.MAX_DECIMAL_PLACES);
			// create MonAmnt, and set content of MonAmnt
	    	monAmnt = new MonAmntType();
			BigDecimal value = new BigDecimal(amountWithDecimal);
			monAmnt.setValue(value);
			if (currCode != null) {
				monAmnt.setCurrCode(currCode);
			}
   	}

		return monAmnt;
   }

   /**
    * 
    * @param nameLine1Txt
    * @param nameLine2Txt
    * @return
    */
   public static NameOrganisationType createNameOrganisationFromStr (String nameLine1Txt, String nameLine2Txt) throws Exception {
		
   	NameOrganisationType name = new NameOrganisationType();
   	
   	// Combine Name Line 1 Text and Name Line 2 Text
		String name1 = nameLine1Txt.trim();
		String name2 = nameLine2Txt.trim();
		String nameStr = "";
		if (!name1.isEmpty() && !name2.isEmpty())
			nameStr = name1 + " " + name2;
		else if (!name1.isEmpty())
			nameStr = name1;
		else if (!name2.isEmpty())
			nameStr = name2;
		
		name.setValue(nameStr);

		return name;
   }
}