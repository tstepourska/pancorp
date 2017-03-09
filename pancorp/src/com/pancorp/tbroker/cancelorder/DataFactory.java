package com.pancorp.tbroker.cancelorder;

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

	private Connection con;
	private PreparedStatement ps;
	
	public static String sqlUpdateOrder = "UPDATE tbl_order_paper SET status=?, active_stop_time=? WHERE order_id=?";
			
	public DataFactory() {
		try {
		Class.forName(db_driver);  
		con=DriverManager.getConnection(db_url,db_user,db_password);  
		ps = con.prepareStatement(sqlUpdateOrder);
		}
		catch(SQLException e){
			lg.error("SQLException in contructor: " + e.getErrorCode() + ": " +e.getMessage());
		}
		catch(Exception e){
			lg.error("Exception in contructor: " +e.getMessage());
		}
	}
	
	public int updateOrder(int orderId, String status, double filled,
			double remaining, double avgFillPrice, double lastFillPrice) {
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
	
	public void destroy(){
		//TODO
	}

}
