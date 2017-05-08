package com.pancorp.tbroker.test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ib.client.Contract;
import com.pancorp.tbroker.day.Broker;
import com.pancorp.tbroker.day.DataFactory;
import com.pancorp.tbroker.main.BrokerManager;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;
import com.pancorp.tbroker.model.Bar;
import com.pancorp.tbroker.model.Candle;
import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;

public class BrokerTest {
	private Broker t;
	private DataFactory dfac;
	private BrokerManagerEWrapperImpl wrapper;
	BrokerManager manager;
	int stockId = 7610;
	String symbol = "GDX";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		manager = new BrokerManager();
		wrapper = new BrokerManagerEWrapperImpl(manager);
		dfac = wrapper.getDataFactory();
		
		String timeframeUnit = Constants.TFU_MIN;
		int timeframeSize = 15;
		int cacheSize = Globals.NUMBER_OF_PERIODS;
		double basePrice = 22.67;
		HashMap<Integer,ArrayDeque<Bar>> map = new HashMap<>();
		ArrayDeque<Bar> cache = Utils.fillTickCache(cacheSize, timeframeUnit, timeframeSize, basePrice);
		dfac.barCache.put(new Integer(stockId), cache);
		
		Contract contract = createContract(symbol);

		t = new Broker(contract,stockId,dfac,Constants.TFU_MIN, wrapper, manager);
    	t.setName("Broker_"+contract.symbol());
	}

	@After
	public void tearDown() throws Exception {
		dfac.cleanUp();
	}

	//@Test
	@Ignore
	public void testBroker() {
		/*
		+------+--------+-------+-------------------+--------+
		| id   | symbol | price | volume_90_day_avg | active |
		+------+--------+-------+-------------------+--------+
		| 7610 | GDX    | 22.67 |   68825091.533333 |      1 |
		+------+--------+-------+-------------------+--------+
		*/
		try{
		Contract contract = createContract(symbol);

		t = new Broker(contract,stockId,dfac,Constants.TFU_MIN, wrapper, manager);
    	t.setName("Broker_"+contract.symbol());
		}
		catch(Exception e){
			fail("Could not start Broker: " + e.getMessage());
		}
	}

	@Test
	//@Ignore
	public void testRun() {
		try{
			System.out.print(dfac.printCache());		
			t.start();
		}
		catch(Exception e){
			fail("Error: " + e.getMessage());
		}
	}


	//@Test
	@Ignore
	public void testIsWorking() {
		fail("Not yet implemented");
	}

	//@Test
	@Ignore
	public void testSetWorking() {
		fail("Not yet implemented");
	}
	
	private Contract createContract(String sym){
		Contract contract = new Contract();
		contract.symbol(sym);
		contract.secType("STK");
		contract.currency("USD");
		contract.exchange("SMART");
		//Specify the Primary Exchange attribute to avoid contract ambiguity
		contract.primaryExch("ISLAND");
		
		return contract;
	}

}
