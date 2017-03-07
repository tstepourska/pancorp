package com.pancorp.tbroker.day;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.ts.test.data.DataFactory;
import com.pancorp.tbroker.util.Calculator;
import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.CandlePattern;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.pancorp.tbroker.model.IBar;
import com.pancorp.tbroker.model.PatternEnum;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

import samples.testbed.contracts.ContractSamples;
import samples.testbed.orders.OrderSamples;

public class Broker ///extends Thread 
{
	private static Logger lg = LogManager.getLogger(Broker.class); 
	private Contract contract;
	private int reqId;
	private DataFactory datafactory;
	private boolean working = true;
	private volatile ArrayDeque<IBar> ticks;
	ArrayDeque<Candle> candles;
	ArrayDeque<IBar> highs;
	ArrayDeque<IBar> lows;
	ArrayDeque<IBar> currentCandle = null;
	String tfUnit;
	long tfFactor = 1; //5 sec
	Calculator calc;
	int periodLong = 21;
	int periodShort = 9;
	int timeframeSize = 15;
	String timeframeUnit = Constants.TFU_MIN;
	
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
	public Broker(Contract c, int rid, DataFactory df, String tfu, BrokerManagerEWrapperImpl wr, BrokerManager mgr){
		mode = Constants.MODE_OPENING;
		//SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_DATE_CONTRACT_FOLLOWING);
		//Date dt = new Date(System.currentTimeMillis());
		//this.setName(c.symbol()+"_vlad_"+sdf.format(dt));
		contract = c;
		reqId = rid;
		datafactory = df;
		ticks = new ArrayDeque<>();
		candles = new ArrayDeque<>();
		highs = new ArrayDeque<>();
		lows = new ArrayDeque<>();
		
		this.tfUnit = tfu;
		
		switch(tfUnit){
		case Constants.TFU_MIN:
			tfFactor = Constants.MIN;
			break;
		case Constants.TFU_HOUR:
			tfFactor = Constants.HOUR;
			break;
		case Constants.TFU_DAY:
			tfFactor = Constants.DAY;
			break;
			default:
		}
		
		calc = new Calculator(periodShort, periodLong, timeframeSize, timeframeUnit, tfFactor);
		this.currentPattern = null;
		this.wrapper = wr;
		this.manager = mgr;
		
		//subscribe to data
		this.datafactory.subscribe(this.reqId, this);
	}
	
	/**
	 * Adds tick to a queue. Called by DataFactory
	 * @param t
	 */
	public synchronized void addTick(IBar t) throws InterruptedException, Exception {
		ticks.addFirst(t);
		recalculate(t);
	}
	
	/**
	 * 
	 */
	private void recalculate(IBar t) throws Exception {
		//translate latest tick into a candle
		
		//add to current candle tick queue
		currentCandle.addFirst(t);
		
		//check  the current candle queue size
		if(currentCandle.size()>=tfFactor){	//current candle queue is full			
			//creating new Candle from the  current candle queue
			double open = currentCandle.peekLast().open();
			double high = calc.maxHigh(currentCandle);
			double low = calc.minLow(currentCandle);
			long volume = calc.sumVolume(currentCandle);
			long time = currentCandle.peekLast().time();
			
			Candle c = new Candle(time,open,t.close(),high,low,t.wap(),volume,t.count());
			latestClose = t.close();
			
			//adding it to a day candle queue 
			candles.push(c);
			
			//recalculate all indicators and patterns, just created candle is passed separately
			recalculateIndicators(c, this.periodShort, this.periodLong);
			
			recalculateCandlePattern(c);

		}
		else {
			; //do nothing?
		}
	}
	
	/**
	 * 
	 * @param newCandle	--candle which just has been created(it has already been added to the candles queue
	 */
	private void recalculateIndicators(Candle newCandle, int maShortPeriod, int maLongPeriod) throws Exception {
		//passing new candle separately to each method below, but dont forget - 
		// it has been already added to the candle cache
		newCandle.emaShort(calc.EMAclose(maShortPeriod, this.candles, newCandle));
		newCandle.emaLong(calc.EMAclose(maLongPeriod, this.candles, newCandle));
		//this.highestHigh = calc.maxHigh(this.candles);
		//this.lowestLow   = calc.minLow(this.candles);

		//newCandle.slope(calc.linearRegressionSlope(this.candles, newCandle));
		//this.slope = newCandle.slope();
		
		//to determine trend
		newCandle.trueRange(calc.TrueRange(Globals.ATR_PERIOD, this.candles));
		newCandle.ATR(calc.AverageTrueRange(Globals.ATR_PERIOD, this.candles));
		
		//newCandle.plusDMI(calc.plusDMI(Globals.ATR_PERIOD, this.candles, newCandle));
		//newCandle.minusDMI(calc.minusDMI(Globals.ATR_PERIOD, this.candles, newCandle));
		//newCandle.adxFactor(calc.adxFactor(Globals.ATR_PERIOD, this.candles, newCandle));
		newCandle.adx(calc.ADX(Globals.ATR_PERIOD, this.candles));
		//end of determine trend
		
		//calc.support(lows);
		//calc.resistance(highs);
		
		//typical period is 14
		//newCandle.williamsR(calc.WilliamsR(latestClose, highestHigh, lowestLow));
		//this.williamsR = newCandle.williamsR();
	}
	
	/**
	 * Recalculates and checks current candle properties, patterns and confirmations, 
	 * makes trading decisions
	 * 
	 * @param newCandle
	 */
	private void recalculateCandlePattern(Candle newCandle){
		//temporary remove newly added candle to be able to check previous candle(s)
		Candle tmp = this.candles.pop();
		
		if(currentPattern!=null){
			//PatternEnum pat = currentPattern.getPatternName();
			
			
	/*		HANGMAN(1),
			HAMMER(1),
			TWEEZER_BOTTOM(2),
			TWEEZER_TOP(2),
			INVERTED_HAMMER(1),
			SHOOTING_STAR(1),
			BULLISH_PIERCING(2),
			BULLISH_ENGULFING(2),

			DARK_CLOUD_COVER(2),
			BEARISH_ENGULFING(2),

			EVENING_DOJI_STAR(3),
			EVENING_STAR_REVERSAL(3),
			
			MORNING_STAR_REVERSAL
			*/
					
			if(currentPattern.isCompleted()){
				int dir = newCandle.getDirection();
				//pattern completed, checking new Candle for confirmation
				if(dir==currentPattern.getConfirmationDirection() && 					// direction confirms the reversal
				   newCandle.open() < candles.peek().close() && newCandle.open() < candles.peek().open()		//confirmation candle opens below pattern candle body
				  ){	
					// PATTERN COMPLETED AND CONFIRMED, OPEN POSITION!!!
					//tell manager to lock the right to place order; if true, proceed
					if(manager.setOrderPlaced(true)){	
						double lmtPrice = 0;
						if(dir>0)
							lmtPrice = newCandle.close();
						else
							lmtPrice = newCandle.open();
						
						//TODO check quantity against amount in the account
						try {
						//placeOrder(currentPattern.getAction(), lmtPrice, Constants.DEFAULT_QUANTITY);	
						PlaceOrderBracketT placeOrderThread = new PlaceOrderBracketT(wrapper.getCurrentOrderId(), 
								this.contract, 					// contract
								lmtPrice, 						// limit price
								Constants.DEFAULT_QUANTITY, 	//quantity of shares, default 100
								currentPattern.getAction(),
								wrapper,
								manager
								);
						placeOrderThread.start();
						currentPattern = null;
						//wait for order to be filled - callback to wrapper, then change mode of operation
						}
						catch(Exception e){
							Utils.logError(lg, e);
						}
					}
				}
				else {
					//pattern is not complete, reset
					if(currentPattern.getConfirmationCount()>=Globals.MAX_PATTERN_CONFIRMATION_COUNT)
						currentPattern = null;
					else
						currentPattern.incrementConfirmationCount();
				}
			}
			else {
				//pattern is not completed, checking new Candle for pattern continuation
			}
		}
		else { //no pattern existed, check whether new candle is part of a new pattern

		if(newCandle.adx() > Globals.ADX_WEAK_TREND_THRESHOLD){		//strong trend
			//trending up, 
			//check for reversal pattern, 
			//wait for pattern to form completely, then it is strongly recommended to wait for a 
			//confirmation candle, then enter a position after a confirmation candle, 
			//for long position stop loss below open price of confirmation candle
			//for short position stop loss above open price of confirmation candle
			if(newCandle.plusDMI()>0){					
				calc.calcAvgLengths(Globals.MAX_TREND_CANDLES, this.candles);

				//doji
				if(newCandle.getDirection()==Constants.DIR_NONE){
					
					//if high wave (doji with long wicks)
					if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
					   newCandle.getLower_shadow_len()>newCandle.getAvgLowerShadowLen()*Globals.CANDLE_TYPE_FACTOR 
					  ){
						
					}	
					//tombstone doji, extremely bearish if occuring at resistance
					else if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
							newCandle.getLower_shadow_len()==0
						){
						
					}
					//harami cross (2nd is doji)
					else if(candles.peek().getDirection()==Constants.DIR_WHITE){
						
					}
					// abandoned baby (first has  considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
					else if(newCandle.open()>candles.peek().close()+newCandle.getAvgBodyLen()*1/Globals.CANDLE_TYPE_FACTOR){
						
					}
				}
				//checking on small body length (spinning top), which is a part of several patterns
				else if(newCandle.getBody_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgBodyLen()){
					//hanging man: body len is no more than 1/3 of average over the previous MAX_TREND_CANDLES
					//	lower shadow is more than 3 average
					if((newCandle.getUpper_shadow_len()==0 || newCandle.getUpper_shadow_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgUpperShadowLen()) && 
							newCandle.getLower_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgLowerShadowLen()
							){
						//check for the gap between hanging man and confirmation candle
							this.currentPattern = new CandlePattern();
							currentPattern.setPatternName(PatternEnum.HANGMAN);
							//currentPattern.addCandleToPattern(newCandle);
							currentPattern.setCompleted(true, Constants.ACTION_SELL);
							currentPattern.setConfirmationDirection(Constants.DIR_BLACK); //down, opposite to trend 

					}
					//shooting star (weaker than hanging man)
					else if((newCandle.getLower_shadow_len()==0 || newCandle.getLower_shadow_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgLowerShadowLen()) && 
					newCandle.getUpper_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgUpperShadowLen()
					){
						//check for the gap between shooting star and confirmation candle
						this.currentPattern = new CandlePattern();
						currentPattern.setPatternName(PatternEnum.SHOOTING_STAR);
						//currentPattern.addCandleToPattern(newCandle);
						currentPattern.setCompleted(true, Constants.ACTION_SELL);
						currentPattern.setConfirmationDirection(Constants.DIR_BLACK); //down, opposite to trend 
					}
					//bearish harami (not reliable, use as warning for current position)	
					/*else if(this.candles.peek().getDirection()==Constants.DIR_WHITE && 
							newCandle.getDirection()==Constants.DIR_BLACK &&
							this.candles.peek().getBody_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getBody_len() &&
							this.candles.peek().close() > newCandle.open() + newCandle.getBody_len() &&
							this.candles.peek().open() < newCandle.close() - newCandle.getBody_len() 
							){
						//no confirmation needed for warning
						this.currentPattern = new CandlePattern();
						currentPattern.setPatternName(PatternEnum.);
						//currentPattern.addCandleToPattern(newCandle);
						currentPattern.setCompleted(true, Constants.ACTION_SELL);
						currentPattern.setConfirmationDirection(Constants.DIR_BLACK); //down, opposite to trend 
					}*/
					//evening star (first has  considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
					else if(this.candles.peek().getDirection()==Constants.DIR_WHITE && 
							this.candles.peek().close() < newCandle.open() - newCandle.getBody_len() &&
							this.candles.peek().open() < newCandle.close() - newCandle.getBody_len() 
						){
						
					}
				}
				//checking on long body length, which is a part of several patterns			
				else if(newCandle.getBody_len() > (Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgBodyLen()){
					//bearish engulfing pattern (stronger if 2nd candle covers body of more than one candle)
				}
				
				//dark cloud cover
				
				
				
				//tweezers - same high wick value - the longer shadows the stronger
				
			}
			else if (newCandle.minusDMI()>0){
				//trending down
				//doji
				//hammer
				
				//innverted hammer (weeker than hammer)
				
				//high wave (doji with long wicks)
				
				//bullish engulfing pattern (stronger if 2nd candle covers body of moer than one candle)
				
				//piercing line (oppsite of dark cloud cover)
				
				//bullish harami (not reliable, use as warning for current position)
				//harami cross (2nd is doji)
				
				//morning star / abandoned baby (first has  considerable body, gap between middle candle,  big coverage of 1st candle by 3rd one)
				
				//tweezers - same low wick value - the longer shadows the stronger
			}
		}
		else {		
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
		}
		}
		
		//don't forget to put pack popped candle!
		this.candles.push(tmp);
	}
	
/*	@Override
	public void run(){
		lg.info("Started thread for " + this.contract.symbol());
		//String symbol = this.contract.symbol();
		
		//subscribe to data
		datafactory.subscribe(reqId, this);
		try {Thread.sleep(Constants.SLEEP_WAIT_FOR_BAR);}catch(InterruptedException e){}
		
		while(working){
		}
	}*/

	
/*	public static void main(String[] args){
		Contract c = new Contract();
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
			
		}
	}*/
}
