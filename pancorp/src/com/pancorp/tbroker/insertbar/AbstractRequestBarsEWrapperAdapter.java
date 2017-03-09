package com.pancorp.tbroker.insertbar;

import java.io.FileOutputStream;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.EClientSocket;
//import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.SoftDollarTier;
import com.ib.client.TickType;
//import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

//! [ewrapperimpl]
public abstract class AbstractRequestBarsEWrapperAdapter implements EWrapper {
	private static Logger lg = LogManager.getLogger(RequestBarsEWrapperImpl.class);
	EClientSocket clientSocket;
	EReaderSignal readerSignal;

	//! [socket_init]
	public EClientSocket getClient() {
		return clientSocket;
	}
	
	public EReaderSignal getSignal() {
		return readerSignal;
	}

	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		lg.trace("Tick Price. Ticker Id:"+tickerId+", Field: "+field+", Price: "+price+", CanAutoExecute: "+canAutoExecute);
		
	}
	//! [tickprice]
	
	//! [ticksize]
	@Override
	public void tickSize(int tickerId, int field, int size) {
		System.out.println("Tick Size. Ticker Id:" + tickerId + ", Field: " + field + ", Size: " + size);
	}
	//! [ticksize]
	
	//! [tickoptioncomputation]
	@Override
	public void tickOptionComputation(int tickerId, int field,
			double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta,
			double undPrice) {
		System.out.println("TickOptionComputation. TickerId: "+tickerId+", field: "+field+", ImpliedVolatility: "+impliedVol+", Delta: "+delta
                +", OptionPrice: "+optPrice+", pvDividend: "+pvDividend+", Gamma: "+gamma+", Vega: "+vega+", Theta: "+theta+", UnderlyingPrice: "+undPrice);
	}
	//! [tickoptioncomputation]
	
	//! [tickgeneric]
	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		System.out.println("Tick Generic. Ticker Id:" + tickerId + ", Field: " + TickType.getField(tickType) + ", Value: " + value);
	}
	//! [tickgeneric]
	
	//! [tickstring]
	@Override
	public void tickString(int tickerId, int tickType, String value) {
		System.out.println("Tick string. Ticker Id:" + tickerId + ", Type: " + tickType + ", Value: " + value);
	}
	//! [tickstring]
	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureLastTradeDate, double dividendImpact,
			double dividendsToLastTradeDate) {
		System.out.println("TickEFP. "+tickerId+", Type: "+tickType+", BasisPoints: "+basisPoints+", FormattedBasisPoints: "+
			formattedBasisPoints+", ImpliedFuture: "+impliedFuture+", HoldDays: "+holdDays+", FutureLastTradeDate: "+futureLastTradeDate+
			", DividendImpact: "+dividendImpact+", DividendsToLastTradeDate: "+dividendsToLastTradeDate);
	}
	//! [orderstatus]
	@Override
	public void orderStatus(int orderId, String status, double filled,
			double remaining, double avgFillPrice, int permId, int parentId,
			double lastFillPrice, int clientId, String whyHeld) {
		lg.info("OrderStatus: Id: "+orderId+", Status: "+status+", Filled"+filled+", Remaining: "+remaining
                +", AvgFillPrice: "+avgFillPrice+", PermId: "+permId+", ParentId: "+parentId+", LastFillPrice: "+lastFillPrice+
                ", ClientId: "+clientId+", WhyHeld: "+whyHeld);
		
		//int sqlStatus = dfac.updateOrder(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		
		//TODO
		switch(status){
		case "PreSubmitted":
			break;
		case "Filled":
			//TODO 
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
		
		//int sqlStatus = dfac.insertOrder(Globals.paperClientId, orderId, contract, order, orderState);
		//lg.info("openOrder: inserted: " + sqlStatus);
		
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
	
	//! [updateaccountvalue]
	@Override
	public void updateAccountValue(String key, String value, String currency,
			String accountName) {
		System.out.println("UpdateAccountValue. Key: " + key + ", Value: " + value + ", Currency: " + currency + ", AccountName: " + accountName);
	}
	//! [updateaccountvalue]
	
	//! [updateportfolio]
	@Override
	public void updatePortfolio(Contract contract, double position,
			double marketPrice, double marketValue, double averageCost,
			double unrealizedPNL, double realizedPNL, String accountName) {
		System.out.println("UpdatePortfolio. "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()
                +": Position: "+position+", MarketPrice: "+marketPrice+", MarketValue: "+marketValue+", AverageCost: "+averageCost
                +", UnrealisedPNL: "+unrealizedPNL+", RealisedPNL: "+realizedPNL+", AccountName: "+accountName);
	}
	//! [updateportfolio]
	
	//! [updateaccounttime]
	@Override
	public void updateAccountTime(String timeStamp) {
		System.out.println("UpdateAccountTime. Time: " + timeStamp+"\n");
	}
	//! [updateaccounttime]
	
	//! [accountdownloadend]
	@Override
	public void accountDownloadEnd(String accountName) {
		System.out.println("Account download finished: "+accountName+"\n");
	}
	//! [accountdownloadend]
	
	//! [nextvalidid]
	@Override
	public void nextValidId(int orderId) {
		lg.info("Next Valid Id: ["+orderId+"]");
		//currentOrderId = orderId;
		//this.placeOrderBracket.setNextOrderId(orderId);
	}
	//! [nextvalidid]
	
	//! [contractdetails]
	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		System.out.println("ContractDetails. ReqId: ["+reqId+"] - ["+contractDetails.contract().symbol()+"], ["+contractDetails.contract().secType()+"], ConId: ["+contractDetails.contract().conid()+"] @ ["+contractDetails.contract().exchange()+"]");
	}
	//! [contractdetails]
	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		System.out.println("bondContractDetails");
	}
	//! [contractdetailsend]
	@Override
	public void contractDetailsEnd(int reqId) {
		System.out.println("ContractDetailsEnd. "+reqId+"\n");
	}
	//! [contractdetailsend]
	
	//! [execdetails]
	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		lg.info("ExecDetails: "+reqId+" - ["+contract.symbol()+"], ["+contract.secType()+"], ["+contract.currency()+"], ["+execution.execId()+"], ["+execution.orderId()+"], ["+execution.shares()+"]");
	}
	//! [execdetails]
	
	//! [execdetailsend]
	@Override
	public void execDetailsEnd(int reqId) {
		System.out.println("ExecDetailsEnd. "+reqId+"\n");
	}
	//! [execdetailsend]
	
	//! [updatemktdepth]
	@Override
	public void updateMktDepth(int tickerId, int position, int operation,
			int side, double price, int size) {
		System.out.println("UpdateMarketDepth. "+tickerId+" - Position: "+position+", Operation: "+operation+", Side: "+side+", Price: "+price+", Size: "+size+"");
	}
	//! [updatemktdepth]
	@Override
	public void updateMktDepthL2(int tickerId, int position,
			String marketMaker, int operation, int side, double price, int size) {
		System.out.println("updateMktDepthL2");
	}
	//! [updatenewsbulletin]
	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message,
			String origExchange) {
		System.out.println("News Bulletins. "+msgId+" - Type: "+msgType+", Message: "+message+", Exchange of Origin: "+origExchange+"\n");
	}
	//! [updatenewsbulletin]
	
	//! [managedaccounts]
	@Override
	public void managedAccounts(String accountsList) {
		System.out.println("Account list: " +accountsList);
	}
	//! [managedaccounts]

	//! [receivefa]
	@Override
	public void receiveFA(int faDataType, String xml) {
		System.out.println("Receing FA: "+faDataType+" - "+xml);
	}
	//! [receivefa]
	
	//! [historicaldata]
	@Override
	public void historicalData(int reqId, String date, double open,
			double high, double low, double close, int volume, int count,
			double WAP, boolean hasGaps) {
		System.out.println("HistoricalData. "+reqId+" - Date: "+date+", Open: "+open+", High: "+high+", Low: "+low+", Close: "+close+", Volume: "+volume+", Count: "+count+", WAP: "+WAP+", HasGaps: "+hasGaps);
	}
	//! [historicaldata]
	
	//! [scannerparameters]
	@Override
	public void scannerParameters(String xml) {
		//System.out.println("ScannerParameters. "+xml+"\n");
		//write into xml file
		String file = Globals.BASEDIR + Globals.DATADIR + "scanner_params.xml";
		byte[] bytes = xml.getBytes();
		try (FileOutputStream out = new FileOutputStream(file)){
			for(int i=0;i<bytes.length;i++){
				out.write(bytes[i]);
			}
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
	}
	//! [scannerparameters]
	
	//! [scannerdata]
	@Override
	public void scannerData(int reqId, int rank,
			ContractDetails contractDetails, String distance, String benchmark,
			String projection, String legsStr) {
		System.out.println("ScannerData. "+reqId+" - Rank: "+rank+", Symbol: "+contractDetails.contract().symbol()+", SecType: "+contractDetails.contract().secType()+", Currency: "+contractDetails.contract().currency()
                +", Distance: "+distance+", Benchmark: "+benchmark+", Projection: "+projection+", Legs String: "+legsStr);
	}
	//! [scannerdata]
	
	//! [scannerdataend]
	@Override
	public void scannerDataEnd(int reqId) {
		System.out.println("ScannerDataEnd. "+reqId);
	}
	//! [scannerdataend]
	
	//! [realtimebar]
	/*@Override
	public void realtimeBar(int reqId, long time, double open, double high,
			double low, double close, long volume, double wap, int count) {
		lg.info("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
		//Candle c = new Candle(time,high,low,open,close,wap,volume, count);
		//TODO  correlate Contract info with Candle
		int status = DataFactory.
	}*/
	
	//! [realtimebar]
	@Override
	public void currentTime(long time) {
		System.out.println("currentTime");
	}
	//! [fundamentaldata]
	@Override
	public void fundamentalData(int reqId, String data) {
		System.out.println("FundamentalData. ReqId: ["+reqId+"] - Data: ["+data+"]");
	}
	//! [fundamentaldata]
	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		System.out.println("deltaNeutralValidation");
	}
	//! [ticksnapshotend]
	@Override
	public void tickSnapshotEnd(int reqId) {
		System.out.println("TickSnapshotEnd: "+reqId);
	}
	//! [ticksnapshotend]
	
	//! [marketdatatype]
	@Override
	public void marketDataType(int reqId, int marketDataType) {
		System.out.println("MarketDataType. ["+reqId+"], Type: ["+marketDataType+"]\n");
	}
	//! [marketdatatype]
	
	//! [commissionreport]
	@Override
	public void commissionReport(CommissionReport commissionReport) {
		System.out.println("CommissionReport. ["+commissionReport.m_execId+"] - ["+commissionReport.m_commission+"] ["+commissionReport.m_currency+"] RPNL ["+commissionReport.m_realizedPNL+"]");
	}
	//! [commissionreport]
	
	//! [position]
	@Override
	public void position(String account, Contract contract, double pos,
			double avgCost) {
		System.out.println("Position. "+account+" - Symbol: "+contract.symbol()+", SecType: "+contract.secType()+", Currency: "+contract.currency()+", Position: "+pos+", Avg cost: "+avgCost);
	}
	//! [position]
	
	//! [positionend]
	@Override
	public void positionEnd() {
		System.out.println("PositionEnd \n");
	}
	//! [positionend]
	
	//! [accountsummary]
	@Override
	public void accountSummary(int reqId, String account, String tag,
			String value, String currency) {
		System.out.println("Acct Summary. ReqId: " + reqId + ", Acct: " + account + ", Tag: " + tag + ", Value: " + value + ", Currency: " + currency);
	}
	//! [accountsummary]
	
	//! [accountsummaryend]
	@Override
	public void accountSummaryEnd(int reqId) {
		System.out.println("AccountSummaryEnd. Req Id: "+reqId+"\n");
	}
	//! [accountsummaryend]
	@Override
	public void verifyMessageAPI(String apiData) {
		System.out.println("verifyMessageAPI");
	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		System.out.println("verifyCompleted");
	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		System.out.println("verifyAndAuthMessageAPI");
	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		System.out.println("verifyAndAuthCompleted");
	}
	//! [displaygrouplist]
	@Override
	public void displayGroupList(int reqId, String groups) {
		System.out.println("Display Group List. ReqId: "+reqId+", Groups: "+groups+"\n");
	}
	//! [displaygrouplist]
	
	//! [displaygroupupdated]
	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		System.out.println("Display Group Updated. ReqId: "+reqId+", Contract info: "+contractInfo+"\n");
	}
	//! [displaygroupupdated]
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
		lg.error("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");
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
	//! [connectack]
	
	//! [positionmulti]
	@Override
	public void positionMulti(int reqId, String account, String modelCode,
			Contract contract, double pos, double avgCost) {
		System.out.println("Position Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Symbol: " + contract.symbol() + ", SecType: " + contract.secType() + ", Currency: " + contract.currency() + ", Position: " + pos + ", Avg cost: " + avgCost + "\n");
	}
	//! [positionmulti]
	
	//! [positionmultiend]
	@Override
	public void positionMultiEnd(int reqId) {
		System.out.println("Position Multi End. Request: " + reqId + "\n");
	}
	//! [positionmultiend]
	
	//! [accountupdatemulti]
	@Override
	public void accountUpdateMulti(int reqId, String account, String modelCode,
			String key, String value, String currency) {
		System.out.println("Account Update Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Key: " + key + ", Value: " + value + ", Currency: " + currency + "\n");
	}
	//! [accountupdatemulti]
	
	//! [accountupdatemultiend]
	@Override
	public void accountUpdateMultiEnd(int reqId) {
		System.out.println("Account Update Multi End. Request: " + reqId + "\n");
	}
	//! [accountupdatemultiend]
	
	//! [securityDefinitionOptionParameter]
	@Override
	public void securityDefinitionOptionalParameter(int reqId, String exchange,
			int underlyingConId, String tradingClass, String multiplier,
			Set<String> expirations, Set<Double> strikes) {
		System.out.println("Security Definition Optional Parameter. Request: "+reqId+", Trading Class: "+tradingClass+", Multiplier: "+multiplier+" \n");
	}
	//! [securityDefinitionOptionParameter]
	@Override
	public void securityDefinitionOptionalParameterEnd(int reqId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
		for (SoftDollarTier tier : tiers) {
			System.out.print("tier: " + tier + ", ");
		}
		
		System.out.println();
	}

}
