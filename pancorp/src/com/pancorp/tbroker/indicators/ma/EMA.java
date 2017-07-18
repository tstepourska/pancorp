package com.pancorp.tbroker.indicators.ma;

import java.util.ArrayDeque;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.event.NotEnoughDataException;
import com.pancorp.tbroker.model.Candle;

/**
 * EMA = multiplier * {Close - EMA(previous day)} + EMA(previous day). 
 * 
 * EMA [today] = (Price [today] x K) + (EMA [yesterday] x (1 – K))

Where:

multiplier K = 2 ÷(N + 1)


EMA = Price(t) * k + EMA(y) * (1 - k)

t = today, y = yesterday, N = number of days in EMA, k = 2/(N+1) 


 * @author pankstep
 *
 */
public class EMA {
	private static Logger lg = LogManager.getLogger(EMA.class);
	double fastMultiplier;
	double slowMultiplier;
	int fastPeriod;
	int slowPeriod;
	
	public EMA(int fp, int sp){
		this.fastPeriod = fp;
		this.slowPeriod = sp;
		fastMultiplier = Math.round((2d /( fastPeriod + 1d) * 10000d)) / 10000d;  //= 2 / (10 + 1)  = 0.1818 (18.18%)
		lg.debug("fastMultiplier for period " + fastPeriod + ": " + fastMultiplier);
			
		slowMultiplier = Math.round((2d /( slowPeriod + 1d) * 10000d)) / 10000d;  //= 2 / (10 + 1)  = 0.1818 (18.18%)
		lg.debug("slowMultiplier for period " + slowPeriod + ": " + slowMultiplier);
	}
	
	public double calculateFastClose(ArrayDeque<Candle> cc) throws NotEnoughDataException {
		if(cc.size()<fastPeriod)
			throw new NotEnoughDataException();

		double ema = 0;
		double sum = 0;
		double sma = 0;
		double latestClose = cc.peekFirst().close();
		double prevEma = 0;
		ArrayDeque<Candle> tmp = new ArrayDeque<>();
		Iterator<Candle> it = cc.iterator();
		
		//head first, so latest push further to the tail
		while(it.hasNext()){
			tmp.push(it.next());
			if(tmp.size()>=fastPeriod)
				break;
		}
		
		//SMA:sum / period
		for(Candle c : tmp){
			sum = sum + c.close();
		}
		
		//(double)Math.round(value * 100000d) / 100000d
		sma = Math.round(sum/fastPeriod * 10000d) / 10000d; 
		lg.debug("calculateFastClose: sma: " + sma);

		//find prev ema value
		Candle t = cc.pop(); //take out the latest candle
		prevEma = cc.peekFirst().emaFast();
			
		if(prevEma<=0){ //first value calculated is current
			ema = sma;  //assign ema equal to just calculated sma
		}
		else {
			//calculate
			
			//EMA: multiplier * {Close - EMA(previous day)} + EMA(previous day). 
			ema = Math.round((fastMultiplier * (latestClose - prevEma) + prevEma)  * 10000d) / 10000d;		
			lg.debug("calculateFastClose: old formula: ema"+fastPeriod+": " + ema);
			
			//EMA = Price(t) * k + EMA(y) * (1 - k)
			//ema = Math.round((latestClose*fastMultiplier + prevEma * (1 - fastMultiplier))  * 10000d) / 10000d;	
			//lg.debug("calculateFastClose: new formula: ema"+fastPeriod+": " + ema);
		}
		lg.debug("calculateFastClose: ema: " + ema);
		//set values for current candle
		t.smaFast(sma);
		t.emaFast(ema);		
		cc.push(t);		// put back the candle
		
		return ema;
	}
	
	public double calculateMediumClose(ArrayDeque<Candle> cc, int medPeriod) throws NotEnoughDataException {
		if(cc.size()<medPeriod)
			throw new NotEnoughDataException();

		double ema = 0;
		double sum = 0;
		double sma = 0;
		double latestClose = cc.peekFirst().close();
		double prevEma = 0;
		ArrayDeque<Candle> tmp = new ArrayDeque<>();
		Iterator<Candle> it = cc.iterator();
		
		//head first, so latest push further to the tail
		while(it.hasNext()){
			tmp.push(it.next());
			if(tmp.size()>=medPeriod)
				break;
		}
		
		//SMA:sum / period
		for(Candle c : tmp){
			sum = sum + c.close();
		}
		
		//(double)Math.round(value * 100000d) / 100000d
		sma = Math.round(sum/medPeriod * 10000d) / 10000d; 
		lg.debug("calculateMediumClose: sma: " + sma);

		//find prev ema value
		Candle t = cc.pop(); //take out the latest candle
		prevEma = cc.peekFirst().emaMedium();
			
		if(prevEma<=0){ //first value calculated is current
			ema = sma;  //assign ema equal to just calculated sma
		}
		else {
			//calculate
			
			//EMA: multiplier * {Close - EMA(previous day)} + EMA(previous day). 
			double medMultiplier = Math.round((2d /( medPeriod + 1d) * 10000d)) / 10000d;  //= 2 / (10 + 1)  = 0.1818 (18.18%)
			lg.debug("medMultiplier for period " + medPeriod + ": " + medMultiplier);
			ema = Math.round((medMultiplier * (latestClose - prevEma) + prevEma)  * 10000d) / 10000d;		
			lg.debug("calculateMediumClose: old formula: ema"+medPeriod+": " + ema);
			
			//EMA = Price(t) * k + EMA(y) * (1 - k)
			//ema = Math.round((latestClose*medMultiplier + prevEma * (1 - medMultiplier))  * 10000d) / 10000d;	
			//lg.debug("calculateMedClose: new formula: ema"+medPeriod+": " + ema);
		}
		lg.debug("calculateMedClose: ema: " + ema);
		//set values for current candle
		t.smaMedium(sma);
		t.emaMedium(ema);		
		cc.push(t);		// put back the candle
		
		return ema;
	}
	
	public double calculateSlowClose(ArrayDeque<Candle> cc) throws NotEnoughDataException {
		
		if(cc.size()<slowPeriod){
			throw new NotEnoughDataException();
		}
		
		double ema = 0;
		double sum = 0;
		double sma = 0;
		double latestClose = cc.peekFirst().close();
		double prevEma = 0;
		
		//int itCnt = 0;
		
		ArrayDeque<Candle> tmp = new ArrayDeque<>();
		Iterator<Candle> it = cc.iterator();
		
		//head first, so latest push further to the tail
		while(it.hasNext()){
			tmp.push(it.next());
			if(tmp.size()>=slowPeriod)
				break;
		}
		
		//SMA: period sum / period
		for(Candle c : tmp){
			sum = sum + c.close();
		}
		
		//(double)Math.round(value * 100000d) / 100000d
		sma = Math.round(sum/slowPeriod * 10000d) / 10000d; 
		lg.debug("calculateSlowClose: sma: " + sma);
		
		//find prev ema value
		Candle t = cc.pop(); //take out the latest candle
		prevEma = cc.peekFirst().emaSlow();
		
		if(prevEma<=0){ //first value calculated is current
			ema = sma;  //assign ema equal to just calculated sma
		}
		else {
			//calculate
			
			//EMA: multiplier * {Close - EMA(previous day)} + EMA(previous day). 
			ema = Math.round((slowMultiplier * (latestClose - prevEma) + prevEma)  * 10000d) / 10000d;			
			lg.debug("calculateFastClose: old formula: ema"+slowPeriod + ": " + ema);
			
			//EMA = Price(t) * k + EMA(y) * (1 - k)
			//ema = Math.round((latestClose*slowMultiplier + prevEma * (1 - slowMultiplier))  * 10000d) / 10000d;	
			//lg.debug("calculateFastClose: new formula: ema"+slowPeriod+": " + ema);
		}
		lg.debug("calculateSlowClose: ema: " + ema);
		//set values for current candle
		t.smaSlow(sma);
		t.emaSlow(ema);		
		cc.push(t);		// put back the candle
		
		return ema;
	}

	
	public static void main(String[] args){
		
		//int pd = 10;
		ArrayDeque<Candle> cc = new ArrayDeque<>();
		Candle c =null;
		long time = System.currentTimeMillis();
		long v = 0;
		double w = 0;
		int cnt = 0;
		//double base = 0;
		
		double[] arr = {
		22.27,
		22.19,
		22.08,
		22.17,
		22.18,
		22.13,
		22.23,
		22.43,
		22.24,
		22.29,
		22.15,
		22.39,
		22.38,
		22.61,
		23.36,
		24.05,
		23.75,
		23.83,
		23.95,
		23.63,
		23.82,
		23.87,
		23.65,
		23.19,
		23.10,
		23.33,
		22.68,
		23.10,
		22.40,
		22.17
		};

		EMA em = new EMA(10,20);
		for(double base : arr){
			c = new Candle(time, base, base,base,base ,w ,v, cnt);
			cc.push(c);
			
			try {
				em.calculateFastClose(cc);
			}catch(NotEnoughDataException nede){
				lg.info("Not enough data: " + cc.size());
			}
		}
		
		for(Candle b : cc){
			lg.debug(b.close() + "   " + b.smaClose() + "   " + 
		//multiplier + "   " + 
					b.emaFast());
		}
	}
}
