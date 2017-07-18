package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.OpenPositionEvent;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.Types;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.ma.EMA;
import com.pancorp.tbroker.indicators.ma.SMA;
import com.pancorp.tbroker.model.Candle;
//import com.pancorp.tbroker.model.EntrySwitch;
//import com.pancorp.tbroker.model.ExitSwitch;
import com.pancorp.tbroker.util.Constants;

/**
 * Strategy description:
 *
 * @author pankstep
 *
 */
public class SMAEMACrossoverStrategy implements IStrategy 
{
	private static Logger lg = LogManager.getLogger(SMAEMACrossoverStrategy.class); 
	
//private ArrayDeque<Candle> patternCache;
	private boolean calibrated = false;
	private int smaFastPeriod = 3;
	private int emaSlowPeriod = 11;
	private static final double EMA_PIP_DIFF = 0.0002;  //2 pips
	
	private EntrySwitch entrySwitch;
	//private ExitSwitch exitSwitch;
	
	private int maxCache = 120; // 10 hrs //240;  // 4-hours
	//private int medCache = 120;  // 2 hours
	//private int minCache = 100;   // 1 hour 40 mins

	EMA ema;
	SMA sma;
	
	public SMAEMACrossoverStrategy() { 
		ema = new EMA(0, this.emaSlowPeriod);	
		sma = new SMA();
		entrySwitch = new EntrySwitch();
		//exitSwitch = new ExitSwitch();
	}

	
	/**
	 * 
	 */
	public void evaluateEntry(ArrayDeque<Candle> candles)  throws OpenPositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateEntry: ";
		//lg.trace(fp);
	
		if(candles.size()<smaFastPeriod){
			lg.debug(fp + "Size less than " +smaFastPeriod + ": " + candles.size());
			fillFastSma(candles.peekFirst());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}
		
		double sma3 = sma.calculateClose(candles, this.smaFastPeriod); //calc.calcForexEmaShortClose(emaFastPeriod, candles);
		lg.debug(fp + "sma3: " + sma3);
		candles.peek().smaFast(sma3);
		
		if(candles.size()<emaSlowPeriod){		
			lg.debug(fp + "Size less than " +emaSlowPeriod + ": " + candles.size());
			fillSlowEma(candles.peekFirst());
			throw new NotEnoughDataException();
		}

		double ema11 = ema.calculateSlowClose(candles); // calc.calcForexEmaLongClose(emaSlowPeriod, candles);
		lg.debug(fp + "ema11: " + ema11);
		
		if(!calibrated){
			calibrated = true;
			lg.info(fp + "set calibrated=true");
		}

		Candle curr 		= candles.peekFirst();
		lg.info("curr.smaSlow11 " + curr.smaSlow());
		//////////
		lg.debug("sma3: "+sma3+",ema11: " + ema11 + ", open: " + curr.open() + ", close: " + curr.close() + ", high: " + curr.high() + ", low: " + curr.low());
		
	//	try {
			if(sma3==ema11){
				
			}
		/*}
		//intercept propagation to set a limit price
		catch(OpenPositionEvent ope){
			ope.setLimitPrice(curr.close());
			throw ope;
		}*/
	}
	
	private void fillFastSma(Candle c){
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
	public void evaluateExit(ArrayDeque<Candle> candles, List<Order> orders, Contract contract, double latestClose5sec)  throws ClosePositionEvent, NotEnoughDataException, Exception {
		String fp = "evaluateExit: ";
		lg.debug(fp);
		if(!calibrated){
			if(candles.size()>=this.emaSlowPeriod){
				calibrated = true;
				lg.info(fp + "set calibrated=true");
			}
		}
		//lg.debug(fp + "calibration checked");
				
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
	
	private class EntrySwitch{
		static final int CACHE_SIZE = 5;
		
		public static final int ABOVE = 1;
		public static final int BELOW = -1;
		public static final int ZERO = 0;
		public static final int NONE = 99;
		
		public int src2 = NONE;
		public int src1 = NONE;
		public int mid = NONE;
		public int  dest1 = NONE;
		public int  dest2 = NONE;
		
		ArrayDeque<Integer> p;

		public EntrySwitch() {
			p = new ArrayDeque<>();
		}
		
		public void add(int n) throws OpenPositionEvent {
			int m = NONE;
			int cnt = 0;
			p.push(n);
			if(p.size()>CACHE_SIZE)
				p.removeLast();
			lg.debug("add: cache size: " + p.size());
			
			//iterate from the tail
			Iterator<Integer> it = p.descendingIterator();
			
			while(it.hasNext()){
				cnt++;
				m = it.next();
				
				switch(cnt){
				//earliest candle
				case 1:
					if(m>0){
						//above
						//start or the crossover from above to below
						src2 = ABOVE;
					}
					else if(m==0){
						//crossing
						src2 = ZERO;
					}
					else{//m<0
						//below
						//start or the crossover from below to above
						src2 = BELOW;
					}
					break;
				case 2:
					if(m>0){
						//above
						src1 = ABOVE;
					}
					else if(m==0){
						//crossing
						src1 = ZERO;
					}
					else{//m<0
						//below
						src1 = BELOW;
					}
					break;
				case 3:
					if(m>0){
						//above
						mid = ABOVE;
					}
					else if(m==0){
						//crossing at the middle 
						//throw event
						mid = ZERO;
					}
					else{//m<0
						//below
						mid = BELOW;
					}
					break;
				case 4:
					if(m>0){
						//above
						dest1 = ABOVE;
					}
					else if(m==0){
						//crossing
						dest1 = ZERO;
					}
					else{//m<0
						//below
						dest1 = BELOW;
					}
					break;
				case 5:
					if(m>0){
						//above
						dest2 = ABOVE;
					}
					else if(m==0){
						//crossing
						dest2 = ZERO;
					}
					else{//m<0
						//below
						dest2 = BELOW;
					}
					break;
					default:
				}
			}
			lg.debug("size="+p.size() + ",src2=" + src2 + ",src1="+src1+",mid="+mid+",dest1="+dest1+",dest2="+dest2);

			if(p.size()<CACHE_SIZE)
				return;
			
			//confirmed crossing from above to below
			if(src2==ABOVE && src1==ZERO && mid==ZERO && dest1==BELOW && dest2==BELOW){
				lg.debug("Confirmed crossover from above to below, resetting cache and throwing SHORT event");
				this.reset();
				throw new OpenPositionEvent("SELL",0);
			}
			else if(src2==ABOVE && src1==ABOVE && mid==ZERO && dest1==BELOW && dest2==BELOW){
				lg.debug("Confirmed crossover from above to below, resetting cache and throwing SHORT event");
				this.reset();
				throw new OpenPositionEvent("SELL",0);
			}	
			else if(src2==ABOVE && src1==ABOVE && mid==ZERO && dest1==ZERO && dest2==BELOW){
				lg.debug("Unconfirmed crossover from above to below, waiting for next candle");
			}
			//unconfirmed crossing from above to below
			else if(src2==ABOVE && src1==ABOVE && mid==ABOVE && dest1==ZERO && dest2==BELOW){
				lg.debug("Unconfirmed crossover from above to below, waiting for next candle");
			}
			else if(src2==ABOVE && src1==ABOVE && mid==ABOVE && dest1==ZERO && dest2==ZERO){
				lg.debug("Unconfirmed crossover from above to below, waiting for next candle");
			}
			//unconfirmed crossing from above to below
			else if(src2==ABOVE && src1==ABOVE && mid==ABOVE && dest1==ABOVE&& dest2==ZERO){
				lg.debug("Unconfirmed crossover from above to below, waiting for next candle");
			}
			//confirmed
			else if(src2==BELOW && src1==ZERO && mid==ZERO && dest1==ABOVE && dest2==ABOVE){
				lg.debug("Confirmed crossover from below to above, resetting cache and throwing LONG event");
				this.reset();
				throw new OpenPositionEvent("BUY",0);
			}
			else if(src2==BELOW && src1==BELOW && mid==ZERO && dest1==ZERO && dest2==ABOVE){
				lg.debug("Unconfirmed crossover from below to above, waiting for next candle");
			}
			//confirmed crossing from below to above
			else if(src2==BELOW && src1==BELOW && mid==ZERO && dest1==ABOVE && dest2==ABOVE){
				lg.debug("Confirmed crossover from below to above, resetting cache and throwing LONG event");
				this.reset();
				throw new OpenPositionEvent("BUY",0);
			}
			//unconfirmed crossing from below to above
			else if(src2==BELOW && src1==BELOW && mid==BELOW && dest1==ZERO && dest2==ABOVE){
				lg.debug("Unconfirmed crossover from below to above, waiting for next candle");
			}
			//unconfirmed crossing from below to above
			else if(src2==BELOW && src1==BELOW && mid==BELOW && dest1==BELOW && dest2==ZERO){
				lg.debug("Unconfirmed crossover from below to above, waiting for next candle");
			}
			else if(src2==BELOW && src1==BELOW && mid==BELOW && dest1==ZERO && dest2==ZERO){
				lg.debug("Unconfirmed crossover from below to above, waiting for next candle");
			}
			//unconfirmed crossing from to below to above
			else if(src2==BELOW && src1==BELOW && mid==BELOW && dest1==BELOW&& dest2==ZERO){
				lg.debug("Unconfirmed crossover from below to above, waiting for next candle");
			}
			else {
				//no match for crossing pattern or expired pattern
				lg.debug("No match");
				//reset();
			}
		}	//end of method

		public void reset(){
			p.clear();
			
			//cross = CROSS_NONE;
			src2 = NONE;
			src1 = NONE;
			mid = NONE;
			dest1 = NONE;
			dest2= NONE;
		}
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
