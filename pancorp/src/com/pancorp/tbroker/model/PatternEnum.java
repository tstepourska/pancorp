package com.pancorp.tbroker.model;

public enum PatternEnum {
	
	//simple: single candle patterns
	DOJI(1),				//no body (or within few pennies, either direction), short or avg shadows
	TOMBSTONE_DOJI(1),		//doji, long top shadow, no bottom
	DRAGONFLY_DOJI(1),		//doji, long lower shadow, no top
	LONG_LEGGED_DOJI(1),	//doji, long both shadows
	//BULLISH_MARABOZU(1),	//white, no shadows, long body
	//BEARISH_MARABOZU(1),	//black, no shadows, long bodu
	SPINNING_TOP(1),	//short body, short or avg shadows, or no shadow(s), can be black or white
	HIGH_WAVE(1),		//has both shadows 2-3 times longer than spinning top, can be black or white
	LONG_LOWER_SHADOW(1),	//black, avg body, the longer the shadow, the more bullish the candle
	LONG_UPPER_SHADOW(1),	//white,avg body, the longer the shadow, the more bearish the candle
	
	//complex: require checking previous values
	HANGMAN(1),
	HAMMER(1),
	TWEEZER_BOTTOM(2),
	TWEEZER_TOP(2),
	INVERTED_HAMMER(1),
	SHOOTING_STAR(1),
	BULLISH_PIERCING(2),
	BULLISH_ENGULFING(2),
	//BULLISH_COUNTER_ATTACK(2),
	BULLISH_HARAMI(2),
	//BULLISH_REST_AFTER_BATTLE(4),
	DARK_CLOUD_COVER(2),
	BEARISH_ENGULFING(2),
	//BEARISH_COUNTER_ATTACK(2),
	BEARISH_HARAMI(2),
	//BEARISH_REST_AFTER_BATTLE(4),
	ONE_BLACK_CROW(2),
	SONE_WHITE_SOLDIER(2),
	EVENING_DOJI_STAR(3),
	EVENING_STAR_REVERSAL(3),
	
	MORNING_STAR_REVERSAL(3),	
	//FALLING_THREE(5),
	
	//bullish continuation
	//RISING_THREE(5),
	
	//bullish reversal signals
	ABANDONED_BABY(3), //MORNING_DOJI_STAR(3),
	//THREE_LINE_STRIKE(4)
	;
	
	private final int size;
	
	private PatternEnum(int i){
		size = i;
	}
	
	public int getSize(PatternEnum p){
		return size;
	}
	
	/*
	 * 	switch(p){
		case DOJI:
			return "doji";
		case TOMBSTONE_DOJI:
			return "tombstone_doji";
		case DRAGONFLY_DOJI:
			return "dragonfly_doji";
		case LONG_LEGGED_DOJI:
			return "long_legged_doji";
		case BULLISH_MARABOZU:
			return "bullish_marabozu";
		case BEARISH_MARABOZU:
			return "bearish_marabozu";
		case SPINNING_TOP:
			return "spinning_top";
		case HIGH_WAVE:
			return "high_wave";
		case LONG_LOWER_SHADOW:
			return "long_lower_shadow";
		case LONG_UPPER_SHADOW:
			return "long_upper_shadow";
			default:
				return null;
		}
	 */
}
