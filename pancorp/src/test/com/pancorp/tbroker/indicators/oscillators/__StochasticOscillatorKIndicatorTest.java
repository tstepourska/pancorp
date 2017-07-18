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
package test.com.pancorp.tbroker.indicators.oscillators;

import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.indicators.oscillators.__StochasticOscillatorKIndicator;

import java.util.ArrayDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class __StochasticOscillatorKIndicatorTest {
	private static Logger lg = LogManager.getLogger(__StochasticOscillatorKIndicatorTest.class);
	private ArrayDeque<Candle> ticks;
	private ArrayDeque<Candle> data;

    @Before
    public void setUp() {
    	ticks = new ArrayDeque<Candle>();
    	//Candle double high, double low, double open, double close,
    	//Tick  double openPrice, double highPrice, double lowPrice, double closePrice,
    	
    	ticks.add(new Candle(0,1.11934 ,   1.1191 ,   1.1193 ,  1.11925,0,0,0));
    	ticks.add(new Candle(0,1.119265 , 1.11923 ,  1.11925 , 1.119265,0,0,0));
    	ticks.add(new Candle(0,1.119265 ,1.119195 , 1.119265 , 1.119245,0,0,0));
    	ticks.add(new Candle(0,1.119255 , 1.119205 , 1.119245 , 1.119235,0,0,0));
    	ticks.add(new Candle(0,1.119255 , 1.119235 , 1.119235 ,  1.11924,0,0,0));
    	ticks.add(new Candle(0,1.11925 , 1.11922 ,  1.11924 ,  1.11924,0,0,0));
    	ticks.add(new Candle(0,1.11926 ,1.119125 ,  1.11924 , 1.119235,0,0,0));
    	ticks.add(new Candle(0,1.11928 ,  1.11914 , 1.119235 , 1.11924,0,0,0));
    	ticks.add(new Candle(0,1.119295 ,  1.11915 ,  1.11924 , 1.119255 ,0,0,0));
    	ticks.add(new Candle(0,1.11929 , 1.119195 ,1.119255 , 1.119265,0,0,0));
    	ticks.add(new Candle(0,1.11927 , 1.119245 , 1.119265 , 1.119265 ,0,0,0));
    	ticks.add(new Candle(0,1.119095 ,  1.11892 , 1.119095 ,  1.11894,0,0,0));
    	ticks.add(new Candle(0,1.119065 ,  1.11891 ,  1.11894 ,  1.11891,0,0,0));
    	ticks.add(new Candle(0,1.11923 , 1.118875 ,  1.11891 ,  1.11902,0,0,0));
    	
        /*ticks.add(new Candle(0,44.98, 119.13, 119.50, 116.00,0,0,0));
        ticks.add(new Candle(0,45.05, 116.75, 119.94, 116.00,0,0,0));
        ticks.add(new Candle(0,45.11, 113.50, 118.44, 111.63,0,0,0));
        ticks.add(new Candle(0,45.19, 111.56, 114.19, 110.06,0,0,0));
        ticks.add(new Candle(0,45.12, 112.25, 112.81, 109.63,0,0,0));
        ticks.add(new Candle(0,45.15, 110.00, 113.44, 109.13,0,0,0));
        ticks.add(new Candle(0,45.13, 113.50, 115.81, 110.38,0,0,0));
        ticks.add(new Candle(0,45.12, 117.13, 117.50, 114.06,0,0,0));
        ticks.add(new Candle(0,45.15, 115.63, 118.44, 114.81,0,0,0));
        ticks.add(new Candle(0,45.24, 114.13, 116.88, 113.13,0,0,0));
        ticks.add(new Candle(0,45.43, 118.81, 119.00, 116.19,0,0,0));
        ticks.add(new Candle(0,45.43, 117.38, 119.75, 117.00,0,0,0));
        ticks.add(new Candle(0,45.58, 119.13, 119.13, 116.88,0,0,0));
        ticks.add(new Candle(0,45.58, 115.38, 119.44, 114.56,0,0,0));
*/
        data = new ArrayDeque<>();
    }

    @Test
    public void stochasticOscilatorK() {
    	int kPeriod = 5;
    	int cacheLen = 14;
    	__StochasticOscillatorKIndicator sof = new __StochasticOscillatorKIndicator();
    	
    	while(!ticks.isEmpty()){
    		data.push(ticks.removeLast());
    		
    		if(data.size()>cacheLen)
    			data.removeLast();
    		
    		try {
    			double k = sof.calculate(data, kPeriod); 
    			data.peekFirst().stochasticK(k);
    		}catch(Exception e){
    			lg.error("Error recalculating: " + e.getMessage());
    		}
    		
    		try {
    			Thread.sleep(1000);
    		}catch(InterruptedException e){}
    	}

        //assertDecimalEquals(sof.getValue(0), 313/3.5);
        //assertDecimalEquals(sof.getValue(12), 1000/10.81);
        //assertDecimalEquals(sof.getValue(13), 57.8168);
    }
}
