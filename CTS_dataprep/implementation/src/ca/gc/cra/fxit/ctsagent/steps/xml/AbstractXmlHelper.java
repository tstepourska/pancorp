/**
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.steps.xml;

import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.util.Utils;
import ca.gc.cra.fxit.ctsagent.util.Constants;
import ca.gc.cra.fxit.ctsagent.model.PackageInfo;

/**
 *
 * 
 * @author Txs285
 */
public abstract class AbstractXmlHelper implements IHelper
{

	private static final Logger log = Logger.getLogger(AbstractXmlHelper.class);
	        	
	        	/**
	        	 * Transform flat file to International XML format.
	        	 * This method reads TXT from the inputStream and writes XML document the outputStream. 
	        	 * The input is read and processed in chunks in order to be able to handle large flat files.
	        	 * 
	        	 * @return
	        	 */
	    private int transform(){
	    	int status = Constants.STATUS_CODE_INCOMPLETE;
	    	
	    	return status;
	    }	        

	   /**
	    *
	    */
	   public void validate(PackageInfo p){
			
			try {
				
			}/* catch (SAXException e) {
			  Utils.logError(log, " is NOT valid. Reason: " + e.getLocalizedMessage());
			}
			catch (IOException e) {
				Utils.logError(log, e);
			}*/
			catch (Exception e) {
				Utils.logError(log, e);
			}
	   }
}
