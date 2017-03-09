package com.pancorp.tbroker.insertbar;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.SoftDollarTier;
import com.ib.client.TickType;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

//! [ewrapperimpl]
public class RequestBarsEWrapperImpl extends AbstractRequestBarsEWrapperAdapter {
	private static Logger lg = LogManager.getLogger(RequestBarsEWrapperImpl.class);
	
	//! [ewrapperimpl]
	
	//! [socket_declare]
	private RequestBars requestBars;
	DataFactory dfac;
	HashMap<Integer,Contract> map;
	//! [socket_declare]
	
	//! [socket_init]
	public RequestBarsEWrapperImpl(RequestBars opb, HashMap<Integer,Contract> m) {
		requestBars = opb;
		this.readerSignal = new EJavaSignal();
		clientSocket = new EClientSocket(this, readerSignal);
		map = m;
		dfac = new DataFactory();
	}
	//! [accountdownloadend]
	
	//! [realtimebar]
	@Override
	public void realtimeBar(int reqId, long time, double open, double high,
			double low, double close, long volume, double wap, int count) {
		lg.info("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
		//Candle c = new Candle(time,high,low,open,close,wap,volume, count);
		//TODO  correlate Contract info with Candle
		Contract c = this.map.get(reqId);
		int status = dfac.insertBar(c.symbol(), c.exchange(), reqId, time, open, high,low, close, volume, wap, count);
	}
	//! [realtimebar]
	
	
}
