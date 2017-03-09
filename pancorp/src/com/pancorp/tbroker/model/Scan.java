package com.pancorp.tbroker.model;

public class Scan {
	int numRows = 100;
	int requestId;
	String instrument;
	String locationCode;
	String scanCode;
	String filter;
	
	public Scan(int reqId, String instr, String locCode, String sCode, String f) {
		this.requestId = reqId;
		this.instrument = instr;
		this.locationCode = locCode;
		this.scanCode = sCode;
		this.filter = f;
	}

	/**
	 * @return the requestId
	 */
	public int getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the instrument
	 */
	public String getInstrument() {
		return instrument;
	}

	/**
	 * @param instrument the instrument to set
	 */
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}

	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	/**
	 * @return the scanCode
	 */
	public String getScanCode() {
		return scanCode;
	}

	/**
	 * @param scanCode the scanCode to set
	 */
	public void setScanCode(String scanCode) {
		this.scanCode = scanCode;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the numRows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * @param numRows the numRows to set
	 */
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("\nrequestId: " + this.requestId);
		sb.append("\nfilter: " + this.filter);
		sb.append("\ninstrument: " + this.instrument);
		sb.append("\nlocationCode: " + this.locationCode);
		sb.append("\nnumRows: " + this.numRows);
		sb.append("\nscanCode: " + this.scanCode);
		
		return sb.toString();
		
	}

}
