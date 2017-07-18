package com.pancorp.tbroker.main;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TickType;
import com.pancorp.tbroker.adapter.AbstractMarketScannerEWrapperAdapter;
import com.pancorp.tbroker.data.DataFactory;
import com.pancorp.tbroker.model.DataTick;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;
import com.pancorp.tbroker.order.PlaceOrderBracketT;

//! [ewrapperimpl]
public class BrokerManagerEWrapperImpl extends AbstractMarketScannerEWrapperAdapter implements EWrapper {
	private static Logger lg = LogManager.getLogger(BrokerManagerEWrapperImpl.class);
	
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
	//public int getCurrentOrderId() {
	//	return currentOrderId;
	//}

	@Override
	public void position(String account, Contract contract, double pos, double avgCost) {
		 lg.info("Position. "+account+" - Symbol: "+contract.symbol()+", SecType: "+contract.secType()+", Currency: "+contract.currency()+", Position: "+pos+", Avg cost: "+avgCost);		
	
		 //TODO code to close each position
		 if(this.manager.toCloseAllPositions){
			 //create order to close this position
		 }
	}

	@Override
	public void positionEnd() {
		lg.info("position end");
		this.clientSocket.cancelPositions(); //request to stop position data coming this way
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

		//update local database
		int sqlStatus = dfac.updateOrder(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		lg.info("orderStatus: order updated: " + sqlStatus + " to status " + status);
		//TODO
		switch(status){
		case "PreSubmitted":
			break;
		case "Filled":
			//TODO call back to PlaceOrderThread or Manager to reset operations mode and other properties
			manager.clearOrder(orderId); //, parentId, clientId);  //update BrokerManager list
			break;
		case "Cancelled":
			manager.clearOrder(orderId); //, parentId, clientId);  //update BrokerManager list
			break;
		case "ApiPending":
			break;
		case "ApiCancelled":
			manager.clearOrder(orderId);
			//TODO this one?
			break;
		case "PendingCancel":
			break;
		case "Submitted":
			manager.orderStatusCallback(orderId,status, permId, parentId,clientId);
			break;
		case "Inactive":
			manager.clearOrder(orderId);
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
		"\nactiveStartTime: " + order.activeStartTime()
		);
		
		//update in manager
		this.manager.openOrderCallback(orderId, contract, order, orderState);
		// update order in local database
		int sqlStatus = dfac.updateOrder(orderId, contract, order, orderState);		
		lg.info("openOrder: updated: " + sqlStatus);
		
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
		List<Order> ol = null;
		if(id==-1){
			//not actually an error but a message
			lg.info("Code: " + errorCode + ", Msg: " + errorMsg);
			switch(errorCode){
			case 504: //Not Connected (so order has ot been submitted, remove from the list and reset)
				ol = this.manager.getOpenOrderList();
				if(ol!=null && !ol.isEmpty()){
				for(Order o : ol){
					if(id==o.orderId()){
						//update order status in database
						this.dfac.updateOrder(id, "Error", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
						//remove order from the list
						manager.clearOrder(id);//.getOpenOrderList().remove(o);
					}
				}
				lg.info("Due to code 504 orders not submitted and have been deleted from the list");
				}
				break;
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
		case 202:  //Order Cancelled
			dfac.updateOrder(id, "Cancelled", 0, 0, 0, 0, 0, 0, 0, null);
			manager.orderCancelledCallback(id);
			break;
		case 110:  //The price does not conform to the minimum price variation for this contract.
			ol = this.manager.getOpenOrderList();
			for(Order o : ol){
				if(id==o.orderId()){
					//update order status in database
					this.dfac.updateOrder(id, "Error", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
					//remove order from the list
					manager.getOpenOrderList().remove(o);
					
					//reset mode to opening to allow open new position
					if(manager.getOpenOrderList().isEmpty())
						manager.setOrderPlaced(false); //.resetBrokerMode();
					break;
				}
			}
			lg.info("Due to code 110 orders not submitted and have been deleted from the list");
			break;
		case 135:  //Can't find order with id = <>  - (parent order does not exist)
			ol = this.manager.getOpenOrderList();
			for(Order o : ol){
				if(id==o.orderId()){
					//update order status in database
					this.dfac.updateOrder(id, "Error", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
					//remove order from the list
					manager.getOpenOrderList().remove(o);
					
					//reset mode to opening to allow open new position
					if(manager.getOpenOrderList().isEmpty())
						manager.setOrderPlaced(false); //.resetBrokerMode();
					break;
				}
			}
			lg.info("Due to code 135 orders not submitted and have been deleted from the list");
			break;
		case 399://Order Message: SELL 7K EUR.USD Forex Warning: Your order size is below the EUR 20000 IdealPro minimum and will be routed as an odd lot order.
			break;
		case 403:  // Code: 403, Msg: Invalid Stop Price
			ol = this.manager.getOpenOrderList();
			int parentID = 0;
			for(Order o : ol){
				if(id==o.orderId()){
					parentID = o.parentId();
					lg.info("removing order with ID " + id);
					//update order status in database
					this.dfac.updateOrder(id, "Error", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
					//remove order from the list
					manager.clearOrder(id);//.getOpenOrderList().remove(o);
					break;
					//reset mode to opening to allow open new position
					//if(manager.getOpenOrderList().isEmpty())
					//	manager.resetBrokerMode();
					//break;
				}
			}
			for(Order o : ol){
				if(o.parentId()==parentID){
					lg.info("removing child of order " +parentID+ ", order with ID " + id);
					int cho = o.orderId();
					//TODO submit cancellation to IB
					//update order status in database
					this.dfac.updateOrder(cho, "Cancelled", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
					manager.clearOrder(cho);
				}
			}
			break;
		case 504: // Not connected (so order has not been submitted, need  to reset)
			ol = this.manager.getOpenOrderList();
			for(Order o : ol){
				if(id==o.orderId()){
					//update order status in database
					this.dfac.updateOrder(id, "Error", 0, 0, 0, 0, 0, 0, Globals.paperClientId, null);
					//remove order from the list
					manager.clearOrder(id);//.getOpenOrderList().remove(o);
					
					//reset mode to opening to allow open new position
					//if(manager.getOpenOrderList().isEmpty())
					//	manager.resetBrokerMode();
					//break;
				}
			}
			lg.info("Due to code 504 orders not submitted and have been deleted from the list");
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
		
		//for the snapshot request; how to determine that?
		if(this.manager.snapshotInProgress){
		if(field==9){
			if(this.tick==null)
				tick = new DataTick(tickerId-Constants.REQ_ID_SNAPSHOT);
			
			tick.setClose(price);
		}
		}
	}
	
	 public void tickSnapshotEnd(int tickerId)
     {
         lg.info("TickSnapshotEnd: "+tickerId + ", updating..");
         dfac.updateSnapshot(this.tick);
         this.tick = null;
         
         this.manager.snapshotInProgress = false;
         
         if(this.conList.isEmpty())
        	 this.manager.startTheDay();
         else
        	 this.manager.nextSnapshot(this.clientSocket,this.conList);
     }
	 
	 @Override
	 public void historicalData(int reqId, String date, double open,
	            double high, double low, double close, int volume, int count,
	            double wap, boolean hasGaps) {
	        lg.info("HistoricalData. "+reqId+" - Date: "+date+", Open: "+open+", High: "+high+", Low: "+low+", Close: "+close+", Volume: "+volume+", Count: "+count+", wap: "+wap+", HasGaps: "+hasGaps);
	
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	        long time = 0;
	        
	        try {
	        	if(date!=null && date.indexOf("finished")>=0){
	        		lg.info("Finished collecting historical data");
	        		historicalDataEnd(reqId);//, startDateStr, endDateStr);;
	        		lg.info("After historicalDataEnd; returning..");

	        		return;
	        	}
	        		
	        	java.util.Date parsedDate = formatter.parse(date);
	        	time = parsedDate.getTime();
	        	 // dfac.recordTick(reqId, time, open, high, low, close, volume, wap, count);
	        	manager.tMap.get(reqId).addHistorical(reqId, time, open, high, low, close, volume, wap, count);
	        }
	        catch(Exception e){
	        	lg.error("historicalData: caught exception getting tick: " + e.getMessage());
	        }        	        
	 }

	 //   @Override
	  public void historicalDataEnd(int reqId) { //, String startDateStr, String endDateStr) {
	        lg.info("HistoricalDataEnd. "+reqId); //+" - Start Date: "+startDateStr+", End Date: "+endDateStr);
	        
	      //lg.info("Finished collecting historical data, cancelling historical data request");
			//client.cancelHistoricalData(reqId);
	        try {
	        	lg.info("Finished collecting historical data, requesting real time bars");
	        	lg.trace("Client: " + this.clientSocket + ", reqId: " + reqId + ", conList: " + conList);
	        	this.manager.realTimeBars(this.clientSocket, reqId, conList.get(reqId));
	        	lg.trace("real time bars requested");
	        }
	        catch(InterruptedException ie){
	        	Utils.logError(lg, ie);
	        }
	  }

}
