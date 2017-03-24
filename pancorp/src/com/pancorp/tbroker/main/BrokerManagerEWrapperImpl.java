package com.pancorp.tbroker.main;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Order;
import com.ib.client.OrderState;
//import com.ib.controller.Bar;
import com.ib.client.TickType;
import com.pancorp.tbroker.adapter.AbstractMarketScannerEWrapperAdapter;
//import com.pancorp.tbroker.data.MarketScanDataFactory;
//import com.pancorp.tbroker.data.MarketScanDataFactory.ScannerLine;
import com.pancorp.tbroker.day.DataFactory;
import com.pancorp.tbroker.model.DataTick;
import com.pancorp.tbroker.util.Constants;
//import com.pancorp.tbroker.day.*;
//import com.pancorp.tbroker.model.*;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

//! [ewrapperimpl]
public class BrokerManagerEWrapperImpl extends AbstractMarketScannerEWrapperAdapter implements EWrapper {
	private static Logger lg = LogManager.getLogger(BrokerManagerEWrapperImpl.class);
	
	//! [ewrapperimpl]
	
	//! [socket_declare]
	private EReaderSignal readerSignal;
	private EClientSocket clientSocket;
	protected int currentOrderId = -1;
	private DataFactory dfac;
	private BrokerManager manager;
	//private LinkedList<ScannerLine> queue;
	//private LinkedList<Bar> barQueue;
	//private LinkedList<Contract> conList = null;
	private HashMap<Integer,Contract> conList = null;
	
	private DataTick tick = null;
	//! [socket_declare]
	
	//! [socket_init]
	public BrokerManagerEWrapperImpl(BrokerManager mgr) throws Exception {
		readerSignal = new EJavaSignal();
		clientSocket = new EClientSocket(this, readerSignal);
		try {
		dfac = new DataFactory();	
		//dFac.start();		
		manager = mgr;
		//queue = new LinkedList<ScannerLine>();
		
		//barQueue = new LinkedList<>();
		}
		catch(Exception e){
			Utils.logError(lg, e);
			throw e;
		}	
		
		lg.trace("Wrapper created");
	}
	//! [socket_init]
	public EClientSocket getClient() {
		return clientSocket;
	}
	
	public EReaderSignal getSignal() {
		return readerSignal;
	}
	
	public DataFactory getDataFactory() {
		return this.dfac;
	}
	/*
	public void setDataFactory(MarketScanDataFactory f) {
		this.dFac = f;
	}*/
	
	/**
	 * @return the symbols
	 */
	//public LinkedList<Contract> getConList() {
	public HashMap<Integer,Contract> getConList() {
		return conList;
	}
	/**
	 * @param symbols the symbols to set
	 */
	public void setConList(HashMap<Integer,Contract> li) {
		this.conList = li;
	}
	public int getCurrentOrderId() {
		return currentOrderId;
	}

	
	
	//! [realtimebar]
	@Override
	public void realtimeBar(int reqId, long time, double open, double high,
				double low, double close, long volume, double wap, int count) {
		if(lg.isTraceEnabled())
			lg.trace("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);

		dfac.recordTick(reqId, time, open, high, low, close, volume, wap, count);
	}
	
	//! [orderstatus]
	@Override
	public void orderStatus(int orderId, String status, double filled,
			double remaining, double avgFillPrice, int permId, int parentId,
			double lastFillPrice, int clientId, String whyHeld) {
		lg.info("OrderStatus: Id: "+orderId+", Status: "+status+", Filled"+filled+", Remaining: "+remaining
                +", AvgFillPrice: "+avgFillPrice+", PermId: "+permId+", ParentId: "+parentId+", LastFillPrice: "+lastFillPrice+
                ", ClientId: "+clientId+", WhyHeld: "+whyHeld);
		
		int sqlStatus = dfac.updateOrder(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		
		//TODO
		switch(status){
		case "PreSubmitted":
			break;
		case "Filled":
			//TODO call back to PlaceOrderThread or Manager to reset operations mode and other properties
			manager.setOrderFilled(orderId, parentId, clientId);
			break;
		case "Cancelled":
			//TODO this one?
			break;
		case "ApiPending":
			break;
		case "ApiCancelled":
			//TODO this one?
			break;
		case "PendingCancel":
			break;
		case "Submitted":
			break;
		case "Inactive":
			break;
		case "PendingSubmit":
			break;
		case "Unknown":
			break;
			default:
		}
	}
	//! [orderstatus]
	
	/**
	 * Returns opened order, commission and margin information 
	 * Use order.whatIf set to true to use order as a query 
	 * and get the information here
	 * 
	 * @param orderId
	 * @param contract
	 * @param order
	 * @param orderState
	 */
	//! [openorder]
	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		lg.info("OpenOrder: ID: "+orderId+", "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()+": "+
			order.action()+", "+order.orderType()+" quantity="+order.totalQuantity()+", status="+orderState.status()+
		"\ncomission=" + orderState.commission()+
		"\ncomissionCurrency=" + orderState.commissionCurrency()+
		"\nequityWithLoan=" + orderState.equityWithLoan()+
		"\ninitMargin=" + orderState.initMargin()+
		"\nmaintMargin="+orderState.maintMargin()+
		"\nwarning="+orderState.warningText()+
		"\nactiveStartDate: " + order.activeStartTime()
		);
		
		int sqlStatus = dfac.insertOrder(Globals.paperClientId, orderId, contract, order, orderState);
		lg.info("openOrder: inserted: " + sqlStatus);
		
		/*
		order.account();
		order.action();
		order.activeStartTime();
		order.activeStopTime();
		order.adjustableTrailingUnit();
		order.adjustedOrderType();
		order.adjustedStopLimitPrice();
		order.adjustedStopPrice();
		order.adjustedTrailingAmount();
		order.algoId();
		order.algoParams();
		order.getAlgoStrategy();
		order.allOrNone();
		order.auctionStrategy();
		order.auxPrice();
		order.basisPoints();
		order.basisPointsType();
		order.blockOrder();
		order.clearingAccount();
		order.clearingIntent();
		order.clientId();
		order.conditions();
		order.conditionsCancelOrder();
		order.conditionsIgnoreRth();
		order.continuousUpdate();
		order.delta();
		order.deltaNeutralAuxPrice();
		order.deltaNeutralClearingAccount();
		order.deltaNeutralClearingIntent();
		order.deltaNeutralConId();
		order.deltaNeutralDesignatedLocation();
		order.deltaNeutralOpenClose();
		order.deltaNeutralOrderType();
		order.deltaNeutralSettlingFirm();
		order.deltaNeutralShortSale();
		order.deltaNeutralShortSaleSlot();
		order.designatedLocation();
		order.discretionaryAmt();
		order.displaySize();
		order.eTradeOnly();
		order.exemptCode();
		order.extOperator();
		order.faGroup();
		order.faMethod();
		order.faPercentage();
		order.faProfile();
		order.firmQuoteOnly();
		order.getAction(); //returned above
		order.getAlgoStrategy();
		order.getDeltaNeutralOrderType();
		order.getFaMethod();
		order.getHedgeType();
		order.getOcaType();
		order.getOrderType();
		order.getReferencePriceType();
		order.getRule80A();
		order.getTif();
		order.getTriggerMethod();
		order.getVolatilityType();
		order.hedgeParam();
		order.hedgeType();
		order.goodAfterTime();
		order.goodTillDate();
		order.hidden();
		order.isPeggedChangeAmountDecrease();
		order.lmtPrice();
		order.lmtPriceOffset();
		order.minQty();
		order.modelCode();
		order.nbboPriceCap();
		order.notHeld();
		order.ocaGroup();
		order.ocaType();
		order.openClose();
		order.optOutSmartRouting();
		order.orderComboLegs();
		order.orderId();  //returned above
		order.orderMiscOptions();
		order.orderRef();
		order.orderType();
		order.origin();
		order.outsideRth();
		order.overridePercentageConstraints();
		order.parentId(); //retured
		order.peggedChangeAmount();
		order.percentOffset();
		order.permId(); //returned
		order.randomizePrice();
		order.randomizeSize();
		order.referenceChangeAmount();
		order.referenceContractId();
		order.referenceExchangeId();
		order.referencePriceType();
		order.rule80A();
		order.scaleAutoReset();
		order.scaleInitFillQty();
		order.scaleInitLevelSize();
		order.scaleInitPosition();
		order.scalePriceAdjustInterval();
		order.scalePriceAdjustValue();
		order.scalePriceIncrement();
		order.scaleProfitOffset();
		order.scaleRandomPercent();
		order.scaleSubsLevelSize();
		order.scaleTable();
		order.settlingFirm();
		order.shortSaleSlot();
		order.smartComboRoutingParams();
		order.softDollarTier();
		order.solicited();
		order.startingPrice();
		order.stockRangeLower();
		order.stockRangeUpper();
		order.stockRefPrice();
		order.sweepToFill();
		order.tif();
		order.totalQuantity();
		order.trailingPercent();
		order.trailStopPrice();
		order.transmit();
		order.triggerMethod();
		order.triggerPrice();
		order.volatility();
		order.volatilityType();
		order.whatIf();
		*/
		
	}
	//! [openorder]
	
	//! [openorderend]
	@Override
	public void openOrderEnd() {
		lg.info("OpenOrderEnd");
	}
	//! [openorderend]


	@Override
	public void error(Exception e) {
		lg.error("Exception: "+e.getMessage());
	}

	@Override
	public void error(String str) {
		lg.error("Error Str: " + str);
	}
	//! [error]
	@Override
	public void error(int id, int errorCode, String errorMsg) {
		if(id==-1){
			//not actually an error but a message
			lg.info("Code: " + errorCode + ", Msg: " + errorMsg);
			switch(errorCode){
			case 507:
				this.clientSocket.eDisconnect();
				System.exit(507);
				break;
				default:
			}
		}
		else{
		lg.error("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");
		switch(errorCode){
		case 200:
			if(!this.conList.isEmpty()){
				this.manager.nextSnapshot(this.clientSocket,this.conList);
			}
			break;
			default:
				
		}
		}
	}
	//! [error]
	@Override
	public void connectionClosed() {
		lg.info("Connection closed");
	}

	//! [connectack]
	@Override
	public void connectAck() {
		if (clientSocket.isAsyncEConnect()) {
			lg.info("Acknowledging connection");
			clientSocket.startAPI();
		}
	}
	
	
	@Override
    public void tickSize(int tickerId, int field, int size) {
		lg.info("Tick Size. Ticker Id:" + tickerId + ", Field: " + field + ", Size: " + size);
		
    }
	
	@Override
    public void tickString(int tickerId, int tickType, String value) {
		lg.info("Tick string. Ticker Id:" + tickerId +", Type: " + tickType + ", Value: " + value);
    }
	
	@Override
    public void tickGeneric(int tickerId, int tickType, double value) {
		lg.info("Tick Generic. Ticker Id:" + tickerId + ", Field: " + TickType.getField(tickType) + ", Value: " + value);
		
		if(TickType.getField(tickType)=="halted"){
			if(this.tick==null)
				tick = new DataTick(tickerId);
			
			tick.setHalted(value);
		}
    }
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		lg.info("Tick Price. Ticker Id:"+tickerId+", Field: "+field+", Price: "+price+", CanAutoExecute: "+ canAutoExecute);
		//if(price > Globals.MAX_STK_PRICE || price < Globals.MIN_STK_PRICE)
		//	this.cMap.remove(tickerId);
		
		if(field==9){
			if(this.tick==null)
				tick = new DataTick(tickerId-Constants.REQ_ID_SNAPSHOT);
			
			tick.setClose(price);
		}
	}
	
	 public void tickSnapshotEnd(int tickerId)
     {
         lg.info("TickSnapshotEnd: "+tickerId + ", updating..");
         dfac.updateSnapshot(this.tick);
         this.tick = null;
         
         if(this.conList.isEmpty())
        	 this.manager.startTheDay();
         else
        	 this.manager.nextSnapshot(this.clientSocket,this.conList);
     }

}
