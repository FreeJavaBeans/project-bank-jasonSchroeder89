package com.revature.ui;

import com.revature.data.DataRequest;
import com.revature.enums.State;

public interface Display {
	
	public State prompt();
	
	public DataRequest requestData();
}
