package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.OpenPositionEvent;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.ma.EMA;
import com.pancorp.tbroker.indicators.ma.SMA;
//import com.pancorp.tbroker.indicators.oscillators.StochasticOscillatorDIndicator;
//import com.pancorp.tbroker.indicators.oscillators.StochasticOscillatorKIndicator;
import com.pancorp.tbroker.indicators.oscillators.StochasticOscillatorD;
import com.pancorp.tbroker.indicators.oscillators.StochasticOscillatorK;
import com.pancorp.tbroker.indicators.simple.Fibonacci;
import com.pancorp.tbroker.model.Candle;
//import com.pancorp.tbroker.order.UpdateOrderT;
import com.pancorp.tbroker.util.Constants;

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
In order to stay safe, stop loss. Stop losses arranged 2-3 pips just below the last point of a particular swing. As FX1 is a short term strategy, 
it is anticipated to gain 8-12 pips on a trade. Hence take profits are best within 8-12 pips
My change to the original strategy: loss - 9 pips
									profit - 19 pips


Short entry point:
EMA 50 must be positioned below EMA 100 at least 2 pips
Wait until price returns to the EMAs.(touches)
Stochastic oscillator is utilized to cross over the 80 level from above
As soon as all items are in place, open a short order.
Similarly, stop losses are positioned near 2-3 pips below (above?!?!) the last low (high!?!?) point of the swing accordingly, and take-profits should be within 8-12 pips from the entry price.
My change to the original strategy: loss - 9 pips
									profit - 19 pips

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

Trying exit strategy:

	Remember entry point, keep track of highest high and lowest low to entry point
	Calculate 
	LONG:  	maximum retracement down from the highest high	Selling point - profit taker
			maximum retracement down from the entryPoint	Stop Loss
		
	SHORT: 	maximum retracement up from the lowest low		buying point - profit taker
			maximum retracement up from the entry point		Stop loss
 *    
 * @author pankstep
 *
 */
public class StrategyFX1B implements IStrategy 
{
	private static Logger lg = LogManager.getLogger(StrategyFX1B.class); 
	
	private boolean calibrated = false;
	//private boolean pivotPointFound = false;
	private int smaFastPeriod = 3;
	private int emaMediumPeriod = 50;
	private int emaSlowPeriod = 100;
	private static final double EMA_PIP_DIFF = 0.0002;  //2 pips
	private static final double PRICE_LIMIT_DEV = 0.00005;  
	private static final double FIB_PERCENT = 0.236;
	
	private String openedTradeAction;
	
	private int stocKPeriod = 5;
	private int stocDPeriod = 3;
	//private int stoc        = 3;
	private int stocOverbought = 82;
	private int stocOversold   = 18;
	
	private int maxCache = 240;  // 4-hours
	//private int medCache = 120;  // 2 hours
	//private int minCache = 100;   // 1 hour 40 mins
	
	//private Stack<Candle> local3Stack = null;
	StochasticOscillatorK stochasticK;
	StochasticOscillatorD stochasticD;
	
	private double entryPoint = 0;
	//TODO - ??actual price or difference from entry??
	double farthestFromEntry = 0;
//	double lowest = 10000;
	EMA ema;
	SMA sma;
	boolean mediumAboveSlow = true;
	
	public StrategyFX1B() { 
		stochasticK = new StochasticOscillatorK();
		stochasticD = new StochasticOscillatorD();
		ema = new EMA(this.emaMediumPeriod, this.emaSlowPeriod); //50, 100
		sma = new SMA(); //for smaFastPeriod
		
		openedTradeAction = "NONE";
	}
	
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, boolean __tradeOpened, List<Order> orders, Contract contract, double latestClose5sec) 
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		String fp = "evaluate: ";
		//lg.trace(fp);
		Candle c = candles.peekFirst();
		double close = c.close();
		
		if(candles.size()<stocKPeriod){
			lg.debug(fp + "Size less than " + stocKPeriod);
			fillFastSma(c,close);
			fillMediumEma(c,close);
			fillSlowEma(c,close);
			throw new NotEnoughDataException();
		}
		
		calculateStochastic(candles);
		lg.debug(fp + "stochasticK: " + candles.peek().stochasticK());
	
		//sma3 - for exit only
		if(candles.size()<smaFastPeriod){
			lg.debug(fp + "Size less than smaFastPeriod " +smaFastPeriod + ": " + candles.size());
			fillFastSma(c,close);
			fillMediumEma(c,close);
			fillSlowEma(c,close);
			throw new NotEnoughDataException();
		}	
		double sma3 = sma.calculateClose(candles,smaFastPeriod); 
		lg.debug(fp + "sma3: " + sma3);
		candles.peekFirst().smaFast(sma3);
		
		if(candles.size()<emaMediumPeriod){
			lg.debug(fp + "Size less than " +emaMediumPeriod + ": " + candles.size());
			fillMediumEma(c,close);
			fillSlowEma  (c,close);
			throw new NotEnoughDataException();
		}	
		double ema50 = ema.calculateMediumClose(candles, emaMediumPeriod); //calc.calcForexEmaShortClose(emaFastPeriod, candles);
		lg.debug(fp + "ema50: " + ema50);
		
		if(candles.size()<emaSlowPeriod){		
			lg.debug(fp + "Size less than " +emaSlowPeriod + ": " + candles.size());
			fillSlowEma(c,close);
			throw new NotEnoughDataException();
		}
		double ema100 = ema.calculateSlowClose(candles); // calc.calcForexEmaLongClose(emaSlowPeriod, candles);
		lg.debug(fp + "ema100: " + ema100);
		
		if(!calibrated){
			calibrated = true;
			lg.info(fp + "set calibrated=true");
		}
		
		if(orderPlaced){		
			//if(tradeOpened)
				evaluateExit(candles, orders, contract, latestClose5sec);
		}
		else{
			evaluateEntry(candles, ema100, ema50);
		}
	}

	/**
	 * 
	 */
	private void evaluateEntry(ArrayDeque<Candle> candles, double ema100, double ema50)  throws OpenPositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateEntry: ";
		lg.trace(fp);

		//temporary remove newly added candle to be able to check previous candle(s)
		Candle curr = candles.pop();
		Candle prev 		= candles.pop();	
		Candle secondPrev = candles.peek();  //did not remove

		//don't forget to put pack popped candles!
		candles.push(prev);
		candles.push(curr);
		
		/////////////////	
		lg.info("fast sma50 "+curr.emaMedium()+",  slow sma100 " + curr.smaSlow());
		////////////////

		//looking for a long entry (confirmed)
		if(ema50 > ema100) {// &&  (ema50 - ema100) >= EMA_PIP_DIFF){ //fast ema is above slow ema by at least 2 pips
			mediumAboveSlow = true;
			lg.info("medium ema50 "+ema50+" is above slow ema100 " + ema100);
			checkLongEntry(curr, prev,secondPrev);
		}
		//looking for a short entry (confirmed)
		else if(ema50 < ema100) { // && (ema100 - ema50) >= EMA_PIP_DIFF){ //fast ema is below slow ema by at least 2 pips
			mediumAboveSlow = false;
			lg.info("medium ema50 "+ ema50+" is BELOW slow ema100 "+ema100);
			checkShortEntry(curr, prev,secondPrev);
		}
		else {//ema50==ema100
		
		}
	}
	
	private void calculateStochastic(ArrayDeque<Candle> cc) throws NotEnoughDataException, Exception {
		if(cc==null || cc.size()<stocKPeriod)
			throw new NotEnoughDataException("calculateStochastic: size is less than " + stocKPeriod);
		
		double K = stochasticK.calculate(cc, stocKPeriod);  //5
		cc.peekFirst().stochasticK(K);
		
		double D = stochasticD.calculate(cc, stocDPeriod);  // 3, orig 14
	    cc.peekFirst().stochasticD(D);	
	}
	
	private void fillFastSma(Candle c, double cl){
		c.emaFast(cl);
		c.smaFast(cl);
		lg.trace("Filled fast sma with close value");
	}
	
	private void fillMediumEma(Candle c, double cl){
		c.emaMedium(cl);
		c.smaMedium(cl);
		lg.trace("Filled medium ema with close value");
	}
	
	private void fillSlowEma(Candle c, double cl){
		c.emaSlow(cl);
		c.smaSlow(cl);
		lg.trace("Filled slow ema with close value");
	}

	private boolean checkLongEntry(Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "checkLongEntry: ";
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
			
			if(//prevStochasticK > 0 && 
					prevStochasticK < this.stocOversold){ 
				createOpenLongPositionEvent(curr);
			}
		}

		return openLong;
	}
	
	private boolean checkShortEntry(Candle curr, Candle prev, Candle secondPrev) throws OpenPositionEvent {
		String fp = "checkShortEntry: ";
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
		
			if(//prevStochasticK <=100 && 
					prevStochasticK > this.stocOverbought){ 
				createOpenShortPositionEvent(curr);
			}
		}
		
		return openShort;
	}

	public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5sec)  throws ClosePositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateExit: ";
		lg.debug(fp);
		
		//temporary remove newly added candle to be able to check previous candle(s)
		Candle curr 		= candles.peekFirst(); //.pop();
	//	Candle prev 		= candles.peekFirst(); //.pop();	
	//	Candle secondPrev 	= candles.peekFirst();  //did not remove
		//don't forget to put pack popped candles!
		//candles.push(prev);
		//candles.push(curr);
		
		double currSmaFast = curr.smaFast();
	//	double prevSmaFast = prev.smaFast();
	///	double secondPrevSmaFast = secondPrev.smaFast();
		double fibPrice = 0;
		double close = curr.close();

		//get action of the parent order
		String action = orders.get(0).getAction();
		lg.info(fp + "action: " + action);

		switch(this.openedTradeAction){
		case Constants.ACTION_BUY: //looking for sell exit
			/*if(prevSmaFast > currSmaFast && prevSmaFast > secondPrevSmaFast){
				//prev is a high pivot point 
			}
			else if(prevSmaFast > currSmaFast && prevSmaFast > secondPrevSmaFast){
				//prev is a low pivot point 
			}*/
			//check farthest from entry point (max)
			if(currSmaFast>this.farthestFromEntry){ 
				//set max and keep going
				this.farthestFromEntry = currSmaFast;
				lg.info("New high point: " + this.farthestFromEntry);
			}
			else{
				fibPrice = Fibonacci.getRetracement(Constants.DIR_WHITE, close, this.entryPoint, FIB_PERCENT);
				lg.info(fp + "fibPrice: " + fibPrice);
				if(close > fibPrice){
					//keep going
					lg.info(fp + "close " + close + " is higher than fibPrice " + fibPrice + ", OK");
				}
				else {
					//price less than retracement percent, sell
					lg.info(fp + "close " + close + " is equal or lower than fibPrice " + fibPrice + ", selling");
					this.createCloseLongPositionEvent(curr,orders,contract);
				}
			}
	
			break;
		case Constants.ACTION_SELL:  //looking for buying exit
			/*if(prevSmaFast > currSmaFast && prevSmaFast > secondPrevSmaFast){
				//prev is a high pivot point 
			}
			else if(prevSmaFast > currSmaFast && prevSmaFast > secondPrevSmaFast){
				//prev is a low pivot point 
			}*/
			//check farthest from entry point (max)
			if(currSmaFast<this.farthestFromEntry){ 
				//set max and keep going
				this.farthestFromEntry = currSmaFast;
				lg.info("New low point: " + this.farthestFromEntry);
			}
			else{
				//TODO check 
				fibPrice = Fibonacci.getRetracement(Constants.DIR_BLACK, close, this.entryPoint, FIB_PERCENT);
				if(close > fibPrice){
					//keep going
					lg.info(fp + "close " + close + " is lower than fibPrice " + fibPrice + ", OK");
				}
				else {
					//price less than retracement percent, sell
					lg.info(fp + "close " + close + " is equal or higher than fibPrice " + fibPrice + ", buying");
					this.createCloseShortPositionEvent(curr,orders,contract);
				}
			}
			break;
			default:
		}
	}
	
	private void createOpenLongPositionEvent(Candle c) throws OpenPositionEvent {
		this.entryPoint = c.close()+PRICE_LIMIT_DEV;
		OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_BUY, this.entryPoint);	
		lg.debug("createOpenLongPositionEvent: throwing OpenPositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		
		openedTradeAction = Constants.ACTION_BUY;
		this.farthestFromEntry = 0;
		throw event;
	}
	
	private void createOpenShortPositionEvent(Candle c) throws OpenPositionEvent {
		this.entryPoint =  c.close()-PRICE_LIMIT_DEV;
		OpenPositionEvent event = new OpenPositionEvent(Constants.ACTION_SELL,this.entryPoint);
		lg.debug("createOpenShortPositionEvent: throwing OpenPositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		
		openedTradeAction = Constants.ACTION_SELL;
		this.farthestFromEntry = 10000;
		throw event;
	}
	
	private void createCloseLongPositionEvent(Candle c, List<Order> orders, Contract contract) throws ClosePositionEvent {
		ClosePositionEvent event = new ClosePositionEvent(Constants.ACTION_SELL, c.close()-PRICE_LIMIT_DEV); //dev value is a tad lower than closing
		event.setOrderList(orders);
		lg.debug("createCloseLongPositionEvent");//: throwing ClosePositionEvent: " + event.getAction() + " at " + event.getLimitPrice());

		throw event;
	}
	
	private void createCloseShortPositionEvent(Candle c, List<Order> orders, Contract contract) throws ClosePositionEvent {
		ClosePositionEvent event = new ClosePositionEvent(Constants.ACTION_BUY, c.close()+PRICE_LIMIT_DEV); //dev value is a tad higher than closing
		event.setOrderList(orders);
		lg.debug("createCloseShortPositionEvent: throwing ClosePositionEvent: " + event.getAction() + " at " + event.getLimitPrice());
		throw event;
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
	
	public void resetStrategy(){
		this.entryPoint = 0;
		this.farthestFromEntry = 0;
		//this.pivotPointFound = false;
	}

	@Override
	public void evaluateEntry(ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, List<Order> orders, Contract contract,
			double latestClose5min) throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}
}
