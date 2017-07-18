package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.OpenPositionEvent;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.Types;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.ma.EMA;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.ExitSwitch;
import com.pancorp.tbroker.util.Constants;

/**
 * Strategy description:
 *
 * @author pankstep
 *
 */
public class MAPriceCrossoverStrategy implements IStrategy 
{
	private static Logger lg = LogManager.getLogger(MAPriceCrossoverStrategy.class); 
	private static final int PT_CACHE_SIZE = 5;
	//private ArrayDeque<Candle> patternCache;
	ArrayDeque<Integer> entrypt;
	ArrayList<Candle> cPattern;
	private boolean calibrated = false;
	private int emaFastPeriod = 9;
	private int emaSlowPeriod = 100;
	private static final double EMA_PIP_DIFF = 0.0002d;  //2 pips
	private static final double LARGE_BODY_PIP = 0.0003d;  //3 pips
	private static final double LIMIT_PRICE_DEV_PIP = 0.00005d;  //half a pip
	
	static final int ABOVE = 10;
	static final int BELOW = -10;
	static final int SOMEWHAT_ABOVE = 1;
	static final int SOMEWHAT_BELOW = -1;
	static final int ZERO = 0;
	static final int NONE = -99;
	
	//private EntrySwitch entrySwitch;
	//private ExitSwitch exitSwitch;
	
	private int maxCache = 120; // 10 hrs //240;  // 4-hours
	//private int medCache = 120;  // 2 hours
	//private int minCache = 100;   // 1 hour 40 mins

	EMA ema;
	
	public MAPriceCrossoverStrategy() { 
		ema = new EMA(this.emaFastPeriod, this.emaSlowPeriod);		
		entrypt = new ArrayDeque<>();
		cPattern = new ArrayList<>();
		//entrySwitch = new EntrySwitch();
		//exitSwitch = new ExitSwitch();
	}
	
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, List<Order> orders, Contract contract, double latestClose5sec) 
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		String fp = "evaluate: ";
		//lg.trace(fp);
	
		if(candles.size()<emaFastPeriod){
			lg.debug(fp + "Size less than " +emaFastPeriod + ": " + candles.size());
			fillFastEma(candles.peekFirst());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}
		
		double ema9 = ema.calculateFastClose(candles); 
		lg.debug(fp + "ema9: " + ema9);
		////candles.peek().emaFast(ema50);
		
		if(candles.size()<emaSlowPeriod){		
			lg.debug(fp + "Size less than " +emaSlowPeriod + ": " + candles.size());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}

		double ema100 = ema.calculateSlowClose(candles); // calc.calcForexEmaLongClose(emaSlowPeriod, candles);
		lg.debug(fp + "ema100: " + ema100);
		//candles.peek().emaSlow(ema100);	

		if(!calibrated){
			calibrated = true;
			lg.info(fp + "set calibrated=true");
		}
		
		if(orderPlaced){
			evaluateExit(candles, orders, contract, latestClose5sec);
		}
		else{
			evaluateEntry(candles, ema100);
		}
	}

	
	/**
	 * 
	 */
	private void evaluateEntry(ArrayDeque<Candle> candles, double ema100)  throws OpenPositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateEntry: ";
		//lg.trace(fp);

		Candle curr 		= candles.peekFirst();
		lg.info(fp + "curr.smaSlow100 " + curr.smaSlow() + ", curr ema100=" + ema100);

		try {			
			this.addToEntryPattern(curr, this.priceToEMASlow(curr));
		}
		//intercept propagation to set a limit price
		catch(OpenPositionEvent ope){
			double limitPrice = curr.close();
			/*
			String a = ope.getAction();
			lg.info(fp + "action: " + a);
			if(a.equals(Constants.ACTION_BUY))
				limitPrice = limitPrice + 0.00005d;
			else if(a.equals(Constants.ACTION_SELL))
				limitPrice = limitPrice - 0.00005d;
				*/
				
			ope.setLimitPrice(limitPrice);
			throw ope;
		}
	}
	
	private int priceToEMASlow(Candle c){	
		double ema = c.emaSlow();
		double emaUp = ema + EMA_PIP_DIFF;
		double emaDown = ema - EMA_PIP_DIFF;
		
		
		double open = c.open();
		double close = c.close();
		double low = c.low();
		double high = c.high();
		
		//all 4 prices above or equal ma + DIFF
		if(close >= emaUp &&
		open >= emaUp &&
		low >= emaUp && 
		high >= emaUp)			
			return ABOVE;
		//all 4 prices are below or equal ma - DIFF
		else if(close <=  emaDown &&
		open <= emaDown &&
		low <= emaDown && 
		high <= emaDown)		
			return BELOW;
		//dir up - crossover
		else if (open <= ema && close >=  ema)	
			return ZERO;
		//dir down - crossover
		else if (open >= ema && close <=  ema)
			return ZERO;
		else {
			if(c.getDirection()==Constants.DIR_WHITE){
				if(low <= ema && open > ema)	//low wick below ema, close (and high) > ema
					return SOMEWHAT_ABOVE;
				else if(high >= ema && close < ema)	//high wick above ema, close (and low ) < ema
					return SOMEWHAT_BELOW;
			}
			else { //none or black
				if(high >= ema && open < ema)	//high wick above ema, close (and low ) < ema
					return SOMEWHAT_BELOW;
				else if(low <= ema && close > ema)	//low wick below ema, close (and high) > ema
					return SOMEWHAT_ABOVE;
			}
		}
		
		return NONE;
	}

	public void addToEntryPattern(Candle c, int n) throws OpenPositionEvent {
		entrypt.push(n);
		cPattern.add(c);
		
		if(entrypt.size()>PT_CACHE_SIZE)
			entrypt.removeLast();
		if(cPattern.size()>PT_CACHE_SIZE)
			cPattern.remove(0);
		
		lg.debug("addToPattern: cache size: " + entrypt.size());
			
		if(entrypt.size()<PT_CACHE_SIZE)
			return;
			
		lg.debug("addToPattern:entrypt: " + entrypt);
			
		int curr = entrypt.pop();
		int prev = entrypt.peekFirst();
		int oldest = entrypt.peekLast();
		
		Candle prevC = cPattern.get(3);
			
		entrypt.push(curr); //put back
			
		if((oldest < 0 && prev > 0 && curr==ABOVE) || 
		   (oldest < 0 && (prev == 0 && prevC.getDirection()==Constants.DIR_WHITE && prevC.getBody_len()>=LARGE_BODY_PIP) && curr>0) //prev candle is on ema but is long and white
		){
				lg.debug("addToPattern: " + entrypt+": oldest < 0 && prev > 0 && curr >0: Crossover from below to above, throwing LONG event");
				throw new OpenPositionEvent(Constants.ACTION_BUY,0);
		}
		else if((oldest > 0 && prev < 0 && curr==BELOW) ||
				(oldest > 0 && (prev == 0 && prevC.getDirection()==Constants.DIR_BLACK && prevC.getBody_len()>=LARGE_BODY_PIP) && curr<0)  //prev candle is on ema but is long and black
		){
				lg.debug("addToPattern: " + entrypt+": oldest > 0 && prev < 0 && curr < 0: Crossover from above to below, throwing SHORT event");
				throw new OpenPositionEvent(Constants.ACTION_SELL,0);
		}		
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
	
	/**
	 * Position opened. Price either above or below EMA100. Keep  following the trend until it changes:
	 * for long position: highest candle, then 1st down, then close position after 2nd down
	 */
	public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5sec//, double ema100, double ema9
			)  throws ClosePositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateExit: ";
		lg.debug(fp);
		cPattern.add(candles.peekFirst());
		
		if(cPattern.size()>PT_CACHE_SIZE)
			cPattern.remove(0);
		lg.debug("addExit: cache size: " + cPattern.size());
		
		if(entrypt.size()<PT_CACHE_SIZE)
			return;
		
	/*	lg.debug(fp + "calibration checked");
		//temporary remove newly added candle to be able to check previous candle(s)
		
		candles.push(curr);
		int dir = curr.getDirection();
		lg.debug(fp + "current candle direction: " + dir);
		*/
		lg.debug(fp + "orderList: " + orders);
		lg.debug(fp + "contract: " + contract);
		
		String openedAction = (orders.get(0)).getAction();
		lg.debug(fp + "opened parent order action: " + openedAction);
		
		//take out from the stack (oldest first) and look at ema9
	/*	Candle five		= null;
		Candle four		= null;
		Candle three 	= null;
		Candle two      = null;
		Candle one      = null;*/
		
		double ema9five = 0;
		double ema9four = 0;
		double ema9three = 0;
		double ema9two = 0;
		double ema9one = 0;
		
		ClosePositionEvent cpe = null;
	
			
		switch(openedAction){
			case Constants.ACTION_BUY:
				if(ema9one<ema9two && ema9one < ema9five - EMA_PIP_DIFF){
					lg.info(fp + "ema9one "+ema9one +"<ema9two "+ema9two+" && ema9one "+ema9one+"< ema9five "+ema9five+" - EMA_PIP_DIFF " +EMA_PIP_DIFF+"), throwing ClosePositionEvent (SELL," + latestClose5sec+")");
					cpe = new ClosePositionEvent(Constants.ACTION_SELL,latestClose5sec);
					throw cpe;
				}
				break;
			case Constants.ACTION_SELL:
				if(ema9one>ema9two && ema9one > ema9five + EMA_PIP_DIFF){
					lg.info(fp + "ema9one "+ema9one +" > ema9two "+ema9two+" && ema9one "+ema9one+" > ema9five "+ema9five+" + EMA_PIP_DIFF " +EMA_PIP_DIFF+"), throwing ClosePositionEvent (BUY," + latestClose5sec+")");
					
					cpe = new ClosePositionEvent(Constants.ACTION_BUY,latestClose5sec);
					throw cpe;
				}
				break;
			case Constants.ACTION_SSHORT:
				if(ema9one>ema9two && ema9one > ema9five + EMA_PIP_DIFF){
					lg.info(fp + "ema9one "+ema9one +" > ema9two "+ema9two+" && ema9one "+ema9one+" > ema9five "+ema9five+" + EMA_PIP_DIFF " +EMA_PIP_DIFF+"), throwing ClosePositionEvent (BUY," + latestClose5sec+")");
					
					cpe = new ClosePositionEvent(Constants.ACTION_BUY,latestClose5sec);
					throw cpe;
				}
				break;
				default:
					lg.info("evaluateExit: unsupported action, returning..");
					return;
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
	public void evaluateEntry(ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evaluate(ArrayDeque<Candle> candles, boolean orderPlaced, boolean tradeOpened, List<Order> orders,
			Contract contract, double latestClose5min)
			throws NotEnoughDataException, OpenPositionEvent, ClosePositionEvent, Exception {
		// TODO Auto-generated method stub
		
	}

	//@Override
	//public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5min)
	//		throws ClosePositionEvent, NotEnoughDataException, Exception {
		// TODO Auto-generated method stub
		
	//}


}
