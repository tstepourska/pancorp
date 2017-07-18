package com.pancorp.tbroker.event;

import java.util.List;

import com.ib.client.Order;

public class ClosePositionEvent extends TradingEvent {

	private static final long serialVersionUID = 2267905202660558739L;
	
	private List<Order> orderList;
	public ClosePositionEvent(String a, double p){
		super(a,p);
	}
	
	public void setOrderList(List<Order> ls){
		this.orderList = ls;
	}
	
	public List<Order> getOrderList(){
		return this.orderList;
	}
}
