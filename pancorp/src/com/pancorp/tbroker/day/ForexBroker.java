package com.pancorp.tbroker.day;

import java.util.ArrayDeque;
//import java.util.List;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.strategy.*;
import com.pancorp.tbroker.data.DataFactory;
import com.pancorp.tbroker.event.*;
import com.pancorp.tbroker.indicators.simple.MaxIndicator;
import com.pancorp.tbroker.indicators.simple.MinIndicator;
import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.order.PlaceAttachedOrderPairT;
import com.pancorp.tbroker.order.PlaceOrderBracketT;
import com.pancorp.tbroker.order.UpdateOrder;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
//import com.ib.client.Order;
import com.pancorp.tbroker.model.*;
//import com.pancorp.tbroker.util.Calculator;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

public class ForexBroker extends Thread 
{
	private static Logger lg = LogManager.getLogger(ForexBroker.class); 
	
	private Contract contract;
	private int reqId;
	private DataFactory datafactory;
	private boolean working = true;
	//private volatile ArrayDeque<IBar> ticks;
	volatile ArrayDeque<Candle> candles;
	//ArrayDeque<IBar> highs;
	//ArrayDeque<IBar> lows;
	volatile ArrayDeque<IBar> currentCandle = null;
	String tfUnit;
	long tfFactor = 1; //5 sec
	MaxIndicator max;
	MinIndicator min;
	int timeframeSize = 1;
	String timeframeUnit = Constants.TFU_MIN;
	
	//StrategyFX1 strategy;
	//StrategyFX1A strategy;
	IStrategy strategy;
	double latestClose = 0;
	private BrokerManagerEWrapperImpl wrapper;
	private BrokerManager manager;
	
	UpdateOrder uo = null;
	
	/**
	 * Creates an instance of a stock monitoring thread
	 * 
	 * @param c
	 * @param df
	 * @param tfu
	 */
	//public Broker(){
	public ForexBroker(Contract c, int rid, DataFactory df, String tfu, BrokerManagerEWrapperImpl wr, BrokerManager mgr) throws Exception {
		contract = c;
		reqId = rid;
		datafactory = df;
		candles = new ArrayDeque<>();
		currentCandle = new ArrayDeque<>();
		
		this.tfUnit = tfu;
		
		switch(tfUnit){
		case Constants.TFU_MIN:
			tfFactor = Constants.MINUTE;
			lg.info("tfFactor: " + tfFactor);
			break;
		case Constants.TFU_HOUR:
			tfFactor = Constants.HOUR;
			break;
		case Constants.TFU_DAY:
			tfFactor = Constants.DAY;
			break;
			default:
		}

		this.wrapper = wr;
		this.manager = mgr;

		//original strategy
		//this.strategy = new StrategyFX1(); 
		//this.strategy = new StrategyFX1A(); 
		//with monitored exit
		this.strategy = new StrategyFX1B(); 
		
		//subscribe to data
		this.datafactory.subscribe(this.reqId);
		
		//testing only TODo to comment
		//this.datafactory.fillUpTestCache(this.reqId);
		
		max = new MaxIndicator();
		min = new MinIndicator();
		
		lg.info("Broker " + this.reqId + " created");
	}
	
	public void run(){
		Bar tick = null;
		
		//instead of goto, acts same way as Exception
		SkipEvent skip = new SkipEvent();
		
		while(working){
			//reset
			tick = null;
			
			try {
				if(datafactory.barCache.get(this.reqId).isEmpty()){
					throw skip;
				}
				
				tick = datafactory.barCache.get(this.reqId).removeLast();
				
				if(tick==null)
					throw skip;

				add(tick);
			}
			catch(SkipEvent se){
				try{
					//if(strategy.isCalibrated() && manager.getOperationsMode()==Constants.OPS_MODE_LIVE_TESTCACHE){
						Thread.sleep(5000);
					//}
					//else
					//	Thread.sleep(50);
				}catch(InterruptedException ie){}
			}
			catch(Exception e){Utils.logError(lg, e);}
			finally{
				try{
					if(strategy.isCalibrated() && manager.getOperationsMode()==Constants.OPS_MODE_LIVE_TESTCACHE){
						Thread.sleep(2000);
					}
					//else
					//	Thread.sleep(50);
				}catch(InterruptedException ie){}
			}
		}
	}
	
	/**
	 * 
	 */
	private void add(IBar t) throws Exception {
		//translate latest tick into a candle
		String fp = "add: ";

		//add to current candle tick queue			
		currentCandle.push(t);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "added tick to current candle, size: " + currentCandle.size());
		//check  the current candle queue size
		if(currentCandle.size()<tfFactor)	//current candle cache is not full
			return;
		
		//current candle cache is full	
		if(lg.isTraceEnabled())
			lg.trace(fp + "current candle cache is full, creating new candle..");

		//creating new Candle from the  current candle queue
		double open = currentCandle.peekLast().open();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "open: " + open);
		
		double high = max.calculateHighestHighFromCache(currentCandle); // calc.maxHigh(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "high: " + high);
		
		double low = min.calculateLowestLowFromCache(currentCandle); // calc.minLow(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "low: " + low);
		
		long volume = sumVolume(currentCandle);
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "volume: " + volume);
		long time = currentCandle.peekLast().time();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "time: " + time);
			
		latestClose = t.close();
		Candle c = new Candle(time,open,latestClose,high,low,t.wap(),volume,t.count());

		//adding it to a day candle queue 
		candles.push(c);
		if(lg.isDebugEnabled())
			lg.debug(fp + "pushed new candle to candles cache, size: " + this.candles.size());
		
		//clear current candle cache to start a new candle
		this.currentCandle.clear();
		//if(lg.isTraceEnabled())
		//	lg.trace(fp + "cleared current candle cache to start new candle");
	
		//remove extra candle
		if(candles.size()>=this.strategy.getMaxCache()){
			candles.removeLast();
		}

		try {
			//if(manager.isOrderPlaced()){
				//TODO select order status to see if it is filled
			//	if(manager.isTradeOpened());
			//		strategy.evaluateExit(candles, manager.getOpenOrderList(), contract, this.latestClose);
				
				//if trade is not opened, but order placed, 
				// do nothing, wait for order to be filled
			//}
			//else
			//	strategy.evaluateEntry(candles);	
			strategy.evaluate(candles, manager.isOrderPlaced(), false, //manager.isTradeOpened(), 
					manager.getOpenOrderList(), contract, this.latestClose);
			//ArrayDeque<Candle> candles, boolean orderPlaced, List<Order> orders, Contract contract, double latestClose5sec
		}
		catch(OpenPositionEvent ope){
			//IB allows up to 15 active orders per contract per side per account.
			//now we allow 1 at a time
			lg.trace("Caught OpenPositionEvent");
			int quantity = 0;
			/*if(this.contract.symbol().equals("AUD"))
				quantity = 25000;
			else*/
				quantity = Constants.DEFAULT_FOREX_QUANTITY;
			
				// PATTERN COMPLETED AND CONFIRMED, OPEN POSITION!!!
				//tell manager to lock the right to place order; if true, proceed
				if(manager.setOrderPlaced(true)){	
					lg.trace("manager locked the right to place order");
					//switch mode of operation, on callback, if not filled, switch back to OPENING
					//this.mode = Constants.MODE_CLOSING;
					double lmtPrice = Utils.truncateDouble(ope.getLimitPrice(), 10000d);
					lg.debug("limit price: " + lmtPrice);
					//TODO check quantity against amount in the account
					try {
					//placeOrder(currentPattern.getAction(), lmtPrice, Constants.DEFAULT_QUANTITY);	
				//	PlaceOrderBracketT placeOrderThread = new PlaceOrderBracketT(//wrapper.getCurrentOrderId(), 
						PlaceAttachedOrderPairT placeOrderThread = new PlaceAttachedOrderPairT(
							this.contract, 					// contract
							lmtPrice, 						// limit price
							//calc.calcForexPositionSizeAcctInQuote(acctEquity, contract, riskPercent, stopLossPips, xRate)
							quantity, 	// position size (quantity currency units), default 20000
							ope.getAction(), //currentPattern.getAction(),
							wrapper,
							manager,
							this.datafactory
							);
					placeOrderThread.start();
					lg.trace("Place order thread started");
					
					//reset current pattern
					//currentPattern = null;
					//lg.trace("Current pattern reset");
					//wait for order to be filled - see callback to wrapper
					}
					catch(Exception e){
						Utils.logError(lg, e);
					}
				}
		}
		catch(ClosePositionEvent cpe){
			lg.trace("Caught ClosePositionEvent");
			//List<Order> orders = cpe.getOrderList();
			double stopPriceToUpdate =(double)Math.round(cpe.getLimitPrice() * 10000d) / 10000d; 
			//if(orders.size()==1){
				//only 1 order of type STP
				//int oid = orders.get(0).orderId();
				
				manager.getOpenOrderList().get(0).auxPrice(stopPriceToUpdate);
			
				if(uo==null){
				uo = new UpdateOrder(
						//orders.get(0),
						this.contract, 
						//stopPriceToUpdate,
						this.wrapper,
						//this.manager,
						this.datafactory
						);
				}
				uo.invoke(manager.getOpenOrderList().get(0)); //pass Order as param
				//uot.start();
			//}
		}
		/*catch(TradingEvent te){
				
		}*/
		catch(NotEnoughDataException cpe){
			//do nothing, just continue loop
			//lg.trace("Caught NotEnoughDataException");
			
		}
		catch(Exception e){
			lg.error("Error evaluating candle pattern: " + e.getMessage());
		}
	}
	
	public long sumVolume(ArrayDeque<IBar> cc){
		long sum = 0;
		for(IBar c : cc){
			sum+=c.volume();
		}
		
		return sum;
	}

	/**
	 * @return the working
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * @param working the working to set
	 */
	public void setWorking(boolean working) {
		this.working = working;
	}

	/**
	 * @return the strategy
	 */
	public IStrategy getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(StrategyFX1 strategy) {
		this.strategy = strategy;
	}
	
	public synchronized ArrayDeque<Candle> getCache(){
		return this.candles;
	}
	
	public synchronized ArrayDeque<IBar> getCurrentCandle(){
		return this.currentCandle;
	}

	/**
	 * @return the reqId
	 */
	public int getReqId() {
		return reqId;
	}

	/**
	 * @param reqId the reqId to set
	 */
	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	/**
	 * @param mode the mode to set
	 */

	public void addHistorical(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) throws Exception {
		//translate latest tick into a candle
		String fp = "addHistorical: ";
		Candle c = new Candle(time,open,latestClose,high,low,wap,volume,count);

		//adding it to a day candle queue 
		candles.push(c);
		if(lg.isDebugEnabled())
			lg.debug(fp + "pushed new candle to candles cache, size: " + this.candles.size());
		
		//if(candles.size()>=240){
		//	notify();
		//}
	}

}