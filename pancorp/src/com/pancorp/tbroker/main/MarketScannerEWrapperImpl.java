package com.pancorp.tbroker.main;

import java.io.FileOutputStream;
import java.util.ArrayList;
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
import com.ib.client.TickType;
//import com.ib.controller.Bar;
import com.pancorp.tbroker.adapter.AbstractMarketScannerEWrapperAdapter;
import com.pancorp.tbroker.data.MarketScanDataFactory;
import com.pancorp.tbroker.data.MarketScanDataFactory.ScannerLine;
import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

//! [ewrapperimpl]
public class MarketScannerEWrapperImpl extends AbstractMarketScannerEWrapperAdapter implements EWrapper {
	private static Logger lg = LogManager.getLogger(MarketScannerEWrapperImpl.class);
	
	//! [ewrapperimpl]
	
	//! [socket_declare]
	private EReaderSignal readerSignal;
	private EClientSocket clientSocket;
	protected int currentOrderId = -1;
	private MarketScanDataFactory dFac;
	private LinkedList<ScannerLine> queue;
	private LinkedList<Bar> barQueue;
	private HashMap<Integer,Contract> cMap = null;
	//! [socket_declare]
	
	//! [socket_init]
	public MarketScannerEWrapperImpl() throws Exception {
		readerSignal = new EJavaSignal();
		clientSocket = new EClientSocket(this, readerSignal);
		try {
		dFac = new MarketScanDataFactory(this);
		dFac.start();
		queue = new LinkedList<ScannerLine>();
		
		barQueue = new LinkedList<>();
		}
		catch(Exception e){
			Utils.logError(lg, e);
			throw e;
		}	
	}
	
	public void clearBarCache(){
		this.barQueue.clear();
	}
	//! [socket_init]
	public EClientSocket getClient() {
		return clientSocket;
	}
	
	public EReaderSignal getSignal() {
		return readerSignal;
	}
	
	public MarketScanDataFactory getDataFactory() {
		return this.dFac;
	}
	public void setDataFactory(MarketScanDataFactory f) {
		this.dFac = f;
	}
	
	/**
	 * @return the symbols
	 */
	public HashMap<Integer,Contract> getCMap() {
		return cMap;
	}
	/**
	 * @param symbols the symbols to set
	 */
	public void setCMap(HashMap<Integer,Contract> m) {
		this.cMap = m;
	}
	public int getCurrentOrderId() {
		return currentOrderId;
	}

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
		lg.info("ScannerData. "+reqId+
				" - Rank: "+rank+
				", Symbol: "+contractDetails.contract().symbol()+
				", SecType: "+contractDetails.contract().secType()+
				", Currency: "+contractDetails.contract().currency()
				//", category: " + contractDetails.category() +
				//",industry: " + contractDetails.industry() + 
				//", liquidHours:" + contractDetails.liquidHours() +
				//", longName:" + contractDetails.longName() + 
				//", marketName: " + contractDetails.marketName() + 
				//", minTick: " + contractDetails.minTick()+
				//", ratings:" + contractDetails.ratings() + 
				//", subcategory: " + contractDetails.subcategory() + 
				//", tradingHours :" + contractDetails.tradingHours()+
				//", timeSoneId:" + contractDetails.timeZoneId() + 
				//", validExchanges:" + contractDetails.validExchanges()+
				//", secIdList - ArrayList<TagValue>: " + contractDetails.secIdList() + 
				//", primaryExch: " + contractDetails.contract().primaryExch() + 
				//", tradingClass:" + contractDetails.contract().tradingClass() 
				
               // +", Distance: "+distance+", Benchmark: "+benchmark+
               // ", Projection: "+projection+", Legs String: "+legsStr
                );
		//dFac.addScanResultLine( reqId, rank,contractDetails, distance, benchmark,projection, legsStr);
		
		queue.addLast(dFac.new ScannerLine(reqId, rank,
				contractDetails.contract().symbol(),
				contractDetails.contract().secType().toString(),
				contractDetails.contract().currency(),
				contractDetails.contract()		
				));
				//distance, benchmark,projection, legsStr));
	}
	//! [scannerdata]
	
	//! [scannerdataend]
	@Override
	public void scannerDataEnd(int reqId) {
		lg.info("ScannerDataEnd. "+reqId);
		dFac.loadQueue(this.queue);
		queue.clear();
	}
	//! [scannerdataend]
	
	@Override
    public void tickSize(int tickerId, int field, int size) {
		lg.info("Tick Size. Ticker Id:" + tickerId + ":"+this.cMap.get(tickerId).symbol() + ", Field: " + field + ", Size: " + size);
    }
	
	@Override
    public void tickString(int tickerId, int tickType, String value) {
		lg.info("Tick string. Ticker Id:" + tickerId + ":"+this.cMap.get(tickerId).symbol() +", Type: " + tickType + ", Value: " + value);
    }
	
	@Override
    public void tickGeneric(int tickerId, int tickType, double value) {
		lg.info("Tick Generic. Ticker Id:" + tickerId + ":"+this.cMap.get(tickerId).symbol() + ", Field: " + TickType.getField(tickType) + ", Value: " + value);
    }
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		lg.info("Tick Price. Ticker Id:"+tickerId+ ":"+this.cMap.get(tickerId).symbol() +", Field: "+field+", Price: "+price+", CanAutoExecute: "+ canAutoExecute);
		//if(price > Globals.MAX_STK_PRICE || price < Globals.MIN_STK_PRICE)
		//	this.cMap.remove(tickerId);
	}

	
	//! [realtimebar]
	@Override
	public void realtimeBar(int reqId, long time, double open, double high,
				double low, double close, long volume, double wap, int count) {
			lg.info("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
			
			barQueue.addLast(new Bar(time, high, low, open, close, wap, volume, count));
			if(barQueue.size()>50){
				dFac.loadBars(barQueue);
				barQueue.clear();
			}
	}
	
	@Override
    public void historicalData(int reqId, String date, double open,
            double high, double low, double close, int volume, int count,
            double WAP, boolean hasGaps) {
        lg.info("HistoricalData. "+reqId+" - Date: "+date+", Open: "+open+", High: "+high+", Low: "+low+", Close: "+close+", Volume: "+volume+", Count: "+count+", WAP: "+WAP+", HasGaps: "+hasGaps);
       	dFac.insertHistoricalBar(new Bar(0, high, low, open, close, WAP, volume, count), reqId, cMap.get(reqId));
		/*if(barQueue.size()>50){
			dFac.loadHistoricalBars(barQueue);
			barQueue.clear();
		}*/
	}
	
	//@Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        lg.info("HistoricalDataEnd. "+reqId+" - Start Date: "+startDateStr+", End Date: "+endDateStr);
        
       // dFac.loadHistoricalBars(barQueue);
		//barQueue.clear();
    }

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
		}
		else
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
}
