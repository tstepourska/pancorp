package com.pancorp.tbroker.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.pancorp.tbroker.data.DataFactory;
import com.pancorp.tbroker.day.ForexBroker;
import com.pancorp.tbroker.day.ForexBroker5min;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

public class BrokerManager {
	private static Logger lg = LogManager.getLogger(BrokerManager.class);
	
	private volatile boolean orderPlaced;
//	private volatile boolean tradeOpened;
	private EClientSocket client;
	private BrokerManagerEWrapperImpl wrapper;
	//HashMap<Integer,ForexBroker5min> tMap;
	HashMap<Integer,ForexBroker> tMap;
	DataFactory dfac;	
	public boolean working = true;
	public boolean toCloseAllPositions = false;
	public boolean snapshotInProgress = false;
	int workingStatus = Constants.WORKING_STATUS_IDLE;  //0 - IDLE, 1-ACTIVE
	
	//int operationsMode = Constants.OPS_MODE_PAPER_TESTCACHE;
	private int operationsMode = Constants.OPS_MODE_PAPER_LIVE;
	
	private List<Order> openOrderList;
	
	public static void main(String[] args) {
		try {
			//resetWorkingFile();
			new BrokerManager().invoke(args);
		}
		catch(InterruptedException e){
			Utils.logError(lg, e);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
		finally{
			
		}
	}

	public void invoke(String[] __args) throws InterruptedException, Exception {
		orderPlaced = false;
		wrapper = new BrokerManagerEWrapperImpl(this);		
		client = wrapper.getClient();

		final EReaderSignal m_signal = wrapper.getSignal();
		//! [connect]
		client.eConnect(Globals.host, Globals.port, Globals.paperClientId);//TWS		
		//m_client.eConnect("127.0.0.1", 4001, 0);  //IB Gateway
		
		//! [connect]
		//! [ereader]
		final EReader reader = new EReader(client, m_signal);        
		reader.setName("Reader_1");
        reader.start();        
        new Thread() {
        	public void run() {
        		this.setName("Runner_1");
        		while (client.isConnected()) {
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
        Thread.sleep(1000);

        dfac = this.wrapper.getDataFactory();
        
        this.openOrderList = new ArrayList<>();

		//initialize brokers
        tMap = new HashMap<>();
        lg.info("Brokers map initialized");
    	
		//*** Requesting all open orders ***
      //  client.reqAllOpenOrders();
        try {Thread.sleep(3000);}catch(InterruptedException ie){}
        //*** Taking over orders to be submitted via TWS ***
        //! [reqautoopenorders]
        //client.reqAutoOpenOrders(true);
     
       	startTheDay();
       	
       	while(working()){
       		if(!working)
       			break;
       		
       		try {
       			Thread.sleep(3000);
       		}catch(InterruptedException ie){} 		
       	}
        
        endTheDay();
        		
		//Thread.sleep(10000);
		//m_client.eDisconnect();
		//lg.trace("disconnected");
		
	}
	
	public void orderCancelledCallback(int orderId){
		this.clearOrder(orderId);
		//this.orderPlaced = false;
		//this.resetBrokerMode();
	}
	
	public void orderStatusCallback(int orderId, String status, int permId, int parentId,int clientId){
		String fp = "orderStatusCallback: ";
		lg.debug(fp + "orderId: " + orderId);
		if(orderId<=0){			
			//if(this.openOrderList.isEmpty()){
				//this.orderPlaced = false;
				return;
			//}
		}
		
		//order id > 0, set orderPlaced flag immediately
		this.setOrderPlaced(true);
		//this.setBrokerMode(Constants.MODE_CLOSING);
		
		boolean found = false;
		for(Order o: this.openOrderList){
			if(o.orderId()==orderId){
				lg.debug(fp + " found orderId: " + orderId + " in the list");
				//found order in the list
				found = true;
				break;
			}
		}
		
		if(!found){
			//create
			Order o = new Order();
			o.orderId(orderId);
			o.parentId(parentId);
			o.clientId(clientId);
			
			
			this.openOrderList.add(o);		
		}
		/*
		if(this.openOrderList.isEmpty()){
			lg.info("setOrderFilled: order list is empty, resetting");
			//reset 
			this.orderPlaced = false;
			this.resetBrokerMode();
		}*/
	}
	
	public void openOrderCallback(int orderId, Contract contract, Order order, OrderState orderState){
		String fp = "openOrderCallback: ";
		lg.debug(fp + "orderId: " + orderId);
		if(orderId<=0){			
			//if(this.openOrderList.isEmpty()){
				//this.orderPlaced = false;
				return;
			//}
		}
		boolean found = false;
		if(orderState==null || (
				(orderState.status().equals(OrderStatus.ApiPending)) ||
				(orderState.status().equals(OrderStatus.PendingSubmit)) ||
				(orderState.status().equals(OrderStatus.PreSubmitted)) ||
				(orderState.status().equals(OrderStatus.Submitted)) ||
				(orderState.status().equals(OrderStatus.PendingCancel)) ||
				(orderState.status().equals(OrderStatus.Unknown)) )
		){
			//order id > 0, set orderPlaced flag immediately
			this.setOrderPlaced(true);
			//this.setBrokerMode(Constants.MODE_CLOSING);
			
			for(Order o: this.openOrderList){
				if(o.orderId()==orderId){
					lg.debug(fp + " found orderId: " + orderId + " in the list");
					//found order in the list
					found = true;
					break;
				}
			}
			
			//if found in the list, then ok, do nothing
			if(!found){
				this.openOrderList.add(order);
			}
		}
	
	}
	
	public void startTheDay(){
		lg.trace("Starting the day...");
		//! [connect]
		if(!client.isConnected()){
			lg.trace("Client is not connected, connecting...");
			client.eConnect(Globals.host, Globals.port, Globals.paperClientId);//TWS		
		//m_client.eConnect("127.0.0.1", 4001, 0);  //IB Gateway
		}
		lg.trace("Connected");
		lg.trace("Loading contracts");   
        //load the list of stocks to monitor and start broker threads
        HashMap<Integer,Contract> contracts = dfac.loadDayList();
        lg.trace("loaded contract list: " + contracts.size());
        
        this.wrapper.setConList(contracts);
				
        Iterator<Integer> keys = contracts.keySet().iterator();
        int key;
        //ForexBroker5min t;
        ForexBroker t;
        Contract contract;
        int i=0;
        while(keys.hasNext()){
        	key = keys.next();
        	contract = contracts.get(key);
 	
        	try {
        		t = new ForexBroker(contract,key,dfac,Constants.TFU_MIN, wrapper, this);
        		t.setName("Broker_"+contract.symbol());
        		//lg.trace("created Broker for id " + key);
        	
        		try {      	
        		switch(this.operationsMode){    		
        			case Constants.OPS_MODE_PAPER_LIVE:
        				//load data for the last available 4 hrs to calibrate
        				//t.calibrateOnLocalData();
        				tMap.put(key, t);
        				t.start();
        				lg.info("PAPER LIVE operations mode, loading historical data for the last 4 hrs for calibration");
        				historicalDataRequests(wrapper.getClient(), key, contract);
        				//wait();
        				
        				//lg.info("PAPER LIVE operations mode, requesting real time bars");
        				//realTimeBars(wrapper.getClient(), key, contract);
        				break;
        			case Constants.OPS_MODE_PAPER_HISTORICAL:
        				tMap.put(key, t);
        				t.start();
        		//historicalDataRequests(wrapper.getClient(), key, contract);
        			case Constants.OPS_MODE_PAPER_TESTCACHE:
        				tMap.put(key, t);
        				t.start();
        				lg.info("PAPER TESTCACHE operations mode, loading test cache from local database");
        				this.setupBacktestCache(key);
        				break;
        				default:
        					//not supported Ops mode, do nothing
        					lg.info("Not supported operations mode, do nothing");
        		}
        		
            	//for testing only:
            	//nextSnapshot(wrapper.getClient(),key, contract);
        		}
        		catch(Exception ie){ //Interrupted for realTimeBars
        		lg.error("Caught Interrupted Exception while trying to request real  time bars!");
        		}
        		i++;
        		try {
        			Thread.sleep(3000);
        		}
        		catch(InterruptedException e){}
        	}
        	catch(Exception e){
        		lg.error("Could not start Broker " + i + ": " + e.getMessage());
        	}
        }
        
        this.workingStatus = Constants.WORKING_STATUS_ACTIVE;
        
       // if(lg.isTraceEnabled())
        //	lg.trace(dfac.printCache());
	}
	
	private void setupBacktestCache(int rid){
		this.dfac.fillUpTestCache(rid);
	}

	private void endTheDay(){
		lg.trace("Wrapping up the day. tMap: " + tMap);
		
		//request all open orders to cancel
		client.reqGlobalCancel();
		
		//request all open positions to close (see callback method in wrapper)
		toCloseAllPositions = true;
		this.client.reqPositions();
		
		 Iterator<Integer> it = tMap.keySet().iterator();
		 if(lg.isTraceEnabled())
		 lg.trace("endTheDay: for each running Broker: ");
		//ForexBroker5min t;
		 ForexBroker t;
		 Integer key;
		 while(it.hasNext()){
			key = it.next();
			 lg.trace("key: " + key);
			 t = tMap.get(key);
			//! [cancelrealtimebars]
		     client.cancelRealTimeBars(key);
		     if(lg.isTraceEnabled())
		    	 lg.trace("--Cancelled real time bars");
			 		 
			 //unsubscribe broker from data 
			 dfac.unsubscribe(key);
			 if(lg.isTraceEnabled())
			     lg.trace("--Unsubscribed from data factory");
			 
			 //shut it down
			 tMap.get(key).setWorking(false);
			 if(lg.isTraceEnabled())
			     lg.trace("--Shut it down");
		 }
		 
		 try {
			 //waiting for all to shut down
			Thread.sleep(7000);
		}
		catch(InterruptedException ie){lg.error("Caught Interrupted Exception");}
		 
		dfac.cleanUp();
		if(lg.isTraceEnabled())
			  lg.trace("Closed data factory");
		tMap.clear();
		if(lg.isTraceEnabled())
			  lg.trace("Cleared the map");
			
		this.workingStatus = Constants.WORKING_STATUS_IDLE;
	}
	
	public void canExit(){
		//wrapper.getClient().eDisconnect();
		//lg.trace("Client disconnected");
		
		//System.exit(0);
	}
	
	public void nextSnapshot(EClientSocket client, HashMap<Integer,Contract> list){
		if(list==null || list.isEmpty())
			return;
		Iterator<Integer> rids = list.keySet().iterator();
		if(rids.hasNext()){
			int rid = rids.next();
			Contract c = list.remove(rid); // list.removeLast();
			//int id = c.conid();
			nextSnapshot(client, rid, c);  
		}
	}
	
	public void nextSnapshot(EClientSocket client, int rid, Contract c){
		snapshotInProgress = true;
		client.reqMktData(rid, c, "", true, null);  
	}
	
	public void endOfSnapshots(){
		startTheDay();
	}
	
	public void cancelRealTimeBars(EClientSocket client, int reqId){
		//! [cancelrealtimebars]
	     client.cancelRealTimeBars(reqId);
	}
	
	/**
	 * @return the openOrderList
	 */
	public List<Order> getOpenOrderList() {
		return openOrderList;
	}

	/**
	 * @param openOrderList the openOrderList to set
	 */
	public void setOpenOrderList(List<Order> openOrderList) {
		this.openOrderList = openOrderList;
	}

	public synchronized boolean isOrderPlaced(){
		return this.orderPlaced;
	}
	
	public synchronized boolean setOrderPlaced(boolean placed){
		if(placed){ //for setting to true (locking the order right)
			if(this.isOrderPlaced()) { //double check the flag first
				//the flag is taken, can't touch it, return false
				return false;
			}
			else{
				this.orderPlaced = placed;	//set the flag and lock the order right
				return true;
			}
		}
		else {
			//resetting the flag to false (releasing the right to place an order)
			this.orderPlaced = false;
			return true;
		}
	}
	
	/*public synchronized boolean isTradeOpened(){
		return this.tradeOpened;
	}*/
	
	/**
	 * Callback from wrapper.orderStatus
	 * @param orderId
	 * @param parentId
	 * @param clientId
	 */
	public void clearOrder(int orderId) { //, int parentId, int clientId){
		for(Order o: this.openOrderList){
			if(o.orderId()==orderId){
				this.openOrderList.remove(o);
				break;
			}
		}
		
		//list is empty, trade is closed
		if(this.openOrderList.isEmpty()){
			lg.info("setOrderFilled: order list is empty, resetting");
			//reset 
			this.orderPlaced = false;
			//tradeOpened = false;
			//this.resetBrokerMode();
		}
		else {
			//list is not empty, trade is opened
			//tradeOpened = true;
		}
	}
	
	/**
	 * @return the operationsMode
	 */
	public int getOperationsMode() {
		return operationsMode;
	}
	
	private void historicalDataRequests(EClientSocket client, int id, Contract contract) throws InterruptedException {	
		//int emptyCnt = 0;
		/*** Requesting historical data ***/
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -4);
		//SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		//String formatted = form.format(cal.getTime());
		String formatted = "";
		//client.reqHistoricalData(id, contract, formatted, "1 D", "1 min", "MIDPOINT", 1, 1, null);
		//client.reqHistoricalData(id, contract, formatted, "14400 S", "1 min", "MIDPOINT", 1, 1, null);  //144 seconds = 4 hrs
		//client.reqHistoricalData(id, contract, formatted, "1 D", "5 mins", "MIDPOINT", 1, 1, null);  //285 5 min bars 
		client.reqHistoricalData(id, contract, formatted, "36000 S", "5 mins", "MIDPOINT", 1, 1, null); //120 5 min bars
		//client.reqHistoricalData(reqId, contract, endDateTime, durationStr, barSize.toString(), whatToShow.toString(), rthOnly ? 1 : 0, 2, Collections.<TagValue>emptyList() );
		
		//boolean progress = true;
		int maxSize = this.tMap.get(id).getStrategy().getMaxCache();
		lg.debug("historicalDataRequests: max cache size: " + maxSize);
		
		//on the callback from historical data finished requesting real time bars
		
		/*** Canceling historical data requests ***/
		//client.cancelHistoricalData(id);
	}

	public void realTimeBars(EClientSocket client, int id, Contract contract) throws InterruptedException {
		lg.trace("realTimeBars");
		//Requesting real time bars 
        client.reqRealTimeBars(id, contract, 5, Constants.BAR_WHAT_TO_SHOW_MIDPOINT, false, null); //Constants.BAR_SIZE  //0	
	}
	
	 static void accountOperations(EClientSocket client) throws InterruptedException {
		
        //client.reqAccountUpdatesMulti(9002, null, "EUstocks", true);
		//! [reqpositionsmulti]
        client.reqPositionsMulti(9003, "DU74649", "EUstocks");
      //! [reqpositionsmulti]
        Thread.sleep(10000);

        /*** Requesting managed accounts***/
        //! [reqmanagedaccts]
        client.reqManagedAccts();
        //! [reqmanagedaccts]
        /*** Requesting accounts' summary ***/
        Thread.sleep(2000);
        //! [reqaaccountsummary]
        client.reqAccountSummary(9001, "All", "AccountType,NetLiquidation,TotalCashValue,SettledCash,AccruedCash,BuyingPower,EquityWithLoanValue,PreviousEquityWithLoanValue,GrossPositionValue,ReqTEquity,ReqTMargin,SMA,InitMarginReq,MaintMarginReq,AvailableFunds,ExcessLiquidity,Cushion,FullInitMarginReq,FullMaintMarginReq,FullAvailableFunds,FullExcessLiquidity,LookAheadNextChange,LookAheadInitMarginReq ,LookAheadMaintMarginReq,LookAheadAvailableFunds,LookAheadExcessLiquidity,HighestSeverity,DayTradesRemaining,Leverage");
        //! [reqaaccountsummary]
        
      //! [reqaaccountsummaryledger]
        client.reqAccountSummary(9002, "All", "$LEDGER");
        //! [reqaaccountsummaryledger]
        Thread.sleep(2000);
        //! [reqaaccountsummaryledgercurrency]
        client.reqAccountSummary(9003, "All", "$LEDGER:EUR");
        //! [reqaaccountsummaryledgercurrency]
        Thread.sleep(2000);
        //! [reqaaccountsummaryledgerall]
        client.reqAccountSummary(9004, "All", "$LEDGER:ALL");
        //! [reqaaccountsummaryledgerall]
		
		//! [cancelaaccountsummary]
		client.cancelAccountSummary(9001);
		client.cancelAccountSummary(9002);
		client.cancelAccountSummary(9003);
		client.cancelAccountSummary(9004);
		//! [cancelaaccountsummary]
        
        /*** Subscribing to an account's information. Only one at a time! ***/
        Thread.sleep(2000);
        //! [reqaaccountupdates]
        client.reqAccountUpdates(true, "U150462");
        //! [reqaaccountupdates]
		Thread.sleep(2000);
		//! [cancelaaccountupdates]
		client.reqAccountUpdates(false, "U150462");
		//! [cancelaaccountupdates]
		
        //! [reqaaccountupdatesmulti]
        client.reqAccountUpdatesMulti(9002, "U150462", "EUstocks", true);
        //! [reqaaccountupdatesmulti]
        Thread.sleep(2000);
        /*** Requesting all accounts' positions. ***/
        //! [reqpositions]
        client.reqPositions();
        //! [reqpositions]
		Thread.sleep(2000);
		//! [cancelpositions]
		client.cancelPositions();
		//! [cancelpositions]
    }
	
	 public HashMap<Integer,ForexBroker> getTMap(){
		 return this.tMap;
	 }
	 
	public boolean working(){
		BufferedReader r =null;
		int i = 1;
		
		try{
			r = new BufferedReader( new FileReader(Globals.BASEDIR + Constants.WORKING_FILE));
			String line = r.readLine();
			//if(lg.isTraceEnabled())
			//	lg.trace("working file line: " + line);
			
			try {
				i = Integer.parseInt(line);
				if(i==0){
					if(lg.isTraceEnabled())
						lg.trace("working is 0, setting flag to false");
					working = false;
				}
			}
			catch(Exception e){
				lg.error("Error parsing working line: " + e.getMessage());
				//TODO 
			}
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
		finally{
			try {
				r.close();
			}catch(Exception e){}
		}
		
		return working;
	}
	
	public static void resetWorkingFile(){
		FileWriter w =null;
		try{
			w = new FileWriter(Globals.BASEDIR + Constants.WORKING_FILE,false);
			w.write(Constants.WORKING_STATUS_ACTIVE);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
		finally{
			try {
				w.flush();
				w.close();
			}catch(Exception e){}
		}
	}
}
