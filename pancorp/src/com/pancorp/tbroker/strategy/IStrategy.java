package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.pancorp.tbroker.event.*;
import com.pancorp.tbroker.model.Candle;

public interface IStrategy {
	public void evaluateEntry(ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception;
	public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5min) throws ClosePositionEvent, NotEnoughDataException, Exception;
	public int getMaxCache();
	public boolean isCalibrated();
	
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, List<Order> orders, Contract contract, double latestClose5min) 
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception;
	
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, boolean tradeOpened, List<Order> orders, Contract contract, double latestClose5min) 
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception;
	//public int getSlope(ArrayDeque<Candle> cc, double latestClose, int pd);
}
