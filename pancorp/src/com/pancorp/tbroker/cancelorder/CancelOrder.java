package com.pancorp.tbroker.cancelorder;

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

public class CancelOrder 
{
	private static Logger lg = LogManager.getLogger(CancelOrder.class);
	
	CancelOrderEWrapperImpl wrapper;
	final EClientSocket m_client;
	final EReaderSignal m_signal;
	//private Contract m_contract;
	
	private int m_orderId=-1;
	//private double m_position;
	//private double m_bid;
	//private double m_ask;
	private boolean m_placedOrder;
	final private int m_clientId = Globals.paperClientId;
	
	void invoke() throws InterruptedException, Exception {
		//create contract
		/*m_contract = new Contract();
		m_contract.secType(Types.SecType.STK);
		m_contract.symbol(m_symbol);
		m_contract.currency("USD");
		m_contract.exchange("SMART");
		//Specify the Primary Exchange attribute to avoid contract ambiguity
		m_contract.primaryExch("ISLAND");
		*/
		
		//connect
		lg.info("run: connecting");
		m_client.eConnect(Globals.host, Globals.port, this.m_clientId);
		Thread.sleep(500);
		
		//m_client.reqIds(-1);
		//Thread.sleep(1000);
		
		//start eReader and runner thread
		int localClientId = this.m_clientId;
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
		
		m_client.cancelOrder(m_orderId);
		
		//Thread.sleep(5000);
		//m_client.eDisconnect();
		//lg.trace("disconnected");
		        
		lg.info("invoke: done");
	}

	void print(String str){
		lg.info(str);
	}
	
	public CancelOrder(int oid) throws Exception{
		//this.m_clientId = cid;
		this.m_orderId = oid;
		
		wrapper = new CancelOrderEWrapperImpl(this);	
		m_client = wrapper.getClient();
		m_signal = wrapper.getSignal();
	}
	
	public static void main(String[] args) throws InterruptedException, Exception {
		lg.info("main: args[]: " + args);

		int cid = Globals.paperClientId;
		int oid = -1;
		for(int i=0;i<args.length;i++){
			lg.info("main: args["+i+"]:"+args[i]);	
			switch(i){
			case 0:
				//cid = Integer.parseInt(args[i]);
				//break;
			//case 1:
				oid = Integer.parseInt(args[i]);
				lg.info("orderId to cancel: " + oid);
				break;
				default:
			}
		}
		new CancelOrder(oid).invoke();
		lg.info("main: done");
		//System.exit(0);
	}
}
