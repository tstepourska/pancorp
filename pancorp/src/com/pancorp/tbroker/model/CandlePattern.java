package com.pancorp.tbroker.model;

//import java.util.ArrayDeque;

import com.pancorp.tbroker.util.Constants;

public class CandlePattern {
	//private ArrayDeque<Candle> cache = null;
	private boolean confirmed = false;
	private boolean completed = false;
	private PatternEnum name;
	private int confirmationDirection = Constants.DIR_NONE;
	//what action this pattern implies, once confirmed (BUY, SELL etc)
	private String action;
	private int confirmationCount = 0;
	
	public CandlePattern() {
		//currentPattern = new ArrayDeque<>();
		action = null;
	}
	
	/*public void addCandleToPattern(Candle c){
		if(cache==null)
			cache = new ArrayDeque<>();
		
		//add first
		cache.push(c);
	}*/
	
	public boolean confirm(Candle c){
		//TODO
		
		return confirmed;
	}
	
	public void setCompleted(boolean comp, String action){
		this.completed = comp;
		this.action = action;
	}
	
	public boolean isCompleted(){
		return this.completed;
	}

	public void reset(){
		//cache = null;
		confirmed = false;
		action = null;
		confirmationDirection = Constants.DIR_NONE;
		confirmationCount = 0;
	}

	/**
	 * @return the name
	 */
	public PatternEnum getName() {
		return name;
	}

	/**
	 * @param patternName the patternName to set
	 */
	public void setPatternName(PatternEnum patternName) {
		this.name = patternName;
	}

	/**
	 * @return the confirmationDirection
	 */
	public int getConfirmationDirection() {
		return confirmationDirection;
	}

	/**
	 * @param confirmationDirection the confirmationDirection to set
	 */
	public void setConfirmationDirection(int confirmationDirection) {
		this.confirmationDirection = confirmationDirection;
	}

	/**
	 * @return the patternAction
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param patternAction the patternAction to set
	 */
	public void setAction(String patternAction) {
		this.action = patternAction;
	}

	/**
	 * @return the confirmationCount
	 */
	public int getConfirmationCount() {
		return confirmationCount;
	}

	/**
	 * @param confirmationCount the confirmationCount to set
	 */
	public void setConfirmationCount(int confirmationCount) {
		this.confirmationCount = confirmationCount;
	}
	
	/**
	 * Increments confirmation count
	 */
	public void incrementConfirmationCount() {
		this.confirmationCount++;
	}
}
