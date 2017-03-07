package com.pancorp.tbroker.strategy;


import java.util.ArrayDeque;
import java.util.Iterator;

import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;
import com.pancorp.tbroker.util.Constants;

public class IntradayLong {
	/**
	 * Trend following
	 * 
	 * ==>price is above sma //at least by 10%
	 * ==>sma (short)> ema (long) //at least by 10%
	 * ==>doji within 3 candles, OR 
	 * ==>hummer within 3 candles, OR
	 * ==>3 line strike
	 * ==>abandoned baby (morning doji star)
	 */
	public static boolean maShortAboveLong(Candle newCandle){
		boolean buy = false;
		
		if(newCandle.close() > newCandle.emaShort() && //> 1.10  &&		//price is above sma //at least by 10%
			newCandle.emaShort() > newCandle.emaLong() && //> 1.10	&&	//sma (short)> ema (long) //at least by 10%
			newCandle.getBody_len()<=0.05						// doji within 3 candles
												// hummer within 3 candles
												// 3 line strike
												// abandoned baby (morning doji star
						)
			buy = true;
		
		return buy;
	}
	
	/**
	 *  Trend following:
	 *  
 	 *   * -->Locate stock that breaking out up(down) strongly 
	30 	 * -->Select 2 SMA to apply to the chart (ex. 5 and 10) 
	31 	 * -->Make sure price has not been touching the 5 SMA or 10 SMA excessively in the last 10 bars 
	32 	 * -->Wait for the price to close above(below) both moving averages in the counter direction  
	33 	 * of the primary trend on the SAME BAR 
	34 	 * -->Enter the trade on the next bar 
	 */
	public static boolean ilMaCrossover(ArrayDeque<IBar> candles){
		boolean buy = false;
		//int n = 3;
		if(lastNCandlesCrossoverPercent(candles, 3) &&	//at least last 3 candles crossover short SMA/EMA to above long SMA/EMA //- 10%
		   lastNCandlesNotTouchingBothMa(candles, 10) &&	//last 10 candles close, low, open >= short SMA/EMA AND >= long SMA/EMA
			closeAboveOnBlack(candles)	//trend is up, close above short SMA/EMA and long SMA/EMA on the last BLACK! candle
		){
			buy = true;
		}
		return buy;
	}
	
	private static boolean lastNCandlesCrossoverPercent(ArrayDeque<IBar> candles, int n){
		boolean b = false;
		boolean g = false;
		int gCnt = 0;
		
		Candle c;
		Iterator<IBar> it = candles.iterator();
		
		for(int i=0;i<n;i++){
			c = (Candle)it.next();
			b = c.emaShort()>c.emaLong();
			if(!b)
				break;
		}
		
		return b;
	}
	
	private static boolean lastNCandlesNotTouchingBothMa(ArrayDeque<IBar> candles, int n){
		boolean b = false;
		
		Candle c;
		Iterator<IBar> it = candles.iterator();
		double open;
		double close;
		double low;
		double maShort;
		double maLong;
		
		for(int i=0;i<n;i++){
			c = (Candle)it.next();
			open = c.open();
			close = c.close();
			low = c.low();
			maShort = c.emaShort();
			maLong = c.emaLong();
			
			b = open>maShort && close>maShort && low > maShort && open>maLong && close>maLong && low > maLong;
			
			if(!b)
				break;
		}
		
		return b;
	}
	
	private static boolean closeAboveOnBlack(ArrayDeque<IBar> candles){
		boolean b = false;
		Candle c = (Candle)candles.peekFirst();
		
		if(c.close()>c.emaShort() && c.close()>c.emaLong() && c.getDirection()==Constants.DIR_BLACK);
			b= true;
		
		return b;
	}
}
