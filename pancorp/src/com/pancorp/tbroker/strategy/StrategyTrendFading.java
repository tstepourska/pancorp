package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;

import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.OpenPositionEvent;
import com.pancorp.tbroker.event.TradingEvent;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.CandlePattern;

public class StrategyTrendFading implements IStrategy {
	public CandlePattern evaluate(ArrayDeque<Candle> candles, CandlePattern currentPattern)  throws OpenPositionEvent, ClosePositionEvent, Exception {
		//not trending, use trend fading strategy
		/*
		//between 0 and -20  - overbought,
		//During a price downtrend, enter short/sell when the indicator was overbought and then drops below the -50 level. 
		if(williamsR > Globals.WILLIAMS_R_SELL_TRIG){
			//sell = true;
		}
		//between -80 and -100  - oversold,
		//During an uptrend, buy when the price was oversold then rallies above the -50 level.
		else if(williamsR < Globals.WILLIAMS_R_BUY_TRIG){
			//buy = true;
		}
		else {
			
		}*/
		return null;
	}
}
