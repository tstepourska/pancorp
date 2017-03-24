package com.pancorp.tbroker.externaldataload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.data.DBConstants;
import com.pancorp.tbroker.util.Utils;

public class LoadStockListTD {
	private static Logger lg = LogManager.getLogger(LoadStockListTD.class);
	public LoadStockListTD() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		Connection con=null;
		PreparedStatement psInsert = null;
		PreparedStatement ps0 = null;
		ResultSet rs0 = null;
		int u = -1;
		BufferedReader reader = null;
		int inserted = 0;
		int updated = 0;
		int totalLines = 0;
		
		try {
		//String sql = "INSERT INTO tbl_contract (symbol,sec_type,primary_exchange, c_name) VALUES (?,?,(SELECT id FROM tbl_exchange WHERE symbol=?),?)";
			//	0 	 1		2		3 		4				5
			//Rank,Symbol,Name,Exchange,Stock Price,Volume 90-Day Average
	        // 1,BAC,Bank of America Corporation,NYSE,24.860000,101224814.911111,
	         
		String sql0 = "SELECT id FROM tbl_contract WHERE symbol=?";
		String sqlInsert = "INSERT INTO tbl_contract (symbol,sec_type,primary_exchange, c_name, price,volume_90_day_avg) VALUES (?,?,(SELECT id FROM tbl_exchange WHERE symbol=?),?,?,?)";
		String sqlUpdate = "UPDATE tbl_contract SET price=?, volume_90_day_avg=?, last_updated=CURRENT_TIMESTAMP WHERE id=?";
		
		
		Class.forName(DBConstants.db_driver);  
		con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
		ps0 = con.prepareStatement(sql0);	
		
		reader = new BufferedReader(new FileReader("/Users/pankstep/run/TBroker/data/TD/US_Stocks.csv"));
		String line;
		String[] arr = null;
		String symbol;
		int id =-1;
		double price = -1;
		double volume = -1;
		
		while((line = reader.readLine())!=null){
			//lg.debug("line: " + line);
			totalLines++;
			try {
				if(line.startsWith("Rank"))
					continue;
				
				arr = line.split(",");
				if(lg.isTraceEnabled())
					lg.trace("arr[0]: "+arr[0]);
				//int rank = Integer.parseInt(arr[0]);
				
			//double d = Double.parseDouble("24.860000");
				//lg.debug("parsed double: " + d);
			}
			catch(Exception e){
				//lg.info("skipping line");
				//continue;
			}
		
			symbol = arr[1];
			ps0.setString(1, symbol);
			rs0 = ps0.executeQuery();
			
			if(rs0.next()){
				//record exists, update
				id=rs0.getInt("ID");
				lg.info("record exists for symbol "+symbol+" id: "+id+ ", updating..");
				
				psInsert = con.prepareStatement(sqlUpdate);
				lg.trace("prepared stmt for update");
				
				price = Double.parseDouble(arr[4]);
				lg.trace("array[4] - price: " + price);
				psInsert.setDouble(1, price);	//~price
				lg.trace("set price");
				
				volume = Double.parseDouble(arr[5]);
				lg.trace("array[5] - volume: " + volume);
				psInsert.setDouble(2, volume);	//volume 90 day avg
				lg.trace("set volume");
				
				//psInsert.setString(3, arr[4]);						// sector /industry
				lg.trace("id: " + id);
				psInsert.setInt   (3, id);
				lg.trace("set id");
				
				u = psInsert.executeUpdate();
				lg.info("Updated: " + u);
				updated = updated + u;
			}
			else {
				//record does not exist, insert
				lg.info("No record exists for symbol "+symbol+", inserting..");
				psInsert = con.prepareStatement(sqlInsert);
				lg.trace("prepared stmt for insert");
				
				//ps.setInt(1, x);
				psInsert.setString(1, symbol);	//symbol
				lg.trace("set symbol");
				psInsert.setInt(2, 1);			//security type id 1=STK
				lg.trace("set secyrity type id");
				
				
				psInsert.setString(3, arr[3]);	//exchange
				lg.trace("set exchange");
				psInsert.setString(4, arr[2]);	//name
				lg.trace("set name");
				psInsert.setDouble(5, Double.parseDouble(arr[4]));	//~price
				lg.trace("set price");
				psInsert.setDouble(6, Double.parseDouble(arr[5]));	//volume 90 day avg
				lg.trace("set volume");
				//psInsert.setDouble(7, Double.parseDouble(arr[4]));	//sector
				
				u = psInsert.executeUpdate();
				lg.info("Inserted: " + u);
				inserted = inserted + u;
			}
		}
	}
	catch(Exception e){
		Utils.logError(lg, e);
	}
	finally{
		try {
			reader.close();
		}catch(Exception e){}
		
		try {
			psInsert.close();
		}catch(Exception e){}
		
		try {
			ps0.close();
		}catch(Exception e){}
		
		try {
			rs0.close();
		}catch(Exception e){}
		
		try {
			con.close();
		}catch(Exception e){}
	}
		
		lg.info("Completed. Total lines processed: " + totalLines + ", inserted: " + inserted + ", updated: "+ updated);
	}

}
