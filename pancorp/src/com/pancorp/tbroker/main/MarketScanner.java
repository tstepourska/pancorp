package com.pancorp.tbroker.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ib.client.ScannerSubscription;
import com.ib.client.Types.FADataType;
import com.ib.controller.AccountSummaryTag;
import com.pancorp.tbroker.data.MarketScanDataFactory;
import com.pancorp.tbroker.model.Scan;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;

public class MarketScanner {
	private static Logger lg = LogManager.getLogger(MarketScanner.class);
	
	public static void main(String[] args) throws InterruptedException, Exception {
		MarketScannerEWrapperImpl wrapper = new MarketScannerEWrapperImpl();		
		final EClientSocket m_client = wrapper.getClient();
		final EReaderSignal m_signal = wrapper.getSignal();
		final MarketScanDataFactory factory = wrapper.getDataFactory();
		
		//HashMap<Integer,Scan> scanMap = createUSMajorStockScanMap();
	   // factory.setScanMap(scanMap);
		
	    //! [connect]
		m_client.eConnect(Globals.host, Globals.port, Globals.paperClientId);//TWS		
		//! [connect]
		//! [ereader]
		final EReader reader = new EReader(m_client, m_signal);        
		reader.setName("Reader_1");
        reader.start();        
        new Thread() {
        	public void run() {
        		this.setName("Runner_1");
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
        Thread.sleep(1000);
  
        //run scanner with some volume and price filters
        //marketScanners(wrapper.getClient(), scanMap);
        /////realTimeBars(wrapper.getClient());
		
		Thread.sleep(4000);

		//realTimeBars(wrapper.getClient(), conList);
		//ArrayList<Contract> list= factory.loadList();
		//int reqId = 4000;
		//load list of all stocks
		HashMap<Integer,Contract> cMap = factory.loadList();	
		wrapper.setCMap(cMap);
		
		Iterator<Integer> it = cMap.keySet().iterator();
		int key;
		while(it.hasNext()){
		//for(Contract c : list){
			key = it.next();
			
			//for each item get historical data for the previous day
			//requestHistoricalData(wrapper.getClient(), key, cMap.get(key));
		String sym = "AAPL";
		String instr = "STK"; //"scan_instrument");
		String currency = "USD";
		
		//create contract
		Contract c = new Contract();
		c.secType(instr);
		c.symbol(sym);
		c.currency(currency);
		c.exchange("SMART");
		//Specify the Primary Exchange attribute to avoid contract ambiguity
		c.primaryExch("ISLAND");
			requestSnapshots(wrapper.getClient(),459, c); //cMap.get(key));
		}
		
		
		//requestMarketData(wrapper.getClient(), wrapper, cMap);
		
		Thread.sleep(4000);
		//factory.working = false;
		///Thread.sleep(4000);
		
		m_client.eDisconnect();
		if(!m_client.isConnected()){
			lg.info("disconnected");
			
		}
		/*
		if(lg.isTraceEnabled()){
			Iterator<Integer> it = cMap.keySet().iterator();
			while(it.hasNext()){
				int k = it.next();
				lg.trace("key: " + k + ", value: " + cMap.get(k).symbol());
			}
		}*/
		
		lg.info("DONE");
	}
	
	private static void requestSnapshots(EClientSocket client, int reqId,Contract c){
		 client.reqMktData(reqId, c, "", true, null);  
	}
	
	private static void requestMarketData(EClientSocket client, MarketScannerEWrapperImpl wr, HashMap<Integer,Contract> map){
      
        wr.clearBarCache();
        Iterator<Integer> it = map.keySet().iterator();
        int key;
        while(it.hasNext()){
        	key = it.next();
        	// Legal ones for (STK) are: 
        	// 100(Option Volume),
        	/*
        	101(Option Open Interest),
        	105(Average Opt Volume),
        	106(impvolat),
        	107(climpvlt),
        	125(Bond analytic data),
        	165(Misc. Stats),
        	166(CScreen),
        	221/220(Creditman Mark Price),
        	225(Auction),
        	232/221(Pl Price),
        	233(RTVolume),
        	236(inventory),
        	258/47(Fundamentals),
        	291(ivclose),
        	293(TradeCount),
        	294(TradeRate),
        	295(VolumeRate),
        	318(LastRTHTrade),
        	370(ParticipationMonitor),
        	370(ParticipationMonitor),
        	375(RTTrdVolume),
        	377(CttTickTag),
        	377(CttTickTag),
        	381(IB Rate),
        	384(RfqTickRespTag),
        	384(RfqTickRespTag),
        	387(DMM),
        	388(Issuer Fundamentals),
        	391(IBWarrantImpVolCompeteTick),
        	405(Index Capabilities),
        	407(FuturesMargins),
        	411(rthistvol),
        	428(Monetary Close Price),
        	439(MonitorTickTag),
        	439(MonitorTickTag),
        	456/59(IBDividends),
        	459(RTCLOSE),
        	460(Bond Factor Multiplier),
        	499(Fee and Rebate Rate),
        	506(midptiv),
        	511(hvolrt10 (per-underlying)),
        	512(hvolrt30 (per-underlying)),
        	513(hvolrt50 (per-underlying)),
        	514(hvolrt75 (per-underlying)),
        	515(hvolrt100 (per-underlying)),
        	516(hvolrt150 (per-underlying)),
        	517(hvolrt200 (per-underlying)),
        	521(fzmidptiv),
        	545(vsiv),
        	576(EtfNavBidAsk(navbidask)),
        	577(EtfNavLast(navlast)),
        	578(EtfNavClose(navclose)),
        	584(Average Opening Vol.),
        	585(Average Closing Vol.),
        	587(Pl Price Delayed),
        	588(Futures Open Interest),
        	595(Short-Term Volume X Mins),
        	608(EMA N),
        	614(EtfNavMisc(hight/low)),
        	619(Creditman Slow Mark Price),
        	623(EtfFrozenNavLast(fznavlast))
        	// ticks: RTVolume (Time & Sales), shortable,  ivclose, volume rate  233,236,291,
        	 * */
        	client.reqMktData(key, map.get(key), "295", false, null);
        }
        try {
        Thread.sleep(10000);
        }
        catch(InterruptedException e){}
        
        it = map.keySet().iterator();
        while(it.hasNext()){
        	key = it.next();
        	client.cancelMktData(key);
        } 
	}
	
	private static void requestHistoricalData(EClientSocket client, int rid, Contract c ) { //MarketScannerEWrapperImpl wr, ArrayList<Contract> list){
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String formatted = form.format(cal.getTime());
       // int rid = 4000;
       // wr.clearBarCache();
        
       // for(Contract c : list){
        //	rid++;																						  RTH only
        	client.reqHistoricalData(rid, c, formatted, "1 D", "5 secs", Constants.BAR_WHAT_TO_SHOW_TRADES, 1, 			1, null);
        	
        	try {
        	Thread.sleep(1500);
        	}
        	catch(InterruptedException ie){}
       // }
	}
	
	/**
	 * 
	 * @param client
	 * @param con
	 * @param reqId
	 * @throws InterruptedException
	 */
	private static void realTimeBars(EClientSocket client, LinkedList<Contract> list) throws InterruptedException {
				
		/*** Requesting real time bars ***/
        //! [reqrealtimebars]
		/*
		 * bar size
		 * 
		 * What to show
		 * 
		 * UseRTH		boolean		Regular Trading Hours only. Valid values include:
			0 = all data available during the time span requested is
			returned, including time intervals when the market in
			question was outside of regular trading hours.
			1 = only data within the regular trading hours for the
			product requested is returned, even if the time time span
			falls partially or completely outside
			
			realTimeBarOptions Vector<TagValue> For internal use only. Use default value XYZ (here null)
		*/
		Iterator<Contract> it = list.iterator();
		int reqId = 3000;
		Contract c;
		
		//request real time data for each symbol and update scan table with latest price and volume
		while(it.hasNext()){
			reqId++;
			c = it.next();
			client.reqRealTimeBars(reqId, c, Constants.BAR_SIZE, Constants.BAR_WHAT_TO_SHOW_MIDPOINT, true, null);
			//! [reqrealtimebars]
			Thread.sleep(5000);
			/*** Canceling real time bars ***/
			//! [cancelrealtimebars]
			client.cancelRealTimeBars(reqId);
			//! [cancelrealtimebars]
			Thread.sleep(3000);
		}
	}

	/**
	 * 
TOP_PERC_GAIN		- Contracts whose last trade price shows the highest percent increase from the previous night's closing price.

MOST_ACTIVE			- Contracts with the highest trading volume today, based on units used by TWS (lots for US stocks; contract 
						for derivatives and non-US stocks). The sample spreadsheet includes two MostActive scans: Most Active List, 
						which displays the most active contracts in the NASDAQ, NYSE and AMEX markets, and Most Active US, 
						which displays the most active stocks in the United States.

TOP_PERC_LOSE		- Contracts whose last trade price shows the lowest percent increase from the previous night's closing price.

HOT_BY_VOLUME		- Contracts where:
							-->today's Volume/avgDailyVolume is highest.
							-->avgDailyVolume is a 30-day exponential moving average of the contract's daily volume.

HOT_BY_PRICE		- Contracts where:
							-->(lastTradePrice-prevClose)/avgDailyChange is highest in absolute value (positive or negative).
							-->The avgDailyChange is defined as an exponential moving average of the contract's (dailyClose-dailyOpen)

TOP_TRADE_COUNT		- The top trade count during the day.

TOP_TRADE_RATE		- Contracts with the highest number of trades in the past 60 seconds 
						(regardless of the sizes of those trades).

TOP_PRICE_RANGE		- The largest difference between today's high and low, or yesterday's close if 
						outside of today's range.

HOT_BY_PRICE_RANGE	- The largest price range (from Top Price Range calculation) over the volatility.

TOP_VOLUME_RATE		- The top volume rate per minute.

NOT_OPEN			- Contracts that have not traded today.

HALTED				- Contracts for which trading has been halted.

TOP_OPEN_PERC_GAIN	- Shows contracts with the highest percent price INCREASE between the last trade and opening prices.

TOP_OPEN_PERC_LOSE	- Shows contracts with the highest percent price DECREASE between the last trade and opening prices.

HIGH_OPEN_GAP		- Shows contracts with the highest percent price INCREASE between the previous close and today's opening prices.

LOW_OPEN_GAP		- Shows contracts with the highest percent price DECREASE between the previous close and today's opening prices.

HIGH_VS_13W_HL		- The highest price for the past 13 weeks.

LOW_VS_13W_HL		- The lowest price for the past 13 weeks.

HIGH_VS_26W_HL		- The highest price for the past 26 weeks.

LOW_VS_26W_HL		- The lowest price for the past 26 weeks.

HIGH_VS_52W_HL		- The highest price for the past 52 weeks.

LOW_VS_52W_HL		- The lowest price for the past 52 weeks.

HIGH_LAST_VS_EMA20
LOW_LAST_VS_EMA20
HIGH_LAST_VS_EMA50
LOW_LAST_VS_EMA50
HIGH_LAST_VS_EMA100
LOW_LAST_VS_EMA100
HIGH_LAST_VS_EMA200
LOW_LAST_VS_EMA200
BULLISH_LAST_VS_EMA20
BEARISH_LAST_VS_EMA20
BULLISH_LAST_VS_EMA50
BEARISH_LAST_VS_EMA50
BULLISH_LAST_VS_EMA100
BEARISH_LAST_VS_EMA100
BULLISH_LAST_VS_EMA200
BEARISH_LAST_VS_EMA200
BULLISH_EMA_DIFF_VS_LAST (Bullish EMA(20) vs EMA(200))
BEARISH_EMA_DIFF_VS_LAST (Bearish EMA(20) vs EMA(200))
BULLISH_MACD_DIST_VS_LAST (histogramm)
BEARISH_MACD_DIST_VS_LAST (histogramm)
BULLISH_PPO_DIST (histogramm)
BEARISH_PPO_DIST (histogramm)



	 * 
	 * @param client
	 * @throws InterruptedException
	 */
	private static void marketScanners(EClientSocket client, HashMap<Integer,Scan> map) throws InterruptedException,Exception {
		
		/*** Requesting all available parameters which can be used to build a scanner request ***/
        //! [reqscannerparameters]
        //client.reqScannerParameters();
        //! [reqscannerparameters]
       // Thread.sleep(2000);
		Scan s;
		int reqId;
		Iterator<Scan> it = map.values().iterator();
        /*** Triggering a scanner subscription ***/
		while(it.hasNext()){
			s = it.next();
			reqId = s.getRequestId();
			//! [reqscannersubscription]
			client.reqScannerSubscription(reqId, createScannerSubscription(s),null);//,scan.instrument,locationCode, scanCodesOffHours[i]), null);
			//! [reqscannersubscription]
			Thread.sleep(5000);
			
			/*** Canceling the scanner subscription ***/
	        //! [cancelscannersubscription]
	        client.cancelScannerSubscription(reqId);
	        //! [cancelscannersubscription]		
	        
	        Thread.sleep(3000);
		} 
	}
	
	private static HashMap<Integer,Scan> createUSMajorStockScanMap(){
		String filter = "PRICE";
		int reqId = 7000;
		//Run these scans for US major stocks only (no pink sheets)
		String instrument 	= "STK";
		String locationCode = "STK.US.MAJOR";	
		String[] scanCodesOffHours = new String[] {
				//"TOP_PERC_GAIN", //compare with previous (yesterday)?
				"MOST_ACTIVE",
				//"TOP_PERC_LOSE", //compare with previous (yesterday)?
				//"HOT_BY_VOLUME",
				//"HOT_BY_PRICE",
				//"TOP_TRADE_COUNT",
				//"TOP_TRADE_RATE",	//should I check it every minute to confirm buy?
				//"TOP_PRICE_RANGE",
				//"HOT_BY_PRICE_RANGE",
				//"TOP_VOLUME_RATE",  //should I check it every minute to support grow trend signal?
				//"NOT_OPEN",
				//"HALTED", //check that ones to monitor not in this list
				//"TOP_OPEN_PERC_GAIN",
				//"TOP_OPEN_PERC_LOSE",
				//"HIGH_OPEN_GAP", //suppose to grow?
				//"LOW_OPEN_GAP",  //suppose to fall?
				//"HIGH_VS_13W_HL",
				//"LOW_VS_13W_HL"
				//"BULLISH_LAST_VS_EMA20",
				//"BULLISH_PPO_DIST"
				};
				//"HIGH_VS_26W_HL","LOW_VS_26W_HL","HIGH_VS_52W_HL","LOW_VS_52W_HL"
		
		HashMap<Integer,Scan> scanMap = new HashMap<>();
		Scan scan;
		
		//setup scan map for data factory
		for(int i=0;i<scanCodesOffHours.length;i++){
			reqId++;
			scan = new Scan(reqId, instrument, locationCode, scanCodesOffHours[i], filter);
			scanMap.put(reqId,scan);
			lg.info("reqID: " + reqId + ", scan: " + scan);
		}
		return scanMap;
	}
	
	/**
	 * Scanner parameters and filters as per scanner_params.xml
	 * 
	 * <ScanParameterResponse><InstrumentList varName="instrumentList"><Instrument>
                        <name>US Stocks</name>
                        <type>STK</type>
                        <filters>AFTERHRSCHANGEPERC,
                        		AVGOPTVOLUME,
                        		AVGPRICETARGET,
                        		AVGRATING,
                        		AVGTARGET2PRICERATIO,
                        		AVGVOLUME,
                        		AVGVOLUME_USD,
                        		CHANGEOPENPERC,
                        		CHANGEPERC,
                        		EMA_20,
                        		EMA_50,
                        		EMA_100,
                        		EMA_200,
                        		PRICE_VS_EMA_20,
                        		PRICE_VS_EMA_50,
                        		PRICE_VS_EMA_100,
                        		PRICE_VS_EMA_200,
                        		DAYSTOCOVER,
                        		DIVIB,
                        		DIVYIELD,
                        		DIVYIELDIB,
                        		FEERATE,
                        		FIRSTTRADEDATE,
                        		GROWTHRATE,
                        		HALTED,
                        		HASOPTIONS,
                        		HISTDIVIB,
                        		HISTDIVYIELDIB,
                        		IMBALANCE,
                        		IMBALANCEADVRATIOPERC,
                        		IMPVOLAT,
                        		IMPVOLATOVERHIST,
                        		INDEX_COMPARISON,
                        		INSIDEROFFLOATPERC,
                        		INSTITUTIONALOFFLOATPERC,
                        		MACD,
                        		MACD_SIGNAL,
                        		MACD_HISTOGRAM,
                        		MKTCAP,
                        		MKTCAP_USD,
                        		NEXTDIVAMOUNT,
                        		NEXTDIVDATE,
                        		NUMPRICETARGETS,
                        		NUMRATINGS,
                        		NUMSHARESINSIDER,
                        		NUMSHARESINSTITUTIONAL,
                        		NUMSHARESSHORT,
                        		OPENGAPPERC,
                        		OPTVOLUME,
                        		OPTVOLUMEPCRATIO,
                        		PERATIO,
                        		PILOT,
                        		PPO,
                        		PPO_SIGNAL,
                        		PPO_HISTOGRAM,
                        		PRICE,
                        		PRICE2BK,
                        		PRICE2TANBK,
                        		PRICERANGE,
                        		PRICE_USD,
                        		QUICKRATIO,
                        		REBATERATE,
                        		REGIMBALANCE,
                        		REGIMBALANCEADVRATIOPERC,
                        		RETEQUITY,
                        		SHORTABLESHARES,
                        		SHORTOFFLOATPERC,
                        		SHORTSALERESTRICTED,
                        		SIC,
                        		ISSUER_COUTRY_CODE,
                        		SOCSACT,
                        		SOCSNET,
                        		STKTYPE,
                        		STVOLUME_3MIN,
                        		STVOLUME_5MIN,
                        		STVOLUME_10MIN,
                        		TRADECOUNT,
                        		TRADERATE,
                        		UNSHORTABLE,
                        		VOLUME,
                        		VOLUMERATE,
                        		VOLUME_USD,
                        		RCGLTCLASS,
                        		RCGLTENDDATE,
                        		RCGLTIVALUE,
                        		RCGLTTRADE,
                        		RCGITCLASS,
                        		RCGITENDDATE,
                        		RCGITIVALUE,
                        		RCGITTRADE,
                        		RCGSTCLASS,
                        		RCGSTENDDATE,
                        		RCGSTIVALUE,
                        		RCGSTTRADE
                        </filters>
                        <group>STK.GLOBAL</group>
                        <shortName>US</shortName>
                        <cloudScanNotSupported>false</cloudScanNotSupported>
      </Instrument>
                
	 * @param instr
	 * @param locCode
	 * @param scanCode
	 * @return
	 */
	private static ScannerSubscription createScannerSubscription(Scan scan) { //String instr, String locCode, String scanCode) {

		ScannerSubscription sub = new ScannerSubscription();
		sub.instrument(scan.getInstrument());
		sub.locationCode(scan.getLocationCode());
		sub.scanCode(scan.getScanCode());	//can be left blank
		sub.numberOfRows(scan.getNumRows());
		//TODO to figure out filters
		//sub.scannerSettingPairs();
		sub.abovePrice(Globals.MIN_STK_PRICE);
		sub.belowPrice(Globals.MAX_STK_PRICE);

        return sub;	
	}
	
	/**
	 * <displayName>America Non-US Stocks</displayName>
                        <locationCode>STK.NA</locationCode>
                        <instruments>STOCK.NA</instruments>
                        <routeExchange>TSE</routeExchange>
                        <LocationTree varName="locationTree">
                                <Location>
                                        <displayName>Canada</displayName>
                                        <locationCode>STK.NA.CANADA</locationCode>
                                        <instruments>STOCK.NA</instruments>
                                        <routeExchange>TSE</routeExchange>
                                        <LocationTree varName="locationTree">
                                                <Location>
                                                        <displayName>TSE</displayName>
                                                        <locationCode>STK.NA.TSE</locationCode>
                                                        <instruments>STOCK.NA</instruments>
                                                        <routeExchange>TSE</routeExchange>
                                                        <delayedOnly>yes</delayedOnly>
                                                        <access>restricted;s=275;s=444;s=445;s=920</access>
                                                </Location>
	 */
}