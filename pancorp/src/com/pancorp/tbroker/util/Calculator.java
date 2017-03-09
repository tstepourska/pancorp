package com.pancorp.tbroker.util;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;

import com.pancorp.tbroker.math.LinearRegression;
import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;
 
public class Calculator {
	private static org.apache.logging.log4j.Logger lg = LogManager.getLogger(Calculator.class);
    public static long tfFactor = 0;
   // private static ArrayDeque<Candle> queue;

      
/*     public void setQueue(LinkedList<Bar> q){
              this.queue = q;
       }
       */
      
       public Calculator(int periodSh, int periodLg, int timeframeSize, String timeframeUnit, long tfFactor2) {
		// TODO Auto-generated constructor stub
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
			double min = 0;
			double t= 0;
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
    	
    	public double linearRegressionSlope(ArrayDeque<Candle> cc,Candle c){
    		//y = mx + b  - we are finding b
    		double b = LinearRegression.calculateLinearRegressionSlope(cc,c);
    		if(lg.isDebugEnabled())
    		lg.debug("slope: " + b);
    		
    		return b;
    	}
    	
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
    	public double ADX(int period, ArrayDeque<Candle> cc) throws Exception {
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
    	}
    	
    	/**
    	 * Absolute value of (+DI minus -DI) divided by (+DI plus -DI)
    	 * 
    	 * @param period
    	 * @param cc
    	 * @param c
    	 * @return
    	 */
    	public double adxFactor(int period, ArrayDeque<Candle> cc) throws Exception {
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
    	}
    	
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
    	private double plusDMI(int period, ArrayDeque<Candle> cc) throws Exception {
    		
    		
    		double plusDMI = 100 * EMA(period, Constants.EMA_TYPE_PLUS_DMI, cc) / AverageTrueRange(period, cc);
    		return plusDMI;
    	}
    	
    	/**
    	 * The negative directional indicator, or -DI, equals 100 times the exponential moving average 
    	 * of -DM divided by the average true range (ATR).
    	 * 
    	 * If the downmove is greater than the upmove and greater than zero, the -DM equals the downmove; 
    	 * otherwise, it equals zero. It is calculated in the adxFactor method, so calculation is not 
    	 * completed here, that is why  this method is private.
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    	private double minusDMI(int period, ArrayDeque<Candle> cc) throws Exception {
    		double minusDMI = 100 * EMA(period, Constants.EMA_TYPE_MINUS_DMI,cc) / AverageTrueRange(period,cc);
    		return minusDMI;
    	}  	
    	
        public double SMA(int smaPeriod, int emaType, ArrayDeque<Candle> cc) throws Exception {
     	   	double sma = 0;
     	   	double total = 0;
     	   	double value;
     	   		
     	   	Iterator<Candle> it = cc.descendingIterator();
     	   	int i=1;
     	   	while(it.hasNext()){
     	   		switch(emaType){		
     	   		case Constants.EMA_TYPE_CLOSE:
     	   			value = it.next().close();	   			
     	   			break;
     	   		case Constants.EMA_TYPE_PLUS_DMI:
     	   			value = it.next().plusDMI();
     	   			break;
     	   		case Constants.EMA_TYPE_MINUS_DMI:
     	   			value = it.next().minusDMI();
     	   			break;
     	   		case Constants.EMA_TYPE_ADX_FACTOR:
     	   			value = it.next().adxFactor();
     	   			break;
     	   		default:
     			   throw new Exception("Unsupported EMA type!");
     	   		}		
     	   		total+=value;
     	   		if(i==smaPeriod)
     	   			break;
     	   		i++;
     	   	}

     	   	sma = total/smaPeriod;   	   		
     	   	return sma;
     	}
      	
       	public double EMA(int emaPeriod, int emaType, ArrayDeque<Candle> cc) throws Exception {
       		String fp = "EMAadxFactor: ";
       		double result = 0;
       		double ema0 = 0;
       		double value = 0;
       		
       		//Step 1. Calculate SMA
       		double sma = SMA(emaPeriod,emaType,cc);
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "sma: " + sma);
       		
       		//Step 2. Calculating the weighting multiplier
       		double multiplier = 2/emaPeriod + 1;
       		if(lg.isDebugEnabled())
       			lg.debug(fp + "multiplier: " + multiplier);
       		
       		//Step 3: Calculate EMA
       		//get the ema for previous (second last) timeframe unit
       		Candle tmp = cc.pop();
       		switch(emaType){
       		case Constants.EMA_TYPE_MINUS_DMI:
       			ema0 = cc.peekFirst().minusDMIEma();
       			value = tmp.minusDMI();
       			break;
       		case Constants.EMA_TYPE_PLUS_DMI:
       			ema0 = cc.peekFirst().plusDMIEma();
       			value = tmp.plusDMI();
       			break;
       		case Constants.EMA_TYPE_ADX_FACTOR:
       			ema0 = cc.peekFirst().adxFactorEma();
       			value = tmp.adxFactorEma();
       			break;
       		case Constants.EMA_TYPE_CLOSE:
       			ema0 = cc.peekFirst().emaShort();
       			value = tmp.close();
       			break;
       			default:
       			 throw new Exception("Unsupported EMA type!");   		
       		}     		
       		result = (value - ema0)*multiplier+ema0;
       		
       		//put back popped candle
       		cc.addFirst(tmp);
       		
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
    	public double AverageTrueRange(int period, ArrayDeque<Candle> cc) throws Exception {
    		double value = 0;
    		//only 1 candle in a deque
    		if(cc.size()==1)
    			return cc.peekFirst().trueRange();

    		Candle tmp = cc.pop();
    		double priorATR = cc.peekFirst().ATR();
    		double currentTR = tmp.trueRange();
    		
    		//- Multiply the previous 14-day ATR by 13.
			//  - Add the most recent day's TR value.
			//  - Divide the total by 14
    		value = ((priorATR * (period - 1)) + currentTR) / period;
	
    		//put back popped candle
    		cc.addFirst(tmp);
    		return value;
    	}
    	
    	/**
    	 * True Range is the greatest of the following: 
    	 * 		current high less the current low, 
    	 * 		the absolute value of the current high less the previous close and 
    	 * 		the absolute value of the current low less the previous close. 
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    	public double TrueRange(int period, ArrayDeque<Candle> cc){
    		double value = 0;
    		Candle tmp = cc.pop();
    		
    		double currHigh = tmp.high();// this.maxHigh(cc);
    		double currLow =  tmp.low(); //this.minLow(cc);
    		double prevClose = cc.peekFirst().close();
    		//current high less the current low, 
    		double tr1 = currHigh - currLow;
       	 	//the absolute value of the current high less the previous close 
    		double tr2 = Math.abs(currHigh - prevClose);
       	 	//the absolute value of the current low less the previous close. 
    		double tr3 = Math.abs(currLow - prevClose);
    		
    		value = tr1;
    		
    		if(tr2>tr1)
    			value = tr2;
    		
    		if(tr3>tr2)
    			value = tr3;
    		//put back popped candle
    		cc.addFirst(tmp);
    		
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
    		
    	}
    	
    	public void resistance(ArrayDeque<IBar> highs){
    		
    	}
    	
    	/**
    	 * Calculates average body length, top and bottom wicks length for PREVIOUS <period> candles (without current candle)
    	 * 
    	 * @param period
    	 * @param cc
    	 * @return
    	 */
    	public void calcAvgLengths(int period, ArrayDeque<Candle> cc){
    		double resultBottom = 0;
    		double resultTop = 0;
    		double resultBody = 0;
    		
    		double low = 0;
    		double top = 0;
    		double body = 0;
    		
    		Stack<Candle> ts = new Stack<>();
    		//take out the latest candle
    		ts.push(cc.pop());
    		
    		for(int i=0;i<period;i++){
    			//get the wick len
    			low = low + cc.peek().getLower_shadow_len();
    			top = top + cc.peek().getUpper_shadow_len();
    			body = body + cc.peek().getBody_len();
    			//take candle out and save it in a temporary stack
    			ts.push(cc.pop());
    		}
    		//calculate result
    		resultBottom = low/period;
    		resultTop = top/period;
    		resultBody = body/period;
    		
    		//put back all candles
    		while(!ts.isEmpty()){
    			cc.push(ts.pop());
    		}
    		
    		cc.peek().setAvgBodyLen(resultBody);
    		cc.peek().setAvgUpperShadowLen(resultTop);
    		cc.peek().setAvgLowerShadowLen(resultBottom);
    	}
       
       public static class NotEnoughDataException extends Exception {
              private static final long serialVersionUID = -1908829006025105879L;
              public NotEnoughDataException(){}
       }
 
       public static void main(String[] args){
              Calculator t = new Calculator();
              if(lg.isDebugEnabled())
                  lg.debug("test created");
             
              double basePrice= 28.7;
              int smaSize=  30;
              Candle b;
              double dev = 0;
              boolean positive = true;
              int volume = 300000;
              int cnt= 10;
              long queueSize = 500;
              String timeframeUnit = "MIN";
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
              int timeframeSize = 15;
              ArrayDeque<Candle> queue = new ArrayDeque<>();
              double closeP = 28.7;
              double sma = 0;
              if(lg.isDebugEnabled())
                  lg.debug("variables assigned");
             
              Random rValue = new Random(System.currentTimeMillis());
              Random rSign = new Random(System.currentTimeMillis());
              if(lg.isDebugEnabled())
                  lg.debug("seeded randomizers");
             
              for(int i=0;i<queueSize; i++) {
                     dev = rValue.nextDouble();
                     positive = rSign.nextBoolean();
                    
                     if(!positive)
                           dev = Math.abs(dev) * (-1);
                    
                     closeP = closeP+dev;
                     if(lg.isDebugEnabled())
                         lg.debug("closeP: " + closeP);
                     b= new Candle(0,
                                  basePrice,
                                  basePrice,
                                  basePrice,
                                  closeP,       //close!
                                  basePrice,
                                  volume,
                                  cnt
                                  );
                     queue.addFirst(b);        
                    
                     try {
                           //sma = t.SMA(smaSize, timeframeSize, timeframeUnit,queue);
                           if(lg.isDebugEnabled())
                               lg.debug("sma["+i+"]: " + sma);
                     }
                    /* catch(NotEnoughDataException e){
                           try {
                                  Thread.sleep(400);
                           }catch(InterruptedException ie){}
                           continue;
                     }*/
                     catch(Exception e){
                           lg.error("Error: " + e.getMessage());
                     }
              }
              if(lg.isDebugEnabled())
                  lg.debug("Queue is full: " + queue.size());
       }     
}
