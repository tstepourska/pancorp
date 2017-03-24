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

public class LoadStocksEnergyMineralsTD {
	private static Logger lg = LogManager.getLogger(LoadStocksEnergyMineralsTD.class);

	public static void main(String[] args) throws Exception {
		BufferedReader reader = null;
		Connection con=null;
		PreparedStatement psInsert = null;
		PreparedStatement ps0 = null;
		ResultSet rs0 = null;
		int u = -1;
		try {
			// 0     1	    2                             3          	4            5               6
			//Rank,	 Symbol,Name,						  Exchange,		Sector 		 Price			  Volume 90 day avg
	        //1,	 BAC,	Bank of America Corporation,  NYSE,			4805,		 24.860000,		 101224814.911111,
	      
			String sql0 = "SELECT id FROM tbl_contract WHERE symbol=?";
			String sqlInsert = "INSERT INTO tbl_contract (symbol,sec_type,primary_exchange, c_name, last_close,volume_90_day_avg,sector) VALUES (?,?,(SELECT id FROM tbl_exchange WHERE symbol=?),?,?,?,?)";
			String sqlUpdate = "UPDATE tbl_contract SET last_close=?, volume_90_day_avg=?, sector_id=? WHERE symbol=?";
			Class.forName(DBConstants.db_driver);  
			con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			ps0 = con.prepareStatement(sql0);		
			
			//LoadStockListTD loader = new LoadStockListTD();
			reader = new BufferedReader(new FileReader("/Users/pankstep/run/TBroker/data/US_SSS_Energy_Minerals.csv"));
			String line;
			String[] arr = null;
			int id = -1;
			String symbol;
			
			while((line = reader.readLine())!=null){
				lg.debug("line: " + line);
				arr = line.split(",");
			
				symbol = arr[1];
				ps0.setString(1, symbol);
				rs0 = ps0.executeQuery();
				
				if(rs0.next()){
					//record exists, update
					id=rs0.getInt("ID");
					
					psInsert = con.prepareStatement(sqlUpdate);
					
					psInsert.setDouble(1, Double.parseDouble(arr[5]));	//~price
					psInsert.setDouble(2, Double.parseDouble(arr[6]));	//volume 90 day avg
					psInsert.setString(3, arr[4]);						// sector /industry
					psInsert.setInt   (4, id);
					
					u = psInsert.executeUpdate();
					lg.info("Updated: " + u);
				}
				else {
					//record does not exist, insert
					psInsert = con.prepareStatement(sqlInsert);
					//ps.setInt(1, x);
					psInsert.setString(1, symbol);	//symbol
					psInsert.setInt(2, 1);			//security type id 1=STK
					psInsert.setString(3, arr[3]);	//exchange
					psInsert.setString(4, arr[2]);	//name
					psInsert.setDouble(5, Double.parseDouble(arr[5]));	//~price
					psInsert.setDouble(6, Double.parseDouble(arr[6]));	//volume 90 day avg
					psInsert.setDouble(7, Double.parseDouble(arr[4]));	//sector
					
					u = psInsert.executeUpdate();
					lg.info("Inserted: " + u);
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
	}

}
