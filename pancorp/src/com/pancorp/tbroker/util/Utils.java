package com.pancorp.tbroker.util;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;

public class Utils {
	private static Logger log = LogManager.getLogger(Utils.class);
	
	public static void logError(Logger lg, Exception e){
		lg.error("Error: " + e.getMessage());
  	  	if(lg.isDebugEnabled()){
  		  StackTraceElement[] trace = e.getStackTrace();
  		  if(trace!=null){
  			  for(StackTraceElement tLine : trace){
  				  lg.error(tLine);
  			  }
  		  }
  	  	}
	}
	
	public static void logError(Logger lg, String s){
		lg.error("Error: " + s);
	}
	
	public static double truncateDouble(double value, double numOfDecimals){
		//double result = (double)Math.round(value * 100000d) / 100000d	//5-digits decimals
		double result = (double)Math.round(value * numOfDecimals) / numOfDecimals;
		return result;
	}
	
	public static GregorianCalendar createEndOfRTH(){
		 // get the supported ids for GMT-08:00 (Pacific Standard Time)
		 String[] ids = TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000);
		 // if no ids were returned, something is wrong. get out.
		 if (ids.length == 0)
		     return null;

		  // begin output
		 //System.out.println("Current Time");

		 // create a Pacific Standard Time time zone
		 SimpleTimeZone edt = new SimpleTimeZone(-5 * 60 * 60 * 1000, ids[0]);

		 // set up rules for Daylight Saving Time
		 edt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		 edt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

		 // create a GregorianCalendar with the Pacific Daylight time zone
		 // and the current date and time
		 GregorianCalendar calendar = new GregorianCalendar(edt);
		 String[] arr = Constants.endRTH.split(":");
		 calendar.set(Calendar.AM_PM, Calendar.PM);
		 calendar.set(Calendar.HOUR, Integer.parseInt(arr[0]));
		 calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(arr[0]));
		 calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
		 
		 if(log.isTraceEnabled()){
			 log.trace("AM_PM: " + calendar.get(Calendar.AM_PM));
			 log.trace("HOUR: " + calendar.get(Calendar.HOUR));
			 log.trace("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
			 log.trace("MINUTE: " + calendar.get(Calendar.MINUTE));
		 }
		 
		 return calendar;
	}
	
	public static GregorianCalendar createStartOfRTH(){
		 // get the supported ids for GMT-08:00 (Pacific Standard Time)
		 String[] ids = TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000);
		 // if no ids were returned, something is wrong. get out.
		 if (ids.length == 0)
		     return null;

		  // begin output
		 //System.out.println("Current Time");

		 // create a Pacific Standard Time time zone
		 SimpleTimeZone edt = new SimpleTimeZone(-5 * 60 * 60 * 1000, ids[0]);

		 // set up rules for Daylight Saving Time
		 edt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		 edt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

		 // create a GregorianCalendar with the Pacific Daylight time zone
		 // and the current date and time
		 GregorianCalendar calendar = new GregorianCalendar(edt);
		 String[] arr = Constants.startRTH.split(":");
		 calendar.set(Calendar.AM_PM, Calendar.PM);
		 calendar.set(Calendar.HOUR, Integer.parseInt(arr[0]));
		 calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(arr[0]));
		 calendar.set(Calendar.MINUTE, Integer.parseInt(arr[1]));
		 
		 if(log.isTraceEnabled()){
			 log.trace("AM_PM: " + calendar.get(Calendar.AM_PM));
			 log.trace("HOUR: " + calendar.get(Calendar.HOUR));
			 log.trace("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
			 log.trace("MINUTE: " + calendar.get(Calendar.MINUTE));
		 }
		 
		 return calendar;
	}
	
	public static void resetCalendars(GregorianCalendar begin, GregorianCalendar end, GregorianCalendar now){
		begin.set(Calendar.YEAR, now.get(Calendar.YEAR));
		begin.set(Calendar.MONTH, now.get(Calendar.MONTH));
		begin.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
		
		end.set(Calendar.YEAR, now.get(Calendar.YEAR));
		end.set(Calendar.MONTH, now.get(Calendar.MONTH));
		end.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Fills up the testing queue
	 * @return
	 */
	public static ArrayDeque<Bar> fillTickCache(long queueSize,String timeframeUnit, int timeframeSize, double basePrice){
		ArrayDeque<Bar> q = new ArrayDeque<>();
		double tfFactor = 0;
       // double basePrice= 28.7;
       // int smaSize=  30;
        Bar b;
        double dev = 0;
        boolean positive = true;
        int volume = 300000;
        int cnt= 10;
        //long queueSize = 72;	//2 days of 10-min bars
       // String timeframeUnit = "MIN";
        switch(timeframeUnit){
        case "MIN":
               tfFactor = Constants.MIN;
               break;
        case "HOUR":
               tfFactor = Constants.HOUR;
               break;
        case "DAY":
               tfFactor = Constants.DAY;
               break;
               default:
        }
      //  int timeframeSize = 10;
        double closeP = basePrice;
        double sma = 0;
        if(log.isDebugEnabled())
            log.debug("variables assigned");
       
        Random rValue = new Random(System.currentTimeMillis());
        Random rSign = new Random(System.currentTimeMillis());
        if(log.isDebugEnabled())
            log.debug("seeded randomizers");
        double close = basePrice;
        double open = basePrice;
        double high = basePrice;
        double low = basePrice;
        double wap = basePrice;
        long time = 0;
       
        for(int i=0;i<queueSize; i++) {
              /* dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               closeP = closeP+dev;
              // if(log.isDebugEnabled())
               //    log.debug("closeP: " + closeP);*/
        	

      	  dev = rValue.nextDouble();
            positive = rSign.nextBoolean();
           
            if(!positive)
                  dev = Math.abs(dev) * (-1);
           
            open = open+dev;
            
            dev = rValue.nextDouble();
            positive = rSign.nextBoolean();
           
            if(!positive)
                  dev = Math.abs(dev) * (-1);
           
            close = close+dev;
            
            dev = rValue.nextDouble();
            high = high + dev;
            
            dev = rValue.nextDouble();
            low = low - dev;
          
               b= new Bar(0,
                            basePrice,
                            basePrice,
                            basePrice,
                            closeP,       //close!
                            basePrice,
                            volume,
                            cnt
                            );
               q.addFirst(b);        
        }
        
        log.debug(q);
		
		return q;
	}
	
	/**
	 * Fills up the testing queue
	 * @return
	 */
	public static ArrayDeque<Candle> fillCache(long cacheSize,String timeframeUnit, int timeframeSize, double basePrice){
		ArrayDeque<Candle> q = new ArrayDeque<>();
		double tfFactor = 0;
       // double basePrice= 28.7;
       // int smaSize=  30;
        Candle b;
        double dev = 0;
        boolean positive = true;
        int volume = 300000;
        int cnt= 10;
        //long queueSize = 72;	//2 days of 10-min bars
       // String timeframeUnit = "MIN";
        switch(timeframeUnit){
        case "MIN":
               tfFactor = Constants.MIN;
               break;
        case "HOUR":
               tfFactor = Constants.HOUR;
               break;
        case "DAY":
               tfFactor = Constants.DAY;
               break;
               default:
        }
      //  int timeframeSize = 10;
        double close = basePrice;
        double open = basePrice;
        double high = basePrice;
        double low = basePrice;
        double wap = basePrice;
        //double sma = 0;
        if(log.isDebugEnabled())
            log.debug("variables assigned");
       
        Random rValue = new Random(System.currentTimeMillis());
        Random rSign = new Random(System.currentTimeMillis());
        if(log.isDebugEnabled())
            log.debug("seeded randomizers");
       
        for(int i=0;i<cacheSize; i++) {
               dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               open = open+dev;
               
               dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               close = close+dev;
               
               dev = rValue.nextDouble();
               high = high + dev;
               
               dev = rValue.nextDouble();
               low = low - dev;
               
              // if(log.isDebugEnabled())
               //    log.debug("closeP: " + closeP);
               b= new Candle(0,
                            open,
                            close,
                            high,
                            low,       //close!
                            wap,
                            volume,
                            cnt
                            );
               q.addFirst(b);        
        }
        
       // log.debug(q);
		
		return q;
	}
	
	/**
	 * Fills up the testing queue and writes it to  the disc.
	 * 
	 * @return String  --file name
	 */
	public static String createReusableCache(long cacheSize,String timeframeUnit, int timeframeSize, double basePrice){
		ArrayDeque<Candle> q = new ArrayDeque<>();
		double tfFactor = 0;
       // double basePrice= 28.7;
       // int smaSize=  30;
        Candle b;
        double dev = 0;
        boolean positive = true;
        int volume = 300000;
        int cnt= 10;
        //long queueSize = 72;	//2 days of 10-min bars
       // String timeframeUnit = "MIN";
        switch(timeframeUnit){
        case "MIN":
               tfFactor = Constants.MIN;
               break;
        case "HOUR":
               tfFactor = Constants.HOUR;
               break;
        case "DAY":
               tfFactor = Constants.DAY;
               break;
               default:
        }
      //  int timeframeSize = 10;
        double close = basePrice;
        double open = basePrice;
        double high = basePrice;
        double low = basePrice;
        double wap = basePrice;
        //double sma = 0;
        if(log.isDebugEnabled())
            log.debug("variables assigned");
       
        Random rValue = new Random(System.currentTimeMillis());
        Random rSign = new Random(System.currentTimeMillis());
        if(log.isDebugEnabled())
            log.debug("seeded randomizers");
       
        for(int i=0;i<cacheSize; i++) {
               dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               open = open+dev;
               
               dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               close = close+dev;
               
               dev = rValue.nextDouble();
               high = high + dev;
               
               dev = rValue.nextDouble();
               low = low - dev;
               
              // if(log.isDebugEnabled())
               //    log.debug("closeP: " + closeP);
               b= new Candle(0,
                            open,
                            close,
                            high,
                            low,       //close!
                            wap,
                            volume,
                            cnt
                            );
               q.addFirst(b);        
        }
        
        log.debug(q);
        
        ObjectOutputStream fw=null;
        String file = "C:/run/other/cache";
        try{
        	fw = new ObjectOutputStream(new FileOutputStream(file));
        	fw.writeObject(q);
        }
        catch(Exception e){
        	log.error("Error writing cache: " + e.getMessage());
        }
        finally{
        	try{
        		fw.flush();
        		fw.close();
        	}
        	catch(Exception ex){}
        }
		
		return file;
	}


	public static void main(String[] args){
		//GregorianCalendar c = createEndOfRTH();
		/*long cacheSize = 10;
		String timeframeUnit = "MIN";
		int timeframeSize = 5;
		double basePrice = 28.7;
		String filename = createReusableCache(cacheSize,timeframeUnit, timeframeSize,basePrice);
		*/
		
		double result = truncateDouble(1.091455, 10000d);
		System.out.println("result: " + result);
	}
}
