package com.pancorp.tbroker.placeorder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.controller.Bar;
import com.pancorp.tbroker.main.MarketScannerEWrapperImpl;
//import com.ib.client.EClientSocket;
import com.pancorp.tbroker.model.Scan;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Utils;

public class DataFactory {
	private static Logger lg = LogManager.getLogger(DataFactory.class);
	
	public final static String db_url = "jdbc:mysql://localhost:3306/tbroker?autoReconnect=true&useSSL=false";
	public final static String db_driver = "com.mysql.jdbc.Driver";
	public final static String db_user    = "pancorp";
	public final static String db_password = "m1lle0n$$$";
	
	//private MarketScannerEWrapperImpl wrapper;
	private Connection con;
	private PreparedStatement ps;
	private PreparedStatement ps2;
	
	public static String sqlInsertOrder= "INSERT INTO tbl_order_paper "+
			"(order_id, client_id, status, perm_id, parent_order_id,symbol, quantity, action, active_start_time, acct) VALUES (?,?,?,?,?,?,?,?,?,?)";
			//+ "(1,2) ON DUPLICATE KEY UPDATE col1 = 2;";
	
	public static String sqlUpdateOrder = "UPDATE tbl_order_paper SET "+
			"status=?, filled=?, remaining=?, avg_Fill_Price=?,last_Fill_Price=?,whyHeld=? WHERE order_id=?";
			//+ "ON DUPLICATE KEY UPDATE client_id = ;";
			
	public DataFactory() {
		//this.wrapper = wr;
		try {
		Class.forName(db_driver);  
		con=DriverManager.getConnection(db_url,db_user,db_password);  
		ps = con.prepareStatement(sqlInsertOrder);
		ps2 = con.prepareStatement(sqlUpdateOrder);
		}
		catch(SQLException e){
			lg.error("SQLException in contructor: " + e.getErrorCode() + ": " +e.getMessage());
		}
		catch(Exception e){
			lg.error("Exception in contructor: " +e.getMessage());
		}
	}
	
	public int updateOrder(int orderId, String status, double filled,
			double remaining, double avgFillPrice, int permId, int parentId,
			double lastFillPrice, int clientId, String whyHeld) {
		int sqlStatus= -1;
		
		try {		

			ps2.setString(1,status);
			ps2.setDouble(2, filled);
			ps2.setDouble(3, remaining);
			ps2.setDouble(4, avgFillPrice);
			ps2.setDouble(5, lastFillPrice);
			ps2.setString(6, whyHeld);
			ps2.setInt   (7, orderId);
			
			//ps.setString(10, symbol);
			//ps.setString(11, quantity);
			//ps.setString(12, action),;
			//ps.setDate(13, active_start_time);
			//ps.setDate(14, active_stop_time);
			//ps.setString(15, acct);
			//last_update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			sqlStatus = this.ps2.executeUpdate(); 
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
	
	public void destroy(){
		//TODO
	}

}
