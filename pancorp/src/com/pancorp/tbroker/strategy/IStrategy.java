package com.pancorp.tbroker.strategy;

import java.util.ArrayDeque;

import com.pancorp.tbroker.event.*;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.CandlePattern;

public interface IStrategy {
	public CandlePattern evaluate(ArrayDeque<Candle> candles, CandlePattern currentPattern) throws OpenPositionEvent, ClosePositionEvent, Exception;
}
