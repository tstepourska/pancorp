package com.pancorp.tbroker.order;

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
		submitCancel(wrapper.getClient(), orderId);	
		
		lg.info("run: done");
	}

	/**
	 * Cancels order
	 * 
	 * @param client
	 * @param oid
	 */
	private static void submitCancel(EClientSocket client, int oid) {
		String fp = "submitCancel: ";
		
		try {
		if(oid>0){
			client.cancelOrder(oid);
			lg.info(fp + "order " + oid + " requested to cancel");
		}
		else{
			lg.info("orderId is invalid; can't cancel the order ");
			//manager.setOrderPlaced(false); //release the flag
		}
		}
		catch(Exception e){
			lg.error("submitCancel: Error: " + e.getMessage());
			Utils.logError(lg, e);
		}
	}
}
