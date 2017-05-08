package com.pancorp.tbroker.day;

import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.util.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.EClientSocket;

public class CancelOrderT extends Thread 
{
	private static Logger lg = LogManager.getLogger(CancelOrderT.class);
	
	BrokerManagerEWrapperImpl wrapper;
	BrokerManager manager;
	final EClientSocket m_client;
	private int orderId=-1;
	
	public CancelOrderT(int orderId, 
			BrokerManagerEWrapperImpl wr,
			BrokerManager mgr
			) throws Exception{
		this.orderId = orderId;
		
		wrapper = wr;
		m_client = wrapper.getClient();
		manager = mgr;
	}
	
	public void run() { 

		try {
			if(orderId>0){
				submitCancel(wrapper.getClient(), orderId);	
			}
			else{
				lg.info("orderId is invalid; can't cancel the order ");
				//manager.setOrderPlaced(false); //release the flag
			}
		}
		catch(InterruptedException ie){
			lg.error("setNextOrder: caught InterruptedException while placing the order: " + ie.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
		
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
	private static void submitCancel(EClientSocket client, int oid) throws InterruptedException,Exception {
		String fp = "submitCancel: ";
		
		client.cancelOrder(oid);
		lg.info(fp + "order " + oid + " requested to cancel");
	}
}
