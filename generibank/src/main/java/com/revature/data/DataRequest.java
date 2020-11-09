package com.revature.data;

import java.util.HashMap;

import com.revature.enums.State;

public class DataRequest {
	private HashMap<String, String> data;
	private boolean hasVals;
	
	public DataRequest() {
		data.put("", "");
		
		hasVals = false;
	}
	
	public void addVals(String data, String val) {
		this.data.put(data, val);
		
		this.hasVals = true;
	}
	
	public boolean hasVals() {
		return this.hasVals;
	}
}
