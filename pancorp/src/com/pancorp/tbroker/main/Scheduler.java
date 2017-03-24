package com.pancorp.tbroker.main;

import java.util.GregorianCalendar;

import com.pancorp.tbroker.util.Utils;

public class Scheduler {

	public Scheduler() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void runSchedule(){
		
    GregorianCalendar end = Utils.createEndOfRTH();
    GregorianCalendar begin = Utils.createStartOfRTH();
   /* int bhh = begin.get(Calendar.HOUR_OF_DAY);
    int bmm = begin.get(Calendar.MINUTE);
    int ehh = end.get(Calendar.HOUR_OF_DAY);
    int emm = end.get(Calendar.MINUTE);
    
    if(lg.isTraceEnabled()){
    	lg.trace("Start of RTH: H:" + bhh + ", M: " + bmm);  
    	lg.trace("End of RTH  : H:" + ehh + ", M: " + emm);  
    }*/
    GregorianCalendar now = null;
    int cnt =0;
    
  //  while(working){
    	//cnt++;       	       	
    	//if(cnt==5){       		
		//if(lg.isTraceEnabled()){
			//lg.trace("... tMap: " + tMap);
    	//}
		//cnt=0;
	//} 
    	now = new GregorianCalendar();
    	//Utils.resetCalendars(begin, end, now);
    /*	if(now.after(begin)&& now.before(end)){
    		if(lg.isDebugEnabled())
    			lg.debug("now is between begin and end");

    		if(this.workingStatus==Constants.WORKING_STATUS_IDLE){
    			if(lg.isDebugEnabled())
        			lg.debug("status is idle, starting to work");	*/
    			//startTheDay();
 /*   		}
    		else{
    			if(lg.isDebugEnabled())
        			lg.debug("status is already active, nothing to do");
    		}
    	}
    	else{
    		if(lg.isDebugEnabled())
    			lg.debug("now is outside working hours");
    		if(this.workingStatus==Constants.WORKING_STATUS_ACTIVE){
    			if(lg.isDebugEnabled())
        			lg.debug("status is active, shutting down..");
    			wrapTheDayUp();
    		}
    		else {
    			if(lg.isDebugEnabled())
        			lg.debug("status is already idle, nothing to do");
    		}
    	}
    	*/
    	
    	/*
    	int hh = now.get(Calendar.HOUR_OF_DAY);
    	int mi = now.get(Calendar.MINUTE);
    	//if(cnt==5){       		
    		if(lg.isTraceEnabled()){
    			lg.trace("... tMap: " + tMap);
            	lg.trace("Now: H:" + hh+ ",M:" +mi);  
        	}
    		//cnt=0;
    	//}    	
    	
    	if(hh >= bhh){  //between 9 and 23
        	if(hh>=ehh){ //between 15 and 23
        		if(mi>=emm){ //greater than both work hours and minutes, if working, stop
            		if(this.workingStatus==Constants.WORKING_STATUS_ACTIVE)
            			wrapTheDayUp();
            	}
        		else { // mins less than stop mins            			
        			// minutes did not reach closing; if not active, start
            		//if(this.workingStatus==Constants.WORKING_STATUS_IDLE)
            		//	startTheDay();
        		}
        	}
        	else { //if(hh<ehh){ //between 9 and 15
        		if(mi>=bmm ){
        			//working hours and minutes, if not active, start
            		if(this.workingStatus==Constants.WORKING_STATUS_IDLE)
            			startTheDay();
            	}
        		else { //mins less than begin mins
        			//not a working time, stop if active
            		if(this.workingStatus==Constants.WORKING_STATUS_ACTIVE)
            			wrapTheDayUp();
        		}            		
        	}        	
        }      	
    	else {  //now is between 1(0?) and 8 hrs  if(hh<bhh) 
    		//not a working time, stop if active
    		if(this.workingStatus==Constants.WORKING_STATUS_ACTIVE)
    			wrapTheDayUp();
    	}*/
    	
    			try{
    	Thread.sleep(1000*60);
    			}
    			catch(InterruptedException ie) {}
  //  }

}


}
