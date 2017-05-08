package com.pancorp.tbroker.event;

import com.ib.client.Types;

public class OpenPositionEvent extends TradingEvent {

	private static final long serialVersionUID = 2857422552814530611L;

	public OpenPositionEvent(String a, double p){
		super(a,p);
	}

}
