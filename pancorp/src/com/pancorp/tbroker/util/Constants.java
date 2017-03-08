package com.pancorp.tbroker.util;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

public enum Constants {

	CONSTANTS;
	
	
	///////////////////////////////////////////////////////////////
	//		CONSTANTS
	///////////////////////////////////////////////////////////////
	/** The only supported bar size - 5 seconds */
	public static final int BAR_SIZE	= 5;
	
	public static final String BAR_WHAT_TO_SHOW_TRADES		= "TRADES";
	public static final String BAR_WHAT_TO_SHOW_BID			= "BID";
	public static final String BAR_WHAT_TO_SHOW_ASK			= "ASK";
	public static final String BAR_WHAT_TO_SHOW_MIDPOINT	= "MIDPOINT";
	
	public static final String ACTION_BUY					= "BUY";
	public static final String ACTION_SELL					= "SELL";
	
	/** Values for candlestick direction */
	public static final int DIR_BLACK 	= -1;
	public static final int DIR_WHITE 	= 1;
	public static final int DIR_NONE	= 0;
	
	//quantity of 5 sec bars in a time frame unit
    public static final long MIN = 12;  // 1*12;
    public static final long HOUR =720; // 1*12*60;
    public static final long DAY = 17280;  //1*12*60*24;
    
    public static final long SLEEP_DF_INTERRUPTED		= 2000;
    public static final long SLEEP_WAIT_FOR_BAR		= 5010;
    
    //timeframe units
    public static final String TFU_MIN = "MIN"; 
    public static final String TFU_HOUR = "HOUR"; 
    public static final String TFU_DAY = "DAY";
	
	public static final int STATUS_ERROR				= 9;
	
	//included in the ContractFollowingThread name
	public static final String FORMAT_DATE_CONTRACT_FOLLOWING		= "yyyyMMdd";

	///// modes for ContractFollowingThread
	// just started, accepting new Ticks, recalculating and making decisions for opening a position
	public static final int MODE_OPENING			= 1;
	// have an open position, monitoring it until close, or force to close
	public static final int MODE_CLOSING			= 0;
	
	public static final int TRIGGER_ACTION_NONE						= -1;
	public static final int TRIGGER_ACTION_PATTERN_TO_CONFIRM		= 0;
	public static final int TRIGGER_ACTION_OPEN_POSITION			= 1;
	
	public static final int EMA_TYPE_CLOSE			= 0;
	public static final int EMA_TYPE_PLUS_DMI		= 1;
	public static final int EMA_TYPE_MINUS_DMI		= 2;
	public static final int EMA_TYPE_ADX_FACTOR		= 3;
	
	public static final int DEFAULT_QUANTITY		= 100;
	
	/**
	 * Percent of total account amount, that I can afford to lose, 
	 * shared between all trades; ex $10000 * 0.05 = $500
	 * Total trailing stops amount for all concurrent positions 
	 * must not exceed this number: new position can't be opened
	 */
	//public static final double TOTAL_STOP_LOSS_PERCENT = 0.05;
	
	/**
	 * Percent of original amount spent on a single position that will trigger 
	 * stop loss order; deduct percent that can be lost (usually 0.03-0.07) from 1 (100%)
	 * TODO   is is the same for long and short positions?
	 */
	public static final double STOP_LOSS_PERCENT_FACTOR   = 0.975; //allowed to lose up to 2.5%
	
	/**
	 * Percent of original price/amount to trigger take profit order
	 */
	public static final double TAKE_PROFIT_PERCENT_FACTOR   = 1.0375; //3.75% gain triggers takeProfit order
	
	/**
	 * Percent of  total account amount that can be invested 
	 * into one economic sector, shared between all positions,
	 * ex $10000 * 0.03 = $300
	 * 
	 * TODO to confirm if this is applicable to day trading
	 */
	//public static final double TOTAL_SECTOR_EXPOSURE_PERCENT = 0.03;
	
	/**
	 * Percent of the total account amount that allowed to be spent  
	 * on a single trade.
	 * Usually between 1 (for most accounts) and 3 (very small accounts) %
	 */
	//public static final double LOSS_PER_TRADE				= 0.03;
	
	/**
	 * Percent of the total account amount that allowed to be lost 
	 * per day
	 */
	//public static final double LOSS_PER_DAY					= 0.03;
	
	/**
	 * Largest % drawdown on each strategy employed, multiplied 
	 * by this factor (may be within the range between 1.5 to 2.0)
	 * If reached, STOP trading this strategy immediately (close and disable)
	 */
	//public static final double STRATEGY_DRAWDOWN_FACTOR = 1.6;
	
	/**
	 * Optional for losing trades. Allows exit before hitting trailing stop.
	 * If the price does not move this 
	 * number of points in my favour, position must be closed
	 */
	//public static final double FAVOUR_POINTS	 = 0.3;  //TODO to determine

	public static final String endOfDay = "16:45:00";
	//public static  XMLGregorianCalendar endOfDayC =Datatype. GregorianCalendar.getInstance(TimeZone.getDefault())
	
	////////////////////////////////////////////////////////////////////////
	// 			END OF CONSTANTS
	////////////////////////////////////////////////////////////////////////

}