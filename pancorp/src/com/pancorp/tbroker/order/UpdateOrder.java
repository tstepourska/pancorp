package com.pancorp.tbroker.order;

//import com.ib.client.OrderType;
//import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
//import com.pancorp.tbroker.util.Constants;
//import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;
import com.pancorp.tbroker.data.DataFactory;

//import java.util.ArrayList;
//import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;

public class UpdateOrder //extends Thread 
{
	private static Logger lg = LogManager.getLogger(UpdateOrder.class);
	
	BrokerManagerEWrapperImpl wrapper;
	///BrokerManager manager;
	final EClientSocket m_client;
	private Contract m_contract;
	//private double m_limitPrice;
	//private Order order;
	DataFactory datafactory;
	//final private int m_clientId = Globals.paperClientId;
	//private boolean stopUpdates;

	public UpdateOrder(
			//Order o,
			Contract con,
			//double limPr, 
			BrokerManagerEWrapperImpl wr,
			//BrokerManager mgr,
			DataFactory df
			) throws Exception{
		//this.m_limitPrice = limPr;
		//this.order = o;
		this.m_contract = con;
		wrapper = wr;
		m_client = wrapper.getClient();
		//manager = mgr;
		this.datafactory = df;
		//stopUpdates = false;
	}
	
	/*public void run() { 
		String orderStatus = null;
		//while(!stopUpdates){
			invoke();		

			orderStatus = datafactory.getOrderStatus(order.orderId());
			lg.info("orderStatus: " + orderStatus);
			if(orderStatus!=null && (orderStatus.equalsIgnoreCase("Filled")||
									orderStatus.equalsIgnoreCase("Cancelled")||
									orderStatus.equalsIgnoreCase("Inactive") ||
									orderStatus.equalsIgnoreCase("Expired") ||
									orderStatus.equalsIgnoreCase("Unknown") ||
									orderStatus.equalsIgnoreCase("PendingCancel") ||
									orderStatus.equalsIgnoreCase("ApiCancelled")
			)){
				stopUpdates = true;
				break;
			}
			else {
				try{
					//wait and keep monitoring
					Thread.sleep(3000);
				}
				catch(InterruptedException e){}
			}
			
		//	if(stopUpdates)
			//	break;
			
			// get close price from latest candle and recalculate and update stop loss order
		//	int conid = this.m_contract.conid(); //EURO sym id
		//	lg.debug("got conid: " + conid);
			
			//double close = (manager.getTMap().get(conid)).getCurrentCandle().peekFirst().close();
			//lg.debug("latest close: " + close);
			//this.order.auxPrice(close); //modify price for the STP order
		///}
		//lg.info("run: done");
	//}*/
	
	public void invoke(Order order){
		try {
			int orderId = order.orderId();
			lg.debug("orderId: " + orderId);

			this.m_client.placeOrder(orderId, this.m_contract, order);
			lg.info("Modified STP order " + orderId + " with  parent order id " + order.parentId() + " and auxPrice " + order.auxPrice()+ " placed");
			//update order in the local database
			this.datafactory.updateOrderLimitPrice(order.orderId(), order.auxPrice());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}
}
