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
package com.pancorp.tbroker.ta.test.indicators.statistics;

import com.pancorp.tbroker.ta.Decimal;
import com.pancorp.tbroker.ta.Rule;
import com.pancorp.tbroker.ta.Strategy;
import static com.pancorp.tbroker.ta.test.TATestsUtils.assertDecimalEquals;
import com.pancorp.tbroker.ta.TimeSeries;
import com.pancorp.tbroker.ta.indicators.simple.ClosePriceIndicator;
import com.pancorp.tbroker.ta.indicators.statistics.PeriodicalGrowthRateIndicator;
import com.pancorp.tbroker.ta.test.mocks.MockTimeSeries;
import com.pancorp.tbroker.ta.trading.rules.CrossedDownIndicatorRule;
import com.pancorp.tbroker.ta.trading.rules.CrossedUpIndicatorRule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


public class PeriodicalGrowthRateIndicatorTest {

    private TimeSeries mockdata;
    
    private ClosePriceIndicator closePrice;
    
    @Before
    public void setUp() {
        mockdata = new MockTimeSeries(
                29.49, 28.30, 27.74, 27.65, 27.60, 28.70, 28.60,
                28.19, 27.40, 27.20, 27.28, 27.00, 27.59, 26.20,
                25.75, 24.75, 23.33, 24.45, 24.25, 25.02, 23.60,
                24.20, 24.28, 25.70, 25.46, 25.10, 25.00, 25.00,
                25.85);
        closePrice = new ClosePriceIndicator(mockdata);
    }

    @Test
    public void testGetTotalReturn() { 
        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice, 5);
        double expResult = 0.9564;
        double result = gri.getTotalReturn();
        assertEquals(expResult, result, 0.01);
    }
    
    @Test
    public void testCalculation() { 
        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice,5);
        
        assertEquals(gri.getValue(0), Decimal.NaN);
        assertEquals(gri.getValue(4), Decimal.NaN);
        assertDecimalEquals(gri.getValue(5), -0.0268);
        assertDecimalEquals(gri.getValue(6), 0.0541);
        assertDecimalEquals(gri.getValue(10), -0.0495);
        assertDecimalEquals(gri.getValue(21), 0.2009);
        assertDecimalEquals(gri.getValue(24), 0.0220);
        assertEquals(gri.getValue(25), Decimal.NaN);
        assertEquals(gri.getValue(26), Decimal.NaN);
    }
    
    @Test
    public void testStrategies() { 
        
        PeriodicalGrowthRateIndicator gri = new PeriodicalGrowthRateIndicator(this.closePrice,5);

        // Rules
        Rule buyingRule = new CrossedUpIndicatorRule(gri, Decimal.ZERO); 
        Rule sellingRule = new CrossedDownIndicatorRule(gri, Decimal.ZERO);     
        
        Strategy strategy = new Strategy(buyingRule, sellingRule);
                
        // Check trades
        int result = mockdata.run(strategy).getTradeCount();             
        int expResult = 3;
        
        assertEquals(expResult, result);
    }
}
