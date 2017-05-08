package com.pancorp.tbroker.event;

import com.ib.client.Types;

public class ClosePositionEvent extends TradingEvent {

	private static final long serialVersionUID = 2267905202660558739L;
	public ClosePositionEvent(String a, double p){
		super(a,p);
	}
}
