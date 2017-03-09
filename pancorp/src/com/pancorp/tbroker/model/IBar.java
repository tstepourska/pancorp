package com.pancorp.tbroker.model;

public interface IBar {

	public long time();
	public double high() ;
	public double low() ;
	public double open() ;
	public double close() ;
	public double wap() ;
	public long volume() ;
	public int count() ;
	public int recId();	//for requesting market data, equals id from tbl_contract + 100000
}