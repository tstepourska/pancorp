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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.model.Candle;

/**
 * Stochastic oscillator D.
 * <p>
 * Receive {@link StochasticOscillatorKIndicator} and returns its {@link SMAIndicator SMAIndicator(3)}.
 */
public class __StochasticOscillatorDIndicator
{
	private static Logger lg = LogManager.getLogger(__StochasticOscillatorDIndicator.class);
	//private ArrayDeque<Candle> kStack;
    public __StochasticOscillatorDIndicator( ){
    	//kStack = new ArrayDeque<Candle>();
    }

   public double calculate(ArrayDeque<Candle> cc, int pd) throws NotEnoughDataException, Exception {
    	//D period: 3
		BigDecimal kSum = BigDecimal.ZERO;
		BigDecimal sk = BigDecimal.ZERO;
		int count =0;
		
		Iterator<Candle> it = cc.iterator();
		//move dPeriod candles from deque to stack while summing K values
		//while(kStack.size()<=pd){
		while(it.hasNext()){
			count++;
			Candle can = it.next(); // (Candle)cc.pop();
			sk = new BigDecimal( can.stochasticK());
			//if prev candle did not have K value, throw NotEnoughData and wait for a next candle
			//if(sk<=0){
			//	throw new NotEnoughDataException("No stochasticK value for previous candle, waiting for the next one.");
			//}
			kSum = kSum.add(sk);
			//push in stack
			//kStack.push(can);
			if(count>=pd)
				break;
		}
		//calculate D
		BigDecimal D = kSum.divide(new BigDecimal(pd), 6, RoundingMode.HALF_UP);
		lg.debug("calcStochastic: D for stack of size "+ count +": " + D);
		
		//put everything back to cache
		//while(!kStack.isEmpty()){
		//	cc.push(kStack.pop());
		//}
		
    	return D.doubleValue();
    }

    //@Override
   // public String toString() {
       // return getClass().getSimpleName() + " " + indicator;
   // }
}
