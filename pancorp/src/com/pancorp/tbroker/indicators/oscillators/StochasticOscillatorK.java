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

import java.util.ArrayDeque;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.indicators.simple.MaxIndicator;
import com.pancorp.tbroker.indicators.simple.MinIndicator;
import com.pancorp.tbroker.model.Candle;

/**
 * Stochastic oscillator K.
 * <p>
 * Receives timeSeries and timeFrame and calculates the StochasticOscillatorK
 * over ClosePriceIndicator, or receives an indicator, MaxPriceIndicator and
 * MinPriceIndicator and returns StochasticOsiclatorK over this indicator.
 * 
 */
public class StochasticOscillatorK 
{
	private static Logger lg = LogManager.getLogger(StochasticOscillatorK.class);
	
	MaxIndicator max;
	MinIndicator min;
    
    public StochasticOscillatorK() {
    	max = new MaxIndicator();
    	min = new MinIndicator();
    }

    /**
     * 100(C - L14)/(H14 - L14)
     * 
     * @param cc
     * @param pd
     * @return
     * @throws NotEnoughDataException
     * @throws Exception
     */
    public double calculate(ArrayDeque<Candle> cc, int pd) throws NotEnoughDataException, Exception{

    	//create temporary stack
    	ArrayDeque<Double> dd = new ArrayDeque<>();
    	//create iterator over cache
    	Iterator<Candle> it = cc.iterator();
    	while(it.hasNext()){
    		//collect highs from <pd> candles
    		dd.push(it.next().high());
    		if(dd.size()>=pd)
    			break;
    	}

    	double maxHighForPeriod = max.calculateDouble(dd);
    	lg.debug("calculate: maxHighForPeriod for " + dd.size()+ " candles : " + maxHighForPeriod);

    	double minLowForPeriod = min.calculateDouble(dd);
		lg.debug("calculate: minLowForPeriod for " +dd.size()+ " candles: " + minLowForPeriod);
		
		double close = cc.peekFirst().close();
		lg.debug("calculate: latest close: " + close);

		double CL = close - minLowForPeriod;
		lg.debug("calculate: CL: " + CL);

		double HL = maxHighForPeriod - minLowForPeriod;
		lg.debug("calculate: HL: " + HL);
		
		//BigDecimal K = (CL.divide(HL, 6, RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
		double K = Math.round(((CL*100d)/HL)*100000) / 100000d;
		lg.debug("calculate: K:" + K);

		return K;
    }

   // @Override
   // public String toString() {
   //     return getClass().getSimpleName() + " period: " + period;
   // }
}
