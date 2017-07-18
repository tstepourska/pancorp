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
import com.pancorp.tbroker.model.Candle;

/**
 * Stochastic oscillator D.
 * <p>
 * Receive {@link StochasticOscillatorKIndicator} and returns its {@link SMAIndicator SMAIndicator(3)}.
 */
public class StochasticOscillatorD
{
	private static Logger lg = LogManager.getLogger(StochasticOscillatorD.class);

    public StochasticOscillatorD( ){}

   public double calculate(ArrayDeque<Candle> cc, int pd) throws NotEnoughDataException, Exception {
    	//D period: 3
		double kSum = 0;
		double sk = 0;
		int count =0;
		
		Iterator<Candle> it = cc.iterator();
		while(it.hasNext()){
			count++;
			Candle can = it.next(); 
			sk = can.stochasticK();
			//if(sk==0)
			//	sk = can.
			kSum = kSum + sk;
			if(count>=pd)
				break;
		}
		//calculate D
		double D = Math.round((kSum / (double)pd) / 100000d) * 100000d;
		lg.debug("calculate: D for stack of size "+ count +": " + D);

    	return D;
    }

    //@Override
   // public String toString() {
       // return getClass().getSimpleName() + " " + indicator;
   // }
}
