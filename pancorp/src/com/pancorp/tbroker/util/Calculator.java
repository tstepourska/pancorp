package com.pancorp.tbroker.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.ib.client.Contract;

//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.oscillators.StochasticOscillatorKIndicator;
//import com.pancorp.tbroker.math.LinearRegression;
//import com.ts.test.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;
 
public class Calculator {
	private static Logger lg = LogManager.getLogger(Calculator.class);
    public static long tfFactor = 0;
    
    public boolean calibrated = false;
    private int calibrationCount = 0;

       public Calculator(int periodSh, int periodLg, int timeframeSize, String timeframeUnit, long tfFactor2) {

       }
       
       public Calculator(){
    	   
       }
       
       public boolean approximatelySame(double base, double compared, double dev){
    	   boolean same = Math.abs(base-compared)<dev;
    	   
    	   return same;
       }


	/**
       +	 * Calculates Simple Moving Average of close price for specified number of timeframe units
       +	 * 5, 10, 20
       +	 * 
       +	 * -->Locate stock that breaking out up(down) strongly
       +	 * -->Select 2 SMA to apply to the chart (ex. 5 and 10)
       +	 * -->Make sure price has not been touching the 5 SMA or 10 SMA excessively in the last 10 bars
       +	 * -->Wait for the price to close above(below) both moving averages in the counter direction 
       +	 * of the primary trend on the SAME BAR
       +	 * -->Enter the trade on the next bar
       +	 * ============================
       +	 * ~ price goes crosses above the SMA  - ~
       +	 *============================== 
       	 * Breakouts in the morning:
       	 * 
       +	 *-->Price greater  than 10 dollars
       +	 *-->Greater than 40000 shares traded every 5 minutes
       +	 *-->Less than 2% from its moving average
       +	 *-->Volatility has to be solid enough to hit profit target 1.62%
       +	 *-->Cannot have a number of bars that are 2% in range (high to low)
       +	 *-->Must open the trade between 9:50 and 10:10 am
       +	 *-->Need to exit the trade no later than 12 noon
       +	 *-->Close the trade out if the stock closes above or below its 10-period MA after 11 am
       +	 * 
       +	 * @param smaSize			--number of units to calculate MA upon, ex 21
       +	 * @param timeframeSize		--number of timeframes in a unit, ex 5 (5-min timeframe)
       +	 * @param timeframeUnit		--time unit used in timeframe, ex MIN (5-min timeframe)
       +	 * @param queue				--list of real time Bars to calculate from
       +	 * 
       +	 * @return double
       +	 * 
       +	 * @throws NotEnoughDataException
       +	 * @throws Exception
       +	 */
       public double SMAclose(int smaPeriod, //int timeframeSize, String timeframeUnit, 
    		   ArrayDeque<Candle> queue, Candle c) throws NotEnoughDataException, Exception {
    	   		double sma = 0;
    	   		double total = 0;
    	   		double value;
    	   	/*	
    	   		switch(timeframeUnit){
    	   		case "MIN":
    	   			tfFactor = Constants.MIN;
    	   			break;
    	   	case "HOUR":
    	   			tfFactor = Constants.HOUR;
    	   			break;
    	   		case "DAY":
    	   			tfFactor = Constants.DAY;
    	   			break;
    	   			default:
    	   		}
    	   		
    	   		long numOfBars = smaPeriod*tfFactor;
    	   		lg.debug("calcSMA: numOfBars: " + numOfBars + ", queue size: " + queue.size());
    	   		*/
    	   		//if((queue.size() + 1 )<smaPeriod)
    	   			//throw new NotEnoughDataException();
    	   		
    	   		Iterator<Candle> it = queue.descendingIterator();
    	   		int i=1;
    	   		while(it.hasNext()){
    	   			value = it.next().close();
    	   			total+=value;
    	   			if(i==(smaPeriod-1))
    	   				break;
    	   			
    	   			i++;
    	   		}
    	   		//add the latest bar close
    	   		total = total + c.close();
    	   		
    	   		if(i<smaPeriod)
    	   			sma = total/i;
    	   		else
    	   			sma = total/smaPeriod;
    	   		
    	   		return sma;
    	   	}
       
       
       	/**
       +	 * Calculates Exponential Moving Average of close price for specified number of timeframe units.
       +	 * EMA is a weighted moving average (WMA) that gives more weighting to recent price data than 
       +	 * SMA does. The EMA responds more quickly to recent price changes than the SMA. 
       +	 * The formula calculating EMA involves using a multiplier and stating with SMA.
       +	 * 
       +	 * @param smaSize			--number of units to calculate MA upon, ex 21
       +	 * @param timeframeSize		--number of timeframes in a unit, ex 5 (5-min timeframe)
       +	 * @param timeframeUnit		--time unit used in timeframe, ex MIN (5-min timeframe)
       +	 * @param queue				--list of real time Bars to calculate from
       +	 * 
       +	 * @return double
       +	 * 
       +	 * @throws NotEnoughDataException
       +	 * @throws Exception
       +	 */
       	public double EMAclose(int emaPeriod, //int timeframeSize, String timeframeUnit, 
       			ArrayDeque<Candle> queue, Candle c) throws //NotEnoughDataException, 
       	Exception {
       		String fp = "EMA: ";
       		double ema = 0;
       		
       		//Step 1. Calculate SMA
       		double sma = SMAclose(emaPeriod,//timeframeSize, timeframeUnit, 
       				queue, c);
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "sma: " + sma);
       		
       		//Step 2. Calculating the weighting multiplier
       		double multiplier = 2/emaPeriod + 1;
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "multiplier: " + multiplier);
       		
       		//Step 3: Calculate EMA
       		//get the closing price of the last bar
       		double close = c.close();
       		//get the ema for previous timeframe unit
       		double ema0 = queue.peekFirst().emaShort();
       		
       		ema = (close - ema0)*multiplier+ema0;
       		
       		return ema;
       	}
       	
    	/**
    	 * between 0 and -20  - overbought, signal to sell
    	 * between -80 and -100  - oversold, signal to buy
    	 * 
    	 * typical period is 14
    	 * 
    	 * @param latestClose
    	 * @param highestHigh
    	 * @param lowestLow
    	 * 
    	 * @return		between 0 and -100
    	 */
    	public double WilliamsR(double latestClose, double highestHigh, double lowestLow){
    		double wr = 0;

    		wr = (highestHigh - latestClose)/(highestHigh - lowestLow)*(-100);
    		return wr;
    	}
    	
    	public double maxHigh(ArrayDeque<IBar> cc){
    		double max = 0;
    		double t = 0;
    		for(IBar c : cc){
    			t = c.high();
    			if(t>max)
    				max = t;
    		}
    		
    		return max;
    	}
    	
    	public double minLow(ArrayDeque<IBar> cc){
			double min = 10000; //some arbitrary number to ensure there will be tick with the low price less than it
			double t = 0;
			for(IBar c : cc){
				t = c.low();
				if(t<min)
					min = t;
			}
			
			return min;
		}
    	
    	public long sumVolume(ArrayDeque<IBar> cc){
			long sum = 0;
			for(IBar c : cc){
				sum+=c.volume();
			}
			
			return sum;
		}
    	
    	/*
    	////////////////////////////////////////////////////////
    	//  Stochastic
    	//
    	// The indicator consists of two lines:
    	//
    	//%K compares the latest closing price to the recent trading range.
    	//%D is a signal line calculated by smoothing %K.
    	 * 
    	//The number of periods used in the indicator can be varied according to the purpose for which the Stochastic Oscillator is used:
    	//
    	//Purpose:						%K Periods		%D Periods	Overbought level	Oversold level	Comments:
    	//Combine with trend indicator	5 to 10 days	3 days		80%					20%				Very sensitive
    	//Stand-alone or trade 			14 or 21 days	3 days		70%					30%				Only shows important turning points
    	//longer cycles
    	 * 
    	 * If the Stochastic Oscillator hovers near 100 it signals accumulation (overbought). 
    	 * Stochastic lurking near zero indicates distribution (oversold).
    	 * 
    	 * Ranging Markets

Signals are listed in order of their importance:

Go long on bullish divergence (on %D) where the first trough is below the Oversold level.
Go long when %K or %D falls below the Oversold level and rises back above it.
Go long when %K crosses to above %D.


Short signals:

Go short on bearish divergence (on %D) where the first peak is above the Overbought level.
Go short when %K or %D rises above the Overbought level then falls back below it.
Go short when %K crosses to below %D.
Place stop-losses below the most recent minor Low when going long (or above the most recent minor High when going short).

%K and %D lines pointed in the same direction are used to confirm the direction of the short-term trend.

Lane also used Classic Divergences, a type of triple divergence.


Trending Markets

Only take signals in the direction of the trend and never go long when the Stochastic Oscillator is overbought, nor short when oversold.

Use trailing buy- and sell-stops to enter trades and protect yourself with stop-losses.

Long:

If %K or %D falls below the Oversold line, place a trailing buy-stop. When you are stopped in, place a stop loss below the Low of the recent down-trend (the lowest Low since the signal day).

Short:

If Stochastic Oscillator rises above the Overbought line, place a trailing sell-stop. When you are stopped in, place a stop loss above the High of the recent up-trend (the highest High since the signal day).

Exit:

Use a trend indicator to exit.
    	*/
    	public void calcStochastic(ArrayDeque<Candle> cc,int kPeriod, int dPeriod) throws NotEnoughDataException {  //Candle c, 
    		if(cc==null || cc.size()<kPeriod)
    			throw new NotEnoughDataException("calcStochastic: size is less than " + kPeriod);
    		
    		/*
    		 * Stochastic Oscillator Formula

				To calculate the Stochastic Oscillator:

				1) decide on the number of periods (%K Periods) to be included in the calculation. 
				The norm is 5 days, but this should be based on the time frame that you are analyzing.
				
				2) Then calculate %K, by comparing the latest Closing price to the range traded over the selected period: 
           			CL = Close [today] - Lowest Low [in %K Periods] 
           			HL =Highest High [in %K Periods] - Lowest Low [in %K Periods] 
           			%K = CL / HL *100

				3) Calculate %D by smoothing %K as a 3 period simple moving average.
				
				=================================
				
				%K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
				%D = 3-day SMA of %K

				Lowest Low = lowest low for the look-back period
				Highest High = highest high for the look-back period
				%K is multiplied by 100 to move the decimal point two places
    		 */
    		//latest candle just added
    		//Candle c = cc.peekFirst();
    		
    		//kPeriod: 5
    		ArrayDeque<IBar> kStack = new ArrayDeque<>();
    		for(int i=0;i<kPeriod;i++){
    			kStack.push(cc.pop());
    		}
    		lg.debug("calcStochastic: kStack.size: "+ kStack.size());
    		
    		StochasticOscillatorKIndicator stochasticK = new StochasticOscillatorKIndicator(kStack, kPeriod);
    		double K = stochasticK.calculate();
    		
    		// 2)
    		/*double kLowestLow = this.minLow(kStack);
    		lg.debug("calcStochastic: kLowestLow: " + kLowestLow);
    		double kHighestHigh = this.maxHigh(kStack);
    		lg.debug("calcStochastic: kHighestHigh: " + kHighestHigh);
    		double close = kStack.peek().close();
    		
    		//CL = Close [today] - Lowest Low [in %K Periods] 
    		double CL = close - kLowestLow;
    		lg.debug("calcStochastic: CL: " + CL);
    		
    		//HL =Highest High [in %K Periods] - Lowest Low [in %K Periods] 
    		double HL = kHighestHigh - kLowestLow;
    		lg.debug("calcStochastic: HL: " + HL);
    		
    		double K = CL / HL * 100;
    		lg.debug("calcStochastic: K:" + K);*/
    		
    		//((Candle)kStack.peek()).stochasticK(K);
    		
    		// 3) 
    		// put all back
    		while(!kStack.isEmpty())   		
    			cc.push((Candle)kStack.pop());
    		
    		// ! assign newly calculated K value to the latest candle!
    		cc.peek().stochasticK(K);  		
    		
    		//D period: 3
    		double kSum = 0;
    		//move dPeriod candles from deque to stack while summing K values
    		while(kStack.size()<=dPeriod){
    			Candle can = (Candle)cc.pop();
    			double sk = can.stochasticK();
    			//if prev candle did not have K value, throw NotEnoughData and wait for a next candle
    			if(sk==0){
    				//sk = K;
    				//can.stochasticK(K);
    				throw new NotEnoughDataException("No stochasticK value for previous candle, waiting for the next one.");
    			}
    			kSum+= sk;
    			//put in stack
    			kStack.push(can);
    		}
    		//calculate D
    		double D = kSum / dPeriod;
    		lg.debug("calcStochastic: D: " + D);
    		
    		//put back into deque
    		while(!kStack.isEmpty())
    			cc.push((Candle)kStack.pop());
    		
    		//assign newly calculated D value to the latest candle
    		cc.peek().stochasticD(D);
    		
    	}
    	
  /*  	public double linearRegressionSlope(ArrayDeque<Candle> cc,Candle c){
    		//y = mx + b  - we are finding b
    		//double b = LinearRegression.calculateLinearRegressionSlope(cc,c);
    		if(lg.isDebugEnabled())
    		lg.debug("slope: " + b);
    		
    		return b;
    	}*/
    	
    	////////////////////////////////////////////////////////////////////////////////////////
    	//
    	// The ADX/DMI is represented by three lines +DM, -DM and ADX.
    	////////////////////////////////////////////////////////////////////////

    	public void calcADX_DMI(int period, ArrayDeque<Candle> cc) throws Exception{
    		String fp = "calcADX_DMI: ";
    		Candle curr = cc.pop();
    		Candle prev = null;
    		
    		lg.debug(fp + "period: " + period);
    		
    		// 1. Calculate True Range, and Directional Movement (+DM and -DM) for each period
    		double tr = TrueRange(curr,cc.peekFirst());
    		if(lg.isTraceEnabled())
        		lg.trace(fp+"current TrueRange: " + tr);
        		
    		try{
    			prev = cc.peekFirst();
    		}catch(Exception e){ lg.error("Error getting prev:" + e.getMessage()); }
    		
    		//The Directional movement
    		plusMinusDM(curr,prev);
    		//if(lg.isTraceEnabled())
        	//	lg.trace(fp+"current plusDI: " + curr.plusDI() + ", curr minusDI: " + curr.minusDI());
    		
    		//2. Smooth these periodic values using the Wilder's smoothing techniques. 
    		//--starts as a simple moving average (SMA) of each: true range, plusDI and minusDI
    		//	Moving average of +DM, -DM and True Range		
    		//�TRMA = exponential moving average of True Range
    		double trma = EMA(period,Constants.EMA_TYPE_TRUE_RANGE, cc, curr);
    		if(lg.isTraceEnabled())
        		lg.trace(fp + "current TRMA: " + trma);
    		//�+DMMA = exponential moving average of +DM
    		double emaPlusDM  = EMA(period, Constants.EMA_TYPE_PLUS_DM,cc,curr);
    		if(lg.isTraceEnabled())
    			lg.trace("current +DMMA: " + emaPlusDM);
    		//�-DMMA = exponential moving average of -DM
    		double emaMinusDM = EMA(period, Constants.EMA_TYPE_MINUS_DM,cc,curr);
    		if(lg.isTraceEnabled())
        		lg.trace("current -DMMA: " + emaMinusDM);

    		//The Directional Indicators
    		double plusDI = 0;
    		double minusDI = 0;
    
    		if(trma!=0){
    		//3. Divide the 14-day smoothed Plus Directional Movement (ema +DM) by the 14-day smoothed True Range 
    		//to find the 14-day Plus Directional Indicator (+DI14). 
    		//Multiply by 100 to move the decimal point two places. 
    		//This +DI14 is the Plus Directional Indicator (green line) that is plotted along with ADX. 
    		//--divide the smoothed +DI by the smoothed TrueRange
    		//�+DI = +DMMA / TRMA
    		plusDI = emaPlusDM / trma;
    		if(lg.isTraceEnabled())
        		lg.trace("current +DI = emaPlusDM / trma = " + plusDI);
    		curr.plusDI(plusDI);
    		
    		//4. Divide the 14-day smoothed Minus Directional Movement (-DM) by the 14-day smoothed True Range 
    		//to find the 14-day Minus Directional Indicator (-DI14). 
    		//Multiply by 100 to move the decimal point two places. 
    		//This -DI14 is the Minus Directional Indicator (red line) that is plotted along with ADX. 
    		//--divide the smoothed -DI by the smoothed TrueRange
    		//double smoothedMinusDI = (smaMinusDI / atr) *100;
    		//if(lg.isTraceEnabled())
        	//	lg.trace("current smoothedMinusDI: " + smoothedMinusDI);
    		//�-DI = -DMMA / TRMA
    		minusDI = emaMinusDM / trma;
    		if(lg.isTraceEnabled())
        		lg.trace("current -DI = emaMinusDM / trma = " + minusDI);
    		curr.minusDI(minusDI);
    		}
	
    		//double smoothedPlusDI = (smaPlusDI / atr) *100;
    		//if(lg.isTraceEnabled())
        	//	lg.trace("current smoothedPlusDI: " + smoothedPlusDI);
        		
    		//Directional Index
    		double DX = 0;
    		//�DX = |(+DI - (-DI))| / (+DI + (-DI))
    		if((plusDI + minusDI)!=0){
    		//5. The Directional Movement Index (DX) equals the absolute value of +DI14 less - DI14 divided 
    		//by the sum of +DI14 and - DI14. Multiply the result by 100 to move the decimal point over two places. 
    		// calculate Directional Movement Index which equals:
    		//((absolute value of the smoothed +DI - smoothed -DI) / (sum of the smoothed plusDI and smoothed -DI)) * 100
    		DX = (Math.abs(plusDI - minusDI))/(plusDI + minusDI) * 100;
    		if(lg.isTraceEnabled())
        		lg.trace("current DX = (Math.abs(plusDI - minusDI))/(plusDI + minusDI) = " + DX);
    		curr.dx(DX);
    		}
    		
    		//The Average Directional Movement Index
    		//�ADX = the exponential moving average of DX
    		//�ADX = SUM[(+DI-(-DI))/(+DI+(-DI)), N]/N
    		//Where:
    		//	N � the number of periods used in the calculation
    		//6. After all these steps, it is time to calculate the Average Directional Index (ADX). 
    		//The first ADX value is simply a 14-day average of DX. 
    		//Subsequent ADX values are smoothed by multiplying the previous 14-day ADX value by 13, 
    		//adding the most recent DX value and dividing this total by 14. 
    		double ADX;
    		
    		if(cc.size()==0){
    			ADX = curr.dx();
    		}
    		else {
    			//check if 1st 
    			ADX = (prev.adx()*(period-1) + curr.dx()) /period;
    		}
    		lg.info("current ADX: " + ADX);
    		
    		/////////////////////////////////////
    		if(prev!=null){
    			if(!calibrated && ADX>prev.adx()){
    				if(calibrationCount<Globals.MAX_CALIBRATION_COUNT){
    					calibrationCount++;
    					lg.info(fp + "calibration count incremented: " + calibrationCount);
    				}
    				else{
    				calibrated = true;
    				lg.info(fp + "Calculator calibration completed.");
    				}
    			}
    		}
    		/////////////////////////////////////
    		curr.adx(ADX);
    		
    		//lg.info(fp + "curr candle: " + curr);
    		//put it back
    		cc.push(curr);
    	}
    	/*
		
		*/
    	
    	/**
    	 * ADX - Average Directional Index - is a number between 0 and 100 which is used to quantify 
    	 * the existence or nonexistence of a trend, market momentum and trend strength. 
    	 * ADX calculations are based on a moving average of price range expansion over a given period of time. 
    	 * The default setting is 14 bars, although other time periods can be used.
    	 * ADX can be used on any trading vehicle such as stocks, mutual funds, exchange-traded funds and futures.
    	 * 
    	 * ADX is non-directional; it registers trend strength whether price is trending up or down.
    	 * The indicator is usually plotted in the same window as the two directional movement indicator (DMI) lines, from which ADX is derived 
    	 *  
    	 * Market direction is determined by the levels of the +DI and -DI. 
    	 * When the +DMI is above the -DMI, prices are moving up, and ADX measures the strength of the uptrend. 
    	 * When the -DMI is above the +DMI, prices are moving down, and ADX measures the strength of the downtrend.
    	 * 
    	 * A value over 20-!25 indicates the existence of a trend; a value over 40 indicates a strong trend.
    	 * 
    	 * When ADX is below 25 for more than 30 bars, price enters range conditions and price patterns are often easier to identify. 
    	 * Price then moves up and down between resistance and support to find selling and buying interest, respectively.
    	 * 
    	 * When the ADX line is rising, trend strength is increasing and price moves in the direction of the trend. 
    	 * When the line is falling, trend strength is decreasing, and price enters a period of retracement or consolidation.
    	 * 
    	 * A falling ADX line does NOT mean that trend is reversing, it only means the trend strength is weakening, 
    	 * but it usually does not mean the trend is reversing unless there has been a PRICE CLIMAX. 
    	 * As long as ADX is above 25, it is best to think of a falling ADX line as simply less strong
    	 * 
    	 * The series of ADX peaks are also a visual representation of overall trend momentum. ADX clearly indicates when 
    	 * the trend is gaining or losing momentum. Momentum is the velocity of price. 
    	 * A series of higher ADX peaks means trend momentum is increasing. 
    	 * A series of lower ADX peaks means trend momentum is decreasing.
    	 * 
    	 * Any ADX peak above 25 is considered strong, even if it is a lower peak. 
    	 * In an uptrend, price can still rise on decreasing ADX momentum because overhead supply is eaten up as the trend progresses
    	 * 
    	 * Knowing when trend momentum is increasing gives the trader confidence to let profits run instead of exiting before the trend has ended. 
    	 * However, a series of lower ADX peaks is a warning to watch price and manage risk.
    	 * 
    	 * ADX can also show momentum divergence. 
    	 * When price makes a higher high and ADX makes a lower high, there is negative divergence, or nonconfirmation, 
    	 * which means that trends continues but becomes weaker.
    	 * In general, divergence is not a signal for a reversal, but rather a warning that trend momentum is changing. 
    	 * It may be appropriate to tighten the stop-loss or take partial profits. 
    	 * 
    	 * Any time the trend changes character, it is time to assess and/or manage risk. 
    	 * Divergence can lead to trend continuation, consolidation, correction or reversal
    	 * 
    	 * Price is the single most important signal on a chart. Read price first, and then read ADX 
    	 * in the context of what price is doing. When any indicator is used, it should add something 
    	 * that price alone cannot easily tell us. 
    	 * For example, the best trends rise out of periods of price range consolidation. 
    	 * Breakouts from a range occur when there is a disagreement between the buyers and sellers on price, which tips the balance of supply and demand. 
    	 * Whether it is more supply than demand, or more demand than supply, it is the difference that creates price momentum.
    	 * 
    	 * Breakouts are not hard to spot, but they often fail to progress or end up being a trap. 
    	 * ADX tells you when breakouts are valid by showing when ADX is strong enough for price to trend after the breakout. 
    	 * When ADX rises from below 25 to above 25, price is strong enough to continue in the direction of the breakout.
    	 * 
    	 * Conversely, it is often hard to see when price moves from trend to range conditions. 
    	 * ADX shows when the trend has weakened and is entering a period of range consolidation. 
    	 * Range conditions exist when ADX drops from above 25 to below 25. 
    	 * In a range, the trend is sideways and there is general price agreement between the buyers and sellers. 
    	 * ADX will meander sideways under 25 until the balance of supply and demand changes again.
    	 * 
    	 * ADX gives great strategy signals when combined with price. 
    	 * First, use ADX to determine whether prices are trending or non-trending, and then choose the appropriate trading strategy for the condition. 
    	 * In trending conditions, entries are made on pullbacks and taken in the direction of the trend. 
    	 * In range conditions, trend trading strategies are not appropriate. 
    	 * However, trades can be made on reversals at support (long) and resistance (short).
    	 * 
    	 * The best profits come from trading the strongest trends and avoiding range conditions. 
    	 * ADX not only identifies trending conditions, it helps the trader find the strongest trends to trade. 
    	 * The ability to quantify trend strength is a major edge for traders.
    	 * 
    	 * ADX also identifies range conditions, so a trader won't get stuck trying to trend trade in sideways price action. 
    	 * In addition, it shows when price has broken out of a range with sufficient strength to use trend trading strategies. 
    	 * ADX also alerts the trader to changes in trend momentum, so risk management can be addressed. 
    	 * If you want the trend to be your friend, you'd better not let ADX become a stranger.
    	 * 
    	 * @param cc
    	 * @return
    	 */
    /* 	public double ADX(int period, ArrayDeque<Candle> cc) throws Exception {
    		double adx = 0;
    		// first determine the + and - directional movement indicators, or DMI
    		//The +DM and -DM are found by calculating the "upmove," or current high minus the previous high, 
    		//and "downmove," or current low minus the previous low.
    		
    		//If the upmove is greater than the downmove and greater than zero, the +DM equals the upmove; 
    		//otherwise, it equals zero. 
    		//If the downmove is greater than the upmove and greater than zero, the -DM equals the downmove; 
    		//otherwise, it equals zero.
    		//FORMULA ADX
    		//adx = 100 * EMA of abs((plusDMI - minusDMI)/(plusDMI + minusDMI));
    		
    		double adxFactor = adxFactor(period,cc);
    		cc.peekFirst().adxFactor(adxFactor); //set the calculated value to the latest candle
    		
    		//The ADX indicator itself equals 100 times the exponential moving average of the 
    		//absolute value of (+DI minus -DI) divided by (+DI plus -DI).
    		adx = 100 * EMA(period,Constants.EMA_TYPE_ADX_FACTOR, cc);

    		return adx;
    	}*/
    	
    	/**
    	 * Absolute value of (+DI minus -DI) divided by (+DI plus -DI)
    	 * 
    	 * @param period
    	 * @param cc
    	 * @param c
    	 * @return
    	 */
  /*  	public double adxFactor(int period, ArrayDeque<Candle> cc) throws Exception {
    		double plusDMI 		= plusDMI(period,cc); 		
    		double minusDMI 	= minusDMI(period,cc);  
    		//If the upmove is greater than the downmove and greater than zero, the +DM equals the upmove; 
    		//otherwise, it equals zero. 
    		//If the downmove is greater than the upmove and greater than zero, the -DM equals the downmove; 
    		//otherwise, it equals zero.
    		
    		if(plusDMI>minusDMI){
    			if(plusDMI > 0)
    				; //keep the value
    			else
    				plusDMI = 0;
    		}
    		else if(plusDMI==minusDMI){  //is this possible?
    			//what values?
    			//but it is clear that there is no movement, no trend
    			plusDMI = 0;
    			minusDMI = 0;
    		}
    		else{ //minusDMI > plusDMI
    			if(minusDMI > 0)
    				; //keep the value
    			else
    				minusDMI = 0;
    		}  			
    		
    		cc.peekFirst().plusDMI(plusDMI); //set the calculated value to the latest candle
    		cc.peekFirst().minusDMI(minusDMI); //set the calculated value to the latest candle
    		
    		double adxFactor 	= 0;
    		if(plusDMI!=0 && minusDMI!=0)	//eliminate division by zero
    			adxFactor = Math.abs((plusDMI - minusDMI)/(plusDMI + minusDMI));
    		
    		cc.peekFirst().adxFactor(adxFactor);
    		
    		return adxFactor;
    	}*/
    	
    	/**
    	 * The positive directional indicator, or +DI, equals 100 times the exponential moving average (EMA) 
    	 * of +DM divided by the average true range over a given number of time periods. Welles usually used 14 periods. 
    	 * 
    	 * If the upmove is greater than the downmove and greater than zero, the +DM equals the upmove; 
    	 * otherwise, it equals zero. It is calculated in the adxFactor method, so calculation is not 
    	 * completed here, that is why  this method is private.
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
 /*   	private double plusDMI(int period, ArrayDeque<Candle> cc) throws Exception {
    		double atr = AverageTrueRange(period, cc);
    		lg.debug("plusDMI: atr: "+atr);
    		if(atr==0)
    			throw new Exception("Invalid ATR, division by zero!");
    		
    		double plusDMI = 100 * EMA(period, Constants.EMA_TYPE_PLUS_DMI, cc) / atr;
    		return plusDMI;
    	}*/
    	
    	/**
    	 * Positive directional indicator shows the difference between today's high price and yesterday's high price.
    	 * These values are added up from the past <N> (14) days / periods and plotted.
    	 * 
    	 *  If the upmove is greater than the downmove and greater than zero, the +DM equals the upmove; 
    	 * otherwise, it equals zero.
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
 /*   	private double plusDMI(Candle curr, Candle prev) throws Exception {
    		double plusDMI = curr.high() - prev.high();
    		if(plusDMI>0){
    			curr.plusDMI(plusDMI);
    			
    		}
    		else{
    			curr.plusDMI(0);
    			plusDMI = 0;
    		}
    		lg.debug("plusDMI:" + plusDMI);
    		return plusDMI;
    	}*/
    	
    	/**
    	 *The negative directional indicator, or -DI, equals 100 times the exponential moving average 
    	 * of -DM divided by the average true range (ATR).
    	 * 
    	 * If the downmove is greater than the upmove and greater than zero, the -DM equals the downmove; 
    	 * otherwise, it equals zero. It is calculated in the adxFactor method, so calculation is not 
    	 * completed here, that is why  this method is private.
    	 * 
    	 * Negative directional indicator shows the difference between today's low price and yesterday's low price.
    	 * These values are added up from the past <N> (14) days / periods and plotted.
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
  /*  	private double minusDMI(int period, ArrayDeque<Candle> cc) throws Exception {
    		double atr = AverageTrueRange(period, cc);
    		lg.debug("plusDMI: atr: "+atr);
    		if(atr==0)
    			throw new Exception("Invalid ATR, division by zero!");
    		
    		double minusDMI = 100 * EMA(period, Constants.EMA_TYPE_MINUS_DMI,cc) / atr;
    		return minusDMI;
    	}  	*/
    	
    	/**
    	 * Negative directional indicator shows the difference between today's low price and yesterday's low price.
    	 * These values are added up from the past <N> (14) days / periods and plotted.
    	 * 
    	 * If the downmove is greater than the upmove and greater than zero, the -DM equals the downmove; 
    	 * otherwise, it equals zero. 
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    /*	private double minusDMI(Candle curr, Candle prev) throws Exception {
    		double minusDMI = curr.low() - prev.low();   		
    		lg.debug("minusDMI:" +minusDMI);
    		return minusDMI;
    	}  	*/
    	
    	/**
    	 * 
    	 * @param curr
    	 * @param prev
    	 */
    	private void plusMinusDM(Candle curr,Candle prev) throws Exception {
    		double prevLow = 0;
    		double prevHigh = 0;
    		/*if(prev==null){
    			curr.setFirstInCache(true);
    		}*/
    		double minusDM = 0;				
    		double plusDM = 0;
    		
    		try{
    			prevLow = prev.low() ;
    			prevHigh = prev.high();
    		}
    		catch(Exception e){
    			lg.error("plusMinusDM: error getting prev data: " + e.getMessage());
    		}
    		
    		minusDM = prevLow - curr.low();   
    		plusDM = curr.high() - prevHigh;  
    		if(lg.isTraceEnabled())
    		lg.trace("plusMinusDM: calculated: minusDM=" + minusDM + ", plusDM="+plusDM);
    		
    		//if both are negative, then both equal to 0
    		if(minusDM <= 0 && plusDM <= 0){
    			curr.minusDM(0);
    			curr.plusDM(0);
    			return;
    		}
    		
    		//if +DM is greater -DM
    		if(plusDM > minusDM){
    			if(plusDM>0)
    				curr.plusDM(plusDM);	
    			else
    				curr.plusDM(0);
    			
    			curr.minusDM(0);
    		}
    		//-DM is greater +DM
    		else {
    			if(minusDM > 0)
    				curr.minusDM(minusDM);
    			else
    				curr.minusDM(0);
    			
    			curr.plusDM(0);
    		}
    	}
    	
        public double SMA(int period, int emaType, ArrayDeque<Candle> cc, Candle curr) throws Exception {
        	String fp = "SMA: ";
        	double value;
        	double sma = 0;
        	double total = 0;
        	String type = "";
        	
        	if(cc==null){
        		lg.info("SMA: cc is null: "+cc);
        		return sma;
        	}
        	else if(cc.size()<=0){
        		//int tot = 0;
        		//empty cache, current candle is the only element
        		switch(emaType){		
     	   		case Constants.EMA_TYPE_CLOSE:
     	   			type = "close ";
     	   			//lg.debug(fp + "calculating SMA close for single candle");
     	   			value = curr.close();	 
     	   			curr.smaClose(value);
     	   			//lg.debug(fp + "value of close=" + value);
     	   			break;
     	   		case Constants.EMA_TYPE_PLUS_DM:
     	   			type = "+DM ";
     	   			//lg.debug(fp + "calculating SMA +DMI  for single candle");
     	   			value = curr.plusDM();
     	   			curr.smaPlusDM(value);
     	   			//lg.debug(fp + "value of +dmi=" + value);
     	   			break;
     	   		case Constants.EMA_TYPE_MINUS_DM:
     	   			type = "-DM ";
     	   			//lg.debug(fp + "calculating SMA -DMI  for single candle");
     	   			value = curr.minusDM();
     	   			curr.smaMinusDM(value);
     	   			//lg.debug(fp + "value of -dmi=" + value);
     	   			break;
     	  /* 	case Constants.EMA_TYPE_PLUS_DI:
 	   			type = "+DI ";
 	   			//lg.debug(fp + "calculating SMA +DMI  for single candle");
 	   			value = curr.plusDI();
 	   			curr.smaPlusDI(value);
 	   			//lg.debug(fp + "value of +dmi=" + value);
 	   			break;
 	   		case Constants.EMA_TYPE_MINUS_DI:
 	   			type = "-DI ";
 	   			//lg.debug(fp + "calculating SMA -DMI  for single candle");
 	   			value = curr.minusDI();
 	   			curr.smaMinusDI(value);
 	   			//lg.debug(fp + "value of -dmi=" + value);
 	   			break;*/
 	   	case Constants.EMA_TYPE_TRUE_RANGE:
			type = "TrueRange ";
   			//lg.debug(fp + "calculating EMA for True Range");
   			value = curr.trueRange();
   			curr.trueRangeEma(value);
   			break;
     	   		default:
     			   throw new Exception("Unsupported SMA type!");
     	   		}	
        		
        		lg.debug(fp + "SMA " + type + ": " + value);
        		return value;
        	}
        	else if(cc.size()==1){
        		lg.info("SMA: cc has 1 candle: "+cc);
        		Candle prev = cc.peekFirst();
        		
        		switch(emaType){		
     	   		case Constants.EMA_TYPE_CLOSE:
     	   			type = "close ";
     	   			//lg.debug(fp + "calculating SMA close for single candle");
     	   			value = curr.close();	   		
     	   			total = prev.smaClose() + value;
     	   			//lg.debug(fp + "value of close=" + value);
     	   			
     	   		sma = total / 2d;
        		lg.debug(fp + "SMA " + type + " = " + sma);
        		curr.smaClose(sma);
     	   			break;
     	   	/*	case Constants.EMA_TYPE_PLUS_DI:
     	   			type = "+DI ";
     	   			//lg.debug(fp + "calculating SMA +DMI  for single candle");
     	   			value = curr.plusDI();
     	   			total = prev.smaPlusDI() + value;
     	   			//lg.debug(fp + "value of +dmi=" + value);
     	   		sma = total / 2;
        		lg.debug(fp + "SMA " + type + " = " + sma);
        		curr.smaPlusDI(sma);
     	   			break;
     	   		case Constants.EMA_TYPE_MINUS_DI:
     	   			type= "-DI ";
     	   			//lg.debug(fp + "calculating SMA -DMI  for single candle");
     	   			value = curr.minusDI();
     	   			total = prev.smaMinusDI() + value;
     	   			//lg.debug(fp + "value of -dmi=" + value);
     	   			
     	   		sma = total / 2;
        		lg.debug(fp + "SMA " + type + " = " + sma);
        		curr.smaMinusDI(sma);
     	   			break;*/
     	   	case Constants.EMA_TYPE_PLUS_DM:
 	   			type = "+DM ";
 	   			//lg.debug(fp + "calculating SMA +DMI  for single candle");
 	   			value = curr.plusDM();
 	   			total = prev.smaPlusDM() + value;
 	   			//lg.debug(fp + "value of +dmi=" + value);
 	   			
 	   		sma = total / 2d;
    		lg.debug(fp + "SMA " + type + " = " + sma);
    		curr.smaPlusDM(sma);
 	   			break;
 	   		case Constants.EMA_TYPE_MINUS_DM:
 	   			type= "-DM ";
 	   			//lg.debug(fp + "calculating SMA -DMI  for single candle");
 	   			value = curr.minusDM();
 	   			total = prev.smaMinusDM() + value;
 	   			//lg.debug(fp + "value of -dmi=" + value);
 	   			
 	   		sma = total / 2d;
    		lg.debug(fp + "SMA " + type + " = " + sma);
    		curr.smaMinusDM(sma);
 	   			break;
     	   	case Constants.EMA_TYPE_TRUE_RANGE:
				type = "TrueRange ";
       			//lg.debug(fp + "calculating SMA for True Range");
       			value = curr.trueRange();
       			total = prev.trueRangeEma() + value;
       			
       			sma = total / 2d;
        		lg.debug(fp + "SMA " + type + " = " + sma);
        		curr.smaTrueRange(sma);
       			break;
     	   		default:
     			   throw new Exception("Unsupported SMA type!");
     	   		}		
        		
        		
        		return sma;
        	}

        	//more than 1 candle in cache		TODO
     	   	Iterator<Candle> it = cc.descendingIterator();
     	   	int i=1;
     	   	while(it.hasNext()){
     	   		switch(emaType){		
     	   		case Constants.EMA_TYPE_CLOSE:
     	   			type = "close ";
     	   			//lg.debug(fp + "calculating SMA close");
     	   			value = it.next().close();	   		
     	   			//lg.debug(fp + "value of close=" + value);
     	   			break;
     	   		case Constants.EMA_TYPE_PLUS_DI:
     	   			type = "+DI ";
     	   			//lg.debug(fp + "calculating SMA +DI");
     	   			value = it.next().plusDI();
     	   			//lg.debug(fp + "value of +di=" + value);
     	   			break;
     	   		case Constants.EMA_TYPE_MINUS_DI:
     	   			type = "-DI ";
     	   			//lg.debug(fp + "calculating SMA -DI");
     	   			value = it.next().minusDI();
     	   			//lg.debug(fp + "value of -di=" + value);
     	   			break;
     	   	case Constants.EMA_TYPE_PLUS_DM:
 	   			type = "+DM ";
 	   			//lg.debug(fp + "calculating SMA +DM");
 	   			value = it.next().plusDM();
 	   			//lg.debug(fp + "value of +dm=" + value);
 	   			break;
 	   		case Constants.EMA_TYPE_MINUS_DM:
 	   			type = "-DM ";
 	   			//lg.debug(fp + "calculating SMA -DM");
 	   			value = it.next().minusDM();
 	   			//lg.debug(fp + "value of -dm=" + value);
 	   			break;
     	   	case Constants.EMA_TYPE_TRUE_RANGE:
				type = "TrueRange ";
       			//lg.debug(fp + "calculating SMA for True Range");
       			value =  it.next().trueRange();    			
       			break;
     	   		default:
     			   throw new Exception("Unsupported SMA type: " + emaType);
     	   		}		
     	   		
     	   		
     	   	total=total + value;
 	   		//lg.debug(fp + "total value = " + total);
     	   	
     	   		if(i==(period-1))
     	   			break;
     	   		i++;
     	   	}
     	   	
     	   	if(i<(period-1))
     	   		sma = total/(double)((double)i+1d);
     	   	else
     	   		sma = total/(double)period;   	
     	   	  	
     	    lg.debug(fp + "SMA " + type + " = " + sma);
     	    
 	   		switch(emaType){		
 	   		case Constants.EMA_TYPE_CLOSE:
 	   			curr.smaClose(sma);
 	   			break;
 	   		/*
			case Constants.EMA_TYPE_PLUS_DI:
 	   			curr.smaPlusDI(sma);
 	   			break;
 	   		case Constants.EMA_TYPE_MINUS_DI:
 	   			curr.smaMinusDI(sma);
 	   			break;*/
 	   		case Constants.EMA_TYPE_PLUS_DM:
	   			curr.smaPlusDM(sma);
	   			break;
	   		case Constants.EMA_TYPE_MINUS_DM:
	   			curr.smaMinusDM(sma);
	   			break;
	   		case Constants.EMA_TYPE_TRUE_RANGE:
	   			curr.smaTrueRange(sma);
	   			break;
 	   		default:
 			   throw new Exception("Unsupported SMA type: " + emaType);
 	   		}	
     	   	return sma;
     	}
      	
        /**
         * EMA: {<value_to_calc_EMA> - EMA(previous day)} x multiplier + EMA(previous day).
         * 
         * @param period
         * @param emaType
         * @param cc
         * @param curr
         * @return
         * @throws Exception
         */
       	public double EMA(int period, int emaType, ArrayDeque<Candle> cc, Candle curr) throws Exception {
       		String fp = "EMA: ";
       		double result = 0;
       		double ema0 = 0;
       		double value = 0;
       		String type = "";
       		
       		lg.debug(fp + "period: " + period);
       		
       		//Step 1. Calculate SMA
       		double sma = SMA(period,emaType,cc, curr);
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "sma: " + sma);
       		
       		//Step 2. Calculating the weighting multiplier
       		double multiplier = 2d/(period + 1d); 
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "multiplier: " + multiplier);
       		
       		//Step 3: Calculate EMA
       		//get the ema for previous (second last) timeframe unit
       		//Candle tmp = cc.pop();
       		//lg.debug(fp + "popped last candle: " + tmp);
       		Candle prev = cc.peekFirst();
       		lg.debug(fp + "peeked previous last candle: " + prev);
       		switch(emaType){
       	/*	case Constants.EMA_TYPE_MINUS_DI:
       			type = "-DI ";
       			lg.debug(fp + "calculating EMA for -DI");
       			value = curr.minusDI();
       			if(prev!=null)
       				ema0 = prev.minusDIEma();
       			else
       				ema0 = value;    
       			
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0 = " + result);
           		
           		curr.minusDIEma(result);
       			break;
       		case Constants.EMA_TYPE_PLUS_DI:   	
       			type = "+DI ";
       			lg.debug(fp + "calculating EMA for +DI");
       			value = curr.plusDI();
       			if(prev!=null)
       				ema0 = prev.plusDIEma();
       			else
       				ema0 = value;
       			
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0 = " + result);
           		
           		curr.plusDIEma(result);
       			break;*/
       		case Constants.EMA_TYPE_MINUS_DM:
       			type = "-DM ";
       			lg.debug(fp + "calculating EMA for -DM");
       			value = curr.minusDM();
       			if(prev!=null)
       				ema0 = prev.minusDMEma();
       			else
       				ema0 = value;
       			
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0 = " + result);
           		
           		curr.minusDMEma(result);
       			break;
       		case Constants.EMA_TYPE_PLUS_DM:   		
       			type = "+DM ";
       			lg.debug(fp + "calculating EMA for +DM");
       			value = curr.plusDM();
       			if(prev!=null)
       				ema0 = prev.plusDMEma();
       			else
       				ema0 = value;
       			
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0=" + result);
           		
           		curr.plusDMEma(result);
       			break;
			case Constants.EMA_TYPE_TRUE_RANGE:
				type = "TrueRange ";
       			lg.debug(fp + "calculating EMA for True Range");
       			value = curr.trueRange();
       			if(prev!=null)
       				ema0 = prev.trueRangeEma();
       			else
       				ema0 = value;
       			
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0=" + result);
           		
           		curr.trueRangeEma(result);
       			break;
       		case Constants.EMA_TYPE_CLOSE:
       			lg.debug(fp + "calculating EMA for closing price");
       			ema0 = prev.emaShort();
       			value = curr.close();
       			lg.debug(fp + "ema0 " + type+ ":" + ema0 + ", curr value: " + value);
           		result = (value - ema0)*multiplier+ema0;
           		lg.debug(fp + "result of "+type+"(value - ema0)*multiplier+ema0=" + result);
           		
           		curr.closeEma(result);
       			break;
       			default:
       			 throw new Exception("Unsupported EMA type!");   		
       		}     		
       		
       		
       		//put back popped candle
       		//cc.push(tmp);
       		//lg.debug(fp + "pushed back last candle");
       		return result;
       	}
     	
 
       	/**
    	 * First start with the True Range indicator which is the greatest of the following: 
    	 * 		current high less the current low, 
    	 * 		the absolute value of the current high less the previous close and 
    	 * 		the absolute value of the current low less the previous close. 
    	 * 
    	 * The average true range is a moving average, generally 14 days, of the true ranges.
    	 * 
    	 * ATR Calculation Example
    	 * Traders can use shorter periods to generate more trading signals, 
    	 * while longer periods have a higher probability to generate less trading signals. 
    	 * For example, 
    	 * assume a short-term trader only wishes to analyze the volatility of a stock over a period of five trading days. 
    	 * Therefore, the trader could calculate the five-day ATR. 
    	 * Assuming the historical price data is arranged in reverse chronological order, 
    	 * the trader finds the maximum of the absolute value of the current high minus the current low, 
    	 * absolute value of the current high minus the previous close 
    	 * and the absolute value of the current low minus the previous close. 
    	 * These calculations of the true range are done for the five most recent trading days 
    	 * and are then averaged to calculate the first value of the five-day ATR.
    	 * 
    	 * Assume the first value of the five-day ATR is calculated at 1.41 and the sixth day has a true range of 1.09. 
    	 * The sequential ATR value could be estimated by multiplying the previous value of the ATR by the number of days less one, 
    	 * and then adding the true range for the current period to the product. Next, divide the sum by the selected timeframe. 
    	 * For example, the second value of the ATR is estimated to be 1.35, or (1.41 * (5 - 1) + (1.09)) / 5. 
    	 * The formula could be repeated over the entire time period.
    	 * 
    	 * @return
    	 */
   /* 	public double ATR(int period, ArrayDeque<Candle> cc, Candle curr) throws Exception {    		
    		double atr = 0; 	
    		//if cache size is less than period, calculate ATR for available candles   		
    		double totalTR = 0;		
    		//Candle tmp = cc.pop();
    		Candle prev = null;
    		
    		//cache has only one candle - current, which is passed separately
    		if(cc.size()<=0){
    			atr = curr.trueRange();
    			curr.ATR(curr.trueRange());
    			return atr;
    		}
    	
    		//	else cc.size() without last candle > 0
    		prev = cc.peekFirst();
    		
    		// get true range for the current candle
    		double currentTR = TrueRange(curr, prev);
    		//lg.trace("AverageTrueRange: currentTR: " + currentTR);

    		//1 or more candle(s) in a cache after popped last one out
    		    		
    		//loop
    		int count = 0;
    		Candle c;
    		Iterator<Candle> it = cc.descendingIterator();
    		//Stack<Candle> stack = new Stack<>();
    		LinkedList<Candle> queue = new LinkedList<Candle>();
    		while(it.hasNext()){			
    			queue.addFirst(it.next());
    			count++;
    			
    			if(count>=(period-1)) //one candle is popped!
    				break;
    		}
    		//lg.trace("AverageTrueRange: put " + count + " candles in the stack: " + stack.size());
    	
    		//add currentTR from the newest candle
    		totalTR = totalTR + currentTR;
    		prev = null;  //reset
    		while(!queue.isEmpty()){
    			c = queue.removeLast(); //starting from newest candle
    			currentTR = c.trueRange();
    			//lg.trace("AverageTrueRange: currentTR from candle: " + currentTR);
    			if(currentTR==0){
    				//for some reason true range was not calculated
    				currentTR = TrueRange(c,prev);
    				//	lg.trace("AverageTrueRange: freshly calculated currentTR: " + currentTR);
    			}
    			totalTR = totalTR + currentTR;
    			//lg.trace("AverageTrueRange: totalTR: " + totalTR);
    			prev = c;
    		}
    		
    		atr = totalTR / (count + 1);
    	//	lg.trace("AverageTrueRange: atr=" + atr);
    		curr.ATR(atr);	//set value to the current candle
    		
    		
    		//double priorATR = cc.peekFirst().ATR();
    		
    		//- Multiply the previous 14-day ATR by 13.
			//  - Add the most recent day's TR value.
			//  - Divide the total by 14
    		//atr = ((priorATR * (period - 1)) + currentTR) / period;
    		
    		//lg.debug("AverageTrueRange: " + atr);
    		//put back popped candle
    		//cc.push(tmp);
    		return atr;	//cannot be 0 !!
    	}
*/
    	
    	/**
    	 * True Range is the greatest of the following: 
    	 * 		current high minus (-) the current low, 
    	 * 		the absolute value of the current high minus (-)  the previous close and 
    	 * 		the absolute value of the current low minus (-) the previous close. 
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    	//private double TrueRange(ArrayDeque<Candle> cc){
    	private double TrueRange(Candle curr, Candle prev) throws Exception {
    		double value = 0;
    		double tr1 = 0;
    		double tr2 = 0;
    		double tr3 = 0;
    		//Candle tmp = cc.pop();
    		
    		double currHigh = curr.high();
    		double currLow =  curr.low(); 
    		double prevClose = 0;
    		
    		try {
    			prevClose = prev.close();
    		}
    		catch(Exception e){
    			lg.error("TrueRange: failed to get prevClose: " + e.getMessage());
    		}
    		//current high minus current low, 
    		tr1 = currHigh - currLow;
    		//if(lg.isTraceEnabled())
    		//	lg.trace("TrueRange: currHigh - currLow = " + tr1);
    		value = tr1;
    		
    		//if prevClose available for calculation
    		if(prevClose > 0){
       	 	//the absolute value of the current high minus previous close 
    			tr2 = Math.abs(currHigh - prevClose);
    			//if(lg.isTraceEnabled())
    			//	lg.trace("TrueRange: currHigh - prevClose = " + tr2);
    		
       	 		//the absolute value of the current low minus previous close. 
    			tr3 = Math.abs(currLow - prevClose);
    			//if(lg.isTraceEnabled())
    			//	lg.trace("TrueRange: currLow - prevClose = " + tr3);
    			
    			if(tr2>value)
        			value = tr2;
        		
        		if(tr3>value)
        			value = tr3;
    		}
    		
    		//set true range value to candle
    		curr.trueRange(value);
    		//put back popped candle
    		//cc.push(tmp);
    		
    		//if(lg.isTraceEnabled())
    		//lg.trace("TrueRange: " + value);
    		
    		return value; 
    	}
    	
  /*  	public double MACD(int periodShort, int periodLong, int tfSize, String tfUnit, ArrayDeque<Candle> q, Candle c) throws Exception { 
    		//TODO
    		double fastEMA = EMA(periodShort, tfSize, tfUnit, q); 
    		double slowEMA = EMA(periodLong, tfSize, tfUnit, q); 
    		double value = fastEMA - slowEMA; 
    		 
    		return value; 
    	}*/
    	
    	public void support(ArrayDeque<IBar> lows){
    		//TODO
    	}
    	
    	public void resistance(ArrayDeque<IBar> highs){
    		//TODO
    	}
    	
    	/**
    	 * Calculates average body length, top and bottom wicks length for PREVIOUS <period> candles (without current candle)
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    	public void calcAvgLengths(int period, ArrayDeque<Candle> cc){
    		String fp = "calcAvgLengths: ";
    		double resultBottom = 0;
    		double resultTop = 0;
    		double resultBody = 0;
    		
    		double low = 0;
    		double top = 0;
    		double body = 0;
    		
    		Stack<Candle> stack = new Stack<>();
    		if(lg.isTraceEnabled())
    			lg.trace(fp + "created stack");
    		
    		//take out the latest candle
    		Candle last = cc.pop();
    		if(lg.isTraceEnabled())
    			lg.trace(fp + "popped the latest candle: " + last);
    		stack.push(last);
    		if(lg.isTraceEnabled())
    			lg.trace(fp + "pushed the latest candle in the stack");
    		
    		int i=0;
    		for(i=0;i<period;i++){
    			if(cc.isEmpty())
    				break;
    			//get the wick len
    			low = low + cc.peek().getLower_shadow_len();
    			top = top + cc.peek().getLower_shadow_len();
    			body = body + cc.peek().getBody_len();
    			//take candle out and save it in a temporary stack
    			stack.push(cc.pop());
    		}
    		if(i>0 && i<(period-1)){
    			resultBottom = low/i;
        		resultTop = top/i;
        		resultBody = body/i;
    		}
    		else{
    		//calculate result
    		resultBottom = low/period;
    		resultTop = top/period;
    		resultBody = body/period;
    		}
    		lg.debug(fp + "avg body: " + resultBody + ", avg top shadow: " + resultTop + ", avg bottom shadow: " + resultBottom);
    		
    		//put back all candles
    		while(!stack.isEmpty()){
    			cc.push(stack.pop());
    		}
    		
    		last.setAvgBodyLen(resultBody);
    		last.setAvgUpperShadowLen(resultTop);
    		last.setAvgLowerShadowLen(resultBottom);
    		cc.push(last);  //put back last
    	}
       
    	/**
    	 * Calculates position size for the case when account denomination is the same as the 
    	 * counter (quote) currency
    	 * 
    	 * @param acctEquity
    	 * @param contract
    	 * @param riskPercent
    	 * @param stopLossPips
    	 * @param xRates
    	 * @return
    	 */
    	public double calcForexPositionSizeAcctInQuote(double acctEquity, //11900.00  USD
    													Contract contract,  // EUR / USD
    													double riskPercent, //0.01
    													double stopLossPips, // 3
    													double xRate
    	){
    		double pSize = 0;
    		
    		// use account balance and risk percentage calculate dollar amount risked
    		double riskAmount = acctEquity * riskPercent;
    		// 11900 * 0.01 = 119 USD
    		
    		//divide amount risked by the stop in pips to find the value per pip
    		double pipValue = riskAmount / stopLossPips;
    		// 119 / 3 = 39.67 
    		
    		// multiply value per pip by a known unit/pip value ratio of EUR/USD
    		// in this case with 10K units (or one mini lot) each pip move is worth 1 USD
    		pSize = pipValue * (10000 / 1);
    		// 39.67 * 10000 = 39670.00
    		
    		return pSize;
    	}
    	
     	/**
    	 * Calculates position size for the case when account denomination is the same as the 
    	 * base currency
    	 * 
    	 * @param acctEquity
    	 * @param contract
    	 * @param riskPercent
    	 * @param stopLossPips
    	 * @param xRates
    	 * @return
    	 */
    	public double calcForexPositionSizeAcctInBase(double acctEquity, //11900.00  USD
    													Contract contract,  // EUR / USD
    													double riskPercent, //0.01
    													double stopLossPips, // 3
    													double xRate		// EUR/USD = 1.5000
    	){
    		double pSize = 0;
    		
    		// use account balance and risk percentage calculate EUR amount risked
    		double riskInBaseAmount = acctEquity * riskPercent;
    		// 11900 * 0.01 = 119 EUR
    		
    		// Now we have to convert this to USD because the value of a currency pair 
    		// is calculated by the counter currency. Lets say,the current exchange rate 
    		// for 1 EUR is 1.5 USD
    		// All we have to do is to find the value in USD is to invert the current exchange rate 
    		// for EUR/ USD and multiply by the amount of euros we wish to risk
    		// (USD 1.5000 / EUR 1.0000) * EUR 119 = 178.5 USD
    		double riskInCounterCurr = (xRate / 1) * riskInBaseAmount;
    		
    		//divide amount risked by the stop in pips to find the value per pip
    		double pipValue = riskInCounterCurr / stopLossPips;
    		// 178.5 / 3 = 59.5
    		
    		// multiply value per pip by a known unit/pip value ratio of EUR/USD
    		// in this case with 10K units (or one mini lot) each pip move is worth 1 USD
    		pSize = pipValue * (10000 / 1);
    		// 59.5 * 10000 = 59500.00
    		
    		return pSize;
    	}
 
    	public double calcForexEmaShortClose(int period, ArrayDeque<Candle> cc) throws Exception{
    		String fp = "calcForexEmaShortClose: ";
    		//lg.debug(fp + "calculating EMA for closing price: period: " + period);
    		
    		Candle curr = cc.pop();
    		
       		//Step 1. Calculate SMA
       		double sma = calcForexSmaClose(period,cc, curr);
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "sma: " + sma);
       		
       		//Step 2. Calculating the weighting multiplier
       		double multiplier = 2d/(period + 1d); 
       		//if(lg.isDebugEnabled())
       		//	lg.debug(fp + "multiplier: " + multiplier);
       		
    		Candle prev = cc.peek();
   			double ema0 = prev.emaShort();
   			double value = curr.close();
   			lg.debug(fp + "ema0 :" + ema0 + ", curr value: " + value);
       		double result = (value - ema0)*multiplier+ema0;
       		//lg.debug(fp + "result of (value - ema0)*multiplier+ema0=" + result);
       		lg.debug(fp + "emaShort: " + result);
       		//curr.emaShort(result);
       		cc.push(curr); //put it back
       		
       		return result;
    	}
    	
    	public double calcForexEmaLongClose(int period, ArrayDeque<Candle> cc) throws Exception{
    		String fp = "calcForexEmaLongClose: ";
    		//lg.debug(fp + "calculating EMA for closing price: period: " + period);
    		
    		Candle curr = cc.pop();
    		
       		//Step 1. Calculate SMA
       		double sma = calcForexSmaClose(period,cc, curr);
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "sma: " + sma);
       		
       		//Step 2. Calculating the weighting multiplier
       		double multiplier = 2d/(period + 1d); 
       		//if(lg.isDebugEnabled())
       		//	lg.debug(fp + "multiplier: " + multiplier);
       		
    		Candle prev = cc.peek();
   			double ema0 = prev.emaLong();
   			double value = curr.close();
   			lg.debug(fp + "ema0 :" + ema0 + ", curr value: " + value);
       		double result = (value - ema0)*multiplier+ema0;
       		//lg.debug(fp + "result of (value - ema0)*multiplier+ema0=" + result);
       		lg.debug(fp + "emaLong: " + result);
       		//curr.emaShort(result);
       		cc.push(curr); //put it back
       		
       		return result;
    	}
    	
    	public double calcForexSmaClose(int period, ArrayDeque<Candle> cc, Candle c) throws Exception {
    		double sma = 0;
    		double sum = 0;
    		int cnt = 0;
    		
    		sum = sum + c.close();
    		
    		Iterator<Candle> it = cc.iterator();
    		while(it.hasNext()){
    			cnt++;			
    			if(cnt>(period-1))
    				break;
    			sum=+it.next().close();
    		}
    		lg.debug("calcForexSmaClose: sum of " + cnt + " elements for period " + period);
    		
    		sma = sum / period;
    		//lg.debug("calcForexSmaShortClose: sma: " + sma);
    		return sma;
    	}

       public static void main(String[] args){
              Calculator t = new Calculator();
            //  if(lg.isDebugEnabled())
             //     lg.debug("test created");
      
              int cacheSize = 30;
              String timeframeUnit = "MIN";
              int timeframeSize = 10;
              double basePrice = 18.7;
              int period = 5;
              ArrayDeque<Candle> initCache = Utils.fillCache(cacheSize, timeframeUnit, timeframeSize, basePrice);
             //String fn = "C:/run/other/cache";
              //ArrayDeque<Candle> initCache = readReusableCache(fn);
            //  lg.info("initCache: " +  initCache);
              
              ArrayDeque<Candle> cache = new ArrayDeque<>();
        	  try{
        		  while(!initCache.isEmpty()){         	        
            		  cache.push(initCache.removeLast());
            		  t.calcADX_DMI(period, cache);
        		  }
        	  }
        	  catch(Exception e){
        	  lg.error("Error calculating ADX: " + e.getMessage());
        	  }
        	  
        	  lg.info("Last candle: " + cache.peekFirst());
       }     
       
       @SuppressWarnings("unchecked")
	private static ArrayDeque<Candle> readReusableCache(String fn){
    	   ObjectInputStream fw=null;
    	   ArrayDeque<Candle> q = null;
    	   try{
           	fw = new ObjectInputStream(new FileInputStream(fn));
           	q = (ArrayDeque<Candle>)fw.readObject();
           }
           catch(Exception e){
           	lg.error("Error reading cache: " + e.getMessage());
           }
           finally{
           	try{
       
           		fw.close();
           	}
           	catch(Exception ex){}
           }
    	   
    	   return q;
       }
}
