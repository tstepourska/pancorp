package com.pancorp.tbroker.insertbar;

import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

public class RequestBars
{
	private static Logger lg = LogManager.getLogger(RequestBars.class);
	
	RequestBarsEWrapperImpl wrapper;
	final EClientSocket m_client;
	final EReaderSignal m_signal;
	private HashMap<Integer, Contract> m_contracts;
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
		
		Iterator<Integer> it = this.m_contracts.keySet().iterator();
		int tickerId;
		Contract contract;
		while(it.hasNext()){
			tickerId = it.next();
			contract = m_contracts.get(tickerId);
			
		m_client.reqRealTimeBars(tickerId, 
								contract,   //contract, 
								Constants.BAR_SIZE, 
								Constants.BAR_WHAT_TO_SHOW_TRADES,  //whatToShow, 
								true,  //useRTH, 
								null  //realTimeBarsOptions
								);
		}
		
		Thread.sleep(20000);
		it = this.m_contracts.keySet().iterator();

		while(it.hasNext()){
			tickerId = it.next();			
			m_client.cancelRealTimeBars(tickerId);
		}
		
		Thread.sleep(5000);
		m_client.eDisconnect();
		lg.trace("disconnected");
		        
		lg.info("invoke: done");
	}

	void print(String str){
		lg.info(str);
	}
	
	public RequestBars(HashMap<Integer,Contract> cc) throws Exception{
		this.m_contracts = cc;
		
		wrapper = new RequestBarsEWrapperImpl(this, cc);	
		m_client = wrapper.getClient();
		m_signal = wrapper.getSignal();
	}
	
	public static void main(String[] args) throws InterruptedException, Exception {
		lg.info("main: args[]: " + args);

		HashMap<Integer,Contract> cc = DataFactory.getMonitorList();
		new RequestBars(cc).invoke();
		lg.info("main: done");
		//System.exit(0);
	}
}
