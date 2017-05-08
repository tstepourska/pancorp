package com.pancorp.tbroker.day;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

import com.pancorp.tbroker.strategy.*;

import com.pancorp.tbroker.event.*;
import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;

import com.pancorp.tbroker.model.CandlePattern;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.pancorp.tbroker.model.*;
import com.pancorp.tbroker.util.Calculator;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

//import samples.testbed.contracts.ContractSamples;
//import samples.testbed.orders.OrderSamples;

public class Broker extends Thread 
{
	private Logger lg = LogManager.getLogger(Broker.class); 
	
	private Contract contract;
	private int reqId;
	private DataFactory datafactory;
	private boolean working = true;
	//private volatile ArrayDeque<IBar> ticks;
	ArrayDeque<Candle> candles;
	ArrayDeque<IBar> highs;
	ArrayDeque<IBar> lows;
	ArrayDeque<IBar> currentCandle = null;
	String tfUnit;
	long tfFactor = 1; //5 sec
	Calculator calc;
	int periodLong = 21;
	int periodShort = 9;
	int timeframeSize = 5;
	String timeframeUnit = Constants.TFU_MIN;
	
	IStrategy strategy;
	
	private int mode;
	
	//double sma=0;
	//double ema=0;
	double slope =0;
	double latestClose = 0;
	double highestHigh=0, lowestLow=0;
	double williamsR = 0;
	//double macd =0;
	//boolean trend = false;
	//boolean orderPlaced = false;
	
	private CandlePattern currentPattern;
	
	private BrokerManagerEWrapperImpl wrapper;
	private BrokerManager manager;
	
	/**
	 * Creates an instance of a stock monitoring thread
	 * 
	 * @param c
	 * @param df
	 * @param tfu
	 */
	//public Broker(){
	public Broker(Contract c, int rid, DataFactory df, String tfu, BrokerManagerEWrapperImpl wr, BrokerManager mgr) throws Exception {
		mode = Constants.MODE_OPENING;
		//SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_DATE_CONTRACT_FOLLOWING);
		//Date dt = new Date(System.currentTimeMillis());
		//this.setName(c.symbol()+"_vlad_"+sdf.format(dt));
		contract = c;
		reqId = rid;
		datafactory = df;
		//ticks = new ArrayDeque<>();*/
		candles = new ArrayDeque<>();
		//highs = new ArrayDeque<>();
		//lows = new ArrayDeque<>();
		currentCandle = new ArrayDeque<>();
		
		this.tfUnit = tfu;
		
		switch(tfUnit){
		case Constants.TFU_MIN:
			tfFactor = Constants.MIN;
			lg.info("tfFactor: " + tfFactor);
			break;
		case Constants.TFU_HOUR:
			tfFactor = Constants.HOUR;
			break;
		case Constants.TFU_DAY:
			tfFactor = Constants.DAY;
			break;
			default:
		}
		
		calc = new Calculator(); //periodShort, periodLong, timeframeSize, timeframeUnit, tfFactor);
		this.currentPattern = null;
		this.wrapper = wr;
		this.manager = mgr;

		this.strategy = new StrategyTrendingCandlePattern(calc);
		//subscribe to data
		this.datafactory.subscribe(this.reqId);//, new ArrayDeque<Bar>());//, this);
		
		//testing only TODO to comment
		//this.datafactory.fillUpTestCache(this.reqId);
		
		lg.info("Broker " + this.reqId + " created");
	}
	
	//}
	
	public void run(){
		Bar tick = null;
		Candle base = null;
		boolean first = true;
		boolean toRemove = true;
		///////////////
		// TODO for test only, to comment out!!!
		//ArrayDeque<Bar> cache = setupDummyTestCache();
		//if(lg.isTraceEnabled())
		//	lg.trace("created tick cache of size: " + cache.size());
		////////////////////////////////////////
		
		//instead of goto, acts same way as Exception
		SkipEvent skip = new SkipEvent();
		
		while(working){
			//reset
			tick = null;
			
			try {
				///////////////
				// TODO for test only, to comment out!!!
				/*if(cache.isEmpty()){
					lg.info("tick cache is empty");
					break;
				}			
				tick = cache.removeLast();
				if(lg.isTraceEnabled())
					lg.trace("tick cache size: " + cache.size());
					*/
				/////end for test only ////////////////

				if(datafactory.barCache.get(this.reqId).isEmpty()){
					throw skip;
				}
				
				tick = datafactory.barCache.get(this.reqId).removeLast();
				if(tick==null){
					throw skip;
				}
				else {
					//if(lg.isTraceEnabled())
					//	lg.trace("recalculating...");
					/*if(first){
						//create a dummy first candle from a single tick to initialize all averages
						//do not push it to the currentCandle cache
						base = initTick(tick);
						this.candles.push(base);
						//recalculateIndicators(base);
						first = false;
					}
					else if(toRemove){
						recalculate(tick);
						if(this.candles.size()==2){
						this.candles.removeLast();
						toRemove = false;
						if(lg.isTraceEnabled())
							lg.trace("Removed base tick, size: " + this.candles.size());
						}				
					}
					else {*/
						recalculate(tick);
					//}
				}
			}
			catch(SkipEvent se){}
			catch(Exception e){Utils.logError(lg, e);}
			finally{
				try{
					//if(calc.calibrated)
						Thread.sleep(1500);
					//else
					//	Thread.sleep(100);
				}catch(InterruptedException ie){}
			}
		}
	}
	
	private Candle initTick(Bar t){
		Candle c = new Candle(t.time(),t.open(),t.close(),t.high(),t.low(),t.wap(),t.volume(),t.count());
		if(lg.isDebugEnabled())
			lg.debug("initTick: initialized the very first tick as a newCandle: " + c);

		return c;
	}
	
	//for test only
 	private ArrayDeque<Bar> __setupDummyTestCache(){
		String timeframeUnit = Constants.TFU_MIN;
		int timeframeSize = 5;
		int cacheSize = Globals.TICK_CACHE_SIZE;
		double basePrice = 22.67;
		//HashMap<Integer,ArrayDeque<Bar>> map = new HashMap<>();
		ArrayDeque<Bar> cache = Utils.fillTickCache(cacheSize, timeframeUnit, timeframeSize, basePrice);
		//datafactory.barCache.put(new Integer(7610), cache);
		lg.debug("Created test cache");
		return cache;
	}
	
	/**
	 * 
	 */
	private void recalculate(IBar t) throws Exception {
		//translate latest tick into a candle
		String fp = "recalculate: ";
		
		lg.debug(fp + "tfFactor: " + tfFactor);
		
		//try {
		//add to current candle tick queue			
		currentCandle.push(t);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "added tick to current candle, size: " + currentCandle.size());
		//check  the current candle queue size
		if(currentCandle.size()<tfFactor){	//current candle cache is not full
			return;
		}
		
		//current candle cache is full	
		if(lg.isTraceEnabled())
			lg.trace(fp + "current candle cache is full, creating new candle..");

		//creating new Candle from the  current candle queue
		double open = currentCandle.peekLast().open();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "open: " + open);
		
		double high = calc.maxHigh(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "high: " + high);
		
		double low = calc.minLow(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "low: " + low);
		
		long volume = calc.sumVolume(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "volume: " + volume);
		long time = currentCandle.peekLast().time();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "time: " + time);
			
		Candle c = new Candle(time,open,t.close(),high,low,t.wap(),volume,t.count());
		latestClose = t.close();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "latestClose: " + latestClose);
		
		/*if(lg.isDebugEnabled()){
			lg.debug(fp + "newCandle: " + c);
		}*/
			
		//adding it to a day candle queue 
		candles.push(c);
		if(lg.isDebugEnabled())
			lg.debug(fp + "pushed new candle to candles cache, size: " + this.candles.size());
		
		//clear current candle cache to start a new candle
		this.currentCandle.clear();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "cleared current candle cache to start new candle");
		
		if(candles.size()>Globals.NUMBER_OF_PERIODS){
			if(lg.isDebugEnabled())
				lg.debug(fp + "removing older extra candle");
			candles.removeLast();
			if(lg.isDebugEnabled())
				lg.debug(fp + "cache size: " + candles.size());
		}	
			
	/*	try{
		//recalculate all indicators and patterns, just created candle is passed separately
			recalculateIndicators(c);//, this.periodShort, this.periodLong);		
		}
		catch(NotEnoughDataException e){
			lg.error("Caught NotEnoughDataException e: " + e.getMessage());
			return;
		}*/
		
		//if(!calc.calibrated)
		//	return;
		
		try {
			//TODO throw event only if calc.calibrated = true;
			currentPattern = strategy.evaluate(candles, currentPattern);			
		}
		catch(OpenPositionEvent ope){
			//IB allows up to 15 active orders per contract per side per account.
			//now we allow 1 at a time
			lg.trace("Caught OpenPositionEvent");
				// PATTERN COMPLETED AND CONFIRMED, OPEN POSITION!!!
				//tell manager to lock the right to place order; if true, proceed
				if(manager.setOrderPlaced(true)){	
					lg.trace("manager locked the right to place order");
					//switch mode of operation, on callback, if not filled, switch back to OPENING
					this.mode = Constants.MODE_CLOSING;
					double lmtPrice = ope.getLimitPrice();
					lg.debug("limit price: " + lmtPrice);
					//TODO check quantity against amount in the account
					try {
					//placeOrder(currentPattern.getAction(), lmtPrice, Constants.DEFAULT_QUANTITY);	
					PlaceOrderBracketT placeOrderThread = new PlaceOrderBracketT(//wrapper.getCurrentOrderId(), 
							this.contract, 					// contract
							lmtPrice, 						// limit price
							Constants.DEFAULT_QUANTITY, 	//quantity of shares, default 100
							currentPattern.getAction(),
							wrapper,
							manager,
							this.datafactory
							);
					placeOrderThread.start();
					lg.trace("Place order thread started");
					
					//reset current pattern
					currentPattern = null;
					lg.trace("Current pattern reset");
					//wait for order to be filled - see callback to wrapper
					}
					catch(Exception e){
						Utils.logError(lg, e);
					}
				}
		}
		catch(ClosePositionEvent cpe){
			lg.trace("Caught ClosePositionEvent");
			this.mode = Constants.MODE_OPENING;
			
		}
		/*catch(TradingEvent te){
				
		}*/
		catch(Exception e){
			lg.error("Error evaluating candle pattern: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param newCandle	--candle which just has been created(it has already been added to the candles queue
	 */
	private void _recalculateIndicators(Candle newCandle) throws NotEnoughDataException, Exception {
		String fp = "recalculateIndicators: ";

		//passing new candle separately to each method below, but dont forget - 
		// it has been already added to the candle cache
		
		//newCandle.emaShort(calc.EMAclose(maShortPeriod, this.candles, newCandle));
		//newCandle.emaLong(calc.EMAclose(maLongPeriod, this.candles, newCandle));
		
		//this.highestHigh = calc.maxHigh(this.candles);
		//this.lowestLow   = calc.minLow(this.candles);

		//newCandle.slope(calc.linearRegressionSlope(this.candles, newCandle));
		//this.slope = newCandle.slope();
		
		//to determine trend
		calc.calcADX_DMI(Globals.NUMBER_OF_PERIODS, candles);
		//end of determine trend
		
		//calc.support(lows);
		//calc.resistance(highs);
		
		//typical period is 14
		//newCandle.williamsR(calc.WilliamsR(latestClose, highestHigh, lowestLow));
		//this.williamsR = newCandle.williamsR();
		
		//lg.info(fp + "indicators: trueRange: " + newCandle.trueRange() + ", ATR: " + newCandle.ATR() + ", ADX: " + newCandle.adx());
		//lg.info(fp + "indicators: ADX: " + newCandle.adx() + ", +DM: " + newCandle.plusDM() + ", -DM: " + newCandle.minusDM());
		//lg.info(fp + "indicators: DX: " + newCandle.dx() + ", +DI ema: " + newCandle.plusDIEma() + ", -DI ema: " + newCandle.plusDIEma());
		lg.info(fp + "newCandle: " + newCandle);
	}


	/**
	 * @return the working
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * @param working the working to set
	 */
	public void setWorking(boolean working) {
		this.working = working;
	}

	
	public static void main(String[] args){
	/*	Contract c = new Contract();
		c.symbol("AAPL");
		int rid = 459 + 100000;
		DataFactory df = null;
		String tfu = Constants.TFU_MIN;
		try {
			df = new DataFactory();
			Broker t = new Broker( c,rid, df, tfu);
			t.start();
		}
		catch(Exception e){
			
		}*/
		//Broker b = new Broker();
		//b.run();
	}
}