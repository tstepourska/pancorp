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

import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.IBar;
//import com.pancorp.tbroker.ta.Tick;
//import com.pancorp.tbroker.ta.TimeSeries;
import com.pancorp.tbroker.indicators.oscillators.__StochasticOscillatorDIndicator;
import com.pancorp.tbroker.indicators.oscillators.__StochasticOscillatorKIndicator;
import com.pancorp.tbroker.indicators.ma.SMA;

import java.util.ArrayDeque;
//import com.pancorp.tbroker.ta.test.mocks.MockTick;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class __StochasticOscillatorDIndicatorTest {
	private static Logger lg = LogManager.getLogger(__StochasticOscillatorDIndicatorTest.class);
    //private TimeSeries data;
	private ArrayDeque<Candle> ticks;
	private ArrayDeque<Candle> data;
	
	private int kPeriod = 5;
	private int cacheLen = 14;
	private __StochasticOscillatorKIndicator sofK;
	private __StochasticOscillatorDIndicator  sofD;
	
    @Before
    public void setUp() {

    	//Candle double high, double low, double open, double close,
    	ticks = new ArrayDeque<>();
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

        data = new ArrayDeque<>();
        sofK = new __StochasticOscillatorKIndicator();
        sofD = new __StochasticOscillatorDIndicator();
    }

    @Test
    public void stochasticOscilatorD() {
    	
    	int dPeriod = 3;
    	int cacheLen = 14;
    	while(!ticks.isEmpty()){
    		data.push(ticks.removeLast());
    		
    		if(data.size()>cacheLen)
    			data.removeLast();
    		try {
    			recalculate(data, dPeriod);
    		}
    		catch(Exception e){
    			lg.error("Error calculating stochasticD: " + e.getMessage());
    		}
    		
    		try {
    			Thread.sleep(1000);
    		}catch(InterruptedException e){}
    	}

    }
    
    private void recalculate(ArrayDeque<Candle> cc, int pd) throws Exception { 
         
       calculateK(cc);
       double d = sofD.calculate(cc, 3);  //orig 14
       cc.peekFirst().stochasticD(d);
        
      //  SMAIndicator sma = new SMAIndicator(sof, 3);
       //StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sma);

       // as
        //assertEquals(sma.getValue(0), sos.getValue(0));
        //assertEquals(sma.getValue(1), sos.getValue(1));
        //assertEquals(sma.getValue(2), sos.getValue(2));
    }
    
    public void calculateK(ArrayDeque<Candle> cc) {
    		try {
    	double k =	sofK.calculate(cc, kPeriod); 
    	cc.peekFirst().stochasticK(k);
    		}catch(Exception e){
    			lg.error("Error recalculating: " + e.getMessage());
    		}
    	

        //assertDecimalEquals(sof.getValue(0), 313/3.5);
        //assertDecimalEquals(sof.getValue(12), 1000/10.81);
        //assertDecimalEquals(sof.getValue(13), 57.8168);
    }

 /*   @Test
    public void stochasticOscilatorDParam14UsingSMA3() {

        StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(data, 14);
        StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sof);
        SMAIndicator sma = new SMAIndicator(sof, 3);

        assertEquals(sma.getValue(0), sos.getValue(0));
        assertEquals(sma.getValue(1), sos.getValue(1));
        assertEquals(sma.getValue(2), sos.getValue(2));
        assertEquals(sma.getValue(13), sos.getValue(13));
    }*/
}
