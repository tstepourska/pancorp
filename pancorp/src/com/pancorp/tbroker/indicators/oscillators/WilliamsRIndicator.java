/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.pancorp.tbroker.indicators.oscillators;

//import com.pancorp.tbroker.indicators.Indicator;
//import com.pancorp.tbroker.Decimal;
//import com.pancorp.tbroker.ta.TimeSeries;
//import com.pancorp.tbroker.util.Calculator;

//import java.math.BigDecimal;
///import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Iterator;
//import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.NotEnoughDataException;
//import com.pancorp.tbroker.indicators.CachedIndicator;
//import com.pancorp.tbroker.indicators.helpers.HighestValueIndicator;
//import com.pancorp.tbroker.indicators.helpers.LowestValueIndicator;
//import com.pancorp.tbroker.indicators.simple.ClosePriceIndicator;
import com.pancorp.tbroker.indicators.simple.MaxIndicator;
import com.pancorp.tbroker.indicators.simple.MinIndicator;
import com.pancorp.tbroker.model.Candle;
///import com.pancorp.tbroker.model.IBar;


/**
 * Stochastic oscillator K.
 * <p>
 * Receives timeSeries and timeFrame and calculates the StochasticOscillatorKIndicator
 * over ClosePriceIndicator, or receives an indicator, MaxPriceIndicator and
 * MinPriceIndicator and returns StochasticOsiclatorK over this indicator.
 * 
 */
public class WilliamsRIndicator //extends CachedIndicator<Double> 
{
	private static Logger lg = LogManager.getLogger(WilliamsRIndicator.class);
	private static final int DEFAULT_PERIOD = 9;
	MaxIndicator max;
	MinIndicator min;
    
    public WilliamsRIndicator() {
    	max = new MaxIndicator();
    	min = new MinIndicator();
    }

    /**
     * wr = (highestHigh - latestClose)/(highestHigh - lowestLow)*(-100);
     * 
     * @param cc
     * @param pd
     * @return
     * @throws NotEnoughDataException
     * @throws Exception
     */
    public double calculate(ArrayDeque<Candle> cc, int pd) throws NotEnoughDataException, Exception{ //int index) {

    	if(pd<=0)
    		pd = DEFAULT_PERIOD;
    	if(cc.size()<pd)
    		throw new NotEnoughDataException();
    	
    	double latestClose = cc.peekFirst().close();
    	lg.debug("latestClose: " + latestClose);
    	
    	//create temporary stacks
    	ArrayDeque<Double> highs = new ArrayDeque<>();
    	ArrayDeque<Double> lows = new ArrayDeque<>();
    	//create iterator over cache - from the head
    	Iterator<Candle> it = cc.iterator();
    	while(it.hasNext()){
    		//collect highs and lows from <pd> candles - latest first, earliest remains at the head of temp stack
    		highs.push(new Double(it.next().high()));
    		lows.push(new Double(it.next().low()));
    		if(lows.size()>=pd)
    			break;
    	}
    	
    	double highestHighForPeriod = max.calculateDouble(highs);
    	lg.debug("calculate: highestHighForPeriod for " + highs.size()+ " candles : " + highestHighForPeriod);
    	
    	/*//reset temprary stack
    	dd = new ArrayDeque<>();
    	//reset iterator over cache
    	it = cc.iterator();
    	while(it.hasNext()){
    		//collect lows from <pd> candles
    		dd.push(new BigDecimal(it.next().low()));
    		if(dd.size()>=pd)
    			break;
    	}*/
 
    	double lowestLowForPeriod = min.calculateDouble(lows);
		lg.debug("calculate: lowestLowForPeriod for " +lows.size()+ " candles: " + lowestLowForPeriod);
		
		//BigDecimal close = new BigDecimal(cc.peekFirst().close());
		//lg.debug("calculate: latest close: " + close);
		
		//CL = Close [today] - Lowest Low [in %K Periods] 
		//BigDecimal CL = close.subtract(minLowForPeriod);
		//lg.debug("calculate: CL: " + CL);
		
		//HL =Highest High [in %K Periods] - Lowest Low [in %K Periods] 
		//BigDecimal HL = maxHighForPeriod.subtract(minLowForPeriod);
		//lg.debug("calculate: HL: " + HL);
		
		//BigDecimal K = (CL.divide(HL, 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
		//lg.debug("calculate: K:" + K);
		
		double wr = (highestHighForPeriod - latestClose)/(highestHighForPeriod - lowestLowForPeriod)*(-100);
		lg.debug("calculate: wr: " + wr);

		return wr;
    }

   // @Override
   // public String toString() {
   //     return getClass().getSimpleName() + " period: " + period;
   // }
}
