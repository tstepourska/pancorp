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
 * Maximum indicator.
 * <p>
 */
public class MaxIndicator {//extends CachedIndicator<Decimal> {

   // @Override
   public BigDecimal calculate(ArrayDeque<BigDecimal> dd) {
    	BigDecimal max = BigDecimal.ZERO;

		for(BigDecimal d : dd){
			if(d.compareTo(max)>0)
				max = d;
		}
		
		return max;
    }
   
   public double calculateHighestHighFromCache(ArrayDeque<IBar> cc){
	   double max = 0;
	   for(IBar c : cc){
			if(c.high()>max)
				max = c.high();
	  }
	   return max;
   }
   
   public double calculateDouble(ArrayDeque<Double> dd) {
   		double max = 0;

		for(double d : dd){
			if(d>max)
				max = d;
		}
		
		return max;
   }
}
