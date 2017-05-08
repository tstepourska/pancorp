package com.pancorp.tbroker.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import samples.testbed.advisor.FAMethodSamples;
import samples.testbed.contracts.ContractSamples;
import samples.testbed.orders.AvailableAlgoParams;
import samples.testbed.orders.OrderSamples;
import samples.testbed.scanner.ScannerSubscriptionSamples;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;
import com.ib.client.Types.FADataType;
import com.ib.controller.AccountSummaryTag;
import com.pancorp.tbroker.day.DataFactory;
import com.pancorp.tbroker.day.ForexBroker;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

public class BrokerManager {
	private static Logger lg = LogManager.getLogger(BrokerManager.class);
	
	private volatile boolean orderPlaced;
	private EClientSocket client;
	private BrokerManagerEWrapperImpl wrapper;
	HashMap<Integer,ForexBroker> tMap;
	DataFactory dfac;	
	public boolean working = true;
	public boolean toCloseAllPositions = false;
	public boolean snapshotInProgress = false;
	int workingStatus = Constants.WORKING_STATUS_IDLE;  //0 - IDLE, 1-ACTIVE
	
	private List<Order> openOrderList;
	
	public static void main(String[] args) {
		try {
			resetWorkingFile();
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
		wrapper = new BrokerManagerEWrapperImpl(this);		
		//final EClientSocket m_client = wrapper.getClient();
		client = wrapper.getClient();
		
		//I've added to try:
		//m_client.setAsyncEConnect(false);
		
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
		
		//orderOperations(wrapper.getClient(), wrapper.getCurrentOrderId());
		//contractOperations(wrapper.getClient());
        //hedgeSample(wrapper.getClient(), wrapper.getCurrentOrderId());
        //testAlgoSamples(wrapper.getClient(), wrapper.getCurrentOrderId());
        //bracketSample(wrapper.getClient(), wrapper.getCurrentOrderId());
		//bulletins(wrapper.getClient());
        //reutersFundamentals(wrapper.getClient());
        //marketDataType(wrapper.getClient());
        //historicalDataRequests(wrapper.getClient());
        //accountOperations(wrapper.getClient());
        //marketScanners(wrapper.getClient());
        

        dfac = this.wrapper.getDataFactory();
        
        //first reload market data
        //HashMap<Integer,Contract> cMap = dfac.loadFullList();	    
        //LinkedList<Contract> cMap = dfac.loadFullList();	
       // this.wrapper.setConList(cMap);

        //lg.debug("requesting market data for id " + rid + "::" + cMap.get(rid));
       // nextSnapshot(wrapper.getClient(),cMap);
     
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
	
	/**
	 * Designed for a single ForexBroker instance
	 */
	public void resetBrokerMode(){
		Iterator<Integer> it = this.tMap.keySet().iterator();
		tMap.get(it.next()).setMode(Constants.MODE_OPENING);
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
	
		//initialize brokers
        tMap = new HashMap<>();
        lg.info("Brokers map initialized");
 
		lg.trace("Loading contracts");   
        //load the list of stocks to monitor and start broker threads
        HashMap<Integer,Contract> stocks = dfac.loadDayList();
        lg.trace("loaded contract list: " + stocks.size());
				
        Iterator<Integer> keys = stocks.keySet().iterator();
        int key;
        ForexBroker t;
        //String sym;
        Contract contract;
       int i=0;
       // String sym;
        while(keys.hasNext()){
        	key = keys.next();
        	contract = stocks.get(key);
 	
        	try {
        		t = new ForexBroker(contract,key,dfac,Constants.TFU_MIN, wrapper, this);
        		t.setName("Broker_"+contract.symbol());
        		//lg.trace("created Broker for id " + key);
        	
        		tMap.put(key, t);
        		//lg.trace("requesting real time bars for id " + key);
        	
        		try {
        		t.start();
            	
            	//realTimeBars(wrapper.getClient(), key, contract);
        		//historicalDataRequests(wrapper.getClient(), key, contract);
        		this.setupBacktestCache(key);
        		
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
	
	public void setOrderFilled(int orderId, int parentId, int clientId){
		
	}
	
	
	private static void orderOperations(EClientSocket client, int nextOrderId) throws InterruptedException {
		
		//Requesting the next valid id 
		//! [reqids]
        //The parameter is always ignored.
        client.reqIds(-1);
        //! [reqids]
        //Thread.sleep(1000);
        /*** Requesting all open orders ***/
        //! [reqallopenorders]
        client.reqAllOpenOrders();
        //! [reqallopenorders]
        //Thread.sleep(1000);
        /*** Taking over orders to be submitted via TWS ***/
        //! [reqautoopenorders]
        client.reqAutoOpenOrders(true);
        //! [reqautoopenorders]
        //Thread.sleep(1000);
        /*** Requesting this API client's orders ***/
        //! [reqopenorders]
        client.reqOpenOrders();
        //! [reqopenorders]
        //Thread.sleep(1000);
		
        /*** Placing/modifying an order - remember to ALWAYS increment the nextValidId after placing an order so it can be used for the next one! ***/
        //! [order_submission]
        client.placeOrder(nextOrderId++, ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 1, 50));
        //! [order_submission]
        
        //! [faorderoneaccount]
        Order faOrderOneAccount = OrderSamples.MarketOrder("BUY", 100);
        // Specify the Account Number directly
        faOrderOneAccount.account("DU119915");
        client.placeOrder(nextOrderId++, ContractSamples.USStock(), faOrderOneAccount);
        //! [faorderoneaccount]
        
        //! [faordergroupequalquantity]
        Order faOrderGroupEQ = OrderSamples.LimitOrder("SELL", 200, 2000);
        faOrderGroupEQ.faGroup("Group_Equal_Quantity");
        faOrderGroupEQ.faMethod("EqualQuantity");
        client.placeOrder(nextOrderId++, ContractSamples.USStock(), faOrderGroupEQ);
        //! [faordergroupequalquantity]
        
        //! [faordergrouppctchange]
        Order faOrderGroupPC = OrderSamples.MarketOrder("BUY", 0); ;
        // You should not specify any order quantity for PctChange allocation method
        faOrderGroupPC.faGroup("Pct_Change");
        faOrderGroupPC.faMethod("PctChange");
        faOrderGroupPC.faPercentage("100");
        client.placeOrder(nextOrderId++, ContractSamples.EurGbpFx(), faOrderGroupPC);
        //! [faordergrouppctchange]
        
        //! [faorderprofile]
        Order faOrderProfile = OrderSamples.LimitOrder("BUY", 200, 100);
        faOrderProfile.faProfile("Percent_60_40");
        client.placeOrder(nextOrderId++, ContractSamples.EuropeanStock(), faOrderProfile);
        //! [faorderprofile]
        
		//client.placeOrder(nextOrderId++, ContractSamples.USStock(), OrderSamples.PeggedToMarket("BUY", 10, 0.01));
		//client.placeOrder(nextOrderId++, ContractSamples.EurGbpFx(), OrderSamples.MarketOrder("BUY", 10));
        //client.placeOrder(nextOrderId++, ContractSamples.USStock(), OrderSamples.Discretionary("SELL", 1, 45, 0.5));
		
        //! [reqexecutions]
        client.reqExecutions(10001, new ExecutionFilter());
        //! [reqexecutions]
        
        Thread.sleep(10000);
        
    }
	/*
	private static void OcaSample(EClientSocket client, int nextOrderId) {
		
		//OCA order
		//! [ocasubmit]
		List<Order> OcaOrders = new ArrayList<Order>();
		OcaOrders.add(OrderSamples.LimitOrder("BUY", 1, 10));
		OcaOrders.add(OrderSamples.LimitOrder("BUY", 1, 11));
		OcaOrders.add(OrderSamples.LimitOrder("BUY", 1, 12));
		OcaOrders = OrderSamples.OneCancelsAll("TestOCA_" + nextOrderId, OcaOrders, 2);
		for (Order o : OcaOrders) {
			
			client.placeOrder(nextOrderId++, ContractSamples.USStock(), o);
		}
		//! [ocasubmit]
		
	}
*/	
	private static void tickDataOperations(EClientSocket client) throws InterruptedException {
		
		/*** Requesting real time market data ***/
		//Thread.sleep(1000);
		//! [reqmktdata]
		client.reqMktData(1001, ContractSamples.StockComboContract(), "", false, null);
		//! [reqmktdata]
		
		//! [reqmktdata_snapshot]
		client.reqMktData(1003, ContractSamples.FutureComboContract(), "", true, null);
		//! [reqmktdata_snapshot]
		
		//! [reqmktdata_genticks]
		//Requesting RTVolume (Time & Sales), shortable and Fundamental Ratios generic ticks
		client.reqMktData(1004, ContractSamples.USStock(), "233,236,258", false, null);
		//! [reqmktdata_genticks]
		//! [reqmktdata_contractnews]
		client.reqMktData(1005, ContractSamples.USStock(), "mdoff,292:BZ", false, null);
		client.reqMktData(1006, ContractSamples.USStock(), "mdoff,292:BT", false, null);
		client.reqMktData(1007, ContractSamples.USStock(), "mdoff,292:FLY", false, null);
		client.reqMktData(1008, ContractSamples.USStock(), "mdoff,292:MT", false, null);
		//! [reqmktdata_contractnews]
		//! [reqmktdata_broadtapenews]
		client.reqMktData(1009, ContractSamples.BTbroadtapeNewsFeed(), "mdoff,292", false, null);
		client.reqMktData(1010, ContractSamples.BZbroadtapeNewsFeed(), "mdoff,292", false, null);
		client.reqMktData(1011, ContractSamples.FLYbroadtapeNewsFeed(), "mdoff,292", false, null);
		client.reqMktData(1012, ContractSamples.MTbroadtapeNewsFeed(), "mdoff,292", false, null);
		//! [reqmktdata_broadtapenews]
		//! [reqoptiondatagenticks]
        //Requesting data for an option contract will return the greek values
        client.reqMktData(1002, ContractSamples.OptionWithLocalSymbol(), "", false, null);
        //! [reqoptiondatagenticks]
		
		Thread.sleep(10000);
		//! [cancelmktdata]
		client.cancelMktData(1001);
		client.cancelMktData(1002);
		client.cancelMktData(1003);
		//! [cancelmktdata]
		
	}
	
	private static void historicalDataRequests(EClientSocket client, int id, Contract contract) throws InterruptedException {
		
		/*** Requesting historical data ***/
        //! [reqhistoricaldata]
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String formatted = form.format(cal.getTime());
		client.reqHistoricalData(id, contract, formatted, "1 M", "1 min", "MIDPOINT", 1, 1, null);
		//Thread.sleep(10000);
		/*** Canceling historical data requests ***/
		//client.cancelHistoricalData(id);

		//! [reqhistoricaldata]
		
	}
	/*
	private static void historicalDataRequests(EClientSocket client) throws InterruptedException {
		
		///Requesting historical data 
        //! [reqhistoricaldata]
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String formatted = form.format(cal.getTime());
		client.reqHistoricalData(4001, ContractSamples.EurGbpFx(), formatted, "1 M", "1 day", "MIDPOINT", 1, 1, null);
		client.reqHistoricalData(4002, ContractSamples.EuropeanStock(), formatted, "10 D", "1 min", "TRADES", 1, 1, null);
		Thread.sleep(2000);
		/// Canceling historical data requests 
		client.cancelHistoricalData(4001);
        client.cancelHistoricalData(4002);
		//! [reqhistoricaldata]
		
	}
	*/
	private static void realTimeBars(EClientSocket client, int id, Contract contract) throws InterruptedException {
		lg.trace("realTimeBars");
		//Requesting real time bars 
        //! [reqrealtimebars]
        client.reqRealTimeBars(id, contract, 5, Constants.BAR_WHAT_TO_SHOW_MIDPOINT, false, null); //Constants.BAR_SIZE  //0
        //! [reqrealtimebars]
        //Thread.sleep(5000);
        // Canceling real time bars will happen on callback 
        //! [cancelrealtimebars]
        //client.cancelRealTimeBars(3001);
        //! [cancelrealtimebars]
		
	}
	
/*	private static void realTimeBars(EClientSocket client) throws InterruptedException {
		
		/// Requesting real time bars 
        //! [reqrealtimebars]
        client.reqRealTimeBars(3001, ContractSamples.EurGbpFx(), 5, "MIDPOINT", true, null);
        //! [reqrealtimebars]
        //Thread.sleep(5000);
        /// Canceling real time bars 
        //! [cancelrealtimebars]
        //client.cancelRealTimeBars(3001);
        //! [cancelrealtimebars]
		
	}*/
	
	private static void marketDepthOperations(EClientSocket client) throws InterruptedException {
		
		/*** Requesting the Deep Book ***/
        //! [reqmarketdepth]
        client.reqMktDepth(2001, ContractSamples.EurGbpFx(), 5, null);
        //! [reqmarketdepth]
        Thread.sleep(2000);
        /*** Canceling the Deep Book request ***/
        //! [cancelmktdepth]
        client.cancelMktDepth(2001);
        //! [cancelmktdepth]
		
	}
	
	private static void accountOperations(EClientSocket client) throws InterruptedException {
		
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
	
	private static void conditionSamples(EClientSocket client, int nextOrderId) {
		
		//! [order_conditioning_activate]
		Order mkt = OrderSamples.MarketOrder("BUY", 100);
		//Order will become active if conditioning criteria is met
		mkt.conditionsCancelOrder(true);
		mkt.conditions().add(OrderSamples.PriceCondition(208813720, "SMART", 600, false, false));
		mkt.conditions().add(OrderSamples.ExecutionCondition("EUR.USD", "CASH", "IDEALPRO", true));
		mkt.conditions().add(OrderSamples.MarginCondition(30, true, false));
		mkt.conditions().add(OrderSamples.PercentageChangeCondition(15.0, 208813720, "SMART", true, true));
		mkt.conditions().add(OrderSamples.TimeCondition("20160118 23:59:59", true, false));
		mkt.conditions().add(OrderSamples.VolumeCondition(208813720, "SMART", false, 100, true));
		client.placeOrder(nextOrderId++, ContractSamples.EuropeanStock(), mkt);
		//! [order_conditioning_activate]
		
		//Conditions can make the order active or cancel it. Only LMT orders can be conditionally canceled.
		//! [order_conditioning_cancel]
		Order lmt = OrderSamples.LimitOrder("BUY", 100, 20);
		//The active order will be cancelled if conditioning criteria is met
		lmt.conditionsCancelOrder(true);
		lmt.conditions().add(OrderSamples.PriceCondition(208813720, "SMART", 600, false, false));
		client.placeOrder(nextOrderId++, ContractSamples.EuropeanStock(), lmt);
		//! [order_conditioning_cancel]
		
	}
	
	private static void contractOperations(EClientSocket client) {
		
		//! [reqcontractdetails]
		client.reqContractDetails(210, ContractSamples.OptionForQuery());
		//! [reqcontractdetails]
		
	}
	
	private static void contractNewsFeed(EClientSocket client) {
		
		//! [reqcontractdetailsnews]
		client.reqContractDetails(211, ContractSamples.NewsFeedForQuery());
		//! [reqcontractdetailsnews]
		
	}
	
	private static void hedgeSample(EClientSocket client, int nextOrderId) throws InterruptedException {
		
		//F Hedge order
		//! [hedgesubmit]
		//Parent order on a contract which currency differs from your base currency
		Order parent = OrderSamples.LimitOrder("BUY", 100, 10);
		parent.orderId(nextOrderId++);
		//Hedge on the currency conversion
		Order hedge = OrderSamples.MarketFHedge(parent.orderId(), "BUY");
		//Place the parent first...
		client.placeOrder(parent.orderId(), ContractSamples.EuropeanStock(), parent);
		//Then the hedge order
		client.placeOrder(nextOrderId++, ContractSamples.EurGbpFx(), hedge);
		//! [hedgesubmit]
		
	}
	
	private static void testAlgoSamples(EClientSocket client, int nextOrderId) throws InterruptedException {
		
		//! [algo_base_order]
		Order baseOrder = OrderSamples.LimitOrder("BUY", 1000, 1);
		//! [algo_base_order]
		
		//! [arrivalpx]
		AvailableAlgoParams.FillArrivalPriceParams(baseOrder, 0.1, "Aggressive", "09:00:00 CET", "16:00:00 CET", true, true);
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [arrivalpx]
		
		Thread.sleep(500);
		
		//! [darkice]
		AvailableAlgoParams.FillDarkIceParams(baseOrder, 10, "09:00:00 CET", "16:00:00 CET", true);
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [darkice]
		
		Thread.sleep(500);
		
		//! [ad]
		AvailableAlgoParams.FillAccumulateDistributeParams(baseOrder, 10, 60, true, true, 1, true, true, "09:00:00 CET", "16:00:00 CET");
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [ad]
		
		Thread.sleep(500);
		
		//! [twap]
		AvailableAlgoParams.FillTwapParams(baseOrder, "Marketable", "09:00:00 CET", "16:00:00 CET", true);
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [twap]
		
		Thread.sleep(500);
		
		//! [vwap]
		AvailableAlgoParams.FillVwapParams(baseOrder, 0.2, "09:00:00 CET", "16:00:00 CET", true, true);
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [vwap]
		
		Thread.sleep(500);
		
		//! [balanceimpactrisk]
		AvailableAlgoParams.FillBalanceImpactRiskParams(baseOrder, 0.1, "Aggressive", true);
		client.placeOrder(nextOrderId++, ContractSamples.USOptionContract(), baseOrder);
		//! [balanceimpactrisk]
		
		Thread.sleep(500);
		
		//! [minimpact]
		AvailableAlgoParams.FillMinImpactParams(baseOrder, 0.3);
		client.placeOrder(nextOrderId++, ContractSamples.USOptionContract(), baseOrder);
		//! [minimpact]
		
		//! [adaptive]
		AvailableAlgoParams.FillAdaptiveParams(baseOrder, "Normal");
		client.placeOrder(nextOrderId++, ContractSamples.USStockAtSmart(), baseOrder);
		//! [adaptive]		
		
	}
	
	private static void bracketSample(EClientSocket client, int nextOrderId) throws InterruptedException {
		
		//BRACKET ORDER
        //! [bracketsubmit]
		List<Order> bracket = OrderSamples.BracketOrder(nextOrderId++, "BUY", 100, 30, 40, 20);
		for(Order o : bracket) {
			client.placeOrder(o.orderId(), ContractSamples.EuropeanStock(), o);
		}
		//! [bracketsubmit]
		
	}
	
	private static void bulletins(EClientSocket client) throws InterruptedException {
		
		//! [reqnewsbulletins]
		client.reqNewsBulletins(true);
		//! [reqnewsbulletins]
		
		Thread.sleep(2000);
		
		//! [cancelnewsbulletins]
		client.cancelNewsBulletins();
		//! [cancelnewsbulletins]
		
	}
	
	private static void reutersFundamentals(EClientSocket client) throws InterruptedException {
		
		//! [reqfundamentaldata]
		client.reqFundamentalData(8001, ContractSamples.USStock(), "ReportsFinSummary");
		//! [reqfundamentaldata]
		
		Thread.sleep(2000);
		
		//! [fundamentalexamples]
		client.reqFundamentalData(8002, ContractSamples.USStock(), "ReportSnapshot"); //for company overview
		client.reqFundamentalData(8003, ContractSamples.USStock(), "ReportRatios"); //for financial ratios
		client.reqFundamentalData(8004, ContractSamples.USStock(), "ReportsFinStatements"); //for financial statements
		client.reqFundamentalData(8005, ContractSamples.USStock(), "RESC"); //for analyst estimates
		client.reqFundamentalData(8006, ContractSamples.USStock(), "CalendarReport"); //for company calendar
		//! [fundamentalexamples]
		
		//! [cancelfundamentaldata]
		client.cancelFundamentalData(8001);
		//! [cancelfundamentaldata]
		
	}
	
	private static void marketScanners(EClientSocket client) throws InterruptedException {
		
		/*** Requesting all available parameters which can be used to build a scanner request ***/
        //! [reqscannerparameters]
        client.reqScannerParameters();
        //! [reqscannerparameters]
        Thread.sleep(2000);

        /*** Triggering a scanner subscription ***/
        //! [reqscannersubscription]
        client.reqScannerSubscription(7001, ScannerSubscriptionSamples.HighOptVolumePCRatioUSIndexes(), null);
        //! [reqscannersubscription]

        Thread.sleep(2000);
        /*** Canceling the scanner subscription ***/
        //! [cancelscannersubscription]
        client.cancelScannerSubscription(7001);
        //! [cancelscannersubscription]
		
	}
	
	private static void testDisplayGroups(EClientSocket client) throws InterruptedException {
		
		//! [querydisplaygroups]
		client.queryDisplayGroups(9001);
		//! [querydisplaygroups]
		
		Thread.sleep(500);
		
		//! [subscribetogroupevents]
		client.subscribeToGroupEvents(9002, 1);
		//! [subscribetogroupevents]
		
		Thread.sleep(500);
		
		//! [updatedisplaygroup]
		client.updateDisplayGroup(9002, "8314@SMART");
		//! [updatedisplaygroup]
		
		Thread.sleep(500);
		
		//! [subscribefromgroupevents]
		client.unsubscribeFromGroupEvents(9002);
		//! [subscribefromgroupevents]
		
	}
	
	private static void marketDataType(EClientSocket client) {
		
		//! [reqmarketdatatype]
        /*** Switch to live (1) frozen (2) delayed (3) or delayed frozen (4)***/
        client.reqMarketDataType(2);
        //! [reqmarketdatatype]
		
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
			w.write("1");
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
