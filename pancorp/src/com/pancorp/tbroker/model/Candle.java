/**
 * Copyright 2016-2017 PanCorp Group
 */
package com.pancorp.tbroker.model;

import java.io.Serializable;

//import com.ib.controller.Bar;
import com.pancorp.tbroker.util.Constants;

/**
 * 
 */

/**
 * @author pankstep
 *
 */
public class Candle extends Bar implements IBar, Serializable {
	private static final long serialVersionUID = -3989756641989895490L;
	
	private double stochasticK;
	private double stochasticD;
	
	private double plusDM;
	private double minusDM;
	
	//directional index
	private double plusDI;
	private double minusDI;
	
	//private double adxFactor;
	private double dx;
	private double minusDIEma;
	private double plusDIEma;
	private double trueRange;
	private double atr;
	private double adx;
	
	private double smaMinusDI;
	private double smaPlusDI;
	private double smaPlusDM;
	private double smaMinusDM;
	
	private double smaClose;
	
	private boolean firstInCache = false;
	
	//public enum LENGTH_TYPE  {LONG, SHORT, AVERAGE};
	
	private double body_len 		= 0;
	private double upper_shadow_len = 0;
	private double lower_shadow_len = 0;
	//private double amp 				= 0;
	
	private int direction;
	
	private double trueRangeEma;
	private double minusDMEma;
	private double plusDMEma;
	private double smaTrueRange;
	
	//private double closeEma;
	private double emaFast = 0;
	private double emaSlow = 0;
	private double emaMedium = 0;
	private double smaFast = 0;
	private double smaSlow = 0;
	private double smaMedium = 0;
	
	private double williamsR = 0;
/*
	private double ATR = 0;  //average true range
	private double plusDMI;
	private double plusDMIEma;
	private double minusDMI;
	private double minusDMIEma;
	private double adxFactor;
	private double adxFactorEma;
	*/
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
	public Candle(){
		super();
	}
	

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb
	//	.append("\nsymbol="+this.symbol())	
		.append("\nopen="+this.open())
		.append("\nclose="+this.close())
		.append("\nhigh="+this.high())
		.append("\nlow="+this.low())
		//.append("\ntime")
		//.append("\nvolume")		
		.append("\ntrueRange=" + this.trueRange)
		.append("\nplusDM="+this.plusDM)
		.append("\nminusDM="+this.minusDM)
		
		.append("\n atr="+this.atr)
		
		.append("\nsmaMinusDI="+this.smaMinusDI)
		.append("\nsmaPlusDI="+this.smaPlusDI)
		//.append("\nsmaClose="+this.smaClose)
		
		.append("\ndx="+this.dx)
		/*
		.append("\nminusDIEma="+this.minusDIEma)
		.append("\nplusDIEma="+this.plusDIEma)
		.append("\ntrueRange="+this.trueRange)
		*/
		
		.append("\nadx="+this.adx)
		
	
		;
		
		return sb.toString();
	}
	/*
	public void closeEma(double d){
		this.closeEma = d;
	}
	
	public double closeEma(){
		return this.closeEma;
	}
	*/
	public void plusDMEma(double d){
		this.plusDMEma = d;
	}
	
	public double plusDMEma(){
		return this.plusDMEma;
	}
	
	public void minusDMEma(double d){
		this.minusDMEma = d;
	}
	
	public double minusDMEma(){
		return this.minusDMEma;
	}
	
	public void trueRangeEma(double d){
		this.trueRangeEma = d;
	}
	
	public double trueRangeEma(){
		return this.trueRangeEma;
	}

	public void plusDM(double d) {
		this.plusDM = d;
	}

	public void minusDM(double d) {
		this.minusDM = d;
	}
	
	public double plusDM() {
		return this.plusDM;
	}

	public double minusDM() {
		return this.minusDM;
	}
	
	public double plusDI() {
		return this.plusDI;
	}

	public double minusDI() {
		return this.minusDI;
	}
	
	public void plusDI(double d) {
		this.plusDI = d;
	}

	public void minusDI(double d) {
		this.minusDI = d;
	}

	public void smaMinusDI(double d) {
		this.smaMinusDI = d;
	}
	
	public void smaPlusDI(double d) {
		this.smaPlusDI = d;
	}
	
	public double smaPlusDI() {
		return this.smaPlusDI;
	}
	
	public double smaMinusDI() {
		return this.smaMinusDI;
	}
	
	public void smaMinusDM(double d) {
		this.smaMinusDM = d;
	}
	
	public void smaPlusDM(double d) {
		this.smaPlusDM = d;
	}
	
	public double smaPlusDM() {
		return this.smaPlusDM;
	}
	
	public double smaMinusDM() {
		return this.smaMinusDM;
	}
	
	public double smaClose(){
		return this.smaClose;
	}
	public void smaClose(double d){
		this.smaClose = d;
	}
	
	public void smaTrueRange(double d){
		this.smaTrueRange = d;
	}
	
	public double smaTrueRange(){
		return this.smaTrueRange;
	}

	public void dx(double d){
		this.dx = d;
	}
	public double dx(){
		return this.dx;
	}
	public double minusDIEma() {
		return minusDIEma;
	}

	public double plusDIEma() {
		return plusDIEma;
	}

	public boolean isFirstInCache() {
		return firstInCache;
	}
	public void setFirstInCache(boolean firstInCache) {
		this.firstInCache = firstInCache;
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
	public double emaFast() {
		return emaFast;
	}

	/**
	 * @param maShort the maShort to set
	 */
	public void emaFast(double f) {
		this.emaFast = f;
	}
	
	/**
	 * @return the emaMedium
	 */
	public double emaMedium() {
		return emaMedium;
	}

	/**
	 * @param emaMedium the emaMedium to set
	 */
	public void emaMedium(double f) {
		this.emaMedium = f;
	}

	/**
	 * @return the emaSlow
	 */
	public double emaSlow() {
		return emaSlow;
	}

	/**
	 * @param emaSlow the emaSlow to set
	 */
	public void emaSlow(double s) {
		this.emaSlow = s;
	}


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
		return atr;
	}

	/**
	 * @param aTR the aTR to set
	 */
	public void ATR(double d) {
		atr = d;
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
	 * @return the smaFast
	 */
	public double smaFast() {
		return smaFast;
	}

	/**
	 * @param smaFast the smaFast to set
	 */
	public void smaFast(double sma) {
		this.smaFast = sma;
	}
	
	/**
	 * @return the smaMedium
	 */
	public double smaMedium() {
		return smaMedium;
	}

	/**
	 * @param smaMedium the smaMedium to set
	 */
	public void smaMedium(double sma) {
		this.smaMedium = sma;
	}


	/**
	 * @return the smaSlow
	 */
	public double smaSlow() {
		return smaSlow;
	}

	/**
	 * @param smaSlow the smaSlow to set
	 */
	public void smaSlow(double sma) {
		this.smaSlow = sma;
	}

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
	 * @return the stochasticK
	 */
	public double stochasticK() {
		return stochasticK;
	}

	/**
	 * @param stochasticK the stochasticK to set
	 */
	public void stochasticK(double stochasticK) {
		this.stochasticK = stochasticK;
	}

	/**
	 * @return the stochasticD
	 */
	public double stochasticD() {
		return stochasticD;
	}

	/**
	 * @param stochasticD the stochasticD to set
	 */
	public void stochasticD(double stochasticD) {
		this.stochasticD = stochasticD;
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