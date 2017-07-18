package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.OpenPositionEvent;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.oscillators.WilliamsRIndicator;
import com.pancorp.tbroker.indicators.ma.EMA;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.util.Constants;

/**
 * Strategy description:
 * 
 * FX2: corrected FX1 - 1-min scalping strategy with Williams R instead of Stochastics

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
public class StrategyFX1A implements IStrategy
{
	private static Logger lg = LogManager.getLogger(StrategyFX1A.class); 
	
	//private Calculator calc;
	//private CandlePattern currentPattern;
	private boolean calibrated = false;
	private int emaFastPeriod = 50;
	private int emaSlowPeriod = 100;
	private static final double EMA_PIP_DIFF = 0.0002;  //2 pips
	
	private int wPeriod = 9;
	//private int stocDPeriod = 3;
	//private int stoc        = 3;
	private int wrOverboughtThreshold = -12;
	private int wrOversoldThreshold   = -88;
	
	private int maxCache = 240;  // 4-hours
	//private int medCache = 120;  // 2 hours
	//private int minCache = 100;   // 1 hour 40 mins
	
	//private Stack<Candle> local3Stack = null;
	//StochasticOscillatorKIndicator stochasticK;
	//StochasticOscillatorDIndicator stochasticD;
	WilliamsRIndicator wri;
	
	//SMAIndicator sma;
	//EMAIndicator ema;
	EMA ema;
	
	public StrategyFX1A() { //Calculator c){
		wri = new WilliamsRIndicator();
		
		//sma = new SMAIndicator();
		//ema = new EMAIndicator(sma);
		ema = new EMA(this.emaFastPeriod, this.emaSlowPeriod);
		
		//calc = new Calculator();
	}
	
	private void calculateWilliamsR(ArrayDeque<Candle> cc) throws NotEnoughDataException, Exception {
	
		double w = wri.calculate(cc, wPeriod);  // 9
		cc.peekFirst().williamsR(w);
	}
	
	/**
	 * 
	 */
	public void evaluateEntry(ArrayDeque<Candle> candles)  throws OpenPositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateEntry: ";
		lg.trace(fp);
		
		/*if(candles.size()>=maxCache){
			candles.removeLast();
		}*/
		
		if(candles.size()<wPeriod){
			lg.debug(fp + "Size less than " + wPeriod);
			fillFastEma(candles.peekFirst());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}
		
		calculateWilliamsR(candles);
		lg.debug(fp + "WilliamsR: " + candles.peek().williamsR());
		
		if(candles.size()<emaFastPeriod){
			lg.debug(fp + "Size less than " +emaFastPeriod + ": " + candles.size());
			fillFastEma(candles.peekFirst());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}
		
		double ema50 = ema.calculateFastClose(candles); //calc.calcForexEmaShortClose(emaFastPeriod, candles);
		//lg.debug(fp + "ema50: " + ema50);
		//candles.peek().emaFast(ema50);
		
		if(candles.size()<emaSlowPeriod){		
			lg.debug(fp + "Size less than " +emaSlowPeriod + ": " + candles.size());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}

		double ema100 = ema.calculateSlowClose(candles); // calc.calcForexEmaLongClose(emaSlowPeriod, candles);
		//lg.debug(fp + "ema100: " + ema100);
		//candles.peek().emaSlow(ema100);	

		if(!calibrated){
			calibrated = true;
			lg.info(fp + "set calibrated=true");
		}
		
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
		prev 		= candles.pop();	
		secondPrev = candles.peek();  //did not remove
		
		//don't forget to put pack popped candles!
		candles.push(prev);
		candles.push(curr);
		
		/////////////////
		
			lg.info("fast sma50 "+curr.smaFast()+",  slow sma100 " + curr.smaSlow());
		////////////////

		//looking for a long entry (confirmed)
		if(ema50 > ema100 &&  (ema50 - ema100) >= EMA_PIP_DIFF){ //fast ema is above slow ema by at least 2 pips
			lg.info("fast ema50 "+ema50+" is above slow ema100 " + ema100);
			checkLong(curr, prev,secondPrev);
		}
		//looking for a short entry (confirmed)
		else if(ema50 < ema100 && (ema100 - ema50) >= EMA_PIP_DIFF){ //fast ema is below slow ema by at least 2 pips){
			lg.info("fast ema50 "+ ema50+" is BELOW slow ema100 "+ema100);
			checkShort(curr, prev,secondPrev);
		}
		
		//return null; 
	}
	
	private void fillFastEma(Candle c){
		double close = c.close();
		c.emaFast(close);
		c.smaFast(close);
		lg.trace("Filled fast ema with close value");
	}
	
	private void fillSlowEma(Candle c){
		double close = c.close();
		c.emaSlow(close);
		c.smaSlow(close);
		lg.trace("Filled slow ema with close value");
	}

	private boolean checkLong(Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "checkLong: ";
		boolean openLong = false;
		
		//determine if price got the closest to the EMA? Or lowest (what if below EMA?)
		double currLow = curr.low();
		double prevLow = prev.low();
		double secondPrevLow = secondPrev.low();
		double prevWilliamsR = 0;
		//double prevEma50 = 0;
		//double prevEma100 = 0;
		
		if(prevLow < currLow && prevLow < secondPrevLow){ //prevLow is confirmed lowest point out of last 3
			//confirming prevLow candle signal event
			prevWilliamsR = prev.williamsR();
			lg.debug(fp + "prevWilliamsR: " + prevWilliamsR);
			//prevEma50 = prev.emaShort();
			//prevEma100 = prev.emaLong();
			
			if(prevWilliamsR < this.wrOversoldThreshold){ 
				lg.debug(fp + "prevWilliamsR " + prevWilliamsR + " < " + this.wrOversoldThreshold + ", creating long position event");
				createOpenLongPositionEvent(curr);
			}
		}

		return openLong;
	}
	
	private boolean checkShort(Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "checkShort: ";
		boolean openShort = false;
		
		//determine if price got the closest to the EMA
		double currHigh = curr.high();
		double prevHigh = prev.high();
		double secondPrevHigh = secondPrev.high();
		double prevWilliamsR = 0;
		
		if(prevHigh > currHigh && prevHigh > secondPrevHigh){ //prevHigh is confirmed highest point out of last 3
			//confirming prevHigh candle signal event
			prevWilliamsR = prev.williamsR();
			lg.debug(fp + "prevWilliamsR: " + prevWilliamsR);
			
			//prevEma50 = prev.emaShort();
			//prevEma100 = prev.emaLong();
			
			if(prevWilliamsR > this.wrOverboughtThreshold){ 
				lg.debug(fp + "prevWilliamsR " + prevWilliamsR + " > " + this.wrOverboughtThreshold + ", creating short position event");
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
		//OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_SSHORT, c.close());
		OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_SELL, c.close());
		lg.debug("createOpenShortPositionEvent: throwing OpenPositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		throw event;
	}
	
	public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5min)  throws ClosePositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateExit: ";
		lg.debug(fp);
	/*	if(candles.size()>=maxCache){
			candles.removeLast();
		}*/
		if(!calibrated){
			if(candles.size()>=this.emaSlowPeriod){
				calibrated = true;
				lg.info(fp + "set calibrated=true");
			}
		}
	}

	/**
	 * @return the maxCache
	 */
	public int getMaxCache() {
		return maxCache;
	}

	/**
	 * @param maxCache the maxCache to set
	 */
	public void setMaxCache(int maxCache) {
		this.maxCache = maxCache;
	}

	/**
	 * @return the calibrated
	 */
	public boolean isCalibrated() {
		return calibrated;
	}

	/**
	 * @param calibrated the calibrated to set
	 */
	public void setCalibrated(boolean calibrated) {
		this.calibrated = calibrated;
	}

	@Override
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, List<Order> orders, Contract contract,
			double latestClose5min) throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, boolean tradeOpened, List<Order> orders,
			Contract contract, double latestClose5min)
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}
}
