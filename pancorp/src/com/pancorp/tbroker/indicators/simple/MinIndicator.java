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
package com.pancorp.tbroker.indicators.simple;

import java.math.BigDecimal;
import java.util.ArrayDeque;

import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;

/**
 * Minimum price indicator.
 * <p>
 */
public class MinIndicator { //extends CachedIndicator<Decimal> {

    public MinIndicator() {

    }

    public BigDecimal calculate(ArrayDeque<BigDecimal> dd) {
    	BigDecimal min = new BigDecimal(100000); //some arbitrary number to ensure there will be tick with the low price less than it
		//double t = 0;
		for(BigDecimal d : dd){
			if(d.compareTo(min)<0)
				min = d;
		}
		
		return min;
    }
    
    public double calculateLowestLowFromCache(ArrayDeque<IBar> cc){
 	   double min = 100000;		//some arbitrary number to ensure there will be tick with the low price less than it
 	   
 	  for(IBar c : cc){
			if(c.low()<min)
				min = c.low();
	  }
 	  
 	   return min;
    }
    
    public double calculateDouble(ArrayDeque<Double> dd) {
    	double min = 100000; //some arbitrary number to ensure there will be tick with the low price less than it
		//double t = 0;
		for(double d : dd){
			if(d<min)
				min = d;
		}
		
		return min;
    }
}
