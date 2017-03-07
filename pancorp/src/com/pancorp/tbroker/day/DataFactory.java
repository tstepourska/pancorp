package com.pancorp.tbroker.day;

import java.util.ArrayDeque;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.util.Calculator;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.pancorp.tbroker.data.DBConstants;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.model.IBar;
//import com.pancorp.tbroker.model.Tick;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

public class DataFactory extends Thread {
	private static Logger lg = LogManager.getLogger(DataFactory.class); 
	private volatile ArrayDeque<IBar> pileup;
	//up to 100 queues by symbol, each up to 17280 ticks per day
	private volatile HashMap<Integer,Broker> map; 
	private boolean working = true;
	
	Connection con = null;
	PreparedStatement ps = null;
	String sql = "";
	String url = null;
	String user = null;
	String password = null;
	BrokerManagerEWrapperImpl wrapper;
	
	public DataFactory(BrokerManagerEWrapperImpl wr) throws Exception {
		pileup = new ArrayDeque<>();
		map = new HashMap<>(); 
		
		con  = DriverManager.getConnection(url, user, password);
		ps = con.prepareStatement(sql);
		
		this.wrapper = wr;
	}
	
	public synchronized void subscribe(int rid, Broker t){
		map.put(rid, t);
	}
	
	public synchronized void unsubscribe(String sym){
		map.remove(sym); //(sym, t);
	}
	
	public int insertOrder(int clientId, int orderId, Contract contract, Order order, OrderState orderState){
		int sqlStatus = -1;
		
		try{  
			ps.setInt(1, orderId);
			ps.setInt(2, clientId);
			ps.setString(3, orderState.status().name());
			//ps.setDouble(4, filled);
			//ps.setDouble(5, remaining);
			//ps.setDouble(6, avgFillPrice);
			ps.setLong(4, order.permId());
			ps.setInt(5, order.parentId());
			//ps.setDouble(9, order.lastFillPrice);
			ps.setString(6, contract.symbol());
			ps.setDouble(7, order.totalQuantity());
			ps.setString(8, order.getAction());
			//TODO date
			ps.setTimestamp(9, new Timestamp(System.currentTimeMillis())); //order.activeStartTime());// active_start_time);
			//ps.setDate(14, order.activeStopTime());// active_stop_time);
			ps.setString(10, order.account());
			//last_update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			//ps.setString(16, whyHeld);
			
			sqlStatus = this.ps.executeUpdate(); 
			
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
			
		}
		
		return sqlStatus;
	}
	
	
	public int updateOrder(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
		//(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		int sqlStatus= -1;
		
		try {		

			ps.setString(1,status);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setInt   (3, orderId);

			sqlStatus = this.ps.executeUpdate(); 
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
			
		}
		return sqlStatus;
	}
	
	/**
	 * Adds new tick into the pileup queue
	 * Make sure to check that tick is not null 
	 * when calling this method
	 * 
	 * @param t
	 */
	public synchronized void addFirst(IBar t){
		pileup.addFirst(t);
	}
	
	/**
	 * Removes last element of the queue.
	 * Only DataFactory object can remove an element
	 * 
	 * @return IBar
	 */
	private synchronized IBar removeLast(){
		if(!this.pileup.isEmpty())
			return this.pileup.removeLast();
		return null;
	}
	
	/**
	 * Saves tick data in a database, then sorts it out into an 
	 * appropriate queue by symbol
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	private int recordTick(IBar t) throws Exception {
		int status = -1;
		//ArrayDeque<IBar> deck;
		
		//String sym  = t.symbol();
		int reqId = t.recId();
		ps.setInt(1,reqId);

		ps.setDouble(2, t.open());
		ps.setDouble(3, t.close());
		ps.setDouble(4, t.high());
		ps.setDouble(5, t.low());
		ps.setLong  (6, t.time());
		ps.setLong  (7, t.volume());
		ps.setDouble(8, t.wap());
		ps.setInt   (9, t.count());
		
		status = ps.executeUpdate();
		lg.debug("inserted: " + status);
		
		//sort by symbol
		synchronized(this){
			if(!this.map.containsKey(reqId)){
				;	//do nothing, not subscribed
				
				//deck = new ArrayDeque<>();
				//deck.add(t);
				//map.put(sym, deck);
			}
			else{
				try {
					map.get(reqId).addTick(t);
				}
				catch(InterruptedException ie){
					//retry
					try {
						Thread.sleep(Constants.SLEEP_DF_INTERRUPTED);
						if(this.map.containsKey(reqId))
							map.get(reqId).addTick(t);
					}
					catch(InterruptedException ie2){}
				}
			}
		}
		
		return status;
	}
	
	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}
	
	
	public static HashMap<Integer,String> loadDayList(){
		HashMap<Integer,String> map = new HashMap<>();
		String sqlStockList = "SELECT id, symbol FROM tbl_contract WHERE active=1 LIMIT 100";
		
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sym = null;
		Integer n = -1;
		
		try{  
			Class.forName(DBConstants.db_driver);  
			con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
	
			ps = con.prepareStatement(sqlStockList);		
			rs = ps.executeQuery();  
			
			if(rs.next())  {
				n = rs.getInt("ID");
				sym = rs.getString("SYMBOL");
				map.put(n, sym);
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


	@Override
	public void run(){
		IBar b;
		int st = -1;
		while(working){
			try {
				b = this.removeLast();
				if(b==null)
					Thread.sleep(Constants.SLEEP_WAIT_FOR_BAR);			
				else
					st = recordTick(b);
			}
			catch(InterruptedException ie){ lg.error(ie.getMessage()); }
			catch(Exception e){ st = Constants.STATUS_ERROR; lg.error(e.getMessage()); }
		}
	}
}
