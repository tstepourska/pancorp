package com.pancorp.tbroker.util;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Random;

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
}
