package com.pancorp.tbroker.day;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.util.Calculator;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.pancorp.tbroker.data.DBConstants;
import com.pancorp.tbroker.data.DBFactory;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.DataTick;
import com.pancorp.tbroker.model.IBar;
//import com.pancorp.tbroker.model.Tick;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataFactory extends DBFactory
{
	private static Logger lg = LogManager.getLogger(DataFactory.class); 
	//private volatile ArrayDeque<IBar> pileup;
	//up to 100 queues by symbol, each up to 17280 ticks per day
	//private volatile HashMap<Integer,Broker> map; 
	public HashMap<Integer,ArrayDeque<Bar>> barCache; 
	
	private boolean working = true;
	Connection persistentConnection;
	PreparedStatement persistentStatement;
	
	PreparedStatement persistentStmtUpdateSnapshot;

	//BrokerManagerEWrapperImpl _wrapper;
	
	public DataFactory() throws Exception {	//BrokerManagerEWrapperImpl wr
		super(); //initializes connection data source
		//pileup = new ArrayDeque<>();
		Class.forName(DBConstants.db_driver);  	
		persistentConnection = DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
		String persistentSql = "INSERT INTO tbl_bar (symbol_id, time, open, high,low, close, volume, wap, count) VALUES (?,?,?,?,?,?,?,?,?)";
		persistentStatement = persistentConnection.prepareStatement(persistentSql);
		
		String persistentSqlUpdateSnapshot = "UPDATE tbl_contract SET last_Close=?, halted=? WHERE id=?";
		persistentStmtUpdateSnapshot = persistentConnection.prepareStatement(persistentSqlUpdateSnapshot);
		//map = new HashMap<>(); 
		barCache = new HashMap<>();
		
		//this.wrapper = wr;
	}
	
	public String printCache(){
		return this.barCache.toString();
	}
	
	public synchronized void subscribe(int rid){ //, Broker t){
		//map.put(rid, t);
		
		barCache.put(rid, new ArrayDeque<Bar>());
	}
	
	public synchronized void unsubscribe(int rid){
		//map.remove(rid); //(sym, t);
		ArrayDeque<Bar> cache = barCache.remove(rid);
	}
	
	public boolean isSubscribed(int reqId){
		return this.barCache.containsKey(reqId);
	}
	
	
	public int insertOrder(int clientId, //int orderId, 
			Contract contract, 
			Order order, 
			OrderState orderState){
		int orderId = -1;
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		
		//TODO
		String sql = "INSERT INTO tbl_order (client_Id, status, perm_Id, parent_order_id, symbol, quantity, action, acct) VALUES (?,?,?,?,?,?,?,?)";
		String sqlMax = "SELECT MAX(order_id) FROM tbl_order";
		//SELECT LAST_INSERT_ID(); returns id inserted by this client
		
		try{  
			con = getDataSource().getConnection();
			if(lg.isTraceEnabled())
				lg.trace("insertOrder: gotConnection: " + con);
			ps = con.prepareStatement(sql);
			
			//ps.setInt(1, orderId);
			ps.setInt(1, clientId);
			if(orderState==null)
				ps.setString(2, "Submitted");
			else
				ps.setString(2, orderState.status().name());
			//ps.setDouble(4, filled);
			//ps.setDouble(5, remaining);
			//ps.setDouble(6, avgFillPrice);
			ps.setLong(3, order.permId());
			ps.setInt(4, order.parentId());

			ps.setString(5, contract.symbol());
			ps.setDouble(6, order.totalQuantity());
			ps.setString(7, order.getAction());
			//ps.setString(8, order.activeStartTime());
			//ps.setString(9, order.activeStopTime());
			//TODO date
			//ps.setTimestamp(9, new Timestamp(System.currentTimeMillis())); //order.activeStartTime());// active_start_time);
			//ps.setDate(14, order.activeStopTime());// active_stop_time);
			ps.setString(8, order.account());
			//last_update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			//ps.setString(16, whyHeld);
			
			int sqlStatus = ps.executeUpdate(); 
			
			if(sqlStatus==1){
				ps2 = con.prepareStatement(sqlMax);
				rs = ps2.executeQuery();
				if(rs.next())
					orderId = rs.getInt(1);
			}
			lg.info("insertOrder: id: " + orderId);
			
		}
		catch(SQLException sqle){
			//sqlStatus = sqle.getErrorCode();
			lg.error("SQLException: code: " + sqle.getErrorCode()+ ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			//sqlStatus = Constants.STATUS_ERROR;
			Utils.logError(lg, e);
		}  
		finally {
			try{
				ps.close();
			}catch(Exception e){}
			try{
				con.close();
			}catch(Exception e){}
		}
		
		return orderId;
	}
	
	public int updateOrder(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
		//(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		int sqlStatus= -1;
		int parameterIndex = 0;
		Connection con = null;
		PreparedStatement ps = null;
		StringBuilder sb = new StringBuilder();
		
		try {	
		sb.append("UPDATE tbl_order SET (");
		
		if(status!=null && status.length()>0){
			sb.append("status=?");
			parameterIndex++;
		}
		
		if(filled>0){
			sb.append(",filled=?");
			parameterIndex++;
		}
		
		if(remaining>0){
			sb.append(",remaining=?");
			parameterIndex++;
		}
		
		if(avgFillPrice>0){
			sb.append(",avgFillPrice=?");
			parameterIndex++;
		}
		
		if(lastFillPrice>0){
			sb.append(",lastFillPrice=?");
			parameterIndex++;
		}
		
		if(whyHeld!=null&&whyHeld.length()>0){
			sb.append(",whyHeld=?");
			parameterIndex++;
		}
		
		sb.append(" WHERE orderId=?");
		
		if(parameterIndex<=0){
			lg.info("updateOrder: data is empty, nothing to update");
			throw new Exception("updateOrder: data is empty, nothing to update");
		}
		
		String sql = sb.toString();
		lg.info("updateOrder: sql: " + sql);
		con = getDataSource().getConnection();
		if(lg.isTraceEnabled())
			lg.trace("insertOrder: gotConnection: " + con);
		ps = con.prepareStatement(sql);
		
		int c = 0;
		
		if(status!=null && status.length()>0)
			ps.setString(++c, status);
		
		if(filled>0)
			ps.setDouble(++c, filled);
		
		if(remaining>0)
			ps.setDouble(++c, remaining);
		
		if(avgFillPrice>0)
			ps.setDouble(++c, avgFillPrice);
		
		if(lastFillPrice>0)
			ps.setDouble(++c, lastFillPrice);
		
		if(whyHeld!=null&&whyHeld.length()>0)
			ps.setString(++c, whyHeld);

			sqlStatus = ps.executeUpdate(); 
		}
		catch(SQLException sqle){
			sqlStatus = sqle.getErrorCode();
			lg.error("SQLException: code: " + sqlStatus+ ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			sqlStatus = Constants.STATUS_ERROR;
			Utils.logError(lg, e);
		}  
		finally {
			try{
				ps.close();
			}catch(Exception e){}
			try{
				con.close();
			}catch(Exception e){}
		}
		return sqlStatus;
	}
	
	
	/**
	 * Saves tick data in a database, then sorts it out into an 
	 * appropriate queue by symbol
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public int recordTick(int reqId, long time, double open, double high,double low, double close, long volume, double wap, int count) {
		                        
		int status = -1;
		//ArrayDeque<IBar> deck;
		//Connection con = null;
		//PreparedStatement ps = null;
		
		
		
		try {
			//con = getDataSource().getConnection();
			//if(lg.isTraceEnabled())
			//	lg.trace("recordTick: gotConnection: " + con);
			//ps = con.prepareStatement(sql);

			persistentStatement.setInt(1,reqId);

			persistentStatement.setLong(2, time);
			persistentStatement.setDouble(3,open);	
			persistentStatement.setDouble(4, high);
			persistentStatement.setDouble(5, low);
			persistentStatement.setDouble(6, close);
			persistentStatement.setLong  (7, volume);
			persistentStatement.setDouble(8, wap);
			persistentStatement.setInt   (9, count);
		
		status = persistentStatement.executeUpdate();
		if(status!=1)
		lg.error("inserted: " + status);
		
		//sort by symbol
		synchronized(this){
			//lg.debug("Current cache: " + this.barCache);
			if(this.isSubscribed(reqId)){
				try {
					//map.get(reqId).addTick(new Bar(time, high, low, open, close, wap, volume, count));
					barCache.get(reqId).push(new Bar(time, high, low, open, close, wap, volume, count));
					lg.debug("pushed to : " + reqId + " cache");
				}
				catch(Exception ie){
					lg.error("Error pushing tick to " + reqId + " cache: " + ie.getMessage());
					
				}
			}
			else{
				lg.info(reqId + " is not subscribed, could not push tick");
			}
		}
		}
		catch(SQLException e){
			lg.error("SQLException: " + e.getErrorCode());
			Utils.logError(lg, e);
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}
		
		return status;
	}
	
	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}
	
	public HashMap<Integer,Contract> loadDayList(){
		lg.trace("LoadDayList started");
		HashMap<Integer,Contract> map = new HashMap<>();
		String sqlStockList = "SELECT c.id, c.symbol,type FROM tbl_contract c JOIN tbL_sec_type t ON sec_type=t.id WHERE active=1 LIMIT 20";
		//String sqlStockList = "SELECT c.id, c.symbol, type FROM tbl_contract c JOIN tbL_sec_type t ON sec_type=t.id  WHERE price IS NOT NULL AND last_close > 15 AND last_close < 90 LIMIT 3";
		
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sym = null;
		Integer n = -1;
		Contract contract;
		String type;
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			con = this.getDataSource().getConnection();
			ps = con.prepareStatement(sqlStockList);		
			rs = ps.executeQuery();  
			
			while(rs.next())  {
				n = rs.getInt("ID");
				sym = rs.getString("SYMBOL");
				type = rs.getString("TYPE");
				lg.debug("ID: " + n + ", sym: " + sym);
				
				contract = new Contract();
	    		contract.symbol(sym);
	    		contract.secType(type);
	    		contract.currency("USD");
	    		//contract.exchange("SMART");
	    		contract.exchange("IDEALPRO");
	    		//Specify the Primary Exchange attribute to avoid contract ambiguity
	    		//contract.primaryExch("ISLAND");
	    	//	contract.primaryExch("IDEALPRO");
	    		lg.trace("created contract for id " +n + " symbol " + sym);
	    		
				map.put(n,contract);
			}
			
		}
		catch(SQLException sqle){
			lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}  
		finally {
			try {
			con.close();
			}
			catch(Exception e){
				
			}
			try {
				ps.close();
				}
				catch(Exception e){
					
				}
			try {
				rs.close();
				}
				catch(Exception e){
					
				}
		}
		
		
		return map;
	}
	

	public HashMap<Integer,Contract> updateSnapshot(DataTick tick){
		lg.trace("updateSnapshot started");
		HashMap<Integer,Contract> map = new HashMap<>();
		String sql = "UPDATE tbl_contract SET last_Close=?, halted=? WHERE id=?";	
		
		//Connection con=null;
		//PreparedStatement ps = null;
		//ResultSet rs = null;
		String sym = null;
		String sec_type = null;
		Integer n = -1;
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			//con = this.getDataSource().getConnection();
			//ps = persistentConnection.prepareStatement(sql);		
			
			persistentStmtUpdateSnapshot.setDouble(1,tick.getClose());
			persistentStmtUpdateSnapshot.setDouble(2, tick.getHalted());
			persistentStmtUpdateSnapshot.setInt(3, tick.getTickerId());
			int st = persistentStmtUpdateSnapshot.executeUpdate();  
			if(lg.isTraceEnabled())
				lg.trace("updateSnapshot: updated "+ tick.getTickerId() +": " + st);
		}
		catch(SQLException sqle){
			lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}  
		finally {
			try {
			//con.close();
			}
			catch(Exception e){
				
			}
			try {
				//ps.close();
				}
				catch(Exception e){
					
				}
		}	
		
		return map;
	}


	public HashMap<Integer,Contract> loadFullList(){
	//public LinkedList<Contract> loadFullList(){
		lg.trace("LoadFullList started");
		HashMap<Integer,Contract> map = new HashMap<>();
		//LinkedList<Contract> map = new LinkedList<>();
		String sqlStockList = "SELECT c.id, c.symbol, type FROM tbl_contract c JOIN tbL_sec_type t ON sec_type=t.id WHERE c.id > 582";
		
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sym = null;
		String sec_type = null;
		Integer n = -1;
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			con = this.getDataSource().getConnection();
			ps = con.prepareStatement(sqlStockList);		
			rs = ps.executeQuery();  
			Contract contract;
			while(rs.next())  {
				n = rs.getInt("ID")+Constants.REQ_ID_SNAPSHOT;
				sym = rs.getString("SYMBOL");
				sec_type = rs.getString("TYPE");
				lg.debug("ID: " + n + ", sym: " + sym + ", sec_type: " + sec_type);
				
				contract = new Contract();
	    		contract.symbol(sym);
	    		contract.secType(sec_type);
	    		contract.currency("USD");
	    		contract.exchange("SMART");
	    		//Specify the Primary Exchange attribute to avoid contract ambiguity
	    		//contract.primaryExch("ISLAND");
	    		
				map.put(n, contract);
	    		//map.addFirst(contract);
			}
			
		}
		catch(SQLException sqle){
			lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}  
		finally {
			try {
			con.close();
			}
			catch(Exception e){
				
			}
			try {
				ps.close();
				}
				catch(Exception e){
					
				}
			try {
				rs.close();
				}
				catch(Exception e){
					
				}
		}
		
		
		return map;
	}


/*	@Override
	public void run(){
		IBar b;
		int st = -1;
		while(working){
			if(lg.isTraceEnabled())
			lg.trace("...");
			try {
				b = this.removeLast();
				if(lg.isTraceEnabled())
					lg.trace("got bar: " + b);
				if(b==null)
					Thread.sleep(Constants.SLEEP_WAIT_FOR_BAR);			
				else
					st = recordTick(b);
			}
			catch(InterruptedException ie){ 
				lg.error(ie.getMessage()); 
			}
			catch(Exception e){ 
				st = Constants.STATUS_ERROR; 
				lg.error(e.getMessage()); 
				try {
				Thread.sleep(Constants.SLEEP_WAIT_FOR_BAR);	
				}
				catch(InterruptedException ie){
					lg.error("Interrupted Exception while catching exception");
				}
				}
		}
	}*/
	
	public void cleanUp(){
		try {
		this.persistentStatement.close();
		}
		catch(Exception e){}
		try {
		this.persistentStmtUpdateSnapshot.close();
		}
		catch(Exception e){}
		try{
		this.persistentConnection.close();
		}
		catch(Exception e){}
	}
	
	public void fillUpTestCache(int rid){
		//int rid = 2117;
		String sqlSelect = "SELECT symbol_id, time, high, low, open ,close ,wap ,volume, count FROM tbl_bar WHERE symbol_id=? ORDER BY create_timestamp";
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int symbol_id =0; 
		long time=0;
		double high =0;
		double low =0;
		double open=0;
		double close=0;
		double wap=0;
		long volume=0;
		int count=0;
		ArrayDeque<Bar> arr = new ArrayDeque<>();
		
		if(!this.barCache.containsKey(rid))
			this.barCache.put(rid, new ArrayDeque<Bar>());
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			con = this.getDataSource().getConnection();
			ps = con.prepareStatement(sqlSelect);		
			ps.setInt(1, rid);
			rs = ps.executeQuery();  
			Bar b = null;
			while(rs.next())  {
				symbol_id = rs.getInt("SYMBOL_ID");
				time = rs.getLong("TIME");
				high = rs.getDouble("HIGH");
				low = rs.getDouble("LOW");
				open = rs.getDouble("OPEN");
				close = rs.getDouble("CLOSE");
				wap = rs.getDouble("WAP");
				volume = rs.getLong("VOLUME");
				count = rs.getInt("COUNT");
				//lg.debug("ID: " + n + ", sym: " + sym + ", sec_type: " + sec_type);
				
				b = new Bar(time, high, low, open, close, wap,volume,count);
	    		//Specify the Primary Exchange attribute to avoid contract ambiguity
	    		//contract.primaryExch("ISLAND");
	    		
				arr.add(b);
				
				if(arr.size()>100){
					this.barCache.get(rid).addAll(arr);
					arr.clear();
				}
			}
			//this.barCache.put(rid, arr);
			this.barCache.get(rid).addAll(arr);
			arr.clear();
		}
		catch(SQLException sqle){
			lg.error("SQLException: code: " + sqle.getErrorCode() + ", msg: " + sqle.getMessage());
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}  
		finally {
			try {
			con.close();
			}
			catch(Exception e){
				
			}
			try {
				ps.close();
				}
				catch(Exception e){
					
				}
			try {
				rs.close();
				}
				catch(Exception e){
					
				}
		}
		
	}
}
