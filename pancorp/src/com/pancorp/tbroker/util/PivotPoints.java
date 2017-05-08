package com.pancorp.tbroker.util;

public class PivotPoints {
	/*
	The first thing you�re going to learn is how to calculate pivot point levels.
	The pivot point and associated support and resistance levels are calculated by using the 
	last trading session�s open, high, low, and close. 
	Since forex is a 24-hour market, most forex traders use the New York closing time of 4:00pm EST as the previous day�s close.
	(Or 23:59 GMT for close, 00:00 GMT for open)

	The calculation for a pivot point is shown below:

	Pivot point (PP) = (High + Low + Close) / 3

	Support and resistance levels are then calculated off the pivot point like so:

	First level support and resistance:

	First resistance (R1) = (2 x PP) � Low

	First support (S1) = (2 x PP) � High

	Second level of support and resistance:

	Second resistance (R2) = PP + (High � Low)

	Second support (S2) = PP � (High � Low)

	Third level of support and resistance:

	Third resistance (R3) = High + 2(PP � Low)
	Third support (S3) = Low � 2(High � PP)

	Keep in mind that some forex charting software plot intermediate levels or mid-point levels. 
	These are basically mini levels between the main pivot point and support and resistance levels.

	*/
	
	public double getPivotPoint(double high, double low, double close){
		double pp = (high + low + close) / 3;
		
		return pp;
	}
	
	public double getResistance1(double pp, double low){
		double r1 =  (2 * pp) - low;
		
		return r1;
	}

	public double getSupport1(double pp, double high){
		double s1 =  (2 * pp) - high;
		
		return s1;
	}
	
	public double getResistance2(double pp, double high, double low){
		double r2 =  pp + (high - low);
		
		return r2;
	}
	
	public double getSupport2(double pp, double high, double low){
		double s2 = pp - (high - low);
		
		return s2;
	}
	
	public double getResistance3(double pp, double high, double low){
		double r3 = high + 2*(pp - low);
		
		return r3;
	}
	
	public double getSupport3(double pp, double high, double low){
		double s3 = low - 2*(high - pp);
		
		return s3;
	}

}
