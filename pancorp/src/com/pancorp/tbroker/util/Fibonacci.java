package com.pancorp.tbroker.util;

public class Fibonacci {

	/**
	 * 
	 * 	 uptrend:
	 * 
	 * 0% (high) 
		23.6% 
		38.2% 
		50% 
		61.8% 
		76.4% 
		100% (low) 
		138.2% 
		
		Calculate the Fibonacci retracement levels. 
		Mark the low with an "A" and the high with a "B" to keep track of which high and low you are using.
		When price is moving in an overall upward direction, calculate the retracement by 
		taking (B minus A) multiplied by F percent, and subtract this number from B. 
		
		downtrend:
		
		Label the high point A and the low point B. Use the formula (A minus B) multiplied by the Fibonacci percentage, and add this to B.
		
		138.2%  
		100% (high)  
		76.4%  
		61.8%  
		50%  
		38.2%  
		23.6%  
		0% (low) 
		
	 * @param trendDir
	 * @param high
	 * @param low
	 * @param percent
	 * @return
	 */
	public static double getRetracement(int trendDir, double high, double low, double percent) throws Exception {
		double result = 0;
		if(trendDir>0){
			result = high - (high - low)*percent;
		}
		else if(trendDir < 0 ){
			result = low + (high - low) * percent;
		}
		else {
			// 0
			throw new Exception("No trend direction identified!");
		}
		return result;
	}
	
	/**
	 * uptrend:
	 * 
	 * 	261.8% 
		200% 
		161.8% 
		138.2% 
		100% 
		61.8% 
		50% 
		38.2%  
		23.6% 

		downtrend:
		
		23.6%  
		38.2%  
		50%  
		61.8%  
		100%  
		138.2%  
		161.8%  	MOST POPULAR (and 1.272%)
		200%  
		261.8% 

The ratios that we use for fibonacci extensions are based on this number sequence.  The most important ratios are:

233/144 = 161.8%
 233/89 = 261.8%
 233/55 = 423.6%

Many traders include 127.2% (square root of 161.8%) as a fibonacci extension level.  I also like to look at the round numbers 200%, 300% and 400%.

	1.Take the difference in price between high and low. In this case: $25.92 - $23.11 = $2.81
	2.Now take that difference, $2.81 and multiply it by 1.272. $2.81 x 1.272 = $3.57
	3.Now add $3.57 to $23.11 (2) and you get...
	4.$26.68 - the exact high at 3. That is the Fibonacci extension.

	 * @param trendDir
	 * @param high
	 * @param low
	 * @param percent
	 * @return
	 */
	public static double getExtention(int trendDir, double high, double low, double percent) throws Exception {
		double result = 0;
		if(trendDir>0){
			//finding resistance
			result = low + (high - low)*percent;
		}
		else if(trendDir < 0){
			//finding support
			
		}
		else {
			// 0
			throw new Exception("No trend direction identified!");
		}
		
		return result;
	}
	
	// Fibonacci Channel
	//A variation of the Fibonacci retracement pattern in which the trendlines run diagonally rather than horizontally. 
	//These channels are used to estimate areas of support and resistance in the same way as the horizontal Fibonacci retracement levels.
}
