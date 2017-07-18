/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package test.com.pancorp.tbroker.order;

import com.pancorp.tbroker.util.Constants;
import com.pancorp.tbroker.util.Globals;
import com.pancorp.tbroker.util.Utils;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.Types;
import com.pancorp.tbroker.data.DataFactory;
import com.pancorp.tbroker.order.CancelOrderT;
import com.pancorp.tbroker.event.OpenPositionEvent;
import com.pancorp.tbroker.main.BrokerManagerEWrapperImpl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import com.pancorp.tbroker.main.BrokerManager ;


public class CancelOrderTest {
	private static Logger lg = LogManager.getLogger(CancelOrderTest.class);
	private com.pancorp.tbroker.main.BrokerManager manager;
	private BrokerManagerEWrapperImpl wrapper;
	int orderId = 390; //389; //388;

    @Before
    public void setUp() {
    	try {
    	manager = new BrokerManager();
    	wrapper = new BrokerManagerEWrapperImpl(manager);	
    	//ope = new OpenPositionEvent(Constants.ACTION_BUY, limitPrice);
    	
    	wrapper.getClient().eConnect(Globals.host, Globals.port, Globals.paperClientId);//TWS		
		//m_client.eConnect("127.0.0.1", 4001, 0);  //IB Gateway
    	}
    	catch(Exception e){
    		lg.error("error initializing: " + e.getMessage());
    	}
    	
    	
    }

    @Test
    public void placeOrder() {
		try {
		//placeOrder(currentPattern.getAction(), lmtPrice, Constants.DEFAULT_QUANTITY);	
		CancelOrderT cancelOrderThread = new CancelOrderT(//wrapper.getCurrentOrderId(), 
				orderId, 					// contract
				wrapper,
				manager
				);
		cancelOrderThread.start();
		//lg.trace("Place order thread started");

		//List<Order> orderList = manager.getOpenOrderList();
		//for(Order o: orderList){
		//	lg.debug(o.toString());
		//}

		//wait for order to be filled - see callback to wrapper
		}
		catch(Exception e){
			Utils.logError(lg, e);
		}

    }
}
