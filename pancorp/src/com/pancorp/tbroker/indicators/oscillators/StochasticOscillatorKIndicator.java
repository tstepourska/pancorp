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

import com.pancorp.tbroker.indicators.Indicator;
//import com.pancorp.tbroker.Decimal;
import com.pancorp.tbroker.ta.TimeSeries;
import com.pancorp.tbroker.util.Calculator;

import java.util.ArrayDeque;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.indicators.CachedIndicator;
import com.pancorp.tbroker.indicators.helpers.HighestValueIndicator;
import com.pancorp.tbroker.indicators.helpers.LowestValueIndicator;
import com.pancorp.tbroker.indicators.simple.ClosePriceIndicator;
import com.pancorp.tbroker.indicators.simple.MaxPriceIndicator;
import com.pancorp.tbroker.indicators.simple.MinPriceIndicator;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;


/**
 * Stochastic oscillator K.
 * <p>
 * Receives timeSeries and timeFrame and calculates the StochasticOscillatorKIndicator
 * over ClosePriceIndicator, or receives an indicator, MaxPriceIndicator and
 * MinPriceIndicator and returns StochasticOsiclatorK over this indicator.
 * 
 */
public class StochasticOscillatorKIndicator //extends CachedIndicator<Double> 
{
	private static Logger lg = LogManager.getLogger(StochasticOscillatorKIndicator.class);
	
	private static final long serialVersionUID = -2343568223729181598L;

//	private final Indicator<Double> indicator;
    private final int period;
    //private MaxPriceIndicator maxPriceIndicator;
    //private MinPriceIndicator minPriceIndicator;
    Calculator calc;
    //ArrayDeque<IBar> cache;
    ArrayDeque<IBar> kStack;

   /* public StochasticOscillatorKIndicator(TimeSeries timeSeries, int timeFrame) {
        this(new ClosePriceIndicator(timeSeries), timeFrame, new MaxPriceIndicator(timeSeries), new MinPriceIndicator(timeSeries));
    }*/
    
    public StochasticOscillatorKIndicator(ArrayDeque<IBar> cc, int pd) {
    	calc = new Calculator();
    	//cache = cc;
    	kStack = cc;
    	period = pd;
       // this(new ClosePriceIndicator(timeSeries), timeFrame, new MaxPriceIndicator(timeSeries), new MinPriceIndicator(timeSeries));
    }
/*
    public StochasticOscillatorKIndicator(Indicator<Double> indicator, int timeFrame,
            MaxPriceIndicator maxPriceIndicator, MinPriceIndicator minPriceIndicator) {
        super(indicator);
        this.indicator = indicator;
        this.timeFrame = timeFrame;
        this.maxPriceIndicator = maxPriceIndicator;
        this.minPriceIndicator = minPriceIndicator;
    }*/

   // @Override
    public double calculate() { //int index) {
    	//ArrayDeque<IBar> kStack = new ArrayDeque<>();
    	//for(int i=0;i<period;i++){
    	//	kStack.push(cache.pop());
    	//}
       // HighestValueIndicator highestHigh = //new HighestValueIndicator(maxPriceIndicator, timeFrame);
    	double highestHigh = calc.maxHigh(kStack);
       // LowestValueIndicator lowestMin = new LowestValueIndicator(minPriceIndicator, timeFrame);
    	double lowestLow = calc.minLow(kStack);

       // Double highestHighPrice = highestHigh.getValue(index);
       // Double lowestLowPrice = lowestMin.getValue(index);
    	
		lg.debug("calcStochastic: kLowestLow: " + lowestLow);
		lg.debug("calcStochastic: kHighestHigh: " + highestHigh);
		double close = kStack.peek().close();
		
		//CL = Close [today] - Lowest Low [in %K Periods] 
		double CL = close - lowestLow;
		lg.debug("calcStochastic: CL: " + CL);
		
		//HL =Highest High [in %K Periods] - Lowest Low [in %K Periods] 
		double HL = highestHigh - lowestLow;
		lg.debug("calcStochastic: HL: " + HL);
		
		double K = (CL / HL) * 100;
		lg.debug("calcStochastic: K:" + K);

       // return indicator.getValue(index).minus(lowestLowPrice)
        //        .dividedBy(highestHighPrice.minus(lowestLowPrice))
         //       .multipliedBy(100);//Double.HUNDRED);
		return K;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " period: " + period;
    }
}
