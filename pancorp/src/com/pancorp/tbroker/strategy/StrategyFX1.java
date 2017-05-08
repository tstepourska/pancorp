package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Types;
import com.ib.client.Types.Action;
import com.pancorp.tbroker.day.ForexBroker;
import com.pancorp.tbroker.event.OpenPositionEvent;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.event.TradingEvent;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.CandlePattern;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Calculator;

/**
 * Strategy description:
 * 
 * FX1: 1-min scalping strategy 

Needed Thin spread, low commission, every currency pair
1-min timeframe
Indicators: Stochastic 5,3,3 and 50 EMA, 100 EMA
Preferred sessions: London, New York (high volatility)

Long entry point:
EMA 50 must be positioned above EMA 100. at least 2 pips
When it happened, it is essential to wait until the price comes back to the EMAs.(touches)
In turn, Stochastic oscillator is exploited to cross above the 20 level from below.
The moment you observe all 3 items arranged in a proper way it is completely safe to open a long (buy) order.
In order to stay safe, stop loss. Stop losses arranged 2-3 pips just below the last point of a particular swing. As FX1 is a short term strategy, it is anticipated to gain 8-12 pips on a trade. Hence take profits are best within 8-12 pips


Short entry point:
EMA 50 must be positioned below EMA 100 at least 2 pips
Wait until price returns to the EMAs.(touches)
Stochastic oscillator is utilized to cross over the 80 level from above
As soon as all items are in place, open a short order.
Similarly, stop losses are positioned near 2-3 pips below (above?!?!) the last low (high!?!?) point of the swing accordingly, and take-profits should be within 8-12 pips from the entry price.


Pros:

Less risk exposure
Small movements easy to achieve, even in slow markets
More frequent


Cons:

Large deposit is needed
Bankers and dealers have more advantage over retail scalpels, as they have more info
1-min scalper needs quick reflexes, good instincts, and mathematical skills
It can be difficult to scalp and maintain good risk/reward ratio. For instance, with ratio 2:1 you take profits at 10 pips requires a stop loss at 5 pips, making it too close not to get stopped out in the majority of cases.
1-minute scalping is time-consuming and may lead stress and an unhealthy life style
 *    
 * @author pankstep
 *
 */
public class StrategyFX1 //implements IStrategy 
{
	private static Logger lg = LogManager.getLogger(StrategyFX1.class); 
	
	private Calculator calc;
	private CandlePattern currentPattern;
	private int emaFastPeriod = 50;
	private int emaSlowPeriod = 100;
	
	private int stocKPeriod = 5;
	private int stocDPeriod = 3;
	private int stoc        = 3;
	private int stocOverbought = 83;
	private int stocOversold   = 17;
	
	private int maxCache = 240;  // 4-hours
	//private int medCache = 120;  // 2 hours
	private int minCache = 100;   // 1 hour 40 mins
	
	//private Stack<Candle> local3Stack = null;
	
	public StrategyFX1(Calculator c){
		this.calc = c;
		//local3Stack = new Stack<Candle>();
	}
	
	/**
	 * 
	 */
	public void evaluateEntry(ArrayDeque<Candle> candles)  throws OpenPositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateEntry: ";
		lg.trace(fp);
		if(candles.size()<stocKPeriod){
			lg.debug(fp + "Size less than " + stocKPeriod);
			throw new NotEnoughDataException();
		}
		
		calc.calcStochastic(candles, stocKPeriod, stocDPeriod);
		lg.debug(fp + "stochasticK: " + candles.peek().stochasticK());
		
		if(candles.size()<emaFastPeriod){
			lg.debug(fp + "Size less than " +emaFastPeriod + ": " + candles.size());
			throw new NotEnoughDataException();
		}
		
		double ema50 = calc.calcForexEmaShortClose(emaFastPeriod, candles);
		lg.debug(fp + "ema50: " + ema50);
		candles.peek().emaShort(ema50);
		
		if(candles.size()<emaSlowPeriod){
			lg.debug(fp + "Size less than " +emaSlowPeriod + ": " + candles.size());
			throw new NotEnoughDataException();
		}

		double ema100 = calc.calcForexEmaLongClose(emaSlowPeriod, candles);
		lg.debug(fp + "ema100: " + ema100);
		candles.peek().emaLong(ema100);	
		
		if(!calc.calibrated)
			calc.calibrated = true;
		
		//OpenPositionEvent event = null;
		Candle curr = null;
		//previous candle		
		Candle prev = null;
		Candle secondPrev = null;
				
	/*	if(candles.size()<3){
			local3Cache.push(candles.peek());
			throw new NotEnoughDataException();
		}*/

		//temporary remove newly added candle to be able to check previous candle(s)
		curr = candles.pop();
		//double sk = curr.stochasticK();
		//int currDir = curr.getDirection();
		
		//double sd = candle.stochasticD();
		
		//local3Stack.push(curr);

		prev = candles.pop();	
		secondPrev = candles.peek();  //did not remove
		
		//do not eliminate, for checking previous candles at least one condition not met
		//if(sk > stocOversold || sk < stocOverbought){
			//TODO reset any flags or patterns here
		//	return null;
		//}
		
		//double prevSK = candles.peek().stochasticK();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "popped latest candle, calc: " + this.calc);

		//don't forget to put pack popped candles!
		candles.push(prev);
		candles.push(curr);

		//looking for a long entry (confirmed)
		if(ema50 > ema100){ //fast ema is above slow ema
			openLong(curr, prev,secondPrev);
		}
		//looking for a short entry (confirmed)
		else if(ema50 < ema100){
			openShort(curr, prev,secondPrev);
		}
		
		//return null; 
	}

	private boolean openLong(//double stochasticK, 
			Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "openLong: ";
		boolean openLong = false;
		
		//determine if price got the closest to the EMA? Or lowest (what if below EMA?)
		double currLow = curr.low();
		double prevLow = prev.low();
		double secondPrevLow = secondPrev.low();
		double prevStochasticK = 0;
		//double prevEma50 = 0;
		//double prevEma100 = 0;
		
		if(prevLow < currLow && prevLow < secondPrevLow){ //prevLow is confirmed lowest point out of last 3
			//confirming prevLow candle signal event
			prevStochasticK = prev.stochasticK();
			lg.debug(fp + "prevStochasticK: " + prevStochasticK);
			//prevEma50 = prev.emaShort();
			//prevEma100 = prev.emaLong();
			
			if(prevStochasticK > 0 && prevStochasticK < this.stocOversold){ 
				createOpenLongPositionEvent(curr);
			}
		}

		return openLong;
	}
	
	private boolean openShort(//double stochasticK, 
			Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "openShort: ";
		boolean openShort = false;
		
		//determine if price got the closest to the EMA
		double currHigh = curr.high();
		double prevHigh = prev.high();
		double secondPrevHigh = secondPrev.high();
		double prevStochasticK = 0;
		
		if(prevHigh > currHigh && prevHigh > secondPrevHigh){ //prevHigh is confirmed highest point out of last 3
			//confirming prevHigh candle signal event
			prevStochasticK = prev.stochasticK();
			lg.debug(fp + "prevStochasticK: " + prevStochasticK);
			
			//prevEma50 = prev.emaShort();
			//prevEma100 = prev.emaLong();
			
			if(prevStochasticK <=100 && prevStochasticK > this.stocOverbought){ 
				createOpenShortPositionEvent(curr);
			}
		}
		
		return openShort;
	}
	
	private void createOpenLongPositionEvent(Candle c) throws OpenPositionEvent {
		OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_BUY, c.close());
		lg.debug("createOpenLongPositionEvent: throwing OpenPositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		throw event;
	}
	
	private void createOpenShortPositionEvent(Candle c) throws OpenPositionEvent {
		OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_SSHORT, c.close());
		lg.debug("createOpenShortPositionEvent: throwing OpenPositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		throw event;
	}
	
	public void evaluateExit(ArrayDeque<Candle> candles)  throws ClosePositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateExit: ";
		lg.debug(fp);
	}
}
