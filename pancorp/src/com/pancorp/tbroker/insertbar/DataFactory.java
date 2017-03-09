package com.pancorp.tbroker.insertbar;

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
import com.ib.client.Types;
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
	
	public static String sqlInsertBar= "INSERT INTO tbl_bar (symbol,exchange,time,high,low ,open ,close,wap,volume,count ) VALUES (?,?,?,?,?,?,?,?,?,?)";
			
	public DataFactory() {
		//this.wrapper = wr;
		try {
		Class.forName(db_driver);  
		con=DriverManager.getConnection(db_url,db_user,db_password);  
		ps = con.prepareStatement(sqlInsertBar);

		}
		catch(SQLException e){
			lg.error("SQLException in contructor: " + e.getErrorCode() + ": " +e.getMessage());
		}
		catch(Exception e){
			lg.error("Exception in contructor: " +e.getMessage());
		}
	}
	
	public static HashMap<Integer,Contract> getMonitorList() throws Exception {
		HashMap<Integer,Contract>  cc = new HashMap<Integer,Contract> ();
		Contract contr;
		Integer i;
		Connection c = null;
		PreparedStatement p = null;
		ResultSet r =null;
		Exception exx = null;
		
		try {
			Class.forName(db_driver);  
			c =DriverManager.getConnection(db_url,db_user,db_password);  
			p = c.prepareStatement("SELECT id,symbol,exchange FROM tbl_monitor_list WHERE inserted > CURDATE() - INTERVAL 1 DAY");
			r = p.executeQuery();
				
				while(r.next()){
					contr= new Contract();
					
					i = r.getInt("ID");
					contr.secType(Types.SecType.STK);
					contr.symbol(r.getString("SYMBOL"));
					contr.exchange(r.getString("EXCHANGE"));
					contr.primaryExch(r.getString("EXCHANGE"));
					contr.currency("USD");
					
					cc.put(i,contr);
				}
			}
			catch(SQLException e){
				lg.error("SQLException in contructor: " + e.getErrorCode() + ": " +e.getMessage());
				exx = e;
				
			}
			catch(Exception e){
				lg.error("Exception in contructor: " +e.getMessage());
				exx = e;
			}
		finally{
			try {
				r.close();
			}catch(Exception e){}
			try {
				p.close();
			}catch(Exception e){}
			try {
				c.close();
			}catch(Exception e){}
			
		}
		

		if(exx!=null)
			throw exx;
		
		return cc;
	}
	
	public int insertBar(String symbol, String exchange, int reqId, long time, double open, double high,
			double low, double close, long volume, double wap, int count) {
		int sqlStatus= -1;
		
		try {		
			
			ps.setString(1, symbol); 
			ps.setString(2, exchange);
			ps.setLong(3, time); 
			ps.setDouble(4, high);
			ps.setDouble(5,low);
			ps.setDouble(6, open); 
			ps.setDouble(7, close);
			ps.setDouble(8, wap);
			ps.setLong(9, volume); 
			ps.setInt(10, count);
			
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
