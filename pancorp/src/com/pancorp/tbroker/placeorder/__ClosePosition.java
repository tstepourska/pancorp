package com.pancorp.tbroker.placeorder;

import com.ib.controller.ApiConnection.ILogger;
import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.SecType;
import com.ib.client.Types.TimeInForce;
import com.pancorp.tbroker.adapter.ConnectionHandlerAdapter;
import com.pancorp.tbroker.adapter.TopMktDataAdapter;
import com.pancorp.tbroker.util.Globals;
import com.ib.controller.ApiController;
import com.ib.controller.ApiController.IOrderHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.ITopMktDataHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TickType;

public class __ClosePosition extends ConnectionHandlerAdapter implements ILogger {
	private static Logger lg = LogManager.getLogger(__ClosePosition.class);
	private final ApiController m_controller = new ApiController(this,this,this);
	private Contract m_contract;
	private String m_symbol;
	private double m_position;
	private double m_bid;
	private double m_ask;
	private boolean m_placedOrder;
	
	public static void main(String[] args){
		new __ClosePosition().run(args[0]);
	}
	
	void run(String symbol){
		m_symbol = symbol;
		m_controller.connect(Globals.host, Globals.port, Globals.paperClientId, "");
	}
	
	/**
	 * Callback method occurs when TWS (IB Gateway) sends the API 
	 * next valid order ID, that is stored in the ApiController class 
	 */
	@Override
	public void connected(){
		print("requesting positions");
		
		//first retrieve the position
		m_controller.reqPositions(new IPositionHandler(){

			/**
			 * Callback after all positions have been fed back
			 */
			@Override
			public void positionEnd() {
				onHavePosition();
			}

			/**
			 * Callback method overwritten in line
			 * called for all open position. Below interested only 
			 * in one position
			 * 
			 * @param account
			 * @param contract
			 * @param position
			 * @param avgCost
			 */
			@Override
			public void position(String account, Contract contract, double pos, double avgCost) {
				if(contract.symbol().equals(m_symbol)&&contract.secType()==SecType.STK){
					m_contract = contract;
					m_position = pos;
				}
			}
		});
	}
	
	protected void onHavePosition(){
		print("current position is " + m_position);
		
		if(m_position!=0){
			print("requesting market data");
			
			m_controller.reqTopMktData(m_contract, 
					"", //genericTickList, 
					false, //snapshot, 
				new TopMktDataAdapter() {

					@Override
					public void tickPrice(TickType tickType, 
							double price, 
							int canAutoExecute) {
						if(tickType == TickType.BID){
							m_bid = price;
							print("received bid " + price);
						}
						else if(tickType == TickType.ASK){
							m_ask = price;
							print("receive ask " + price);
						}
						
						checkPrices(this);
					}
			});
		}
	}
	
	void checkPrices(ITopMktDataHandler handler){
		if(m_bid!=0 && m_ask !=0 &&!m_placedOrder){
			m_placedOrder= true;
			
			print("desubscribing market data");
			m_controller.cancelTopMktData(handler);
		}
		
		placeOrder();
	}
	
	void placeOrder(){
		double midPrice = Math.round((m_bid + m_ask)/2*100) / 100.0 + 1;
		
		m_contract.exchange("SMART"); //smart routing
		m_contract.primaryExch("ISLAND"); // works for all US stocks
		
		Order order = new Order();
		order.action(m_position > 0 ? Action.SELL : Action.BUY);
		order.totalQuantity(Math.abs( m_position));
		
		//place LIMIT order at the current price (mid price)
		order.orderType(OrderType.LMT);
		order.lmtPrice(midPrice);
		order.tif(TimeInForce.DAY);
		
		print("placing order " + order);
		
		m_controller.placeOrModifyOrder(m_contract, order, new IOrderHandler() {
			
			@Override public void handle(int errorCode, String errMsg) {
				print("Order code and message: " + errorCode + " " + errMsg);
			}
			
			/**
			 * Callback
			 */
			@Override
			public void orderState(OrderState orderState) {
				print("Order state: " + orderState);
			}

			@Override
			public void orderStatus(OrderStatus status, double filled, double remaining, double avgFillPrice,
					long permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
				if(status==OrderStatus.Filled){
					print("Position has been closed");
					System.exit(0);
				}
			}
		});
	//}
	}

	@Override
	public void log(String valueOf){
	}
	
	void print(String str){
		lg.info(str);
	}
}
