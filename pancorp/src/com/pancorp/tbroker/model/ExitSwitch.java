package com.pancorp.tbroker.model;

import java.util.ArrayDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.ClosePositionEvent;

public class ExitSwitch {
	private static Logger lg = LogManager.getLogger(ExitSwitch.class); 
	//public static final int TRIGGER = 1;
	private static final int SIZE = 3;
	//public boolean trigger;
	//public boolean confirmation1;
	//public boolean confirmation2;
	
	private ArrayDeque<Integer> p;
	public ExitSwitch() {
		p = new ArrayDeque<>();
		//trigger = false;
		//confirmation1 = false;
		//confirmation2 = false;
	}

	public void add(int n) throws ClosePositionEvent {
		boolean noevent = false;
		p.push(n);
		if(p.size()>SIZE)
			p.removeLast();
		
		lg.info(p);
		for(int k : p){
			if(k==0){
				noevent = true;
				break;
			}
		}
		
		if(noevent){}
		else {
			//reset and throw ClosePositionEvent
			reset();
			throw new ClosePositionEvent(null,0);
		}
		
		/*if(!trigger)
			trigger = true;
		else{
			if(!confirmation1)
				confirmation1 = true;
			else{
				if(!confirmation2)
					confirmation2 = true;
				else {
					//all values were previously set to true
					//reset and throw ClosePositionEvent
					reset();
					throw new ClosePositionEvent(null,0);
				}
			}
		}*/
	}
	
	public void reset(){
		//trigger = false;
		//confirmation1 = false;
		//confirmation2 = false;
	}
}
