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
package com.pancorp.tbroker.ta.test.trading.rules;

import com.pancorp.tbroker.ta.trading.rules.BooleanRule;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class BooleanRuleTest {

    private BooleanRule satisfiedRule;
    private BooleanRule unsatisfiedRule;
    
    @Before
    public void setUp() {
        satisfiedRule = new BooleanRule(true);
        unsatisfiedRule = new BooleanRule(false);
    }
    
    @Test
    public void isSatisfied() {
        assertTrue(satisfiedRule.isSatisfied(0));
        assertTrue(satisfiedRule.isSatisfied(1));
        assertTrue(satisfiedRule.isSatisfied(2));
        assertTrue(satisfiedRule.isSatisfied(10));
        
        assertFalse(unsatisfiedRule.isSatisfied(0));
        assertFalse(unsatisfiedRule.isSatisfied(1));
        assertFalse(unsatisfiedRule.isSatisfied(2));
        assertFalse(unsatisfiedRule.isSatisfied(10));
    }
}
        