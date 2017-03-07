/**
 * Copyright 2016-2017 PanCorp Group
 */
package com.pancorp.tbroker.model;

//import com.ib.controller.Bar;
import com.pancorp.tbroker.util.Constants;

/**
 * 
 */

/**
 * @author pankstep
 *
 */
public class Candle extends Bar implements IBar {
	
	//public enum LENGTH_TYPE  {LONG, SHORT, AVERAGE};
	
	private double body_len 		= 0;
	private double upper_shadow_len = 0;
	private double lower_shadow_len = 0;
	//private double amp 				= 0;
	
	private int direction;
	
	private double emaShort = 0;
	private double emaLong = 0;
	//private double smaShort = 0;
	//private double smaLong = 0;
	//private double slope = 0;
	
	private double williamsR = 0;
	
	private double trueRange = 0;
	private double ATR = 0;  //average true range
	private double plusDMI;
	private double plusDMIEma;
	private double minusDMI;
	private double minusDMIEma;
	private double adxFactor;
	private double adxFactorEma;
	private double adx;
	
	private double avgBodyLen;
	private double avgUpperShadowLen;
	private double avgLowerShadowLen;
	
	/**
	 * Constructor
	 */
	public Candle( long time, double high, double low, double open, double close, double wap, long volume, int count) {
		super(time,high,low,open,close,wap,volume,count);	
		calcProperties();
	}

	public Candle(Bar b){
		super(b.time(),b.high(),b.low(),b.open(),b.close(),b.wap(),b.volume(),b.count());
		calcProperties();
	}
	
	public Candle(int rid, long time, double high, double low, double open, double close, double wap, long volume, int count){
		super(rid, time,high,low,open,close,wap,volume,count);

		calcProperties();
	}
/*
	public PatternEnum getSimplePattern(){
		return this.simplePattern;
	}
	
	public void setSimplePattern(PatternEnum p){
		this.simplePattern = p;
	}*/
	
	/**
	 * Returns candlestick direction
	 * @return int   --Negative - down (black or red candle), positive - up (while or green candle)
	 */
	public int getDirection(){
		return this.direction;
	}
	
	/**
	 * @return the body_len
	 */
	public double getBody_len() {
		return body_len;
	}

	/**
	 * @param body_len the body_len to set
	 */
	public void setBody_len(double body_len) {
		this.body_len = body_len;
	}

	/**
	 * @return the upper_shadow_len
	 */
	public double getUpper_shadow_len() {
		return upper_shadow_len;
	}

	/**
	 * @param upper_shadow_len the upper_shadow_len to set
	 */
	public void setUpper_shadow_len(double upper_shadow_len) {
		this.upper_shadow_len = upper_shadow_len;
	}

	/**
	 * @return the lower_shadow_len
	 */
	public double getLower_shadow_len() {
		return lower_shadow_len;
	}

	/**
	 * @param lower_shadow_len the lower_shadow_len to set
	 */
	public void setLower_shadow_len(double lower_shadow_len) {
		this.lower_shadow_len = lower_shadow_len;
	}

	/**
	 * @return the bodyType
	 */
/*	public LENGTH_TYPE getBodyType() {
		return bodyType;
	}*/

	/**
	 * @param lenT the lenT to set
	 */
/*	public void setBodyType(LENGTH_TYPE lenT) {
		this.bodyType = lenT;
	}*/

	/**
	 * @return the upperShadowType
	 */
/*	public LENGTH_TYPE getUpperShadowType() {
		return upperShadowType;
	}*/

	/**
	 * @param upperShadowType the upperShadowType to set
	 */
/*	public void setUpperShadowType(LENGTH_TYPE upperShadowType) {
		this.upperShadowType = upperShadowType;
	}*/

	/**
	 * @return the lowerShadowType
	 */
/*	public LENGTH_TYPE getLowerShadowType() {
		return lowerShadowType;
	}*/

	/**
	 * @param lowerShadowType the lowerShadowType to set
	 */
/*	public void setLowerShadowType(LENGTH_TYPE lowerShadowType) {
		this.lowerShadowType = lowerShadowType;
	}*/

	/**
	 * @return the amp
	 */
/*	public double getAmp() {
		return amp;
	}*/

	/**
	 * @param amp the amp to set
	 */
/*	public void setAmp(double amp) {
		this.amp = amp;
	}	*/
	
	/**
	 * @return the maShort
	 */
	public double emaShort() {
		return emaShort;
	}

	/**
	 * @param maShort the maShort to set
	 */
	public void emaShort(double maShort) {
		this.emaShort = maShort;
	}

	/**
	 * @return the maLong
	 */
	public double emaLong() {
		return emaLong;
	}

	/**
	 * @param maLong the maLong to set
	 */
	public void emaLong(double maLong) {
		this.emaLong = maLong;
	}

	/**
	 * @return the slope
	 */
/*	public double slope() {
		return slope;
	}*/

	/**
	 * @param slope the slope to set
	 */
/*	public void slope(double slope) {
		this.slope = slope;
	}*/

	/**
	 * @return the williamsR
	 */
	public double williamsR() {
		return williamsR;
	}

	/**
	 * @param williamsR the williamsR to set
	 */
	public void williamsR(double wr) {
		this.williamsR = wr;
	}

	/**
	 * @return the trueRange
	 */
	public double trueRange() {
		return trueRange;
	}

	/**
	 * @param trueRange the trueRange to set
	 */
	public void trueRange(double tr) {
		this.trueRange = tr;
	}

	/**
	 * @return the aTR
	 */
	public double ATR() {
		return ATR;
	}

	/**
	 * @param aTR the aTR to set
	 */
	public void ATR(double atr) {
		ATR = atr;
	}

	/**
	 * @return the plusDMI
	 */
	public double plusDMI() {
		return plusDMI;
	}

	/**
	 * @param plusDMI the plusDMI to set
	 */
	public void plusDMI(double plusDMI) {
		this.plusDMI = plusDMI;
	}

	/**
	 * @return the minusDMI
	 */
	public double minusDMI() {
		return minusDMI;
	}

	/**
	 * @param minusDMI the minusDMI to set
	 */
	public void minusDMI(double minusDMI) {
		this.minusDMI = minusDMI;
	}

	/**
	 * @return the adxFactor
	 */
	public double adxFactor() {
		return adxFactor;
	}

	/**
	 * @param adxFactor the adxFactor to set
	 */
	public void adxFactor(double adxFactor) {
		this.adxFactor = adxFactor;
	}

	/**
	 * @return the adx
	 */
	public double adx() {
		return adx;
	}

	/**
	 * @param adx the adx to set
	 */
	public void adx(double adx) {
		this.adx = adx;
	}

	/**
	 * @return the plusDMIEma
	 */
	public double plusDMIEma() {
		return plusDMIEma;
	}

	/**
	 * @param plusDMIEma the plusDMIEma to set
	 */
	public void plusDMIEma(double plusDMIEma) {
		this.plusDMIEma = plusDMIEma;
	}

	/**
	 * @return the minusDMIEma
	 */
	public double minusDMIEma() {
		return minusDMIEma;
	}

	/**
	 * @param minusDMIEma the minusDMIEma to set
	 */
	public void minusDMIEma(double minusDMIEma) {
		this.minusDMIEma = minusDMIEma;
	}

	/**
	 * @return the adxFactorEma
	 */
	public double adxFactorEma() {
		return adxFactorEma;
	}

	/**
	 * @param adxFactorEma the adxFactorEma to set
	 */
	public void adxFactorEma(double adxFactorEma) {
		this.adxFactorEma = adxFactorEma;
	}

	/**
	 * @return the smaShort
	 */
/*	public double smaShort() {
		return smaShort;
	}
*/
	/**
	 * @param smaShort the smaShort to set
	 */
/*	public void smaShort(double smaShort) {
		this.smaShort = smaShort;
	}*/

	/**
	 * @return the smaLong
	 */
/*	public double smaLong() {
		return smaLong;
	}*/

	/**
	 * @param smaLong the smaLong to set
	 */
/*	public void smaLong(double smaLong) {
		this.smaLong = smaLong;
	}*/

	/**
	 * @return the avgBodyLen
	 */
	public double getAvgBodyLen() {
		return avgBodyLen;
	}

	/**
	 * @param avgBodyLen the avgBodyLen to set
	 */
	public void setAvgBodyLen(double avgBodyLen) {
		this.avgBodyLen = avgBodyLen;
	}

	/**
	 * @return the avgUpperShadowLen
	 */
	public double getAvgUpperShadowLen() {
		return avgUpperShadowLen;
	}

	/**
	 * @param avgUpperShadowLen the avgUpperShadowLen to set
	 */
	public void setAvgUpperShadowLen(double avgUpperShadowLen) {
		this.avgUpperShadowLen = avgUpperShadowLen;
	}

	/**
	 * @return the avgLowerShadowLen
	 */
	public double getAvgLowerShadowLen() {
		return avgLowerShadowLen;
	}

	/**
	 * @param avgLowerShadowType the avgLowerShadowLen to set
	 */
	public void setAvgLowerShadowLen(double avgLowerShadowLen) {
		this.avgLowerShadowLen = avgLowerShadowLen;
	}

	/**
	 * Calculates candlestick direction, amplitude, 
	 * length of the body and each shadow
	 */
	private void calcProperties(){
		//amp 	 = high() - low();
		body_len = open() - close();
			
		if(body_len==0){
			direction = Constants.DIR_NONE;
		}
		else if(body_len>0)
			direction = Constants.DIR_BLACK;
		else
			direction = Constants.DIR_WHITE;	
		
		switch(direction){
		case Constants.DIR_WHITE:	
			upper_shadow_len = high() - close();
			lower_shadow_len = open() - low();
			break;
			default: //BLACK or DOJI
			upper_shadow_len = high() - open();
			lower_shadow_len = close() - low();
		}
		
		//get rid of minus sign if any
		body_len = Math.abs(body_len);
	}
}