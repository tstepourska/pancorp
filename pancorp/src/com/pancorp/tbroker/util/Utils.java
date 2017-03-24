package com.pancorp.tbroker.util;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;

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
	public static ArrayDeque<Candle> fillTheQueue(long queueSize,String timeframeUnit, int timeframeSize, double basePrice){
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
        double closeP = basePrice;
        double sma = 0;
        if(log.isDebugEnabled())
            log.debug("variables assigned");
       
        Random rValue = new Random(System.currentTimeMillis());
        Random rSign = new Random(System.currentTimeMillis());
        if(log.isDebugEnabled())
            log.debug("seeded randomizers");
       
        for(int i=0;i<queueSize; i++) {
               dev = rValue.nextDouble();
               positive = rSign.nextBoolean();
              
               if(!positive)
                     dev = Math.abs(dev) * (-1);
              
               closeP = closeP+dev;
              // if(log.isDebugEnabled())
               //    log.debug("closeP: " + closeP);
               b= new Candle(0,
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

	public static void main(String[] args){
		GregorianCalendar c = createEndOfRTH();
	}
}
