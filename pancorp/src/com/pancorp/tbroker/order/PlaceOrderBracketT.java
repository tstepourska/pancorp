package com.pancorp.tbroker.order;

import com.ib.client.OrderType;
import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;
import com.pancorp.tbroker.data.DataFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;

public class PlaceOrderBracketT extends Thread 
{
	private static Logger lg = LogManager.getLogger(PlaceOrderBracketT.class);
	
	BrokerManagerEWrapperImpl wrapper;
	BrokerManager manager;
	final EClientSocket m_client;// = wrapper.getClient();
	//final EReaderSignal m_signal;// = wrapper.getSignal();
	private Contract m_contract;
	//private String m_symbol;
	private double m_limitPrice;
	private long quantity;
	//private int nextOrderId=-1;
	DataFactory datafactory;
	private String action;
	//private double m_position;
	//private double m_bid;
	//private double m_ask;
	//private boolean m_placedOrder;
	//final private int m_clientId = Globals.paperClientId;
	
	
	public PlaceOrderBracketT(//int orderId, 
			Contract c, 
			double limPr, 
			int q, 
			String a, 
			BrokerManagerEWrapperImpl wr,
			BrokerManager mgr,
			DataFactory df
			) throws Exception{
		//this.m_symbol = sym;
		m_contract = c;
		this.m_limitPrice = limPr;
		this.quantity = q;
		//this.nextOrderId = orderId;
		this.action = a;
		
		wrapper = wr; //new PlaceOrderEWrapperImpl(this);	
		m_client = wrapper.getClient();
		//m_signal = wrapper.getSignal();
		manager = mgr;
		this.datafactory = df;
		//this.m_clientId = cid;
	}
	
	public void run() { //throws InterruptedException, Exception {
		//create contract
	/*	m_contract = new Contract();
		m_contract.secType(Types.SecType.STK);
		m_contract.symbol(m_symbol);
		m_contract.currency("USD");
		m_contract.exchange("SMART");
		//Specify the Primary Exchange attribute to avoid contract ambiguity
		m_contract.primaryExch("ISLAND");*/
		
		invoke();
		
		lg.info("run: done");
	}
	
	public void invoke(){
		try {
			double stopLoss = 0;
			double takeProfit = 0;
			double adjustedLimPrice = 0;
			
			if(this.action==null){
				lg.error("Error: action unknown, exiting PlaceOrderBracket");
				return;
			}
			
			if(action.equals("BUY")){
				stopLoss 	= this.m_limitPrice - Constants.PIPS_STOP_LOSS;
				takeProfit 	= this.m_limitPrice + Constants.PIPS_PROFIT_TAKER;
			}
			else if(action.equals("SELL") || action.equals("SSHORT")){
				stopLoss 	= this.m_limitPrice + Constants.PIPS_STOP_LOSS;
				takeProfit 	= this.m_limitPrice - Constants.PIPS_PROFIT_TAKER;
			}
			adjustedLimPrice= (double)Math.round(m_limitPrice* 10000d) / 10000d;			
			takeProfit 		= (double)Math.round(takeProfit	 * 10000d) / 10000d;
			stopLoss 		= (double)Math.round(stopLoss	 * 10000d) / 10000d;

			lg.info("action: " + action + ", limit: " + adjustedLimPrice+ ", takeProfit: " + takeProfit + ", stopLoss: " + stopLoss);
			
				List<Order> bracketOrder = createBracketOrder(//nextOrderId, 
						action, quantity, adjustedLimPrice, takeProfit, stopLoss);
				lg.info("bracketOrder created, submitting order");
			
				bracketSubmit(wrapper.getClient(), bracketOrder, m_contract);	
				
				//in the wrapper
				//this.wrapper.getDataFactory().insertOrder(clientId, orderId, contract, order, orderState);
			//}
			//else{
				//lg.info("orderId is invalid; can't place the order ");
				//manager.setOrderPlaced(false); //release the flag
			//}
		}
		catch(InterruptedException ie){
			lg.error("setNextOrder: caught InterruptedException while placing the order: " + ie.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}

	/**
	 * Places order(s) 
	 * Use order.whatIf set to true to use order as a query without execution 
	 * to get commission and margin info in EWrapper.openOrder method
	 * 
	 * You can use it to modify order's price and/or size
	 * 
	 * @param client
	 * @param bracket
	 * @param contract
	 * @throws InterruptedException
	 */
	private void bracketSubmit(EClientSocket client, List<Order> bracket, Contract contract ) throws InterruptedException,Exception {
		String fp = "bracketSubmit: ";
		int parentOrderId = 0;
		int cnt = 0;
		
		//BRACKET ORDER
        //! [bracketsubmit]
		//List<Order> bracket = OrderSamples.BracketOrder(nextOrderId++, "BUY", 100, 30, 40, 20);
		
		for(Order o : bracket) {
			int orderId = datafactory.insertOrder(Globals.paperClientId, 
					//orderId, 
					contract, 
					o,    // order
					null  //orderState
					);
			lg.info(fp + "orderId: " + orderId);
			o.orderId(orderId);
			if(cnt==0){
				lg.debug("assigning " + orderId + " as parentOrderId");
				parentOrderId = orderId;
			}
			else{
				o.parentId(parentOrderId);
				lg.debug("assigned " + parentOrderId + " as parent to order " + o.orderId());
			}
			
			if(o.parentId()>0){
				lg.debug("parentOrderId " + o.parentId() + " >0, updating order #"+ o.orderId());
			datafactory.updateOrder(orderId, o.parentId());
			}
			cnt++;
		}
		
		for(Order o : bracket) {
			//client.placeOrder(o.orderId(), ContractSamples.EuropeanStock(), o);
			client.placeOrder(o.orderId(), contract, o);
			lg.info(fp + "order " + o.orderId() + " with  parent order id " + o.parentId() + " placed");
		}
		//! [bracketsubmit]	
		
		this.manager.setOpenOrderList(bracket);
	}

	/**
	 * Creates bracket order structure. No order Ids are set in this method!
	 * 
	 * @param action
	 * @param quantity
	 * @param limit
	 * @param takeProfitLimitPrice
	 * @param stopLossPrice
	 * @return
	 */
	private List<Order> createBracketOrder(//int parentOrderId, 
			String action, double quantity, double limit, double takeProfitLimitPrice, double stopLossPrice){
		//String fp = "createOrder: ";
		
		//This will be our main or "parent" order
		Order parent = new Order();
		//parent.orderId(parentOrderId);
		parent.action(action);
		parent.totalQuantity(quantity);
		
		/*
		 * A Limit order is an order to buy or sell at a specified price or better. 
		 * The Limit order ensures that if the order fills, it will not fill at a price 
		 * less favorable than your limit price, but it does not guarantee a fill.
		 */
		parent.orderType(OrderType.LMT);// limit
		parent.lmtPrice(limit);
		parent.account(Constants.PAPER_ACCT);
		
		/*
		 * (MIT) is an order to buy (or sell) a contract below (or above) the market. 
		 * Its purpose is to take advantage of sudden or unexpected changes in share 
		 * or other prices and provides investors with a trigger price to set an order 
		 * in motion. Investors may be waiting for excessive strength (or weakness) to cease, 
		 * which might be represented by a specific price point. MIT orders can be used 
		 * to determine whether or not to enter the market once a specific price level 
		 * has been achieved. This order is held in the system until the trigger price 
		 * is touched, and is then submitted as a market order. An MIT order is similar 
		 * to a stop order, except that an MIT sell order is placed above the current market price, 
		 * and a stop sell order is placed below
		 */
		//parent.orderType(OrderType.MIT);// market if touched
		//parent.auxPrice(price);

		//The parent and children orders will need this attribute set to false to prevent accidental executions.
		//The LAST CHILD will have it set to true.
		parent.transmit(false);
		
		//creating take profit order
		Order takeProfit = new Order();
		//takeProfit.orderId(parent.orderId() + 1);
		if(this.action.equals("BUY"))
			takeProfit.action("SELL");//action.equals("BUY") ? "SELL" : "BUY");
		else if(this.action.equals("SSHORT") || this.action.equals("SELL"))
			takeProfit.action("BUY");
		
		takeProfit.orderType(OrderType.LMT);//"LMT");
		takeProfit.totalQuantity(quantity);
		takeProfit.lmtPrice(takeProfitLimitPrice);
		//takeProfit.parentId(parentOrderId);
		takeProfit.account(Constants.PAPER_ACCT);
		takeProfit.transmit(false);
		
		//creating stop loss order
		Order stopLoss = new Order();
		//stopLoss.orderId(parent.orderId() + 2);
		//stopLoss.action(action.equals("BUY") ? "SELL" : "BUY");
		if(this.action.equals("BUY"))
			stopLoss.action("SELL");//action.equals("BUY") ? "SELL" : "BUY");
		else if(this.action.equals("SSHORT") || this.action.equals("SELL"))
			stopLoss.action("BUY");
		
		/*
		 * A Stop order is an instruction to submit a buy or sell market order if and when 
		 * the user-specified stop trigger price is attained or penetrated. A Stop order 
		 * is not guaranteed a specific execution price and may execute significantly away 
		 * from its stop price. 
		 * A Sell Stop order is always placed below the current market price and is typically 
		 * used to limit a loss or protect a profit on a long stock position.
		 * A Buy Stop order is always placed above the current market price. It is typically 
		 * used to limit a loss or help protect a profit on a short sale.
		 */
		stopLoss.orderType(OrderType.STP);//"STP");
		//Stop trigger price
		stopLoss.auxPrice(stopLossPrice);
		stopLoss.totalQuantity(quantity);
		//stopLoss.parentId(parentOrderId);
		stopLoss.account(Constants.PAPER_ACCT);
		//In this case, the low side order will be the last child being sent. Therefore, it needs to set this attribute to true 
        //to activate all its predecessors
		stopLoss.transmit(true);
		
		List<Order> bracketOrder = new ArrayList<Order>();
		bracketOrder.add(parent);
		bracketOrder.add(takeProfit);
		bracketOrder.add(stopLoss);
		
		return bracketOrder;
	}
	

	
	//public int getNextOrderId(){
	//	return this.nextOrderId;
	//}
}
