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
package com.pancorp.tbroker.ta.test.indicators.trackers;

import static com.pancorp.tbroker.ta.test.TATestsUtils.assertDecimalEquals;
import com.pancorp.tbroker.ta.TimeSeries;
import com.pancorp.tbroker.ta.indicators.simple.ClosePriceIndicator;
import com.pancorp.tbroker.ta.indicators.trackers.SMAIndicator;
import com.pancorp.tbroker.ta.indicators.trackers.ZLEMAIndicator;
import com.pancorp.tbroker.ta.test.mocks.MockTimeSeries;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ZLEMAIndicatorTest {

    private TimeSeries data;

    @Before
    public void setUp() {
        data = new MockTimeSeries(
                10, 15, 20,
                18, 17, 18,
                15, 12, 10,
                8, 5, 2);
    }

    @Test
    public void ZLEMAUsingTimeFrame10UsingClosePrice() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);

        assertDecimalEquals(zlema.getValue(9), 11.9091);
        assertDecimalEquals(zlema.getValue(10), 8.8347);
        assertDecimalEquals(zlema.getValue(11), 5.7739);
    }

    @Test
    public void ZLEMAFirstValueShouldBeEqualsToFirstDataValue() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);
        assertDecimalEquals(zlema.getValue(0), "10");
    }

    @Test
    public void valuesLessThanTimeFrameMustBeEqualsToSMAValues() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);
        SMAIndicator sma = new SMAIndicator(new ClosePriceIndicator(data), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(sma.getValue(i), zlema.getValue(i));
        }
    }
    
    @Test
    public void smallTimeFrame() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 1);
        assertDecimalEquals(zlema.getValue(0), "10");
    }
}
