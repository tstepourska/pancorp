package com.pancorp.tbroker.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.pancorp.tbroker.util.Utils;

public abstract class DBFactory {
	private static Logger lg = LogManager.getLogger(DBFactory.class);
	private MysqlDataSource dataSource;
	
	public DBFactory(){
		this.initConnectionPool();
	}
	
	public void initConnectionPool(){
		Properties props = new Properties();		
		InputStream in = null;
		try { //
			//in = getClass().getResourceAsStream("db.properties");
			in = this.getClass().getClassLoader().getResourceAsStream("com/pancorp/tbroker/data/db.properties");
			props.load(in);
			dataSource = new MysqlDataSource();
			dataSource.setURL		(props.getProperty("db_url"));
			dataSource.setUser		(props.getProperty("db_user"));
			dataSource.setPassword	(props.getProperty("db_password"));
		} catch (Exception e) {
			Utils.logError(lg, e);
		}
		finally {
			try {
				in.close();
			} catch(Exception e){}
		}
	}

	public MysqlDataSource getDataSource(){
		return this.dataSource;
	}
}
