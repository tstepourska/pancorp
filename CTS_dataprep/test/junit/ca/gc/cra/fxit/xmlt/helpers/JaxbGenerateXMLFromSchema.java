package ca.gc.cra.fxit.xmlt.helpers;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.BinaryEncodingSchemeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSCommunicationTypeCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CTSSenderFileMetadataType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.CountryCodeType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.FileFormatCdType;
import ca.gc.cra.fxit.xmlt.generated.jaxb.metadata.ObjectFactory;
import ca.gc.cra.fxit.xmlt.util.Utils;

public class JaxbGenerateXMLFromSchema {
	private static Logger lg = Logger.getLogger(JaxbGenerateXMLFromSchema.class);
	
	public static void main(String[] args)// throws JAXBException, Exception
    {
 
		try {
        ObjectFactory factory = new ObjectFactory();
 
        CTSSenderFileMetadataType mdt = factory.createCTSSenderFileMetadataType();
        mdt.setCTSSenderCountryCd(CountryCodeType.CA);
        mdt.setCTSReceiverCountryCd(CountryCodeType.GB);
        mdt.setCTSCommunicationTypeCd(CTSCommunicationTypeCdType.CRS);
        mdt.setSenderFileId("CA2016GB1234567");
        mdt.setFileFormatCd(FileFormatCdType.XML);
        mdt.setBinaryEncodingSchemeCd(BinaryEncodingSchemeCdType.NONE);
        
       /* XMLGregorianCalendar cal = Utils.generateXMLTimestamp(System.currentTimeMillis());
        int year = cal.getYear();
        int mon = cal.getMonth();
        int day = cal.getDay();
        int hr = cal.getHour();
        int min = cal.getMinute();
        int sec = cal.getSecond();
        String ts = "" + year +"-"+mon+"-"+day+"T"+hr+"-"+min+"-"+sec;*/
        mdt.setFileCreateTs(Utils.toUTC(new Date(System.currentTimeMillis())));
        
        mdt.setTaxYear(Utils.generateReportingPeriod("2016", null, null));
        
        mdt.setFileRevisionInd(false);
        mdt.setOriginalCTSTransmissionId("");
        mdt.setSenderContactEmailAddressTxt("casd@cra-arc.gc.ca");
 
        //JAXBContext context = JAXBContext.newInstance("generated");
        JAXBContext  context = JAXBContext.newInstance(CTSSenderFileMetadataType.class);
        
       // JAXBElement<ExpenseT> element = factory.createExpenseReport(expense);
        JAXBElement <CTSSenderFileMetadataType> element = factory.createCTSSenderFileMetadata(mdt);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
        marshaller.marshal(element,System.out);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
    }

}
