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
package com.pancorp.tbroker.ta.test.indicators.simple;

import com.pancorp.tbroker.ta.Decimal;
import static com.pancorp.tbroker.ta.test.TATestsUtils.assertDecimalEquals;
import com.pancorp.tbroker.ta.TimeSeries;
import com.pancorp.tbroker.ta.indicators.simple.PriceVariationIndicator;
import com.pancorp.tbroker.ta.test.mocks.MockTimeSeries;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PriceVariationIndicatorTest {

    private PriceVariationIndicator variationIndicator;

    private TimeSeries timeSeries;

    @Before
    public void setUp() {
        timeSeries = new MockTimeSeries();
        variationIndicator = new PriceVariationIndicator(timeSeries);
    }

    @Test
    public void indicatorShouldRetrieveTickVariation() {
        assertDecimalEquals(variationIndicator.getValue(0), 1);
        for (int i = 1; i < 10; i++) {
            Decimal previousTickClosePrice = timeSeries.getTick(i - 1).getClosePrice();
            Decimal currentTickClosePrice = timeSeries.getTick(i).getClosePrice();
            assertEquals(variationIndicator.getValue(i), currentTickClosePrice.dividedBy(previousTickClosePrice));
        }
    }
}
