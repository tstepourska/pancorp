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
		
		try {
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
		catch(EventOpenPosition eop){
			// PATTERN COMPLETED AND CONFIRMED, OPEN POSITION!!!
			//tell manager to lock the right to place order; if true, proceed
			if(manager.setOrderPlaced(true)){	
				//switch mode of operation, on callback, if not filled, switch back to OPENING
				this.mode = Constants.MODE_CLOSING;
			
				
				//TODO check quantity against amount in the account
				try {
				//placeOrder(currentPattern.getAction(), lmtPrice, Constants.DEFAULT_QUANTITY);	
				PlaceOrderBracketT placeOrderThread = new PlaceOrderBracketT(wrapper.getCurrentOrderId(), 
						this.contract, 					// contract
						eop.getLimitPrice(), 						// limit price
						Constants.DEFAULT_QUANTITY, 	//quantity of shares, default 100
						eop.getAction(),
						wrapper,
						manager
						);
				placeOrderThread.start();
				//reset current pattern
				currentPattern = null;
				//wait for order to be filled - see callback to wrapper
				}
				catch(Exception e){
					Utils.logError(lg, e);
				}
			}
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
	private void recalculateCandlePattern(Candle newCandle) throws EventOpenPosition {
		//temporary remove newly added candle to be able to check previous candle(s)
		Candle tmp = this.candles.pop();
		
		int triggerAction = Constants.TRIGGER_ACTION_NONE;   //0 - create pattern and require confirmation; 1 - GO and place order!
		PatternEnum pat = null;
		int dir = newCandle.getDirection();
		//pattern existed; looking for a confirmation candle 
		if(currentPattern!=null){
			if(currentPattern.isCompleted() && dir!=currentPattern.getConfirmationDirection()){ //direction does NOT confirms the reversal
				//confirmation count exausted, reset
				if(currentPattern.getConfirmationCount()>=Globals.MAX_PATTERN_CONFIRMATION_COUNT)
					currentPattern = null;
				else
					currentPattern.incrementConfirmationCount();
				
			return;
		}
			
		//////////////////////////////
		double lmtPrice = 0;
		//currentPattern is Completed, direction OK, checking new Candle for confirmation
		if(dir>0){	//direction up, for entering long position or buy
			if(newCandle.open() > candles.peek().close() && newCandle.open()> candles.peek().open()){		//confirmation candle opens above pattern candle body
				lmtPrice = newCandle.close();
				triggerAction = Constants.TRIGGER_ACTION_OPEN_POSITION;
				throw new EventOpenPosition(Constants.ACTION_BUY, lmtPrice);
			}
		}
		else if(dir < 0){   //direction down, for entering short or sell
			if(newCandle.open() < candles.peek().close() && newCandle.open() < candles.peek().open())	{	//confirmation candle opens below pattern candle body
				lmtPrice = newCandle.open();
				triggerAction = Constants.TRIGGER_ACTION_OPEN_POSITION;
				throw new EventOpenPosition(Constants.ACTION_SELL, lmtPrice);
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		 //no pattern existed, check whether new candle is part of a new pattern
		calc.calcAvgLengths(Globals.MAX_TREND_CANDLES, this.candles);
		double currBody = newCandle.getBody_len();
		double deviation = currBody*Globals.DEVIATION_FACTOR;

		if(newCandle.adx() < Globals.ADX_WEAK_TREND_THRESHOLD){		//weak trend
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
			return;
		}
			
		//trending up, 
		//check for reversal pattern, 
		//wait for pattern to form completely, then it is strongly recommended to wait for a 
		//confirmation candle, then enter a position after a confirmation candle, 
		//for long position stop loss below open price of confirmation candle
		//for short position stop loss above open price of confirmation candle
		if(newCandle.plusDMI()>0 && this.candles.peek().getDirection()==Constants.DIR_WHITE ){								
			//doji - 0 or close to 0 body length
			if(newCandle.getDirection()==Constants.DIR_NONE || calc.approximatelySame(0,currBody, deviation)){
					
				//if high wave (doji with long wicks)
				//if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
				//   newCandle.getLower_shadow_len()>newCandle.getAvgLowerShadowLen()*Globals.CANDLE_TYPE_FACTOR 
				//){
					//get confirmation
				//	triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				//}	
				//tombstone doji, extremely bearish if occuring at resistance
				 if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
					calc.approximatelySame(0, newCandle.getLower_shadow_len(), deviation)// &&
					//TODO find support, or at least previous day high
					//calc.approximatelySame(newCandle.p, currBody, deviation);
				){
					pat = PatternEnum.TOMBSTONE_DOJI;
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				// previous candle is long
				else if(candles.peek().getBody_len() > currBody * Globals.CANDLE_TYPE_FACTOR){
					//current is doji, almost no matter where it is located (harami cross or reversal star-abandoned baby)
					// need long confirmation - big coverage of 1st candle by 3rd one
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					pat = PatternEnum.EVENING_DOJI_STAR;
				}
			}
			//checking on small body length (spinning top), which is a part of several patterns
			else if(currBody < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgBodyLen()){
				//hanging man: body len is no more than 1/3 of average over the previous MAX_TREND_CANDLES
				//	lower shadow is more than 3 average
				if((newCandle.getUpper_shadow_len()==0 || newCandle.getUpper_shadow_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgUpperShadowLen()) && 
							newCandle.getLower_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgLowerShadowLen()
							){
					//check for the gap between hanging man and confirmation candle
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					pat = PatternEnum.HANGMAN;
				}
				//shooting star (weaker than hanging man)
				else if((newCandle.getLower_shadow_len()==0 || newCandle.getLower_shadow_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgLowerShadowLen()) && 
				newCandle.getUpper_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgUpperShadowLen()
				){
					//check for the gap between shooting star and confirmation candle
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					pat = PatternEnum.SHOOTING_STAR;
				}
				//bearish harami (not reliable, use as warning for current position)	
				else if(newCandle.getDirection()==Constants.DIR_BLACK &&
						this.candles.peek().getBody_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getBody_len() &&
						this.candles.peek().close() > newCandle.open() + newCandle.getBody_len()*1/3 &&
						this.candles.peek().open() < newCandle.close() - newCandle.getBody_len()*1/3
				){
					//for warning?
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM; 
					pat = PatternEnum.BEARISH_HARAMI;
				}
				//evening star reversal (first has  considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
				else if(candles.peek().close() <= newCandle.open() && candles.peek().close() <= newCandle.close()){
					// requires 3rd candle - long, black, gaps down at open, pushes down at least 1/2 of first candle body
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					pat = PatternEnum.EVENING_STAR_REVERSAL;
				}
			}
			//checking on long body length and direction opposite to trend, which is a part of several patterns			
			else if(newCandle.getBody_len() > (Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgBodyLen() &&
					newCandle.getDirection()==Constants.DIR_BLACK
			){
				//bearish engulfing pattern, gaps up at the opening, closes below previous body (stronger if new candle covers body of more than one previous candle)
				//bearish counter attack,    gaps up at the opening, closes at (or close to) the previous close
				//dark cloud cover,          gaps up at the opening, closes at least 1/2 into previous body
				if(newCandle.open()  > candles.peek().close() + (1/3)*newCandle.getBody_len()){		//gaps up at the opening
					if(newCandle.close() < candles.peek().open() - (1/3)*newCandle.getBody_len()){	//closes below previous body
							triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					}
					else if(newCandle.close() < candles.peek().open()){   //closes at or close to previous body, or at least 1/2 into previous body
							triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					}
				}
				//one black crow, gaps down at the opening, closes below previous body
				else if(newCandle.open() < candles.peek().close() - (1/3)*newCandle.getBody_len()){
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				//tweezers top, matching highs, previous close matches current open, stronger when bodies are about same size
				else if(newCandle.high()==candles.peek().high() && 
							newCandle.open()==candles.peek().close() &&
							newCandle.getBody_len()==candles.peek().getBody_len()
							){
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
			}
					
				if(triggerAction==Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM){
					this.currentPattern = new CandlePattern();
					//currentPattern.setPatternName(pName);
					//currentPattern.addCandleToPattern(newCandle);
					currentPattern.setCompleted(true, Constants.ACTION_SELL);
					currentPattern.setConfirmationDirection(Constants.DIR_BLACK); //down, opposite to trend 
			}
		}
		else if (newCandle.minusDMI()>0 && this.candles.peek().getDirection()==Constants.DIR_BLACK ){  //trending down, looking for bullish reversal																			
			//doji
			if(newCandle.getDirection()==Constants.DIR_NONE || calc.approximatelySame(0,currBody, deviation)){				
				//high wave (doji with long wicks)
				//if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
				//newCandle.getLower_shadow_len()>newCandle.getAvgLowerShadowLen()*Globals.CANDLE_TYPE_FACTOR 
				//){
				//	triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				//}	
				//dragonfly doji, extremely bullish if occuring at support
				if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
					calc.approximatelySame(0, newCandle.getUpper_shadow_len(), deviation)
					//TODO find support, or at least previous day high
					//calc.approximatelySame(newCandle.p, currBody, deviation);
				){
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				// previous candle is long
				else if(candles.peek().getBody_len() > currBody * Globals.CANDLE_TYPE_FACTOR){
					//current is doji, almost no matter where it is located (harami cross or reversal abandoned baby)
					// need long confirmation - big coverage of 1st candle by 3rd one
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
			}
			//checking on small body length (spinning top), which is a part of several patterns
			else if(currBody < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgBodyLen()){
				//hammer: body len is no more than 1/3 of average over the previous MAX_TREND_CANDLES
				//	lower shadow is more than 3 average
				if(calc.approximatelySame(0, newCandle.getUpper_shadow_len(), deviation) && 
							newCandle.getLower_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgLowerShadowLen()
							){
						//check for the gap between hanging man and confirmation candle
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				//inverted hammer (weaker than hammer or hanging man) - TO REMOVE?
				else if(calc.approximatelySame(0, newCandle.getLower_shadow_len(), deviation) && 
				newCandle.getUpper_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgUpperShadowLen()
				){
						//check for the gap between shooting star and confirmation candle
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				//bullish harami (not reliable, use as warning for current position)
				else if(newCandle.getDirection()==Constants.DIR_WHITE &&
							this.candles.peek().getBody_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getBody_len() &&
							this.candles.peek().close() > newCandle.open() + newCandle.getBody_len()*1/3 &&
							this.candles.peek().open() < newCandle.close() - newCandle.getBody_len()*1/3
							){
						// for warning?
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				//morning star reversal (first has  considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
				else if(candles.peek().close() >= newCandle.open() && candles.peek().close() >= newCandle.close()){
						// requires 3rd candle - long, black, gaps down at open, pushes down at least 1/2 of first candle body
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
			}
			//checking on long body length and direction opposite to trend, which is a part of several patterns			
			else if(newCandle.getBody_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgBodyLen() &&
						newCandle.getDirection()==Constants.DIR_WHITE
				){
				//bullish engulfing pattern, gaps down at the opening, closes above previous body (stronger if new candle covers body of more than one previous candle)
				//bullish counter attack,    gaps down at the opening, closes at (or close to) the previous close
				//bullish piercing line (oppsite of dark cloud cover), gaps down at the opening, closes at least 1/2 into previous body
				if(newCandle.open() < candles.peek().close() + (1/3)*newCandle.getBody_len()){		//gaps up at the opening
					if(newCandle.close() > candles.peek().open() + (1/3)*newCandle.getBody_len()){	//closes above previous body
							triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					}
					else if(newCandle.close() > candles.peek().open()){   //closes at or close to previous body, or at least 1/2 into previous body
							triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
					}
				}
				//one white soldier, gaps up at the opening, closes above previous body
				else if(newCandle.open() > candles.peek().close() + (1/3)*newCandle.getBody_len()){
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				//tweezers bottom, matching lows, previous close matches current open, stronger when bodies are about same size
				//tweezers - same low wick value - the longer shadows the stronger
				else if(newCandle.low()==candles.peek().low() && 
							calc.approximatelySame(newCandle.open(), candles.peek().close(), deviation) &&
							calc.approximatelySame(currBody, candles.peek().getBody_len(), deviation)
				){
						triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}		
					
				if(triggerAction==Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM){
					this.currentPattern = new CandlePattern();
					//currentPattern.setPatternName(pName);
					//currentPattern.addCandleToPattern(newCandle);
					currentPattern.setCompleted(true, Constants.ACTION_BUY); //action buy for long position
					currentPattern.setConfirmationDirection(Constants.DIR_WHITE); //up, opposite to trend 
				}
			}
		}	
	}
		
		//don't forget to put pack popped candle!
		this.candles.push(tmp);
	}
}
