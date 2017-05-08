package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Types;
import com.pancorp.tbroker.day.Broker;
import com.pancorp.tbroker.event.OpenPositionEvent;
import com.pancorp.tbroker.event.ClosePositionEvent;
import com.pancorp.tbroker.event.TradingEvent;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.CandlePattern;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Calculator;

public class StrategyTrendingCandlePattern implements IStrategy {
	private static Logger lg = LogManager.getLogger(StrategyTrendingCandlePattern.class); 
	
	private Calculator calc;
	private CandlePattern currentPattern;
	
	public StrategyTrendingCandlePattern(Calculator c){
		this.calc = c;
	}
	
	/**
	 * 
	 */
	public CandlePattern evaluate(ArrayDeque<Candle> candles, CandlePattern cp)  throws OpenPositionEvent, ClosePositionEvent, Exception {
		String fp = "evaluate: ";
		if(candles.size()<3)
			return null;
		this.currentPattern = cp;
		
		CandlePattern newCurrentPattern = null;
		
		//temporary remove newly added candle to be able to check previous candle(s)
		Candle newCandle = candles.pop();
		if(lg.isTraceEnabled())
			lg.trace(fp + "popped latest candle, calc: " + this.calc);
		
	  //0 - create pattern and require confirmation; 1 - GO and place order!
		
		if(currentPattern!=null){					
			if(currentPattern.isCompleted()){
				if(lg.isTraceEnabled())
					lg.trace(fp + "pattern is complete, evaluating current candle for confirmation ");
				//evaluate current candle for confirmation of the completed pattern
				newCurrentPattern = evaluateConfirmation(newCandle,candles);
			}
			else {
				//pattern is not completed, checking new Candle for pattern continuation
				if(lg.isTraceEnabled())
					lg.trace(fp + "pattern is not complete, checking new Candle for pattern c ontinuation (not implemented)");
			}
		}
		else { //no pattern existed, check whether new candle is part of a new pattern
			if(lg.isTraceEnabled())
				lg.trace(fp + "no pattern existed, checking whther new candle is part of a new pattern");
						
			calc.calcAvgLengths(Globals.NUMBER_OF_PERIODS, candles);
			if(lg.isTraceEnabled())
				lg.trace(fp + "calculated avg lengths");
			
			//if(newCandle.adx() > Globals.ADX_WEAK_TREND_THRESHOLD){		//strong trend
				//if(lg.isTraceEnabled())
				//	lg.trace(fp + "strong trend detected");
				
				//trending up, looking for reversal up	
				if(//newCandle.plusDIEma()>0 && 
						candles.peek().getDirection()==Constants.DIR_WHITE ){				
					newCurrentPattern = evaluateForShort(newCandle,candles);
				}
				//trending down, looking for reversal up
				else if (//newCandle.minusDIEma()>0 && 
						candles.peek().getDirection()==Constants.DIR_BLACK ){  //trending down, looking for bullish reversal
					newCurrentPattern = evaluateForLong(newCandle,candles);
				}
			/*}
			else {
				if(lg.isTraceEnabled())
					lg.trace(fp + "no strong trend detected, trend fading strategy required (not implemented)");
			}*/
		}

		//don't forget to put pack popped candle!
		candles.push(newCandle);
		
		return newCurrentPattern;
	}
	
	/**
	 * 
	 * @param newCandle
	 * @throws TradingEvent
	 */
	private CandlePattern evaluateConfirmation(Candle newCandle, ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception {
		int dir = newCandle.getDirection();
		double lmtPrice = 0;
		//pattern completed, checking new Candle for confirmation
		if(dir==currentPattern.getConfirmationDirection()){						// direction confirms the reversal
			double newOpen = newCandle.open();
				// //confirmation candle opens below pattern candle body
			//TODO add some confirmation for different pattern/cases
			switch(dir){
			case Constants.DIR_BLACK:
				if(newOpen < candles.peek().close() - candles.peek().getBody_len()/3){  //below previous close for at least 1/3 of its body length
					lmtPrice = newCandle.close();
					throw new OpenPositionEvent(Constants.ACTION_SSHORT,lmtPrice);
				}
				break;
			case Constants.DIR_WHITE:
				if(newCandle.open() > candles.peek().close() + candles.peek().getBody_len()/3){   //above previous close for at least 1/3 of its body length
					lmtPrice = newCandle.close();
					throw new OpenPositionEvent(Constants.ACTION_BUY,lmtPrice);
				}
				break;
				default:
			}		
		}
		else {
			//pattern is not complete
			
			//max count is reached, reset and abandon pattern
			if(currentPattern.getConfirmationCount()>=Globals.MAX_PATTERN_CONFIRMATION_COUNT)
				currentPattern = null;
			else //max count not reached, increment
				currentPattern.incrementConfirmationCount();
		}
		return currentPattern;
	}
			
 	private CandlePattern evaluateForLong(Candle newCandle,ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception {
 		double currBody = newCandle.getBody_len();
 		double deviation = currBody*Globals.DEVIATION_FACTOR;
 		int triggerAction = Constants.TRIGGER_ACTION_NONE; 
		//hammer
		
		//innverted hammer (weeker than hammer)
		
		//high wave (doji with long wicks)
		
		//bullish engulfing pattern (stronger if 2nd candle covers body of moer than one candle)
		
		//piercing line (oppsite of dark cloud cover)
		
		//bullish harami (not reliable, use as warning for current position)
		//harami cross (2nd is doji)
		
		//morning star / abandoned baby (first has  considerable body, gap between middle candle,  big coverage of 1st candle by 3rd one)
		
		//tweezers - same low wick value - the longer shadows the stronger
		
		//doji
		if(newCandle.getDirection()==Constants.DIR_NONE || calc.approximatelySame(0,currBody, deviation)){
			
			//if high wave (doji with long wicks)
			if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
			   newCandle.getLower_shadow_len()>newCandle.getAvgLowerShadowLen()*Globals.CANDLE_TYPE_FACTOR 
			){
				//get confirmation
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}	
			//dragonfly doji, extremely bullish if occuring at support
			else if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
					calc.approximatelySame(0, newCandle.getUpper_shadow_len(), deviation)
			){
				//get confirmation
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}
			// previous candle is long
			else if(candles.peek().getBody_len() > currBody * Globals.CANDLE_TYPE_FACTOR){

				//harami cross (2nd is doji),
				if(newCandle.open() > (candles.peek().close()+newCandle.getAvgBodyLen()*1/3)){
					//needs confirmation
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				// abandoned baby (first has considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
				else if(newCandle.open() < candles.peek().close() - newCandle.getAvgBodyLen()*1/4){
					//needs confirmation
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
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
			//inverted hammer(weaker than hanging man)
			else if(calc.approximatelySame(0, newCandle.getLower_shadow_len(), deviation) && 
			newCandle.getUpper_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgUpperShadowLen()
			){
				//check for the gap between shooting star and confirmation candle
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}
			//bullish harami (not reliable, use as warning for current position)	
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
			//bullish piercing,          gaps down at the opening, closes at least 1/2 into previous body
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
			else if(newCandle.low()==candles.peek().low() && 
					calc.approximatelySame(newCandle.open(), candles.peek().close(), deviation) &&
					calc.approximatelySame(currBody, candles.peek().getBody_len(), deviation)
			){
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}
		}
			
		if(triggerAction==Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM){
			currentPattern = new CandlePattern();
			//currentPattern.setPatternName(pName);
			//currentPattern.addCandleToPattern(newCandle);
			currentPattern.setCompleted(true, Constants.ACTION_BUY); //action buy for long position
			currentPattern.setConfirmationDirection(Constants.DIR_WHITE); //up, opposite to trend 
		}
		return currentPattern;
	}
	
	/**
	* @param newCandle
	* @param candles
	* @return
	* @throws TradingEvent
	* @throws Exception
	*/
	private CandlePattern evaluateForShort(Candle newCandle,ArrayDeque<Candle> candles) throws OpenPositionEvent, ClosePositionEvent, Exception{
		double currBody = newCandle.getBody_len();
		double deviation = currBody*Globals.DEVIATION_FACTOR;
		int triggerAction = Constants.TRIGGER_ACTION_NONE; 
		//was trending up, now check for reversal pattern - down and entry signal for short position, 
		//wait for pattern to form completely, then it is strongly recommended to wait for a 
		//confirmation candle, then enter a position after a confirmation candle, 
		//for long position stop loss below open price of confirmation candle
		//for short position stop loss above open price of confirmation candle
		
		//doji
		if(newCandle.getDirection()==Constants.DIR_NONE || calc.approximatelySame(0,currBody, deviation)){
			
			//if high wave (doji with long wicks)
			if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
			   newCandle.getLower_shadow_len()>newCandle.getAvgLowerShadowLen()*Globals.CANDLE_TYPE_FACTOR 
			  ){
				//get confirmation
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}	
			//tombstone doji, extremely bearish if occuring at resistance
			else if(newCandle.getUpper_shadow_len()>newCandle.getAvgUpperShadowLen()*Globals.CANDLE_TYPE_FACTOR &&
					newCandle.getLower_shadow_len()==0
				){
				//get confirmation
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
			}
			// previous candle is long
			else if(candles.peek().getBody_len() > currBody * Globals.CANDLE_TYPE_FACTOR){

				//harami cross (2nd is doji),
				if(newCandle.open()< (candles.peek().close()+newCandle.getAvgBodyLen()*1/3)){
					//needs confirmation
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
				// abandoned baby (first has considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
				else if(newCandle.open()>candles.peek().close()+newCandle.getAvgBodyLen()*1/4){
					//needs confirmation
					triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
				}
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
			}
			//shooting star (weaker than hanging man)
			else if((newCandle.getLower_shadow_len()==0 || newCandle.getLower_shadow_len() < (1/Globals.CANDLE_TYPE_FACTOR)*newCandle.getAvgLowerShadowLen()) && 
			newCandle.getUpper_shadow_len() > Globals.CANDLE_TYPE_FACTOR*newCandle.getAvgUpperShadowLen()
			){
				//check for the gap between shooting star and confirmation candle
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
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
			//evening star reversal (first has  considerable body,gap between middle candle,  big coverage of 1st candle by 3rd one)
			else if(candles.peek().close() <= newCandle.open() && candles.peek().close() <= newCandle.close()){
				// requires 3rd candle - long, black, gaps down at open, pushes down at least 1/2 of first candle body
				triggerAction = Constants.TRIGGER_ACTION_PATTERN_TO_CONFIRM;
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
			currentPattern = new CandlePattern();
			//currentPattern.setPatternName(pName);
			//currentPattern.addCandleToPattern(newCandle);
			currentPattern.setCompleted(true, Constants.ACTION_SELL);
			currentPattern.setConfirmationDirection(Constants.DIR_BLACK); //down, opposite to trend 
		}
		return currentPattern;
	}
			
	private boolean isPatternComplete(Candle newCandle, ArrayDeque<Candle> candles){
		boolean complete = false;
	
		if(currentPattern.isCompleted()){
			complete = true;
		
			int dir = newCandle.getDirection();
			//pattern completed, checking new Candle for confirmation
			if(dir==currentPattern.getConfirmationDirection() && 					// direction confirms the reversal
			   newCandle.open() < candles.peek().close() && newCandle.open() < candles.peek().open()		//confirmation candle opens below pattern candle body
			  ){	
				
			}
			else {
				//pattern is not confirmed, increment confirmation count or reset
				if(currentPattern.getConfirmationCount()>=Globals.MAX_PATTERN_CONFIRMATION_COUNT)
					currentPattern = null;
				else
					currentPattern.incrementConfirmationCount();
			}
		}
		else {
			//pattern is not completed, checking new Candle for pattern continuation
		}
		return complete;
	}

}
