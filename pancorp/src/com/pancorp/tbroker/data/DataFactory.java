package com.pancorp.tbroker.data;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
//import java.util.LinkedList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

import com.ib.client.Contract;
import com.pancorp.tbroker.model.Bar;
//import com.pancorp.tbroker.model.Cache;
import com.pancorp.tbroker.model.Candle;
//import com.pancorp.tbroker.model.Data;
import com.pancorp.tbroker.util.Utils;

public class DataFactory extends DBFactory{

	private static org.apache.logging.log4j.Logger lg = LogManager.getLogger(DataFactory.class);
	
	public void insertBar(Bar bar, Contract contr){
		Connection con=null;
		PreparedStatement ps = null;
		//Data d = new Data();
			
		double open = bar.open();
		double close = bar.close();
		
		double high = bar.high();
		double low = bar.low();
		int count = bar.count();
		//bar.formattedTime();
		long time = bar.time();
		long volume = bar.volume();
		double wap = bar.wap();
		
		Candle c = new Candle(bar);
		
		//double amp = c.getAmp();
		double bl = c.getBody_len();
		double usl = c.getUpper_shadow_len();
		double lsl = c.getLower_shadow_len();	
		int dir = c.getDirection();
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			con = getDataSource().getConnection();
	
			String sql = "INSERT INTO tbl_candle (symbol, exchange, open, close, high, low, count,c_datetime, volume, wap, body_len, up_shadow_len,low_shadow_len, direction) "+
						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, contr.symbol());
			ps.setString(2, contr.primaryExch());
			ps.setDouble(3, open);
			ps.setDouble(4, close);
			ps.setDouble(5, high);
			ps.setDouble(6, low);
			ps.setInt(7, count);
			ps.setTimestamp(8, new Timestamp(time));
			ps.setLong(9, volume);
			ps.setDouble(10, wap);
			ps.setDouble(11, bl);
			ps.setDouble(12, usl);
			ps.setDouble(13, lsl);
			ps.setInt(14, dir);
			
			int updated =ps.executeUpdate();  
			lg.info("inserted " + updated + " record");
			
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
		}
		
		//return d;
	}
	
/*	public Cache getCache(Contract c){
		Cache cache = new Cache();
		
		return cache;
	}*/
	
	public void insertHistoricData(){
		
	}
	

	public HashMap<Integer,String> loadDayList(){
		HashMap<Integer,String> map = new HashMap<>();
		String sqlStockList = "SELECT id, symbol FROM tbl_contract WHERE active=1 LIMIT 100";
		
		Connection con=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sym = null;
		Integer n = -1;
		
		try{  
			//Class.forName(DBConstants.db_driver);  
			//con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			con = getDataSource().getConnection();
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
	
	public DataFactory() {	}
}
