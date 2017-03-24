package com.pancorp.tbroker.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types;
//import com.ib.controller.Bar;
import com.pancorp.tbroker.main.MarketScannerEWrapperImpl;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.model.Bar;
//import com.ib.client.EClientSocket;
import com.pancorp.tbroker.model.Scan;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

public class MarketScanDataFactory extends Thread {
	private static Logger lg = LogManager.getLogger(MarketScanDataFactory.class);
	private volatile LinkedList<ScannerLine> queue;
	private volatile LinkedList<Bar> barQueue;
	HashMap<Integer,Scan> scanMap = null;
	public boolean working = true;
	//public boolean connected = true;

	
	private MarketScannerEWrapperImpl wrapper;
	private Connection con;
	private PreparedStatement ps;
	private PreparedStatement ps2;
	private ResultSet rs;
	private Timestamp scanTimestamp;
	private HashMap<Integer,Contract> cMap;
	
	public static String sqlInsertScanLine = "INSERT INTO tbl_market_scan (" + 
			"request_id,scan_instrument,scan_location_code,scan_code,scan_filter,rank,symbol,sec_type,currency,scan_date_time) VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	public static String sql2 = "UPDATE tbl_market_scan SET (last_close=?, last_volume=?) WHERE symbol=? AND scan_date_time>NOW()-INTERVAL 1 DAY"; 
	
	
	public MarketScanDataFactory(MarketScannerEWrapperImpl wr) throws Exception {
		this.setName("MScanData_1");
		this.wrapper = wr;
		queue = new LinkedList<>();
		cMap = new HashMap<>();
		
		Class.forName(DBConstants.db_driver);  
		con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
		ps = con.prepareStatement(sqlInsertScanLine);
		ps2 = con.prepareStatement(sql2);
	}

	/**
	 * @return the scanMap
	 */
	public HashMap<Integer, Scan> getScanMap() {
		return scanMap;
	}

	/**
	 * @param scanMap the scanMap to set
	 */
	public void setScanMap(HashMap<Integer, Scan> sm) {
		this.scanMap = sm;
		if(lg.isTraceEnabled()){
			Iterator<Integer> it = scanMap.keySet().iterator();
			while(it.hasNext()){
				int k = it.next();
				lg.trace("key: " + k + ", value: " + scanMap.get(k));
			}
		}
		this.scanTimestamp = new Timestamp(System.currentTimeMillis());
	}

	public synchronized void addScanResultLine(int reqId, int rank,
			 String sym, String st, String curr, Contract con){
		this.queue.addLast(new ScannerLine( reqId, rank,sym, st, curr, con));
	}
	
	private synchronized ScannerLine getNextLine(){
		ScannerLine line = null;
		if(!this.queue.isEmpty()){
			line = queue.removeFirst();
		}
		return line;
	}
	
	public void run(){
		ScannerLine line = null;
		int cnt = 0;
		while(working){
			line = getNextLine();
			if(line==null){
				try{
					//if(cnt%7==0)
					//	lg.info("...");
					Thread.sleep(500);
				}
				catch(InterruptedException e){}
				continue;
			}
			
			int status = this.insertData(line);
			lg.info("Inserted " + status);
			cnt++;
		}
		
		try {
		int size = this.queue.size();
		lg.info("Queue size is " + size);
		if(size>0){
			for(int i=0;i<size;i++){
				ScannerLine li = this.queue.get(i);
				int st = insertData(li);
				lg.info("Inserted " + st);
			}
		}
		
			this.wrapper.setCMap(this.cMap);
		}
		catch(Exception ex){
			Utils.logError(lg, ex);
		}		
		finally{
			try {
				con.close();
				lg.info("Connection closed");
				}
				catch(Exception e){
					
				}
			try {
				this.ps.close();
				lg.info("Prepared statement closed");
			}
			catch(Exception e){}
			
			try {
				this.ps2.close();
				lg.info("Prepared statement closed");
			}
			catch(Exception e){}
			
			try {
				this.rs.close();
				lg.info("ResultSet closed");
			}
			catch(Exception e){}
			
			this.queue = null;
		}
		
	}
	
	public int insertHistoricalBar(//Bar bar, int reqId,Contract contr){
			int reqId, String date, double open,
            double high, double low, double close, int volume, int count,
            double WAP, boolean hasGaps, Contract contr) {
			//Connection con=null;
			PreparedStatement psh = null;
			//Data d = new Data();
			int updated = -1;
		/*	double open = bar.open();
			double close = bar.close();
			
			double high = bar.high();
			double low = bar.low();
			int count = bar.count();
			//bar.formattedTime();
			long time = bar.time();
			long volume = bar.volume();
			double wap = bar.wap();*/
			long time = Date.valueOf(date).getTime();
			Candle c = new Candle(reqId,  time,high, low,  open, close, WAP, volume, count);
			
			//double amp = c.getAmp();
			double bl = c.getBody_len();
			double usl = c.getUpper_shadow_len();
			double lsl = c.getLower_shadow_len();	
			int dir = c.getDirection();
			
			try{  
				//Class.forName(DBConstants.db_driver);  
				//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
		
				String sql = "INSERT INTO tbl_candle (open, close, high, low, count,c_datetime, volume, wap, body_len, up_shadow_len,low_shadow_len, direction, symbol_id) "+
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				/*try{
					psh.close();
				}catch(Exception e){}*/
				psh = con.prepareStatement(sql);
				
				
				psh.setDouble(1, open);
				psh.setDouble(2, close);
				psh.setDouble(3, high);
				psh.setDouble(4, low);
				psh.setInt(5, count);
				psh.setTimestamp(6, new Timestamp(time));
				psh.setLong(7, volume);
				psh.setDouble(8, WAP);
				psh.setDouble(9, bl);
				psh.setDouble(10, usl);
				psh.setDouble(11, lsl);
				psh.setInt(12, dir);
				psh.setInt(13, reqId);
				
				updated =psh.executeUpdate();  
				lg.info("inserted " + updated + " record");
			/*	
				Thread.sleep(1000);
				
				String sql2 = "SELECT * FROM tbl_calc_data WHERE symbol=?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, contr.symbol());
				ResultSet rs = ps2.executeQuery();
				double ema_10;
				double sma_10;
				
				if(rs.next())  {
					ema_10 = rs.getDouble(1);
					sma_10 = rs.getDouble(2);
					
					//d.EMA_10_DAYS = ema_10;
					//d.SMA_10_DAYS = sma_10;
				}
				*/
			}
			catch(SQLException sqle){
				lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
			}
			catch(Exception e){
				Utils.logError(lg, e);
			}  
			finally {
				try {
				psh.close();
				}
				catch(Exception e){
					
				}
			}
			
		return updated;
		
	}
	
	private int insertData(ScannerLine ln){
		int status = -1;
		
		try{  
			int reqId = ln.getReqId();
			Scan s = this.scanMap.get(reqId);
			Contract c = ln.getContract();
			this.cMap.put(reqId,c);
			/*"ScannerData. "+reqId+
			" - Rank: "+rank+
			", Symbol: "+contractDetails.contract().symbol()+
			", SecType: "+contractDetails.contract().secType()+
			", Currency: "+contractDetails.contract().currency()*/
			ps.setInt(1, reqId); //request Id
			ps.setString(2, s.getInstrument());
			ps.setString(3, s.getLocationCode());
			ps.setString(4, s.getScanCode());
			ps.setString(5, s.getFilter());
			ps.setInt(6, ln.getRank());  //rank in the returned list
			ps.setString(7, ln.getSymbol());
			ps.setString(8, ln.getSecType());
			ps.setString(9, ln.getCurrency());
			ps.setTimestamp(10, this.scanTimestamp);
			
			status = this.ps.executeUpdate(); 
			
		}
		catch(SQLException sqle){
			lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}  
		finally {
			
		}
		
		return status;
	}
	
	public HashMap<Integer,Contract> loadList() throws SQLException, Exception {
		//String sql3 = "SELECT DISTINCT symbol,scan_instrument, currency FROM tbl_market_scan WHERE scan_date_time > curdate()- INTERVAL 1 HOUR ORDER BY RANK";
		//String sql3 = "SELECT DISTINCT symbol,scan_instrument, currency FROM tbl_market_scan WHERE scan_date_time > curdate()- INTERVAL 1 HOUR";
		String sql3 = "select c.id, c.symbol, type, currency from tbl_contract c join tbl_sec_type s on c.sec_type=s.id join tbl_exchange e on c.primary_exchange=e.id";  //LIMIT 10
		
		ps2 = con.prepareStatement(sql3);
		rs = ps2.executeQuery();
		Contract c;
		//ArrayList<Contract> list = new ArrayList<>();
		HashMap<Integer,Contract> map = new HashMap<>();
		while(rs.next()){
			Integer id = rs.getInt("ID") + Constants.REQ_ID_HISTORICAL;
			String sym = rs.getString("SYMBOL");
			String instr = rs.getString("TYPE"); //"scan_instrument");
			String currency = rs.getString("CURRENCY");
			
			//create contract
			c = new Contract();
			c.secType(instr);
			c.symbol(sym);
			c.currency(currency);
			c.exchange("SMART");
			//Specify the Primary Exchange attribute to avoid contract ambiguity
			c.primaryExch("ISLAND");
			
			//list.add(c);
			map.put(id, c);
		}
		
		ps2.close();
		rs.close();
		
		return map; //list;
	}

	public ArrayList<Integer> callbackRunSelectionQuery(HashMap<Integer,Contract> map){
		//TODO run calculations
		ArrayList<Integer> list = null;
		String sql4 = "SELECT distinct symbol_id FROM tbl_candle WHERE low > 10 AND high < 100"; //, Max(high) as Max_price,Min(low) AS min_price
		try {
		ps2 = con.prepareStatement(sql4);
		rs = ps2.executeQuery();
		
		list = new ArrayList<>();
	//	HashMap<Integer,Contract> map = new HashMap<>();
		while(rs.next()){
			Integer id = rs.getInt("Symbol_ID");
			
			list.add(id);
			//map.put(id, c);
		}
		
		ps2.close();
		rs.close();
		}
		catch(Exception e){
			
		}
		
		return list;
	}
	
	/*private LinkedList<String> selectSymbols() throws SQLException, Exception {
		LinkedList<String> ss= new LinkedList<>();
		String sql2 = "SELECT DISTINCT symbol FROM tbl_market_scan WHERE (scan_date_time > NOW() - INTERVAL 1 DAY) ORDER BY symbol";
		String sym = null;
		Contract c;
		ps = con.prepareStatement(sql2);
		rs = ps.executeQuery();
		
		while(rs.next()){
			sym = rs.getString("SYMBOL");
			c = new Contract();
			c.
		}
		
		return ss;
	}*/
	
	public void loadQueue(LinkedList<ScannerLine> q){
		if(q==null||q.isEmpty())
			return;
		
		while(!q.isEmpty()){
			this.queue.addLast(q.removeFirst());
		}
		lg.info("loadQueue: done: " + this.queue.size());
	}
	
	public void loadBars(LinkedList<Bar> q){
		if(q==null||q.isEmpty())
			return;
		this.barQueue.addAll(q);
		lg.info("loadBars: done: " + this.barQueue.size());
	}
	/*
	public void loadHistoricalBars(LinkedList<Bar> q){
		if(q==null||q.isEmpty())
			return;
		this.barQueue.addAll(q);
		lg.info("loadBars: done: " + this.barQueue.size());
	}*/
	
	public class ScannerLine {
		int reqId; 
		int rank;
		String symbol;
		String secType;
		String currency;
		Contract contract; 
		String distance; 
		String benchmark;
		String projection;
		String legsStr;
		
		//derived
		//String category;
		
		public ScannerLine(int rid, int r, String sym, String st, String curr, Contract con){//ContractDetails cd,String d,String b,String p, String ls){
			this.reqId = rid;
			this.rank = r;
			this.symbol = sym;
			this.secType = st;
			this.currency = curr;
			this.contract = con;
			this.contract.symbol(sym);
			contract.exchange("SMART");
			//Specify the Primary Exchange attribute to avoid contract ambiguity
			contract.primaryExch("ISLAND");
			//this.distance = d;
			//this.benchmark = b;
			//this.projection = p;
			//this.legsStr = ls;
			
			//category = contractDetails.category();
			
		}

		/**
		 * @return the reqId
		 */
		public int getReqId() {
			return reqId;
		}

		/**
		 * @param reqId the reqId to set
		 */
		public void setReqId(int reqId) {
			this.reqId = reqId;
		}

		/**
		 * @return the rank
		 */
		public int getRank() {
			return rank;
		}

		/**
		 * @param rank the rank to set
		 */
		public void setRank(int rank) {
			this.rank = rank;
		}

		/**
		 * @return the symbol
		 */
		public String getSymbol() {
			return symbol;
		}

		/**
		 * @param symbol the symbol to set
		 */
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		/**
		 * @return the secType
		 */
		public String getSecType() {
			return secType;
		}

		/**
		 * @param secType the secType to set
		 */
		public void setSecType(String secType) {
			this.secType = secType;
		}

		/**
		 * @return the currency
		 */
		public String getCurrency() {
			return currency;
		}

		/**
		 * @param currency the currency to set
		 */
		public void setCurrency(String currency) {
			this.currency = currency;
		}

		/**
		 * @return the contract
		 */
		public Contract getContract() {
			return contract;
		}

		/**
		 * @param contract the contract to set
		 */
		public void setContract(Contract con) {
			this.contract = con;
		}

		/**
		 * @return the distance
		 */
		public String getDistance() {
			return distance;
		}

		/**
		 * @param distance the distance to set
		 */
		public void setDistance(String distance) {
			this.distance = distance;
		}

		/**
		 * @return the benchmark
		 */
		public String getBenchmark() {
			return benchmark;
		}

		/**
		 * @param benchmark the benchmark to set
		 */
		public void setBenchmark(String benchmark) {
			this.benchmark = benchmark;
		}

		/**
		 * @return the projection
		 */
		public String getProjection() {
			return projection;
		}

		/**
		 * @param projection the projection to set
		 */
		public void setProjection(String projection) {
			this.projection = projection;
		}

		/**
		 * @return the legsStr
		 */
		public String getLegsStr() {
			return legsStr;
		}

		/**
		 * @param legsStr the legsStr to set
		 */
		public void setLegsStr(String legsStr) {
			this.legsStr = legsStr;
		}
	}
}
