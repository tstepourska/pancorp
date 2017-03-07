package com.pancorp.tbroker.adapter;

import java.util.ArrayList;

import com.ib.controller.ApiController.IConnectionHandler;

public abstract class ConnectionHandlerAdapter implements IConnectionHandler {

	//@Override
/*	public void connected() {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountList(ArrayList<String> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void message(int id, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show(String string) {
		// TODO Auto-generated method stub
		
	}

}
