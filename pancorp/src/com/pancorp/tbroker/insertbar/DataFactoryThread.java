package com.pancorp.tbroker.insertbar;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;

import com.pancorp.tbroker.model.Bar;

public class DataFactoryThread extends Thread{
	private static Logger lg = LogManager.getLogger(DataFactoryThread.class);

	 	Connection con = null;
	 	PreparedStatement ps = null;
	 	String sql = "";
	 	String url = null;
	 	String user = null;
	 	String password = null;
	 
	 	public DataFactoryThread() throws Exception {
	 		con  = DriverManager.getConnection(url, user, password);
	 		ps = con.prepareStatement(sql);
	 	}
	 	
	 	public int insertTick(Bar t) throws Exception {
	 		int status = -1;
	 		
	 		ps.setInt(1, t.recId());
	 		ps.setDouble(2, t.open());
	 		ps.setDouble(3, t.close());
	 		ps.setDouble(4, t.high());
	 		ps.setDouble(5, t.low());
	 		ps.setLong(6, t.time());
	 		ps.setLong(7, t.volume());
	 		ps.setDouble(8, t.wap());
	 		ps.setInt(9, t.count());
	 	
	 		status = ps.executeUpdate();
	 		return status;
		}
	 
}
