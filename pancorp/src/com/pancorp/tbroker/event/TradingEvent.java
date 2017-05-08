package com.pancorp.tbroker.event;

import com.ib.client.Types;

public abstract class TradingEvent extends Throwable implements ITradingEvent{
	private static final long serialVersionUID = -5802717100937001680L;
	private String action;
	private double limitPrice;
	
	public TradingEvent(String a, double p){
		this.action = a;
		this.limitPrice = p;
	}
	
	public String getAction(){
		return this.action;
	}

	public double getLimitPrice(){
		return this.limitPrice;
	}
}
