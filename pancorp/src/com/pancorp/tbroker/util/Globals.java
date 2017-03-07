package com.pancorp.tbroker.util;

public enum Globals {

	GLOBALS;
	
	///////// IB CONNECTION PROPERTIES ////////////////////
	//public static int port				 = 7496;	//real trading
	public static int port				 = 7497;	//paper trading
	//public static int port				 = 4002;	//IB Gateway
	public static String host            = "127.0.0.1";
	public static int masterClientId	 = 19640509;
	public static int paperClientId		 = 19980331;
	
	///////// END OF IB CONNECTION PROPERTIES /////////////
	
	///////
	public static final String BASEDIR 				= "/Users/pankstep/run/TBroker/";
	public static final String DATADIR 				= "data/";
	
	//for IB
	public static final String SCANNER_INPUT_FILE 	= "scanner_params.xml";
	
	//1st level stock selection parameters (reduce list from thousands to under 20)
	public static final double MIN_STK_PRICE		= 10.0;	//USD
	public static final double MAX_STK_PRICE		= 100.0;	//USD
	public static final int MIN_STK_VOLUME			= 2000000;	//USD
	public static double PRICE_DAY_RANGE_PERC_MIN		= 0.049;		// high volatility
	
	public static final int NA = -999;
	
	//for calculation averages: difference between MIN and MAX must be > 1
	public static int MIN_CALC_CANDLES = 1;
	public static int MAX_CALC_CANDLES = 50;
	
	public static int MIN_TREND_CANDLES = 3;
	public static int MAX_TREND_CANDLES = 5;
	/** For calculation of body and shadows length to determine 'small' (< 1/3 len) and 'large' (> 3 len) */
	public static int CANDLE_TYPE_FACTOR	 = 3;
	public static int MAX_PATTERN_CONFIRMATION_COUNT		 = 1;
	
	public static int ATR_PERIOD		= 14;
	
	public static double MIN_TRENDING_SLOPE		= 2.5;
	
	//////////////////// WILLIAMS R	
	//between 0 and -20  - overbought, might be a signal to sell if
	public static int WILLIAMS_R_OVERBOUGHT_THRESH				= -15;
	//During a price downtrend, enter short/sell when the indicator was overbought and then drops below the -50 level. 
	public static int WILLIAMS_R_SELL_TRIG						= -50;
	
	//between -80 and -100  - oversold, might be a signal to buy if
	public static int WILLIAMS_R_OVERSOLD_THRESH					= -85;
	//During an uptrend, buy when the price was oversold then rallies above the -50 level.
	public static int WILLIAMS_R_BUY_TRIG							= -50;
	///////////// END OF WILLIAMS R
	
	///////// ADX - non-direction trend strength indicator for stocks and futures
	///
	/// when line is rising, trend strength is increasing (both uptrend or downtrend)
	//  when line is falling, trend STRENGTH is decreasing and price enters period of retracement or consolidation
	// 0-25		Absent or Weak Trend		-if for more than 30 bars - range conditions
	// 25-50	Strong Trend
	// 50-75	Very Strong trend
	// 75-100	Extremely Strong Trend
	public static int ADX_WEAK_TREND_THRESHOLD					= 25;
	public static int ADX_VERY_STRONG_TREND_THRESHOLD				= 50;

	/**
	 * Deviation percent of the opening price for estimating body length 
	 * when defining candlestick pattern.
	 * 
	 * For the definition of Doji:
	 * 
	 *  Point is the smallest possible price change on the left side of the decimal point. ($1 ?)
	 *  
	 *  1/8 point for $20 stock:    0.00630517*100%
	 *  1 1/4 point for $200 stock: 0.00625*100%
	 */
	public static double PATTERN_BODY_DEVIATION_PERC				= 0.006;	//0.6%
	
	/**
	* Percent from the open price that allowed for Doji pattern (considered the same)
	*/
	//public static final double _DOJI_LEG_RANGE_PERC = 0.01;
	
	/**
	 * 
	 */
	private static double accountTotal		= 10000.00;
	
	public static double getAccountTotal(){
		return accountTotal;
	}
	
	public static synchronized void setAccountTotal(double d){
		accountTotal = d;
	}
	
	///////////////////////////////////////////////////////////////////////
	//		STRATEGIES GLOBAL VARIABLES
	///////////////////////////////////////////////////////////////////////

	
	/**
	 * Success ratio: probability of the trade being successful. 
	 * Number of profitable trades divided by a number of total trades 
	 * multiplied by 100
	 * 
	 * Has to be established, monitored and updated, depending on 
	 * the trade setup (TODO - to define and test it) and numerous 
	 * external variables
	 * For example, the setup may have a higher probability of success 
	 * if it appears just above the round number (for a long position) 
	 * than it does if it appears just beneath it
	 */
	public static double PROBABILITY_OF_SUCCESS = 50;  //TODO - to determine and test!!
	
	/**
	 * To calculate, need to know Sharpe and Success ratios
	 * 
	 * Example:
	 * 		Success ratio: 2:1 (66%)
	 * 		Sharpe ratio: 1.5:1 so if we risk $40, we stand to make $60 on the winning trades
	 * 
	 * The success ratio tells us that we win 2 out of every 3 trades, and the Sharpe ratio 
	 * tells us, that, of the 2 winners, we make 2 * +$60+ = +$120
	 * Our one losing trade of the three costs us -$40
	 * On all three trades we risked $40 and ended up with a net gain of +$80
	 * 
	 * Therefore, if we divide the net gain by the amount we risked, we arrive at 
	 * risk-reward ratio of 2:1
	 * 
	 * Caution: assumed that we only trade the setups defined in our plan and that 
	 * they have been thoroughly back and forward tested to determine their probability 
	 * of success (Success Ratio)
	 */
	public static double RISK_REWARD_RATIO      = 50;
	
	/**
	 * Average number of $$$ made on profitable trades relative to the average 
	 * number of $$$ lost on unprofitable trades: divide avg $$$ gained in profitable 
	 * trades on combined figure of the average number of $$$ gained and lost, 
	 * multiplied by 100
	 */
	public static double SHARPE_RATIO      		= 50;	//TODO to determine and test
	////////////////////////////////////////////////////////////////////////////////////
	//		END OF STRATEGIES GLOBAL VARIABLES
	////////////////////////////////////////////////////////////////////////////////////


}
