package com.pancorp.tbroker.placeorder;

//import com.ib.controller.ApiConnection.ILogger;
//import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
//import com.ib.client.TagValue;
//import com.ib.client.Types.Action;
//import com.ib.client.Types.SecType;
//import com.ib.client.Types.TimeInForce;
//import com.pancorp.tbroker.adapter.ConnectionHandlerAdapter;
//import com.pancorp.tbroker.adapter.TopMktDataAdapter;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;

//import samples.testbed.contracts.ContractSamples;
//import samples.testbed.orders.OrderSamples;
/*
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IOrderHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.ITopMktDataHandler;
*/
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.Order;
//import com.ib.client.OrderState;
//import com.ib.client.TickType;
import com.ib.client.Types;

public class PlaceOrderBracket //extends ConnectionHandlerAdapter implements ILogger 
{
	private static Logger lg = LogManager.getLogger(PlaceOrderBracket.class);
	//private final ApiController m_controller = new ApiController(this,this,this);
	PlaceOrderEWrapperImpl wrapper;// = new PlaceOrderEWrapperImpl();	
	final EClientSocket m_client;// = wrapper.getClient();
	final EReaderSignal m_signal;// = wrapper.getSignal();
	private Contract m_contract;
	private String m_symbol;
	private double m_limitPrice;
	private int nextOrderId=-1;
	//private double m_position;
	//private double m_bid;
	//private double m_ask;
	private boolean m_placedOrder;
	final private int m_clientId = Globals.paperClientId;
	
	void invoke() throws InterruptedException, Exception {
		//create contract
		m_contract = new Contract();
		m_contract.secType(Types.SecType.STK);
		m_contract.symbol(m_symbol);
		m_contract.currency("USD");
		m_contract.exchange("SMART");
		//Specify the Primary Exchange attribute to avoid contract ambiguity
		m_contract.primaryExch("ISLAND");
		
		//connect
		lg.info("run: connecting");
		m_client.eConnect(Globals.host, Globals.port, this.m_clientId);
		Thread.sleep(500);
		
		m_client.reqIds(-1);
		//Thread.sleep(1000);
		
		//start eReader and runner thread
		final int localClientId = this.m_clientId;
		//! [ereader]a
		final EReader reader = new EReader(m_client, m_signal);        
		reader.setName("Reader_" + this.m_clientId);
		reader.start();        
		new Thread() {
		    public void run() {
		        this.setName("Runner_"+localClientId);
		        while (m_client.isConnected()) {
			    	m_signal.waitForSignal();
		    		try {
		    			reader.processMsgs();
		    		} catch (Exception e) {
		    					lg.error("Exception: "+e.getMessage());
		    		}
		        }
		    }
		}.start();
		//! [ereader]
		
		//Thread.sleep(5000);
		//m_client.eDisconnect();
		//lg.trace("disconnected");
		        
		lg.info("run: done");
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
	private static void bracketSubmit(EClientSocket client, List<Order> bracket, Contract contract ) throws InterruptedException {
		String fp = "bracketSubmit: ";
		//BRACKET ORDER
        //! [bracketsubmit]
		//List<Order> bracket = OrderSamples.BracketOrder(nextOrderId++, "BUY", 100, 30, 40, 20);
		for(Order o : bracket) {
			//client.placeOrder(o.orderId(), ContractSamples.EuropeanStock(), o);
			client.placeOrder(o.orderId(), contract, o);
			lg.info(fp + "order " + o.orderId() + " placed");
		}
		//! [bracketsubmit]	
	}

	
	private List<Order> createBracketOrder(int parentOrderId, String action, double quantity, double limit, double takeProfitLimitPrice, double stopLossPrice){
		//String fp = "createOrder: ";
		
		//This will be our main or "parent" order
		Order parent = new Order();
		parent.orderId(parentOrderId);
		parent.action(action);
		parent.totalQuantity(quantity);
		
		/*
		 * A Limit order is an order to buy or sell at a specified price or better. 
		 * The Limit order ensures that if the order fills, it will not fill at a price 
		 * less favorable than your limit price, but it does not guarantee a fill.
		 */
		parent.orderType(OrderType.LMT);// limit
		parent.lmtPrice(limit);
		
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
		
		Order takeProfit = new Order();
		takeProfit.orderId(parent.orderId() + 1);
		takeProfit.action(action.equals("BUY") ? "SELL" : "BUY");
		takeProfit.orderType(OrderType.LMT);//"LMT");
		takeProfit.totalQuantity(quantity);
		takeProfit.lmtPrice(takeProfitLimitPrice);
		takeProfit.parentId(parentOrderId);
		takeProfit.transmit(false);
		
		Order stopLoss = new Order();
		stopLoss.orderId(parent.orderId() + 2);
		stopLoss.action(action.equals("BUY") ? "SELL" : "BUY");
		
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
		stopLoss.parentId(parentOrderId);
		//In this case, the low side order will be the last child being sent. Therefore, it needs to set this attribute to true 
        //to activate all its predecessors
		stopLoss.transmit(true);
		
		List<Order> bracketOrder = new ArrayList<Order>();
		bracketOrder.add(parent);
		bracketOrder.add(takeProfit);
		bracketOrder.add(stopLoss);
		
		return bracketOrder;
	}
	
	void print(String str){
		lg.info(str);
	}
	
	public void setNextOrderId(int oid){
		this.nextOrderId = oid;
		
		lg.info("nextOrderId: " + nextOrderId);
		
		if(nextOrderId>0){
			String action = "BUY";
			double quantity = 100;
			double stopLoss = (double)Math.round(m_limitPrice*Constants.STOP_LOSS_PERCENT_FACTOR* 100d) / 100d;
			double takeProfit = (double)Math.round(m_limitPrice*Constants.TAKE_PROFIT_PERCENT_FACTOR* 10d) / 10d;
			//this.m_limitPrice*Constants.TAKE_PROFIT_PERCENT_FACTOR;
			lg.info("limit: " + this.m_limitPrice+ ", takeProfit: " + takeProfit + ", stopLoss: " + stopLoss);
			List<Order> bracketOrder = this.createBracketOrder(nextOrderId, action, quantity, this.m_limitPrice, takeProfit, stopLoss);
			lg.info("bracketOrder created");
			lg.info("submitting order");
			
			try {
				bracketSubmit(wrapper.getClient(), bracketOrder, m_contract);
			}
			catch(InterruptedException ie){
				lg.error("setNextOrder: caught InterruptedException while placing the order: " + ie.getMessage());
				this.m_client.eDisconnect();
				System.exit(-1);
			}
		}
		else{
			lg.info("SetNextOrderId: orderId is invalid; can't place the order ");
			this.m_client.eDisconnect();
			System.exit(-1);
		}
	}
	public int getNextOrderId(){
		return this.nextOrderId;
	}
	
	public PlaceOrderBracket(String sym, double limPr) throws Exception{
		this.m_symbol = sym;
		this.m_limitPrice = limPr;
		
		wrapper = new PlaceOrderEWrapperImpl(this);	
		m_client = wrapper.getClient();
		m_signal = wrapper.getSignal();
		
	
		//this.m_clientId = cid;
	}
	
	public static void main(String[] args) throws InterruptedException, Exception {
		lg.info("main: args[]: " + args);
		String sym=null;
		double lim=0;
		//int cid = Globals.clientId;
		for(int i=0;i<args.length;i++){
			lg.info("main: args["+i+"]:"+args[i]);	
			switch(i){
			case 0:
				sym = args[i];
				break;
			case 1:
				lim = Double.parseDouble(args[i]);
				break;
			/*case 2:
				try{
				cid = Integer.parseInt(args[i]);
				}
				catch(Exception e){
					cid = Globals.clientId;
				}*/
				default:
			}
		}
		new PlaceOrderBracket(sym,lim).invoke();
		lg.info("main: done");
		//System.exit(0);
	}
}
