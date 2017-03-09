package com.pancorp.tbroker.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
		PreparedStatement ps = null;
		try {
		String sql = "INSERT INTO tbl_contract (symbol,sec_type,primary_exchange, c_name) VALUES (?,?,(SELECT id FROM tbl_exchange WHERE symbol=?),?)";
		Class.forName(DBConstants.db_driver);  
		con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
		ps = con.prepareStatement(sql);
		LoadStockListTD loader = new LoadStockListTD();
		FileReader f = new FileReader("/Users/pankstep/run/TBroker/data/US_Stocks.csv");
		BufferedReader reader = new BufferedReader(f);
		String line;
		String[] arr = null;
		while((line = reader.readLine())!=null){
			lg.debug("line: " + line);
			arr = line.split(",");
			//ps.setInt(1, x);
			ps.setString(1, arr[1]);
			ps.setInt(2, 1);
			ps.setString(3, arr[3]);
			ps.setString(4, arr[2]);
			int u = ps.executeUpdate();
		}
	}
	catch(Exception e){
		Utils.logError(lg, e);
	}
	finally{
		try {
			ps.close();
		}catch(Exception e){
			
		}
		try {
			con.close();
		}catch(Exception e){
			
		}
	}
	}

}
