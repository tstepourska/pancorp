package other;

//RealTimeData.java    
//API Version 9.72.14 (BETA)
//Version 1.2
//20160421
//R. Holowczak

//Import Java utilities and Interactive Brokers API
import java.util.Vector;
import java.util.Set;   // Needed for IB API 9.72.14
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TagValue;
import com.ib.client.CommissionReport;
//import com.ib.client.UnderComp;   // Comment out for API version 9.72
//Add the following for API version 9.72
import com.ib.client.DeltaNeutralContract;  // Add for API version 9.72
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EWrapperMsgGenerator;


//RealTimeData Class is an implementation of the 
//IB API EWrapper class
public class RealTimeData //implements EWrapper
{
// Add for API 9.72
    private EJavaSignal m_signal = new EJavaSignal();
    private EReader m_reader;

//Keep track of the next ID
private int nextOrderID = 0;
//The IB API Client Socket object
private EClientSocket client = null;
//Keep track of prices for Moving Average
private double priceTotal;
private int numberOfPrices;

public RealTimeData ()
{
// Initialize to 0
priceTotal = 0.0;
numberOfPrices = 0;
// Create a new EClientSocket object version 9.71
    // was commented: client = new EClientSocket (this);
          //  client = new EClientSocket( this, m_signal);
// Connect to the TWS or IB Gateway application
// Leave null for localhost
// Port Number (should match TWS/IB Gateway configuration
client.eConnect (null, 7496, 0);

// Pause here for connection to complete
try 
{
// Thread.sleep (1000);
while (! (client.isConnected()));
// Can also try: while (client.NextOrderId <= 0);
} catch (Exception e) 
{
};

// API Version 9.72 Launch EReader Thread
m_reader = new EReader(client, m_signal);
m_reader.start();
new Thread() {
public void run() {
processMessages();
}
}.start();

// Create a new contract
Contract contract = new Contract ();
contract.symbol("ES");
//was commented: contract.expiry("20160318");
//contract.lastTradeDateOrContractMonth("20160318");
//contract.exchange("GLOBEX");
//contract.secType("FUT");
//contract.currency("USD");
// Create a TagValue list
Vector<TagValue> mktDataOptions = new Vector<TagValue>();
// Make a call to start off data retrieval
client.reqMktData(0, contract, null, false, mktDataOptions);
// At this point our call is done and any market data events
// will be returned via the tickPrice method

} // end RealTimeData

private void processMessages() 
{
        while(true)
        {
            try {
                  m_reader.processMsgs();
            } catch (Exception e) {
                error(e);
            }
        } // end while
} // end processMessages()


// New for API version 9.72.14
public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass,
                     String multiplier, Set expirations, Set strikes) {
             // TODO Auto-generated method stub
 }
// New for API version 9.72.14
 public void securityDefinitionOptionalParameterEnd(int reqId) {
             // TODO Auto-generated method stub
 }
// New for API version 9.72.14
 public void accountUpdateMulti( int reqId, String account, String modelCode, String key, String value, String currency) {
             // TODO Auto-generated method stub
 }
// New for API version 9.72.14
 public void accountUpdateMultiEnd( int reqId) {
             // TODO Auto-generated method stub
 }
// New for API version 9.72.14
  public void positionMulti( int reqId, String account, String modelCode, Contract contract, double pos, double avgCost) {
             // TODO Auto-generated method stub
  }
// New for API version 9.72.14
  public void positionMultiEnd( int reqId) {
             // TODO Auto-generated method stub
  }


public void bondContractDetails(int reqId, ContractDetails contractDetails)
{
}

public void contractDetails(int reqId, ContractDetails contractDetails)
{
}

public void contractDetailsEnd(int reqId)
{
}

public void fundamentalData(int reqId, String data)
{
}

public void bondContractDetails(ContractDetails contractDetails)
{
}

public void contractDetails(ContractDetails contractDetails)
{
}

public void currentTime(long time)
{
}

public void displayGroupList(int requestId, String contraftInfo)
{
}


public void displayGroupUpdated(int requestId, String contractInfo)
{
}

// Add for API version 9.72
public void verifyAndAuthCompleted(boolean isSuccessful, String errorText)
{
}

// Add for API version 9.72
public void verifyAndAuthMessageAPI(String apiData, String xyzChallange)
{
}

public void verifyCompleted(boolean completed, String contractInfo)
{
}
public void verifyMessageAPI(String message)
{
}

public void execDetails(int orderId, Contract contract, Execution execution)
{
}

public void execDetailsEnd(int reqId)
{
}

public void historicalData(int reqId, String date, double open,
        double high, double low, double close, int volume, int count,
        double WAP, boolean hasGaps)
{
}

public void managedAccounts(String accountsList)
{
}

public void commissionReport(CommissionReport cr)
{
}

// For API Version 9.72 pos is now a double
public void position(String account, Contract contract, double pos, double avgCost)
{
}

// Below is API version 9.71
public void position(String account, Contract contract, int pos, double avgCost)
{
}

public void positionEnd()
{
}

public void accountSummary(int reqId, String account, String tag, String value, String currency)
{
}

public void accountSummaryEnd(int reqId)
{
}

public void accountDownloadEnd(String accountName)
{
}

public void openOrder(int orderId, Contract contract, Order order,
        OrderState orderState)
{
}

public void openOrderEnd()
{
}


// For API Version 9.72
public void orderStatus(int orderId, String status, double filled,
double remaining, double avgFillPrice, int permId, int parentId,
double lastFillPrice, int clientId, String whyHeld)
{
}

// For API Version 9.71
public void orderStatus(int orderId, String status, int filled,
        int remaining, double avgFillPrice, int permId, int parentId,
        double lastFillPrice, int clientId, String whyHeld)
{
}

public void receiveFA(int faDataType, String xml)
{
}

public void scannerData(int reqId, int rank,
        ContractDetails contractDetails, String distance, String benchmark,
        String projection, String legsStr)
{
}

public void scannerDataEnd(int reqId)
{
}

public void scannerParameters(String xml)
{
}

public void tickEFP(int symbolId, int tickType, double basisPoints,
        String formattedBasisPoints, double impliedFuture, int holdDays,
        String futureExpiry, double dividendImpact, double dividendsToExpiry)
{
}

public void tickGeneric(int symbolId, int tickType, double value)
{
}

public void tickOptionComputation( int tickerId, int field, 
                  double impliedVol, double delta, double optPrice, 
                  double pvDividend, double gamma, double vega, 
                  double theta, double undPrice)
{
}

/*
//  public void deltaNeutralValidation(int reqId, UnderComp underComp) 
public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp)
{
}
*/

public void updateAccountTime(String timeStamp)
{
}

public void updateAccountValue(String key, String value, String currency,
        String accountName)
{
}

public void updateMktDepth(int symbolId, int position, int operation,
        int side, double price, int size)
{
}

public void updateMktDepthL2(int symbolId, int position,
        String marketMaker, int operation, int side, double price, int size)
{
}

public void updateNewsBulletin(int msgId, int msgType, String message,
        String origExchange)
{
}

// For API Version 9.72
public void updatePortfolio(Contract contract, double position,
double marketPrice, double marketValue, double averageCost,
double unrealizedPNL, double realizedPNL, String accountName)
{
}

// For API Version 9.71:
public void updatePortfolio(Contract contract, int position,
        double marketPrice, double marketValue, double averageCost,
        double unrealizedPNL, double realizedPNL, String accountName)
{
}

public void marketDataType(int reqId, int marketDataType)
{
}

public void tickSnapshotEnd(int tickerId)
{
}

public void connectionClosed()
{
}
// Add connectAck for API version 9.72
public void connectAck() 
{
}


public void realtimeBar (int reqId, long time, double open, double high,
        double low, double close, long volume, double wap, int count)
{
}


public void error(Exception e)
{
//Print out a stack trace for the exception
    e.printStackTrace ();
}

public void error(String str)
{
//Print out the error message
    System.err.println (str);
}

public void error(int id, int errorCode, String errorMsg)
{
//Overloaded error event (from IB) with their own error 
//codes and messages
    System.err.println ("error: " + id + "," + errorCode + "," + errorMsg);
}

public void nextValidId (int orderId)
{
//Return the next valid OrderID
    nextOrderID = orderId;
}


public void tickPrice(int orderId, int field, double price,
        int canAutoExecute)
{
double movingAverage = 0.0;
try 
{
// Print out the current price
// field will provide the price type:
// 1 = bid,  2 = ask, 4 = last
// 6 = high, 7 = low, 9 = close
if (field == 4) 
{
numberOfPrices++;
priceTotal += price;
movingAverage = priceTotal / numberOfPrices;
System.out.println("tickPrice: " + orderId + "," + field + "," + price + ", " + movingAverage);
}
} 
catch (Exception e)
    {
e.printStackTrace ();
    }


}

public void tickSize (int orderId, int field, int size)
{
         // field will provide the size type:
         // 0=bid size, 3=ask size, 5=last size, 8=volume
         //System.out.println("tickSize: " + orderId + "," + field + "," + size);
}

public void tickString (int orderId, int tickType, String value)
{
}


public static void main (String args[])
{
    try
    {
        // Create an instance
        // At this time a connection will be made
        // and the request for market data will happen
        RealTimeData myData = new RealTimeData();
    }
    catch (Exception e)
    {
        e.printStackTrace ();
    }
} // end main

} // end public class RealTimeData
