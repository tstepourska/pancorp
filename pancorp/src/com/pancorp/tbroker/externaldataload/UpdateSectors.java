package com.pancorp.tbroker.externaldataload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.data.DBConstants;
import com.pancorp.tbroker.util.Utils;

public class UpdateSectors {
	private static Logger lg = LogManager.getLogger(UpdateSectors.class);

	public static void main(String[] args) throws Exception {
		BufferedReader reader = null;
		File f = null;
		Connection con=null;
		PreparedStatement psInsert = null;
		String line;
		String[] arr = null;
		int id = -1;
		String symbol;
		int sector;
		//PreparedStatement ps0 = null;
		//ResultSet rs0 = null;
		int u = -1;
		try {
			// 0     1	    2                             3          	4            5               6
			//Rank,	 Symbol,Name,						  Exchange,		Sector 		 Price			  Volume 90 day avg
	        //1,	 BAC,	Bank of America Corporation,  NYSE,			4805,		 24.860000,		 101224814.911111,
	      
			//create directory
			f = new File("/Users/pankstep/run/TBroker/data/TD");
			String[] list = f.list();
			
			String sqlUpdate = "UPDATE tbl_contract SET sector_id=? WHERE symbol=?";
			Class.forName(DBConstants.db_driver);  
			con=DriverManager.getConnection(DBConstants.db_url,DBConstants.db_user,DBConstants.db_password);  
			
			for(String s : list){
				lg.info("File " + s);
				if(!s.startsWith("US_SSS_"))
					continue;
				
				try{
				reader = new BufferedReader(new FileReader("/Users/pankstep/run/TBroker/data/TD/" + s));
				
				while((line = reader.readLine())!=null){
					lg.debug("line: " + line);
					if(line.startsWith("Rank"))
						continue;
					arr = line.split(",");
				
					symbol = arr[1];
					sector = Integer.parseInt(arr[4]);
					
					psInsert = con.prepareStatement(sqlUpdate);
		
					psInsert.setInt(1, sector);						// sector /industry
					psInsert.setString(2, symbol);
						
					u = psInsert.executeUpdate();
					lg.info("Updated: " + u);				
				}
				
				reader.close();
				}
				catch(Exception e){
					lg.error("Error reading file " + s + ": " + e.getMessage());
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
			con.close();
		}catch(Exception e){}
		}
	}

}
