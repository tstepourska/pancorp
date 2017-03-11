package com.pancorp.tbroker.day;

public class EventOpenPosition extends Exception {

	private static final long serialVersionUID = 6712885443230071671L;

	private double limitPrice;
	private String action;
	public EventOpenPosition(String act, double lmtPrice){
		this.limitPrice = lmtPrice;
		this.action = act;
	}
	public double getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
