package com.pancorp.tbroker.main;

import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;

public class Shutdown {
	private static Logger lg = LogManager.getLogger(Shutdown.class);
	
	public static void overrideWorking(String fn) throws Exception {
		
		try (FileWriter w = new FileWriter(fn, false)){
			w.write(Constants.WORKING_STATUS_IDLE);
		}
		catch(Exception e){
			lg.error("Error writing to working: " + e.getMessage());
			throw e;
		}
	}

	public static void main(String[] args) {
		int status = 99;
		try {
			overrideWorking(Globals.BASEDIR + Constants.WORKING_FILE);
			status = 0;
		} 
		catch(Exception e){
			status = 1;
		}
		finally {
			System.exit(status);
		}
	}

}
