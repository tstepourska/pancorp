package com.pancorp.tbroker.model;

public class DataTick {

	private double close;
	private double halted;
	private int tickerId;
	
	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 * @return the halted
	 */
	public double getHalted() {
		return halted;
	}

	/**
	 * @param halted the halted to set
	 * //Halted		Value	Description
//-1	Halted status not available. Usually returned with frozen data.
//0	Not halted. This value will only be returned if the contract is in a TWS watchlist.
//1	General halt. Trading halt is imposed for purely regulatory reasons with/without volatility halt.
//2	Volatility halt. Trading halt is imposed by the exchange to protect against extreme volatility.
	 */
	public void setHalted(double halted) {
		this.halted = halted;
	}

	/**
	 * @return the tickerId
	 */
	public int getTickerId() {
		return tickerId;
	}

	/**
	 * @param tickerId the tickerId to set
	 */
	public void setTickerId(int tickerId) {
		this.tickerId = tickerId;
	}

	public DataTick(int id) {
		this.tickerId = id;
	}

	
}
